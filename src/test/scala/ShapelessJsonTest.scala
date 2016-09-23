import org.scalatest._
import play.api.libs.json._

class ShapelessJsonTest
  extends FlatSpec
  with Matchers {

  "SWrites" should "Json を Write できる" in new ShapelessJson {
    case class Foo(name: String, number: Int)

    object Foo {
      implicit val writes: Writes[Foo] = SWrites.deriveInstance
    }

    val json = Json.parse(""" {
      | "name": "foo",
      | "number": 12
      |}""".stripMargin)


    assert(Json.toJson(Foo("foo", 12)) === json)
  }

}
