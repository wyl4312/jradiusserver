package radius.server.cfg;

import java.util.Iterator;
import java.util.Properties;

import org.dom4j.Element;

import radius.util.Dom4jUtil;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class ImplConfig extends Properties {
	private String clazz;

	public ImplConfig() {
	}
	
	public ImplConfig(Element node) {
	    this.clazz = Dom4jUtil.getAttribute(node,"class");
	    Iterator iterator = node.elementIterator("property");
	    while (iterator.hasNext()) {
            Element property = (Element) iterator.next();
            String name = Dom4jUtil.getAttribute(property,"name");
            String value = property.getText();
            setProperty(name,value);
        }
	}

	/**
	 * @return Returns the clazz.
	 */
	public String getClazz() {
		return clazz;
	}

	/**
	 * @param clazz
	 *            The clazz to set.
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
	public int getPropertyAsInt(String name) {
	    String s = getProperty(name);
	    if (s==null || "".endsWith(s))
	        return 0;
	    return Integer.parseInt(s); 
	}
	
	public String toString() {
	    StringBuffer sb = new StringBuffer();
	    
	    sb.append("class=").append(clazz);
	    
	    Iterator ite = keySet().iterator();
	    int count = 0; 
	    sb.append(",properties:");
	    while (ite.hasNext()) {
            String name = (String) ite.next();
            if (count>0) {
                sb.append(",");
            }
            sb.append("name=").append(name);
            sb.append(",value=").append(getProperty(name));
            count++;
        }
	    
	    return sb.toString();
	}
}
