package com.anl.phone.datastoreio;

import android.app.Activity;
import android.app.AlertDialog;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends Activity {

    private GestureOverlayView mGesture;

    /**
     * 记录手机上已有的手势库
     * */
    private GestureLibrary mLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGesture = (GestureOverlayView) findViewById(R.id.gesture);
        //读取上一个程序所创建的手势库,,暂时只能每次加载一个
        mLibrary = GestureLibraries.fromFile(Environment.getExternalStorageDirectory().getPath()
                + File.separator + "Gesture" + File.separator + "v");

        if (mLibrary.load()){
            Toast.makeText(MainActivity.this,"手势文件装载成功",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this,"手势文件装载失败",Toast.LENGTH_SHORT).show();
        }

        //为手势编辑组件绑定事件监听器
        mGesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                //识别用户刚刚所绘制的手势
                ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
                ArrayList<String> result = new ArrayList<String>();
                //遍历所有找到的Prediciton对象
                for (Prediction pred : predictions){
                    //只有相似度大于2.0的手势才会被输出
                    if (pred.score > 2.0){
                        result.add("与手势（" + pred.name + ")相似度为" + pred.score);
                    }
                }
                if (result.size() > 0){
                    ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(MainActivity.this
                    ,android.R.layout.simple_dropdown_item_1line,result.toArray());
                    //使用一个带List的对话框来显示所有匹配的手势
                    new AlertDialog.Builder(MainActivity.this).setAdapter(adapter,null)
                            .setPositiveButton("确定",null).show();
                } else {
                    Toast.makeText(MainActivity.this,"无法找到能匹配的手势",Toast.LENGTH_SHORT).show();
                }
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
