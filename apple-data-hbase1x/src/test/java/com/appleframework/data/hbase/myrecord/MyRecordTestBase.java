package com.appleframework.data.hbase.myrecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import allen.test.Config;

import com.appleframework.data.hbase.client.PutRequest;
import com.appleframework.data.hbase.client.RowKey;
import com.appleframework.data.hbase.client.SimpleHbaseAdminClient;
import com.appleframework.data.hbase.client.SimpleHbaseClient;

import com.appleframework.data.hbase.config.HBaseTableConfigParser;
import com.appleframework.data.hbase.config.SimpleHbaseRuntimeSetting;

import com.appleframework.data.hbase.hql.HBaseQuery;

/**
 * @author xinzhi
 */
public class MyRecordTestBase {

    protected static Log                    log       = LogFactory
                                                              .getLog(MyRecordTestBase.class);
    protected static SimpleHbaseClient      simpleHbaseClient;
    protected static SimpleHbaseAdminClient simpleHbaseAdminClient;
    protected static String                 TestHqlId = "TestHqlId";

    static {
        simpleHbaseClient = Config.getSimpleHbaseClient();
        simpleHbaseAdminClient = Config.getSimpleHbaseAdminClient();
    }

    @Before
    public void before() {
        deleteRecords();
    }

    @After
    public void after() {
        deleteRecords();
    }

    private void deleteRecords() {
        simpleHbaseClient.delete("delete * from " + Config.TableName
                + " startkey is hbasestartkey , endkey is hbaseendkey");
    }

    protected void addHql(String hql) {
        List<HBaseQuery> hbaseQueryList = HBaseTableConfigParser
                .parseHBaseQueryWithRawHQL(hql, TestHqlId);
        simpleHbaseClient.getHbaseTableConfig().addHBaseQueryList(
                hbaseQueryList);
    }

    protected MyRecord mockSlim(int id) {
        return parseSlim("id=" + id + ",name=allen_" + id
                + ",date=2012-01-01,gender=MALE,version=0");
    }

    public MyRecord[] mockSlims(int size) {
        MyRecord[] myRecords = new MyRecord[size];
        for (int i = 0; i < myRecords.length; i++) {
            myRecords[i] = mockSlim(i);
        }
        return myRecords;
    }

    protected void putMockSlims(int size) {
        MyRecord[] myRecords = mockSlims(size);
        putRecords(myRecords);
    }

    protected List<RowKey> rowkeyList(int start, int length) {
        List<RowKey> list = new ArrayList<RowKey>();
        for (int i = start; i < start + length; i++) {
            list.add(new MyRecordRowKey(i));
        }
        return list;
    }

    protected List<RowKey> rowkeyList(int... ids) {
        List<RowKey> list = new ArrayList<RowKey>();
        for (int i : ids) {
            list.add(new MyRecordRowKey(i));
        }
        return list;
    }

    protected void putSlim(String str) {
        MyRecord myRecord = parseSlim(str);
        MyRecordRowKey myRecordRowKey = new MyRecordRowKey(myRecord.getId());
        simpleHbaseClient.putObject(myRecordRowKey, myRecord);
    }

    protected MyRecord parseSlim(String str) {
        SimpleHbaseRuntimeSetting runtimeSetting = new SimpleHbaseRuntimeSetting();
        MyRecord record = new MyRecord();

        String[] parts = str.split("[=,]");

        for (int i = 0; i < parts.length; i += 2) {

            if (parts[i].equals("id")) {
                record.setId((Integer) (runtimeSetting.interpret(int.class,
                        parts[i + 1])));
                continue;
            }

            if (parts[i].equals("name")) {
                record.setName(parts[i + 1]);
                continue;
            }

            if (parts[i].equals("date")) {
                record.setDate((Date) (runtimeSetting.interpret(Date.class,
                        parts[i + 1])));
                continue;
            }

            if (parts[i].equals("gender")) {
                record.setGender((Gender) (runtimeSetting.interpret(
                        Gender.class, parts[i + 1])));
                continue;
            }

            if (parts[i].equals("age")) {
                record.setAge((Long) (runtimeSetting.interpret(long.class,
                        parts[i + 1])));
                continue;
            }

            if (parts[i].equals("version")) {
                record.setVersion((Long) (runtimeSetting.interpret(long.class,
                        parts[i + 1])));
                continue;
            }

        }

        return record;
    }

