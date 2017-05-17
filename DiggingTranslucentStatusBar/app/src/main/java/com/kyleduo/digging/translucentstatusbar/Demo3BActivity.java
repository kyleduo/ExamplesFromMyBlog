package com.kyleduo.digging.translucentstatusbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * for DiggingTranslucentStatusBar
 * Created by kyleduo on 2017/5/11.
 */

public class Demo3BActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_demo3);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.demo3_fragment_container, new Demo3BFragment())
                .commit();
    }
}
