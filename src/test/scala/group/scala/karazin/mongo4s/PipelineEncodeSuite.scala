package group.scala.karazin.mongo4s

import cats.implicits._
import io.circe._
import io.circe.syntax._
import org.scalacheck.Prop._
import org.scalacheck._

import group.scala.karazin.circe.literal.extras.macros
import group.scala.karazin.mongo4s.model._

class PiplineEncodeSuite extends munit.ScalaCheckSuite:
  
  test("inlined sort and limit pipeline parsing") {
    
    type Pipeline = Aggregate.Sort[1 | -1] | Aggregate.Limit
    
    extension (inline sc: StringContext)
      inline def sortAndLimit(inline args: Any*): Json =
        ${ macros.encode[Aggregate[Pipeline]]('sc, 'args) }

    lazy val result: Json =
      sortAndLimit"""
               {
                  "aggregate": "collection",
                  "pipeline": [
                      { "$$sort": -1 }, {"$$limit": 3}
                  ]
               }
            """
  }
