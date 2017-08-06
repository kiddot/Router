package com.android.annotation.core;

import com.android.annotation.model.RouteMeta;
import com.android.annotation.template.IProvider;
import com.android.annotation.template.IRouteGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kiddo on 17-8-6.
 */

public class Warehouse {
    // Cache route and metas
    public static Map<String, Class<? extends IRouteGroup>> groupsIndex = new HashMap<>();
    public static Map<String, RouteMeta> routes = new HashMap<>();

    // Cache provider
    public static Map<Class, IProvider> providers = new HashMap<>();
    public static Map<String, RouteMeta> providersIndex = new HashMap<>();


    public static void clear() {
        routes.clear();
        groupsIndex.clear();
        providers.clear();
        providersIndex.clear();
    }
}
