package com.anl.phone.datastoreio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private EditText mEditText;
    private GestureOverlayView mGesture;
    private String mPath;
    private String[] name = new String[]{"o","c","s","v"};
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        InputMethodManager manager = (InputMethodManager) editText.getContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        manager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

        mPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "Gesture";
        File GestureFile = new File(mPath);
        if (!GestureFile.exists()){
            GestureFile.mkdirs();
        }

        mGesture = (GestureOverlayView) findViewById(R.id.gesture);
        mGesture.setGestureStrokeWidth(4);
        mGesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, final Gesture gesture) {
                //架子啊save.xml界面布局代的视图
                View saveDialog = getLayoutInflater().inflate(R.layout.dialog_save,null);
                ImageView imageView = (ImageView) saveDialog.findViewById(R.id.ig_show);
                final EditText editText = (EditText) saveDialog.findViewById(R.id.et_name);
                //根据Gesture包含的手势创建一个位图
                Bitmap bitmap = gesture.toBitmap(128,128,10,0xffff0000);
                imageView.setImageBitmap(bitmap);

                //使用对话框显示saveDialog组件
                new AlertDialog.Builder(MainActivity.this).
                        setView(saveDialog)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //获取指定文件对应的手势库
                                GestureLibrary gestureLibrary = GestureLibraries
                                        .fromFile(mPath + File.separator + name[i]);
                                //添加手势
                                gestureLibrary.addGesture(editText.getText().toString()
                                        ,gesture);
                                //保存手势库
                                gestureLibrary.save();
                                i ++;
                                if (i == 4){
                                    Toast.makeText(MainActivity.this,"已经创建4个了",Toast.LENGTH_SHORT).show();
                                    i = 0;
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
