package com.aic.aicdetactor.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aic.aicdetactor.data.MyJSONParse;
import com.aic.aicdetactor.data.T_Route;
import com.aic.aicdetactor.data.TurnInfo;
import com.aic.aicdetactor.data.WorkerInfo;
import com.aic.aicdetactor.database.DBHelper.SourceTable;

public class RouteDao {
	private final String TAG ="luotest";
	DBHelper helper = null;
	private String RouteTableName = DBHelper.TABLE_SOURCE_FILE;
	private String WorkerTableName = DBHelper.TABLE_WORKERS;
	private String TurnTableName = DBHelper.TABLE_TURN;
	
	private SQLiteDatabase mDB = null;

	public RouteDao(Context cxt) {
		helper = new DBHelper(cxt);
		mDB = helper.getWritableDatabase();
	}

	/**
	 * 当Activity中调用此构造方法，传入一个版本号时，系统会在下一次调用数据库时调用Helper中的onUpgrade()方法进行更新
	 * 
	 * @param cxt
	 * @param version
	 */
	public RouteDao(Context cxt, int version) {
		helper = new DBHelper(cxt, version);
		mDB = helper.getWritableDatabase();
	}

	
	/**
	 * 根据JSON全路径，解析插入数据库
	 * @param filePath:JSON的全路径文件
	 * @return
	 */
	public int insertNewRouteInfo(String filePath) {	
	
		T_Route info = 	MyJSONParse.getPlanInfo(filePath);
		info.parseBaseInfo();
		

		Cursor cursor = null;		
		//查数据表中是否存在已有的GUID，如果没有的话 ，直接插入
		cursor = mDB.query(RouteTableName,
					null,
					DBHelper.SourceTable.PLANGUID + "=?", new String[] { info.Guid }, null, null,
					null);
		String sql = null;
		if(cursor== null || cursor.getCount()<1){ 
		 sql = "insert into "+RouteTableName
				 +"("
				 + DBHelper.SourceTable.Path+","
				+ DBHelper.SourceTable.PLANNAME+","
				+ DBHelper.SourceTable.PLANGUID+")values(?,?,?)";
		
		mDB.execSQL(sql, new Object[] { info.Path, info.Name,info.Guid});
		
		}
		//如果数据表中存在已有的GUID ，如何处理呢？  目前是不做插入
		if(cursor != null){
			cursor.close();
			}
		
		//解析工人信息到数据表中
		WorkerInfo workerinfo = null;
		
		//parse worker information from json txt
		////////////////////////////////////////////
		
		for(int i = 0;i<info.mWorkerList.size();i++){
			workerinfo = (WorkerInfo) info.mWorkerList.get(i);
			cursor = mDB.query(WorkerTableName,
					null,
					DBHelper.Plan_Worker_Table.Mumber + "=?", new String[] { workerinfo.Mumber }, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
				 sql = "insert into "+WorkerTableName
						 +"(" 
						 + DBHelper.Plan_Worker_Table.Name+","				 
						 + DBHelper.Plan_Worker_Table.Alias_Name+","
						 + DBHelper.Plan_Worker_Table.Class_Group+","
						 + DBHelper.Plan_Worker_Table.Mumber+","
						 + DBHelper.Plan_Worker_Table.T_Line_Content_Guid+","
						 + DBHelper.Plan_Worker_Table.T_Line_Guid+ ","
						 + DBHelper.Plan_Worker_Table.T_Organization_Guid
						 +")values(?,?,?,?,?,?,?)";
						
			
				mDB.execSQL(sql, new Object[] {
						workerinfo.Name,
						workerinfo.Alias_Name,
						workerinfo.Class_Group,								
						workerinfo.Mumber,
						workerinfo.T_Line_Content_Guid,		
						workerinfo.T_Line_Guid,
						workerinfo.T_Organization_Guid});
				}
			if(cursor != null){cursor.close();}
		}
		
		/////////////////////////////////////////////
		
		TurnInfo turninfo = null;
		
		//parse turn information from json txt
		
		for(int i = 0;i<info.mTurnList.size();i++){
			turninfo = (TurnInfo) info.mTurnList.get(i);
			cursor = mDB.query(TurnTableName,
					null,
					DBHelper.Plan_Turn_Table.Number + "=?" , 
					new String[] { turninfo.Number}, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
				 sql = "insert into "+TurnTableName
						 +"(" + DBHelper.Plan_Turn_Table.Number +","				 
						 + DBHelper.Plan_Turn_Table.End_Time+","
						 + DBHelper.Plan_Turn_Table.Name+","
						 + DBHelper.Plan_Turn_Table.Start_Time+","
						 + DBHelper.Plan_Turn_Table.T_Line_Guid+","						
						 + DBHelper.Plan_Turn_Table.T_Line_Content_Guid
						 +")values(?,?,?,?,?,?)";
						
				// SQLiteDatabase db = helper.getWritableDatabase();
				mDB.execSQL(sql, new Object[] {
						turninfo.Number,
						turninfo.End_Time,
						turninfo.Name,
						turninfo.Start_Time,	
						turninfo.T_Line_Guid,		
						turninfo.T_Line_Content_Guid
						});
				}
			if(cursor != null){cursor.close();}
		}
		return 1;
		
	}	

