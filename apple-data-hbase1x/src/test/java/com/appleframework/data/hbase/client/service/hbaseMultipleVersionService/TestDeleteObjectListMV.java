package com.appleframework.data.hbase.client.service.hbaseMultipleVersionService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import allen.studyhbase.TimeDepend;
import allen.test.CreateTestTable;

import com.appleframework.data.hbase.client.DeleteRequest;
import com.appleframework.data.hbase.client.QueryExtInfo;
import com.appleframework.data.hbase.client.RowKey;
import com.appleframework.data.hbase.client.SimpleHbaseDOResult;
import com.appleframework.data.hbase.myrecord.MyRecord;
import com.appleframework.data.hbase.myrecord.MyRecordRowKey;
import com.appleframework.data.hbase.myrecord.MyRecordTestBase;

/**
 * @author xinzhi
 * */
public class TestDeleteObjectListMV extends MyRecordTestBase {

    private void fillData() {

        MyRecord[] myRecords = mockSlims(5);

        for (int i = 0; i < myRecords.length; i++) {
            simpleHbaseClient.putObjectMV(myRecords[i].rowKey(), myRecords[i],
                    1L);
            simpleHbaseClient.putObjectMV(myRecords[i].rowKey(), myRecords[i],
                    2L);
            simpleHbaseClient.putObjectMV(myRecords[i].rowKey(), myRecords[i],
                    3L);
        }
    }

    @TimeDepend
    @Test
    public void deleteObjectMV() {
        CreateTestTable.main(null);
        fillData();
        QueryExtInfo queryExtInfo = new QueryExtInfo();
        queryExtInfo.setMaxVersions(3);

        List<List<SimpleHbaseDOResult<MyRecord>>> resultList = simpleHbaseClient
                .findObjectListMV(new MyRecordRowKey(0), new MyRecordRowKey(5),
                        MyRecord.class, queryExtInfo);
        assertSize(resultList, 5, 3, 3, 3, 3, 3);

        List<RowKey> rowKeyList = new ArrayList<RowKey>();
        rowKeyList.add(new MyRecordRowKey(0));
        rowKeyList.add(new MyRecordRowKey(1));
        simpleHbaseClient.deleteObjectListMV(rowKeyList, MyRecord.class, 2L);

        resultList = simpleHbaseClient.findObjectListMV(new MyRecordRowKey(0),
                new MyRecordRowKey(5), MyRecord.class, queryExtInfo);
        assertSize(resultList, 5, 2, 2, 3, 3, 3);

        List<DeleteRequest> deleteRequestList = new ArrayList<DeleteRequest>();
        deleteRequestList.add(new DeleteRequest(new MyRecordRowKey(2), 3L));
        deleteRequestList.add(new DeleteRequest(new MyRecordRowKey(3), 3L));
        deleteRequestList.add(new DeleteRequest(new MyRecordRowKey(4), 3L));
        deleteRequestList.add(new DeleteRequest(new MyRecordRowKey(4), 2L));
        simpleHbaseClient.deleteObjectListMV(deleteRequestList, MyRecord.class);
        resultList = simpleHbaseClient.findObjectListMV(new MyRecordRowKey(0),
                new MyRecordRowKey(5), MyRecord.class, queryExtInfo);
        assertSize(resultList, 5, 2, 2, 2, 2, 1);

        CreateTestTable.main(null);
    }

    private void assertSize(
            List<List<SimpleHbaseDOResult<MyRecord>>> resultList,
            int totalSize, Integer... subListSize) {
        Assert.assertEquals(totalSize, resultList.size());
        for (int i = 0; i < subListSize.length; i++) {
            Assert.assertEquals(subListSize[i].intValue(), resultList.get(i)
                    .size());
        }
    }
}
