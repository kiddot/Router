package com.android.annotation.launcher;

import android.app.Application;
import android.content.Context;

import com.android.annotation.Postcard;

/**
 * Created by kiddo on 17-8-6.
 */

public class Router {
    private static final String TAG = "Router";

    private static Router mRouter = null;
    private volatile static boolean hasInit = false;

    private Router(){}

    public static Router getInstance(){
        synchronized (Router.class){
               if (mRouter == null){
                    mRouter = new Router();
               }
        }
        return mRouter;
    }

    /**
     * Init, it must be call before used router.
     */
    public static void init(Application application) {
        if (!hasInit) {
            hasInit = _Router.init(application);

//            if (hasInit) {
//                _Router.afterInit();
//            }

        }
    }

    /**
     * Build the roadmap, draw a postcard.
     *
     * @param path Where you go.
     */
    public Postcard build(String path) {
        return _Router.getInstance().build(path);
    }

    /**
     * Launch the navigation by type
     *
     * @param service interface of service
     * @param <T>     return type
     * @return instance of service
     */
    public <T> T navigation(Class<? extends T> service) {
        return _Router.getInstance().navigation(service);
    }

    /**
     * Launch the navigation.
     *
     * @param mContext    .
     * @param postcard    .
     * @param requestCode Set for startActivityForResult
     */
    public Object navigation(Context mContext, Postcard postcard, int requestCode) {
        return _Router.getInstance().navigation(mContext, postcard, requestCode);
    }
}
