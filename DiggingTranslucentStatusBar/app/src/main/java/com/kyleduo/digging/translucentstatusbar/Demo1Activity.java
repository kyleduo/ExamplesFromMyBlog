package com.kyleduo.digging.translucentstatusbar;

import android.view.MenuItem;

/**
 * for DiggingTranslucentStatusBar
 * Created by kyleduo on 2017/5/5.
 */

public class Demo1Activity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.act_demo1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
