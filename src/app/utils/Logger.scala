package utils

import org.slf4j.{ Logger, LoggerFactory, Marker, MarkerFactory }

/**
 * Implement this to get a named logger in scope.
 */
trait Logger {

  val logger = LoggerFactory.getLogger(this.getClass)
  val securityMaker = MarkerFactory.getMarker("SECURITY")

}
