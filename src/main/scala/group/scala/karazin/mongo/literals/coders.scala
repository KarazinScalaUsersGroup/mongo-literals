package group.scala.karazin.mongo.literals

import java.time.Instant

import cats.implicits._
import cats.{MonadError, Traverse}
import io.circe.syntax._
import io.circe._
import org.mongodb.scala.bson._

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

object coders:

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

  extension (document: Document)
    def toCirceJson[F[_]](using M: MonadError[F, Throwable]): F[Json] = {
      if(document == null)
        M.raiseError(
          ParsingFailure(
            s"Cannot convert $document to io.circe.Json",
            IllegalArgumentException(s"Cannot convert $document to io.circe.Json")
          )
        )
      else
        M.fromEither(bsonFolder(new BsonDocument(document.toList.map { case (key, value) => new BsonElement(key, value) }.asJava)))
    }

  extension (bson: BsonValue)
    def toCirceJson[F[_]](using M: MonadError[F, Throwable]): F[Json] =
      M.fromEither(bsonFolder(bson))

//    def toCirceJsonUnsafe: Json =
//      bsonFolder(bson).right.get

  extension (json: Json)
    def toBson[F[_]](using M: MonadError[F, Throwable]): F[BsonValue] =
      M.fromEither(json.foldWith(jsonFolder))

//    def toBsonUnsafe: BsonValue =
//      json.foldWith(jsonFolder).right.get

  extension [V: Encoder](value: V)
    def toBson[F[_]](using M: MonadError[F, Throwable]): F[BsonValue] =
      M.fromEither(value.asJson.foldWith(jsonFolder))

//    def toBsonUnsafe: BsonValue =
//      value.asJson.foldWith(jsonFolder).right.get

  private[this] def readerFailure(value: BsonValue): ParsingFailure =
    ParsingFailure(
      s"Cannot convert $value: ${value.getClass} to io.circe.Json",
      IllegalArgumentException(s"Cannot convert $value: ${value.getClass} to io.circe.Json")
    )

  private[this] def bsonFolder(bson: BsonValue): Either[Throwable, Json] = bson match {
    case v: BsonBoolean => Right(Json.fromBoolean(v.getValue))
    case v: BsonString  => Right(Json.fromString(v.getValue))
    case v: BsonDouble  => Json.fromDouble(v.getValue) match {
        case Some(json) => Right(json)
        case None =>       Left(readerFailure(bson))
      }
    case v: BsonInt64       => Right(Json.fromLong(v.getValue))
    case v: BsonInt32       => Right(Json.fromInt(v.getValue))
    case v: BsonDecimal128  => Right(Json.fromBigDecimal(v.decimal128Value().bigDecimalValue()))
    case v: BsonArray =>
      v.getValues.asScala.toVector.map(bsonFolder).sequence.map(Json.fromValues)
    case v: BsonDocument => v.entrySet().asScala.toVector.map { m =>
        bsonFolder(m.getValue).map(m.getKey -> _)
      }.sequence.map(Json.fromFields)
    case v: BsonDateTime            => Right(Json.fromString(Instant.ofEpochMilli(v.getValue).toString))
    case v: BsonTimestamp           => Right(Json.fromString(Instant.ofEpochMilli(v.getValue).toString))
    case _: BsonNull                => Right(Json.Null)
    case _: BsonUndefined           => Right(Json.Null)
    case v: BsonSymbol              => Right(Json.fromString(v.getSymbol))
    case v: BsonJavaScript          => Right(Json.fromString(v.getCode))
    case v: BsonJavaScriptWithScope => Right(Json.fromString(v.getCode))
    case _: BsonMaxKey              => Left(readerFailure(bson))
    case _: BsonMinKey              => Left(readerFailure(bson))
    case id: BsonObjectId           => Right(Json.fromString(id.getValue.toHexString))
    case v: BsonBinary              => Right(Json.fromFields(("type", Json.fromInt(v.getType())) :: ("data", Json.fromString(new String(v.getData(), "UTF-8"))) :: Nil))
    case v: BsonRegularExpression   => Right(Json.fromString(v.getPattern))
  }

  private[this] lazy val jsonFolder: Json.Folder[Either[Throwable, BsonValue]] =
    new Json.Folder[Either[Throwable, BsonValue]] { self =>
      final val onNull: Either[Throwable, BsonValue] = Right(BsonNull())
      final def onBoolean(value: Boolean): Either[Throwable, BsonValue] = Right(BsonBoolean(value))
      final def onNumber(value: JsonNumber): Either[Throwable, BsonValue] = {
        value.toLong match {
          case Some(n) => Right(BsonNumber(n))
          case None =>
            value.toBigDecimal match {
              case Some(n) => Try { BsonDecimal128(n) } match {
                case Success(dec)   => Right(dec)
                case Failure(error) => Left(error)
              }
              case None => Try { BsonDecimal128(value.toString) } match {
                case Success(dec)   => Right(dec)
                case Failure(error) => Left(error)
              }
            }
        }
      }

      final def onString(value: String): Either[Throwable, BsonValue] =
        Try { Instant.parse(value) } map { v =>
          Right(BsonDateTime(v.toEpochMilli))
        } getOrElse Right(BsonString(value))

      final def onArray(value: Vector[Json]): Either[Throwable, BsonValue] =
        Traverse[Vector].traverse(value) { json =>
          json.foldWith(self)
        }.map(BsonArray.fromIterable(_))

      final def onObject(value: JsonObject): Either[Throwable, BsonValue] =
        Traverse[Vector].traverse(value.toVector) {
          case (key, json) => json.foldWith(self).map(key -> _)
        }.map(BsonDocument(_))
    }
