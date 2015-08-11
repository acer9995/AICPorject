package com.aic.aicdetactor.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.TemporaryDataBean;
import com.aic.aicdetactor.database.TemporaryRouteDao;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class setting_Fragment extends Fragment {
	ListView mListView = null;
	private RadioGroup mRadioGroup = null; 
	private int mRadioIndex = 0;
	private RadioButton mRadioButton_left = null;
	private List<Map<String, String>> mItemDatas = null;
	private final String TAG = "luotest";
	private SimpleAdapter mListViewAdapter = null;
	//GridView mGridView = null;
	//TabHost mTabHost = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.setting, container, false);
		
		return view;
	}
	
}
