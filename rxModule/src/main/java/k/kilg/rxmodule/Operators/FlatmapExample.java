package k.kilg.rxmodule.Operators;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

//@tagName: flatmap
//@tagCategory: simple
public class FlatmapExample {

    /**
     * flatmap используется:
     * 1. результат преобразования map() должен иметь тип Observable.
     * 2. преобразование имеет вид один-ко-многим,
     * т.е. одно входящее порождает несколько подсобытий.
     */

    public static Observable<String> emit() {
        return Observable
                .interval(500, TimeUnit.MILLISECONDS)
                .flatMap(
                    (Function<Long, ObservableSource<String>>) integer -> {
                        if ((integer & 1) > 0) {
                            return Observable.just("№ " + integer + " is Odd!")
                                    .delay(integer, TimeUnit.SECONDS);
                        }
                        return Observable.just("№ " + integer + " not Odd");
                    }
                );
    }

}
