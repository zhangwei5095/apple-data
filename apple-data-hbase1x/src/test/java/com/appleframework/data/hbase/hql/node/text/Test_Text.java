package com.appleframework.data.hbase.hql.node.text;

import org.junit.Test;

import com.appleframework.data.hbase.config.SimpleHbaseRuntimeSetting;
import com.appleframework.data.hbase.hql.HQLNode;
import com.appleframework.data.hbase.hql.node.HQLTestBase;

/**
 * @author xinzhi
 */
public class Test_Text extends HQLTestBase {

    @Test
    public void test() {
        HQLNode hqlNode = findStatementHQLNode("test_text");
        hqlNode.applyParaMap(para, sb, context, new SimpleHbaseRuntimeSetting());
        assertEqualHQL("hello world to allen .", sb.toString());
    }
}
