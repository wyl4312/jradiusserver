/**
 * 
 */
package radius.chain;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public interface FilterChain {
	
	void addFilter(Filter filter);
	
	void removeFilter(int index);
	
	void doFilter(Object context);

}
