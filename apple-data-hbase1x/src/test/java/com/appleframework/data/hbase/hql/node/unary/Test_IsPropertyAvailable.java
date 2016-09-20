package com.appleframework.data.hbase.hql.node.unary;

import org.junit.Test;

import com.appleframework.data.hbase.config.SimpleHbaseRuntimeSetting;
import com.appleframework.data.hbase.hql.HQLNode;
import com.appleframework.data.hbase.hql.node.HQLTestBase;

/**
 * @author xinzhi
 */
public class Test_IsPropertyAvailable extends HQLTestBase {

    @Test
    public void test_0() {
        HQLNode hqlNode = findStatementHQLNode("test_isPropertyAvailable");
        hqlNode.applyParaMap(para, sb, context, new SimpleHbaseRuntimeSetting());
        assertEqualHQL("allen", sb.toString());
    }

    @Test
    public void test_1() {
        HQLNode hqlNode = findStatementHQLNode("test_isPropertyAvailable");
        para.put("c", null);
        hqlNode.applyParaMap(para, sb, context, new SimpleHbaseRuntimeSetting());
        assertEqualHQL("allen  love dandan", sb.toString());
    }

}
