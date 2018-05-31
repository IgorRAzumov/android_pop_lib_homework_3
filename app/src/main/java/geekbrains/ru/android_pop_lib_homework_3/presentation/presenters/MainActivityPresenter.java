package geekbrains.ru.android_pop_lib_homework_3.presentation.presenters;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import geekbrains.ru.android_pop_lib_homework_3.presentation.views.MainActivityView;

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> {
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showMainFragment();
    }
}
