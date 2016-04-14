package com.kyleduo.blogdemo.exchangefragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private FragmentTabHost mTabHost;
	private Class[] mOrigin = new Class[]{
			Holder1Fragment.class,
			Holder2Fragment.class,
			Holder3Fragment.class,
			Holder4Fragment.class,
	};

	private Class[] mFragments = new Class[]{
			Holder1Fragment.class,
			Holder2Fragment.class,
			Holder3Fragment.class,
			Holder4Fragment.class,
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.tab_content);
		mTabHost.getTabWidget().setDividerDrawable(null);

		createTabs();
	}

	private void createTabs() {
		for (int index = 0; index < 4; index++) {
			Class clz = mFragments[index];
			String tag = clz.getName();
			if (tag.equals(ExchangedFragment.class.getName())) {
				tag = tag + (index + 1);
			}
			mTabHost.addTab(mTabHost.newTabSpec(tag)
					.setIndicator(getIndicator(index)), clz, null);
		}
	}

	private View getIndicator(int index) {
		View tab = LayoutInflater.from(this).inflate(R.layout.layout_tab, null);
		boolean exchanged = mFragments[index].getName().equals(ExchangedFragment.class.getName());
		String tabText = exchanged ? "Exchanged " + (index + 1) : String.format(getString(R.string.holder_tab_pattern), index + 1);
		((TextView) tab.findViewById(R.id.tab_text)).setText(tabText);
		return tab;
	}

	public void exchange() {
		int index = mTabHost.getCurrentTab();
		if (index < 0 || index > 3) {
			return;
		}
		Class fragment = mFragments[index];
		String tag = fragment.getName();
		if (tag.equals(ExchangedFragment.class.getName())) {
			mFragments[index] = mOrigin[index];
			tag = tag + (index + 1);
		} else {
			mFragments[index] = ExchangedFragment.class;
		}

		resetTabs(tag);
	}

public void resetTabs(String oldFragmentTag) {
	if (mTabHost != null) {
		int currentTab = mTabHost.getCurrentTab();
		mTabHost.clearAllTabs();

		//noinspection TryWithIdenticalCatches
		try {
			Field mTabs = mTabHost.getClass().getDeclaredField("mTabs");
			mTabs.setAccessible(true);
			Object o = mTabs.get(mTabHost);
			if (o instanceof List) {
				((List) o).clear();
			}

			Field mLastTab = mTabHost.getClass().getDeclaredField("mLastTab");
			mLastTab.setAccessible(true);
			mLastTab.set(mTabHost, null);

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(oldFragmentTag)) {
			try {
				Fragment f = getSupportFragmentManager().findFragmentByTag(oldFragmentTag);
				if (f != null) {
					getSupportFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		createTabs();
		mTabHost.setCurrentTab(currentTab);
	}
}
}
