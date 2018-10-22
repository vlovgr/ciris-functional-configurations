package se.vlovgr.example

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets.UTF_8

import cats.effect.Sync
import cats.syntax.show._
import cats.instances.int._
import eu.timepit.refined.cats.refTypeShow
import eu.timepit.refined.types.string.NonEmptyString
import se.vlovgr.example.cowsays.CowEyes._
import se.vlovgr.example.cowsays.WordWrap.{MaxWidth, NoMaxWidth}

import scala.sys.process._

package object cowsays {
  def cowsay[F[_]](
    message: NonEmptyString,
    config: CowsayConfig => CowsayConfig
  )(implicit F: Sync[F]): F[String] = F.delay {
    val bytes = message.value.getBytes(UTF_8)
    val bais = new ByteArrayInputStream(bytes)
    (cowsayCommand(config(CowsayConfig.Default)) #< bais).!!
  }

  private[cowsays] def cowsayCommand(config: CowsayConfig): List[String] = {
    val wordWrapOption =
      config.wordWrap.toList.flatMap {
        case NoMaxWidth         => List("-n")
        case MaxWidth(maxWidth) => List("-W", maxWidth.show)
      }

    val tongueOption =
      config.tongue.toList.flatMap(tongue => List("-T", tongue.stringValue))

    val eyesOption =
      config.eyes.toList.flatMap {
        case Borg           => List("-b")
        case Dead           => List("-d")
        case Greedy         => List("-g")
        case Paranoid       => List("-p")
        case Stoned         => List("-s")
        case Tired          => List("-t")
        case Wired          => List("-w")
        case Youthful       => List("-y")
        case Custom(custom) => List("-e", custom.value)
      }

    val cowFileOption =
      config.cowFile.toList.flatMap(file => List("-f", file.name.value))

    val options =
      wordWrapOption ++
        tongueOption ++
        eyesOption ++
        cowFileOption

    "cowsay" :: options
  }
}
