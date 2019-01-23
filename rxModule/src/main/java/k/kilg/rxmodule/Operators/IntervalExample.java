package k.kilg.rxmodule.Operators;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;


//@tagName: interval
//@tagCategory: simple
public class IntervalExample {

    /**
     * interval()
     *  возвращает Long от 0 до бесконечности
     *  с временным интервалом.
     */
    public static Observable<Long> emit() {
        return Observable
                .interval(500, TimeUnit.MILLISECONDS);
    }
}
