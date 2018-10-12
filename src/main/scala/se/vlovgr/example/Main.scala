package se.vlovgr.example

import cats.effect.{ExitCode, IO, IOApp}
import se.vlovgr.example.cowsays._

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      first <- cowsay[IO]("Please: stop using configuration files!",
                          _.withEyes(Wired).withTongue("()").withMaxWidth(30))
      second <- cowsay[IO](first, _.withNoMaxWidth.withCowFile(Dragon))
      _ <- IO(println(second))
    } yield ExitCode.Success
}
