package se.vlovgr.example

import cats.data.Validated.{Invalid, Valid}
import cats.data.ValidatedNel
import cats.effect.{ConcurrentEffect, ExitCode}
import cats.implicits._
import eu.timepit.refined.api.{RefType, Validate}
import eu.timepit.refined.auto._
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.OptionalValidatingQueryParamDecoderMatcher
import org.http4s.server.blaze.BlazeServerBuilder
import se.vlovgr.example.config.Config
import se.vlovgr.example.cowsays.CowEyes.Wired
import se.vlovgr.example.cowsays.WordWrap.{MaxWidth, NoMaxWidth}
import se.vlovgr.example.cowsays._

package object http {
  def refTypeQueryParamDecoder[F[_, _], T, P](errorMessage: String)(
    implicit refType: RefType[F],
    decoder: QueryParamDecoder[T],
    validate: Validate[T, P],
  ): QueryParamDecoder[F[T, P]] =
    decoder
      .decode(_)
      .leftMap(_.map(failure => ParseFailure(errorMessage, failure.details)))
      .andThen(refType.refine[P](_).leftMap(ParseFailure(errorMessage, _)).toValidatedNel)

  object Message
      extends OptionalValidatingQueryParamDecoderMatcher[NonEmptyString]("message")(
        refTypeQueryParamDecoder(s"The value for parameter 'message' must be non-empty"))

  implicit val cowEyesDecoder: QueryParamDecoder[CowEyes] =
    QueryParamDecoder[NonEmptyString](
      refTypeQueryParamDecoder {
        val eyes = CowEyes.values.flatMap(_.name)
        s"The value for parameter 'eyes' must be non-empty or one of: ${eyes.mkString(", ")}"
      }
    ).map { value =>
      CowEyes.values
        .find(_.name.contains(value))
        .getOrElse(CowEyes.Custom(value))
    }

  object Eyes extends OptionalValidatingQueryParamDecoderMatcher[CowEyes]("eyes")

  implicit val cowTongueDecoder: QueryParamDecoder[CowTongue] =
    QueryParamDecoder[NonEmptyString](
      refTypeQueryParamDecoder(s"The value for parameter 'tongue' must be non-empty")
    ).map(CowTongue.apply)

  object Tongue extends OptionalValidatingQueryParamDecoderMatcher[CowTongue]("tongue")

  implicit val wordWrapDecoder: QueryParamDecoder[WordWrap] = {
    val decoder: QueryParamDecoder[PosInt] =
      refTypeQueryParamDecoder(
        "The value for parameter 'wrap' must be 'none' or a positive integer")

    param =>
      param.value match {
        case "none" => NoMaxWidth.valid
        case _      => decoder.decode(param).map(MaxWidth.apply)
      }
  }

  object Wrap extends OptionalValidatingQueryParamDecoderMatcher[WordWrap]("wrap")

  implicit val cowFileDecoder: QueryParamDecoder[CowFile] =
    param =>
      CowFile.values
        .collectFirst { case file if file.name.value === param.value => file.valid }
        .getOrElse {
          val files = CowFile.values.map(_.name)
          ParseFailure(
            s"The value for parameter 'file' must be one of: ${files.mkString(", ")}",
            param.value
          ).invalidNel
      }

  object File extends OptionalValidatingQueryParamDecoderMatcher[CowFile]("file")

  def withDefaults(config: CowsayConfig): CowsayConfig =
    config.withTongue("U").withEyes(Wired)

  def startHttpApi[F[_]](config: Config)(
    implicit F: ConcurrentEffect[F]
  ): F[ExitCode] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    BlazeServerBuilder[F]
      .bindHttp(config.http.port.value, config.http.host.value)
      .withIdleTimeout(config.http.idleTimeout.value)
      .withHttpApp {
        HttpRoutes
          .of[F] {
            case GET -> Root / "cowsay"
                  :? Message(messageNel)
                    +& Eyes(eyesNel)
                    +& Tongue(tongueNel)
                    +& File(cowFileNel)
                    +& Wrap(wordWrapNel) =>
              val parseFailures: List[ParseFailure] =
                List(messageNel, eyesNel, tongueNel, cowFileNel, wordWrapNel)
                  .flatMap {
                    case Some(Invalid(nel))    => nel.toList
                    case None | Some(Valid(_)) => Nil
                  }

              def set[A, L, B](a: Option[ValidatedNel[L, A]])(f: (B, A) => B): B => B =
                b => a.fold(b)(_.fold(_ => b, f(b, _)))

              messageNel
                .collect {
                  case Valid(message) if parseFailures.isEmpty =>
                    Ok(cowsay[F](
                      message,
                      config => {
                        List[CowsayConfig => CowsayConfig](
                          set(eyesNel)(_ withEyes _),
                          set(tongueNel)(_ withTongue _),
                          set(wordWrapNel)(_ withWordWrap _),
                          set(cowFileNel)(_ withCowFile _),
                        ).foldLeft(withDefaults(config))((c, f) => f(c))
                      }
                    ))
                }
                .getOrElse {
                  BadRequest {
                    val missingMessage =
                      if (messageNel.isEmpty)
                        "The value for parameter 'message' must be specified and must be non-empty" :: Nil
                      else Nil
                    val errors = missingMessage ::: parseFailures.map(_.sanitized)
                    s"Please fix the following errors in the request.\n\n${errors.map(e => s"- $e.").mkString("\n")}"
                  }
                }
          }
          .orNotFound
      }
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
