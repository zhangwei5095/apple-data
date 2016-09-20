package com.appleframework.data.hbase.hql;

import com.appleframework.data.hbase.util.Util;

/**
 * HBaseQuery.
 * 
 * @author xinzhi
 * */
public class HBaseQuery {

    private String  id;
    private HQLNode hqlNode;

    public HBaseQuery(String id, HQLNode hqlNode) {
        Util.checkEmptyString(id);
        Util.checkNull(hqlNode);

        this.id = id;
        this.hqlNode = hqlNode;
    }

    public String getId() {
        return id;
    }

    public HQLNode getHqlNode() {
        return hqlNode;
    }

}
