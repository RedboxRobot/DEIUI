package com.delicloud.app.uidemo.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Irvin
 * on 2017/11/21 0021.
 */

public class SystemFileUtils {

    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = Environment.getExternalStorageDirectory().getPath();

    public static final String DIVIDE_COPY_FILE = "copyFile";//其他app拷贝文件

    public static String getCopyPath(Context context) {
        return getBasePath(context, DIVIDE_COPY_FILE);
    }

    public static String getBasePath(Context context, String dirName) {
        return context.getExternalFilesDir(dirName).toString() + "/";
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file://" + SDCARD + File.separator;
        String pre2 = "file://" + SDCARD_MNT + File.separator;

        if (mUriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + mUriString.substring(pre2.length());
        }
        return filePath;
    }

    public static String getAbsoluteUriPath(Context c, Uri uri) {
        String imgPath = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = new CursorLoader(c, uri, proj, null, null, null).loadInBackground();

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imgPath = cursor.getString(column_index);
            }
        }

        return imgPath;
    }

    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        final String cacheDir = "/Android/data/Deli/chat/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public static File findOrCreateDir(File parent, String dirName) {
        File directory = new File(parent, dirName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    private static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    //  4.4以上  content://com.android.providers.media.documents/document/image:3952
    //  4.4以下  content://media/external/images/media/3951
    @SuppressLint("NewApi")
    public static String uriToPath(final Context context, final Uri uri) {

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
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                try {
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
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
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            if (isHwFileUri(uri)) {
                String temp = uri.toString();
                temp = temp.replace("content://com.huawei.hidisk.fileprovider/root", "");
                temp = temp.substring(0, temp.lastIndexOf("/") + 1);
                return temp + uri.getLastPathSegment();
            }

            if (isExternalStorageUri(uri)) {
                String temp = uri.toString();
                temp = temp.substring(temp.indexOf("storage"), temp.lastIndexOf("/") + 1);
                return "/" + temp + uri.getLastPathSegment();
            }

            if (isSmartOfficeStorageUri(uri)) {
                String temp = uri.toString();
                temp = temp.substring(temp.indexOf("external_files") + "external_files".length());
                return Environment.getExternalStorageDirectory() + temp;
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    //Android 4.4以下版本自动使用该方法
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
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SystemFileUtils", uri.toString());

            String temp = uri.toString();
            if (temp.contains("storage")) {
                temp = temp.substring(temp.indexOf("storage"), temp.lastIndexOf("/") + 1);
                return "/" + temp + uri.getLastPathSegment();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isHwFileUri(Uri uri) {
        return "com.huawei.hidisk.fileprovider".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageUri(Uri uri) {
        return uri.toString().contains(Environment.getExternalStorageDirectory().getPath());
    }

    public static boolean isSmartOfficeStorageUri(Uri uri) {
        return uri.toString().contains("com.delicloud.app.smartoffice");
    }

    public static File uriToFile(Uri uri) {
        try {
            return new File(new URI(uri.toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URI fileToUri(File file) {
        return file.toURI();
    }

    public static Uri uriStringToUri(String path) {
        return Uri.parse(path);
    }

    public static Uri pathToUri(String path) {
        File pathFile = new File(path);
        Uri uri = null;
        if (pathFile.exists()) {
            uri = Uri.fromFile(pathFile);
        }
        return uri;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isFileProvider(Uri uri) {
        if (uri == null || uri.getAuthority() == null) {
            return false;
        }
        return uri.getAuthority().toLowerCase().contains("fileprovider");
    }

    public static Uri getUriFromExternalFile(String fileName) {
        final String cacheDir = "/Deli/chat/file/";
        File externalPubPath = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);

        File picPath = new File(externalPubPath, fileName);
        Uri uri = null;
        if (picPath.exists()) {
            uri = Uri.fromFile(picPath);
        }

        return uri;
    }

    //建立一个文件类型与文件后缀名的匹配表
    public static final String[][] MATCH_ARRAY = {
            //{后缀名，    文件类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };

    /**
     * 根据路径打开文件
     *
     * @param context 上下文
     * @param path    文件路径
     */
    public static void openFileByPath(Context context, String path) {
        if (context == null || path == null)
            return;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //文件的类型
        String type = "";
        for (int i = 0; i < MATCH_ARRAY.length; i++) {
            //判断文件的格式
            if (path.toString().contains(MATCH_ARRAY[i][0].toString())) {
                type = MATCH_ARRAY[i][1];
                break;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        try {
            //设置intent的data和Type属性
            intent.setDataAndType(Uri.fromFile(new File(path)), type);
            //跳转
            context.startActivity(intent);
        } catch (Exception e) { //当系统没有携带文件打开软件，提示
            Toast.makeText(context, "无法打开该格式文件!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
