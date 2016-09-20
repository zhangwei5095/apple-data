package com.appleframework.data.hbase.client.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.appleframework.data.hbase.client.DeleteRequest;
import com.appleframework.data.hbase.client.PutRequest;
import com.appleframework.data.hbase.client.QueryExtInfo;
import com.appleframework.data.hbase.client.RowKey;
import com.appleframework.data.hbase.client.SimpleHbaseDOResult;
import com.appleframework.data.hbase.core.Nullable;

/**
 * HbaseMultipleVersionService
 * 
 * <pre>
 * Provides hbase multiple version related service.
 * </pre>
 * 
 * @author xinzhi.zhang
 * */
public interface HbaseMultipleVersionService {

    /**
     * Put POJO with timestamp.
     * 
     * @param rowKey rowKey.
     * @param t POJO.
     * @param timestamp timestamp.
     * */
    public <T> void putObjectMV(RowKey rowKey, T t, long timestamp);

    /**
     * Put POJO with timestamp.
     * 
     * @param rowKey rowKey.
     * @param t POJO.
     * @param timestamp timestamp.
     * */
    public <T> void putObjectMV(RowKey rowKey, T t, Date timestamp);

    /**
     * Put POJO list with specified timestamp.
     * 
     * @param putRequestList putRequestList.
     * @param timestamp timestamp.
     * */
    public <T> void putObjectListMV(List<PutRequest<T>> putRequestList, long timestamp);

    /**
     * Put POJO list with specified timestamp.
     * 
     * @param putRequestList putRequestList.
     * @param timestamp timestamp.
     * */
    public <T> void putObjectListMV(List<PutRequest<T>> putRequestList, Date timestamp);

    /**
     * Put list of POJO with its timestamp.
     * 
     * @param putRequestList putRequestList.
     * */
    public <T> void putObjectListMV(List<PutRequest<T>> putRequestList);

    /**
     * Find object with row key.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return SimpleHbaseDOResult list, timestamp desc ordered.
     * */
    public <T> List<SimpleHbaseDOResult<T>> findObjectMV(RowKey rowKey,
            Class<? extends T> type, QueryExtInfo queryExtInfo);

    /**
     * Dynamic query to find POJO.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return SimpleHbaseDOResult list, timestamp desc ordered.
     * */
    public <T> List<SimpleHbaseDOResult<T>> findObjectMV(RowKey rowKey,
            Class<? extends T> type, String id,
            @Nullable Map<String, Object> para, QueryExtInfo queryExtInfo);

    /**
     * Find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return SimpleHbaseDOResult list of list, sub list is timestamp desc
     *         ordered.
     * */
    public <T> List<List<SimpleHbaseDOResult<T>>> findObjectListMV(
            RowKey startRowKey, RowKey endRowKey, Class<? extends T> type,
            QueryExtInfo queryExtInfo);
    
    
    /**
     * Find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return SimpleHbaseDOResult list of list, sub list is timestamp desc
     *         ordered.
     * */
    public <T> List<List<SimpleHbaseDOResult<T>>> findObjectListMV(
            RowKey startRowKey, long pageSize, Class<? extends T> type,
            QueryExtInfo queryExtInfo);

    /**
     * Dynamic query to find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return SimpleHbaseDOResult list of list, sub list is timestamp desc
     *         ordered.
     * */
    public <T> List<List<SimpleHbaseDOResult<T>>> findObjectListMV(
            RowKey startRowKey, RowKey endRowKey, Class<? extends T> type,
            String id, @Nullable Map<String, Object> para,
            QueryExtInfo queryExtInfo);
    
    /**
     * Dynamic query to find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return SimpleHbaseDOResult list of list, sub list is timestamp desc
     *         ordered.
     * */
    public <T> List<List<SimpleHbaseDOResult<T>>> findObjectListMV(
            RowKey startRowKey, long pageSize, Class<? extends T> type,
            String id, @Nullable Map<String, Object> para,
            QueryExtInfo queryExtInfo);

    /**
     * Delete POJO.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * @param timeStamp timeStamp.
     * */
    public void deleteObjectMV(RowKey rowKey, Class<?> type, long timeStamp);

    /**
     * Delete POJO.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * @param timeStamp timeStamp.
     * */
    public void deleteObjectMV(RowKey rowKey, Class<?> type, Date timeStamp);

    /**
     * Delete POJO list.
     * 
     * @param rowKeyList rowKeyList.
     * @param type POJO type.
     * @param timeStamp timeStamp.
     * */
    public void deleteObjectListMV(List<RowKey> rowKeyList, Class<?> type, long timeStamp);

    /**
     * Delete POJO list.
     * 
     * @param rowKeyList rowKeyList.
     * @param type POJO type.
     * @param timeStamp timeStamp.
     * */
    public void deleteObjectListMV(List<RowKey> rowKeyList, Class<?> type, Date timeStamp);

    /**
     * Delete POJO list with its timestamp.
     * 
     * @param deleteRequestList deleteRequestList.
     * @param type POJO type.
     * */
    public void deleteObjectListMV(List<DeleteRequest> deleteRequestList, Class<?> type);
}
