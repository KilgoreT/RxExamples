package k.kilg.rxoperators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class RxOperators extends AppCompatActivity {

    /**
     * 1. Каждый оператор возвращает новый Observable, оставляя исходный без изменений.
     *
     */

    @BindView(R.id.text)
    public TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_operators);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn01FilterOdd)
    public void filterOdd() {
        clearText();
        RxOparatorsUtils
                .getFilteredOdd()
                .subscribe(this::showText);
    }

    private void clearText() {
        text.setText("");
    }

    private void showText(Integer integer) {
        text.setText(text.getText().toString().concat(integer.toString()));
    }
}
