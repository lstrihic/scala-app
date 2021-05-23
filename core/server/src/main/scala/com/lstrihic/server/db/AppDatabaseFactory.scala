package com.lstrihic.server.db

import cats.effect.Resource
import cats.effect.kernel.Async
import com.lstrihic.config.AppConfig
import com.zaxxer.hikari.HikariDataSource
import doobie.{ExecutionContexts, Transactor}
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor

import javax.sql.DataSource

object AppDatabaseFactory {

  def createDB[F[_] : Async](config: AppConfig): Resource[F, Transactor[F]] = for {
    datasource <- crateDataSource(config)
    _ = System.setProperty("liquibase.hub.mode", "off")
    _ <- migrate(config, datasource)
    xa <- createDoobieTransactor(config, datasource)
  } yield xa

  private def createDoobieTransactor[F[_] : Async](config: AppConfig, dataSource: DataSource): Resource[F, Transactor[F]] = for {
    ec <- ExecutionContexts.fixedThreadPool[F](config.database.maxPoolSize)
  } yield Transactor.fromDataSource[F](dataSource, ec)

  private def migrate[F[_] : Async](config: AppConfig, dataSource: DataSource): Resource[F, Unit] = {
    val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection))
    val liquibase = new Liquibase(config.database.migration.changeSet, new ClassLoaderResourceAccessor(), database)
    Resource.eval(Async[F].delay(liquibase.update("")))
  }

  private def crateDataSource[F[_] : Async](config: AppConfig): Resource[F, HikariDataSource] = {
    val hikariDataSource = new HikariDataSource
    hikariDataSource.setJdbcUrl(config.database.dbUrl)
    hikariDataSource.setDriverClassName(config.database.dbDriverClassname)
    hikariDataSource.setUsername(config.database.dbUsername)
    hikariDataSource.setPassword(config.database.dbPassword)
    hikariDataSource.setMaximumPoolSize(config.database.maxPoolSize)
    hikariDataSource.setPoolName(config.database.poolName)
    Resource.make(Async[F].delay(hikariDataSource))(ds => Async[F].delay(ds.close()))
  }
}
