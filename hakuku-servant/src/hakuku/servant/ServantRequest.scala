package hakuku.servant

import java.net.Socket
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.PrintWriter
import java.util.StringTokenizer

class ServantRequest(socket: Socket) {
  
  var in = new BufferedReader(new InputStreamReader(socket getInputStream))
  var out = new PrintWriter(socket getOutputStream,true)
  
  def loop {
    out println("HELLO")
    while(socket isConnected) {
      var st = new StringTokenizer(in readLine)
      var res: String = null
      val cmd = st nextToken()
      if(cmd == "ADD")
        out println(Memory.add(st nextToken, st nextToken))
      else if(cmd == "GET")
        out println(Memory.get(st nextToken))
      else if(cmd == "MEMQUERY")
        out println(Servant.r.freeMemory)
      else
        out println("ERROR")
    }
  }
}