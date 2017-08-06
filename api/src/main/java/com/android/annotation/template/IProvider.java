package com.android.annotation.template;

/**
 * Created by kiddo on 17-8-6.
 */

public interface IProvider {

    /**
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    void init(Context context);
}
