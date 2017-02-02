import rx.Observable

class Test {


    @org.junit.Test
    fun test() {
        val obs1 = Observable.just("Hello")
        val obs2 = Observable.just("World")

        Observable.zip(obs1, obs2, { s1, s2 ->  return@zip s1 + s2 })
                .map { it.plus("da") }
                .subscribe ({s -> print(s)},{t -> print(t)})
    }


}