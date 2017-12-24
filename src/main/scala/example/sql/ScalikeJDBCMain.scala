package example.sql

import org.slf4j.LoggerFactory
import scalikejdbc._
import scalikejdbc.config._

object ScalikeJDBCMain extends App {

  // ロガーの取得
  val logger = LoggerFactory.getLogger("application")

  // 設定ファイル(application.conf)を元にScalikeJDBCの設定やコネクションプールを初期化
  DBs.setupAll()

  // 暗黙のパラメータとしてsession変数を定義。DBSession型であるAutoSessionによって初期化される。
  implicit val session = AutoSession

  // ScalikeJDBCのsql補間子を使って、全件削除する
  sql"delete from users".update().apply()

  Seq(("test1@test.com", "test1"), ("test2@test.com", "test2"), ("test3@test.com", "test3")) foreach {
    case (email, password) =>
      sql"insert into users (email, password) values ($email, $password)".update().apply()
  }

  val users: List[Map[String, Any]] = sql"select * from users".map(_.toMap()).list().apply()
  val user  = sql"select * from users where email = 'test1@test.com'".map(_.toMap()).single().apply()

  logger.info(users.toString())
  logger.info(user.toString)

  // コネクションプールを破棄
  DBs.closeAll()
}
