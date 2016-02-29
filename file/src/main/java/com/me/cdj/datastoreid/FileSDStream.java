package com.me.cdj.datastoreid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;


/**
 * 读写SD卡上的文件
 * */
public class FileSDStream extends Activity {

    private static final String TAG = "FileSDStream";

    private EditText etWrite;
    private TextView tvRead;
    private Button btWrite;
    private Button btRead;
    private Button btClear;
    private File dirFile;
    private File file;
    private String dirPath;
    private String filePath;
    private String SDState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_sd);

        etWrite = (EditText) findViewById(R.id.et_write);
        tvRead = (TextView) findViewById(R.id.tv_read);
        btWrite = (Button) findViewById(R.id.bt_write);
        btRead = (Button) findViewById(R.id.bt_read);
        btClear = (Button) findViewById(R.id.bt_clear);

        SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)){
            dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DataStore";
            filePath = dirPath + File.separator +  "file.txt";
            dirFile = new File(dirPath);
            file = new File(filePath);
            Log.i(TAG,"path: " + file.getAbsolutePath());
            if (!dirFile.exists()){
                dirFile.mkdirs();
            }
        }

        btClear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(FileSDStream.this,FileIOStream.class));
                return true;
            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_write:
                /*读取输入的内容*/
                String content = etWrite.getText().toString();
                if (!content.equals("")){
                    try {
                       /*以指定文件创建RandomAcessFiles对象*/
                        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
                        /*将文件记录指针移动到最后*/
                        randomAccessFile.seek(file.length());
                        /*输出文件内容*/
                        randomAccessFile.write(content.getBytes());
                        /*关闭RandomAccessFile*/
                        randomAccessFile.close();
                        Toast.makeText(this,"内容写入成功" + file.hashCode(),Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.bt_read:
                try {
                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    /*将指定输入流包装成BufferedReader*/
                    BufferedReader buff = new BufferedReader(new InputStreamReader(fileInputStream));
                    String hasRead = null;
                    StringBuilder stringBuilder = new StringBuilder("");
                    /*读取文件内容*/
                    while ((hasRead = buff.readLine()) != null){
                        stringBuilder.append(hasRead);
                    }
                    fileInputStream.close();
                    tvRead.setText(stringBuilder.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_ioutput, menu);
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
