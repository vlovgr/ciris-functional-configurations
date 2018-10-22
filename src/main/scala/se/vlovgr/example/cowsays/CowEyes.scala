package se.vlovgr.example.cowsays

import enumeratum.{Enum, EnumEntry}
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString

sealed abstract class CowEyes(val name: Option[NonEmptyString]) extends EnumEntry

object CowEyes extends Enum[CowEyes] {
  case object Borg extends CowEyes(Some("borg"))
  case object Dead extends CowEyes(Some("dead"))
  case object Greedy extends CowEyes(Some("greedy"))
  case object Paranoid extends CowEyes(Some("paranoid"))
  case object Stoned extends CowEyes(Some("stoned"))
  case object Tired extends CowEyes(Some("tired"))
  case object Wired extends CowEyes(Some("wired"))
  case object Youthful extends CowEyes(Some("youthful"))
  final case class Custom(value: NonEmptyString) extends CowEyes(None)

  override val values: Vector[CowEyes] =
    findValues.toVector
}
