package hakuku.servant

import java.net.ServerSocket
import scala.concurrent.ops._

object Servant {
  var r = Runtime getRuntime

  def main(args: Array[String]) {
    val server = new ServerSocket(Constants PORT)
    println("Hakuku Servant up and running...")
    while(true) {
      val socket = server accept
      var request = new ServantRequest(socket)
      spawn { request loop}
    }
  }
}