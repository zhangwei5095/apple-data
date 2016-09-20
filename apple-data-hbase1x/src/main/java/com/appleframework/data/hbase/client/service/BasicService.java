package com.appleframework.data.hbase.client.service;

import java.util.List;
import java.util.Map;

import com.appleframework.data.core.page.Pagination;
import com.appleframework.data.hbase.client.PutRequest;
import com.appleframework.data.hbase.client.QueryExtInfo;
import com.appleframework.data.hbase.client.RowKey;
import com.appleframework.data.hbase.client.SimpleHbaseDOWithKeyResult;
import com.appleframework.data.hbase.core.Nullable;

/**
 * BasicService
 * 
 * <pre>
 * Provides basic services.
 * </pre>
 * 
 * @author xinzhi.zhang
 * */
public interface BasicService {
    /**
     * Find object with row key.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * 
     * @return POJO.
     * */
    public <T> T findObject(RowKey rowKey, Class<? extends T> type);

    /**
     * Find object with row key.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO.
     * */
    public <T> T findObject(RowKey rowKey, Class<? extends T> type,
            QueryExtInfo queryExtInfo);

    /**
     * Dynamic query to find POJO.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * 
     * @return POJO.
     * */
    public <T> T findObject(RowKey rowKey, Class<? extends T> type, String id,
            @Nullable Map<String, Object> para);

    /**
     * Dynamic query to find POJO.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO.
     * */
    public <T> T findObject(RowKey rowKey, Class<? extends T> type, String id,
            @Nullable Map<String, Object> para, QueryExtInfo queryExtInfo);

    /**
     * Find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @return POJO list.
     * */
    public <T> List<T> findObjectList(RowKey startRowKey, RowKey endRowKey,
            Class<? extends T> type);

    /**
     * Find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO list.
     * */
    public <T> List<T> findObjectList(RowKey startRowKey, RowKey endRowKey,
            Class<? extends T> type, QueryExtInfo queryExtInfo);

    /**
     * Dynamic query to find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * 
     * @return POJO list.
     * */
    public <T> List<T> findObjectList(RowKey startRowKey, RowKey endRowKey,
            Class<? extends T> type, String id,
            @Nullable Map<String, Object> para);

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
     * @return POJO list.
     * */
    public <T> List<T> findObjectList(RowKey startRowKey, RowKey endRowKey,
            Class<? extends T> type, String id,
            @Nullable Map<String, Object> para, QueryExtInfo queryExtInfo);
    
    
    //
    /**
     * Find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @return POJO list.
     * */
    public <T> List<T> findObjectList(RowKey startRowKey, long pageSize, Class<? extends T> type);

    /**
     * Find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO list.
     * */
    public <T> List<T> findObjectList(RowKey startRowKey, long pageSize,
            Class<? extends T> type, QueryExtInfo queryExtInfo);

    /**
     * Dynamic query to find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * 
     * @return POJO list.
     * */
    public <T> List<T> findObjectList(RowKey startRowKey, long pageSize,
            Class<? extends T> type, String id,
            @Nullable Map<String, Object> para);

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
     * @return POJO list.
     * */
    public <T> List<T> findObjectList(RowKey startRowKey, long pageSize,
            Class<? extends T> type, String id,
            @Nullable Map<String, Object> para, QueryExtInfo queryExtInfo);
    //

    /**
     * Find object and row key with row key.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * 
     * @return POJO and key.
     * */
    public <T> SimpleHbaseDOWithKeyResult<T> findObjectAndKey(RowKey rowKey, Class<? extends T> type);

    /**
     * Find object and row key with row key.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO and key.
     * */
    public <T> SimpleHbaseDOWithKeyResult<T> findObjectAndKey(RowKey rowKey,
            Class<? extends T> type, QueryExtInfo queryExtInfo);

