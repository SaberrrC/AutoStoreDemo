package com.example.shanlin.facedemo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mBtFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtFace = (Button) findViewById(R.id.bt_face);
        mBtFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断授权
                checkSDKPermission();


                Intent intent = new Intent(MainActivity.this, FaceActivity.class);
                startActivity(intent);
            }
        });


    }

    private void checkSDKPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            //重新请求

        } else if (Build.VERSION.SDK_INT >= 23) {
            //如果CAMERA、WRITE_EXTERNAL_STORAGE、ACCESS_COARSE_LOCATION、READ_PHONE_STATE、READ_SMS、READ_CONTACTS等权限没有获得，需要进行动态权限申请

        }
    }
}
