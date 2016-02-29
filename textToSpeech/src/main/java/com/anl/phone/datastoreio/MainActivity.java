package com.anl.phone.datastoreio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextToSpeech tts;
    private EditText etContent;
    private Button btSpeech;
    private Button btSave;
    private String path;
    private File fileDir;
    private File fileSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etContent = (EditText) findViewById(R.id.et_content);
        btSpeech = (Button) findViewById(R.id.bt_speech);
        btSave = (Button) findViewById(R.id.bt_save);
        path = Environment.getExternalStorageDirectory().getPath() + File.separator
                + "DataStoreIO";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd_hh:mm:ss");
        final String time = format.format(new Date(System.currentTimeMillis()));
        fileDir = new File(path);
        fileSpeech = new File(path + File.separator + time + "wav");
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }

        //初始化TextToSpeech对象
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                //如果装载TTS引擎成功
                 int result = tts.setLanguage(Locale.ENGLISH);
                //如果不支持所设置的语言
                if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                        && result != TextToSpeech.LANG_AVAILABLE){
                    Toast.makeText(MainActivity.this,"TTS暂时不支持这种语言的朗读",Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        btSpeech.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                //执行朗读
                tts.speak(etContent.getText().toString(),TextToSpeech.QUEUE_ADD,null,null);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                tts.synthesizeToFile(etContent.getText().toString(), null,fileSpeech,null);
                Toast.makeText(MainActivity.this,"声音记录成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭TextToSpeech
        if (tts != null){
            tts.shutdown();
        }
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
