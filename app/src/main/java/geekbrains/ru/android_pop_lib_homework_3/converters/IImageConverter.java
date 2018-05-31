package geekbrains.ru.android_pop_lib_homework_3.converters;

import io.reactivex.Observable;

public interface IImageConverter {
    Observable<Boolean> convertToPng(String filePath);
}
