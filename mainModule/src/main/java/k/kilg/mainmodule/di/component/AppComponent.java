package k.kilg.mainmodule.di.component;

import dagger.Component;
import k.kilg.mainmodule.di.PerTarget;
import k.kilg.mainmodule.di.module.PresenterModule;
import k.kilg.mainmodule.ui.MainActivity;
import k.kilg.mainmodule.ui.fragments.ListOperatorFragment;
import k.kilg.mainmodule.ui.fragments.OperatorResultFragment;

@PerTarget
@Component(modules = {PresenterModule.class})
public interface AppComponent {

    void injectMainActivity(MainActivity target);
    void injectListOperationFragment(ListOperatorFragment target);
    void injectOperatorResultFragment(OperatorResultFragment target);

}
