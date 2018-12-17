package com.eraz.camera.camera;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class ImageViewUtils {

    private static String mFileName;

    public static Bitmap makeNewBitmap(final String pImagePath) {

        int orientation = ExifInterface.ORIENTATION_NORMAL;
        try {
            final ExifInterface exif = new ExifInterface(pImagePath);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Decode the JPEG file into a Bitmap */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(pImagePath, options);
        final Matrix matrix = new Matrix();
        if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
            matrix.postRotate(180);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            matrix.postRotate(270);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else if (orientation == ExifInterface.ORIENTATION_UNDEFINED) {
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else if (orientation == ExifInterface.ORIENTATION_NORMAL) {
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    public static File getOutputMediaFile(Context context) {
        return new File(context.getFilesDir(), "IMG_" + System.currentTimeMillis() + "iconus.jpeg");
    }

    public static String getOutputVideoFile(Context context) {
        return new File(context.getFilesDir(), "VID_" + System.currentTimeMillis() + "iconus.mp4").getAbsolutePath();
    }

    public static String getFileName() {
        return mFileName;
    }

    public static boolean saveToFile(byte[] bytes, File file) {
        boolean saved = false;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
            saved = true;
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }
        return saved;
    }

    public static String getPathFromByteArray(Context context, byte[] data, int angle) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//
//        //Bitmap bitmap = ImageViewUtils.makeNewBitmap(savePictureToFileSystem(bitmap.getb));
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
//        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,
//                System.currentTimeMillis() + ".jpeg", null);

        return savePictureToFileSystem(context, data);
    }

    public static Bitmap getPictureBitmap(final byte[] data, final int angle) {
        Bitmap bitmap = null;

        final Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        if (data != null) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (angle != 0) {
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        }

        return bitmap;
    }

    public static String rotateImageFile(Context context, String path, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return savePictureToFileSystem(context, stream.toByteArray());
    }

    private static String savePictureToFileSystem(Context context, byte[] data) {
        File file = ImageViewUtils.getOutputMediaFile(context);
        boolean saved = ImageViewUtils.saveToFile(data, file);
        return saved ? file.getAbsolutePath() : null;
    }

    /**
     * Get a file content from a Uri. This will get the the content for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getRealPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file PATH.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String _getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            String wholeID = DocumentsContract.getDocumentId(contentUri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            cursor = context.getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);

            String filePath = "";

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            return filePath;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}