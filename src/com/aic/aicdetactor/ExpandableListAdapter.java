package com.aic.aicdetactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.data.KEY;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private myApplication app = null;
	private Context mContext = null;
	private int mStationIndex = 0;
	private Activity mActivity = null;
	private LayoutInflater mInflater;
	private final String TAG = "luotest";
	private List<Object> mPartItemList = null;
	
	// groupView data
	private List<Map<String, String>> mDataList = null;
	private ArrayList<ArrayList<Map<String, String>>> mChildrenList = null;
	private List<Object> deviceItemList = null;

	public ExpandableListAdapter(Activity av, Context context, int StatoinIndex) {
		mContext = context;
		mStationIndex = StatoinIndex;
		mInflater = LayoutInflater.from(mContext);
		mActivity = av;
		mDataList = new ArrayList<Map<String, String>>();
		app = ((myApplication) mActivity.getApplication());

		InitData();
		initeChild();
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mChildrenList.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		
		final TextView indexText;
		final TextView NameText;
		final TextView CheckValueText;
		final TextView DeadLineText;
		
		if (arg3 == null) {			
			arg3 = mInflater.inflate(R.layout.checkunit, null);
		}
		HashMap<String, String> map = (HashMap<String, String>) mChildrenList
				.get(arg0).get(arg1);

		
		indexText = (TextView) arg3.findViewById(R.id.index);
		NameText = (TextView) arg3.findViewById(R.id.pathname);
		CheckValueText = (TextView) arg3.findViewById(R.id.checkvalue);
		DeadLineText = (TextView) arg3.findViewById(R.id.deadtime);
		
		
		NameText.setText(map.get(CommonDef.check_item_info.NAME));
		CheckValueText.setText(map.get(CommonDef.check_item_info.VALUE));	
		DeadLineText.setText(map.get(CommonDef.check_item_info.DEADLINE));	
		
		//NameText.setTextColor(Color.RED);	
		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return mChildrenList.get(arg0).size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return mDataList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub

		final TextView indexText;
		final TextView NameText;
		final TextView DeadLineText;
		final TextView StausText;
		final TextView ProcessText;
		if (arg2 == null) {			
			arg2 = mInflater.inflate(R.layout.checkitem, null);
		}
		HashMap<String, String> map = (HashMap<String, String>) mDataList
				.get(arg0);

		indexText = (TextView) arg2.findViewById(R.id.index);
		indexText.setText(""+(arg0+1));
		NameText = (TextView) arg2.findViewById(R.id.pathname);
		NameText.setText(map.get(CommonDef.device_info.NAME).toString());
		
		DeadLineText = (TextView) arg2.findViewById(R.id.deadtime);
		DeadLineText.setText(map.get(CommonDef.device_info.DEADLINE));
		
		StausText = (TextView) arg2.findViewById(R.id.status);
		StausText.setText(map.get(CommonDef.device_info.STATUS));
		
		ProcessText = (TextView) arg2.findViewById(R.id.progress);
		ProcessText.setText(map.get(CommonDef.device_info.PROGRESS));
		
		indexText.setTextColor(Color.RED);

		return arg2;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	void InitData() {
		try {
			/**
			 * 返回的DeviceItem 下的数组list,每项包含 DeviceName ,ItemDef及PartItem
			 */

			deviceItemList = app.getDeviceItemList(mStationIndex);

			CheckStatus status = null;
			// int itemindex = mStationIndex;
			int itemindex = 0;
			mDataList.clear();
			for (int i = 0; i < deviceItemList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				status = app.getNodeCount(deviceItemList.get(i), 2, 0);
				status.setContext(mContext);
				map.put(CommonDef.device_info.INDEX, "" + (itemindex + 1));
				map.put(CommonDef.device_info.NAME,
						app.getDeviceItemName(deviceItemList.get(i)));

				map.put(CommonDef.device_info.DEADLINE, status.mLastTime);
				map.put(CommonDef.device_info.STATUS, status.getStatus());

				map.put(CommonDef.device_info.PROGRESS, status.mCheckedCount
						+ "/" + status.mSum);
				mDataList.add(map);
				itemindex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void initeChild() {
		Log.d(TAG,"initeChild() start ");
		mChildrenList = new ArrayList<ArrayList<Map<String, String>>>();
		for (int deviceIndex = 0; deviceIndex < deviceItemList.size(); deviceIndex++) {
			InitChidrenData(deviceIndex, 0, false);
		}
		Log.d(TAG,"initeChild() end ");
	}

	void InitChidrenData(int deviceIndex, int itemIndexs, boolean updateAdapter) {
		try {

			//Partitem数组中的每一项，包括Fast_Record_Item_Name 及PartItemData
			Object object = deviceItemList.get(deviceIndex);
			Log.d(TAG,"InitChidrenData() object is "+object.toString());
			mPartItemList = app.getPartItem(object,-1);
			Log.d(TAG,"InitChidrenData() mPartItemList size is "+mPartItemList.size());
			ArrayList<Map<String, String>> childList = new ArrayList<Map<String, String>>();
		
			for (int i = 0; i < mPartItemList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				Log.d(TAG,"InitChidrenData() PartItemData is "+mPartItemList.get(i).toString());
				map.put(CommonDef.check_item_info.NAME,
						app.getPartItemCheckUnitName(
								mPartItemList.get(i),
								CommonDef.partItemData_Index.PARTITEM_CHECKPOINT_NAME));
				Log.d(TAG,"InitChidrenData() Name  is "+app.getPartItemCheckUnitName(
						mPartItemList.get(i),
						CommonDef.partItemData_Index.PARTITEM_CHECKPOINT_NAME));
				map.put(CommonDef.check_item_info.DATA_TYPE,
						app.getPartItemCheckUnitName(mPartItemList.get(i),
								CommonDef.partItemData_Index.PARTITEM_DATA_TYPE));

				map.put(CommonDef.check_item_info.VALUE,
						app.getPartItemCheckUnitName(
								mPartItemList.get(i),
								CommonDef.partItemData_Index.PARTITEM_ADDITIONAL_INFO));

				map.put(CommonDef.check_item_info.DEADLINE,
						app.getPartItemCheckUnitName(
								mPartItemList.get(i),
								CommonDef.partItemData_Index.PARTITEM_ADD_END_DATE_20));

			
				childList.add(map);
			}
			mChildrenList.add(childList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		InitData();
		initeChild();
		super.notifyDataSetChanged();
	}
	
}
