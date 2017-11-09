package app.utils.config

import com.typesafe.config.ConfigFactory

trait ConfigProvider {

  val configuration = ConfigFactory.load

}
