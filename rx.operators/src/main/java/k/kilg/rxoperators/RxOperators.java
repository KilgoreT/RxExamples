package k.kilg.rxoperators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import k.kilg.rxoperators.Operators.SimpleOperators;

public class RxOperators extends AppCompatActivity {

    /**
     * 1. Каждый оператор возвращает новый Observable, оставляя исходный без изменений.
     * т.е. создается обертка вокруг исходного Observable.
     * 2. Пока клиент не подписался - операторы не срабатывают,
     * даже если идут какие-то данные из источника.
     *
     */

    CompositeDisposable subscription = new CompositeDisposable();

    @BindView(R.id.text)
    public TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_operators);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn01FilterOdd)
    public void onButton01() {
        clearText();
        subscription.clear();
        Disposable s = SimpleOperators
                .filterExample()
                .subscribe(this::showText);
        subscription.add(s);
    }

    @OnClick(R.id.btn02Map)
    public void onButton02() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .multiplyBy10()
                .subscribe(this::showText);
        subscription.add(s);
    }

    @OnClick(R.id.btn03FlatMapSimple)
    public void onButton03() {
        clearText();
        subscription.clear();
        Disposable s =  RxOparatorsUtils
                .intNumber()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );

        subscription.add(s);
    }

    @OnClick(R.id.btn04FlatMapMaxConcurrency)
    public void onButton04() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .intNumberMaxConcurrent()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showText);
    }

    @OnClick(R.id.btn05MergeSimple)
    public void onButton05() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .manySources()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showText);
        subscription.add(s);
    }

    @OnClick(R.id.btn06MergeWithError)
    public void onButton06() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .manySourcesWithError()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn07ZipSimple)
    public void onButton07() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .zipSimple()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn08CombineLatest)
    public void onButton08() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .combineLatestExample()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn09WithLatestFrom)
    public void onButton09() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .withLatestFromExample()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn10Amb)
    public void onButton10() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .ambExample()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn11Scan)
    public void onButton11() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .scanExample()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn12ScanWithDefault)
    public void onButton12() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .scanWithDefaultValue()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn13Reduce)
    public void onButton13() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .reduceExample()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn14Collect)
    public void onButton14() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .collectExample()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn15Distinct)
    public void onButton15() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .distinctExample()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn16TakeUntil)
    public void onButton16() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .takeUntilExample()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn17TakeWhile)
    public void onButton17() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .takeWhileExample()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn18SwitchOnNext)
    public void onButton18() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .switchOnNextExample()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    @OnClick(R.id.btn19GroupByExample)
    public void onButton19() {
        clearText();
        subscription.clear();
        Disposable s = RxOparatorsUtils
                .groupByExample()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showText,
                        throwable -> showToast(throwable.getMessage())
                );
        subscription.add(s);
    }

    private void showText(Boolean aBoolean) {
        showText(aBoolean.toString());
    }


    private void clearText() {
        text.setText("");
    }

    private void showText(String string) {
        if (text.getText().toString().isEmpty()) {
            text.setText(string);
        } else {
            text.setText(text.getText().toString().concat(", " + string));
        }
    }

    private void showText(Integer integer) {
        showText(String.valueOf(integer));
    }

    private void showText(Long l) {
        showText(String.valueOf(l));
    }

    private void showText(List<Long> list) {
        for (Long l : list) {
            showText(String.valueOf(l));
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
