package k.kilg.rxcreate01;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

public class RxUtils {

    /**
     * 01.
     * Создание простейшего Observable
     *
     * @param id некое число
     * @return Observable возвращает заданный id
     */
    public static Observable<Integer> getIntObserver(int id) {

        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(id);
                emitter.onComplete();
            }
        });
    }

    // lambda version
    public static Observable<Integer> getIntObserverLambda(int id) {

        return Observable.create(emitter -> {
            emitter.onNext(id);
            emitter.onComplete();
        });

    }

    /**
     * 02. Создание с кешированием,
     * при первой подписке начинается генерация чисел,
     * при каждой следующей числа достаются из кэша.
     *
     * @param id    некое первоначальное число
     * @param range некий интервал
     * @return
     */
    public static Observable<Integer> getIntObserverWithCache(final int id, int range) {
        return Observable.<Integer>create(emitter -> {
            if (range <= 0) {
                emitter.onError(new Throwable("Range must be positive and non zero."));
            }
            for (int index = id; index < id + range; index++) {
                emitter.onNext(index);
            }
            emitter.onComplete();
        }).cache();
    }

    /**
     * 03. Обработка отписки
     * примитивная версия
     */
    public static Observable<Integer> getIntObserverWithUnSubscriber(final int id, int range) {
        return Observable.<Integer>create(emitter -> {

            if (range <= 0) {
                emitter.onError(new Throwable("Range must be positive and non zero."));
            }

            Runnable r = () -> {
                for (int index = id; index < id + range; index++) {
                    Utils.sleep(10, TimeUnit.SECONDS);
                    if (!emitter.isDisposed()) {
                        emitter.onNext(index);
                    } else {
                        emitter.onComplete();
                    }
                }
            };
            final Thread thread = new Thread(r);
            thread.start();
            // в setCancellable размещается Cancellable с "cancel" callback
            /**
             * Здесь установлен листенер на отписку.
             * Отписка -> поток прерывается -> onComplete
             */
            emitter.setCancellable(thread::interrupt);

        });
    }








    public static Observable<Integer> test() {
        return Observable.<Integer>create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onComplete();
            }
        });
    }
}
