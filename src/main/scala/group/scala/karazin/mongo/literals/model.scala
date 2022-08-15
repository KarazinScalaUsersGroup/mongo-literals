package group.scala.karazin.mongo.literals

import cats.data.NonEmptyList
import io.circe.syntax._
import io.circe._

import group.scala.karazin.mongo.literals.coders.{given, _}

object model:

  type EmptyObject = EmptyTuple
  given Encoder[EmptyTuple] = _ => JsonObject.empty.asJson

  object Collation:
    type CaseFirst = "upper" | "lower" | "off"
    type Alternate = "non-ignorable" | "shifted"
    type MaxVariable = "punct" | "space"
  end Collation
  given Encoder[Collation.CaseFirst] = value => value.toString.asJson
  given Decoder[Collation.CaseFirst] = new Decoder[Collation.CaseFirst] {
    def apply(c: HCursor): Decoder.Result[Collation.CaseFirst] =
      c.as[String] map {
        case result @ ("upper" | "lower" | "off") => result.asInstanceOf[Collation.CaseFirst]
      }
  }
  given Encoder[Collation.Alternate] = value => value.toString.asJson
  given Decoder[Collation.Alternate] = new Decoder[Collation.Alternate] {
    def apply(c: HCursor): Decoder.Result[Collation.Alternate] =
      c.as[String] map {
        case result @ ("non-ignorable" | "shifted") => result.asInstanceOf[Collation.Alternate]
      }
  }
  given Encoder[Collation.MaxVariable] = value => value.toString.asJson
  given Decoder[Collation.MaxVariable] = new Decoder[Collation.MaxVariable] {
    def apply(c: HCursor): Decoder.Result[Collation.MaxVariable] =
      c.as[String] map {
        case result @ ("punct" | "space") => result.asInstanceOf[Collation.MaxVariable]
      }
  }

  final case class Collation(locale: String,
                             caseLevel: Option[Boolean],
                             caseFirst: Option[Collation.CaseFirst],
                             strength: Option[Int],
                             numericOrdering: Option[Boolean],
                             alternate: Option[Collation.Alternate],
                             maxVariable: Option[Collation.MaxVariable],
                             backwards: Option[Boolean],
                             normalization: Option[Boolean]) derives Codec.AsObject

  object ReadConcern:
    type Level = "local" | "available" | "majority" | "linearizable" | "snapshot"
  end ReadConcern
  given Encoder[ReadConcern.Level] = value => value.toString.asJson
  given Decoder[ReadConcern.Level] = new Decoder[ReadConcern.Level] {
    def apply(c: HCursor): Decoder.Result[ReadConcern.Level] =
      c.as[String] map {
        case result @ ("local" | "available" | "majority" | "linearizable" | "snapshot") =>
          result.asInstanceOf[ReadConcern.Level]
      }
  }

  final case class ReadConcern(level: ReadConcern.Level) derives Codec.AsObject

  final case class WriteConcern(w: Int | String, j: Boolean, wtimeout: Long) derives Codec.AsObject

  final case class Insert[V](insert: String,
                             documents: List[V],
                             ordered: Option[Boolean] = None,
                             writeConcern: Option[WriteConcern] = None,
                             bypassDocumentValidation: Option[Boolean] = None,
                             comment: Option[Json] = None) derives Codec.AsObject

  object Update:

    final case class Update[Q, U, ArrayFilters, Hint](q: Q,
                                                      u: U,
                                                      upsert: Option[Boolean] = None,
                                                      multi: Option[Boolean] = None,
                                                      collation: Option[Collation] = None,
                                                      arrayFilters: Option[List[ArrayFilters]] = None,
                                                      hint: Option[Hint] = None) derives Codec.AsObject

  end Update

  final case class Update[Q, U, ArrayFilters, Hint](update: String,
                                                    updates: List[Update.Update[Q, U, ArrayFilters, Hint]],
                                                    ordered: Option[Boolean] = None,
                                                    writeConcern: Option[WriteConcern] = None,
                                                    bypassDocumentValidation: Option[Boolean] = None,
                                                    comment: Option[Json] = None) derives Codec.AsObject

  object Delete:

    type Limit = 0 | 1

    final case class Delete[Q, Hint](q: Q,
                                     limit: Limit,
                                     collation: Option[Collation] = None,
                                     hint: Option[Hint] = None) derives Codec.AsObject

  end Delete
  given Encoder[Delete.Limit] = value => value.toInt.asJson
  given Decoder[Delete.Limit] = new Decoder[Delete.Limit] {
    def apply(c: HCursor): Decoder.Result[Delete.Limit] =
      c.as[Int] map {
        case result @ (0 | 1) => result.asInstanceOf[Delete.Limit]
      }
  }

  final case class Delete[Q, Hint](delete: String,
                                   deletes: List[Delete.Delete[Q, Hint]],
                                   ordered: Option[Boolean] = None,
                                   writeConcern: Option[WriteConcern] = None,
                                   comment: Option[Json] = None) derives Codec.AsObject

  final case class Find[Filter, Sort, Projection, Hint, Min, Max](find: String,
                                                                  filter: Option[Filter] = None,
                                                                  sort: Option[Sort] = None,
                                                                  projection: Option[Projection] = None,
                                                                  hint: Option[Hint] = None,
                                                                  skip: Option[Int] = None,
                                                                  limit: Option[Int] = None,
                                                                  batchSize: Option[Int] = None,
                                                                  singleBatch: Option[Boolean] = None,
                                                                  comment: Option[Json] = None,
                                                                  maxTimeMS: Option[Int] = None,
                                                                  readConcern: Option[ReadConcern] = None,
                                                                  max: Option[Max] = None,
                                                                  min: Option[Min] = None,
                                                                  returnKey: Option[Boolean] = None,
                                                                  showRecordId: Option[Boolean] = None,
                                                                  tailable: Option[Boolean] = None,
                                                                  oplogReplay: Option[Boolean] = None,
                                                                  noCursorTimeout: Option[Boolean] = None,
                                                                  awaitData: Option[Boolean] = None,
                                                                  allowPartialResults: Option[Boolean] = None,
                                                                  collation: Option[Collation] = None,
                                                                  allowDiskUse: Option[Boolean] = None) derives Codec.AsObject

  final case class FindAndModify[Query, Sort, Update, Fields, ArrayFilters, Hint](findAndModify: String,
                                                                                  query: Option[Query] = None,
                                                                                  sort: Option[Sort] = None,
                                                                                  remove: Option[Boolean] = None,
                                                                                  update: Option[Update] = None,
                                                                                  `new`: Option[Boolean] = None,
                                                                                  fields: Option[Fields] = None,
                                                                                  upsert: Option[Boolean] = None,
                                                                                  bypassDocumentValidation: Option[Boolean] = None,
                                                                                  writeConcern: Option[WriteConcern] = None,
                                                                                  maxTimeMS: Option[Long] = None,
                                                                                  collation: Option[Collation] = None,
                                                                                  arrayFilters: Option[List[ArrayFilters]] = None,
                                                                                  hint: Option[Hint] = None,
                                                                                  comment: Option[Json] = None) derives Codec.AsObject

  final case class Count[Query](count: String,
                                query: Query,
                                limit: Option[Int] = None,
                                skip: Option[Int] = None,
                                readConcern: Option[ReadConcern] = None,
                                collation: Option[Collation] = None,
                                comment: Option[Json] = None) derives Codec.AsObject

  final case class Aggregate[Cursor, Hint, Let](aggregate: String,
                                                pipeline: Seq[JsonObject],
                                                cursor: Cursor,
                                                explain: Option[Boolean] = None,
                                                allowDiskUse: Option[Boolean] = None,
                                                maxTimeMS: Option[Long] = None,
                                                bypassDocumentValidation: Option[Boolean] = None,
                                                readConcern: Option[ReadConcern] = None,
                                                collation: Option[Collation] = None,
                                                hint: Option[Hint] = None,
                                                comment: Option[Json] = None,
                                                writeConcern: Option[WriteConcern] = None,
                                                let: Option[Let] = None) derives Codec.AsObject

  case class Gte[T]($gte: T) derives Codec.AsObject
  case class Lte[T]($lte: T) derives Codec.AsObject
  case class In[T]($in: List[T]) derives Codec.AsObject
  case class Set[T]($set: T) derives Codec.AsObject
  case class Ne[T]($ne: T) derives Codec.AsObject
  case class Nin[T]($nin: List[T]) derives Codec.AsObject
  case class Match[T]($match: T) derives Codec.AsObject
  case class Group[T]($group: T) derives Codec.AsObject
  case class Sort[T]($sort: T) derives Codec.AsObject
  case class Sum($sum: Int) derives Codec.AsObject
  case class Meta($meta: String) derives Codec.AsObject
  case class Unwind($unwind: String) derives Codec.AsObject
  case class Search($search: String) derives Codec.AsObject
