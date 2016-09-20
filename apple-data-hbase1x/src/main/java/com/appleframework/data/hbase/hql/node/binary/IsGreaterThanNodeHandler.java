package com.appleframework.data.hbase.hql.node.binary;

import org.w3c.dom.Node;

import com.appleframework.data.hbase.hql.HQLNode;
/**
 * @author xinzhi
 */
public class IsGreaterThanNodeHandler extends BinaryNodeHandler {

    @Override
    public HQLNode handle(Node node) {
        IsGreaterThanNode isGreaterThanNode = new IsGreaterThanNode();
        super.handle(isGreaterThanNode, node);
        return isGreaterThanNode;
    }
}
