package k.kilg.rxoperators.Operators;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class SimpleOperators {

    /**
     * Пример предиката используемого внутри функций Rx
     */
    private static Predicate<Integer> oddPredicate = integer -> (integer & 1) > 0;

    /**
     * Пример фильтрации четных чисел.
     * Предикат возвращает только нечетные
     *
     * @return поток нечетных чисел.
     */
    public static Observable<Integer> filterExample() {
        return Observable
                .just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .filter(oddPredicate);
    }
}
