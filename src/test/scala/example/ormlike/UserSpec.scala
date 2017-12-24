package example.ormlike

import org.scalatest.{ fixture, BeforeAndAfterAll }
import scalikejdbc._
import scalikejdbc.config._
import scalikejdbc.scalatest._

class UserSpec extends fixture.FunSpec with AutoRollback with BeforeAndAfterAll {

  val expectedUserSeq = Seq(User(None, "test1@test.com", "test1"),
                            User(None, "test2@test.com", "test2"),
                            User(None, "test3@test.com", "test3"))

  override def fixture(implicit session: DBSession): Unit = {
    User.deleteAll() // データがある場合は事前にデータを消さないと一意制約違反になる
    User.createAll(expectedUserSeq)
  }


  // すべてのテストを実行する前に呼ばれるメソッド
  override protected def beforeAll(): Unit = DBs.setupAll()

  // すべてのテストが終了した後に呼ばれるメソッド
  override protected def afterAll(): Unit = DBs.closeAll()

  describe("User") {
    it("should get all entities") { implicit session =>
      val userSeq = User.selectAll()
      println(userSeq)
      assert(userSeq.map(v => v.copy(id = None)) == expectedUserSeq)
    }
  }

}
