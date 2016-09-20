package com.appleframework.data.hbase.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.w3c.dom.Node;

import com.appleframework.data.hbase.client.HBaseTable;
import com.appleframework.data.hbase.client.TypeInfo;
import com.appleframework.data.hbase.core.NotNullable;
import com.appleframework.data.hbase.core.Nullable;
import com.appleframework.data.hbase.exception.SimpleHBaseException;
import com.appleframework.data.hbase.hql.HBaseQuery;
import com.appleframework.data.hbase.util.StringUtil;
import com.appleframework.data.hbase.util.Util;
import com.appleframework.data.hbase.util.XmlUtil;

/**
 * HbaseTable's config info.
 * 
 * <pre>
 * Including following info.
 * 1 hbaseTableSchema Table's schema.
 * 2 queryMap HQL collection.
 * 3 mapping JOPO's info.
 * </pre>
 * 
 * @author xinzhi
 * */
public class HBaseTableConfig {
	
    /** log. */
    final private static Logger               log              = Logger.getLogger(HBaseTableConfig.class);

    //------------bean config-------------------
    /**
     * Config resource.
     * */
    @ConfigAttr
    private Resource                          configResource;
    
    private boolean                           autoFlush        = true;


    //------------bean config-------------------
    private HBaseTableSchema                  hbaseTableSchema = new HBaseTableSchema();

    private ConcurrentMap<String, HBaseQuery> queryMap         = new ConcurrentHashMap<String, HBaseQuery>();

    /**
     * type info mapping of class.
     */
    private ConcurrentMap<Class<?>, TypeInfo> mappingTypes     = new ConcurrentHashMap<Class<?>, TypeInfo>();

    private String tableName;
    
    /**
     * Init this object.
     * */
    public void init() {
        Util.checkNull(configResource);

        try {
            List<HBaseColumnSchema> hbaseColumnSchemas = new ArrayList<HBaseColumnSchema>();
            HBaseTableConfigParser.parseTableSchema(configResource.getInputStream(), hbaseTableSchema, hbaseColumnSchemas);
            hbaseTableSchema.init(hbaseColumnSchemas);
            if(StringUtil.isNotEmptyString(tableName)) {
                hbaseTableSchema.setTableName(tableName);
            }
            
            List<HBaseQuery> hbaseQueries = HBaseTableConfigParser.parseHBaseQuery(configResource.getInputStream());

            addHBaseQueryList(hbaseQueries);

            List<Node> typeInfoNodes = XmlUtil.findTopLevelNodes(configResource.getInputStream(), "MappingType");

            for (Node typeInfoNode : typeInfoNodes) {
                TypeInfo typeInfo = TypeInfo.parseNode(typeInfoNode, hbaseTableSchema);
                addTypeInfo(typeInfo);
            }

            log.info(this);

        } catch (Exception e) {
            log.error("parseConfig error.", e);
            throw new SimpleHBaseException("parseConfig error.", e);
        }
    }

    /**
     * Find type info.
     * 
     * <pre>
     * There are 3 ways to config type info .
     * 1 in config xml.
     * 2 use @HBaseTable.
     * 3 simplehase will use reflection to discover the mapping.
     * </pre>
     */
    @Nullable
    public TypeInfo findTypeInfo(@NotNullable Class<?> type) {
        Util.checkNull(type);
        TypeInfo result = mappingTypes.get(type);

        if (result != null) {
            return result;
        }

        HBaseTable hbaseTable = type.getAnnotation(HBaseTable.class);
        if (hbaseTable != null) {
            result = TypeInfo.parse(type);
            addTypeInfo(result);
            return result;
        }

        result = TypeInfo.parseInAir(type, hbaseTableSchema);
        if (result != null) {
            addTypeInfo(result);
            return result;
        }

        throw new SimpleHBaseException("can't find type info. type=" + type);
    }

    private void addTypeInfo(TypeInfo typeInfo) {
        log.info("register TypeInfo\n" + typeInfo);
        mappingTypes.putIfAbsent(typeInfo.getType(), typeInfo);
    }

    /**
     * add hbaseQueryList.
     * */
    public void addHBaseQueryList(List<HBaseQuery> hbaseQueryList) {
        for (HBaseQuery hBaseQuery : hbaseQueryList) {
            queryMap.put(hBaseQuery.getId(), hBaseQuery);
        }
    }

    public Resource getConfigResource() {
        return configResource;
    }

    public void setConfigResource(Resource configResource) {
        this.configResource = configResource;
    }

    public HBaseTableSchema getHbaseTableSchema() {
        return hbaseTableSchema;
    }

    public Map<String, HBaseQuery> getQueryMap() {
        return queryMap;
    }    

    public boolean isAutoFlush() {
		return autoFlush;
	}

	public void setAutoFlush(boolean autoFlush) {
		this.autoFlush = autoFlush;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(hbaseTableSchema.toString());
        return sb.toString();
    }
}
