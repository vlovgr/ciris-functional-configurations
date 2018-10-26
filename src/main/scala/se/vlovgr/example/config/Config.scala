package se.vlovgr.example.config

import cats.effect.IO
import eu.timepit.refined.api.Refined
import eu.timepit.refined.pureconfig._
import eu.timepit.refined.string.IPv4
import eu.timepit.refined.types.net.UserPortNumber
import pureconfig.module.catseffect.loadConfigF

import scala.concurrent.duration._

final case class HttpConfig(
  host: String Refined IPv4,
  port: UserPortNumber,
  idleTimeout: FiniteDuration
)

final case class Config(
  http: HttpConfig
)

object Config {
  def load: IO[Config] =
    loadConfigF[IO, Config]("se.vlovgr.example")
}
