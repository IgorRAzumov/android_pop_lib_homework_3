package geekbrains.ru.android_pop_lib_homework_3.presentation.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface MainFragmentView extends MvpView {

    void startPicturePicker();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void requestStoragePermission();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showPermissionErrorMessage();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showDecodeErrorMessage();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showCompleteDecodeMessage();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showNoSelectedPictureMessage();

    void checkPermission();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showFileErrorMessage();

    void showAbortButton();

    void showConvertButton();
}
