package k.kilg.rxoperators;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.GroupedObservable;

public class RxOparatorsUtils {

    private static List<Integer> integerList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

    /**
     * Пример предиката используемого внутри функций Rx
     */
    private static Predicate<Integer> oddPredicate = integer -> (integer & 1) > 0;

//    /**
//     * Пример фильтрации четных чисел.
//     * Предикат возвращает только нечетные
//     *
//     * @return поток нечетных чисел.
//     */
//    public static Observable<Integer> filterExample() {
//        return Observable
//                .just(1, 2, 3, 4, 5, 6, 7, 8, 9)
//                .filter(oddPredicate);
//    }

    private static Function<Integer, Integer> multiply = integer -> integer * 10;

    /**
     * map() изменяет каждое входящее событие
     * Function(входящее, исходящее), в теле функции алгоритм изменения.
     *
     * @return
     */
    public static Observable<Integer> multiplyBy10() {
        return Observable
                .just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .map(multiply);
    }

    /**
     * flatmap используется:
     * 1. результат преобразования map() должен иметь тип Observable.
     * 2. преобразование имеет вид один-ко-многим,
     * т.е. одно входящее порождает несколько подсобытий.
     *
     * @return
     */
    public static Observable<String> intNumber() {
        return Observable
                .fromIterable(integerList)
                .flatMap((Function<Integer, ObservableSource<String>>) integer -> {
                            if ((integer & 1) > 0) {
                                return Observable.just("№" + integer).delay(integer, TimeUnit.SECONDS);
                            }
                            return Observable.just("№" + integer);
                        },
                        throwable -> Observable.error(throwable),
                        () -> Observable.just("EOF").delay(20, TimeUnit.SECONDS))
                .doOnNext(s -> Log.d("###", ">>>> " + s));
    }

    /**
     * параметр maxCurrency оператора flatmap() позволяет задать количество
     * конкурентных подписок на внутренние потоки.
     *
     * @return
     */
    public static Observable<String> intNumberMaxConcurrent() {
        return Observable
                .fromIterable(integerList)
                .flatMap((Function<Integer, ObservableSource<String>>) integer -> {
                            if ((integer & 1) > 0) {
                                return Observable.just("№" + integer).delay(2, TimeUnit.SECONDS);
                            }
                            return Observable.just("№" + integer);
                        },
                        throwable -> Observable.error(throwable),
                        () -> Observable.just("EOF").delay(30, TimeUnit.SECONDS),
                        3)
                .doOnNext(s -> Log.d("###", ">>>> " + s));
    }

    /**
     * merge()
     * подписывается на все события нескольких потоков
     * и возвращает их по мере их поступления.
     * Используется, когда нужно обращаться с несколькими источниками однотипных событий.
     */
    public static Observable<String> manySources() {

        AtomicInteger interval = new AtomicInteger(1);

        return Observable.merge(

                Observable.fromIterable(integerList)
                        .flatMap((Function<Integer, ObservableSource<Integer>>) integer -> Observable
                                .just(integer)
                                .delay(integer, TimeUnit.SECONDS)
                                .delay(300 * integer, TimeUnit.MILLISECONDS))
                        .map(x -> x.toString())
                        .doOnNext(s -> Log.d("###", ">>> " + s)),

                Observable.just('a', 'b', 'c', 'd', 'e', 'f', 'g')
                        .flatMap((Function<Character, ObservableSource<Character>>) character -> Observable
                                .just(character)
                                .delay(interval.getAndIncrement(), TimeUnit.SECONDS))
                        .map(x -> x.toString())

        );
    }

    /**
     * merge() c ошибками
     * при ошибке хотя бы из одного источника, ошибка пробрасывается далее
     * и события перестают поступать.
     * mergeDelayError() отложив доставку ошибки, дает потоку без ошибок завершиться,
     * и лишь потом передает ошибку.
     */

