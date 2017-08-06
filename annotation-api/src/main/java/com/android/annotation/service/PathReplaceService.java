package com.android.annotation.service;

import android.net.Uri;

import com.android.annotation.template.IProvider;

/**
 * Created by kiddo on 17-8-6.
 */

public interface PathReplaceService extends IProvider {

    /**
     * For normal path.
     *
     * @param path raw path
     */
    String forString(String path);

    /**
     * For uri type.
     *
     * @param uri raw uri
     */
    Uri forUri(Uri uri);
}
