package com.urrecliner.andriod.squeezepng;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import static com.urrecliner.andriod.squeezepng.Vars.currActivity;
import static com.urrecliner.andriod.squeezepng.Vars.mainContext;
import static com.urrecliner.andriod.squeezepng.Vars.utils;

public class MainActivity extends AppCompatActivity {

    static File sourceFolder, targetFolder, sourceFile, targetFile;
    String nowName;
    String [] currFiles;
    TextView tVNowFile, tVText2, tVText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        sourceFolder = new File(Environment.getExternalStorageDirectory(), "@hymngasa");
        sourceFolder = new File(Environment.getExternalStorageDirectory(), "@Input");
//        targetFolder = new File(Environment.getExternalStorageDirectory(), "myHolyBible/hymn_png");
        targetFolder = new File(Environment.getExternalStorageDirectory(), "@Output");
        utils = new Utils();
        currActivity = this;
        mainContext = this;
        tVNowFile = findViewById(R.id.text1);
        tVText2 = findViewById(R.id.text2);
        tVText3 = findViewById(R.id.text3);
        tVText3.setText(sourceFolder.getName()+" => "+targetFolder.getName());

        Button btn00 = findViewById(R.id.button00);
        btn00.setText("투명화 후 빈 가로라인 없애기");
        btn00.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickButton00();
                utils.appendText("finished");
            }
        });

        Button btn01 = findViewById(R.id.button01);
        btn01.setText("clickDumpSource");
        btn01.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDumpSource();
            }
        });


        Button btn02 = findViewById(R.id.button02);
        btn02.setText("jpg 2 png");
        btn02.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDumpSource();
            }
        });
