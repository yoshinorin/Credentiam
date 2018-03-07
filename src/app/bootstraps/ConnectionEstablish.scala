package app.bootstraps

import javax.inject.Singleton
import com.unboundid.ldap.sdk.{ LDAPConnection, LDAPException }
import app.utils.Logger
import app.utils.config.LDAPConfig

/**
 * Companion object.
 */
object ConnectionEstablish {

  val instance = new ConnectionEstablish

}

@Singleton
class ConnectionEstablish extends Logger {

  val established = LDAPConnectionEstablishe

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
