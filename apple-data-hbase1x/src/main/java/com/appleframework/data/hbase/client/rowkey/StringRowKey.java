package com.appleframework.data.hbase.client.rowkey;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.hadoop.hbase.util.Bytes;

import com.appleframework.data.hbase.client.RowKey;
import com.appleframework.data.hbase.util.Util;

/**
 * StringRowKey.
 * 
 * @author xinzhi.zhang
 * */
public class StringRowKey implements RowKey {

    private String key;

    public StringRowKey(String key) {
        Util.checkNull(key);
        this.key = key;
    }

    @Override
    public byte[] toBytes() {
        return Bytes.toBytes(key);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
