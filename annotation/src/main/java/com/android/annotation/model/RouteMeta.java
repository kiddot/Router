package com.android.annotation.model;

import com.android.annotation.enums.RouteType;

import java.util.Map;

/**
 * Created by kiddo on 17-8-6.
 */

public class RouteMeta {
    private RouteType routeType;
    private Class<?> destination;
    private String path;
    private String group;
    private int priority = -1;
    private int extra;
    private Map<String, Integer> paramsType;

    public RouteMeta(){

    }

    /**
     * For versions of 'compiler' less than 1.0.7, contain 1.0.7
     *
     * @param type        type
     * @param destination destination
     * @param path        path
     * @param group       group
     * @param priority    priority
     * @param extra       extra
     * @return this
     */
    public static RouteMeta build(RouteType type, Class<?> destination, String path, String group, int priority, int extra) {
        return new RouteMeta(type, destination, path, group, null, priority, extra);
    }

    /**
     * For versions of 'compiler' greater than 1.0.7
     *
     * @param type        type
     * @param destination destination
     * @param path        path
     * @param group       group
     * @param paramsType  paramsType
     * @param priority    priority
     * @param extra       extra
     * @return this
     */
    public static RouteMeta build(RouteType type, Class<?> destination, String path, String group, Map<String, Integer> paramsType, int priority, int extra) {
        return new RouteMeta(type, destination, path, group, paramsType, priority, extra);
    }

    /**
     * Type
     *
     * @param type        type
     * @param destination destination
     * @param path        path
     * @param group       group
     * @param paramsType  paramsType
     * @param priority    priority
     * @param extra       extra
     */
    public RouteMeta(RouteType type, Class<?> destination, String path, String group, Map<String, Integer> paramsType, int priority, int extra) {
        this.routeType = type;
        this.destination = destination;
        this.path = path;
        this.group = group;
        this.paramsType = paramsType;
        this.priority = priority;
        this.extra = extra;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getExtra() {
        return extra;
    }

    public void setExtra(int extra) {
        this.extra = extra;
    }

    public Map<String, Integer> getParamsType() {
        return paramsType;
    }

    public void setParamsType(Map<String, Integer> paramsType) {
        this.paramsType = paramsType;
    }
}
