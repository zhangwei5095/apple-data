package com.appleframework.data.hbase.client.rowkey.handler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.appleframework.data.hbase.exception.SimpleHBaseException;
import com.appleframework.data.hbase.util.ClassUtil;
import com.appleframework.data.hbase.util.Util;

/**
 * The holder of RowKeyHandler's instance.
 * 
 * @author xinzhi
 * */
public class RowKeyHandlerHolder {

    /**
     * RowKeyHandler'Type -> RowKeyHandler's instance cache.
     * */
    private static ConcurrentMap<String, RowKeyHandler> cache = new ConcurrentHashMap<String, RowKeyHandler>();

    /**
     * Find RowKeyHandler instance using type's class name.
     * 
     * @param type RowKeyHandler's class name.
     * @return RowKeyHandler instance.
     * */
    public static RowKeyHandler findRowKeyHandler(String type) {
        Util.checkEmptyString(type);

        if (cache.get(type) == null) {
            try {
                Class<?> c = ClassUtil.forName(type);
                cache.putIfAbsent(type, (RowKeyHandler) c.newInstance());
            } catch (Exception e) {
                throw new SimpleHBaseException(e);
            }
        }
        return cache.get(type);
    }
}
