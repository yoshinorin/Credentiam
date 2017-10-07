package utils

import scala.reflect.runtime.{ universe => runtimeUniverse }
import scala.reflect.runtime.universe._
import app.models.LDAPAttribute

object ClassUtil {

  /**
   * Get LDAPAttribute type fields name from class.
   *
   * @param Class[T]
   * @return Seq[String] of class fields
   */
  def getLDAPAttributeFields[T](implicit tag: TypeTag[T]): Seq[String] = {
    runtimeUniverse.typeOf[T].members.collect {
      case member: LDAPAttribute => member.toString
    }.toSeq
  }

}