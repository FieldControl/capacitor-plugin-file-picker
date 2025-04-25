package com.whiteguru.capacitor.plugin.filepicker;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.PluginMethod;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CapacitorPlugin(name = "FilePicker")
public class FilePickerPlugin extends Plugin {

    private ActivityResultLauncher<Intent> pickerLauncher;
    private PluginCall savedCall;
    private ExecutorService executor;

    @Override
    public void load() {
        super.load();
        executor = Executors.newSingleThreadExecutor();
        pickerLauncher =
            registerActivityResultLauncher(
                new ActivityResultContracts.StartActivityForResult(),
                result -> processPickResult(savedCall, result)
            );
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
        if (!checkStoragePermission(savedCall)) {
            return;
        }

        Intent intent = PickerIntentFactory.create(getActivity(), settings.mimeTypes);
        wrapAndLaunch(intent, settings);
    }

    private void wrapAndLaunch(Intent intent, PickerSettings settings) {
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, settings.multiple);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        pickerLauncher.launch(Intent.createChooser(intent, settings.title));
    }

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

                if (data.getClipData() != null) {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        files.put(FileCopyHelper.copy(getContext(), uri));
                    }
                } else {
                    Uri uri = data.getData();
                    files.put(FileCopyHelper.copy(getContext(), uri));
                }

                int limit = call.getInt("limit", 0);
                if (settings.multiple && limit > 0 && files.length() > limit) {
                    showToast("Selecione no máximo " + limit + " arquivo(s). Você selecionou " + files.length());

                    startPicker(settings);
                    return;
                }

                call.resolve(files);
            }
        );
    }

    private boolean checkStoragePermission(PluginCall call) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (!hasPermission("photos")) {
                requestPermissionForAlias("photos", call, "storagePermissionCallback");
                return false;
            }
        }

        return true;
    }

    @PluginMethod
    public void storagePermissionCallback(PluginCall call) {
        PickerSettings settings = PickerSettings.from(call);
        startPicker(settings);
    }

    @Override
    protected void handleOnDestroy() {
        if (pickerLauncher != null) {
            pickerLauncher.unregister();
        }

        executor.shutdownNow();
    }
}
