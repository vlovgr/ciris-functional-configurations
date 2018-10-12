package se.vlovgr.example.cowsays

sealed trait CowEyes

case object Borg extends CowEyes
case object Dead extends CowEyes
case object Greedy extends CowEyes
case object Paranoid extends CowEyes
case object Stoned extends CowEyes
case object Tired extends CowEyes
case object Wired extends CowEyes
case object Youthful extends CowEyes
final case class CustomEyes(value: String) extends CowEyes
