package utils

import scala.reflect._

object ClassUtil {

  /**
   * Get all fields of Class.
   *
   * @param Class[T]
   * @return Array[String] of class fields
   */
  def getFields[T](implicit tag: ClassTag[T]): Seq[String] = {
    tag.runtimeClass.getDeclaredFields.map(_.getName)
  }

}