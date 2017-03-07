import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class Test {


    @org.junit.Test
    fun test() {
        val obs1 = Observable.just("Hello")
        val obs2 = Observable.just("World")

        Observable.zip(obs1, obs2, BiFunction<String,String,String> { s1, s2 ->   return@BiFunction s1 + s2 })
                .map { it.plus("da") }
                .subscribe (
                        ::print,
                        ::print
                )
    }


}