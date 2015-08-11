package com.aic.aicdetactor.fragment;

import java.util.List;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.database.TemporaryRouteDao;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class DownLoadFragment extends Fragment {
	private LinearLayout mSetting_linearLayout = null; 
	private LinearLayout mUp_linearLayout = null;
	private LinearLayout mDown_linearLayout = null;
	private RadioGroup mRadioGroup = null;
  
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.downup_fragment, container, false);
		
		mSetting_linearLayout = (LinearLayout)view.findViewById(R.id.setting_linear);
		mUp_linearLayout = (LinearLayout)view.findViewById(R.id.up_linear);
		mDown_linearLayout = (LinearLayout)view.findViewById(R.id.down_linear);
		mRadioGroup = (RadioGroup)view.findViewById(R.id.downup_group);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg0.getCheckedRadioButtonId()){
				case R.id.downup_radioButton1:
					
					mUp_linearLayout.setVisibility(View.VISIBLE);
					mSetting_linearLayout.setVisibility(View.GONE);
					mDown_linearLayout.setVisibility(View.GONE);
					break;
				case R.id.downup_radioButton2:
					mUp_linearLayout.setVisibility(View.GONE);
					mSetting_linearLayout.setVisibility(View.GONE);
					mDown_linearLayout.setVisibility(View.VISIBLE);
					
					break;
				case R.id.downup_radioButton3:
					
					mSetting_linearLayout.setVisibility(View.VISIBLE);
					mUp_linearLayout.setVisibility(View.GONE);
					mDown_linearLayout.setVisibility(View.GONE);
					break;
				}
			}
			
		});	
	  
	    return view;
	}


	
}
