package com.lstrihic.server.web

import cats.effect.{Async, Resource}
import com.codahale.metrics.MetricRegistry
import com.lstrihic.config.AppConfig
import org.http4s.HttpRoutes
import org.http4s.metrics.dropwizard.{Dropwizard, metricsService}
import org.http4s.server.Server
import org.http4s.server.jetty.JettyBuilder
import org.http4s.server.middleware.Metrics

object AppServer {
  private def builder[F[_] : Async]: JettyBuilder[F] = {
    val metricsRegistry: MetricRegistry = new MetricRegistry
    val metrics: HttpRoutes[F] => HttpRoutes[F] = Metrics[F](Dropwizard(metricsRegistry, "server"))

    JettyBuilder[F]
      .bindHttp(AppConfig.getConfig.server.port)
      .mountService(metrics(WebService[F].routes), "/")
      .mountService(metricsService(metricsRegistry), "/metrics")
  }

  def resource[F[_] : Async]: Resource[F, Server] = builder[F].resource
}
