package com.lstrihic.config

import com.typesafe.config.{Config, ConfigFactory}

object AppConfig {
  private lazy val defaultConfig = apply("app.config")

  def apply(configName: String) = new AppConfig(configName)

  def getConfig: AppConfig = defaultConfig
}

class AppConfig(configFile: String) {
  private val rootConfig: Config = ConfigFactory.load(configFile)
  private lazy val appConfig = rootConfig.getConfig("app")

  object server {
    lazy val port: Int = appConfig.getInt("server.port")
  }

  object database {
    lazy val dbDriverClassname: String = appConfig.getString("database.db-driver-classname")
    lazy val dbUrl: String = appConfig.getString("database.db-url")
    lazy val dbUsername: String = appConfig.getString("database.db-username")
    lazy val dbPassword: String = appConfig.getString("database.db-password")
    lazy val poolName: String = appConfig.getString("database.pool-name")
    lazy val maxPoolSize: Int = appConfig.getInt("database.max-pool-size")
    lazy val maxLifeTime: Long = appConfig.getLong("database.max-life-time")
    lazy val idleTimeout: Long = appConfig.getLong("database.idle-timeout")
    lazy val minimumIdle: Int = appConfig.getInt("database.minimum")
    lazy val connectionTimeout: Long = appConfig.getLong("database.connection")
    lazy val leakConnectionThreshold: Long = appConfig.getLong("database.leak-connection-threshold")
  }
}
