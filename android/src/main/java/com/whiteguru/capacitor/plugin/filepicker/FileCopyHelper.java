package com.whiteguru.capacitor.plugin.filepicker;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.widget.Toast;
import com.getcapacitor.FileUtils;
import com.getcapacitor.JSObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileCopyHelper {

    public static JSObject copy(Context context, String localUrl, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return null;
        }

        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String name = cursor.getString(nameIndex);
        File file = new File(context.getCacheDir(), name);

        try (InputStream in = context.getContentResolver().openInputStream(uri); FileOutputStream out = new FileOutputStream(file)) {
            byte[] buf = new byte[1024 * 1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            cursor.close();
            return null;
        }

        cursor.close();

        Uri fileUri = Uri.fromFile(file);
        JSObject result = new JSObject();
        result.put("path", fileUri.toString());

        result.put("webPath", FileUtils.getPortablePath(context, localUrl, fileUri));
        result.put("name", name);

        int dot = name.lastIndexOf('.');
        String ext = dot > 0 ? name.substring(dot + 1).toLowerCase() : "";
        result.put("extension", ext);

        return result;
    }
}
