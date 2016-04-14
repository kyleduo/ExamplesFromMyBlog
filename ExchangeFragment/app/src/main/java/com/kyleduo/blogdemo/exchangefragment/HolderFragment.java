package com.kyleduo.blogdemo.exchangefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kyle on 16/4/14.
 */
public class HolderFragment extends Fragment {
	private String mHolderName;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHolderName = getName();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_holder, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		((TextView) view.findViewById(R.id.holder_tv)).setText(String.format(getString(R.string.holder_pattern), mHolderName));

		view.findViewById(R.id.holder_exchange).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exchange();
			}
		});
	}

	private void exchange() {
		if (getActivity() != null && getActivity() instanceof MainActivity) {
			((MainActivity) getActivity()).exchange();
		}
	}

	protected String getName() {
		return "HOLDER_FRAGMENT";
	}
}
