/**
 * 
 */
package radius.chain;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public interface Filter {

	void init(FilterConfig config);
	
	void doFilter(Object context,FilterChain chain);
	
	void destroy();
	
}
