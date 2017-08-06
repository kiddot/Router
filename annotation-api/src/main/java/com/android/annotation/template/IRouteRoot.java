package com.android.annotation.template;

import java.util.Map;

/**
 * Created by kiddo on 17-8-6.
 */

public interface IRouteRoot {

    /**
     * Load routes to input
     * @param routes input
     */
    void loadInto(Map<String, Class<? extends IRouteGroup>> routes);
}
