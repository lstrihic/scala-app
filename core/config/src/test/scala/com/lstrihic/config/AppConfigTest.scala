package com.lstrihic.config

import com.lstrihic.test.AppTest
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AppConfigTest extends AppTest {

  describe("AppConfig") {
    it("should load default configuration") {
      val config = AppConfig.getConfig
      config.server.port shouldBe 8080
      config.database.dbDriverClassname shouldBe "org.h2.Driver"
    }

    it("should load custom configuration") {
      val config = AppConfig("app.conf")
      config.server.port shouldBe 8888
      config.database.dbDriverClassname shouldBe "org.postgresql.Driver"
    }
  }
}
