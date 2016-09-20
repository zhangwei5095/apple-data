package com.appleframework.data.hbase.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;

import com.appleframework.data.hbase.client.rowkey.handler.RowKeyHandler;
import com.appleframework.data.hbase.config.HBaseColumnSchema;
import com.appleframework.data.hbase.config.HBaseDataSource;
import com.appleframework.data.hbase.config.HBaseTableConfig;
import com.appleframework.data.hbase.config.SimpleHbaseRuntimeSetting;
import com.appleframework.data.hbase.core.Nullable;
import com.appleframework.data.hbase.exception.SimpleHBaseException;
import com.appleframework.data.hbase.type.TypeHandler;
import com.appleframework.data.hbase.util.Util;

/**
 * SimpleHbaseClient's skeleton implementation.
 * 
 * @author xinzhi
 * */
@SuppressWarnings("deprecation")
public abstract class SimpleHbaseClientBase implements SimpleHbaseClient {
	
    protected HBaseDataSource           hbaseDataSource;
    protected HBaseTableConfig          hbaseTableConfig;
    protected SimpleHbaseRuntimeSetting simpleHbaseRuntimeSetting = new SimpleHbaseRuntimeSetting();

    /**
     * Get HTableInterface.
     * */
    protected HTableInterface htableInterface() {
        return hbaseDataSource.getHTable(hbaseTableConfig.getHbaseTableSchema().getTableName());
    }
    
    protected boolean getAutoFlush() {
        return hbaseTableConfig.isAutoFlush();
    }
    
    protected void closeHTable(HTableInterface table) {
    	try {
    		if (hbaseDataSource.getTableFactory() != null) {
        		hbaseDataSource.getTableFactory().releaseHTableInterface(table);
    		}
    		else {
    			table.close();
    		}
    	} catch (Exception e) {
			
		}
    }

    /**
     * Get AggregationClient.
     * */
    protected AggregationClient aggregationClient() {
        AggregationClient aggregationClient = new AggregationClient(hbaseDataSource.getHbaseConfiguration());
        return aggregationClient;
    }

    /**
     * Get table name bytes.
     **/
    protected byte[] tableNameBytes() {
        return hbaseTableConfig.getHbaseTableSchema().getTableNameBytes();
    }

    /**
     * Find HBaseColumnSchema by family and qualifier.
     * */
    protected HBaseColumnSchema columnSchema(String family, String qualifier) {
        return hbaseTableConfig.getHbaseTableSchema().findColumnSchema(family, qualifier);
    }

    /**
     * Find HBaseColumnSchema by qualifier.
     * */
    protected HBaseColumnSchema columnSchema(String qualifier) {
        return hbaseTableConfig.getHbaseTableSchema().findColumnSchema(qualifier);
    }

    /**
     * Get scan's caching size.
     * */
    protected int getScanCaching() {
        return simpleHbaseRuntimeSetting.getScanCachingSize();
    }

    /**
     * Get batch size when do delete.
     * */
    protected int getDeleteBatch() {
        return simpleHbaseRuntimeSetting.getDeleteBatchSize();
    }

    /**
     * Construct Scan.
     * */
    protected Scan constructScan(RowKey startRowKey, RowKey endRowKey,
            @Nullable Filter filter, @Nullable QueryExtInfo queryExtInfo) {
        Util.checkRowKey(startRowKey);
        Util.checkRowKey(endRowKey);

        Scan scan = new Scan();
        scan.setStartRow(startRowKey.toBytes());
        scan.setStopRow(endRowKey.toBytes());

        int cachingSize = getScanCaching();

        if (simpleHbaseRuntimeSetting.isIntelligentScanSize()) {
            if (queryExtInfo != null && queryExtInfo.isLimitSet()) {
                long limitScanSize = queryExtInfo.getStartIndex() + queryExtInfo.getLength();
                if (limitScanSize > Integer.MAX_VALUE) {
                    cachingSize = Integer.MAX_VALUE;
                } else {
                    cachingSize = (int) limitScanSize;
                }
            }
        }

        scan.setCaching(cachingSize);
        scan.setFilter(filter);
        return postConstructScan(scan);
    }
    
    /**
     * Construct Scan.
     * */
    protected Scan constructScan(RowKey startRowKey, long pageSize, @Nullable Filter filter, @Nullable QueryExtInfo queryExtInfo) {
        Util.checkRowKey(startRowKey);

        Scan scan = new Scan();
        scan.setStartRow(startRowKey.toBytes());

        scan.setCaching((int)pageSize);
        scan.setFilter(filter);
        return postConstructScan(scan);
    }

