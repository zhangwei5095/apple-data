package com.appleframework.data.hbase.hql.node.text;

import org.w3c.dom.Node;

import com.appleframework.data.hbase.hql.HQLNode;
/**
 * @author xinzhi
 */
public class TextNodeHandler extends BaseTextNodeHandler {

    @Override
    public HQLNode handle(Node node) {
        TextNode textNode = new TextNode();
        super.handle(textNode, node);
        return textNode;
    }

}
