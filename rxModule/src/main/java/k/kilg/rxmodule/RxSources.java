package k.kilg.rxmodule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RxSources {

    private static List<Integer> integerList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

    public static List<Integer> getIntegerList() {
        return integerList;
    }

}