    public static Observable<String> manySourcesWithError() {

        AtomicInteger interval = new AtomicInteger(1);

        return Observable.mergeDelayError(

                Observable.fromIterable(integerList)
                        .flatMap((Function<Integer, ObservableSource<Integer>>) integer -> Observable
                                .just(integer)
                                .delay(integer, TimeUnit.SECONDS))
                        .flatMap(x -> x == 4 ? Observable.error(new Throwable("4 is not for 4")) : Observable.just(x))
                        .map(x -> x.toString())
                        .doOnNext(s -> Log.d("###", ">>> " + s)),

                Observable.just('a', 'b', 'c', 'd', 'e', 'f', 'g')
                        .flatMap((Function<Character, ObservableSource<Character>>) character -> Observable
                                .just(character)
                                .delay(interval.getAndIncrement(), TimeUnit.SECONDS))
                        .map(x -> x.toString())

        );
    }

    /**
     * zip() комбинирование событий из нескольких источников
     * оператор ждет по событию из каждого источника
     * и бинарной функцией каким-либо образом обрабатывает их.
     * обычно используется, когда источник может передать только один ответ.
     * В случае, когда потоки *НЕ СИНХРОНИЗИРОВАНЫ*, лучше избегать его использования.
     */

    public static Observable<String> zipSimple() {
        return Observable.zip(
                Observable.fromIterable(integerList)
                        .flatMap((Function<Integer, ObservableSource<Integer>>) integer -> Observable
                                .just(integer)
                                .delay(integer, TimeUnit.SECONDS))
                        .map(x -> x.toString())
                        .doOnNext(s -> Log.d("###", ">>> " + s)),

                Observable.just('a', 'b', 'c', 'd', 'e', 'f', 'g')
                        .flatMap((Function<Character, ObservableSource<Character>>) character -> Observable.just(character))
                        .map(x -> x.toString()),
                (sInt, sCh) -> sCh + sInt
        );
    }

    /**
     * combineLatest() комбинирование событий из нескольких источников.
     * при появлении события из одного источника
     * функция комбинирует его с последним событием другого источника.
     * Оператор симметричен, т.е. функция срабатывает при событии из любого источника.
     */

    public static Observable<String> combineLatestExample() {
        return Observable.combineLatest(

                Observable.interval(3, TimeUnit.SECONDS)
                        .map(x -> "S" + x),

                Observable.interval(2, TimeUnit.SECONDS)
                        .map(x -> "F" + x),

                (sInt, sCh) -> sCh + ":" + sInt
        );
    }

    /**
     * withLatestFrom() комбинирует сообщения из нескольких источников
     * отправляет результат только когда событие приходит из источника,
     * из которого вызван оператор.
     * Из другого источника берется последнее событие.
     * <p>
     * Если у другого источника не получено никаких событий, то идет ожидание первого события
     * и используются последние события обоих источников.
     * <p>
     * Можно указать дефолтное значение для другого потока оператором startWith(),
     * в этом случае с событиями главного источника комбинируется дефолтное значение.
     */
    public static Observable<String> withLatestFromExample() {

        Observable<String> fast = Observable
                .interval(200, TimeUnit.MILLISECONDS).map(x -> "F" + x)
                .delay(4, TimeUnit.SECONDS)
                .startWith("F?");
        Observable<String> slow = Observable.interval(500, TimeUnit.MILLISECONDS).map(x -> "S" + x);

        return slow.withLatestFrom(fast, (s, f) -> s + ":" + f);
    }

    /**
     * amb() подписывается на несколько источников
     * ждет, пока придет первое событие от любого из них,
     * после первого сообщения отбрасывает все другие источники,
     * переправляет события только с того источника, событие которого было первым.
     */
    private static Observable<String> stream(int initialDelay, int interval, String name) {
        return Observable
                .interval(initialDelay, interval, TimeUnit.MILLISECONDS)
                .map(x -> name + x)
                .doOnSubscribe(disposable -> Log.d("###", "Subscription to " + name))
                .doOnDispose(() -> Log.d("###", "Unsubscription from " + name));
    }

    public static Observable<String> ambExample() {
        return Observable
                .amb(Arrays
                        .asList(stream(100, 1700, "S"),
                                stream(200, 1000, "F")));
    }


