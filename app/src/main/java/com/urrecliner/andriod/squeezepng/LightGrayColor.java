package com.urrecliner.andriod.squeezepng;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;

import java.io.File;

import static com.urrecliner.andriod.squeezepng.MainActivity.sourceFile;
import static com.urrecliner.andriod.squeezepng.MainActivity.sourceFolder;
import static com.urrecliner.andriod.squeezepng.MainActivity.targetFile;
import static com.urrecliner.andriod.squeezepng.MainActivity.targetFolder;
import static com.urrecliner.andriod.squeezepng.Utils.showProgress;
import static com.urrecliner.andriod.squeezepng.Vars.nowName;
import static com.urrecliner.andriod.squeezepng.Vars.utils;

class LightGrayColor {

    static String fileHandled;
    static void loop_removeLightGrayColor() {
        String [] currFiles;
        String nowFile;
        Bitmap inpMap, outMap;
        currFiles = utils.getCurrentFileNames(sourceFolder);
        fileHandled = "";

        for (String currFile : currFiles) {
            nowFile = currFile;
            fileHandled = fileHandled +"\n"+nowFile;
            Message msgText = Message.obtain(); msgText.obj = fileHandled;
            showProgress.sendMessage(msgText);
            sourceFile = new File(sourceFolder, nowFile);
            nowName = sourceFile.getName();
            targetFile = new File(targetFolder, nowName);
            utils.appendText(" target --- " + nowName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            inpMap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);
            outMap = removeLightGrayColor(inpMap);
            utils.writeBitMap(outMap, targetFile);
        }
        Message msgText = Message.obtain(); msgText.obj = "Done ..";
        showProgress.sendMessage(msgText);
        utils.appendText("Done");
        utils.dingDone();
    }

    static Bitmap removeLightGrayColor(Bitmap inpMap) {
        int whiteColor = 0xffffffff, darkColor = 0xff6f6f6f;

        Bitmap outMap = inpMap.copy(Bitmap.Config.ARGB_8888, true);
        int xSize = inpMap.getWidth();
        int ySize = inpMap.getHeight();
        int whiteCnt = 0, blackCnt = 0, whiteNear = 0, blackNear = 0, noneCnt = 0;

        for (int yp = 0; yp < ySize; yp++) {
            for (int xp = 0; xp < xSize; xp++) {
                int nowColor = inpMap.getPixel(xp, yp);

//                if (ColorDetect.isNearWhite(nowColor)) {
//                    nowColor = whiteColor;
//                    whiteNear++;
//                }
//                else if (ColorDetect.isNearBlack(nowColor)) {
//                    nowColor = darkColor;
//                    blackNear++;
//                }
//                else {
                    int redC = nowColor & 0xFF0000;
                    int greenC = nowColor & 0xFF00;
                    int blueC = nowColor & 0xFF;

                    if (redC < 0x900000 & greenC < 0x9000 & blueC < 0x90) {
                        nowColor = darkColor;
                        blackCnt++;
                    } else if (redC > 0xC00000 & greenC > 0xC000 & blueC > 0xC0) {
                        nowColor = whiteColor;
                        whiteCnt++;
                    } else {
                        nowColor = 0;
                        noneCnt++;
                    }
//                }
                outMap.setPixel(xp, yp, nowColor);
            }
        }
        utils.appendText("whiteCnt="+whiteCnt+" blackCnt="+blackCnt+" whiteNear="+whiteNear+" blackNear="+blackNear+" none="+noneCnt);
        return outMap;
    }

}
