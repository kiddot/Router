package com.android.annotation.launcher;

import android.app.Application;

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
}
