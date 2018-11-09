package se.vlovgr.example.config

import ciris._
import ciris.enumeratum._
import ciris.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.string.IPv4
import eu.timepit.refined.types.net.UserPortNumber

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
  def load: Either[ConfigErrors, Config] =
    loadConfig(
      env[Option[String Refined IPv4]]("HTTP_HOST"),
      env[Option[UserPortNumber]]("HTTP_PORT"),
      env[Option[FiniteDuration]]("HTTP_IDLE_TIMEOUT")
    ) { (host, port, idleTimeout) =>
      Config(
        http = HttpConfig(
          host = host.getOrElse("0.0.0.0"),
          port = port.getOrElse(9000),
          idleTimeout = idleTimeout.getOrElse(20.seconds)
        )
      )
    }.result
}
