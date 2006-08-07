/*
 * 创建日期 2005-5-4
 *
 */
package radius.util;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class Dom4jUtil {

    public static String getAttribute(Element element, String attributeName,
            String defaultValue) {
        Attribute att = element.attribute(attributeName);
        if (att != null) {
            return att.getValue();
        }
        return defaultValue;
    }

    public static String getAttribute(Element element, String attributeName) {
        return getAttribute(element, attributeName, StringUtil.EMPTY);
    }

    public static int getAttribute2Int(Element element, String attributeName) {
        return getAttribute2Int(element, attributeName, 0);
    }

    public static int getAttribute2Int(Element element, String attributeName,
            int defaultValue) {
        String s = getAttribute(element, attributeName);
        return StringUtil.toInt(s, defaultValue);
    }

    public static boolean getAttribute2Boolean(Element element,
            String attributeName) {
        return getAttribute2Boolean(element, attributeName, false);
    }

    public static boolean getAttribute2Boolean(Element element,
            String attributeName, boolean defaultValue) {
        String s = getAttribute(element, attributeName);
        return StringUtil.toBoolean(s, defaultValue);
    }

    public static String getSubNodeValue(Element element, String subNodeName) {
        Element subNode = element.element(subNodeName);
        if (subNode != null) {
            return subNode.getText();
        } else {
            return StringUtil.EMPTY;
        }
    }

    public static int getSubNodeValue2Int(Element element, String subNodeName,
            int defaultValue) {
        String s = getSubNodeValue(element, subNodeName);
        return StringUtil.toInt(s, defaultValue);
    }

    public static int getSubNodeValue2Int(Element element, String subNodeName) {
        String s = getSubNodeValue(element, subNodeName);
        return StringUtil.toInt(s, 0);
    }

    public static boolean getSubNodeValue2Boolean(Element element,
            String subNodeName, boolean defaultValue) {
        String s = getSubNodeValue(element, subNodeName);
        return StringUtil.toBoolean(s, defaultValue);
    }

    public static boolean getSubNodeValue2Boolean(Element element,
            String subNodeName) {
        String s = getSubNodeValue(element, subNodeName);
        return StringUtil.toBoolean(s, false);
    }

    private Dom4jUtil() {
    }

}