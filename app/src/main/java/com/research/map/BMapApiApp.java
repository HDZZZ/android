package com.research.map;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.research.Entity.CountryList;
import com.research.Utils.LogUtil;
import com.research.global.ResearchCommon;
import com.research.net.ResearchException;

/**
 * http://lbsyun.baidu.com/apiconsole/key
 * 百度地图key
 * @author dongli
 *
 */
public class BMapApiApp extends Application {
    private Bitmap bitmap;
    private static BMapApiApp mInstance = null;
    public boolean m_bKeyRight = true;
    public BMapManager mBMapManager = null;
    private static  CountryList mCountryList = null;
   

 

  public static final String strKey = "ylg8yBCSUPFnuu82sDnIbZ5H";
    public LogUtil mlogUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    	ResearchCommon.verifyNetwork(mInstance);
        initEngineManager(this);
        if(ResearchCommon.getUserId(mInstance)!=null && !ResearchCommon.getUserId(mInstance).equals("")){
        	 new Thread(){
             	public void run() {
             		try {
             			mCountryList = ResearchCommon.getResearchInfo()
             					.getCityAndContryUser();
             		} catch (ResearchException e) {
             			e.printStackTrace();
             		}
             	};
             }.start();
        }
        mlogUtils = LogUtil.getInstance();


    }
    
    public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
            Toast.makeText(BMapApiApp.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
    }
    
    public static BMapApiApp getInstance() {
        return mInstance;
    }
    
    public static CountryList getContryList(){
    	return mCountryList;
    }
    
    public static void setContryList(CountryList contryList)    {
    	mCountryList = contryList;
    }
   
    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(BMapApiApp.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(BMapApiApp.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
            //非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
                //Toast.makeText(BMapApiApp.getInstance().getApplicationContext(), 
                //        "请在 BMapApiApp.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();
               // BMapApiApp.getInstance().m_bKeyRight = false;
                BMapApiApp.getInstance().m_bKeyRight = true;
            }
            else{
                BMapApiApp.getInstance().m_bKeyRight = true;
                //Toast.makeText(BMapApiApp.getInstance().getApplicationContext(), 
                //        "key认证成功", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}