package group.scala.karazin.mongo.literals

import io.circe._
import io.circe.syntax._
import group.scala.karazin.circe.literal.extras._
import group.scala.karazin.mongo.literals.model.{Delete, Find, Insert, Update, WriteConcern}
import group.scala.karazin.mongo.literals.model.Delete.{Delete => DeleteObj}
import group.scala.karazin.mongo.literals.coders._

import scala.util._
import org.scalacheck._
import org.scalacheck.Prop._
import org.bson.BsonDocument

class BsonEncodeSuite extends munit.ScalaCheckSuite:

  property("encode Insert Command into Bson representation") {

    case class MyDocument(userName: String, age: Int, isActive: Boolean) derives Codec.AsObject

    extension (inline sc: StringContext)
      inline def insert(inline args: Any*): Json = {
        ${ macros.encode[Insert[MyDocument]]('sc, 'args) }
      }

    forAll { (name: String, age: Int, isActive: Boolean) =>

      val document = MyDocument(name, age, isActive)

      val insert: Json =
        insert"""{
                "insert": "collection",
                "documents": [$document]
              }
            """

      val bson = insert.toBson[Try]

      assert(bson.isSuccess)
      assert(bson.get.isDocument)
      assertEquals(bson.get.asDocument().toCirceJson[Try], Success(insert))
    }
  }

  property("encode Insert Command with WriteConcern into Bson representation") {

    case class MyDocument(userName: String, age: Int, isActive: Boolean) derives Codec.AsObject

    extension (inline sc: StringContext)
      inline def insert(inline args: Any*): Json = {
        ${ macros.encode[Insert[MyDocument]]('sc, 'args) }
      }

    forAll { (name: String, age: Int, isActive: Boolean) =>

      val document = MyDocument(name, age, isActive)

      val insert: Json =
        insert"""{
                "insert": "collection",
                "documents": [$document],
                "writeConcern": {
                    "w": 1,
                    "j": true,
                    "wtimeout": 1000
                  }
              }
            """

      val bson = insert.toBson[Try]

      assert(bson.isSuccess)
      assert(bson.get.isDocument)
      assertEquals(bson.get.asDocument().toCirceJson[Try], Success(insert))
    }
  }

  property("encode Insert Command into Bson representation via model") {

    case class MyDocument(userName: String, age: Int, isActive: Boolean) derives Codec.AsObject

    extension (inline sc: StringContext)
      inline def insert(inline args: Any*): Json = {
        ${ macros.encode[Insert[MyDocument]]('sc, 'args) }
      }

    forAll { (name: String, age: Int, isActive: Boolean) =>

      val document = MyDocument(name, age, isActive)

      val insert: Json =
        insert"""${Insert("collection", List(document))}"""

      val bson = insert.toBson[Try]

      assert(bson.isSuccess)
      assert(bson.get.isDocument)
      assertEquals(bson.get.asDocument().toCirceJson[Try], Success(insert))
    }
  }

  property("encode Update Command into Bson representation") {

    case class QueryByIdAndUserName(id: String, userName: Option[String]) derives Codec.AsObject

    extension (inline sc: StringContext)
      inline def updateByIdAndUserName(inline args: Any*): Json =
        ${ macros.encode[Update[QueryByIdAndUserName, Json, Json, Json]]('sc, 'args) }

    forAll { (id: String, userName: Option[String]) =>

      val document = QueryByIdAndUserName(id, userName)

      val update: Json =
        updateByIdAndUserName"""{
                 "update": "collection",
                 "updates": [{"q": $document, "u": {}}]
              }
            """

      val bson = update.toBson[Try]

      assert(bson.isSuccess)
      assert(bson.get.isDocument)
      assertEquals(bson.get.asDocument().toCirceJson[Try], Success(update))
    }
  }

  property("encode Delete Command into Bson representation") {

    case class QueryById(id: String) derives Codec.AsObject

    extension (inline sc: StringContext)
      inline def delete(inline args: Any*): Json =
        ${ macros.encode[Delete[QueryById, Json]]('sc, 'args) }

    forAll { (id: String) =>

      val document = QueryById(id)

      val delete: Json =
        delete"""{
                 "delete": "collection",
                 "deletes": [{"q": $document, "limit": 1}]
              }
            """

      val bson = delete.toBson[Try]

      assert(bson.isSuccess)
      assert(bson.get.isDocument)
      assertEquals(bson.get.asDocument().toCirceJson[Try], Success(delete))
    }
  }

  property("encode Find Command into Bson representation") {

    case class QueryByIdFilter(id: String) derives Codec.AsObject

    extension (inline sc: StringContext)
      inline def find(inline args: Any*): Json =
        ${ macros.encode[Find[QueryByIdFilter, Json, Json, Json, Json, Json]]('sc, 'args) }

    forAll { (id: String) =>

      val document = QueryByIdFilter(id)

      val find: Json =
        find"""{
                 "find": "collection",
                 "filter": $document
              }
            """

      val bson = find.toBson[Try]

      assert(bson.isSuccess)
      assert(bson.get.isDocument)
      assertEquals(bson.get.asDocument().toCirceJson[Try], Success(find))
    }
  }
