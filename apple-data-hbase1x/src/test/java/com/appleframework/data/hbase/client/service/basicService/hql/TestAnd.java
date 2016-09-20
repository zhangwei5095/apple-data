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
public class TestAnd extends MyRecordTestBase {

    @Test
    public void testConstants() {
        putSlim("id=0,name=aaa,age=10");
        putSlim("id=1,name=bbb,age=11");
        putSlim("id=2,name=ccc,age=12");

        addHql("select where name greater \"aaa\" and age less \"12\"");

        List<MyRecord> myRecordList = simpleHbaseClient.findObjectList(
                new MyRecordRowKey(0), new MyRecordRowKey(100), MyRecord.class,
                TestHqlId, null);
        Assert.assertTrue(myRecordList.size() == 1);

        addHql("select where name greater \"bbb\" and age less \"12\"");
        myRecordList = simpleHbaseClient.findObjectList(new MyRecordRowKey(0),
                new MyRecordRowKey(100), MyRecord.class, TestHqlId, null);
        Assert.assertTrue(myRecordList.size() == 0);

        addHql("select where name greater \"ccc\" and age less \"12\"");
        myRecordList = simpleHbaseClient.findObjectList(new MyRecordRowKey(0),
                new MyRecordRowKey(100), MyRecord.class, TestHqlId, null);
        Assert.assertTrue(myRecordList.size() == 0);
    }

    @Test
    public void testVar() {
        putSlim("id=0,name=aaa,age=10");
        putSlim("id=1,name=bbb,age=11");
        putSlim("id=2,name=ccc,age=12");

        addHql("select where name greater #name# and age less #age#");
        Map<String, Object> para = new HashMap<String, Object>();

        para.put("name", "aaa");
        para.put("age", 12L);

        List<MyRecord> myRecordList = simpleHbaseClient.findObjectList(
                new MyRecordRowKey(0), new MyRecordRowKey(100), MyRecord.class,
                TestHqlId, para);
        Assert.assertTrue(myRecordList.size() == 1);

        para.put("name", "bbb");
        para.put("age", 12L);
        myRecordList = simpleHbaseClient.findObjectList(new MyRecordRowKey(0),
                new MyRecordRowKey(100), MyRecord.class, TestHqlId, para);
        Assert.assertTrue(myRecordList.size() == 0);

        para.put("name", "ccc");
        para.put("age", 12L);
        myRecordList = simpleHbaseClient.findObjectList(new MyRecordRowKey(0),
                new MyRecordRowKey(100), MyRecord.class, TestHqlId, para);
        Assert.assertTrue(myRecordList.size() == 0);

    }
}
