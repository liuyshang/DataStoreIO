package com.anl.phone.datastoreio;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shoushuo.android.tts.ITts;
import com.shoushuo.android.tts.ITtsCallback;

import static com.shoushuo.android.tts.ITtsCallback.*;


/**
 * 需要安装手说apk
 * */
public class MainActivity extends Activity {

    private ITts ttsService;
    private boolean ttsBound;
    private EditText etContent;
    private Button btSpeech;
    private Button btSave;
    private Button btStop;

    /**
     * 声明ServiceConnection
     * */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ttsService = ITts.Stub.asInterface(service);
            ttsBound = true;

            //在应用第一个使用TTS的地方，调用下面的initialize()
            try {
                ttsService.initialize();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                ttsService.registerCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            try {
                ttsService.unregisterCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            ttsService = null;
            ttsBound = false;
        }
    };

    /**
     * 注册和响应事件
     *
     * ITtsCallback 为 TTS 服务的一个回调接口
     * */
    private ITtsCallback mCallback = new ITtsCallback.Stub(){

        @Override
        public void speakCompleted() throws RemoteException {
            ttsHandler.sendEmptyMessage(0);
        }
    };

    private Handler ttsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MainActivity.this,"文本朗读完毕",Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etContent = (EditText) findViewById(R.id.et_content);
        btSpeech = (Button) findViewById(R.id.bt_speech);
        btSave = (Button) findViewById(R.id.bt_save);
        btStop = (Button) findViewById(R.id.bt_stop);

        btSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ttsService.speak(etContent.getText().toString(),1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isRead = false;
                try {
                    isRead = ttsService.isSpeaking();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (isRead){
                    try {
                        ttsService.stop();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 处理bindService 和 unbindService
     * */
    @Override
    protected void onStart() {
        super.onStart();
        if (!ttsBound){
            String actionName = "com.shoushuo.android.tts.intent.action.InvokeTts";
            Intent intent = new Intent(actionName);
            intent.setPackage(getPackageName());
            this.bindService(intent,connection,BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        if (ttsBound){
            ttsBound = false;
            this.unbindService(connection);
        }
        super.onStop();
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
