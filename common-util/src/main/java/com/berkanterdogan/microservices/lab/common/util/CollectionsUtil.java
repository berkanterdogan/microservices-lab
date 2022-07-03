package com.berkanterdogan.microservices.lab.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class
 * Initialization on-demand holder provides thread-safe and lazy approach
 */
public class CollectionsUtil {

    private CollectionsUtil() {
    }

    private static class CollectionsUtilHolder {
        static final CollectionsUtil INSTANCE = new CollectionsUtil();
    }

    public static CollectionsUtil getInstance() {
        return CollectionsUtilHolder.INSTANCE;
    }

    public <T> List<T> getListFromIterable(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}
