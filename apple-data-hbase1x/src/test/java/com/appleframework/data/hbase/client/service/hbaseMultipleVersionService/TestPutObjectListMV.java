package com.appleframework.data.hbase.client.service.hbaseMultipleVersionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import allen.studyhbase.TimeDepend;
import allen.test.CreateTestTable;

import com.appleframework.data.hbase.client.PutRequest;
import com.appleframework.data.hbase.client.RowKey;
import com.appleframework.data.hbase.client.SimpleHbaseDOResult;
import com.appleframework.data.hbase.myrecord.MyRecord;
import com.appleframework.data.hbase.myrecord.MyRecordTestBase;

/**
 * @author xinzhi
 * */
public class TestPutObjectListMV extends MyRecordTestBase {

    @TimeDepend
    @Test
    public void putObjectListMV() {
        CreateTestTable.main(null);
        MyRecord myRecord_1 = parseSlim("id=1000,name=namea,age=1");
        MyRecord myRecord_2 = parseSlim("id=2000,name=nameb,age=2");

        RowKey rowKey_1 = myRecord_1.rowKey();
        RowKey rowKey_2 = myRecord_2.rowKey();
        List<SimpleHbaseDOResult<MyRecord>> resultList = simpleHbaseClient
                .findObjectMV(rowKey_1, MyRecord.class, null);
        Assert.assertTrue(resultList.isEmpty());

        resultList = simpleHbaseClient.findObjectMV(rowKey_2, MyRecord.class,
                null);
        Assert.assertTrue(resultList.isEmpty());

        List<PutRequest<MyRecord>> putRequestList = new ArrayList<PutRequest<MyRecord>>();
        putRequestList.add(new PutRequest<MyRecord>(myRecord_1.rowKey(),
                myRecord_1));
        putRequestList.add(new PutRequest<MyRecord>(myRecord_2.rowKey(),
                myRecord_2));

        simpleHbaseClient.putObjectListMV(putRequestList, new Date());

        resultList = simpleHbaseClient.findObjectMV(rowKey_1, MyRecord.class,
                null);
        Assert.assertTrue(resultList.size() == 1);
        Assert.assertEquals(myRecord_1, resultList.get(0).getT());

        resultList = simpleHbaseClient.findObjectMV(rowKey_2, MyRecord.class,
                null);
        Assert.assertTrue(resultList.size() == 1);
        Assert.assertEquals(myRecord_2, resultList.get(0).getT());

        CreateTestTable.main(null);
    }

    @TimeDepend
    @Test
    public void putObjectListMV2() {
        CreateTestTable.main(null);

        MyRecord myRecord_1 = parseSlim("id=1000,name=namea,age=1");
        MyRecord myRecord_2 = parseSlim("id=2000,name=nameb,age=2");

        RowKey rowKey_1 = myRecord_1.rowKey();
        RowKey rowKey_2 = myRecord_2.rowKey();
        List<SimpleHbaseDOResult<MyRecord>> resultList = simpleHbaseClient
                .findObjectMV(rowKey_1, MyRecord.class, null);
        Assert.assertTrue(resultList.isEmpty());

        resultList = simpleHbaseClient.findObjectMV(rowKey_2, MyRecord.class,
                null);
        Assert.assertTrue(resultList.isEmpty());

        List<PutRequest<MyRecord>> putRequestList = new ArrayList<PutRequest<MyRecord>>();
        putRequestList.add(new PutRequest<MyRecord>(myRecord_1.rowKey(),
                myRecord_1, 1L));
        putRequestList.add(new PutRequest<MyRecord>(myRecord_2.rowKey(),
                myRecord_2, 2L));

        simpleHbaseClient.putObjectListMV(putRequestList);

        resultList = simpleHbaseClient.findObjectMV(rowKey_1, MyRecord.class,
                null);
        Assert.assertTrue(resultList.size() == 1);
        Assert.assertEquals(myRecord_1, resultList.get(0).getT());

        resultList = simpleHbaseClient.findObjectMV(rowKey_2, MyRecord.class,
                null);
        Assert.assertTrue(resultList.size() == 1);
        Assert.assertEquals(myRecord_2, resultList.get(0).getT());

        CreateTestTable.main(null);
    }
}
