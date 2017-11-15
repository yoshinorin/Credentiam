package app.utils

import app.utils.ConvertException
import app.utils.types._

object Converter {

  implicit class stringConverter(s: String) {

    /**
     * Convert [[SearchRelations]] type from string.
     *
     * @param relation Name of relation.
     * @return [[SearchRelations]]
     */
    def toSearchRelation: SearchRelations = {
      s.toUpperCase match {
        case "CONTAINS" => SearchRelations.CONTAINS
        case "EXCLUDES" => SearchRelations.EXCLUDES
        case "STARTSWITH" => SearchRelations.STARTSWITH
        case "ENDSWITH" => SearchRelations.ENDSWITH
        case "EQUAL" => SearchRelations.EQUAL
        case "NOTEQUAL" => SearchRelations.NOTEQUAL
        case _ => throw new ConvertException(s"Can not convert ${s} to SearchRelations type.")
      }
    }
  }

}
