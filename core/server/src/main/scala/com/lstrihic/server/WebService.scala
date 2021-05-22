package com.lstrihic.server

import cats.effect._
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Allow
import org.http4s.server.Router

object WebService {
  def apply[F[_] : Async]: WebService[F] = new WebService[F]
}

class WebService[F[_] : Async] extends Http4sDsl[F] {
  def routes: HttpRoutes[F] = Router[F]("" -> rootRoutes)

  private def rootRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root => Ok("Home endpoint")
    case _ -> Root => MethodNotAllowed(Allow(GET))
    case req@GET -> Root / "ip" => Ok(
      Json.obj("ip" -> Json.fromString(req.remoteAddr.fold("unknown")(_.toString)))
    )
  }
}
