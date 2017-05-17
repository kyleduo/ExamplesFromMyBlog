package com.kyleduo.digging.translucentstatusbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * for DiggingTranslucentStatusBar
 * Created by kyleduo on 2017/5/17.
 */

public class Demo3BFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frgm_demo3b, container, false);
    }
}
