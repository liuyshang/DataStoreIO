package com.me.cdj.datastoreio;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SharedPreference extends Activity {

    private static final String TAG = "SharedPreference";

    private EditText edName;
    private EditText edAge;
    private Button btWrite;
    private Button btRead;
    private Button btClear;
    private Button btDelete;
    private ListView listView;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private List<String> list = new ArrayList();
    private ArrayAdapter<String> arrayAdapter;
    private Map<String,?> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preference);
        Log.i(TAG,"onCreate");

        edName = (EditText) findViewById(R.id.et_name);
        edAge = (EditText) findViewById(R.id.et_age);
        btRead = (Button) findViewById(R.id.bt_read);
        btWrite = (Button) findViewById(R.id.bt_write);
        btClear = (Button) findViewById(R.id.bt_clear);
        btDelete = (Button) findViewById(R.id.bt_delete);
        listView = (ListView) findViewById(R.id.list_name);

        /**
         * 获取只能被本应用程序读、写的SharedPreferences对象
         *
         * mode： MODE_PRIVATE  数据只能被本应用程序读、写
         *         MODE_WORLD_READABLE  数据能被其他应用程序读，但不能写
         *         MODE_WORLD_WRITEABLE  数据能被其他应用程序读、写
         * */
        preference = getSharedPreferences("datastore", MODE_PRIVATE);
        editor = preference.edit();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
//        arrayAdapter.notifyDataSetInvalidated();

        /**
         * Sharedpreference 监听
         * */
        preference.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.i(TAG,"value: " + sharedPreferences.getString(key,""));
            }
        });
    }

    /**
     * 按钮点击监听
     *
     * @param view
     */
    public void onClick(View view){
        Log.i(TAG,"onClick");
        switch (view.getId()){
            case R.id.bt_write:
                String name = edName.getText().toString();
                String age = edAge.getText().toString();
                if (!preference.contains(name)) {
                    editor.putString(name, age);
                    editor.apply();
                    list.add(name + "  " + age);
                    Toast.makeText(this,"保存成功 " + list.size(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,"已经保存",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_read:
                /**
                 * 显示listView
                 * */
                listView.setVisibility(View.VISIBLE);

                map = preference.getAll();
                for (Map.Entry<String, ?> entry : map.entrySet()){
                    Log.i(TAG,"key: " + entry.getKey() + " value: " + entry.getValue());
                }

                Iterator<? extends Map.Entry<String, ?>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String,?> entry = iterator.next();
                    Log.i(TAG,"key= " + entry.getKey() + " and value= " + entry.getValue());
                }
                break;
            case R.id.bt_clear:
                edName.setText("");
                edAge.setText("");
                /**
                 * 获取焦点
                 * */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edName.requestFocus();
                    }
                },100);
                break;
            case R.id.bt_delete:
                editor.clear();
                editor.apply();
                list.clear();
                break;
            default:
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shared_preference, menu);
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
