package com.whiteguru.capacitor.plugin.filepicker;

import com.getcapacitor.PluginCall;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

public class PickerSettings {

    public final boolean multiple;
    public final int limit;
    public final List<String> mimeTypes;
    public final String title;

    private PickerSettings(boolean multiple, int limit, List<String> mimeTypes, String title) {
        this.multiple = multiple;
        this.limit = limit;
        this.mimeTypes = mimeTypes;
        this.title = title;
    }

    public static PickerSettings from(PluginCall call) {
        boolean multiple = call.getBoolean("multiple", false);
        int limit = call.getInt("limit", 0);
        String title = call.getString("title", "Selecione um arquivo");

        JSONArray arr = call.getArray("mimes");
        List<String> mimes = new ArrayList<>();
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                try {
                    mimes.add(arr.getString(i));
                } catch (JSONException e) {}
            }
        }

        if (mimes.isEmpty()) {
            mimes = Collections.singletonList("*/*");
        }

        return new PickerSettings(multiple, limit, mimes, title);
    }

    public PickerSettings withMimeTypes(List<String> mimes) {
        return new PickerSettings(this.multiple, this.limit, mimes, this.title);
    }

    public PickerSettings withTitle(String title) {
        return new PickerSettings(this.multiple, this.limit, this.mimeTypes, title);
    }
}
