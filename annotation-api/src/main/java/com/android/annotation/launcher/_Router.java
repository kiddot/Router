package com.android.annotation.launcher;

import android.app.Application;
import android.content.Context;

import com.android.annotation.core.LogisticsCenter;
import com.android.annotation.thread.DefaultPoolExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by kiddo on 17-8-6.
 */

public class _Router {
    private static final String TAG = "_Router";

    private static _Router m_Router = null;
    private volatile static boolean hasInit = false;
    private volatile static ThreadPoolExecutor executor = DefaultPoolExecutor.getInstance();
    private static Context mContext;

    private _Router(){}

    public static _Router getInstance(){
        synchronized (_Router.class){
               if (m_Router == null){
                    m_Router = new _Router();
               }
        }
        return m_Router;
    }

    protected static synchronized boolean init(Application application) {
        mContext = application;
        LogisticsCenter.init(mContext, executor);
        hasInit = true;

        // It's not a good idea.
        // if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        //     application.registerActivityLifecycleCallbacks(new AutowiredLifecycleCallback());
        // }
        return true;
    }
}
