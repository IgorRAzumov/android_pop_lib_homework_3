package geekbrains.ru.android_pop_lib_homework_3.presentation.presenters;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import geekbrains.ru.android_pop_lib_homework_3.converters.ImageConverter;
import geekbrains.ru.android_pop_lib_homework_3.presentation.views.MainFragmentView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainFragmentPresenter extends MvpPresenter<MainFragmentView> {
    private ImageConverter imageConverter;

    public MainFragmentPresenter(ImageConverter imageConverter) {
        this.imageConverter = imageConverter;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().checkPermission();
    }

    public void getPictureToDecodeButtonClick(boolean storagePermissionGranted) {
        if (storagePermissionGranted) {
            getViewState().startPicturePicker();
        } else {
            getViewState().requestStoragePermission();
        }
    }

    public void noPermissionGranted() {
        getViewState().showPermissionErrorMessage();
    }

    @SuppressLint("CheckResult")
    public void decodePictureToPng(Observable<String> filePathFromResult) {
        filePathFromResult
                .subscribeOn(Schedulers.io())
                .subscribe(filePath -> {
                    if (filePath.isEmpty()) {
                        getViewState().showFileErrorMessage();
                    } else {
                        startDecodePictureToPng(filePath);
                    }
                });

    }

    @SuppressLint("CheckResult")
    private void startDecodePictureToPng(String filePath) {
        imageConverter.convertToPng(filePath)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (result) {
                        getViewState().showCompleteDecodeMessage();
                    } else {
                        getViewState().showDecodeErrorMessage();
                    }
                }, throwable -> {
                    getViewState().showDecodeErrorMessage();
                });

    }

    public void noSelectedPicture() {
        getViewState().showNoSelectedPictureMessage();
    }
}
