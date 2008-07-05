package radius.dict;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class FileVendorDictFactory implements VendorDictFactory {
    public final static String FILE_VENDOR_DICT_FACTORY_KEY = "vendor.dict.file.path";

    public final static String VENDOR_DICT_FILE_PATH = "/raddict.dat";

    /** VENDOR <vendor_name><vendor_id>[ <attr_type>] */
    public final static String VENDOR_REGEX = "^VENDOR\\s+([\\w-_]+)\\s+(\\d+)(\\s+([\\w-_]+))?$";

    /** ATTRIBUTE <attr_name><attr_id><attr_type>[ <vendor>] */
    public final static String ATTRIBUTE_REGEX = "^ATTRIBUTE\\s+([\\w-_]+)\\s+(\\d+)\\s+([string|ipaddr|date|tag\\-str|tag\\-int]+)(\\s+([\\w-_]+))?$";

    /** VALUE <attr-name><value-enum-name><value-integer> */
    public final static String VALUE_REGEX = "^VALUE\\s+([\\w-_]+)\\s+([\\w-_]+)(\\s+([\\d]+))?$";

    private static Pattern vendorPattern = Pattern.compile(VENDOR_REGEX,
            Pattern.CASE_INSENSITIVE);

    private static Pattern valuePattern = Pattern.compile(VALUE_REGEX,
            Pattern.CASE_INSENSITIVE);

    private static Pattern attributePattern = Pattern.compile(ATTRIBUTE_REGEX,
            Pattern.CASE_INSENSITIVE);

    private Object lock = new Object();

    private Map vendorIdMap;

    private Map vendorNameMap;

    private Map valueMap;

    private Map tempVendorIdMap;

    private Map tempVendorNameMap;

    private Map tempValueMap;

    private boolean loadded;

    private Log log = LogFactory.getLog(getClass());

    FileVendorDictFactory() {
        vendorIdMap = new HashMap();
        vendorNameMap = new HashMap();
        valueMap = new HashMap();
    }

    public VendorDict getVendorDict(String vendorName) {
        synchronized (lock) {
            return (VendorDict) vendorNameMap.get(vendorName);
        }
    }

    public VendorDict getVendorDict(int vendorId) {
        synchronized (lock) {
            return (VendorDict) vendorNameMap.get(new Integer(vendorId));
        }
    }

    public void load() {
        synchronized (lock) {
            if (!loadded) {
                doLoad();
                loadded = true;
            }
        }
    }

    void doLoad() {
        String filePath = System.getProperty(FILE_VENDOR_DICT_FACTORY_KEY,
                VENDOR_DICT_FILE_PATH);
        try {
            InputStream in = null;
            in = getClass().getResourceAsStream(filePath);
            if (in == null) {
                in = new FileInputStream(filePath);
            }
            if (in==null)
                return;
            tempVendorIdMap = new HashMap();
            tempVendorNameMap = new HashMap();
            tempValueMap = new HashMap();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            VendorDict normal = new VendorDict();
            normal.setId(VendorDict.NORMAL_VENDOR_ID);
            normal.setName(VendorDict.NORMAL_VENDOR_NAME);
            addVendor(normal);
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || "".equals(line))
                    continue;
                StringTokenizer token = new StringTokenizer(line);
                Matcher m = vendorPattern.matcher(line);
                if (m.matches()) {
                    VendorDict vendor = new VendorDict();
                    vendor.setName(m.group(1));
                    vendor.setId(Integer.parseInt(m.group(2)));
                    vendor.setType(m.group(4));
                    addVendor(vendor);
                    continue;
                }

                m = attributePattern.matcher(line);
                if (m.matches()) {
                    AttributeDict ad = new AttributeDict();
                    ad.setAttributeName(m.group(1));
                    ad.setAttributeId(Integer.parseInt(m.group(2)));
                    ad.setAttributeType(m.group(3));
                    ad.setVendor(m.group(5));
                    addAttribute(ad);
                    continue;
                }

                m = valuePattern.matcher(line);
                if (m.matches()) {
                    ValueDict vd = new ValueDict();
                    vd.setAttributeName(m.group(1));
                    vd.setValueEnumName(m.group(2));
                    vd.setValue(Integer.parseInt(m.group(4)));

                }
            }
            vendorIdMap = tempVendorIdMap;
            vendorNameMap = tempVendorNameMap;
            valueMap = tempValueMap;
        } catch (IOException e) {
            log.warn("can't load vendor dict:" + e.getMessage());
        } finally {
            tempVendorIdMap = null;
            tempVendorNameMap = null;
            tempValueMap = null;
        }
    }

    private void addValue(ValueDict vd) {
        HashMap map = (HashMap) valueMap.get(vd.getAttributeName());
        if (map != null) {
            map = new HashMap();
            valueMap.put(vd.getAttributeName(), map);
        }
        map.put(vd.getValueEnumName(), vd);
    }

    private void addAttribute(AttributeDict ad) {
        VendorDict vendor = (VendorDict) tempVendorNameMap
                .get(VendorDict.NORMAL_VENDOR_NAME);
        if (ad.getVendor() != null) {
            VendorDict vd = (VendorDict) tempVendorNameMap.get(ad.getVendor());
            if (vd != null) {
                vendor = vd;
            }
            vendor.addAttributeDict(ad);
        } else {// basic attribute
            Iterator ite = tempVendorNameMap.values().iterator();
            while (ite.hasNext()) {
                vendor = (VendorDict) ite.next();
                vendor.addAttributeDict(ad);
            }
        }

    }

    private void addVendor(VendorDict vendor) {
        tempVendorIdMap.put(new Integer(vendor.getId()), vendor);
        tempVendorNameMap.put(vendor.getName(), vendor);
    }

    public void reload() {
        synchronized (lock) {
            doLoad();
        }
    }

    public ValueDict getValueDict(String name, String enumName) {
        Map map = (Map) valueMap.get(name);
        if (map != null) {
            return (ValueDict) map.get(enumName);
        } else {
            return null;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator ite = vendorIdMap.values().iterator();
        while (ite.hasNext()) {
            sb.append(ite.next()).append("\n");
        }
        ite = valueMap.values().iterator();
        while (ite.hasNext()) {
            sb.append(ite.next()).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // Pattern p = Pattern.compile(VENDOR_REGEX);
        // Matcher m = p
        // .matcher("VENDOR ACC 5 ietf "
        // .trim());
        // if (m.matches()) {
        // System.out.println("find");
        // System.out.println("count=" + m.groupCount());
        // for (int i = 1; i <= m.groupCount(); i++) {
        // System.out.println("group=" + m.group(i));
        // }
        // }
        // p = Pattern.compile(ATTRIBUTE_REGEX, Pattern.CASE_INSENSITIVE);
        // m = p
        // .matcher("ATTRIBUTE User-Name 1 tag-str 3Com"
        // .trim());
        // if (m.matches()) {
        // System.out.println("find");
        // System.out.println("count=" + m.groupCount());
        // for (int i = 1; i <= m.groupCount(); i++) {
        // System.out.println("group=" + m.group(i));
        // }
        // }
        //
        // p = Pattern.compile(VALUE_REGEX);
        // m = p
        // .matcher("VALUE MS-Acct-Auth-Type PAP 1 "
        // .trim());
        // if (m.matches()) {
        // System.out.println("find");
        // System.out.println("count=" + m.groupCount());
        // for (int i = 1; i <= m.groupCount(); i++) {
        // System.out.println("group=" + m.group(i));
        // }
        // }
        FileVendorDictFactory f = new FileVendorDictFactory();
        f.load();
        System.out.println(f.toString());
    }
}