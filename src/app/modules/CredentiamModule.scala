package modules

import com.google.inject.AbstractModule
import app.bootstraps.ConnectionEstablish

class BootStrapModule extends AbstractModule {

  def configure() {
    bind(classOf[ConnectionEstablish]).asEagerSingleton()
  }

}
