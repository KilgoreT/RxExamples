package k.kilg.mainmodule.ui.fragments;

import android.util.Log;

import k.kilg.mainmodule.ui.base.BasePresenter;

public class ListOperationPresenter<V extends ListOperationMvpView> extends BasePresenter<V> implements ListOperationMvpPresenter<V> {

    @Override
    public void test() {
        Log.d("###", ">>>>>>>>>>> fine testing!");
    }

}
