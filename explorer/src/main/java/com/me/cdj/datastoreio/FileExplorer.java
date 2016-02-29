package com.me.cdj.datastoreio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileExplorer extends Activity {

    private static final String TAG = "FileExplorer";

    private TextView tvPath;
    private ImageButton btBack;
    private ImageButton btHome;
    private ListView listExplorer;

    /**
     * list列表,元素为Map
     * */
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    /**
     * 适配器
     * */
    private SimpleAdapter adapter;

    /**
     * 记录当前的父文件夹
     * */
    private File currentParent;

    /**
     * 记录当前路径下的所有文件的文件数组
     * */
    private File[] currentFiles;

    /**
     * SD卡根目录
     * */
    private File root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);

        tvPath = (TextView) findViewById(R.id.tv_path);
        btHome = (ImageButton) findViewById(R.id.bt_home);
        btBack = (ImageButton) findViewById(R.id.bt_back);
        listExplorer = (ListView) findViewById(R.id.list_explorer);

        /**
         * 获取系统的SD卡的目录
         * */
        root = new File(Environment.getRootDirectory().getAbsolutePath());
        currentParent = root.getParentFile();

        /**
         * 如果SD卡存在
         * */
        if (currentParent.exists()){
            currentFiles = currentParent.listFiles();
            /**
             * 使用当前目录下的全部文件、文件夹来填充ListView
             * */
            inflateListView(currentFiles);
        }

        setListener();

    }

    private void setListener() {
        listExplorer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentFiles[position].isDirectory()) {
                    //获取用户点击的文件夹下的所有文件
                    File[] tmp = currentFiles[position].listFiles();
                    if (tmp == null || tmp.length == 0) {
                        Toast.makeText(FileExplorer.this, "当前路径不可访问或该路径下没有文件",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        //获取当前用户单击的列表对应的文件夹，设为当前的父文件夹
                        currentParent = currentFiles[position];
                        //保存当前的父文件夹的全部文件和文件夹
                        currentFiles = tmp;
                        //更新listExplorer
                        inflateListView(currentFiles);
                    }
                } else {
                    new AlertDialog.Builder(FileExplorer.this).setTitle("是否打开这个文件")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentParent != root){
                    currentParent = currentParent.getParentFile();
                    currentFiles = currentParent.listFiles();
                    inflateListView(currentFiles);
                }
            }
        });

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentParent = root.getParentFile();
                currentFiles = currentParent.listFiles();
                inflateListView(currentFiles);
            }
        });
    }

    private void inflateListView(File[] currentFiles) {
        list.clear();
        for (int i=0; i<currentFiles.length; i++){
            Map<String,Object> listItem = new HashMap<>();
            /**
             * 如果当前File是文件夹，使用folder图标；否则使用file图标
             * */
            if (currentFiles[i].isDirectory()){
                listItem.put("icon",R.drawable.folder);
            }else {
                listItem.put("icon",R.drawable.file);
            }
            listItem.put("fileName",currentFiles[i].getName());
            /*添加list项*/
            list.add(listItem);
        }
        adapter = new SimpleAdapter(this,list,R.layout.list_item,new String[]{"icon", "fileName"},new int[]{R.id.icon, R.id.file_name});
        listExplorer.setAdapter(adapter);

        tvPath.setText("当前路径为： " + currentParent.getAbsolutePath().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_explorer, menu);
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
