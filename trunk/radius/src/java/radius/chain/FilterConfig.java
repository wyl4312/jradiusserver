/**
 * 
 */
package radius.chain;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class FilterConfig{
	private String name;
	private HashMap params;

	public FilterConfig() {
		params = new HashMap();
	}
	
	public FilterConfig(String name) {
		this.name = name;
		params = new HashMap();
	}
	
	public void setFilterName(String name) {
		this.name = name;
	}
	
	public String getFilterName() {
		return name;
	}
	
	public void addInitParameter(String name,String value) {
		params.put(name,value);
	}

	public String getInitParameter(String name) {
		return (String) params.get(name);
	}

	public Iterator iterateParameterName() {
		return params.keySet().iterator();
	}

}