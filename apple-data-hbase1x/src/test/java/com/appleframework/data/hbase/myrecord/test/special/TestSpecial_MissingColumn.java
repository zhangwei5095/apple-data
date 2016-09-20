package com.appleframework.data.hbase.myrecord.test.special;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.appleframework.data.hbase.myrecord.MyFatRecord;
import com.appleframework.data.hbase.myrecord.MyRecordTestBase;

import com.appleframework.data.hbase.myrecord.MyRecordRowKey;

/**
 * @author xinzhi
 */
public class TestSpecial_MissingColumn extends MyRecordTestBase {

    @Test
    public void less() {
        putSlim("id=0");
        putFat("id=1,fatname=aaa");
        putFat("id=2,fatname=bbb");

        addHql("select where fatname less #name#");
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("name", "ccc");

        List<MyFatRecord> myRecordList = simpleHbaseClient.findObjectList(
                new MyRecordRowKey(0), new MyRecordRowKey(100),
                MyFatRecord.class, TestHqlId, para);
        Assert.assertTrue(myRecordList.size() == 2);
    }

    @Test
    public void equal() {
        putSlim("id=0");
        putFat("id=1,fatname=aaa");
        putFat("id=2,fatname=bbb");

        addHql("select where fatname equal #name#");
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("name", "");

        List<MyFatRecord> myRecordList = simpleHbaseClient.findObjectList(
                new MyRecordRowKey(0), new MyRecordRowKey(100),
                MyFatRecord.class, TestHqlId, para);
        Assert.assertTrue(myRecordList.size() == 0);
    }

    @Test
    public void notequal() {
        putSlim("id=0");
        putFat("id=1,fatname=aaa");
        putFat("id=2,fatname=bbb");

        addHql("select where fatname notequal #name#");
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("name", "");

        List<MyFatRecord> myRecordList = simpleHbaseClient.findObjectList(
                new MyRecordRowKey(0), new MyRecordRowKey(100),
                MyFatRecord.class, TestHqlId, para);
        Assert.assertTrue(myRecordList.size() == 2);
    }

    @Test
    public void isnull() {
        putSlim("id=0");
        putFat("id=1,fatname=aaa");
        putFat("id=2,fatname=bbb");

        addHql("select where fatname isnull");

        List<MyFatRecord> myRecordList = simpleHbaseClient.findObjectList(
                new MyRecordRowKey(0), new MyRecordRowKey(100),
                MyFatRecord.class, TestHqlId, null);
        Assert.assertTrue(myRecordList.size() == 0);
    }

    @Test
    public void isnotnull() {
        putSlim("id=0");
        putFat("id=1,fatname=aaa");
        putFat("id=2,fatname=bbb");

        addHql("select where fatname isnotnull");

        List<MyFatRecord> myRecordList = simpleHbaseClient.findObjectList(
                new MyRecordRowKey(0), new MyRecordRowKey(100),
                MyFatRecord.class, TestHqlId, null);
        Assert.assertTrue(myRecordList.size() == 2);
    }
}
