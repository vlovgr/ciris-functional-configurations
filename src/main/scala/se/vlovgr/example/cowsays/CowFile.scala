package se.vlovgr.example.cowsays

import enumeratum.{Enum, EnumEntry}
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString

sealed abstract class CowFile(val name: NonEmptyString) extends EnumEntry

object CowFile extends Enum[CowFile] {
  case object BeavisZen extends CowFile("beavis.zen")
  case object Blowfish extends CowFile("blowfish")
  case object Bong extends CowFile("bong")
  case object BudFrogs extends CowFile("bud-frogs")
  case object Bunny extends CowFile("bunny")
  case object Cheese extends CowFile("cheese")
  case object Cower extends CowFile("cower")
  case object Daemon extends CowFile("daemon")
  case object Default extends CowFile("default")
  case object Dragon extends CowFile("dragon")
  case object DragonAndCow extends CowFile("dragon-and-cow")
  case object Elephant extends CowFile("elephant")
  case object ElephantInSnake extends CowFile("elephant-in-snake")
  case object Eyes extends CowFile("eyes")
  case object FlamingSheep extends CowFile("flaming-sheep")
  case object Ghostbusters extends CowFile("ghostbusters")
  case object HeadIn extends CowFile("head-in")
  case object Hellokitty extends CowFile("hellokitty")
  case object Kiss extends CowFile("kiss")
  case object Kitty extends CowFile("kitty")
  case object Koala extends CowFile("koala")
  case object Kosh extends CowFile("kosh")
  case object LukeKoala extends CowFile("luke-koala")
  case object Meow extends CowFile("meow")
  case object Milk extends CowFile("milk")
  case object Moofasa extends CowFile("moofasa")
  case object Moose extends CowFile("moose")
  case object Mutilated extends CowFile("mutilated")
  case object Ren extends CowFile("ren")
  case object Satanic extends CowFile("satanic")
  case object Sheep extends CowFile("sheep")
  case object Skeleton extends CowFile("skeleton")
  case object Small extends CowFile("small")
  case object Sodomized extends CowFile("sodomized")
  case object Stegosaurus extends CowFile("stegosaurus")
  case object Stimpy extends CowFile("stimpy")
  case object Supermilker extends CowFile("supermilker")
  case object Surgery extends CowFile("surgery")
  case object Telebears extends CowFile("telebears")
  case object ThreeEyes extends CowFile("three-eyes")
  case object Turkey extends CowFile("turkey")
  case object Turtle extends CowFile("turtle")
  case object Tux extends CowFile("tux")
  case object Udder extends CowFile("udder")
  case object Vader extends CowFile("vader")
  case object VaderKoala extends CowFile("vader-koala")
  case object Www extends CowFile("www")

  override val values: Vector[CowFile] =
    findValues.toVector
}
