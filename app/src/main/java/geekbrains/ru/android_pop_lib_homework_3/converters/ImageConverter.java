package geekbrains.ru.android_pop_lib_homework_3.converters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;

public class ImageConverter implements IImageConverter {
    private static final String DOT = ".";
    private static final String PNG_EXT = "png";
    private static final int COMPRESS_QUALITY = 100;

    @Override
    public  Observable<Boolean> convertToPng(String filePath) {
        return Observable.fromCallable(() -> startDecodeToPng(filePath));
    }

    private  boolean startDecodeToPng(String filePath) {
        boolean resultDecode = false;
        File imageFile = new File(filePath);
        File convertedImageFile = new File(createConvertedFilePath(filePath, PNG_EXT));
        if (imageFile.equals(convertedImageFile)) {
            return true;
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(convertedImageFile)) {
            resultDecode = BitmapFactory
                    .decodeFile(imageFile.getAbsolutePath())
                    .compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, fileOutputStream);
            fileOutputStream.flush();

  //          imageFile.delete()
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (!resultDecode && convertedImageFile.exists()) {
                convertedImageFile.delete();
            }
        }
        return resultDecode;
    }

    private void deleteImageFile(File imageFile) {
        imageFile.delete();
    }

    private String createConvertedFilePath(String originalFilePath, String convertExt) {
        StringBuilder convertedFilePath = new StringBuilder();
        int lastIndexOfDot = originalFilePath.lastIndexOf(DOT);
        if (lastIndexOfDot > 0) {
            convertedFilePath
                    .append(originalFilePath.substring(0, lastIndexOfDot))
                    .append(DOT)
                    .append(convertExt);
        } else if (lastIndexOfDot == 0) {
            convertedFilePath
                    .append(DOT)
                    .append(convertExt);
        }
        return convertedFilePath.toString();
    }
}