    /**
     * Dynamic query to find POJO and row key.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * 
     * @return POJO and key.
     * */
    public <T> SimpleHbaseDOWithKeyResult<T> findObjectAndKey(RowKey rowKey,
            Class<? extends T> type, String id,
            @Nullable Map<String, Object> para);

    /**
     * Dynamic query to find POJO and row key.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO and key.
     * */
    public <T> SimpleHbaseDOWithKeyResult<T> findObjectAndKey(RowKey rowKey,
            Class<? extends T> type, String id,
            @Nullable Map<String, Object> para, QueryExtInfo queryExtInfo);

    /**
     * Find POJO and row key list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @return POJO and key list.
     * */
    public <T> List<SimpleHbaseDOWithKeyResult<T>> findObjectAndKeyList(
            RowKey startRowKey, RowKey endRowKey, Class<? extends T> type);

    /**
     * Find POJO and row key list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO and key list.
     * */
    public <T> List<SimpleHbaseDOWithKeyResult<T>> findObjectAndKeyList(
            RowKey startRowKey, RowKey endRowKey, Class<? extends T> type,
            QueryExtInfo queryExtInfo);

    /**
     * Dynamic query to find POJO and row key list with range in
     * [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * 
     * @return POJO and key list.
     * */
    public <T> List<SimpleHbaseDOWithKeyResult<T>> findObjectAndKeyList(
            RowKey startRowKey, RowKey endRowKey, Class<? extends T> type,
            String id, @Nullable Map<String, Object> para);

    /**
     * Dynamic query to find POJO and row key list with range in
     * [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO and key list.
     * */
    public <T> List<SimpleHbaseDOWithKeyResult<T>> findObjectAndKeyList(
            RowKey startRowKey, RowKey endRowKey, Class<? extends T> type,
            String id, @Nullable Map<String, Object> para,
            QueryExtInfo queryExtInfo);
    
    
    //
    /**
     * Find POJO and row key list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @return POJO and key list.
     * */
    public <T> List<SimpleHbaseDOWithKeyResult<T>> findObjectAndKeyList(
            RowKey startRowKey, long pageSize, Class<? extends T> type);

    /**
     * Find POJO and row key list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO and key list.
     * */
    public <T> List<SimpleHbaseDOWithKeyResult<T>> findObjectAndKeyList(
            RowKey startRowKey, long pageSize, Class<? extends T> type,
            QueryExtInfo queryExtInfo);

    /**
     * Dynamic query to find POJO and row key list with range in
     * [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * 
     * @return POJO and key list.
     * */
    public <T> List<SimpleHbaseDOWithKeyResult<T>> findObjectAndKeyList(
            RowKey startRowKey, long pageSize, Class<? extends T> type,
            String id, @Nullable Map<String, Object> para);

    /**
     * Dynamic query to find POJO and row key list with range in
     * [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO and key list.
     * */
    public <T> List<SimpleHbaseDOWithKeyResult<T>> findObjectAndKeyList(
            RowKey startRowKey, long pageSize, Class<? extends T> type,
            String id, @Nullable Map<String, Object> para,
            QueryExtInfo queryExtInfo);

    /**
     * Find POJO in batch mode.
     * 
     * @param rowKeyList rowKeyList.
     * @param type type.
     * 
     * @return POJO list.
     * */
    public <T> List<T> findObjectBatch(List<RowKey> rowKeyList, Class<? extends T> type);

    /**
     * Find POJO and key in batch mode.
     * 
     * @param rowKeyList rowKeyList.
     * @param type type.
     * 
     * @return POJO and key list.
     * */
    public <T> List<SimpleHbaseDOWithKeyResult<T>> findObjectAndKeyBatch(
            List<RowKey> rowKeyList, Class<? extends T> type);

    /**
     * Put POJO.
     * 
     * @param rowKey rowKey.
     * @param t POJO.
     * */
    public <T> void putObject(RowKey rowKey, T t);

    /**
     * Put POJO list.
     * 
     * @param putRequestList putObjectList.
     * */
    public <T> void putObjectList(List<PutRequest<T>> putRequestList);

