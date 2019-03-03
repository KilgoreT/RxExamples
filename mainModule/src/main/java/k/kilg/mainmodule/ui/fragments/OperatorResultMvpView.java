package k.kilg.mainmodule.ui.fragments;

import k.kilg.mainmodule.ui.MainMvpView;
import k.kilg.mainmodule.ui.base.MvpFragmentView;

public interface OperatorResultMvpView extends MvpFragmentView {

    // TODO: 04.01.19 переименовать
    MainMvpView getRoot();
    void displayResult(String longIndex);

}
