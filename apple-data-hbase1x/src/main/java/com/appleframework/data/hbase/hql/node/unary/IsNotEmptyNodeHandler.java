package com.appleframework.data.hbase.hql.node.unary;

import org.w3c.dom.Node;

import com.appleframework.data.hbase.hql.HQLNode;
/**
 * @author xinzhi
 */
public class IsNotEmptyNodeHandler extends UnaryNodeHandler {

    @Override
    public HQLNode handle(Node node) {
        IsNotEmptyNode isNotEmptyNode = new IsNotEmptyNode();
        super.handle(isNotEmptyNode, node);
        return isNotEmptyNode;
    }

}
