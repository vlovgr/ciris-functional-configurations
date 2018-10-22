package se.vlovgr.example.cowsays

import eu.timepit.refined.types.numeric.PosInt

sealed abstract class WordWrap

object WordWrap {
  case object NoMaxWidth extends WordWrap

  final case class MaxWidth(value: PosInt) extends WordWrap
}