    /**
     * Post construct Scan.
     * */
    protected Scan postConstructScan(Scan scan) {
        return scan;
    }

    /**
     * Construct Get.
     * */
    protected Get constructGet(RowKey rowkey, @Nullable Filter filter) {
        Util.checkRowKey(rowkey);

        Get get = new Get(rowkey.toBytes());
        get.setFilter(filter);
        return postConstructGet(get);
    }

    /**
     * Post construct Get.
     * */
    protected Get postConstructGet(Get get) {
        return get;
    }

    //FIXME [simplehbase] the columns in select list and condition can vary. 
    /**
     * Apply family and qualifier to scan request, to prevent return more data
     * than we need.
     * */
    protected <T> void applyRequestFamilyAndQualifier(Class<? extends T> type, Scan scan) {
        TypeInfo typeInfo = findTypeInfo(type);
        List<ColumnInfo> columnInfoList = typeInfo.getColumnInfos();
        for (ColumnInfo columnInfo : columnInfoList) {
            scan.addColumn(columnInfo.familyBytes, columnInfo.qualifierBytes);
        }
    }

    //FIXME [simplehbase] the columns in select list and condition can vary. 
    /**
     * Apply family and qualifier to scan request, to prevent return more data
     * than we need.
     * */
    protected <T> void applyRequestFamilyAndQualifier(Class<? extends T> type, Get get) {
        TypeInfo typeInfo = findTypeInfo(type);
        List<ColumnInfo> columnInfoList = typeInfo.getColumnInfos();
        for (ColumnInfo columnInfo : columnInfoList) {
            get.addColumn(columnInfo.familyBytes, columnInfo.qualifierBytes);
        }
    }

    //FIXME [simplehbase] the columns in select list and condition can vary. 
    /**
     * Apply family and qualifier to scan request, to prevent return more data
     * than we need.
     * */
    protected <T> void applyRequestFamilyAndQualifier(
            List<HBaseColumnSchema> hbaseColumnSchemaList, Scan scan) {
        for (HBaseColumnSchema hbaseColumnSchema : hbaseColumnSchemaList) {
            scan.addColumn(hbaseColumnSchema.getFamilyBytes(), hbaseColumnSchema.getQualifierBytes());
        }
    }

    //FIXME [simplehbase] the columns in select list and condition can vary. 
    /**
     * Apply family and qualifier to scan request, to prevent return more data
     * than we need.
     * */
    protected void applyRequestFamilyAndQualifier(Scan scan) {
        List<String> families = hbaseTableConfig.getHbaseTableSchema().findAllFamilies();
        for (String s : families) {
            scan.addFamily(Bytes.toBytes(s));
        }
    }

    /**
     * Convert hbase result to SimpleHbaseCellResult list.
     * 
     * @param hbaseResult hbase result.
     * 
     * @return SimpleHbaseCellResult list.
     * */
    protected List<SimpleHbaseCellResult> convertToSimpleHbaseCellResultList(Result hbaseResult) {

        Cell[] cells = hbaseResult.rawCells();
        if (cells == null || cells.length == 0) {
            return new ArrayList<SimpleHbaseCellResult>();
        }

        String familyStr = null;
        String qualifierStr = null;
        RowKeyHandler rowKeyHandler = null;

        try {
            List<SimpleHbaseCellResult> resultList = new ArrayList<SimpleHbaseCellResult>();

            for (Cell cell : cells) {

                byte[] familyBytes = CellUtil.cloneFamily(cell);
                familyStr = Bytes.toString(familyBytes);

                byte[] qualifierBytes = CellUtil.cloneQualifier(cell);
                qualifierStr = Bytes.toString(qualifierBytes);

                byte[] hbaseValue = CellUtil.cloneValue(cell);

                HBaseColumnSchema hbaseColumnSchema = columnSchema(familyStr, qualifierStr);
                TypeHandler typeHandler = hbaseColumnSchema.getTypeHandler();
                Object valueObject = typeHandler.toObject(hbaseColumnSchema.getType(), hbaseValue);

                long ts = cell.getTimestamp();
                Date tsDate = new Date(ts);

                SimpleHbaseCellResult cellResult = new SimpleHbaseCellResult();

                cellResult.setFamilyStr(familyStr);
                cellResult.setQualifierStr(qualifierStr);
                cellResult.setValueObject(valueObject);
                cellResult.setTsDate(tsDate);

                resultList.add(cellResult);
            }

            //reset to empty.
            familyStr = "";
            qualifierStr = "";

            byte[] row = CellUtil.cloneRow(cells[0]);

            rowKeyHandler = hbaseTableConfig.getHbaseTableSchema().getRowKeyHandler();
            RowKey rowKey = rowKeyHandler.convert(row);

            for (SimpleHbaseCellResult cell : resultList) {
                cell.setRowKey(rowKey);
            }

            return resultList;

        } catch (Exception e) {
            throw new SimpleHBaseException(
                    "convert result exception. familyStr=" + familyStr
                            + " qualifierStr=" + qualifierStr
                            + " rowKeyHandler=" + rowKeyHandler + " result="
                            + hbaseResult, e);
        }
    }

