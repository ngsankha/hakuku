package hakuku.master

import java.net.ServerSocket
import java.io.FileInputStream
import scala.concurrent.ops._

object Master {
  
  def main(args: Array[String]) {
    val server = new ServerSocket(Constants PORT)
    Constants.users.load(new FileInputStream("conf/users.conf"))
    println("Connecting to the servants...")
    Memory.loadIpMem
    println("Hakuku Master Server up and running...")
    while(true) {
      val socket = server accept
      var request = new MasterRequest(socket)
      spawn { request loop}
    }
  }
}