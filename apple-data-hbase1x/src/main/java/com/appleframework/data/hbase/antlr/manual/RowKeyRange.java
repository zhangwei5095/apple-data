package com.appleframework.data.hbase.antlr.manual;

import com.appleframework.data.hbase.client.RowKey;

/**
 * RowKeyRange.
 * 
 * @author xinzhi.zhang
 * */
public class RowKeyRange {

    private RowKey start;
    private RowKey end;

    public RowKey getStart() {
        return start;
    }

    public void setStart(RowKey start) {
        this.start = start;
    }

    public RowKey getEnd() {
        return end;
    }

    public void setEnd(RowKey end) {
        this.end = end;
    }

}
