package se.vlovgr.example

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import cats.effect.Sync

import scala.sys.process._

package object cowsays {
  def cowsay[F[_]](message: String, config: CowsayConfig => CowsayConfig)(
    implicit F: Sync[F]
  ): F[String] = F.delay {
    val bais = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8))
    (cowsayCommand(config(CowsayConfig.Default)) #< bais).!!
  }

  private[cowsays] def cowsayCommand(config: CowsayConfig): List[String] = {
    val wordWrapOption =
      config.wordWrap.toList.flatMap {
        case NoMaxWidth      => List("-n")
        case MaxWidth(value) => List("-W", value.toString)
      }

    val tongueOption =
      config.tongue.toList.flatMap(tongue => List("-T", tongue.value))

    val eyesOption =
      config.eyes.toList.flatMap {
        case Borg              => List("-b")
        case Dead              => List("-d")
        case Greedy            => List("-g")
        case Paranoid          => List("-p")
        case Stoned            => List("-s")
        case Tired             => List("-t")
        case Wired             => List("-w")
        case Youthful          => List("-y")
        case CustomEyes(value) => List("-e", value)
      }

    val cowFileOption =
      config.cowFile.toList
        .map {
          case BeavisZen       => "beavis.zen"
          case Blowfish        => "blowfish"
          case Bong            => "bong"
          case BudFrogs        => "bud-frogs"
          case Bunny           => "bunny"
          case Cheese          => "cheese"
          case Cower           => "cower"
          case Daemon          => "daemon"
          case Default         => "default"
          case Dragon          => "dragon"
          case DragonAndCow    => "dragon-and-cow"
          case Elephant        => "elephant"
          case ElephantInSnake => "elephant-in-snake"
          case Eyes            => "eyes"
          case FlamingSheep    => "flaming-sheep"
          case Ghostbusters    => "ghostbusters"
          case HeadIn          => "head-in"
          case Hellokitty      => "hellokitty"
          case Kiss            => "kiss"
          case Kitty           => "kitty"
          case Koala           => "koala"
          case Kosh            => "kosh"
          case LukeKoala       => "luke-koala"
          case Meow            => "meow"
          case Milk            => "milk"
          case Moofasa         => "moofasa"
          case Moose           => "moose"
          case Mutilated       => "mutilated"
          case Ren             => "ren"
          case Satanic         => "satanic"
          case Sheep           => "sheep"
          case Skeleton        => "skeleton"
          case Small           => "small"
          case Sodomized       => "sodomized"
          case Stegosaurus     => "stegosaurus"
          case Stimpy          => "stimpy"
          case Supermilker     => "supermilker"
          case Surgery         => "surgery"
          case Telebears       => "telebears"
          case ThreeEyes       => "three-eyes"
          case Turkey          => "turkey"
          case Turtle          => "turtle"
          case Tux             => "tux"
          case Udder           => "udder"
          case Vader           => "vader"
          case VaderKoala      => "vader-koala"
          case Www             => "www"
        }
        .flatMap(value => List("-f", value))

    val options =
      wordWrapOption ++
        tongueOption ++
        eyesOption ++
        cowFileOption

    "cowsay" :: options
  }
}
