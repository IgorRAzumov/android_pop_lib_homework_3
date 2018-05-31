package geekbrains.ru.android_pop_lib_homework_3.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import geekbrains.ru.android_pop_lib_homework_3.R;
import geekbrains.ru.android_pop_lib_homework_3.converters.ImageConverter;
import geekbrains.ru.android_pop_lib_homework_3.presentation.presenters.MainFragmentPresenter;
import geekbrains.ru.android_pop_lib_homework_3.presentation.views.MainFragmentView;
import geekbrains.ru.android_pop_lib_homework_3.ui.AndroidUtils;
import io.reactivex.Observable;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends MvpAppCompatFragment implements MainFragmentView {
    private static final int REQUEST_STORAGE_PERMISSION = 22;
    private static final int PICK_PICTURE_RESULT_CODE = 1;
    @BindView(R.id.bt_fragment_main_get_picture)
    Button actionButton;

    @InjectPresenter
    MainFragmentPresenter presenter;

    private Unbinder unbinder;
    private boolean storagePermissionGranted;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @ProvidePresenter
    public MainFragmentPresenter providePresenter() {
        return new MainFragmentPresenter(new ImageConverter());
    }

    @OnClick(R.id.bt_fragment_main_get_picture)
    public void getPictureToDecodeButtonClick() {
        presenter.getPictureToDecodeButtonClick(storagePermissionGranted, actionButton.getText()
                .equals(getString(R.string.fragment_main_bt_get_picture_to_decode_text)));
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            storagePermissionGranted = savedInstanceState
                    .getBoolean(getString(R.string.storage_permisson_granted_key));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(getString(R.string.storage_permisson_granted_key),
                storagePermissionGranted);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                storagePermissionGranted = (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED);
                if (storagePermissionGranted) {
                    presenter.getPictureToDecodeButtonClick(storagePermissionGranted,
                            actionButton.getText().equals(getString(
                                    R.string.fragment_main_bt_get_picture_to_decode_text)));
                } else {
                    presenter.noPermissionGranted();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_PICTURE_RESULT_CODE: {
                if (resultCode == RESULT_OK) {
                    presenter.decodePictureToPng(getFilePathFromResult(data));
                } else {
                    presenter.noSelectedPicture();
                }
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void startPicturePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(getString(R.string.image_type));
        startActivityForResult(intent, PICK_PICTURE_RESULT_CODE);
    }

    @Override
    public void checkPermission() {
        Context context = getContext();
        if (!storagePermissionGranted && context != null)
            storagePermissionGranted = ContextCompat.checkSelfPermission(
                    getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestStoragePermission() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_STORAGE_PERMISSION);
    }

    @Override
    public void showPermissionErrorMessage() {
        showMessage(getString(R.string.er_no_storage_permission));
    }

    @Override
    public void showDecodeErrorMessage() {
        showMessage(getString(R.string.er_error_decode));
    }

    @Override
    public void showCompleteDecodeMessage() {
        showMessage(getString(R.string.decode_complete));
    }

    @Override
    public void showNoSelectedPictureMessage() {
        showMessage(getString(R.string.no_selected_picture));
    }

    @Override
    public void showFileErrorMessage() {
        showMessage(getString(R.string.er_file_path));
    }

    @Override
    public void showAbortButton() {
        actionButton.setText(R.string.abort_text);
    }

    @Override
    public void showConvertButton() {
        actionButton.setText(R.string.fragment_main_bt_get_picture_to_decode_text);
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private Observable<String> getFilePathFromResult(Intent data) {
        Uri uri = data.getData();
        assert uri != null;
        return AndroidUtils.getRealPathFromMediaUri(getContext(), uri);
    }
}