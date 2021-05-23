package com.lstrihic.server

import cats.effect._
import com.lstrihic.config.AppConfig
import com.lstrihic.server.db.AppDatabaseFactory
import com.lstrihic.server.web.AppServer
import org.http4s.server.Server

object MainBootstrap extends IOApp.Simple {

  def runApplication[F[_] : Async]: Resource[F, Server] = for {
    xa <- AppDatabaseFactory.createDB(AppConfig.getConfig)
    server <- AppServer.resource
  } yield server

  override def run: IO[Unit] = runApplication[IO].use(_ => IO.never).as(ExitCode.Success)
}