    protected void putFat(String str) {
        MyFatRecord myRecord = parseFat(str);
        MyRecordRowKey myRecordRowKey = new MyRecordRowKey(myRecord.getId());
        simpleHbaseClient.putObject(myRecordRowKey, myRecord);
    }

    protected MyFatRecord parseFat(String str) {
        SimpleHbaseRuntimeSetting runtimeSetting = new SimpleHbaseRuntimeSetting();
        MyFatRecord record = new MyFatRecord();

        String[] parts = str.split("[=,]");

        for (int i = 0; i < parts.length; i += 2) {

            if (parts[i].equals("id")) {
                record.setId((Integer) (runtimeSetting.interpret(int.class,
                        parts[i + 1])));
                continue;
            }

            if (parts[i].equals("name")) {
                record.setName(parts[i + 1]);
                continue;
            }

            if (parts[i].equals("date")) {
                record.setDate((Date) (runtimeSetting.interpret(Date.class,
                        parts[i + 1])));
                continue;
            }

            if (parts[i].equals("gender")) {
                record.setGender((Gender) (runtimeSetting.interpret(
                        Gender.class, parts[i + 1])));
                continue;
            }

            if (parts[i].equals("age")) {
                record.setAge((Long) (runtimeSetting.interpret(long.class,
                        parts[i + 1])));
                continue;
            }

            if (parts[i].equals("version")) {
                record.setVersion((Long) (runtimeSetting.interpret(long.class,
                        parts[i + 1])));
                continue;
            }

            if (parts[i].equals("fatid")) {
                record.setFatid((Integer) (runtimeSetting.interpret(int.class,
                        parts[i + 1])));
                continue;
            }

            if (parts[i].equals("fatname")) {
                record.setFatname(parts[i + 1]);
                continue;
            }

            if (parts[i].equals("fatdate")) {
                record.setFatdate((Date) (runtimeSetting.interpret(Date.class,
                        parts[i + 1])));
                continue;
            }

            if (parts[i].equals("fatgender")) {
                record.setFatgender((Gender) (runtimeSetting.interpret(
                        Gender.class, parts[i + 1])));
                continue;
            }

            if (parts[i].equals("fatage")) {
                record.setFatage((Long) (runtimeSetting.interpret(long.class,
                        parts[i + 1])));
                continue;
            }

            if (parts[i].equals("fatversion")) {
                record.setFatversion((Long) (runtimeSetting.interpret(
                        long.class, parts[i + 1])));
                continue;
            }

        }

        return record;
    }

    protected void putRecord(MyRecord myRecord) {
        MyRecordRowKey myRecordRowKey = new MyRecordRowKey(myRecord.getId());
        simpleHbaseClient.putObject(myRecordRowKey, myRecord);
    }

    protected void putRecords(MyRecord[] myRecords) {
        List<PutRequest<MyRecord>> putRequests = new ArrayList<PutRequest<MyRecord>>();
        for (MyRecord myRecord : myRecords) {
            PutRequest<MyRecord> putRequest = new PutRequest<MyRecord>(
                    myRecord.rowKey(), myRecord);
            putRequests.add(putRequest);
        }

        simpleHbaseClient.putObjectList(putRequests);
    }

    protected void putRecord(MyFatRecord myFatRecord) {
        MyRecordRowKey myRecordRowKey = new MyRecordRowKey(myFatRecord.getId());
        simpleHbaseClient.putObject(myRecordRowKey, myFatRecord);
    }

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
