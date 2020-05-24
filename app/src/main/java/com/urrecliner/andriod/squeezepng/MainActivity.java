package com.urrecliner.andriod.squeezepng;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.urrecliner.andriod.squeezepng.LightGrayColor.removeLightGrayColor;
import static com.urrecliner.andriod.squeezepng.Vars.currActivity;
import static com.urrecliner.andriod.squeezepng.Vars.mainContext;
import static com.urrecliner.andriod.squeezepng.Vars.utils;

public class MainActivity extends AppCompatActivity {

    static File sourceFolder, targetFolder, sourceFile, targetFile;
    String nowName;
    TextView tVText2, tVText3;
    static TextView tvProgress;
    int timeAfter = 3000;

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
        utils.appendText("Start with "+sourceFolder.toString()+" > "+targetFolder.toString());
        tvProgress = findViewById(R.id.progress);
        tVText2 = findViewById(R.id.text2);
        tVText3 = findViewById(R.id.text3);
        tVText3.setText(sourceFolder.getName()+" => "+targetFolder.getName());

        Button btn00 = findViewById(R.id.button00);
        btn00.setText("흑백 강조 후 세로 폭 줄이기");
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
                btn03.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark,mainContext.getTheme()));
                clickDumpSourcePartial(xBase, yBase, delta);
                btn03.setBackgroundColor(getResources().getColor(android.R.color.darker_gray,mainContext.getTheme()));
            }
        });

        final Button btn04 = findViewById(R.id.button04);
        String s4 = "04 더 하얗게, 더 까맣게 ";
        btn04.setText(s4);
        btn04.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
//                clickDumpSource();

                btn04.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark,mainContext.getTheme()));
                new Timer().schedule(new TimerTask() {
                    public void run () {
                        LightGrayColor.loop_removeLightGrayColor();
                        btn04.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light,mainContext.getTheme()));
                    }
                }, 0);
            }
        });


        final Button btn05 = findViewById(R.id.button05);
        String s5 = "05 vertical 라인 remove ";
        btn05.setText(s5);
        btn05.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn05.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark,mainContext.getTheme()));
                new Timer().schedule(new TimerTask() {
                    public void run () {
                        VerticalDup.loop_removeVerticalDup();
                        btn05.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light,mainContext.getTheme()));
                    }
                }, 0);
            }

        });

        final Button btn06 = findViewById(R.id.button06);
        String s6 = "06 Timer Test ";
        btn06.setText(s6);

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                btn06.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark, mainContext.getTheme()));
                btn06.setText("" + timeAfter);
                btn06.invalidate();
                btn06.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light, mainContext.getTheme()));
            }
        };


        btn06.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler1 = new Handler();
                timeAfter = 5000;
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                        timeAfter -= 300;
                        Log.w("timer","now "+timeAfter);
                    }
                }, timeAfter);

            }
        });

        final int [] org = new int [10];
        int [] lup = new int [10];
        Button btn09 = findViewById(R.id.button09);
        btn09.setText("array Text");
        btn09.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 10; i++)
                    org [i] = i;
                int [] clo = org.clone();
                int [] cpy = new int [10];
                System.arraycopy(org,0,cpy,0,10);
                clo[3] = 123;
                cpy[4] = 456;
                String s = ""; for (int i = 0; i < 10; i++) s += org[i]+",";
                String s2 = ""; for (int i = 0; i < 10; i++) s2 += clo[i]+",";
                String s3 = ""; for (int i = 0; i < 10; i++) s3 += cpy[i]+",";
                Log.w("s1",s);
                Log.w("s2",s2);
                Log.w("s3",s3);

            }
        });


//        Button btn09 = findViewById(R.id.button09);
//        btn09.setText("deleteLogFile");
//        btn09.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                utils.deleteLogFile();
//                Toast.makeText(mainContext," log deleted",Toast.LENGTH_LONG).show();
//            }
//        });


        //        final SimpleDateFormat timeLogFormat = new SimpleDateFormat("MM/dd HH:mm:ss", Locale.ENGLISH);
