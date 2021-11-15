import com.example.data.Book
import com.example.repository.{BookRepositoryPostgre, BookRepository}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.meta.MTable


class MyPostgresTest extends FunSuite with BeforeAndAfter with ScalaFutures {

  implicit override val patienceConfig = PatienceConfig(timeout = Span(5, Seconds))

  implicit var db: Database = _
  var repository: BookRepository = _

  val id: Int = 101
  val bookForInsert = Book(id, "Harry Potter and the Prisoner of Azkaban", "J. K. Rowling")
  val bookForUpdate = Book(id, "Harry Potter and the Goblet of Fire", "J. K. Rowling")

  def insertBook: Int = repository.save(bookForInsert).futureValue
  def updateBook: Int = repository.update(bookForUpdate).futureValue

  before {
    db = Database.forConfig("db.localhost.postgre")
    repository = new BookRepositoryPostgre()
  }

  test("Creating the Schema works") {
    repository.prepareRepository()

    val tables = db.run(MTable.getTables).futureValue
    assert(tables.count(_.name.name.equalsIgnoreCase("books")) == 1)
  }

  test("Inserting a Book works") {
    val result = insertBook
    assert(result == 1)
  }

  test("Selecting the Book works after insert operation") {
    val result = repository.find(id).futureValue
    assert(result.head == bookForInsert)
  }

  test("Updating the Book works") {
    val result = updateBook
    assert(result == 1)
  }

  test("Selecting the Book works after update operation") {
    val result = repository.find(id).futureValue
    assert(result.head == bookForUpdate)
  }

  test("Deleting the Book works") {
    val result = repository.remove(id).futureValue
    assert(result == 1)
  }

  test("Selecting the Book works after delete operation") {
    val result = repository.find(id).futureValue
    assert(result.isEmpty)
  }

}
