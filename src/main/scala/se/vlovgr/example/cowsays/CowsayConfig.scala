package se.vlovgr.example.cowsays

import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import se.vlovgr.example.cowsays.WordWrap.{MaxWidth, NoMaxWidth}

final case class CowsayConfig(
  wordWrap: Option[WordWrap],
  tongue: Option[CowTongue],
  cowFile: Option[CowFile],
  eyes: Option[CowEyes],
) {
  def withMaxWidth(maxWidth: PosInt): CowsayConfig =
    withWordWrap(MaxWidth(maxWidth))

  def withNoMaxWidth: CowsayConfig =
    withWordWrap(NoMaxWidth)

  def withWordWrap(wordWrap: WordWrap): CowsayConfig =
    copy(wordWrap = Some(wordWrap))

  def withCowFile(cowFile: CowFile): CowsayConfig =
    copy(cowFile = Some(cowFile))

  def withTongue(tongue: NonEmptyString): CowsayConfig =
    withTongue(CowTongue(tongue))

  def withTongue(tongue: CowTongue): CowsayConfig =
    copy(tongue = Some(tongue))

  def withEyes(eyes: CowEyes): CowsayConfig =
    copy(eyes = Some(eyes))
}

object CowsayConfig {
  val Default: CowsayConfig =
    CowsayConfig(
      wordWrap = None,
      cowFile = None,
      tongue = None,
      eyes = None
    )
}
