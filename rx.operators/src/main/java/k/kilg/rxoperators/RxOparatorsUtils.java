package k.kilg.rxoperators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class RxOparatorsUtils {

    private static List<Integer> integerList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

    private static Predicate<Integer> oddPredicate = integer -> (integer & 1) > 0;

    public static Observable<Integer> getFilteredOdd() {
        return Observable
                .just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .filter(oddPredicate);
    }
}
