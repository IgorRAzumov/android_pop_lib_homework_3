package geekbrains.ru.android_pop_lib_homework_3.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import io.reactivex.Observable;

public class AndroidUtils {
    private static final String ANDROID_PROVIDES_MEDIA = "com.android.providers.media";
    private static final String DOCUMENT_CONTRACT_SPLIT_SYMB = ":";
    private static final String SELECTION_STRING = "=?";
    private static final int DOCUMENT_ID_SEGMENT = 1;

    public static Observable<String> getRealPathFromMediaUri(Context context, Uri uri) {
        return Observable.fromCallable(() -> convertUriToRealPath(context, uri));
    }

    private static String convertUriToRealPath(Context context, Uri uri) {
        String filePath = "";
        if (uri.getHost().contains(ANDROID_PROVIDES_MEDIA)) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(DOCUMENT_CONTRACT_SPLIT_SYMB)[DOCUMENT_ID_SEGMENT];
            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + SELECTION_STRING;

            Cursor cursor = context
                    .getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
                            new String[]{id}, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        }
        return filePath;
    }
}