    /**
     * scan() принимает два параметра:
     * последнее сгенерированное значение(аккумулятор) исходного Observable
     * текущее значение из исходного Observable.
     * в бинарной функции производятся какие-то вычисления, их результат отправляется дальше.
     */

    private static Observable<Long> progress = Observable.create(new ObservableOnSubscribe<Long>() {
        @Override
        public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
            for (int i = 0; i < 100; i++) {
                long size = (long) new Random().nextInt(10);
                Log.d("###", ">>> size : " + size);
                emitter.onNext(size);
            }
            emitter.onComplete();
        }
    });

    public static Observable<Long> scanExample() {
        return progress
                .scan((total, chunk) -> total + chunk);
    }

    /**
     * scan() c параметром первоначального значения.
     * в данном случае первоначальный параметр уйдет первым.
     * Т.е. уйдет на одно событие больше.
     * Если последовательность бесконечна - будет работать бесконечно.
     */

    public static Observable<Long> scanWithDefaultValue() {
        return progress
                .scan(7L, (total, chunk) -> total + chunk);
    }

    /**
     * reduce() тот же принцип, что и у scan,
     * но возвращает только конечное значение,
     * пропуская промежуточные
     * Так как возвращается Maybe, может не вернуть значение вообще,
     * например, если последовательность бесконечна.
     * По сути, reduce() - это scan().takeLast(1)
     */

    public static Maybe<Long> reduceExample() {
        return progress
                .reduce((aLong, aLong2) -> aLong + aLong2);
    }

    /**
     * reduce() может возвратить List всех значений.
     */
    public static Single<List<Long>> reduceList() {
        return progress
                .reduce(new ArrayList<>(), new BiFunction<List<Long>, Long, List<Long>>() {
                    @Override
                    public List<Long> apply(List<Long> list, Long aLong) throws Exception {
                        list.add(aLong);
                        return list;
                    }
                });
    }

    /**
     * collect() возвращает один элемент(аккумулятор) с контейнером/коллекцией/структурой данных,
     * содержащий события источника данных.
     * В бинарной функции возможно определить условие, по которому события собираются в контейнер.
     * Контейнер(аккумулятор) обертывается в Callable, в отличии от Rx1
     */
    public static Single<List<Long>> collectExample() {
        return progress
                .collect(
                        new Callable<ArrayList<Long>>() {
                            @Override
                            public ArrayList<Long> call() throws Exception {
                                return new ArrayList<Long>();
                            }
                        },
                        (list, longSize) -> list.add(longSize));
    }

    /**
     * distinct() - удаляет дубликаты
     * Для сравнения элементов используются equals() и hashCode().
     * Нужно помнить, что оператор потребляет память.
     */
    public static Observable<Long> distinctExample() {
        return progress
                .distinct();
    }

    /**
     * distinctUntilChanged() отбрасывает событие, если оно совпадает с предыдущим.
     * По умолчанию, используется equals() для сравнения.
     */
    public static Observable<Long> distinctUntilChangedExample() {
        return progress
                .distinctUntilChanged();
    }

    // TODO: 27.12.18 операторы без примера могут отличаться в деталях в Rxv2, проверить.
    /**
     * take(n) - обрывает поток, получив первые n событий.
     * skip(n) - отбрасывает первые n событий и начинает ретранслировать события n+1
     *  в обоих случаях:
     *  отрицательные числа рассматриваются как 0
     *  превышение размера Observable не считается ошибкой.
     */

    /**
     * takeLast(n) - отдает n последних событий.
     *  хранит n событий в буфере и отдает буфер, когда поток завершится.
     *  к бесконечному потоку применять нет смысла.
     * skipLast(n) - отдает все события, кроме последних n.
     *  т.е. отдает первое событие, только после получения n+1 событий,
     *  поэтому так же используется буфер. и не несет смысла в бесконечных потоках.
     */

    /**
     * first() == take(1).single()
     * last() == takeLast(1).single()
     *  могут принимать предикаты, берется первое/последнее соответствующее предикату событие.
     *  в случае отсутствия подходящих значений - возвращают NoSuchElementException.
     */

    /**
     * takeFirst(predicate) - отличается от first(predicate) тем,
     *  что не возвращает NoSuchElementException, если отсутствуют походящие события.
     */

    /**
     * takeUntil(predicate) - ретранслирует события
     * и отписывается сразу после отправки первого соответствующего события,
     * т.е. последнее его сообщение - первое соответствующее предикату.
     */
    public static Observable<Integer> takeUntilExample() {
        return Observable
                .fromIterable(integerList)
                .takeUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer == 4;
                    }
                });
    }

    /**
     * takeWhile(predicate) - продолжает ретранслировать, пока события удовлетворяют предикату.
     * если первое событие не удовлетворяет - не ретранслирует ничего.
     */
    public static Observable<Integer> takeWhileExample() {
        return Observable
                .fromIterable(integerList)
                .takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 4;
                    }
                });
    }

    /**
     * elementAt(n) - выборка элемента по индексу.
     *  вызывает {@link IndexOutOfBoundsException}, если n < 0 и n > "Observable.size"
     *  хз, особо не нужен имх.
     */

    /**
     * all(predicate) - выдает true, если все события удовлетворяют
     * exist(predicate) - выдает true, как только появилось первое удовлетворяющее
     * contains(value) - выдает true, если значение содержится в потоке.
     */

    /**
     * concat() - соединяет два источника.
     *  когда завершается один источник, оператор подписывается на второй.
     *  одно из использований(с first()): предоставление события по умолчанию,
     *  если первый поток ничего не породил.
     */
    // TODO: 27.12.18 охуенно большой пример в книге. Раскурить.

    /**
     * switchOnNext() - как только появляется событие от нового источника,
     *  оператор отписывается от старого и подписывается на новый.
     */

    static Observable<String> speak(String quote, long millisPerChar) {

        String[] tokens = quote.replaceAll("[:,]", "").split(" ");

        Observable<String> words = Observable.fromArray(tokens);

        Observable<Long> absoluteDelay = words
                .map(String::length)
                .map(len -> len * millisPerChar)
                .scan((total, current) -> total + current);

        return Observable
                .zip(
                        words,
                        absoluteDelay.startWith(0L),
                        Pair::new)
                .flatMap(
                        pair -> Observable
                                .just(pair.first)
                                .delay(pair.second, TimeUnit.MILLISECONDS));

    }

    static Observable<String> alice = speak("To be or not to be", 110);
    static Observable<String> bob = speak("Lalala Tapala", 300);
    static Observable<String> jane = speak("Lapi comnut hunder brodasumus", 100);

    static Random rnd = new Random();
    static Observable<Observable<String>> quotes = Observable
            .just(
                    alice.map(w -> "Alice: " + w),
                    bob.map(w -> "Bob: " + w),
                    jane.map(w -> "Jane: " + w))
            .flatMap(innerObj -> Observable.just(innerObj))
            .delay(rnd.nextInt(5), TimeUnit.SECONDS);

    public static Observable<String> switchOnNextExample() {
        return Observable.switchOnNext(quotes);
    }

    /**
     * groupBy() - разбивает поток на несколько параллельных потоков,
     *  каждый из которых характеризуется одним и тем же значением ключа.
     */

    public static Observable<Boolean> groupByExample() {
        return Observable.fromIterable(integerList)
                .groupBy(integer -> ((integer & 1) > 0))
                .map(new Function<GroupedObservable<Boolean, Integer>, Boolean>() {
                    @Override
                    public Boolean apply(GroupedObservable<Boolean, Integer> booleanIntegerGroupedObservable) throws Exception {
                        return booleanIntegerGroupedObservable.getKey();
                    }
                });
    }

    /**
     * compose() - повторное использование операторов, преобразовывает Observable.
     * По факту, тут просто группировка существующих операторов.
     *  параметр - ObservableTransformer.
     *  параметр выполняется в момент сборки Observable, а не лениво в момент подписки.
     */

    private static ObservableTransformer<Integer, Integer> odd() {
        return upstream -> upstream
                .filter(oddPredicate);
    }

    public static Observable<Integer> composeExample() {
        return Observable
                .fromIterable(integerList)
                .compose(odd());
    }
}
