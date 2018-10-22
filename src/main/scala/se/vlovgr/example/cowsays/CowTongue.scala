package se.vlovgr.example.cowsays

import eu.timepit.refined.types.string.NonEmptyString

final case class CowTongue(value: NonEmptyString) {
  def stringValue: String = value.value
}
