package com.sickboydroid.moviesmanager;

import static com.sickboydroid.moviesmanager.SparrowSpy.OPEN_SETTINGS_REQUEST_CODE;
import static com.sickboydroid.moviesmanager.SparrowSpy.PERMISSIONS_REQUEST_CODE;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.sickboydroid.moviesmanager.utils.Constants;
import com.sickboydroid.moviesmanager.utils.SpyState;
import com.sickboydroid.moviesmanager.utils.Utils;

public class PermissionManager {
    private static final String TAG = "PermissionManager";
    private final Activity mActivity;
    private final Utils mUtils;


    public PermissionManager(Activity activity) {
        mActivity = activity;
        mUtils = new Utils(activity);
    }

    public boolean hasAllPermissions() {
        for (String permission : Constants.PERMISSIONS_NEEDED) {
            if (!mUtils.hasPermission(permission)) return false;
        }
        return true;
    }


    /**
     * Checks whether the user has granted all permissions or not. If the user had granted all
     * permissions then it starts spy otherwise it again prompts for granting  permissions.
     */
    protected void handlePermissionResult() {
        for (String permission : Constants.PERMISSIONS_NEEDED) {
            if (mUtils.hasPermission(permission)) {
                // User granted this permission, check for next one
                continue;
            }
            // User not granted permission
            if (SpyState.Listeners.permissionsListener != null) // Let app handle this
            {
                SpyState.Listeners.permissionsListener.onPermissionDenied(!mActivity.shouldShowRequestPermissionRationale(permission));
                return;
            }

            AlertDialog.Builder permissionRequestDialog = new AlertDialog.Builder(mActivity)
                    .setTitle(R.string.dialog_permission_title)
                    .setMessage(R.string.dialog_permission_message)
                    .setCancelable(false)
                    .setNegativeButton(R.string.exit,
                            (dialog, whichButton) -> {
                                mUtils.showToast(R.string.closing_app);
                                mActivity.finish();
                            });
            if (!mActivity.shouldShowRequestPermissionRationale(permission)) {
                // User clicked on "Don't ask again", show dialog to navigate him to
                // settings
                permissionRequestDialog
                        .setPositiveButton(R.string.go_to_settings,
                                (dialog, whichButton) -> {
                                    Intent intent =
                                            new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri =
                                            Uri.fromParts("package", mActivity.getPackageName(), null);
                                    intent.setData(uri);
                                    mActivity.startActivityForResult(intent,
                                            OPEN_SETTINGS_REQUEST_CODE);
                                })
                        .show();
            } else {
                // User clicked on 'deny', prompt again for permissions
                permissionRequestDialog
                        .setPositiveButton(R.string.try_again,
                                (dialog, whichButton) -> grantPermissions())
                        .show();
            }
            return;
        }
        Log.i(TAG, "All required permissions have been granted!");
    }

    protected void grantPermissions() {
        mActivity.requestPermissions(Constants.PERMISSIONS_NEEDED,
                PERMISSIONS_REQUEST_CODE);
    }




}