    /**
     * convert hhbase result to SimpleHbaseDOWithKeyResult.
     * 
     * @param hbaseResult hbase result.
     * @param type POJO type.
     * 
     * @return SimpleHbaseDOWithKeyResult.
     * */
    protected <T> SimpleHbaseDOWithKeyResult<T> convertToSimpleHbaseDOWithKeyResult(
            Result hbaseResult, Class<? extends T> type) {

        Cell[] cells = hbaseResult.rawCells();
        if (cells == null || cells.length == 0) {
            return null;
        }

        String familyStr = null;
        String qualifierStr = null;
        RowKeyHandler rowKeyHandler = null;

        try {

            TypeInfo typeInfo = findTypeInfo(type);
            T result = type.newInstance();

            for (Cell cell : cells) {

                byte[] familyBytes = CellUtil.cloneFamily(cell);
                familyStr = Bytes.toString(familyBytes);

                byte[] qualifierBytes = CellUtil.cloneQualifier(cell);
                qualifierStr = Bytes.toString(qualifierBytes);

                byte[] hbaseValue = CellUtil.cloneValue(cell);

                ColumnInfo columnInfo = typeInfo.findColumnInfo(familyStr, qualifierStr);

                HBaseColumnSchema hbaseColumnSchema = columnSchema(columnInfo.family, columnInfo.qualifier);

                TypeHandler typeHandler = hbaseColumnSchema.getTypeHandler();
                Object value = typeHandler.toObject(hbaseColumnSchema.getType(), hbaseValue);

                if (value != null) {
                    columnInfo.field.set(result, value);
                }
            }
            //reset to empty.
            familyStr = "";
            qualifierStr = "";

            byte[] row = CellUtil.cloneRow(cells[0]);

            rowKeyHandler = hbaseTableConfig.getHbaseTableSchema().getRowKeyHandler();
            RowKey rowKey = rowKeyHandler.convert(row);

            SimpleHbaseDOWithKeyResult<T> pojoWithKey = new SimpleHbaseDOWithKeyResult<T>();
            pojoWithKey.setRowKey(rowKey);
            pojoWithKey.setT(result);

            return pojoWithKey;

        } catch (Exception e) {
            throw new SimpleHBaseException(
                    "convert result exception. familyStr=" + familyStr
                            + " qualifierStr=" + qualifierStr
                            + " rowKeyHandler=" + rowKeyHandler + " result="
                            + hbaseResult + " type=" + type, e);
        }
    }

