package se.vlovgr.example

import cats.effect.{ExitCode, IO, IOApp}
import se.vlovgr.example.http.startHttpApi

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    startHttpApi[IO]
}
