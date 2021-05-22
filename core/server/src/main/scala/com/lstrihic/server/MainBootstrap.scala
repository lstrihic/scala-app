package com.lstrihic.server

import cats.effect.{ExitCode, IO, IOApp}

object MainBootstrap extends IOApp.Simple {
  override def run: IO[Unit] = AppServer.resource[IO].use(_ => IO.never).as(ExitCode.Success)
}
