package hakuku.master

import scala.collection.mutable.HashMap
import java.util.TreeMap
import java.util.NoSuchElementException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.FileInputStream
import java.net.Socket
import java.io.PrintWriter

object Memory {
  
  var iptable = new HashMap[String, Socket]
  var ipMem = new HashMap[Socket, Long]
  var memIp = new TreeMap[Long, Socket]
  
  def loadIpMem {
    var reader = new BufferedReader(new InputStreamReader(new FileInputStream("conf/servants.conf")))
    var line = reader.readLine
    while(line != null) {
      if(!line.startsWith("#")) {
        val s = new Socket(line, Constants.SERVANT_PORT)
        val in = new BufferedReader(new InputStreamReader(s getInputStream))
        val out = new PrintWriter(s getOutputStream, true)
        if(in.readLine == "HELLO") {
          out println("MEMQUERY")
          val mem: Long = java.lang.Long parseLong(in readLine)
          ipMem += (s -> mem)
          memIp.put(mem, s)
        }
      }
      line = reader.readLine
    }
    println(ipMem)
  }
}