package example.transaction

import org.slf4j.LoggerFactory
import scalikejdbc._
import scalikejdbc.config._

object ScalikeJDBCTransactionMain extends App {

  // ロガー
  val logger = LoggerFactory.getLogger("application")

  // 設定ファイルを元にScalikeJDBCの設定やコネクションプールを初期化
  DBs.setupAll()

  // トランザクション用DBSessionを利用する場合
  DB.localTx { implicit session: DBSession =>
    sql"delete from users".update().apply()

    Seq(("test1@test.com", "test1"), ("test2@test.com", "test2"), ("test3@test.com", "test3")) foreach {
      case (email, password) =>
        sql"insert into users (email, password) values ($email, $password)".update().apply()
    }
  }

  // 読み取り専用DBSessionを利用する場合
  val users: Seq[Map[String, Any]] = DB.readOnly { implicit session: DBSession =>
    sql"select * from users".map(_.toMap()).list().apply()
  }

  // 結果を出力
  logger.info(users.toString())

  // コネクションプールを破棄
  DBs.closeAll()
}
