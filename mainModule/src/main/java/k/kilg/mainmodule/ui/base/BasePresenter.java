package k.kilg.mainmodule.ui.base;

import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private V mMvpView;
    protected CompositeDisposable subscription = new CompositeDisposable();

    @Override
    public V getView() {
        return mMvpView;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mMvpView = null;
        subscription.clear();
    }

    @Override
    public void showLog(String message) {
        Log.d("###", "<" + getClass().getSimpleName() + ">: " + message);
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }
}
