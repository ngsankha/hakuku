package hakuku.master

import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.StringTokenizer

class MasterRequest(socket: Socket) {
  
  var in = new BufferedReader(new InputStreamReader(socket getInputStream))
  var out = new PrintWriter(socket getOutputStream,true)
  var servantIn: BufferedReader = null
  var servantOut: PrintWriter = null
  var authed = false
  var reply = ""
  
  def loop {
    out println("HELLO")
    while(socket isConnected) {
      var st = new StringTokenizer(in readLine)
      var res: String = null
      val cmd = st nextToken()
      if(cmd == "LOGIN")
        out println(auth(st nextToken, st nextToken))
      else if(cmd == "ADD" && authed)
        out println(add(st nextToken, st nextToken))
      else if(cmd == "GET" && authed)
        out println(get(st nextToken))
      else
        out println("ERROR")
    }
  }
  
  def auth(username: String, password: String): String = {
    if(Constants.users.getProperty(username) == password) authed = true
    println("Login from "+username)
    if(authed) "OK" else "ERROR"
  }
  
  def add(key: String, value: String): String = {
    if(Memory.iptable.contains(key)) Memory.iptable -= key
    val s = Memory.memIp.get(Memory.memIp.lastKey)
    servantIn = new BufferedReader(new InputStreamReader(s getInputStream))
    servantOut = new PrintWriter(s getOutputStream,true)
    servantOut println("ADD " + key + " " + value)
    reply = servantIn readLine
    val mem = java.lang.Long.parseLong(reply.substring(reply.lastIndexOf(' ') + 1))
    Memory.memIp.remove(Memory.memIp.lastKey)
    Memory.ipMem += (s -> mem)
    Memory.memIp.put(mem, s)
    Memory.iptable += (key -> s)
    "OK"
  }
  
  def get(key: String): String = {
    if(!Memory.iptable.contains(key)) "ERROR"
    else {
      val s = Memory.iptable(key)
      servantIn = new BufferedReader(new InputStreamReader(s getInputStream))
      servantOut = new PrintWriter(s getOutputStream,true)
      servantOut println("GET " + key)
      servantIn readLine
    }
  }
}