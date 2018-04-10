package com.example.cao.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    private Context context;

    private boolean isClivkAd = false;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.time_text)
    TextView timer;

    private int num = 4;
    private final String url = "https://img.xjh.me/random_img.php?type=bg&ctype=nature&return=302&device=mobile";
    private Handler mHandler = new Handler();
    private Runnable runable = new Runnable() {
        @Override
        public void run() {
            if (isClivkAd){
                return;
            }
            timer.setText("跳过 "+num+"s");
            num--;
            if (num>0){
                mHandler.postDelayed(this,1000);
            }else {
                Log.d("caorui", "handle: ");
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                WelcomeActivity.this.startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
      ButterKnife.bind(this);
      context = this;
        clearImageMemoryCache(this);
        clearImageDiskCache();
//        img = findViewById(R.id.img);
//        timer.findViewById(R.id.time_text);
        initImg();
        initTimer();
        mHandler.post(runable);
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClivkAd = true;
                Log.d("caorui", "onClick: ");
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                WelcomeActivity.this.startActivity(intent);
//                mHandler.removeCallbacks();
            }
        });
    }

    private void initTimer() {

    }

    private void initImg() {
        Glide.with(this).load(url).into(img);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isClivkAd = true;
    }

    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
// BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        finish();
    }
}
