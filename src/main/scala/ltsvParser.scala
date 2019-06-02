package MyParser {

  import scala.io.Source
  import org.joda.time.DateTime
  import org.joda.time.DateTimeZone
  import org.joda.time.format.DateTimeFormat

  class ltsvParser {
    def parse(file_name: String): List[Log] = {
      val labeled_values = Source.fromFile(file_name).mkString.replace("\n", "\t").split("\t")
      val body = Map(labeled_values.map { lv =>
        (lv.split(":")(0), labeled_values.filter(_.split(":")(0) == lv.split(":")(0)).map(
          _.split(":")(1)).toList
        )
      }: _*)

      val logs = for (i <- 0 until labeled_values.length / 7) yield {
        val host = body("host")(i)
        val user: Option[String] = if (body("user")(i) == "-") None else Option(body("user")(i))
        val epoch = body("epoch")(i).toLong
        val req = body("req")(i)
        val status = body("status")(i).toInt
        val size = body("size")(i).toLong
        val referer: Option[String] = if (body("referer")(i) == "-") None else Option(body("referer")(i))
        new Log(host, user, epoch, req, status, size, referer)
      }

      return logs.toList
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
      return Map(logs.map { s => (s.user.getOrElse("guest"), logs.filter(_.user == s.user)) }: _*)
    }
  }

}
