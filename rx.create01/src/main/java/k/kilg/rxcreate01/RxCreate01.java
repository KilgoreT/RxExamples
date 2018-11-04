package k.kilg.rxcreate01;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@SuppressLint("CheckResult")
public class RxCreate01 extends AppCompatActivity {

    @BindView(R.id.text)
    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_create01);

        ButterKnife.bind(this);




    }

    @OnClick(R.id.btn01Simple)
    private void clickSimple() {
        /**
         * В subscriber размещается Consumer c "accept" callback.
         * в случае onComplete там размещается Action.
         */
        RxUtils.getIntObserver(7)
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
    private void clickWithCache() {

        RxUtils.getIntObserverWithCache(4, 5)
                .subscribe(
                        this::showText,
                        error -> showError(error.getMessage()),
                        this::showCompleteStatus
                );

    }


    @OnClick(R.id.btn03WithUnSubscribe)
    private void click03() {

        RxUtils.getIntObserverWithUnSubscriber(1, 7)
                .subscribe(
                        this::showText,
                        error -> showError(error.getMessage()),
                        this::showCompleteStatus
                );

    }

    private void showText(Integer integer) {
        text.setText(text.getText().toString().concat(integer.toString()));
    }

    private void showCompleteStatus() {

    }

    private void showError(String message) {

    }
}
