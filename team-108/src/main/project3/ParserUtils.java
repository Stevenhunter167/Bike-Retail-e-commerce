package main.project3;

import org.w3c.dom.NodeList;

import org.w3c.dom.Element;

public class ParserUtils {

    static public String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    }


    static public int getIntValue(Element ele, String tagName) {
        //in production application you would catch the exception
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    static public double getDoubleValue(Element ele, String tagName) {
        return Double.parseDouble(getTextValue(ele, tagName));
    }

    static public Element getSubEl(Element ele, String tagName) {
        Element subEl = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            subEl = (Element) nl.item(0);
        }
        return subEl;
    }

}
