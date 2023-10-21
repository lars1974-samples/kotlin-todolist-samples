import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object SlowAPI {
    suspend fun callMeSlow(): String{
        println("call me slow " + Thread.currentThread().name)
        delay(1000)
        return "callMeSlow"
    }

    suspend fun callMeSlower(): String {

        println("call me slower " + Thread.currentThread().name)

        var j = 0L
        for(i in 0..100000000000L){
            j = i+1
        }
        return "callMeSlower ${j}"
        delay(1)
    }
}