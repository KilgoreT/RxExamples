package k.kilg.mainmodule.ui;

import android.util.Log;

import k.kilg.mainmodule.ui.base.BasePresenter;

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {
    @Override
    public void test() {
        Log.d("###", ">>>>>>>>>>>> test passed!");
    }
}
