package com.android.annotation.launcher;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.android.annotation.Postcard;
import com.android.annotation.core.LogisticsCenter;
import com.android.annotation.core.Warehouse;
import com.android.annotation.model.RouteMeta;
import com.android.annotation.service.PathReplaceService;
import com.android.annotation.thread.DefaultPoolExecutor;
import com.android.annotation.utils.TextUtils;

import java.util.concurrent.ThreadPoolExecutor;

import static compiler.utils.Consts.ACTIVITY;

/**
 * Created by kiddo on 17-8-6.
 */

public class _Router {
    private static final String TAG = "_Router";

    private static _Router m_Router = null;
    private volatile static boolean hasInit = false;
    private volatile static ThreadPoolExecutor executor = DefaultPoolExecutor.getInstance();
    private static Context mContext;

    private _Router() {
    }

    public static _Router getInstance() {
        synchronized (_Router.class) {
            if (m_Router == null) {
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

    /**
     * Build postcard by path and default group
     */
    protected Postcard build(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        } else {
            PathReplaceService pService = Router.getInstance().navigation(PathReplaceService.class);
            if (null != pService) {
                path = pService.forString(path);
            }
            return build(path, extractGroup(path));
        }
    }

    /**
     * Build postcard by path and group
     */
    protected Postcard build(String path, String group) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(group)) {
            return null;
        } else {
            PathReplaceService pService = Router.getInstance().navigation(PathReplaceService.class);
            if (null != pService) {
                path = pService.forString(path);
            }
            return new Postcard(path, group);
        }
    }

    /**
     * Extract the default group from path.
     */
    private String extractGroup(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
        }

        try {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            if (TextUtils.isEmpty(defaultGroup)) {
                return null;
            } else {
                return defaultGroup;
            }
        } catch (Exception e) {
            return null;
        }
    }


    protected <T> T navigation(Class<? extends T> service) {
        Postcard postcard = LogisticsCenter.buildProvider(service.getName());

        // Compatible 1.0.5 compiler sdk.
        if (null == postcard) { // No service, or this service in old version.
            postcard = LogisticsCenter.buildProvider(service.getSimpleName());
        }

        LogisticsCenter.completion(postcard);
        return (T) postcard.getProvider();

    }

    /**
     * Use router navigation.
     *
     * @param context     Activity or null.
     * @param postcard    Route metas
     * @param requestCode RequestCode
     */
    protected Object navigation(final Context context, final Postcard postcard, final int requestCode) {
        LogisticsCenter.completion(postcard);

        if (!postcard.isGreenChannel()) {   // It must be run in async thread, maybe interceptor cost too mush time made ANR.
//            interceptorService.doInterceptions(postcard, new InterceptorCallback() {
//                /**
//                 * Continue process
//                 *
//                 * @param postcard route meta
//                 */
//                @Override
//                public void onContinue(Postcard postcard) {
//
//                }
//
//                /**
//                 * Interrupt process, pipeline will be destory when this method called.
//                 *
//                 * @param exception Reson of interrupt.
//                 */
//                @Override
//                public void onInterrupt(Throwable exception) {
//                    if (null != callback) {
//                        //callback.onInterrupt(postcard);
//                    }
//
//                }
//            });
            _navigation(context, postcard, requestCode);
        } else {
            return _navigation(context, postcard, requestCode);
        }

        return null;
    }

    private Object _navigation(final Context context, final Postcard postcard, final int requestCode) {
        final Context currentContext = null == context ? mContext : context;

        switch (postcard.getRouteType()) {
            case ACTIVITY:
                // Build intent
                final Intent intent = new Intent(currentContext, postcard.getDestination());
                intent.putExtras(postcard.getExtras());

                // Set flags.
                int flags = postcard.getFlags();
                if (-1 != flags) {
                    intent.setFlags(flags);
                } else if (!(currentContext instanceof Activity)) {    // Non activity, need less one flag.
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                // Navigation in main looper.
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (requestCode > 0) {  // Need start for result
                            ActivityCompat.startActivityForResult((Activity) currentContext, intent, requestCode, postcard.getOptionsBundle());
                        } else {
                            ActivityCompat.startActivity(currentContext, intent, postcard.getOptionsBundle());
                        }

                        if ((0 != postcard.getEnterAnim() || 0 != postcard.getExitAnim()) && currentContext instanceof Activity) {    // Old version.
                            ((Activity) currentContext).overridePendingTransition(postcard.getEnterAnim(), postcard.getExitAnim());
                        }

//                        if (null != callback) { // Navigation over.
//                            //callback.onArrival(postcard);
//                        }
                    }
                });

                break;
//            case PROVIDER:
//                return postcard.getProvider();
//            case BOARDCAST:
//            case CONTENT_PROVIDER:
//            case FRAGMENT:
//                Class fragmentMeta = postcard.getDestination();
//                try {
//                    Object instance = fragmentMeta.getConstructor().newInstance();
//                    if (instance instanceof Fragment) {
//                        ((Fragment) instance).setArguments(postcard.getExtras());
//                    } else if (instance instanceof android.support.v4.app.Fragment) {
//                        ((android.support.v4.app.Fragment) instance).setArguments(postcard.getExtras());
//                    }
//
//                    return instance;
//                } catch (Exception ex) {
//                    logger.error(Consts.TAG, "Fetch fragment instance error, " + TextUtils.formatStackTrace(ex.getStackTrace()));
//                }
//            case METHOD:
//            case SERVICE:
            default:
                return null;
        }

        return null;
    }
}
