package se.vlovgr.example.cowsays

final case class CowsayConfig(
  wordWrap: Option[WordWrap],
  tongue: Option[CowTongue],
  cowFile: Option[CowFile],
  eyes: Option[CowEyes],
) {
  def withMaxWidth(maxWidth: Int): CowsayConfig =
    copy(wordWrap = Some(MaxWidth(maxWidth)))

  def withNoMaxWidth: CowsayConfig =
    copy(wordWrap = Some(NoMaxWidth))

  def withCowFile(cowFile: CowFile): CowsayConfig =
    copy(cowFile = Some(cowFile))

  def withTongue(tongue: String): CowsayConfig =
    copy(tongue = Some(CowTongue(tongue)))

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