	// 其它操作
	// 删除操作
	public void delete(int id) {
		// SQLiteDatabase db = helper.getWritableDatabase();
		String where = id + " = ?";
		String[] whereValue = { Integer.toString(id) };
		mDB.delete(RouteTableName, where, whereValue);
	}

	/**
	 * 修改操作
	 */
	public void update(int id, RouteBean updateRoute) {
		if (updateRoute == null) {
			return;
		}
		// SQLiteDatabase db = helper.getWritableDatabase();
		String where = id + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put("guid", updateRoute.mGUID);
		cv.put("jxName", updateRoute.mjxName);
		cv.put("downTime", updateRoute.mDownLoadTime);
		cv.put("isChecked", updateRoute.mIsChecked);
		cv.put("isBeiginChecked", updateRoute.mIsBeiginCheck);
		cv.put("isuploaded", updateRoute.mIsUploaded);
		cv.put("lastcheckTime", updateRoute.mLastCheckTime);
		cv.put("workerName", updateRoute.mWorkerName);
		cv.put("firstcheckTime", updateRoute.mFirstCheckTime);
		cv.put("lastCheckStation", updateRoute.mLastCheckedStationIndex);
		cv.put("lastCheckDeviceIndex", updateRoute.mLastCheckedDeviceIndex);
		cv.put("lastCheckPartItemIndex", updateRoute.mLastCheckedPartItemIndex);
		cv.put("isReverseCheck", updateRoute.mIsReverseChecking);

		mDB.update(RouteTableName, cv, where, whereValue);
	}

	// 查询操作

	public Cursor queryLastCheckIndex(String strGUID) {
		return mDB
				.query(RouteTableName,
						new String[] { "lastCheckStation,lastCheckDeviceIndex,lastCheckPartItemIndex,isReverseCheck,isChecked,isuploaded" },
						"guid" + "=?", new String[] { strGUID }, null, null,
						null);

	}

