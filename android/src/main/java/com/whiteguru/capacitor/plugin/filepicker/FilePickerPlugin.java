package com.whiteguru.capacitor.plugin.filepicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CapacitorPlugin(name = "FilePicker")
public class FilePickerPlugin extends Plugin {

    private PluginCall savedCall;
    private ExecutorService executor;

    @Override
    public void load() {
        super.load();
        executor = Executors.newSingleThreadExecutor();
    }

    @PluginMethod
    public void pick(PluginCall call) {
        savedCall = call;

        PickerSettings settings = PickerSettings.from(call);
        startPicker(settings);
    }

    @PluginMethod
    public void pickImages(PluginCall call) {
        savedCall = call;
        PickerSettings settings = PickerSettings
            .from(call)
            .withMimeTypes(Collections.singletonList("image/*"))
            .withTitle(call.getString("title", "Select Images"));

        startPicker(settings);
    }

    @PluginMethod
    public void pickVideos(PluginCall call) {
        savedCall = call;
        PickerSettings settings = PickerSettings
            .from(call)
            .withMimeTypes(Collections.singletonList("video/*"))
            .withTitle(call.getString("title", "Select Videos"));

        startPicker(settings);
    }

    @PluginMethod
    public void pickFiles(PluginCall call) {
        savedCall = call;
        PickerSettings settings = PickerSettings
            .from(call)
            .withMimeTypes(Collections.singletonList("*/*"))
            .withTitle(call.getString("title", "Select Files"));

        startPicker(settings);
    }

    private void startPicker(PickerSettings settings) {
        Intent intent = PickerIntentFactory.create(getActivity(), settings.mimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, settings.multiple);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(savedCall, Intent.createChooser(intent, settings.title), "processPickResult");
    }

    @ActivityCallback
    private void processPickResult(PluginCall call, ActivityResult result) {
        if (call == null) return;
        if (result.getResultCode() != Activity.RESULT_OK || result.getData() == null) {
            call.reject("canceled");
            return;
        }

        executor.execute(
            () -> {
                Intent data = result.getData();
                JSArray files = new JSArray();

                int limit = call.getInt("limit", 0);
                if (limit > 0 && files.length() > limit) {
                    for (int i = limit; i < files.length(); i++) {
                        files.remove(i);
                    }
                }

                if (data.getClipData() != null) {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        String localUrl = getBridge().getLocalUrl();
                        files.put(FileCopyHelper.copy(getContext(), localUrl, uri));
                    }
                } else {
                    Uri uri = data.getData();
                    String localUrl = getBridge().getLocalUrl();
                    files.put(FileCopyHelper.copy(getContext(), localUrl, uri));
                }

                JSObject ret = new JSObject();
                ret.put("files", files);
                call.resolve(ret);
            }
        );
    }

    @Override
    protected void handleOnDestroy() {
        executor.shutdownNow();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
