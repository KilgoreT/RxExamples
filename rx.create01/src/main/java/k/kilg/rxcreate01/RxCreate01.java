package k.kilg.rxcreate01;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;

@SuppressLint("CheckResult")
public class RxCreate01 extends AppCompatActivity {

    @BindView(R.id.text)
    public TextView text;

    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

     CompositeDisposable subscription = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_create01);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscription.clear();
    }

    @OnClick(R.id.btn01Simple)
    public void clickSimple() {
        clearText();
        /**
         * В subscriber размещается Consumer c "accept" callback.
         * в случае onComplete там размещается Action.
         */
        RxCreateUtils.getObserver(7)
                .subscribe(
                        // onNext
                        this::showText,
                        // onError
                        error -> showError(error.getMessage()),
                        // onComplete
                        this::showCompleteStatus
                );
    }

    @OnClick(R.id.btn02WithCache)
    public void clickWithCache() {
        clearText();
        RxCreateUtils.getObserverWithCache(4, 5)
                .subscribe(
                        this::showText,
                        error -> showError(error.getMessage()),
                        this::showCompleteStatus
                );

    }

    @OnClick(R.id.btn03WithUnSubscribe)
    public void click03() {
        clearText();
        Disposable disposable = RxCreateUtils.getObserverWithUnSubscriber(1, 7)
                .subscribe(
                        this::showText,
                        error -> showError(error.getMessage()),
                        this::showCompleteStatus
                );
        subscription.add(disposable);

    }

    @OnClick(R.id.btn04Error)
    public void click04() {
        clearText();
        RxCreateUtils.getErrorPattern()
                .subscribe(
                        this::showText,
                        error -> showError(error.getMessage()),
                        this::showCompleteStatus
                );
    }

    @OnClick(R.id.btn05HotObserver)
    public void click05() {
        checkGpsPermissions();
    }

    private void checkGpsPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(LOCATION_PERMS, 1);
            }

        } else {
            loadHotObserver2();
        }
    }

    private void loadHotObserver() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        RxCreateUtils.getHotObservable(locationManager)
                .subscribe(location -> {
                    text.setText(String.valueOf(location.getLatitude() + " " + location.getLongitude()));
                },
                        error -> showError(error.getMessage()),
                        this::showCompleteStatus);
    }

    /**
     * .publish().refCount() подсчитывают количество активных подписчиков.
     * Когда число подписчиков изменяется с 0 до 1 - производится подписка.
     * Все другие подписки игнорируются, и один и тот же обернутый Subscriber
     * совместно используется всеми подписчиками.
     * Когда подписчиков становится 0 - идет отписка.
     * Таким образом, используется одно соединение к какому-то ни было ресурсу.
     */
    private void loadHotObserver2() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Observable<Location> lazy = RxCreateUtils.getHotObservable(locationManager).publish().refCount();
        System.out.println(">>>> До всех подписчиков");
        Disposable s1 = lazy.subscribe();
        System.out.println(">>>> 1 подписка");
        Disposable s2 = lazy.subscribe();
        System.out.println(">>>> 2 подписка");
        s1.dispose();
        System.out.println(">>>> 1 отписка");
        s2.dispose();
        System.out.println(">>>> 2 отписка");
    }

    /**
     * Вызывая у Observable метод publish()
     * получаем #ConnectableObservable.
     * Впринципе можно и дальше пользоваться этим Observable,
     * но любой клиент подписавшийся на #ConnectableObservable запоминается.
     * Пока не вызван метод #connect ни один из клиентов не подписан на изначальный Observable.
     * После #connect подписчик-посредник подписывается на Observable,
     * несмотря на количество клиентов, пусть их даже и нет.
     * Если в множестве есть подписчики,
     * то они будут получать одну и ту же последовательность.
     */
    private void loadHotObserver3() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ConnectableObservable<Location> published = RxCreateUtils.getHotObservable(locationManager).publish();
        Disposable d1 = published.subscribe();
        Disposable d2 = published.subscribe();
        published.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadHotObserver();
                }
            }

        }
    }


    private void showText(Integer integer) {
        text.setText(text.getText().toString().concat(integer.toString()));
    }

    private void clearText() {
        text.setText("");
    }

    private void showCompleteStatus() {

    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
