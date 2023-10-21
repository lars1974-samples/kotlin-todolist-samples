import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main(args: Array<String>)   {

    val launch = GlobalScope.launch {
        val cms1 = async { SlowAPI.callMeSlow() }
        val cms2 = async { SlowAPI.callMeSlower() }


          while(cms1.isActive || cms2.isActive){
              if(cms1.isActive && cms2.isActive) println("0 finished" + Thread.currentThread().name)
              else println("1 finished " + Thread.currentThread().name)
              delay(300)
          }

        println("2 finished " + Thread.currentThread().name)
        println(cms1.getCompleted() + "  " + cms2.getCompleted())
    }

    println("done "+Thread.currentThread().name)
    runBlocking {     // but this expression blocks the main thread
        delay(3000L)  // ... while we delay for 2 seconds to keep JVM alive
    }

    measureTimeMillis {  }

}