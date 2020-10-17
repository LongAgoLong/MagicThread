package com.leo.magicthread;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leo.magic.annotation.MagicThread;
import com.leo.magic.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private TextView mTvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvTest = findViewById(R.id.tvTest);
        mTvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress();
                Log.e("-----", "跑完了");
                delayToast();
            }
        });
    }

    @MagicThread(threadMode = ThreadMode.IO)
    public void progress() {
        for (int i = 0; i <= 10; i++) {
            //处理内存泄漏
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            Log.e("-----" + Thread.currentThread().getName(), "progress: " + i);
            showProgress(i);
            SystemClock.sleep(1000);
        }
        Log.e("-----", "跳出循环");
    }

    @MagicThread(threadMode = ThreadMode.UI)
    private void showProgress(int progress) {
        Log.e("-----" + Thread.currentThread().getName(), "showProgress: " + progress);
        mTvTest.setText(progress + "%");
    }

    @MagicThread(threadMode = ThreadMode.UI, delayMillisecond = 12000)
    public void delayToast() {
        Toast.makeText(this, "延迟成功", Toast.LENGTH_LONG).show();
    }
}
