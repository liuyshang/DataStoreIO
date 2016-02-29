package com.me.cdj.datastoreid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by admin on 2015/11/10.
 */
public class FileIOStream  extends Activity{

    private static final String TAG = "FileIOStream";

    private final String FILE_NAME = "fileIO.bin";
    private EditText etWrite;
    private TextView tvRead;
    private Button btWrite;
    private Button btRead;
    private Button btClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_io);

        etWrite = (EditText) findViewById(R.id.et_write);
        tvRead = (TextView) findViewById(R.id.tv_read);
        btWrite = (Button) findViewById(R.id.bt_write);
        btRead = (Button) findViewById(R.id.bt_read);
        btClear = (Button) findViewById(R.id.bt_clear);

    }


    /**
     * MODE_PRIVATE: 该文件只能被当前程序读写
     * MODE_APPEND:  已追加方式打开该文件，应用程序可以向该文件中追加内容
     * MODE_WORLD_READABLE: 该文件的内容可以被其他程序读取
     * MODE_WORLD_WRITEABLE: 该文件的内容可由其他程序读、写
     * */
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_read:
                try {
                    /*打开文件输入流*/
                    FileInputStream fis = openFileInput(FILE_NAME);
                    byte[] buff = new byte[1024];
                    int hasRead = 0;
                    StringBuilder sb = new StringBuilder("");
                    /*读取文件内容*/
                    while ((hasRead = fis.read(buff)) > 0){
                        sb.append(new String(buff, 0, hasRead));
                    }
                    /*关闭文件输入流*/
                    fis.close();
                    tvRead.setText(sb);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_write:
                String content = etWrite.getText().toString();
                try {
                    /*以追加模式打开文件输出流*/
                    FileOutputStream fos = openFileOutput(FILE_NAME,MODE_APPEND);
                    /*将FiliOutputStream包装成PrintStream*/
                    PrintStream ps = new PrintStream(fos);
                    /*输出文件内容*/
                    ps.println(content);
                    /*关闭文件输出流*/
                    ps.close();
                    Toast.makeText(this,"写入成功",Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_clear:
                etWrite.setText("");
                etWrite.requestFocus();
                break;
            default:
                break;
        }
    }
}
