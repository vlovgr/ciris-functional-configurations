package se.vlovgr.example.config

import com.typesafe.config.{ConfigFactory, Config => LightbendConfig}

import scala.concurrent.duration._

final case class Config(config: LightbendConfig) {
  object http {
    def host: String =
      config.getString("se.vlovgr.example.http.host")

    def port: Int =
      config.getInt("se.vlovgr.example.http.port")

    def idleTimeout: FiniteDuration =
      config.getInt("se.vlovgr.example.http.idle-timeout-seconds").seconds
  }
}

object Config {
  def load(): Config =
    Config(ConfigFactory.load())
}
