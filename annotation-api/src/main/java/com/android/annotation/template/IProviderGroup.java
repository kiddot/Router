package com.android.annotation.template;

import com.android.annotation.model.RouteMeta;

import java.util.Map;

/**
 * Created by kiddo on 17-8-6.
 */

public interface IProviderGroup {
    /**
     * Load providers map to input
     *
     * @param providers input
     */
    void loadInto(Map<String, RouteMeta> providers);
}