//        final SimpleDateFormat timeLogFormat = new SimpleDateFormat("MM/dd HH:mm:ss", Locale.ENGLISH);
//        String s = timeLogFormat.format(new Date());
//        long timel = System.currentTimeMillis();
////        clickButton00();
//        clickDumpSource();
//        s += " >> "+timeLogFormat.format(new Date())+ "\nElapsed sec "+(System.currentTimeMillis()-timel)/60000;
//        tVText2.setText(s);
    }

    private void clickButton00() {
        String nowFile;
        Bitmap inpMap, outMap;
//                TextView tv = findViewById(R.id.startNumber);
        currFiles = utils.getCurrentFileNames(sourceFolder);

        for (int idx = 0; idx < currFiles.length; idx++) {
            nowFile = currFiles[idx];
            sourceFile = new File(sourceFolder, nowFile);
//            String tgt = "" + Integer.parseInt(nowFile.substring(1,4));
//            targetFile = new File(targetFolder, tgt+".pngz");
            nowName = sourceFile.getName()+"z";
            targetFile = new File(targetFolder, nowName);
            utils.appendText(" target ------- "+targetFile);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            inpMap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);
            outMap = removeRedColor(inpMap);
//            outMap = dumpSourceMap(inpMap);
            //            tgtMap = srcMap.copy(Bitmap.Config.ARGB_8888, true);
//            inpMap = BitmapFactory.decodeFile(targetFile.getAbsolutePath(), options);
//            outMap = dumpSourceMap(outMap);
            outMap = removeBlankNorLine(outMap);
            utils.writeBitMap(outMap, targetFile);
        }
        tVNowFile.setText("Done..");
        utils.appendText("Done");
    }
    private void clickDumpSource() {
        Bitmap inpMap, outMap;
        Matrix matrix = new Matrix();
        currFiles = utils.getCurrentFileNames(sourceFolder);

        for (int idx = 0; idx < currFiles.length; idx++) {
            nowName = currFiles[idx];
            sourceFile = new File(sourceFolder, nowName);
            targetFile = new File(targetFolder, nowName+"z");
            inpMap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
//            Bitmap outMap = removeRedColor(inpMap);
            outMap = dumpSourceMap(inpMap);
//            utils.writeBitMap(outMap, targetFile);
        }
    }

    private Bitmap removeBlankNorLine(Bitmap inpMap) {
        Matrix matrix = new Matrix();
        Bitmap outMap = inpMap.copy(Bitmap.Config.ARGB_8888, true);
        int xSize = inpMap.getWidth();
        int ySize = inpMap.getHeight();
        int tgtY = 0;
        boolean blankLine = false;

        for (int yp = 0; yp < ySize; yp++) {
            int nonZero = 0;
//                        StringBuilder oneLine = new StringBuilder();
            for (int xp = 0; xp < xSize; xp++) {
                int nowColor = inpMap.getPixel(xp, yp);
                outMap.setPixel(xp, tgtY, nowColor);
                if (nowColor != 0)
                    nonZero++;
            }
            if (nonZero > 9) {
                tgtY++;
                blankLine = false;
            }
            else {
                if(!blankLine) {
                    tgtY++;
                    blankLine = true;
                }
            }
        }
        utils.appendText(  nowName + " x:" + xSize + ", y:" + ySize + " > "+tgtY + " -"+(ySize-tgtY));

//        return outMap;
        return Bitmap.createBitmap(outMap, 0, 0, xSize, tgtY, matrix, false);
    }

    private Bitmap removeRedColor(Bitmap inpMap) {
        int xSize = inpMap.getWidth();
        int ySize = inpMap.getHeight();
        Matrix matrix = new Matrix();
        int outYp = 0;
        int yStart = -1;
        Bitmap outMap = inpMap.copy(Bitmap.Config.ARGB_8888, true);
        tVText2.setText(tVText2.getText().toString()+" "+nowName);
        for (int yp = 110; yp < ySize; yp++) {
            // 110 start : 241, 311
            StringBuilder oneLine = new StringBuilder();
            for (int xp = 0; xp < xSize; xp++) {
                int nowColor = inpMap.getPixel(xp, yp);
                switch (nowColor) {

                    case 0xFFFFEFEF:
                    case 0xFFFFDFDF:
                    case 0xFFFFD8D8:
                    case 0xFFFFD7D7:
                    case 0xFFFFCFCF:
                    case 0xFFFFBFBF:
                    case 0xFFFFBEBE:
                    case 0xFFFFBDBD:
                    case 0xFFFFBCBC:
                    case 0xFFFFAFAF:
                    case 0xFFFF9F9F:
                    case 0xFFFF9A9A:
                    case 0xFFFF9999:
                    case 0xFFFF9898:
                    case 0xFFFF9797:
                    case 0xFFFF8F8F:
                    case 0xFFFF8282:
                    case 0xFFFF8181:
                    case 0xFFFF8080:
                    case 0xFFFF7F7F:
                    case 0xFFFF7E7E:
                    case 0xFFFF7D7D:
                    case 0xFFFF7C7C:
                    case 0xFFFF7979:
                    case 0xFFFF7777:
                    case 0xFFFF7070:
                    case 0xFFFF6060:
                    case 0xFFFF5050:
                    case 0xFFFF4444:
                    case 0xFFFF4343:
                    case 0xFFFF4242:
                    case 0xFFFF4141:
                    case 0xFFFF4040:
                    case 0xFFFF3E3E:
                    case 0xFFFF3030:
                    case 0xFFFF2A2A:
                    case 0xFFFF2929:
                    case 0xFFFF2828:
                    case 0xFFFF2727:
                    case 0xFFFF2626:
                    case 0xFFFF2020:
                    case 0xFFFF1010:
                    case 0xFFFF0303:
                    case 0xFFFF0202:
                    case 0xFFFF0101:
                    case 0xFFFF0000:
                    case 0xFFFE2727:
                    case 0xFFFE2020:
                    case 0xFFFE1010:
                    case 0xFFFE0E0E:
                    case 0xFFFE0707:
                    case 0xFFFE0000:
                    case 0xFFFD1616:
                    case 0xFFFD0A0A:
                    case 0xFFFC0D0D:
                    case 0xFFFBFBFB:
                    case 0xFFFBFAFA:
                    case 0xFFFAFAFA:
                    case 0xFFF9F3F3:
                    case 0xFFF9F2F2:
                    case 0xFFF92F2F:
                    case 0xFFF84949:
                    case 0xFFF7F1F1:
                    case 0xFFF79696:
                    case 0xFFF6F6F6:
                    case 0xFFF6ECEC:
                    case 0xFFF6EAEA:
                    case 0xFFF6E9E9:
                    case 0xFFF6E2E2:
                    case 0xFFF6CBCB:
                    case 0xFFF69696:
                    case 0xFFF67474:
                    case 0xFFF65151:
                    case 0xFFF64A4A:
                    case 0xFFF5F5F5:
                    case 0xFFF5E5E5:
                    case 0xFFF5E1E1:
                    case 0xFFF5DBDB:
                    case 0xFFF4CCCC:
                    case 0xFFF3DBDB:
                    case 0xFFF2D0D0:
                    case 0xFFF27171:
                    case 0xFFF1F1F1:
                    case 0xFFF1F0F0:
                    case 0xFFF1DEDE:
                    case 0xFFF1CDCD:
                    case 0xFFF1B3B3:
                    case 0xFFF16F6F:
                    case 0xFFF14444:
                    case 0xFFF0D0D0:
                    case 0xFFEF7272:
                    case 0xFFECEBEB:
                    case 0xFFEC9F9F:
                    case 0xFFEBEBEB:
                    case 0xFFEBEAEA:
                    case 0xFFEBAAAA:
                    case 0xFFEB9696:
                    case 0xFFEB7474:
                    case 0xFFEAEAEA:
                    case 0xFFEAB8B8:
                    case 0xFFE7B5B5:
                    case 0xFFE6E6E6:
                    case 0xFFE4E4E4:
                    case 0xFFE3E3E3:
                    case 0xFFE3B8B8:
                    case 0xFFE1E1E1:
                    case 0xFFDDDDDD:
                    case 0xFFDCDCDC:
                    case 0xFFDCDBDB:
                    case 0xFFDBDBDB:
                    case 0xFFDADADA:
                    case 0xFFD8D7D7:
                    case 0xFFD6D6D6:
                    case 0xFFD3D3D3:
                    case 0xFFD2D1D1:
                    case 0xFFD1D0D0:
                    case 0xFFCF9999:
                    case 0xFFCD9797:
                    case 0xFFCCCCCC:
                    case 0xFFCB8B8B:
                    case 0xFFC9C9C9:
                    case 0xFFC7C7C7:
                    case 0xFFC3C3C3:
                    case 0xFFC0C0C0:
                    case 0xFFBDBDBD:
                    case 0xFFBABABA:
                    case 0xFFAEAEAE:
                    case 0xFFA4A4A4:
                    case 0xFF9C9C9C:
                    case 0xFF939393:
                    case 0xFF868686:
                    case 0xFF777777:
                    case 0xFF686868:


                        if (yStart == -1) {
//                            utils.appendText("yp "+yp+" xp "+xp+" "+String.format("#%08X",nowColor));
                            yStart = yp;
                        }
                        nowColor = 0;
                        break;
                    default:
                }
                outMap.setPixel(xp, outYp, nowColor);
            }
//            outMap.setPixel(outYp,outYp, 0xFF345678);
            if (yStart != -1) {
                outYp++;
            }
        }
        if (yStart == -1) {
            yStart = 0;
            outYp = ySize;
            utils.appendText("yStart is Zero "+nowName+" @@@@@@@@@@@@@@@@");
        }
//        utils.appendText(ySize+" >  "+outYp+" start="+yStart);
        return Bitmap.createBitmap(outMap, 0, 0, xSize, outYp, matrix, false);
    }

    private Bitmap dumpSourceMap(Bitmap inpMap) {
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
                } else {
                    oneLine.append(String.format("#%08X", nowColor & 0xffffffff));
                    oneLine.append(",");
                }
                outMap.setPixel(xp, tgtY, nowColor);
            }
            tgtY++;
            utils.appendText(yp+": " +oneLine.toString());
        }
        utils.appendText("Dump Finish, "+nowName+",_,_,_,_,_,_,_,_,_,_,_,_");
        return outMap;
    }

}
//                                if (redValue == 255) {
//                                        tgtMap.setPixel(xp, tgtY, Color.TRANSPARENT);
//                                        nowColor = 0;
//                                        oneLine.append("0 ");
