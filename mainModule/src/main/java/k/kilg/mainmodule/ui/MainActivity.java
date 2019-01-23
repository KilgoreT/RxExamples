package k.kilg.mainmodule.ui;

import android.os.Bundle;

import javax.inject.Inject;

import k.kilg.mainmodule.R;
import k.kilg.mainmodule.di.component.AppComponent;
import k.kilg.mainmodule.di.component.DaggerAppComponent;
import k.kilg.mainmodule.di.module.PresenterModule;
import k.kilg.mainmodule.ui.base.BaseActivity;
import k.kilg.mainmodule.ui.fragments.ListOperatorFragment;
import k.kilg.mainmodule.ui.fragments.OperatorResultFragment;

public class MainActivity extends BaseActivity implements ListOperatorFragment.OnListOperatorFragmentListener,
        OperatorResultFragment.OnFragmentInteractionListener, MainMvpView {

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;

    private ListOperatorFragment mListOperationFragment;
    private OperatorResultFragment mOperatorResultFragment;

    private static AppComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mComponent = DaggerAppComponent
                .builder()
                .presenterModule(new PresenterModule())
                .build();

        mComponent.injectMainActivity(this);

        mPresenter.onAttach(this);


        if (getSupportFragmentManager().findFragmentById(R.id.fragment) != null) {
        } else {
            mListOperationFragment = ListOperatorFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, mListOperationFragment)
                    .commit();
        }
    }

    @Override
    public AppComponent getComponent() {
        // TODO: 04.01.19 проверка на null
        // TODO: 24.01.19 зачем? уже не помню
        return mComponent;
    }

    @Override
    public void onRunClick(String operator) {
        mOperatorResultFragment = OperatorResultFragment.newInstance(operator);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, mOperatorResultFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCloseClick() {
        getSupportFragmentManager()
                .popBackStack();
    }
}