    /**
     * Convert hbase result to SimpleHbaseDOResult.
     * 
     * @param hbaseResult hbase result.
     * @param type POJO type.
     * 
     * @return SimpleHbaseDOResult list, timestamp desc ordered.
     * */
    protected <T> List<SimpleHbaseDOResult<T>> convertToSimpleHbaseDOResult(
            Result hbaseResult, Class<? extends T> type) {

        Cell[] cells = hbaseResult.rawCells();

        if (cells == null || cells.length == 0) {
            return new ArrayList<SimpleHbaseDOResult<T>>();
        }

        TreeMap<Long, T> temMap = new TreeMap<Long, T>(Collections.reverseOrder());

        TypeInfo typeInfo = findTypeInfo(type);

        String familyStr = null;
        String qualifierStr = null;
        RowKeyHandler rowKeyHandler = null;
        try {
            for (Cell cell : cells) {

                byte[] familyBytes = CellUtil.cloneFamily(cell);
                familyStr = Bytes.toString(familyBytes);

                byte[] qualifierBytes = CellUtil.cloneQualifier(cell);
                qualifierStr = Bytes.toString(qualifierBytes);

                byte[] hbaseValue = CellUtil.cloneValue(cell);

                long ts = cell.getTimestamp();

                if (!temMap.containsKey(ts)) {
                    temMap.put(ts, type.newInstance());
                }

                ColumnInfo columnInfo = typeInfo.findColumnInfo(familyStr, qualifierStr);

                HBaseColumnSchema hbaseColumnSchema = columnSchema(columnInfo.family, columnInfo.qualifier);

                TypeHandler typeHandler = hbaseColumnSchema.getTypeHandler();
                Object value = typeHandler.toObject(hbaseColumnSchema.getType(), hbaseValue);

                if (value != null) {
                    columnInfo.field.set(temMap.get(ts), value);
                }
            }
            //reset to empty.
            familyStr = "";
            qualifierStr = "";

            byte[] row = CellUtil.cloneRow(cells[0]);

            rowKeyHandler = hbaseTableConfig.getHbaseTableSchema().getRowKeyHandler();
            RowKey rowKey = rowKeyHandler.convert(row);

            List<SimpleHbaseDOResult<T>> result = new ArrayList<SimpleHbaseDOResult<T>>();

            for (Long ts : temMap.keySet()) {
                SimpleHbaseDOResult<T> r = new SimpleHbaseDOResult<T>();
                r.setRowKey(rowKey);
                r.setTimestamp(ts);
                r.setT(temMap.get(ts));
                result.add(r);
            }
            return result;
        } catch (Exception e) {
            throw new SimpleHBaseException(
                    "convert result exception. familyStr=" + familyStr
                            + " qualifierStr=" + qualifierStr
                            + " rowKeyHandler=" + rowKeyHandler + " result="
                            + hbaseResult + " type=" + type, e);
        }
    }

    /**
     * Convert t's field to bytes.
     * */
    protected <T> byte[] convertPOJOFieldToBytes(T t, ColumnInfo columnInfo) {
        try {
            Object value = columnInfo.field.get(t);
            return convertValueToBytes(value, columnInfo);
        } catch (Exception e) {
            throw new SimpleHBaseException(e);
        }
    }

    /**
     * Convert value to bytes.
     * */
    protected byte[] convertValueToBytes(Object value, ColumnInfo columnInfo) {
        HBaseColumnSchema hbaseColumnSchema = columnSchema(columnInfo.family, columnInfo.qualifier);
        return convertValueToBytes(value, hbaseColumnSchema);
    }

    /**
     * Convert value to bytes.
     * */
    protected byte[] convertValueToBytes(Object value, HBaseColumnSchema hbaseColumnSchema) {
        TypeHandler typeHandler = hbaseColumnSchema.getTypeHandler();
        return typeHandler.toBytes(hbaseColumnSchema.getType(), value);
    }

    /**
     * Check for typeInfo is versioned typeInfo.
     * */
    protected void checkVersioned(TypeInfo typeInfo) {

        if (!typeInfo.isVersionedType()) {
            throw new SimpleHBaseException("not a versioned type. typeInfo = " + typeInfo);
        }
    }

    /**
     * Check for table name.
     * */
    protected void checkTableName(String tableName) {
        Util.checkEquals(tableName, hbaseTableConfig.getHbaseTableSchema().getTableName());
    }

    /**
     * Find type info.
     * */
    protected TypeInfo findTypeInfo(Class<?> type) {
        return getHbaseTableConfig().findTypeInfo(type);
    }

    @Override
    public HTableInterface getTable(String tableName) {
        return hbaseDataSource.getHTable(tableName);
    }

    @Override
    public HBaseAdmin getHBaseAdmin() {
        return hbaseDataSource.getHBaseAdmin();
    }

    @Override
    public HBaseTableConfig getHbaseTableConfig() {
        return hbaseTableConfig;
    }

    @Override
    public void setHbaseTableConfig(HBaseTableConfig hbaseTableConfig) {
        this.hbaseTableConfig = hbaseTableConfig;
    }

    @Override
    public HBaseDataSource getHbaseDataSource() {
        return hbaseDataSource;
    }

    @Override
    public void setHbaseDataSource(HBaseDataSource hbaseDataSource) {
        this.hbaseDataSource = hbaseDataSource;
    }

    @Override
    public void setSimpleHbaseRuntimeSetting(SimpleHbaseRuntimeSetting simpleHbaseRuntimeSetting) {
        this.simpleHbaseRuntimeSetting = simpleHbaseRuntimeSetting;
    }

    @Override
    public SimpleHbaseRuntimeSetting getSimpleHbaseRuntimeSetting() {
        return simpleHbaseRuntimeSetting;
    }
}
