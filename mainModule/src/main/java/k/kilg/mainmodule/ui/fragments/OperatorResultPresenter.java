package k.kilg.mainmodule.ui.fragments;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import k.kilg.mainmodule.ui.base.BasePresenter;

public class OperatorResultPresenter<V extends OperatorResultMvpView> extends BasePresenter<V> implements OperatorResultMvpPresenter<V> {

    @Override
    public void runOperator(String operator) {

        Observable<String> observableToRun;

        try {

            Class clazz = Class.forName("k.kilg.rxmodule.Operators." + operator);
            Method method = clazz.getMethod("emit", null);
            observableToRun = (Observable<String>) method.invoke(null, null);

            Disposable d = observableToRun
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(longIndex -> {
                        if (getView() != null) {
                            getView().displayResult(longIndex);
                        }
                    });

            subscription.add(d);

        } catch (ClassNotFoundException e) {
            // TODO: 21.01.19 добавить вывод ошибок на экран
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
