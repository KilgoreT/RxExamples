package k.kilg.mainmodule.di.module;

import dagger.Module;
import dagger.Provides;
import k.kilg.mainmodule.di.PerTarget;
import k.kilg.mainmodule.ui.MainMvpPresenter;
import k.kilg.mainmodule.ui.MainMvpView;
import k.kilg.mainmodule.ui.MainPresenter;
import k.kilg.mainmodule.ui.fragments.ListOperationMvpPresenter;
import k.kilg.mainmodule.ui.fragments.ListOperationMvpView;
import k.kilg.mainmodule.ui.fragments.ListOperationPresenter;
import k.kilg.mainmodule.ui.fragments.OperatorResultMvpPresenter;
import k.kilg.mainmodule.ui.fragments.OperatorResultMvpView;
import k.kilg.mainmodule.ui.fragments.OperatorResultPresenter;

@Module
public class PresenterModule {

    @PerTarget
    @Provides
    public MainMvpPresenter<MainMvpView> provideMainPresenter() {
        return new MainPresenter<>();
    }

    @PerTarget
    @Provides
    public ListOperationMvpPresenter<ListOperationMvpView> provideListOperationPresenter() {
        return new ListOperationPresenter<>();
    }

    @PerTarget
    @Provides
    public OperatorResultMvpPresenter<OperatorResultMvpView> provideOperatorResultPresenter() {
        return new OperatorResultPresenter<>();
    }
}
