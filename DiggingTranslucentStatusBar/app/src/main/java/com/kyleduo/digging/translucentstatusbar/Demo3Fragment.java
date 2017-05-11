package com.kyleduo.digging.translucentstatusbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * for DiggingTranslucentStatusBar
 * Created by kyleduo on 2017/5/11.
 */

public class Demo3Fragment extends Fragment {

    Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_demo3, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        AppCompatActivity act = (AppCompatActivity) getActivity();

        act.setSupportActionBar(mToolbar);
        ActionBar actionBar = act.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            //noinspection deprecation
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
        }

        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
