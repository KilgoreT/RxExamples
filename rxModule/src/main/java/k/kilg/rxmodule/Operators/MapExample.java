package k.kilg.rxmodule.Operators;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

//@tagName: map
//@tagCategory: simple
public class MapExample {

    /**
     * map() изменяет каждое входящее событие
     * Function(входящее, исходящее), в теле функции алгоритм изменения.
     */

    public static Observable<String> emit() {
        return Observable
                .interval(500, TimeUnit.MILLISECONDS)
                .map(integer -> integer * 10)
                .map(String::valueOf);
    }
}
