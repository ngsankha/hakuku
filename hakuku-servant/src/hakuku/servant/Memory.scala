package hakuku.servant

import scala.collection.mutable.HashMap
import java.util.TreeMap
import java.util.NoSuchElementException

object Memory {
  
  var mem = new HashMap[String, String]
  var keyTime = new HashMap[String, Long]
  var timeKey = new TreeMap[Long, String]
  
  def add(key: String, value: String): String = synchronized {
    if(Servant.r.freeMemory < Constants.MIN_MEM) {
      val key = timeKey.get(timeKey.lastKey)
      mem -= key
      keyTime -= key
      timeKey.remove(timeKey.lastKey())
      System gc
    }
    val time = System.currentTimeMillis
    mem += (key -> value)
    try {
      val oldTime = keyTime(key)
      timeKey.remove(oldTime)
    } catch {
      case _: NoSuchElementException => 
    }
    keyTime += (key -> time)
    timeKey.put(time, key)
    println("Added key " + key)
    "OK " + Servant.r.freeMemory()
  }
  
  def get(key: String): String = synchronized {
    var res: String = null
    try{
      res = "OK " + mem(key)
      val oldTime = keyTime(key)
      timeKey.remove(oldTime)
      val time = System.currentTimeMillis
      keyTime += (key -> time)
      timeKey.put(time, key)
    } catch {
      case _: NoSuchElementException => res = "ERROR"
    }
    res
  }
}