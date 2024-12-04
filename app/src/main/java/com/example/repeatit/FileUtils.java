package com.example.repeatit;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {

    public static File getFileFromUri(Context context, Uri uri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();

            // Ambil nama file dari URI
            String fileName = getFileNameFromUri(contentResolver, uri);

            // Salin data dari URI ke file sementara
            File tempFile = new File(context.getCacheDir(), fileName != null ? fileName : "temp_file");
            InputStream inputStream = contentResolver.openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getFileNameFromUri(ContentResolver contentResolver, Uri uri) {
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        String fileName = null;
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (cursor.moveToFirst()) {
                fileName = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return fileName;
    }
}


