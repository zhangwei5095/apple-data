package com.appleframework.data.hbase.hql.node.binary;

import org.junit.Test;

import com.appleframework.data.hbase.config.SimpleHbaseRuntimeSetting;
import com.appleframework.data.hbase.hql.HQLNode;
import com.appleframework.data.hbase.hql.node.HQLTestBase;
import com.appleframework.data.hbase.util.DateUtil;

/**
 * @author xinzhi
 */
public class Test_IsGreaterEqual extends HQLTestBase {
    @Test
    public void test_0() {
        HQLNode hqlNode = findStatementHQLNode("test_isGreaterEqual");
        para.put("name", "alice");
        para.put("age", 11);
        para.put("date", DateUtil.parse("2013-09-01", DateUtil.DayFormat));
        hqlNode.applyParaMap(para, sb, context, new SimpleHbaseRuntimeSetting());
        assertEqualHQL("allen", sb.toString());
    }

    @Test
    public void test_1() {
        HQLNode hqlNode = findStatementHQLNode("test_isGreaterEqual");
        para.put("name", "bob");
        para.put("age", 30);
        para.put("date", DateUtil.parse("2013-10-01", DateUtil.DayFormat));
        hqlNode.applyParaMap(para, sb, context, new SimpleHbaseRuntimeSetting());
        assertEqualHQL("allen AND MARRIED OR AGE OR tree", sb.toString());
    }

    @Test
    public void test_2() {
        HQLNode hqlNode = findStatementHQLNode("test_isGreaterEqual");
        para.put("name", "cat");
        para.put("age", 40);
        para.put("date", DateUtil.parse("2013-11-01", DateUtil.DayFormat));
        hqlNode.applyParaMap(para, sb, context, new SimpleHbaseRuntimeSetting());
        assertEqualHQL("allen AND MARRIED OR AGE OR tree", sb.toString());
    }

}
