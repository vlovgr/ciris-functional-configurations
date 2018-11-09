package se.vlovgr.example.config

import enumeratum._
import cats.derived._
import cats.effect.IO
import cats.implicits._
import cats.Show
import ciris._
import ciris.cats._
import ciris.enumeratum._
import ciris.cats.effect._
import ciris.kubernetes._
import ciris.enumeratum._
import ciris.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.cats.refTypeShow
import eu.timepit.refined.string.IPv4
import eu.timepit.refined.types.net.UserPortNumber
import se.vlovgr.example.config.AppEnv.{Production, Testing}

import scala.concurrent.duration._

final case class HttpConfig(
  host: String Refined IPv4,
  port: UserPortNumber,
  idleTimeout: FiniteDuration,
  apiKey: Secret[ApiKey]
)

final case class Config(
  http: HttpConfig
)

sealed abstract class AppEnv extends EnumEntry

object AppEnv extends Enum[AppEnv] {
  case object Testing extends AppEnv
  case object Production extends AppEnv

  override def values: Vector[AppEnv] =
    findValues.toVector
}

object Config {
  implicit val showConfig: Show[Config] = {
    implicit val finiteDurationShow: Show[FiniteDuration] =
      Show.fromToString

    import auto.showPretty._
    semi.showPretty
  }

  def load: IO[Config] =
    for {
      apiClient <- defaultApiClient[IO]
      secret = secretInNamespace[IO]("secrets", apiClient)
      config <- loadConfig(
        envF[IO, AppEnv]("APP_ENV"),
        envF[IO, Secret[ApiKey]]("API_KEY").orElse(secret("api-key"))
      ) { (environment, apiKey) =>
        Config(
          http = HttpConfig(
            host = "0.0.0.0",
            port = 9000,
            idleTimeout = environment match {
              case Testing    => 10.seconds
              case Production => 20.seconds
            },
            apiKey = apiKey
          )
        )
      }.orRaiseThrowable
    } yield config
}
