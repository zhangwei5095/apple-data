package com.appleframework.data.hbase.hql.node;

import org.junit.Test;

import com.appleframework.data.hbase.config.SimpleHbaseRuntimeSetting;
import com.appleframework.data.hbase.hql.HQLNode;

/**
 * @author xinzhi
 */
public class Test_Dynamic3 extends HQLTestBase {

    @Test
    public void test_0() {
        HQLNode hqlNode = findStatementHQLNode("test_dynamic3");
        para.put("c", null);

        hqlNode.applyParaMap(para, sb, context, new SimpleHbaseRuntimeSetting());
        assertEqualHQL("allen hi love cup", sb.toString());
    }

    @Test
    public void test_1() {
        HQLNode hqlNode = findStatementHQLNode("test_dynamic3");
        para.put("c", null);
        para.put("d", null);

        hqlNode.applyParaMap(para, sb, context, new SimpleHbaseRuntimeSetting());
        assertEqualHQL("allen hi love hello dandan cup", sb.toString());
    }

    @Test
    public void test_2() {
        HQLNode hqlNode = findStatementHQLNode("test_dynamic3");
        para.put("c", null);
        para.put("d", null);
        para.put("e", null);

        hqlNode.applyParaMap(para, sb, context, new SimpleHbaseRuntimeSetting());
        assertEqualHQL("allen hi love hello dandan cup cat linzhi",
                sb.toString());
    }

    @Test
    public void test_3() {
        HQLNode hqlNode = findStatementHQLNode("test_dynamic3");
        para.put("c", null);
        para.put("e", null);

        hqlNode.applyParaMap(para, sb, context, new SimpleHbaseRuntimeSetting());
        assertEqualHQL("allen hi love cup hello linzhi", sb.toString());
    }

}
