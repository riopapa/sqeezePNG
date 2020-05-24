package com.urrecliner.andriod.squeezepng;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Message;

import java.io.File;

import static com.urrecliner.andriod.squeezepng.MainActivity.sourceFile;
import static com.urrecliner.andriod.squeezepng.MainActivity.sourceFolder;
import static com.urrecliner.andriod.squeezepng.MainActivity.targetFile;
import static com.urrecliner.andriod.squeezepng.MainActivity.targetFolder;
import static com.urrecliner.andriod.squeezepng.Utils.showProgress;
import static com.urrecliner.andriod.squeezepng.Vars.nowName;
import static com.urrecliner.andriod.squeezepng.Vars.utils;

class VerticalDup {

    static void loop_removeVerticalDup() {
        String [] currFiles;
        String nowFile;
        Bitmap inpMap, outMap;
        currFiles = utils.getCurrentFileNames(sourceFolder);
        for (int idx = 0; idx < currFiles.length; idx++) {
            nowFile = currFiles[idx];
            sourceFile = new File(sourceFolder, nowFile);
            nowName = sourceFile.getName();
            targetFile = new File(targetFolder, nowName);
            utils.appendText(" target ---- "+nowName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            inpMap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);
            outMap = removeVerticalDup(inpMap);
            utils.writeBitMap(outMap, targetFile);
            Message msgText = Message.obtain(); msgText.obj = idx+" ; "+nowName+" ..";
            showProgress.sendMessage(msgText);
        }
        Message msgText = Message.obtain(); msgText.obj = "Done ..";
        showProgress.sendMessage(msgText);
        utils.appendText("Done");
        utils.dingDone();
    }

    static private Bitmap removeVerticalDup(Bitmap inpMap) {
        Matrix matrix = new Matrix();
        Bitmap outMap = inpMap.copy(Bitmap.Config.ARGB_8888, true);
        int xSize = inpMap.getWidth();
        int ySize = inpMap.getHeight();
        int tgtX = 80;

        for (int xp = 0; xp < xSize-1; xp++) {
            int diffCount = 0;
            for (int yp = 0; yp < ySize; yp++) {
                int nowColor = inpMap.getPixel(xp, yp);
                int rightColor = inpMap.getPixel(xp+1, yp);
                outMap.setPixel(xp, yp, nowColor);
                if (nowColor != rightColor)
                    diffCount++;
            }
            if (diffCount > 5)
                tgtX++;
        }
        for (int xp = 0; xp <  10; xp++, tgtX = (tgtX<xSize-1)? tgtX+1:tgtX)
            for (int yp = 0; yp < ySize; yp++)
                outMap.setPixel(tgtX, yp, 0);

        utils.appendText(  nowName + " x:" + xSize  + " > "+tgtX + " -"+(xSize-tgtX));
        return Bitmap.createBitmap(outMap, 0, 0, tgtX, ySize, matrix, false);
    }
}
