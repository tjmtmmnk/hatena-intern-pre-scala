package MyParser {

  import scala.io.Source
  import org.joda.time.DateTime
  import org.joda.time.DateTimeZone
  import org.joda.time.format.DateTimeFormat

  class ltsvParser {
    def parse(file_name: String): List[Log] = {
      val body_array = Source.fromFile(file_name).mkString.replace("\n", "\t").split("\t").map(_.split(":")(1))
      var logs = List[Log]()
      for (i <- 0 until body_array.length / 7) {
        val user: Option[String] = if (body_array(7 * i + 1) == "-") None else Option(body_array(7 * i + 1))
        val referer: Option[String] = if (body_array(7 * i + 6) == "-") None else Option(body_array(7 * i + 6))
        logs :+= new Log(body_array(7 * i), user, body_array(7 * i + 2).toLong,
          body_array(7 * i + 3), body_array(7 * i + 4).toInt, body_array(7 * i + 5).toLong, referer)
      }
      return logs
    }
  }

  class Log(val host: String, val user: Option[String], val epoch: Long, val req: String, val status: Int, val size: Long, val referer: Option[String]) {
    val req_list: Array[String] = req.split(" ")

    def method: String = {
      return req_list(0)
    }

    def path: String = {
      return req_list(1)
    }

    def protocol: String = {
      return req_list(2)
    }

    def uri: String = {
      val _uri = "http://" + host + "/" + path
      return _uri
    }

    def time: String = {
      val dt = new DateTime(epoch * 1000L, DateTimeZone.UTC)
      val fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
      return fmt.print(dt)
    }
  }

  class LogController(val logs: List[Log]) {
    def countError: Int = {
      return logs.count(log => 500 <= log.status && log.status < 600)
    }

    def groupByUser: Map[String, List[Log]] = {
      return Map(logs map { s => (s.user.getOrElse("guest"), logs.filter(_.user == s.user)) }: _*)
    }
  }

}
