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
import eu.timepit.refined.types.string.NonEmptyString
import se.vlovgr.example.config.AppEnv.{Production, Testing}

import scala.concurrent.duration._

final case class HttpConfig(
  host: String Refined IPv4,
  port: UserPortNumber,
  idleTimeout: FiniteDuration,
  apiKey: Secret[ApiKey]
)

final case class DatabaseConfig(
  username: NonEmptyString,
  password: Secret[DatabasePassword]
)

final case class Config(
  http: HttpConfig,
  database: DatabaseConfig
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

  def httpConfigWith(
    environment: AppEnv,
    secret: SecretInNamespace[IO]
  ) = {
    val kubernetesApiKey =
      secret[Secret[ApiKey]]("api-key")

    loadConfig(
      environment match {
        case Testing =>
          envF[IO, Secret[ApiKey]]("API_KEY")
            .orElse(kubernetesApiKey)
        case Production =>
          kubernetesApiKey
      }
    ) { apiKey =>
      HttpConfig(
        host = "0.0.0.0",
        port = 9000,
        idleTimeout = environment match {
          case Testing    => 10.seconds
          case Production => 20.seconds
        },
        apiKey = apiKey
      )
    }
  }

  def databaseConfigWith(secret: SecretInNamespace[IO]) =
    loadConfig(
      secret[Secret[DatabasePassword]]("database-password")
    ) { password =>
      DatabaseConfig(
        username = "cowsay",
        password = password
      )
    }

  def configWith(secret: SecretInNamespace[IO]) =
    withValue(envF[IO, AppEnv]("APP_ENV")) { environment =>
      loadConfig(
        httpConfigWith(environment, secret),
        databaseConfigWith(secret)
      ) { (http, database) =>
        Config(
          http = http,
          database = database
        )
      }
    }

  def load: IO[Config] =
    for {
      apiClient <- defaultApiClient[IO]
      secret = secretInNamespace[IO]("secrets", apiClient)
      config <- configWith(secret).orRaiseThrowable
    } yield config
}
