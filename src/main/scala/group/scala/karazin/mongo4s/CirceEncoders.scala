package group.scala.karazin.mongo4s

import io.circe.syntax._
import io.circe._
import io.circe.{Encoder, Decoder}

object CirceEncoders:

  given intOrStringEncoder: Encoder[Int | String] = value =>
    value match
      case v: Int ⇒ v.toInt.asJson
      case v: String ⇒ v.toString.asJson

  given intOrStringDecoder: Decoder[Int | String] = new Decoder[Int | String] {
    def apply(c: HCursor): Decoder.Result[Int | String] = {
      c.focus match
        case Some(json) if json.isNumber && json.asNumber.get.toInt.isDefined ⇒ json.as[Int]
        case Some(json) if json.isString                                      ⇒ json.as[String]
        case _                                                                ⇒ c.as[Int]
    }
  }

end CirceEncoders
