package k.kilg.mainmodule.ui.base;

public interface MvpPresenter<V extends MvpView> {

    V getView();
    //Context getContext();
    void onAttach(V mvpView);
    void onDetach();
    void showLog(String message);
}
