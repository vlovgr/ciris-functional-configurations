package se.vlovgr.example.config

import cats.effect.IO
import pureconfig.module.catseffect.loadConfigF

import scala.concurrent.duration._

final case class HttpConfig(
  host: String,
  port: Int,
  idleTimeout: FiniteDuration
)

final case class Config(
  http: HttpConfig
)

object Config {
  def load: IO[Config] =
    loadConfigF[IO, Config]("se.vlovgr.example")
}
