package com.appleframework.data.hbase.hql.node.text;

import org.w3c.dom.Node;

import com.appleframework.data.hbase.hql.HQLNodeHandler;
/**
 * @author xinzhi
 */
abstract public class BaseTextNodeHandler implements HQLNodeHandler {

    public void handle(BaseTextNode baseTextNode, Node node) {
        baseTextNode.setTextValue(node.getNodeValue());
    }
}
