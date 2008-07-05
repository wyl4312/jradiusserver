/*
 * 创建日期 2005-5-4
 *
 */
package radius.util;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public final class StringUtil {
    public static final String EMPTY = "";
    
    public static int toInt(String s) {
        return toInt(s,0);
    }

    public static int toInt(String s,int defaultInt) {
        if (s==null || EMPTY.equals(s)) {
            return defaultInt;
        }
        try {
            return Integer.parseInt(s);
        }catch (Exception e) {
            return defaultInt;
        }
    }
    
    public static boolean toBoolean(String s) {
        return toBoolean(s,false);
    }
    
    public static boolean toBoolean(String s,boolean defaultBoolean) {
        if (s==null || EMPTY.equals(s)) {
            return defaultBoolean;
        }
        try {
            return Boolean.valueOf(s).booleanValue();
        }catch (Exception e) {
            return defaultBoolean;
        }
    }
    
    public static String null2empty(String s) {
        return s==null?EMPTY:s;
    }
    
    private StringUtil(){}
}
