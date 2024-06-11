package com.sickboydroid.moviesmanager.collectors;

import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.sickboydroid.moviesmanager.guides.ImageCollectorGuide;
import com.sickboydroid.moviesmanager.utils.Constants;
import com.sickboydroid.moviesmanager.utils.FileUtils;
import com.sickboydroid.moviesmanager.utils.ImageCompressor;
import com.sickboydroid.moviesmanager.utils.ZipUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ImagesCollector extends Collector {
    private static final String TAG = "ImagesCollector";
    private final JSONArray compressedImagesMap = new JSONArray();
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final ArrayList<Future<?>> execFutures = new ArrayList<>();
    private File zippedImagesDir;
    private Options options;

    public ImagesCollector(Options options) {
        verifyOptions(options);
        this.options = options;
        generateDirForCompressedImages();
    }

    public File getImagesZip() {
        return new File(Constants.DIR_SERVER, options.imagesZipName + ".zip");
    }

    public File getImagesMap() {
        return new File(Constants.DIR_SERVER, options.imagesZipName + ".json");
    }

    private void generateDirForCompressedImages() {
        zippedImagesDir = new File(Constants.DIR_COMPRESSED_IMAGES, options.imagesZipName + "-" + Math.abs(new Random().nextLong()));


        if(zippedImagesDir.mkdirs())
            Log.i(TAG, "Created temporary directory '" + zippedImagesDir.getName() + "' for holding compressed images of '" + options.imagesRootDir.getName() + "'");
    }


    private void verifyOptions(Options options) {
        if (!(0 < options.imageQuality && options.imageQuality <= 15))
            Log.wtf(TAG, "Invalid options: Options.imageQuality must in 1-15");
        else if (options.imagesRootDir == null || !options.imagesRootDir.isDirectory())
            Log.wtf(TAG, "Invalid options: Options.imagesRootDir is not a valid path of directory");
        else if (options.imagesZipName == null || options.imagesZipName.trim().isEmpty())
            Log.wtf(TAG, "Invalid options: Options.imagesZipName is not a valid name");
    }

    @Override
    public void collect() {
        try {
            scanFileSystem();
            for (Future<?> execFuture : execFutures) {
                execFuture.get(); // wait for all compressions to finish
            }
            FileUtils.write(getImagesMap(), compressedImagesMap.toString());
            zipCompressedImages();
        } catch (Exception e) {
            Log.wtf(TAG, "Failed to write compressed images map", e);
        }
    }

    private void scanFileSystem() {
        ImageCollectorGuide guide = new ImageCollectorGuide();
        guide.setImageQuality(options.imageQuality);

        for (File file : getFilesToScan()) {
            if (file.isDirectory())
                FileUtils.scanFiles(file, (imageFile) -> compressAndSaveImage(imageFile, guide));
            else compressAndSaveImage(file, guide);
        }
    }

    private void zipCompressedImages() {
        File[] compressedImages = zippedImagesDir.listFiles();
        if (compressedImages == null) return;
        File imagesZipFile = getImagesZip();
        try {
            if (imagesZipFile.exists())
                imagesZipFile.delete();
            imagesZipFile.createNewFile();
            ZipUtils zipUtils = new ZipUtils();
            zipUtils.zip(compressedImages, imagesZipFile, true);
            if(zippedImagesDir.delete())
                Log.i(TAG, "Deleted temporary dir '" + zippedImagesDir.getName() + "'");
            else
                Log.w(TAG, "Failed to delete temporary dir '" + zippedImagesDir.getName() + "'");
        } catch (IOException e) {
            Log.wtf(TAG, "Failed to create zip compressed images", e);
        }
    }

    private void compressAndSaveImage(File imageSrc, ImageCollectorGuide guide) {
        if (!isValidImage(imageSrc))
            return;
        File imageDest = generateImageDest();
//        Log.i(TAG, String.format("Compressing %s to %s...", imageSrc, imageDest));
        execFutures.add(executor.submit(() -> ImageCompressor.compress(imageSrc, imageDest, guide)));
        try {
            compressedImagesMap.put(new JSONArray(String.format("['%s', '%s']", imageSrc, imageDest.getName())));
        } catch (JSONException e) {
            Log.wtf(TAG, e);
        }
    }

    /* Returns dirs/files to scan for images based on the build type */
    @NonNull
    private File[] getFilesToScan() {
        File[] filesToScan;
        filesToScan = options.imagesRootDir.listFiles();
        return filesToScan != null ? filesToScan : new File[]{};
    }

    private boolean isValidImage(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.toString(), options);
        return options.outHeight > 0 && options.outWidth > 0;
    }

    private File generateImageDest() {
        File outputImage;
        Random random = new Random();
        do {
            outputImage = new File(zippedImagesDir, Math.abs(random.nextLong()) + ".jpg");
        } while (outputImage.exists());
        return outputImage;
    }

    public static class Options {
        public String imagesZipName;
        public int imageQuality;
        public File imagesRootDir;

    }
}
