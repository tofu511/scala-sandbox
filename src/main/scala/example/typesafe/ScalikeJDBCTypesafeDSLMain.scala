package example.typesafe

import org.slf4j.LoggerFactory
import scalikejdbc._
import scalikejdbc.config._

case class User(id: Option[Long] = None, email: String, password: String)

object User extends SQLSyntaxSupport[User] {

  override def tableName: String = "users"

  def apply(e: ResultName[User])(rs: WrappedResultSet): User =
    new User(id = rs.get(e.id), email = rs.get(e.email), password = rs.get(e.password))
}

object ScalikeJDBCTypesafeDSLMain extends App {

  val logger = LoggerFactory.getLogger("application")

  DBs.setupAll()

  val u = User.syntax("u")

  DB.localTx { implicit session: DBSession =>
    withSQL {
      delete.from(User)
    }.update().apply()

    Seq(User(None, "test1@test.com", "test1"),
        User(None, "test2@test.com", "test2"),
        User(None, "test3@test.com", "test3")) foreach {
      case User(_, email, password) =>
        val c = User.column
        withSQL {
          insertInto(User).columns(c.email, c.password).values(email, password)
        }.update().apply()
    }
  }

  val users = DB.readOnly{ implicit session: DBSession =>
    withSQL {
      select.from(User as u)
    }.map(User(u.resultName)).list().apply()
  }

  logger.info(users.toString())

  DBs.closeAll()
}
