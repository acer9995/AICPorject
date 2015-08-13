package com.aic.aicdetactor.data;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class T_Route {
	//巡检名称，在ListView 中显示的巡检路径名称
	 public  String Name="";
	 //巡检GUID,同时是JSON的文件名称
	 public  String Guid="";
	 //文件的存储路径
	 public String Path ="";
	 
	 public List<Object> mStationList = null;
	 public JSONArray mStationArrary = null;
	 public JSONArray mWorkerArrary = null;
	 public JSONArray mTurnArrary = null;
	 
	 public JSONObject mGloableObject = null;
	 public JSONObject mLineObject = null;
	 public LineInfo mLineInfo = null;
	 public GlobalInfo mGlobalInfo = null;
	 public List<TurnInfo> mTurnList = null;
	 public List<WorkerInfo> mWorkerList = null;
     public JSONArray mT_PeriodArray = null;
	public void parseBaseInfo() {
		if (mWorkerArrary != null) {
			try {

				mWorkerList = MyJSONParse.parseWorkerNode(mWorkerArrary);
				Log.e("luotest", "parseBaseInfo() mWorkerList is" + mWorkerList.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 if(mLineObject != null){
		 try {
			 mLineInfo = MyJSONParse.parseRouteNameNode(mLineObject);
			 Name = mLineInfo.Name;
			// mLineInfo.T_Content_Guid;
			 Guid=  mLineInfo.T_Line_Guid;
		 } catch (JSONException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		 }
		if (mTurnArrary != null) {
			try {
				mTurnList = MyJSONParse.parseTurnNode(mTurnArrary);
				Log.e("luotest", "parseBaseInfo() mTurnList is" + mTurnList.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
