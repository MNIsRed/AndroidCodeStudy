import android.annotation.SuppressLint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Test

class RxJavaFunctionTest {
    @SuppressLint("CheckResult")
    @Test
    fun testPlainObserve() {
        Observable.just("hello", "world").subscribe {
            println(it)
        }
    }

    /**
     * 通过trampoline立即执行，避免直接执行完。
     */
    @SuppressLint("CheckResult")
    @Test
    fun testNormalObserve() {
        Observable.create { emitter ->
            Thread.sleep(3000)
            emitter.onNext("hello")
            emitter.onNext("world")
            emitter.onComplete()
        }.subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .map {
                it + "123"
            }
            .subscribe({
                println(it)
            }, {
                println(it.message)
            }, {
                println("onComplete")
            })
    }
}