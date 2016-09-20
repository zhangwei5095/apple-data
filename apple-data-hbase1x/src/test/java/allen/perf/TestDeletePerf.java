package allen.perf;

import org.junit.Test;

import allen.test.Config;

import com.appleframework.data.hbase.myrecord.MyRecord;
import com.appleframework.data.hbase.myrecord.MyRecordRowKey;
import com.appleframework.data.hbase.myrecord.MyRecordTestBase;

/**
 * @author xinzhi
 */
public class TestDeletePerf extends MyRecordTestBase {

    @Test
    public void deleteObjectListPerf() {

        int[] testSizes = new int[] { 1 };
        if (Config.isPerfTestOn) {
            testSizes = new int[] { 1, 10, 100, 1000, 2000, 5000, 10000 };
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < testSizes.length; i++) {
            final int size = testSizes[i];

            putMockSlims(size);

            long start = System.currentTimeMillis();
            for (int j = 0; j < size; j++) {
                simpleHbaseClient.deleteObject(new MyRecordRowKey(j),
                        MyRecord.class);
            }
            long end = System.currentTimeMillis();
            long singleDeleteTime = end - start;

            putMockSlims(size);

            start = System.currentTimeMillis();

            simpleHbaseClient.deleteObjectList(rowkeyList(0, size),
                    MyRecord.class);

            end = System.currentTimeMillis();
            long batchDeleteTime = end - start;

            sb.append("\ndeleteObjectListPerf size = " + size
                    + " singleDeleteTime=" + singleDeleteTime
                    + " batchDeleteTime=" + batchDeleteTime + "\n");
        }

        log.info(sb);
    }
}