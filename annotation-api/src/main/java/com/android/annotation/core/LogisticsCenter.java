package com.android.annotation.core;

import android.content.Context;
import android.net.Uri;

import com.android.annotation.Postcard;
import com.android.annotation.enums.TypeKind;
import com.android.annotation.launcher.Router;
import com.android.annotation.model.RouteMeta;
import com.android.annotation.template.IProviderGroup;
import com.android.annotation.template.IRouteGroup;
import com.android.annotation.template.IRouteRoot;
import com.android.annotation.utils.ClassUtils;
import com.android.annotation.utils.TextUtils;


import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import static com.android.annotation.utils.Consts.DOT;
import static com.android.annotation.utils.Consts.ROUTE_ROOT_PAKCAGE;
import static com.android.annotation.utils.Consts.SDK_NAME;
import static com.android.annotation.utils.Consts.SUFFIX_ROOT;
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

    /**
     * Completion the postcard by route metas
     *
     * @param postcard Incomplete postcard, should completion by this method.
     */
    public synchronized static void completion(Postcard postcard) {
        if (null == postcard) {
        }

        RouteMeta routeMeta = Warehouse.routes.get(postcard.getPath());
        if (null == routeMeta) {    // Maybe its does't exist, or didn't load.
            Class<? extends IRouteGroup> groupMeta = Warehouse.groupsIndex.get(postcard.getGroup());  // Load route meta.
            if (null == groupMeta) {
            } else {
                // Load route and cache it into memory, then delete from metas.
                try {

                    IRouteGroup iGroupInstance = groupMeta.getConstructor().newInstance();
                    iGroupInstance.loadInto(Warehouse.routes);
                    Warehouse.groupsIndex.remove(postcard.getGroup());

                } catch (Exception e) {
                }

                completion(postcard);   // Reload
            }
        } else {
            postcard.setDestination(routeMeta.getDestination());
            postcard.setRouteType(routeMeta.getRouteType());
            postcard.setPriority(routeMeta.getPriority());
            postcard.setExtra(routeMeta.getExtra());

            Uri rawUri = postcard.getUri();
            if (null != rawUri) {   // Try to set params into bundle.
                Map<String, String> resultMap = TextUtils.splitQueryParameters(rawUri);
                Map<String, Integer> paramsType = routeMeta.getParamsType();

                if (MapUtils.isNotEmpty(paramsType)) {
                    // Set value by its type, just for params which annotation by @Param
                    for (Map.Entry<String, Integer> params : paramsType.entrySet()) {
                        setValue(postcard,
                                params.getValue(),
                                params.getKey(),
                                resultMap.get(params.getKey()));
                    }

                    // Save params name which need autoinject.
                    //postcard.getExtras().putStringArray(Router.AUTO_INJECT, paramsType.keySet().toArray(new String[]{}));
                }

                // Save raw uri
                //postcard.withString(ARouter.RAW_URI, rawUri.toString());
            }

            switch (routeMeta.getRouteType()) {
//                case PROVIDER:  // if the route is provider, should find its instance
//                    // Its provider, so it must be implememt IProvider
//                    Class<? extends IProvider> providerMeta = (Class<? extends IProvider>) routeMeta.getDestination();
//                    IProvider instance = Warehouse.providers.get(providerMeta);
//                    if (null == instance) { // There's no instance of this provider
//                        IProvider provider;
//                        try {
//                            provider = providerMeta.getConstructor().newInstance();
//                            provider.init(mContext);
//                            Warehouse.providers.put(providerMeta, provider);
//                            instance = provider;
//                        } catch (Exception e) {
//                            throw new HandlerException("Init provider failed! " + e.getMessage());
//                        }
//                    }
//                    postcard.setProvider(instance);
//                    postcard.greenChannel();    // Provider should skip all of interceptors
//                    break;
//                case FRAGMENT:
//                    postcard.greenChannel();    // Fragment needn't interceptors
                default:
                    break;
            }
        }
    }

    /**
     * Set value by known type
     *
     * @param postcard postcard
     * @param typeDef  type
     * @param key      key
     * @param value    value
     */
    private static void setValue(Postcard postcard, Integer typeDef, String key, String value) {
        try {
            if (null != typeDef) {
                if (typeDef == TypeKind.BOOLEAN.ordinal()) {
                    postcard.withBoolean(key, Boolean.parseBoolean(value));
                } else if (typeDef == TypeKind.BYTE.ordinal()) {
                    postcard.withByte(key, Byte.valueOf(value));
                } else if (typeDef == TypeKind.SHORT.ordinal()) {
                    postcard.withShort(key, Short.valueOf(value));
                } else if (typeDef == TypeKind.INT.ordinal()) {
                    postcard.withInt(key, Integer.valueOf(value));
                } else if (typeDef == TypeKind.LONG.ordinal()) {
                    postcard.withLong(key, Long.valueOf(value));
                } else if (typeDef == TypeKind.FLOAT.ordinal()) {
                    postcard.withFloat(key, Float.valueOf(value));
                } else if (typeDef == TypeKind.DOUBLE.ordinal()) {
                    postcard.withDouble(key, Double.valueOf(value));
                } else if (typeDef == TypeKind.STRING.ordinal()) {
                    postcard.withString(key, value);
                } else if (typeDef == TypeKind.PARCELABLE.ordinal()) {
                    // TODO : How to description parcelable value with string?
                } else if (typeDef == TypeKind.OBJECT.ordinal()) {
                    postcard.withString(key, value);
                } else {    // Compatible compiler sdk 1.0.3, in that version, the string type = 18
                    postcard.withString(key, value);
                }
            } else {
                postcard.withString(key, value);
            }
        } catch (Throwable ex) {

        }
    }


    /**
     * Build postcard by serviceName
     *
     * @param serviceName interfaceName
     * @return postcard
     */
    public static Postcard buildProvider(String serviceName) {
        RouteMeta meta = Warehouse.providersIndex.get(serviceName);

        if (null == meta) {
            return null;
        } else {
            return new Postcard(meta.getPath(), meta.getGroup());
        }
    }
}
