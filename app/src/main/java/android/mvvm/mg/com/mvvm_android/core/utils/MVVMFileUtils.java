package android.mvvm.mg.com.mvvm_android.core.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public final class MVVMFileUtils {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String onSavePicture(final byte[] data) {

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String outputFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "image_" + timeStamp + ".jpeg";

        File pictureFile = new File(outputFileName);
        if (pictureFile.exists()) {
            pictureFile.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);

            Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);

            ExifInterface exif = new ExifInterface(pictureFile.toString());

            Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
            if (Objects.requireNonNull(exif.getAttribute(ExifInterface.TAG_ORIENTATION)).equalsIgnoreCase("6")) {
                realImage = rotate(realImage, 90);
            } else if (Objects.requireNonNull(exif.getAttribute(ExifInterface.TAG_ORIENTATION)).equalsIgnoreCase("8")) {
                realImage = rotate(realImage, 270);
            } else if (Objects.requireNonNull(exif.getAttribute(ExifInterface.TAG_ORIENTATION)).equalsIgnoreCase("3")) {
                realImage = rotate(realImage, 180);
            } else if (Objects.requireNonNull(exif.getAttribute(ExifInterface.TAG_ORIENTATION)).equalsIgnoreCase("0")) {
                realImage = rotate(realImage, 90);
            }

            boolean bo = realImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.close();
            Log.d("Info", bo + "");

        } catch (FileNotFoundException e) {
            Log.d("Info", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }

        return pictureFile.getAbsolutePath();
    }

    private static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
}
