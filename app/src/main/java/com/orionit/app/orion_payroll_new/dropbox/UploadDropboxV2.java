package com.orionit.app.orion_payroll_new.dropbox;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadDropboxV2 extends AsyncTask<String, Void, FileMetadata> {

    private final Context mContext;
    private final DbxClientV2 mDbxClient;
    private final Callback mCallback;
    private Exception mException;
    private String mFileDropbox;
    private String mFileLocation;

    public interface Callback {
        void onUploadComplete(FileMetadata result);
        void onError(Exception e);
    }

    public UploadDropboxV2(Context context, DbxClientV2 dbxClient, Callback callback, String fileDropbox, String fileLocation) {
        mContext = context;
        mDbxClient = dbxClient;
        mCallback = callback;
        mFileDropbox = fileDropbox;
        mFileLocation = fileLocation;
    }

    @Override
    protected void onPostExecute(FileMetadata result) {
        super.onPostExecute(result);
        if (mException != null) {
            mCallback.onError(mException);
        } else if (result == null) {
            mCallback.onError(null);
        } else {
            mCallback.onUploadComplete(result);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected FileMetadata doInBackground(String... params) {
        File localFile = new File(mFileLocation);

        if (localFile != null) {
            try (InputStream inputStream = new FileInputStream(localFile)) {
                return mDbxClient.files().uploadBuilder(mFileDropbox)
                        .withMode(WriteMode.OVERWRITE)
                        .uploadAndFinish(inputStream);
            } catch (DbxException | IOException e) {
                mException = e;
            }
        }

        return null;
    }
}
