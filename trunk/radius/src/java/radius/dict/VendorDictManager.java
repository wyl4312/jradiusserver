/*
 * 创建日期 2005-5-5
 *
 */
package radius.dict;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class VendorDictManager {

    public static final String VENDOR_DICT_FACTORY_IMPL_KEY = "radius.dict.vendorfactory";

    public static final String DEFAULT_VENDOR_DICT_FACTORY_IMPL = "radius.dict.FileVendorDictFactory";

    private static VendorDictFactory factory;

    private static Object lock = new Object();
    
    private static Log log = LogFactory.getLog(VendorDictManager.class);

    public static VendorDictFactory getFactory() {
        if (factory == null) {
            synchronized (lock) {
                init();
            }
        }
        return factory;
    }
    
    public static void init() {
        if (factory == null) {
            log.info("init vendor dict factory");
            String clazz = System
                    .getProperty(VENDOR_DICT_FACTORY_IMPL_KEY);
            if (clazz == null) {
                clazz = DEFAULT_VENDOR_DICT_FACTORY_IMPL;
            }
            factory = loadFactory(clazz);
        }
    }

    private static VendorDictFactory loadFactory(String clazz) {
        try {
            VendorDictFactory factory = (VendorDictFactory) Class
                    .forName(clazz).newInstance();
            return factory;
        } catch (Exception e) {
            log.warn("invalid vendor factory impl:"+clazz+",use default implement");
            return loadFactory(DEFAULT_VENDOR_DICT_FACTORY_IMPL);
        }
    }
}