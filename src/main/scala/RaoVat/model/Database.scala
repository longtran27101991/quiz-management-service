package RaoVat.model

import java.text.SimpleDateFormat
import java.util.Calendar

import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Database {

  val collection = connect()


  def connect(): BSONCollection = {

    val driver = new MongoDriver
    val connection = driver.connection(List("10.3.9.67"))

    val db = connection("RaoVat")
    db.collection("RaoVat")
  }

  def findAllTickers(): Future[List[Post]] = {
    val today = Calendar.getInstance();
    //get data from 2 month ago
    today.add(Calendar.DATE, -60)

    // (2) create a date "formatter"
    val formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    // (3) create a new String using the date format we want
    val folderName = formatter.format(today.getTime());

    val a = Calendar.getInstance.getTime
    val epoch = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(folderName).getTime() / 1000
    val query = BSONDocument("updated_at" -> BSONDocument("$gt" -> epoch), "category_id" -> 3)
    // which results in a Future[List[BSONDocument]]
    Database.collection
      .find(query)
      .cursor[Post]
      .collect[List]()
  }

  def findTicker(id: Int) : Future[Option[Post]] = {
    val query = BSONDocument("_id" -> id)

    Database.collection
      .find(query).one[Post]
  }

}

