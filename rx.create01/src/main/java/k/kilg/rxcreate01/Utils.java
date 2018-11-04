package k.kilg.rxcreate01;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (Throwable ignored) {
            //
        }
    }

}
