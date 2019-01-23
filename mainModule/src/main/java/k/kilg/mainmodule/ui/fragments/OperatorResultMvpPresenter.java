package k.kilg.mainmodule.ui.fragments;

import k.kilg.mainmodule.ui.base.MvpPresenter;

public interface OperatorResultMvpPresenter<V extends OperatorResultMvpView> extends MvpPresenter<V> {

    void runOperator(String operator);

}
