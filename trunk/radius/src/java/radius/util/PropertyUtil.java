/*
 * 创建日期 2005-5-6
 *
 */
package radius.util;

import java.util.Properties;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public class PropertyUtil {
    
    public static int getInt(Properties prop,String key,int defaultValue) {
        String s =prop.getProperty(key);
        return StringUtil.toInt(s,defaultValue);
    }
    
    public static boolean getBoolean(Properties prop,String key) {
        String s = prop.getProperty(key);
        return StringUtil.toBoolean(s);
    }
    
    private PropertyUtil(){}

}
