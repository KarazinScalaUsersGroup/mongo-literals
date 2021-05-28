package group.scala.karazin.mongo4s

import io.circe.Json
import io.circe.JsonObject
import io.circe.Encoder
import io.circe.Codec
import io.circe.syntax._
import io.circe._
import cats.data.NonEmptyList

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

  final case class WriteConcern(w: Int, j: Boolean, wtimeout: Long) derives Codec.AsObject

  final case class Insert[V, Comment](insert: String,
                             documents: List[V],
                             ordered: Option[Boolean] = None,
                             writeConcern: Option[WriteConcern] = None,
                             bypassDocumentValidation: Option[Boolean] = None,
                             comment: Option[Comment] = None) derives Codec.AsObject

  object Update:

    final case class Update[Q, U, ArrayFilters, Hint](q: Q,
                                                      u: U,
                                                      upsert: Option[Boolean] = None,
                                                      multi: Option[Boolean] = None,
                                                      collation: Option[Collation] = None,
                                                      arrayFilters: Option[List[ArrayFilters]] = None,
                                                      hint: Option[Hint] = None) derives Codec.AsObject

  end Update

  final case class Update[Q, U, ArrayFilters, Hint, Comment](update: String,
                                                             updates: List[Update.Update[Q, U, ArrayFilters, Hint]],
                                                             ordered: Option[Boolean] = None,
                                                             writeConcern: Option[WriteConcern] = None,
                                                             bypassDocumentValidation: Option[Boolean] = None,
                                                             comment: Option[Comment] = None) derives Codec.AsObject

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

  final case class Delete[Q, Hint, Comment](delete: String,
                                   deletes: List[Delete.Delete[Q, Hint]],
                                   ordered: Option[Boolean] = None,
                                   writeConcern: Option[WriteConcern] = None,
                                   comment: Option[Comment] = None) derives Codec.AsObject

  final case class Find[Filter, Sort, Projection, Hint, Comment, Min, Max](find: String,
                                                  filter: Option[Filter] = None,
                                                  sort: Option[Sort] = None,
                                                  projection: Option[Projection] = None,
                                                  hint: Option[Hint] = None,
                                                  skip: Option[Int] = None,
                                                  limit: Option[Int] = None,
                                                  batchSize: Option[Int] = None,
                                                  singleBatch: Option[Boolean] = None,
                                                  comment: Option[Comment] = None,
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

  final case class FindAndModify[Query, Sort, Update, Fields](findAndModify: String,
                                                              query: Option[Query] = None,
                                                              sort: Option[Sort] = None,
                                                              remove: Option[Boolean] = None,
                                                              update: Option[Update] = None,
                                                              `new`: Option[Boolean] = None,
                                                              fields: Option[Fields] = None,
                                                              upsert: Option[Boolean] = None,
                                                              bypassDocumentValidation: Option[Boolean] = None,
                                                              writeConcern: Option[WriteConcern] = None,
                                                              collation: Option[Collation] = None,
                                                              comment: Option[String] = None) derives Codec.AsObject

  final case class Count[Query](count: String,
                                query: Query,
                                limit: Option[Int] = None,
                                skip: Option[Int] = None,
                                readConcern: Option[ReadConcern] = None,
                                collation: Option[Collation] = None,
                                comment: Option[String] = None) derives Codec.AsObject

  object Aggregate:

    final case class Cursor(batchSize: Option[Int] = None) derives Codec.AsObject

    type Pipeline =
      AddFields[_] | Bucket[_, _] | BucketAuto[_, _] | CollStats | Count | Facet[_] | GeoNear[_] |
      GraphLookup[_] | Group[_] | IndexStats | Limit | ListSessions | LookupEquality | LookupJoin[_, _] |
      Match[_] | Merge | Out | PlanCacheStats | Project[_] | Redact[_] | ReplaceRoot[_] | ReplaceWith[_] |
      Sort[_] | SortByCount[_] | UnionWith[_] | Unset | Unwind | Sample | Search[_] | Set[_] | Skip

    final case class AddFields[Document]($addFields: Document)

    object Bucket:
      final case class Command[GroupBy, Output](groupBy: GroupBy,
                                                boundaries: List[Int],
                                                default: Option[Int],
                                                output: List[Output])
    end Bucket
    final case class Bucket[GroupBy, Output]($bucket: Bucket.Command[GroupBy, Output])

    object BucketAuto:
      final case class Command[GroupBy, Output](groupBy: GroupBy,
                                                buckets: Int,
                                                output: List[Output],
                                                granularity: Option[String])
    end BucketAuto
    final case class BucketAuto[GroupBy, Output]($bucketAuto: BucketAuto.Command[GroupBy, Output])

    object CollStats:
      final case class LatencyStats(histograms: Boolean)

      // Should be rewritten with a builder
      final case class Command(latencyStats: Option[LatencyStats],
                               storageStats: Option[EmptyObject],
                               count: Option[EmptyObject],
                               queryExecStats: Option[EmptyObject])
    end CollStats
    final case class CollStats($collStats: CollStats.Command)

    final case class Count($count: String)

    final case class Facet[Document]($facet: Document)

    object GeoNear:
      type GeoJSON = Point | LineString | Polygon | MultiPoint | MultiLineString | MultiPolygon

      // Should be improved with builder
      final case class Point(`type`: String = "Point", coordinates: List[Double])
      final case class LineString(`type`: String = "LineString", coordinates: List[List[Double]])
      final case class Polygon(`type`: String = "Polygon", coordinates: List[List[List[Double]]])
      final case class MultiPoint(`type`: String = "MultiPoint", coordinates: List[List[Double]])
      final case class MultiLineString(`type`: String = "MultiLineString", coordinates: List[List[Double]])
      final case class MultiPolygon(`type`: String = "MultiPolygon", coordinates: List[List[List[List[Double]]]])

      final case class Command[Query](near: GeoJSON,
                                      distanceField: String,
                                      spherical: Option[Boolean],
                                      maxDistance: Option[Double],
                                      query: Option[Query],
                                      distanceMultiplier: Option[Double],
                                      includeLocs: Option[String],
                                      uniqueDocs: Option[Boolean],
                                      minDistance: Option[Double],
                                      key: Option[String])
    end GeoNear
    final case class GeoNear[Query]($geoNear: GeoNear.Command[Query])

    object GraphLookup:
      final case class Command[RestrictSearchWithMatch](from: String,
                                                        startWith: String,
                                                        connectFromField: String,
                                                        connectToField: String,
                                                        as: String,
                                                        maxDepth: Option[Int],
                                                        depthField: Option[String],
                                                        restrictSearchWithMatch: Option[RestrictSearchWithMatch])
    end GraphLookup
    final case class GraphLookup[RestrictSearchWithMatch]($graphLookup: GraphLookup.Command[RestrictSearchWithMatch])

    final case class Group[Group]($group: Group)

    final case class IndexStats($indexStats: EmptyObject)

    final case class Limit($limit: Int)

    object ListSessions:
      final case class User(user: String, db: String)

      final case class Command(users: Option[List[User]],
                               allUsers: Option[Boolean])
    end ListSessions
    final case class ListSessions($listSessions: ListSessions.Command)

    object Lookup:
      final case class EqualityMatchCommand(from: String,
                                            localField: String,
                                            foreignField: String,
                                            as: Option[String])

      final case class JoinCommand[Let, Pipeline](from: String,
                                                  let: Let,
                                                  pipeline: Pipeline,
                                                  as: String)
    end Lookup
    final case class LookupEquality($lookup: Lookup.EqualityMatchCommand)
    final case class LookupJoin[Let, Pipeline]($lookup: Lookup.JoinCommand[Let, Pipeline])

    final case class Match[Match]($match: Match)

    object Merge:
      final case class Command(into: String,
                               on: Option[String],
                               let: Option[String],
                               whenMatched: Option["replace" | "keepExisting" | "merge" | "fail" | "pipeline"],
                               whenNotMatched: Option["insert" | "discard" | "fail"])
    end Merge
    final case class Merge($merge: Merge.Command)

    object Out:
      type Command = Out | String
      final case class Out(db: String, coll: String)
    end Out
    final case class Out($out: Out.Command)

    final case class PlanCacheStats($planCacheStats: EmptyObject)

    final case class Project[Project]($project: Project)

    final case class Redact[Redact]($redact: Redact)

    object ReplaceRoot:
      final case class Command[NewRoot](newRoot: NewRoot)
    end ReplaceRoot
    final case class ReplaceRoot[NewRoot]($replaceRoot: ReplaceRoot.Command[NewRoot])

    final case class ReplaceWith[ReplaceWith]($replaceWith: ReplaceWith)

    object Sample:
      final case class Command(size: Int)
    end Sample
    final case class Sample($sample: Sample.Command)

    final case class Search[Search]($search: Search)

    final case class Set[Set]($set: Set)

    object Skip:
      type Command = Int
    end Skip
    final case class Skip($skip: Skip.Command)

    final case class Sort[Sort]($sort: Sort)

    final case class SortByCount[SortByCount]($sortByCount: SortByCount)

    object UnionWith:
      type Command[Pipeline] = UnionWithCommand[Pipeline] | String
      final case class UnionWithCommand[Pipeline](coll: String, pipeline: Pipeline)
    end UnionWith
    final case class UnionWith[Pipeline]($unionWith: UnionWith.Command[Pipeline])

    object Unset:
      type Command = List[String] | String
    end Unset
    final case class Unset($unset: Unset.Command)

    object Unwind:
      type Command = UnwindCommand | String

      final case class UnwindCommand(path: String,
                                     includeArrayIndex: Option[String],
                                     preserveNullAndEmptyArrays: Option[Boolean])
    end Unwind
    final case class Unwind($unwind: Unwind.Command)

  end Aggregate
  final case class Aggregate(aggregate: String,
                             pipeline: List[Aggregate.Pipeline],
                             explain: Option[Boolean] = None,
                             allowDiskUse: Option[Boolean] = None,
                             cursor: Option[Aggregate.Cursor],
                             maxTimeMS: Option[Int] = None,
                             bypassDocumentValidation: Option[Boolean] = None,
                             readConcern: Option[ReadConcern] = None,
                             collation: Option[Collation] = None,
                             comment: Option[String] = None,
                             writeConcern: Option[WriteConcern] = None) /*derives Codec.AsObject TODO: Should be fixed*/
