package modules

import javax.inject.Singleton
import com.google.inject.AbstractModule
import com.unboundid.ldap.sdk.{ LDAPConnection, LDAPException }
import app.utils.Logger
import app.utils.config.LDAPConfig

object BootStrapModule {

  val instance = new BootStrapModule

}

@Singleton
class BootStrapModule extends AbstractModule with Logger {

  val established = LDAPConnectionEstablishe

  def configure() {

  }

  private def LDAPConnectionEstablishe: Boolean = {

    val checkConnectionMessage = "Checking LDAP connection..."
    println("[INFO] " + checkConnectionMessage)
    logger.info(checkConnectionMessage)

    val failedConnectionEstablishedMessage = "FAILED to connect LDAP."
    val connectionEstablishedMessage = "LDAP connection established."

    try {
      val x = new LDAPConnection(LDAPConfig.host, LDAPConfig.port, LDAPConfig.bindDN, LDAPConfig.password)

      println("[INFO] " + connectionEstablishedMessage)
      logger.info(connectionEstablishedMessage)
      true
    } catch {
      case e: LDAPException => {
        println("[ERROR] " + failedConnectionEstablishedMessage)
        println("[ERROR] " + e.getMessage)
        logger.error(failedConnectionEstablishedMessage)
        logger.error(e.getMessage)
        false
      }
      case e: Exception => {
        println("[ERROR] " + e.getMessage)
        logger.error(e.getMessage)
        false
      }
    }

  }

}
