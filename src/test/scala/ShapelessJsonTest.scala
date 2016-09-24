import org.scalatest._
import org.joda.time.DateTime
import play.api.libs.json._

class ShapelessJsonTest
  extends FlatSpec
  with Matchers {

  "SWrites" should "Json を Write できる" in new ShapelessJson {
    case class Foo(name: String, number: Int)

    object Foo {
      implicit val writes: Writes[Foo] = SWrites.deriveInstance
    }

    val json = Json.parse(
      """{
      | "name": "foo",
      | "number": 12
      |}""".stripMargin)

    assert(Json.toJson(Foo("foo", 12)) === json)
  }

  it should "入れ子になった Json を Write できる" in new ShapelessJson {
    case class Foo(name: String, number: Int, bar: Bar)
    case class Bar(name: String, number: Int, datetime: DateTime)

    object Foo {
      implicit val writes: Writes[Foo] = SWrites.deriveInstance
    }
    object Bar {
      implicit val writes: Writes[Bar] = SWrites.deriveInstance
    }

    val json = Json.parse(
      """{
        | "name": "foo",
        | "number": 22,
        | "bar": {
        |   "name": "bar",
        |   "number": 33,
        |   "datetime": 1451574000000
        |  }
        |}
      """.stripMargin)
    
    assert(Json.toJson(Foo("foo", 22, Bar("bar", 33, new DateTime(2016, 1, 1, 0, 0)))) === json)
  }

}
