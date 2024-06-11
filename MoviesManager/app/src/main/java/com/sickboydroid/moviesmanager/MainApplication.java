package com.sickboydroid.moviesmanager;

import android.app.Application;
import android.os.Environment;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.sickboydroid.moviesmanager.utils.Constants;
import com.sickboydroid.moviesmanager.utils.FileUtils;

import java.io.File;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        Constants.init(this, new File(getExternalCacheDir(), "sparrow"));
//        Constants.init(this, new File("/sdcard/sparrow"));
        Constants.init(this, new File(getFilesDir(), "sparrow"));
    }
}
