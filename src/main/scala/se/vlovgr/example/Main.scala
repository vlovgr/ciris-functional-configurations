package se.vlovgr.example

import cats.effect.{ExitCode, IO, IOApp}
import se.vlovgr.example.config.Config
import se.vlovgr.example.http.startHttpApi
import cats.implicits._

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    Config.load.flatMap { config =>
      IO(println(config.show)) >>
        startHttpApi[IO](config)
    }
}
