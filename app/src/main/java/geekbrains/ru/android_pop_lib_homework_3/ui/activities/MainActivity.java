package geekbrains.ru.android_pop_lib_homework_3.ui.activities;

import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import geekbrains.ru.android_pop_lib_homework_3.R;
import geekbrains.ru.android_pop_lib_homework_3.presentation.presenters.MainActivityPresenter;
import geekbrains.ru.android_pop_lib_homework_3.presentation.views.MainActivityView;
import geekbrains.ru.android_pop_lib_homework_3.ui.fragments.MainFragment;

public class MainActivity extends MvpAppCompatActivity implements MainActivityView {
    @InjectPresenter
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void showMainFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_activity_main_container, MainFragment.newInstance())
                .commit();
    }
}
