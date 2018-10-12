package se.vlovgr.example.cowsays

sealed trait WordWrap

case object NoMaxWidth extends WordWrap
final case class MaxWidth(value: Int) extends WordWrap
