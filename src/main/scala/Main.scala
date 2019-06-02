import MyParser._

object Main {
  def main(args: Array[String]): Unit = {
    val ltsv_parser = new ltsvParser()
    val logs = ltsv_parser.parse("ltsv.log")
    val log_controller = new LogController(logs)

    println(log_controller.countError)
    for(elm <- log_controller.groupByUser("guest")) {
      println(elm.size)
    }
    println("---")
    for(elm <- log_controller.groupByUser("john")) {
      println(elm.size)
    }
    println("---")
    for(elm <- log_controller.groupByUser("frank")) {
      println(elm.size)
    }
      //    for(elm <- log_controller.groupByUser("frank")){
//      println(elm.uri)
//    }
  }
}