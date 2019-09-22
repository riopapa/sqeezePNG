package com.urrecliner.andriod.squeezepng;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import static com.urrecliner.andriod.squeezepng.Vars.currActivity;
import static com.urrecliner.andriod.squeezepng.Vars.mainContext;
import static com.urrecliner.andriod.squeezepng.Vars.utils;

public class MainActivity extends AppCompatActivity {

    static File sourceFolder, targetFolder, sourceFile, targetFile;
    String nowName;
    String [] currFiles;
    TextView tvText1, tVText2, tVText3;
    int whiteColor = 0xffffffff, blackColor = 0xff000000;

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
        tvText1 = findViewById(R.id.text1);
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

        String s = "Patial Dump at position";
        final Button btn03 = findViewById(R.id.button03);
        btn03.setText(s);
        btn03.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = findViewById(R.id.input1);
                int xBase = Integer.parseInt(et.getText().toString());
                et = findViewById(R.id.input2);
                int yBase = Integer.parseInt(et.getText().toString());
                et = findViewById(R.id.input3);
                int delta = Integer.parseInt(et.getText().toString());
                btn03.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                clickDumpSourcePartial(xBase, yBase, delta);
                btn03.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

            }
        });

        final Button brighterDarker = findViewById(R.id.button04);
        String s4 = "04 더 하얗게, 더 까맣게 ";
        brighterDarker.setText(s4);
        brighterDarker.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
//                clickDumpSource();

                brighterDarker.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                String nowFile;
                Bitmap inpMap, outMap;
                currFiles = utils.getCurrentFileNames(sourceFolder);

                for (int idx = 0; idx < currFiles.length; idx++) {
                    nowFile = currFiles[idx];
                    sourceFile = new File(sourceFolder, nowFile);
                    nowName = sourceFile.getName();
                    targetFile = new File(targetFolder, nowName);
                    utils.appendText(" target ------- "+targetFile);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    inpMap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);
                    outMap = removeLightGrayColor(inpMap);
                    utils.writeBitMap(outMap, targetFile);
//                    utils.deleteLogFile();
//                    for (int yp = 2000; yp < 4000; yp++)  {
//                        String s = ",";
//                        for (int xp = 2000; xp < 4000; xp++) {
//                            if (inpMap.getPixel(xp,yp) == outMap.getPixel(xp,yp))
//                                s = s + "XX,";
//                            else
//                                s = s + ",";
//                        }
//                        utils.appendText(s);
//                    }

                }
                tvText1.setText("Done..");
                utils.appendText("Done");
                utils.sayFinished();
                brighterDarker.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            }
        });

        Button btn09 = findViewById(R.id.button09);
        btn09.setText("deleteLogFile");
        btn09.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.deleteLogFile();
                Toast.makeText(mainContext," log deleted",Toast.LENGTH_LONG).show();
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
        tvText1.setText("Done..");
        utils.appendText("Done");
    }

    private void clickDumpSource() {
        Bitmap inpMap, outMap;
        Matrix matrix = new Matrix();
        currFiles = utils.getCurrentFileNames(sourceFolder);
        utils.deleteLogFile();
        for (int idx = 0; idx < currFiles.length; idx++) {
            nowName = currFiles[idx];
            tVText2.setText(tVText2.getText().toString()+" "+nowName);
            sourceFile = new File(sourceFolder, nowName);
            targetFile = new File(targetFolder, nowName);
            inpMap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
//            Bitmap outMap = removeRedColor(inpMap);
            SourceDump.dumpSourceMap(inpMap);
//            utils.writeBitMap(outMap, targetFile);
        }
        Toast.makeText(mainContext," source Dumped",Toast.LENGTH_LONG).show();
    }

    private void clickDumpSourcePartial(int xBase, int yBase, int delta) {
        Bitmap inpMap;
        currFiles = utils.getCurrentFileNames(sourceFolder);
        utils.deleteLogFile();
        for (int idx = 0; idx < currFiles.length; idx++) {
            nowName = currFiles[idx];
            tVText2.setText(tVText2.getText().toString()+" "+nowName);
            sourceFile = new File(sourceFolder, nowName);
            targetFile = new File(targetFolder, nowName);
            inpMap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
            SourceDump.dumpSourceMapPartial(inpMap, xBase, yBase, delta);
            tvText1.setText(nowName+" Done..");
            tvText1.invalidate();
        }
        Toast.makeText(mainContext," source partial Dumped (X: "+xBase+", Y: "+yBase+")" ,Toast.LENGTH_LONG).show();
        utils.sayFinished();
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


    private Bitmap removeLightGrayColor(Bitmap inpMap) {
        Bitmap outMap = inpMap.copy(Bitmap.Config.ARGB_8888, true);
        int xSize = inpMap.getWidth();
        int ySize = inpMap.getHeight();
        int whiteCnt = 0, blackCnt = 0, whiteNear = 0, blackNear = 0, noneCnt = 0;

        for (int yp = 0; yp < ySize; yp++) {
            for (int xp = 0; xp < xSize; xp++) {
                int nowColor = inpMap.getPixel(xp, yp);

                if (ColorDetect.isNearWhite(nowColor)) {
                    nowColor = whiteColor;
                    whiteNear++;
                }
                else if (ColorDetect.isNearBlack(nowColor)) {
                    nowColor = blackColor;
                    blackNear++;
                }
                else {
                    int redC = nowColor & 0xFF0000;
                    int greenC = nowColor & 0xFF00;
                    int blueC = nowColor & 0xFF;

                    if (redC < 0x800000 & greenC < 0x8000 & blueC < 0x80) {
                        nowColor = blackColor;
                        blackCnt++;
                    } else if (redC > 0xD00000 & greenC > 0xD000 & blueC > 0xD0) {
                        nowColor = whiteColor;
                        whiteCnt++;
                    } else {
                        nowColor = 0;
                        noneCnt++;
                    }
                }
                outMap.setPixel(xp, yp, nowColor);
            }
        }
            utils.appendText("whiteCnt="+whiteCnt+" blackCnt="+blackCnt+" whiteNear="+whiteNear+" blackNear="+blackNear+" none="+noneCnt);
        return outMap;
    }

    private Bitmap removeRedColor(Bitmap inpMap) {
        int xSize = inpMap.getWidth();
        int ySize = inpMap.getHeight();
        Matrix matrix = new Matrix();
        int outYp = 0;
        int yStart = -1;
        Bitmap outMap = inpMap.copy(Bitmap.Config.ARGB_8888, true);
        tVText2.setText(tVText2.getText().toString()+" "+nowName);
        for (int yp = 110; yp < ySize; yp++) {  // 120 is normal for hymn
            // 110 start : 241, 311 hymn

            StringBuilder oneLine = new StringBuilder();
            for (int xp = 0; xp < xSize; xp++) {
                int nowColor = inpMap.getPixel(xp, yp);
                if (ColorDetect.isRedColor(nowColor)) {
                    if (yStart == -1) {
                        yStart = yp;
                    }
                    nowColor = 0;
                }
                outMap.setPixel(xp, outYp, nowColor);
            }
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

}

