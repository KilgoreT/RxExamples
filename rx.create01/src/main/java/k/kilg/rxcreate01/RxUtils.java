package k.kilg.rxcreate01;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.subjects.PublishSubject;

public class RxUtils {

    /**
     * 01.
     * Создание простейшего Observable
     *
     * @param id некое число
     * @return Observable возвращает заданный id
     */
    public static Observable<Integer> getObserver(int id) {

        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(id);
                emitter.onComplete();
            }
        });
    }

    // lambda version
    public static Observable<Integer> getObserverLambda(int id) {

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
    public static Observable<Integer> getObserverWithCache(final int id, int range) {
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
     * примитивная версия, работает с бесконечными потоками
     */
    public static Observable<Integer> getObserverWithUnSubscriber(final int id, int range) {
        return Observable.<Integer>create(emitter -> {

            if (range <= 0) {
                emitter.onError(new Throwable("Range must be positive and non zero."));
            }

            Runnable r = () -> {
                for (int index = id; index < id + range; index++) {
                    Utils.sleep(2, TimeUnit.SECONDS);
                    if (!emitter.isDisposed()) {
                        emitter.onNext(index);
                    } else {
                        Log.d("###", ">>> Unsubscribe!");
                        emitter.onComplete();
                    }
                }
                emitter.onComplete();
            };
            final Thread thread = new Thread(r);
            thread.start();
            // в setCancellable размещается Cancellable с "cancel" callback
            /**
             * Здесь установлен листенер на отписку.
             * Отписка -> поток прерывается -> onComplete
             * Но кажется что это нихрена не работает.
             */
            emitter.setCancellable(() -> {
                Log.d("###", ">>> Interrupt!");
                thread.interrupt();
            });

        });
    }

    /**
     * 04. Обработка ошибок.
     * Рекомендация: заключать целые выражения внутри create() в try-catch.
     *
     * @param index
     * @return
     */
    public static Observable<Integer> getErrorObserver(int index) {
        return Observable.create(emitter -> {
            if (index == 0) emitter.onError(new Throwable("Errors always go!"));
            emitter.onNext(index);
            emitter.onComplete();
        });
    }

    /**
     * fromCallable отрабатывает паттерн с обертыванием в try-catch
     * и возвращением одного значения.
     * внутрь кладется метод, получающие либо одно значение, либо ошибку
     *
     * @return либо ошибка, либо число.
     */
    public static Observable<Integer> getErrorPattern() {
        return Observable.fromCallable(() -> {
            return loadPossibleError(0);
        });
    }

    private static Integer loadPossibleError(int index) throws Exception {
        if (index == 0) throw new Exception("This error is fromCallable");
        return 42;
    }

    /**
     * 05. пример создания горячего Observable.
     * когда есть некий листенер и он асинхронно колбэками выдает какую-то информацию.
     *
     * @param locationManager
     * @return
     */
    @SuppressLint("MissingPermission")
    public static Observable<Location> getHotObservable(LocationManager locationManager) {
        return Observable.create(emitter -> {
            System.out.println(">>> Открывается соединение");
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // новая порция информации
                    emitter.onNext(location);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {
                    //когда провайдеров не останется - поток завершается.
                    emitter.onComplete();
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        });
    }


    /**
     *  06. Subject
     *  Расширяет Observable и одновременно реализует Observer.
     *  Полезное средство создания экземпляров Observable в случае,
     *  когда Observable.create() кажется слишком сложным.
     *  Есть разные ньюансы, поэтому лучше вначале пробовать Observable.create()
     *  и фабричные методы.
     * @param locationManager
     * @return
     */
    @SuppressLint("MissingPermission")
    public static Observable<Location> getHotObservableThroughSubject(LocationManager locationManager) {

        final PublishSubject<Location> subject = PublishSubject.create();

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // новая порция информации
                subject.onNext(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                subject.onComplete();
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        return subject;

    }
}
