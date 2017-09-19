package utils

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Implement this to get a named logger in scope.
 */
trait Logger {

  val logger = LoggerFactory.getLogger(this.getClass)
  val securityMaker = MarkerFactory.getMarker("SECURITY")

}
