package com.android.annotation.core;

import android.content.Context;

import com.android.annotation.template.IProviderGroup;
import com.android.annotation.template.IRouteRoot;
import com.android.annotation.utils.ClassUtils;


import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadPoolExecutor;

import static compiler.utils.Consts.SEPARATOR;

/**
 * Created by kiddo on 17-8-6.
 */

public class LogisticsCenter {
    private static Context mContext;
    static ThreadPoolExecutor executor;

    /**
     * LogisticsCenter init, load all metas in memory. Demand initialization
     */
    public synchronized static void init(Context context, ThreadPoolExecutor tpe) {
        mContext = context;
        executor = tpe;

        try {
            // These class was generate by arouter-compiler.
            List<String> classFileNames = ClassUtils.getFileNameByPackageName(mContext, ROUTE_ROOT_PAKCAGE);

            //
            for (String className : classFileNames) {
                if (className.startsWith(ROUTE_ROOT_PAKCAGE + DOT + SDK_NAME + SEPARATOR + SUFFIX_ROOT)) {
                    // This one of root elements, load root.
                    ((IRouteRoot) (Class.forName(className).getConstructor().newInstance())).loadInto(Warehouse.groupsIndex);
                }
            }

            if (Warehouse.groupsIndex.size() == 0) {
            }
        } catch (Exception e) {

        }
    }
}
