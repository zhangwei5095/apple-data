package com.appleframework.data.hbase.client.service.basicService.hql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.appleframework.data.hbase.myrecord.MyRecord;
import com.appleframework.data.hbase.myrecord.MyRecordRowKey;
import com.appleframework.data.hbase.myrecord.MyRecordTestBase;

/**
 * @author xinzhi
 */
public class TestWarpper extends MyRecordTestBase {

    @Test
    public void testConstants() {
        putSlim("id=0,name=aaa");
        putSlim("id=1,name=bbb");
        putSlim("id=2,name=bbb");

        addHql("select where ( name equal \"aaa\" )");

        List<MyRecord> myRecordList = simpleHbaseClient.findObjectList(
                new MyRecordRowKey(0), new MyRecordRowKey(100), MyRecord.class,
                TestHqlId, null);

        Assert.assertTrue(myRecordList.size() == 1);

        addHql("select where ( name equal \"bbb\" )");
        myRecordList = simpleHbaseClient.findObjectList(new MyRecordRowKey(0),
                new MyRecordRowKey(100), MyRecord.class, TestHqlId, null);
        Assert.assertTrue(myRecordList.size() == 2);

        addHql("select where ( name equal \"ccc\" )");
        myRecordList = simpleHbaseClient.findObjectList(new MyRecordRowKey(0),
                new MyRecordRowKey(100), MyRecord.class, TestHqlId, null);
        Assert.assertTrue(myRecordList.size() == 0);
    }

    @Test
    public void testVar() {
        putSlim("id=0,name=aaa");
        putSlim("id=1,name=bbb");
        putSlim("id=2,name=bbb");

        addHql("select where ( name equal #name# )");
        Map<String, Object> para = new HashMap<String, Object>();

        para.put("name", "aaa");
        List<MyRecord> myRecordList = simpleHbaseClient.findObjectList(
                new MyRecordRowKey(0), new MyRecordRowKey(100), MyRecord.class,
                TestHqlId, para);
        Assert.assertTrue(myRecordList.size() == 1);

        para.put("name", "bbb");
        myRecordList = simpleHbaseClient.findObjectList(new MyRecordRowKey(0),
                new MyRecordRowKey(100), MyRecord.class, TestHqlId, para);
        Assert.assertTrue(myRecordList.size() == 2);

        para.put("name", "ccc");
        myRecordList = simpleHbaseClient.findObjectList(new MyRecordRowKey(0),
                new MyRecordRowKey(100), MyRecord.class, TestHqlId, para);
        Assert.assertTrue(myRecordList.size() == 0);
    }

}
