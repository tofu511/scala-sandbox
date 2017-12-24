package example.ormlike

import org.slf4j.LoggerFactory
import scalikejdbc._
import scalikejdbc.config._

case class User(id: Option[Long] = None, email: String, password: String)

object User extends SQLSyntaxSupport[User] {

  override def tableName: String = "users"

  private val u = User.syntax("u")
  private val c = column

  private def toUser(e: ResultName[User])(rs:  WrappedResultSet): User =
    new User(id = rs.get(e.id), email = rs.get(e.email), password = rs.get(e.password))

  def createAll(userSeq: Seq[User])(implicit session: DBSession): Seq[Int] = {
    val batchParams = userSeq.map(e => Seq(e.email, e.password))
    withSQL {
      insertInto(User).namedValues(c.email -> sqls.?, c.password -> sqls.?)
    }.batch(batchParams: _*).apply()
  }

  def deleteAll()(implicit session: DBSession): Int = {
    withSQL {
      delete.from(User)
    }.update().apply()
  }

  def selectAll()(implicit session: DBSession): Seq[User] = {
    withSQL {
      select.from(User as u)
    }.map(toUser(u.resultName)).list().apply()
  }

}

object ScalikeJDBCOrmLikeMain extends App {

  val logger = LoggerFactory.getLogger("application")

  DBs.setupAll()

  DB.localTx {implicit session: DBSession =>
    User.deleteAll()
    val userSeq = Seq(User(None, "test1@test.com", "test1"),
                      User(None, "test2@test.com", "test2"),
                      User(None, "test3@test.com", "test3"))

    User.createAll(userSeq)
  }

  val users = DB.readOnly {implicit session: DBSession =>
    User.selectAll()
  }

  logger.info(users.toString())

  DBs.closeAll()


}
