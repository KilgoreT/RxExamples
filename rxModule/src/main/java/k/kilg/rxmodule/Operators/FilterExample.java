package k.kilg.rxmodule.Operators;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;


//@tagName: filter
//@tagCategory: simple
public class FilterExample {

//    Пример предиката используемого внутри функций Rx
    private static Predicate<Long> oddPredicate = longIndex -> (longIndex & 1) > 0;

    /**
     * Пример фильтрации четных чисел.
     *  Предикат возвращает только нечетные
     */
    public static Observable<Long> emit() {
        return Observable
                .interval(500, TimeUnit.MILLISECONDS)
                .filter(oddPredicate);
    }
}