	/**
	 * 有两种情况 第一种，还没巡检过的 第二种，已经开始巡检了，但还没巡检完毕
	 * 
	 * @return
	 */
	public Cursor queryNotFinishRouteInfo() {
		String strarg = "0";

		Cursor cursor = mDB
				.query(RouteTableName,
						new String[] { "guid,jxName,filePath,downTime,isChecked,isBeiginChecked,isuploaded,lastcheckTime,workerName,firstcheckTime,lastCheckStation,lastCheckDeviceIndex,lastCheckPartItemIndex,isReverseCheck" },
						"isChecked" + "=?", new String[] { strarg }, null,
						null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}
	
	public Cursor queryRouteInfoByPath(String path) {
		String strarg = "0";

		Cursor cursor = mDB
				.query(RouteTableName,
						null,
						SourceTable.Path+ "=?", new String[] { path }, null,
						null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	/**
	 * 查询已经巡检完毕了，但还没上传服务器的巡检路线信息
	 * 
	 * @return
	 */
	public Cursor queryNotUploadRouteInfo() {
		String strarg = "0";

		Cursor cursor = mDB
				.query(RouteTableName,
						new String[] { "guid,jxName,downTime,isChecked,isBeiginChecked,isuploaded,lastcheckTime,workerName,firstcheckTime,lastCheckStation,lastCheckDeviceIndex,lastCheckPartItemIndex,isReverseCheck" },
						"isChecked" + "=?", new String[] { strarg }, null,
						null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

public List<String> queryWorkerNumber(String name,String pwsd) {
		
		List<String>guidList = new ArrayList<String>();
		Cursor cur = mDB
				.query(WorkerTableName,						
						null,
						DBHelper.Plan_Worker_Table.Name+ "=?" +" and "+DBHelper.Plan_Worker_Table.Mumber+ "=?", new String[] { name,pwsd }, 
						null,
						null, null);
		if (cur != null) {
			cur.moveToFirst();
			for(int n=0;n<cur.getCount();n++){
			String number = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Mumber));
			guidList.add(number);
			cur.moveToNext();
			Log.d("luotest", "search worker table result  number is" +number);
			}
			cur.close();
		}
		return guidList;
}
	public List<String> queryLogIn(String name,String pwsd,ContentValues cv  ) {
		Log.d("luotest", "queryLogIn()  name is" +name + ",pwsd is "+pwsd);
		List<String>guidList = new ArrayList<String>();
		String error= "";
		Cursor cur = mDB
				.query(WorkerTableName,						
						null,
						DBHelper.Plan_Worker_Table.Name+ "=?" +" and "+DBHelper.Plan_Worker_Table.Mumber+ "=?", new String[] { name,pwsd }, 
						null,
						null, null);
		if (cur != null  && cur.getCount()>0) {
			Log.d("luotest", "queryLogIn()  cur != null cur.count =" +cur.getCount());
			cur.moveToFirst();
			for(int n=0;n<cur.getCount();n++){
			String GUID = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.T_Line_Guid));
			guidList.add(GUID);
			cur.moveToNext();
			Log.d("luotest", "queryLogIn()  searvher worker table GUID is" +GUID);
			}
			cur.close();
			cur= null;
		}else{
			Log.d("luotest", "queryLogIn() searvher worker table cur is null");
			error = "没有您的信息，请核实再登录";
		}
		
		if(cur!=null){
			cur.close();
		}
		
		List<String>fileNameList = new ArrayList<String>();
	
		
		for(int k =0 ;k<guidList.size();k++){
			Log.d("luotest", " queryLogIn() search worker table " +k);
			Cursor cursor2 = mDB
					.query(RouteTableName,						
							new String[]{DBHelper.SourceTable.Path},
							DBHelper.SourceTable.PLANGUID+ "=?" , new String[] { guidList.get(k) }, 
							null,
							null,
							null);
				if (cursor2 != null && cursor2.getCount()>0) {
					cursor2.moveToFirst();
					for(int i=0;i<cursor2.getCount();i++){
						String path =	cursor2.getString(0);				
					fileNameList.add(path);
					cursor2.moveToNext();
					Log.d("luotest", " queryLogIn() search route table  result path is " +path);
					}
					cursor2.close();
					cursor2 = null;
				}else{
					Log.d("luotest", " queryLogIn() search route table cursor2 is null" );
					error = "没有您对应的巡检任务";
				}
				
				if(cursor2 != null){
					cursor2.close();
				}
		}
		Log.d("luotest", "queryLogIn() end  error="+error);
		cv.put("error", error);
		return fileNameList;
	}
	
	/**
	 * 根据传入的文件路径 解析路线信息
	 * @param filelist
	 * @return
	 */
	public List<T_Route> getRouteInfoByFilePath(List<String >filelist) {
		if(filelist ==null) return null;
		
		List<T_Route> list = new ArrayList<T_Route>();
		for (int n = 0; n < filelist.size(); n++) {
			Cursor cursor = queryRouteInfoByPath(filelist.get(n));
			if (cursor != null) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					T_Route info = new T_Route();

					info.Guid =cursor.getString(cursor.getColumnIndex(DBHelper.SourceTable.PLANGUID));
					info.Name =cursor.getString(cursor.getColumnIndex(DBHelper.SourceTable.PLANNAME));
					info.Path =cursor.getString(cursor.getColumnIndex(DBHelper.SourceTable.Path));
					list.add(info);
					cursor.moveToNext();
				}

			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	
	public List<T_Route> getNoCheckFinishRouteInfo() {
		List<T_Route> list = new ArrayList<T_Route>();
		Cursor cursor = queryNotFinishRouteInfo();
		if (cursor != null) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				T_Route info = new T_Route();

				info.Name = cursor.getString(cursor
						.getColumnIndex(DBHelper.SourceTable.Path));
				info.Guid = cursor.getString(cursor
						.getColumnIndex(DBHelper.SourceTable.PLANGUID));
				info.Name = cursor
						.getString(cursor
								.getColumnIndex(DBHelper.SourceTable.PLANNAME));
				list.add(info);
				cursor.moveToNext();
			}

		}
		return list;
	}

	public int getCount() {
		int count = 0;
		Cursor cursor = mDB
				.query(RouteTableName,						
						null,
						null, 
						null,
						null, 
						null, 
						null);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getCount();
		}
		return count;
	}

	public void updateRouteInfoList() {

	}

}