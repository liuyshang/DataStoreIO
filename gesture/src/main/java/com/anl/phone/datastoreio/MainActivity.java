package com.anl.phone.datastoreio;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private GestureDetector mGesture;
    private ImageView image;
    private Bitmap mBitmap;
    private int width,height;
    private float currentScale = 1;
    private Matrix matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);
        mBitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.i);
        matrix = new Matrix();
        width = mBitmap.getWidth();
        height = mBitmap.getHeight();

        mGesture = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                velocityX = velocityX > 4000 ? 4000 : velocityX;
                velocityX = velocityX < -4000 ? -4000 : velocityX;

                currentScale += currentScale * velocityX / 4000.0f;
                //保证currentScale不会等于0
                currentScale = currentScale > 0.01 ? currentScale : 0.01f;
                //重置Matrix
                matrix.reset();
                //缩放Matrix
                matrix.setScale(currentScale,currentScale);
                BitmapDrawable tmp = (BitmapDrawable) image.getDrawable();
                //如果图片还未回收，先强制回收该图片
                if (!tmp.getBitmap().isRecycled()){
                    tmp.getBitmap().recycle();
                }
                //根据原始位图和Matrix创建新图片
                Bitmap bitmap = Bitmap.createBitmap(mBitmap,0,0,width,height,matrix,true);
                //显示新的位图
                image.setImageBitmap(bitmap);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGesture.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
