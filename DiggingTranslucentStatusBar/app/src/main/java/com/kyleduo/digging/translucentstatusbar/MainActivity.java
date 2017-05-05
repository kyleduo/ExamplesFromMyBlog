package com.kyleduo.digging.translucentstatusbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.digging.translucentstatusbar.widgets.OnItemClickListener;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.main_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.main_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.main_appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.main_status_bar_stub)
    ViewStub mStatusBarStub;
    View mStatusBarOverlay;

    @BindView(R.id.main_drawer)
    LinearLayout mDrawer;
    @BindView(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_burger));
        }

        mCollapsingToolbarLayout.setExpandedTitleColor(0x00FFFFFF);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new DummyAdapter(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == 0) {
                    startActivity(new Intent(MainActivity.this, Demo1Activity.class));
                } else if (position == 1) {
                    startActivity(new Intent(MainActivity.this, Demo2Activity.class));
                }
            }
        }));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            mStatusBarStub.inflate();
            mStatusBarOverlay = findViewById(R.id.main_status_bar_overlay);
            mStatusBarOverlay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mStatusBarOverlay.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    ViewGroup.LayoutParams layoutParams = mStatusBarOverlay.getLayoutParams();
                    layoutParams.height = mToolbar.getPaddingTop();
                }
            });
        }
    }

    @OnClick(R.id.main_header)
    public void clickHeader() {
        startActivity(new Intent(this, SecondActivity.class));
    }


    static class DummyItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1)
        TextView tv;

        public DummyItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static class DummyAdapter extends RecyclerView.Adapter<DummyItemViewHolder> {

        private OnItemClickListener mOnItemClickListener;

        private DummyAdapter(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public DummyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final DummyItemViewHolder holder = new DummyItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, holder, holder.getAdapterPosition());
                    }
                }
            });
            return holder;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(DummyItemViewHolder holder, int position) {
            if (position == 0) {
                holder.tv.setText("CTL titleEnable == true");
            } else if (position == 1) {
                holder.tv.setText("No header. Has TabLayout");
            } else {
                holder.tv.setText(String.format(Locale.getDefault(), "Item: %d", position));
            }
        }

        @Override
        public int getItemCount() {
            return 40;
        }
    }
}