    /**
     * Delete POJO.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * */
    public void deleteObject(RowKey rowKey, Class<?> type);
    
    /**
     * Delete POJO.
     * 
     * @param rowKey rowKey.
     * @param type POJO type.
     * */
    public void deleteObject(RowKey rowKey, Class<?> type, boolean isDeleteColumn);

    /**
     * Delete POJO list.
     * 
     * @param rowKeyList rowKeyList.
     * @param type POJO type.
     * */
    public void deleteObjectList(List<RowKey> rowKeyList, Class<?> type);
    
    /**
     * Delete POJO list.
     * 
     * @param rowKeyList rowKeyList.
     * @param type POJO type.
     * */
    public void deleteObjectList(List<RowKey> rowKeyList, Class<?> type, boolean isDeleteColumn);

    /**
     * Batch delete POJO list.
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * 
     * */
    public void deleteObjectList(RowKey startRowKey, RowKey endRowKey, Class<?> type);
    
    /**
     * Batch delete POJO list.
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * 
     * */
    public void deleteObjectList(RowKey startRowKey, RowKey endRowKey, Class<?> type, boolean isDeleteColumn);
    
    
    //分页查询:针对小数据量的分页查询
    
    /**
     * Find POJO and row key list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @return POJO and key list.
     * */
    public <T> Pagination<T> findPageAndKeyList(
            RowKey startRowKey, RowKey endRowKey, Class<? extends T> type,
            long pageNo, long pageSize);

    /**
     * Find POJO and row key list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO and key list.
     * */
    public <T> Pagination<T> findPageAndKeyList(
            RowKey startRowKey, RowKey endRowKey, Class<? extends T> type,
            QueryExtInfo queryExtInfo,
            long pageNo, long pageSize);

    /**
     * Dynamic query to find POJO and row key list with range in
     * [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * 
     * @return POJO and key list.
     * */
    public <T> Pagination<T> findPageAndKeyList(
            RowKey startRowKey, RowKey endRowKey, Class<? extends T> type,
            String id, @Nullable Map<String, Object> para,
            long pageNo, long pageSize);

    /**
     * Dynamic query to find POJO and row key list with range in
     * [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO and key list.
     * */
    public <T> Pagination<T> findPageAndKeyList(
            RowKey startRowKey, RowKey endRowKey, Class<? extends T> type,
            String id, @Nullable Map<String, Object> para,
            QueryExtInfo queryExtInfo,
            long pageNo, long pageSize);
    
    
    
    /**
     * Find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @return POJO list.
     * */
    public <T> Pagination<T> findPageList(RowKey startRowKey, RowKey endRowKey,
            Class<? extends T> type,
            long pageNo, long pageSize);

    /**
     * Find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param queryExtInfo queryExtInfo.
     * 
     * @return POJO list.
     * */
    public <T> Pagination<T> findPageList(RowKey startRowKey, RowKey endRowKey,
            Class<? extends T> type, QueryExtInfo queryExtInfo,
            long pageNo, long pageSize);

    /**
     * Dynamic query to find POJO list with range in [startRowKey,endRowKey).
     * 
     * @param startRowKey startRowKey.
     * @param endRowKey endRowKey.
     * @param type POJO type.
     * @param id dynamic query id.
     * @param para parameter map.
     * 
     * @return POJO list.
     * */
    public <T> Pagination<T> findPageList(RowKey startRowKey, RowKey endRowKey,
            Class<? extends T> type, String id,
            @Nullable Map<String, Object> para,
            long pageNo, long pageSize);

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
     * @return POJO list.
     * */
    public <T> Pagination<T> findPageList(RowKey startRowKey, RowKey endRowKey,
            Class<? extends T> type, String id,
            @Nullable Map<String, Object> para, QueryExtInfo queryExtInfo,
            long pageNo, long pageSize);
}
