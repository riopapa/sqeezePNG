package com.urrecliner.andriod.squeezepng;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static com.urrecliner.andriod.squeezepng.MainActivity.tvProgress;
import static com.urrecliner.andriod.squeezepng.Vars.mainContext;
import static com.urrecliner.andriod.squeezepng.Vars.utils;

class Utils {

    final String PREFIX = "log_";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd", Locale.ENGLISH);
    private final SimpleDateFormat timeLogFormat = new SimpleDateFormat("MM/dd HH:mm:ss", Locale.ENGLISH);
    private final SimpleDateFormat jpegTimeFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.ENGLISH);
    private int appendCount = 0;

    public void appendText(String textLine) {
        File directory = getPackageDirectory();
        try {
            if (!directory.exists()) {
                boolean result = directory.mkdirs();
                Log.e("Directory",  directory.toString() + " created " + result);
            }
        } catch (Exception e) {
            Log.e("Directory", "Create error " + directory.toString() + "_" + e.toString());
        }

        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            File file = new File(directory, PREFIX + dateFormat.format(new Date())+".csv");
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Log.e("createFile", " Error");
                }
            }
            StackTraceElement[] traces;
            traces = Thread.currentThread().getStackTrace();
            String outText = timeLogFormat.format(new Date())  + " " + traces[5].getMethodName() + " > " + traces[4].getMethodName() + " > " + traces[3].getMethodName() + " #" + traces[3].getLineNumber() + " " + textLine + "\n";
            // true = append file
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(outText);
            Log.w("append " + appendCount++, outText);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) bw.close();
                if (fw != null) fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File getPublicCameraDirectory() {
        // Get the directory for the user's public pictures directory.
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM),"/Camera");
    }

    static SimpleDateFormat imgDateFormat;

    static {
        imgDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);
    }

    private  String getAppLabel(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getApplicationInfo().packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "Unknown");
    }

    private File getPackageDirectory() {
        File directory = new File(Environment.getExternalStorageDirectory(), getAppLabel(mainContext));
        try {
            if (!directory.exists()) {
                if(directory.mkdirs()) {
                    Log.e("mkdirs","Failed "+directory);
                }
            }
        } catch (Exception e) {
            Log.e("creating Dir error", directory.toString() + "_" + e.toString());
        }
        return directory;
    }

    String[] getCurrentFileNames(File fullPath) {
        File [] currFullFiles = getCurrentFileList(fullPath);
        String [] currFiles = new String[currFullFiles.length];
        for (int i = 0; i < currFiles.length; i++)
            currFiles[i] = currFullFiles[i].getName();
        return currFiles;
//        return nbrStringSort(currFiles);
    }

    private File[] getCurrentFileList(File fullPath) {
        return fullPath.listFiles();
    }

    String [] nbrStringSort(String [] inp) {
        int len = inp.length;
        String [] working = new String [len];
        String [] result = new String [len];
        for (int i = 0; i < len; i++)
            working[i] = "0000".substring(inp[i].length()-4)+inp[i]+";"+inp[i];
        Arrays.sort(working);
        for (int i = 0; i < len; i++) {
            String[] split = working[i].split(";");
            result[i] = split[1];
        }
        return result;
    }
    void deleteLogFile() {
        File directory = utils.getPackageDirectory();
        File file = new File(directory, PREFIX + dateFormat.format(new Date())+".txt");
        file.delete();
    }

    void writeBitMap(Bitmap outMap, File targetFile) {
        FileOutputStream os;
        try {
            os = new FileOutputStream(targetFile);
            outMap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (IOException e) {
            utils.appendText("Create ioException\n" + e);
        }
    }

    void dingDone() {
        MediaPlayer mp = MediaPlayer.create(mainContext, R.raw.inform);
        mp.start();

    }

    static Handler showProgress = new Handler() {
        public void handleMessage(Message msg) {
            String text =msg.obj.toString();
            Toast.makeText(mainContext, text, Toast.LENGTH_SHORT).show();
            tvProgress.setText(text);
            tvProgress.invalidate();
        }
    };

}
