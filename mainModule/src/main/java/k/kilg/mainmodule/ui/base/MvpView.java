package k.kilg.mainmodule.ui.base;

import android.support.annotation.StringRes;

public interface MvpView {

    void showLoading();

    void hideLoading();

    //void openActivityOnTokenExpire();

    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    void showSnackBar(String message);

    void showLog(String message);

    //boolean isNetworkConnected();

    //void hideKeyboard();

}
