package com.orionit.app.orion_payroll_new.dropbox;

/**
 * Created by User on 13/10/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class DownloadDropboxV2 extends AsyncTask<File, Void, File> {
    private final Context mContext;
    private final DbxClientV2 mDbxClient;
    private final Callback mCallback;
    private Exception mException;
    private String mFileDropbox;
    private String mFileLocation;

    public interface Callback {
        void onDownloadComplete(File result);
        void onError(Exception e);
    }

    public DownloadDropboxV2(Context context, DbxClientV2 dbxClient, Callback callback, String fileDropbox, String fileLocation) {
        mContext = context;
        mDbxClient = dbxClient;
        mCallback = callback;
        mFileDropbox = fileDropbox;
        mFileLocation = fileLocation;
    }

    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);
        if (mException != null) {
            mCallback.onError(mException);
        } else {
            mCallback.onDownloadComplete(result);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected File doInBackground(File... params) {
        File metadata = params[0];
        try {
            Log.w("asasa", "proses 1");
            File file = new File(mFileLocation);
            File path = Environment.getExternalStoragePublicDirectory(file.getPath());

            Log.w("asasa", "proses 2");
            // Make sure the Downloads directory exists.
            if (!path.exists()) {
                if (!path.mkdirs()) {
                    mException = new RuntimeException("Unable to create directory: " + path);
                    Log.w("asasa", "proses 3A");
                }
            } else if (!path.isDirectory()) {
                Log.w("asasa", "proses 3B");
                mException = new IllegalStateException("Download path is not a directory: " + path);
                return null;
            }

            /*
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, metadata.getName());

            // Make sure the Downloads directory exists.
            if (!path.exists()) {
                if (!path.mkdirs()) {
                    mException = new RuntimeException("Unable to create directory: " + path);
                }
            } else if (!path.isDirectory()) {
                mException = new IllegalStateException("Download path is not a directory: " + path);
                return null;
            }
            */

            // Download the file.
            try (OutputStream outputStream = new FileOutputStream(file)) {
                mDbxClient.files().download(mFileDropbox)
                        .download(outputStream);
            }

            Log.w("asasa", "proses 5");
            // Tell android about the file
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            mContext.sendBroadcast(intent);

            return file;
        } catch (DbxException | IOException e) {
            Log.w("salah", e.toString());
            mException = e;
        }

        return null;
    }
}
