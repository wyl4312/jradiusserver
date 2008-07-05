package chain.impl;

import radius.chain.Filter;
import radius.chain.FilterChain;
import radius.chain.FilterConfig;
import radius.chain.impl.FilterChainImpl;
import junit.framework.TestCase;

public class FilterChainImplTest extends TestCase {
	private FilterChainImpl impl;
	private boolean next;
	private int count;

	protected void setUp() throws Exception {
		super.setUp();
		impl = new FilterChainImpl();
		impl.addFilter(new SimpleFilter());
		impl.addFilter(new SimpleFilter());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDoFilter() {
		next = true;
		count = 0;
		impl.doFilter(null);
		assertEquals(2,count);
		next = false;
		count = 0;
		impl.doFilter(null);
		assertEquals(1,count);
	}
	
	class SimpleFilter implements Filter {

		public void init(FilterConfig config) {
			
		}

		public void doFilter(Object context, FilterChain chain) {
			count++;
			if (next) {
				chain.doFilter(context);
			}
		}

		public void destroy() {
			
		}
		
	}

}
