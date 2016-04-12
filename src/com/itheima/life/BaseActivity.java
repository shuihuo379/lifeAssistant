package com.itheima.life;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.itheima.net.NetWorkHelper;
import com.itheima.util.T;

/**
 * 所有Activity的基类
 * 
 * @author zhangming
 * @date 2016/04/09
 */
public class BaseActivity extends Activity {
	protected void startActivity(Class<?> cls) {
		Intent i = new Intent(this, cls);
		startActivity(i);
	}

	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent i = new Intent(this, cls);
		i.putExtras(bundle);
		startActivity(i);
	}

	protected BaseActivity getContext() {
		return this;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (null != this.getCurrentFocus()) {
			// 点击空白位置 隐藏软键盘
			InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			return mInputMethodManager.hideSoftInputFromWindow(this
					.getCurrentFocus().getWindowToken(), 0);
		}
		return super.onTouchEvent(event);
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d("test", "网络状态已经改变");
                if(NetWorkHelper.isNetworkAvailable(context)){
                    operateMethod();
                }else{
                    T.show(context,context.getString(R.string.network_exception));
                    return;
                }
            }
        }
    };
    
    /**
     * 子类可以去覆写的方法，若子类覆写,则执行子类的操作代码
     */
    protected void operateMethod(){
    	
    }
    
    protected void initNetManager(){
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
	}
    
    protected void destroyNetManager(){
    	if(mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
    }
}
