package com.jscheng.srich.image_loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.media.ExifInterface;
import android.util.Size;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created By Chengjunsen on 2019/3/21
 */
public class DownSampler {

    private static final int MARK_POSITION = 10 * 1024 * 1024;

    public static Size getDimensions(BufferedInputStream is, BitmapFactory.Options options) {
        options.inJustDecodeBounds = true;
        decodeStream(is, options);
        options.inJustDecodeBounds = false;
        return new Size(options.outWidth, options.outHeight);
    }

    public static boolean isScaling(BitmapFactory.Options options) {
        return options.inTargetDensity > 0 && options.inDensity > 0
                && options.inTargetDensity != options.inDensity;
    }

    public static Bitmap decodeStream(BufferedInputStream is, BitmapFactory.Options options) {
        if (options.inJustDecodeBounds) {
            is.mark(MARK_POSITION);
        }
        final Bitmap result = BitmapFactory.decodeStream(is, null, options);
        if (options.inJustDecodeBounds) {
            try {
                is.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static int getRotate(BufferedInputStream is) {
        try {
            int orientation = getOrientation(is);
            return getExifOrientationDegrees(orientation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getOrientation(BufferedInputStream is) throws IOException {
        ExifInterface exifInterface = null;
        is.mark(MARK_POSITION);
        exifInterface = new ExifInterface(is);
        int result = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        try {
            is.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result == ExifInterface.ORIENTATION_UNDEFINED) {
            return 0;
        }
        return result;
    }

    public static int getExifOrientationDegrees(int exifOrientation) {
        final int degreesToRotate;
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_TRANSPOSE:
            case ExifInterface.ORIENTATION_ROTATE_90:
                degreesToRotate = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                degreesToRotate = 180;
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
            case ExifInterface.ORIENTATION_ROTATE_270:
                degreesToRotate = 270;
                break;
            default:
                degreesToRotate = 0;
                break;
        }
        return degreesToRotate;
    }
}