//        String s = timeLogFormat.format(new Date());
//        long timel = System.currentTimeMillis();
////        clickButton00();
//        clickDumpSource();
//        s += " >> "+timeLogFormat.format(new Date())+ "\nElapsed sec "+(System.currentTimeMillis()-timel)/60000;
//        tVText2.setText(s);
    }

    private void clickButton00() {
        String [] currFiles;
        String nowFile;
        Bitmap inpMap, outMap;
//                TextView tv = findViewById(R.id.startNumber);
        currFiles = utils.getCurrentFileNames(sourceFolder);

        for (int idx = 0; idx < currFiles.length; idx++) {
            nowFile = currFiles[idx];
            sourceFile = new File(sourceFolder, nowFile);
//            String tgt = "" + Integer.parseInt(nowFile.substring(1,4));
//            targetFile = new File(targetFolder, tgt+".pngz");
            nowName = sourceFile.getName();
            targetFile = new File(targetFolder, "a"+nowName);
            utils.appendText(" target ------- "+targetFile);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            inpMap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);
            Bitmap bitmap = removeLightGrayColor(inpMap);
            outMap = removeBlankNorLine(bitmap);
            utils.writeBitMap(outMap, targetFile);
        }
        utils.dingDone();
        TextView tv = findViewById(R.id.progress);
        tv.setText("Done..");
    }

    private void clickDumpSource() {
        String [] currFiles;
        Bitmap inpMap;
        currFiles = utils.getCurrentFileNames(sourceFolder);
        utils.deleteLogFile();
        for (String currFile : currFiles) {
            nowName = currFile;
            tVText2.setText(tVText2.getText().toString() + " " + nowName);
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
        String [] currFiles;
        Bitmap inpMap;
        currFiles = utils.getCurrentFileNames(sourceFolder);
        utils.deleteLogFile();
        for (String currFile : currFiles) {
            nowName = currFile;
            tVText2.setText(tVText2.getText().toString() + " " + nowName);
            sourceFile = new File(sourceFolder, nowName);
            targetFile = new File(targetFolder, nowName);
            inpMap = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
            SourceDump.dumpSourceMapPartial(inpMap, xBase, yBase, delta);
            TextView tv = findViewById(R.id.progress);
            tv.setText(nowName+" Done..");
            tv.invalidate();
            utils.appendText("Done");
        }
        Toast.makeText(mainContext," source partial Dumped (X: "+xBase+", Y: "+yBase+")" ,Toast.LENGTH_LONG).show();
        utils.dingDone();
    }

    private Bitmap removeBlankNorLine(Bitmap inpMap) {
        Matrix matrix = new Matrix();
        Bitmap outMap = inpMap.copy(Bitmap.Config.ARGB_8888, true);
        int xSize = inpMap.getWidth();
        int ySize = inpMap.getHeight();
        int nonZeroTolerance = xSize / 60;
        int [] upLine = new int[4000];
        int tgtY = 0;
        boolean blankLine = false;
        utils.appendText(  nowName + " x:" + xSize + ", y:" + ySize);
        int ySame = ySize * 4/ 5;
        for (int yp = 0; yp < ySize; yp++) {
            int nonZero = 0;
            int sameWithUpCnt = 0;
            int xColorChanged = 0;
            int xLeftColor = -123;
            for (int xp = 0; xp < xSize; xp++) {
                int nowColor = inpMap.getPixel(xp, yp) & 0xffffff;
                if (nowColor != xLeftColor) {
                    xColorChanged++;
                    xLeftColor = nowColor;
                }
                outMap.setPixel(xp, tgtY, nowColor);
                if (nowColor != 0xffffff && nowColor != 0x0f0f0f)
                    nonZero++;
                if (nowColor == upLine[xp])
                    sameWithUpCnt++;
                upLine[xp] = nowColor;
            }
            if (sameWithUpCnt < ySame) {
                if (nonZero > nonZeroTolerance) {
                    tgtY++;
                    blankLine = false;
                }
                else {
                    if (xColorChanged < 8) {
                    }
                    else {
                        if (!blankLine) {
                            tgtY++;
                            blankLine = true;
                        }
                    }
                }
            }
        }
        utils.appendText(  nowName + " x:" + xSize + ", y:" + ySize + " > "+tgtY + " -"+(ySize-tgtY));
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
        for (int yp = 110; yp < ySize; yp++) {  // 120 is normal for hymn
            // 110 start : 241, 311 hymn

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
            outYp = ySize;
            utils.appendText("yStart is Zero "+nowName+" @@@@@@@@@@@@@@@@");
        }
        return Bitmap.createBitmap(outMap, 0, 0, xSize, outYp, matrix, false);
    }
}

