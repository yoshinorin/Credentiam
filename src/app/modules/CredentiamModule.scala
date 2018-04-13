package modules

import com.google.inject.AbstractModule
import app.bootstraps.ConnectionEstablish

class BootStrapModule extends AbstractModule {

  override def configure() {
    bind(classOf[ConnectionEstablish]).asEagerSingleton()
  }

}
