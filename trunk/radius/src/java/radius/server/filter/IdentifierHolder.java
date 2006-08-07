package radius.server.filter;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class IdentifierHolder {
	private Log log = LogFactory.getLog(getClass());
	private Map nas;
	private IdComparator comparator = new IdComparator();
	private int maxTime;
	
	public IdentifierHolder(int maxTime) {
		nas = new WeakHashMap();
		this.maxTime = maxTime;
	}
	
	public boolean hasIdentifier(String ip,int port,int id) {
		long cur = System.currentTimeMillis();
		Integer key = new Integer((port<<8) | (id&0xff));
		Map  map = (Map) nas.get(ip);
		if (map==null) {
			map = new TreeMap(comparator);
			map.put(key,new Long(cur));
			nas.put(ip,map);
			return false;
		}
		else {
			Long last = (Long) map.get(key);
			if (last!=null) {
				if (cur-last.longValue()<maxTime) {
					return true;
				}
				else {
					map.put(key,new Long(cur));
					return false;
				}
			}
			else {
				map.put(key,new Long(cur));
				return false;
			}
		}
	}
	
	private class IdComparator implements Comparator{

		public int compare(Object o1, Object o2) {
			int id1 = ((Integer) o1).intValue();
			int id2 = ((Integer) o2).intValue();
			return id1-id2;
		}
		
	}
	

}
