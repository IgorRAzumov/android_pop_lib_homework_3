package geekbrains.ru.android_pop_lib_homework_3.presentation.presenters;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import geekbrains.ru.android_pop_lib_homework_3.converters.ImageConverter;
import geekbrains.ru.android_pop_lib_homework_3.presentation.views.MainFragmentView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainFragmentPresenter extends MvpPresenter<MainFragmentView> {
    private ImageConverter imageConverter;
    private Disposable convertToPngDisposable;

    public MainFragmentPresenter(ImageConverter imageConverter) {
        this.imageConverter = imageConverter;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().checkPermission();
    }

    public void getPictureToDecodeButtonClick(boolean storagePermissionGranted, boolean action) {
        if (!action) {
            getViewState().showConvertButton();
            if (convertToPngDisposable != null && !convertToPngDisposable.isDisposed()) {
                convertToPngDisposable.dispose();
            }
        } else {
            if (storagePermissionGranted) {
                getViewState().showAbortButton();
                getViewState().startPicturePicker();
            } else {
                getViewState().requestStoragePermission();
            }
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
        convertToPngDisposable = imageConverter.convertToPng(filePath).singleOrError()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(@NonNull Boolean result) {
                        if (result) {
                            getViewState().showCompleteDecodeMessage();
                        } else {
                            getViewState().showDecodeErrorMessage();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getViewState().showDecodeErrorMessage();
                    }
                });
    }

    public void noSelectedPicture() {
        getViewState().showNoSelectedPictureMessage();
    }
}
