package com.easecred.android.mockui;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.common.io.Closeables;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by android on 3/2/16.
 */
public  class CreditPhotoUtils {

    private final static String TAG = "CreditPhotoUtils";

    public  static Intent getCropImageIntent(Uri inputUri, Uri outputUri,int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        addPhotoPickerExtras(intent, outputUri);
        addCropExtras(intent, size);
        return intent;
    }

    public static Intent  getPhotoPickerIntent(Uri photoUri) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        addPhotoPickerExtras(captureIntent, photoUri);
        return captureIntent;

    }

    public static Intent getPhotoGalleryIntent(Uri photoUri) {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        addPhotoPickerExtras(galleryIntent, photoUri);
        return galleryIntent;
    }

    private static void addPhotoPickerExtras(Intent intent, Uri photoUri) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, photoUri));
    }

    public static void addCropExtras(Intent intent, int photoSize) {
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", photoSize);
        intent.putExtra("outputY", photoSize);
    }


    public static boolean savePhotoFromUriToUri(Context context, Uri inputUri, Uri outputUri,
                                                boolean deleteAfterSave) {
        if (inputUri == null || outputUri == null) {
            return false;
        }

        if(inputUri == outputUri) {
            throw new IllegalArgumentException("Input and Output URI are same");
        }

        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream = context.getContentResolver()
                    .openAssetFileDescriptor(outputUri, "rw").createOutputStream();
            inputStream = context.getContentResolver().openInputStream(
                    inputUri);

            final byte[] buffer = new byte[16 * 1024];
            int length;
            int totalLength = 0;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
                totalLength += length;
            }
            Log.v(TAG, "Wrote " + totalLength + " bytes for photo " + inputUri.toString());
        } catch (IOException | NullPointerException e) {
            Log.e(TAG, "Failed to write photo: " + inputUri.toString() + " because: " + e);
            return false;
        } finally {
            Closeables.closeQuietly(inputStream);
            Closeables.closeQuietly(outputStream);
            if(deleteAfterSave) {
                deletePhoto(context,inputUri);
            }
        }
        return true;
    }


    public static void deletePhoto(Context context,Uri inputUri) {
        context.getContentResolver().delete(inputUri, null, null);
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws FileNotFoundException {
        final InputStream imageStream = context.getContentResolver().openInputStream(uri);
        try {
            return BitmapFactory.decodeStream(imageStream);
        } finally {
            Closeables.closeQuietly(imageStream);
        }
    }

    public static boolean isCameraIntentRegistered(BaseActivity context) {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        return context.isIntentRegistered(intent);
    }
}

