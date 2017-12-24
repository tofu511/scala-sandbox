package example.skinnyorm

import scalikejdbc._
import skinny.orm._

case class User(id: Option[Long] = None, email: String, password: String)

object User extends SkinnyCRUDMapper[User] {

  override def tableName: String = "users"

  override def defaultAlias: Alias[User] = createAlias("u")

  override def extract(rs: WrappedResultSet, n: ResultName[User]): User =
    autoConstruct(rs, n)

  // 戻り値は、AUTO_INCREMENTによるID値
  def create(user: User)(implicit session: DBSession): Long =
    createWithAttributes(toNamedValues(user): _*)

  private def toNamedValues(record: User): Seq[(Symbol, Any)] = Seq(
    'email -> record.email,
    'password -> record.password
  )

  // 戻り値は、更新したレコード件数
  def update(user: User)(implicit session: DBSession): Int =
    updateById(user.id.get).withAttributes(toNamedValues(user): _*)

  // SkinnyORMではバッチインサートがサポートされていない
  // そのため、ScalikeJDBCの機能を利用する
  def createAll(userSeq: Seq[User])(implicit session: DBSession): Seq[Int] = {
    val c = column
    val batchParams = userSeq.map(e => Seq(e.email, e.password))
    withSQL {
      insertInto(User).namedValues(c.email -> sqls.?, c.password -> sqls.?)
    }.batch(batchParams: _*).apply()
  }
}
