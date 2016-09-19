import play.api.libs._
import json._

import shapeless.{ `::` => :#:, _ }

object SWries extends LabelledProductTypeClass[Writes] {

  def emptyProduct: Writes[HNil] = Writes(_ => Json.obj())

  def product[H, T <: HList](name: String, ch: Writes[H], ct: Writes[T]): Writes[:#:[H, T]] = Writes[H :#: T] {
    case head :#: tail =>
      (ch.writes(head), ct.writes(tail)) match {
        case (JsNull, t: JsObject) => t
        case (h: JsValue, t: JsObject) => Json.obj(name -> h) ++ t
        case _ => Json.obj()
      }
  }

  def project[F, G](instance: => Writes[G], to: (F) => G, from: (G) => F): Writes[F] = {
    Writes[F] { f => instance.writes(to(f)) }
  }

}

object SReads extends LabelledProductTypeClass[Reads] {

  def emptyProduct: Reads[HNil] = Reads(_ => JsSuccess(HNil))

  def product[H, T <: HList](name: String, ch: Reads[H], ct: Reads[T]): Reads[:#:[H, T]] = {
    case obj @ JsObject(fields) =>
      for {
        head <- ch.reads(obj \ name)
        tail <- ct.reads(obj - name)
      } yield head :: tail
    case _ => JsError("Json object should be required")
  }

  def project[F, G](instance: => Reads[G], to: (F) => G, from: (G) => F): Reads[F] = {
    Reads[F] { instance.map(from).reads }
  }

}

object SFormat extends LabelledProductTypeClass[Format] {

  def emptyProduct: Format[HNil] = Format(
    SReads.emptyProduct,
    SWries.emptyProduct
  )

  def product[H, T <: HList](name: String, ch: Format[H], ct: Format[T]): Format[:#:[H, T]] = Format(
    SReads.product[H, T](name, ch, ct),
    SWries.product[H, T](name, ch, ct)
  )


  def project[F, G](instance: => Format[G], to: (F) => G, from: (G) => F): Format[F] = Format(
    SReads.project(instance, to, from),
    SWries.project(instance, to, from)
  )

}
