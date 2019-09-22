package com.urrecliner.andriod.squeezepng;

import android.graphics.Bitmap;

import static com.urrecliner.andriod.squeezepng.Vars.nowName;
import static com.urrecliner.andriod.squeezepng.Vars.utils;

public class SourceDump {

    static Bitmap dumpSourceMap(Bitmap inpMap) {
        int xSize = inpMap.getWidth();
        int ySize = inpMap.getHeight();
        int tgtY = 0;
        Bitmap outMap = inpMap.copy(Bitmap.Config.ARGB_8888, true);
        utils.appendText("Dump start, "+nowName+",_,_,_,_,_,_,_,_,_,_,_,_");
        for (int yp = 0; yp < ySize; yp++) {
            StringBuilder oneLine = new StringBuilder();
            for (int xp = 0; xp < xSize; xp++) {
                int nowColor = inpMap.getPixel(xp, yp);

                if (nowColor == 0) {
                    oneLine.append(",");
                } else if (nowColor == 0xff000000) {
                    oneLine.append(",");
                } else {
                    oneLine.append(String.format("#%08X", nowColor & 0xffffffff));
                    oneLine.append(",");
                }
                outMap.setPixel(xp, tgtY, nowColor);
            }
            tgtY++;
            utils.appendText(","+yp+": " +oneLine.toString());
        }
        utils.appendText("Dump Finish, "+nowName+",_,_,_,_,_,_,_,_,_,_,_,_");
        return outMap;
    }


    static void dumpSourceMapPartial(Bitmap inpMap, int xBase, int yBase, int delta) {
        utils.appendText("X=,"+xBase+",Y=,"+yBase+",Dump start, "+nowName);
        for (int yp = yBase; yp < yBase + delta; yp++) {
            StringBuilder oneLine = new StringBuilder();
            for (int xp = xBase; xp < xBase+delta; xp++) {
                int nowColor = inpMap.getPixel(xp, yp);
                int redC = nowColor & 0xFF0000;
                int greenC = nowColor & 0xFF00;
                int blueC = nowColor & 0xFF;

                if (redC < 0x800000 & greenC < 0x8000 & blueC < 0x80)
                    nowColor = 0xff000000;

                if (redC > 0xC00000 & greenC > 0xC000 & blueC > 0xC0)
                    nowColor = 0xffffffff;

                if (nowColor == 0) {
                    oneLine.append(",");
                } else if (nowColor == 0xff000000) {
                    oneLine.append("XX,");
                } else if (nowColor == 0xffffffff) {
                    oneLine.append(",");
                } else {
                    oneLine.append(String.format("#%08X", nowColor & 0xffffffff));
                    oneLine.append(",");
                }
//                outMap.setPixel(xp, tgtY, nowColor);
            }
            utils.appendText(","+yp+": " +oneLine.toString());
        }
        utils.appendText("Dump Finish, "+nowName+",_,_,_,_,_,_,_,_,_,_,_,_");
    }

}
