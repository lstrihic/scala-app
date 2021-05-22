package com.lstrihic.test


import grizzled.slf4j.Logging
import org.mockito.MockitoSugar
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

class AppTest extends AnyFunSpecLike
  with Matchers
  with BeforeAndAfterAll
  with BeforeAndAfterEach
  with MockitoSugar
  with Logging {}
