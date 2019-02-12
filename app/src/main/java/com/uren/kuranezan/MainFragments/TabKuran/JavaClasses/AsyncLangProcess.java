package com.uren.kuranezan.MainFragments.TabKuran.JavaClasses;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.uren.kuranezan.Interfaces.OnEventListener;
import com.uren.kuranezan.MainActivity;
import com.uren.kuranezan.Models.QuranModels.Quran;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.CommonUtils;
import com.uren.kuranezan.Utils.DialogBoxUtil.DialogBoxUtil;
import com.uren.kuranezan.Utils.DialogBoxUtil.Interfaces.YesNoDialogBoxCallback;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static com.uren.kuranezan.Constants.StringConstants.INTERNAL_FILE_NAME_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.QURAN_TRANSLATION_TR_DIYANET;
import static com.uren.kuranezan.Constants.StringConstants.QURAN_TRANSLATION_URL_PREFIX;

public class AsyncLangProcess extends AsyncTask<Activity, Void, Quran> {

    private OnEventListener<Quran> mCallBack;
    private Exception mException;
    private String mSelectedLangIdentifier;
    private Quran quran = null;

    public AsyncLangProcess(OnEventListener callback, String selectedLangIdentifier) {
        mCallBack = callback;
        mSelectedLangIdentifier = selectedLangIdentifier;
    }

    @Override
    protected Quran doInBackground(Activity... activities) {

        Context context = activities[0];
        Activity activity = activities[0];

        checkDirectories(context, activity);
        return quran;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mCallBack != null) {
            mCallBack.onTaskContinue();
        }

    }

    @Override
    protected void onPostExecute(Quran quran) {
        super.onPostExecute(quran);

        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(quran);
            } else {
                mCallBack.onFailure(mException);
            }
        }

    }

    private void checkDirectories(final Context context, final Activity activity) {

        boolean isRawFile = false;
        boolean isInternalFile = false;
        boolean isDownloadFile = false;

        //check if it is Raw/Internal/Download
        //Raw file ??
        isRawFile = isRawFile(context);

        //Internal file ??
        if (!isRawFile) {
            isInternalFile = isInternalFile(context);
        }

        if (!isRawFile && !isInternalFile) {
            isDownloadFile = true;
        }

        if (isRawFile) {
            readFromRaw(context);
        } else {
            if (isInternalFile) {
                readFromInternal(context);
            } else {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // WORK on UI thread here
                        openDialogBox(context, activity);
                    }
                });

                downloadFile(context);
                readFromInternal(context);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // WORK on UI thread here
                        ((MainActivity) activity).stopProgressBar();
                    }
                });


            }
        }

    }

    private void openDialogBox(final Context context, final Activity activity) {

        DialogBoxUtil.showYesNoDialog(context, "", context.getResources().getString(R.string.downloadLanguagePAck), new YesNoDialogBoxCallback() {
            @Override
            public void yesClick() {
                if(CommonUtils.isNetworkConnected(context)){
                    ((MainActivity) activity).startProgressBar();
                }else{
                    Toast.makeText(context,"Internet error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void noClick() {

            }
        });

    }

    private boolean isRawFile(Context context) {
        String fileName = getInternalFileName();
        String translationList[] = context.getResources().getStringArray(R.array.translation_list);

        for (int i = 0; i < translationList.length; i++) {
            if (translationList[i].equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInternalFile(Context context) {

        String fileName = getInternalFileName();
        FileInputStream fis = null;

        try {
            fis = context.openFileInput(fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getInternalFileName() {
        return INTERNAL_FILE_NAME_PREFIX + getFileIdentifier() + ".json";
    }

    private String getFileIdentifier() {
        return mSelectedLangIdentifier.replace('.', '_');
    }

    private void readFromRaw(Context context) {
        String fileName = getInternalFileName();
        int rawNum = 0;
        if (fileName.equals(QURAN_TRANSLATION_TR_DIYANET)) {
            rawNum = R.raw.quran_translation_tr_diyanet;
        } else {
            rawNum = R.raw.quran_translation_tr_diyanet;
        }

        loadRaw(context, rawNum);
    }

    private void loadRaw(Context context, int rawNum) {
        InputStream raw = context.getResources().openRawResource(rawNum);
        Reader rd = new BufferedReader(new InputStreamReader(raw));
        // Now do the magic.
        quran = new Gson().fromJson(rd, Quran.class);
    }

    private void readFromInternal(Context context) {

        String fileName = getInternalFileName();
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
            Reader rd = new BufferedReader(new InputStreamReader(fis));
            // Now do the magic.
            quran = new Gson().fromJson(rd, Quran.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void downloadFile(Context context) {

        String endpointUrl = QURAN_TRANSLATION_URL_PREFIX + mSelectedLangIdentifier;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointUrl)
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();
            saveFileToInternal(context, response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveFileToInternal(Context context, String file) {

        String fileContents = file;
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(getInternalFileName(), Context.MODE_PRIVATE);
            fos.write(fileContents.getBytes());
            String path = context.getFilesDir() + "/" + getInternalFileName();
            Log.i("saved to", path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}