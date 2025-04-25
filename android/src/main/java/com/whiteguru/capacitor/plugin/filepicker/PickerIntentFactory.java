package com.whiteguru.capacitor.plugin.filepicker;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

import java.util.List;

public class PickerIntentFactory {

    public static Intent create(Activity activity, List<String> types) {
        if (isOnlyImages(types)) {
            return createImageIntent(types.get(0));
        }

        return createDocumentIntent(types);
    }

    private static boolean isOnlyImages(List<String> types) {
        return types.size() == 1 && types.get(0).startsWith("image/");
    }

    private static Intent createImageIntent(String mime) {
        return new Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE)
                .setType(mime)
                .putExtra(Intent.EXTRA_MIME_TYPES, new String[]{mime})
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                .putExtra(MediaStore.EXTRA_OUTPUT, EXTERNAL_CONTENT_URI);
    }

    private static Intent createDocumentIntent(List<String> types) {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE);

        if (types.size() == 1) {
            i.setType(types.get(0));
        } else {
            i.setType("*/*").putExtra(Intent.EXTRA_MIME_TYPES, types.toArray(new String[0]));
        }

        return i;
    }
}
