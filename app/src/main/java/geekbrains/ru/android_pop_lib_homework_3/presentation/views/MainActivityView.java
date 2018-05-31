package geekbrains.ru.android_pop_lib_homework_3.presentation.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface MainActivityView extends MvpView {

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showMainFragment();
}
