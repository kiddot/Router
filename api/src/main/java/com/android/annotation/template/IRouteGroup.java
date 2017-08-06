package com.android.annotation.template;

import com.android.annotation.model.RouteMeta;

import java.util.Map;

/**
 * Created by kiddo on 17-8-6.
 */

public interface IRouteGroup {
    /**
     * Fill the atlas with routes in group.
     */
    void loadInto(Map<String, RouteMeta> atlas);
}