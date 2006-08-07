package radius.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.chain.Filter;
import radius.chain.FilterChain;
import radius.server.filter.AddressFilter;
import radius.server.filter.ConsumeFilter;
import radius.server.filter.DecodePacketFilter;
import radius.server.filter.EncodePacketFilter;
import radius.server.filter.ReleaseContextFilter;
import radius.server.filter.ServiceFilter;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class ProcessChain implements FilterChain {
	private static Comparator filterComparator = new FilterCompare();

	private static Log log = LogFactory.getLog(ProcessChain.class);

	private ArrayList filters;

	public ProcessChain() {
		filters = new ArrayList();
		addFilter(new ReleaseContextFilter());
		// prepare
		addFilter(new AddressFilter());
		// addFilter(new FormatCheckFilter());
		addFilter(new DecodePacketFilter());
		// addFilter(new IdFilter());
		// service
		addFilter(new ServiceFilter());
		// consume
		addFilter(new EncodePacketFilter());
		addFilter(new ConsumeFilter());
	}

	public synchronized void addFilter(Filter filter) {
		if (!(filter instanceof RadiusFilter)) {
			log.warn(filter + " is not a radius filter,ignore");
			return;
		}
		RadiusFilter rf = (RadiusFilter) filter;
		if (rf.getType() < 0 || rf.getType() > RadiusFilter.SERVICE_TYPE) {
			log.warn("unknown radius filter type:" + rf.getType()
					+ ",ignore this filter");
			return;
		}
		log.info("add radius filter:" + filter);
		ArrayList list = new ArrayList(filters);
		list.add(filter);
		Collections.sort(list, filterComparator);
		filters = list;
	}

	public synchronized void removeFilter(int index) {
		ArrayList list = new ArrayList(filters);
		list.remove(index);
		Collections.sort(list, filterComparator);
		filters = list;
	}

	public void doFilter(Object context) {
		log.info("do filter for context:" + context);
		assert context != null && context instanceof RadiusContext : "not a RadiusContext";
		ChainFacade facade = new ChainFacade(this);
		try {
			facade.doFilter(context);
		} catch (Throwable t) {
			log.error("doFilter failed:"+t.getMessage(),t);
		}
	}

	private static class ChainFacade implements FilterChain {
		private ArrayList filters;

		private int cur;

		public ChainFacade(ProcessChain target) {
			this.filters = target.filters;
		}

		public void addFilter(Filter filter) {
			throw new UnsupportedOperationException();
		}

		public void removeFilter(int index) {
			throw new UnsupportedOperationException();
		}

		public void doFilter(Object context) {
			if (cur < filters.size()) {
				Filter f = (Filter) filters.get(cur);
				cur++;
				f.doFilter(context, this);
			}
		}

	}

	private static class FilterCompare implements Comparator {

		public int compare(Object o1, Object o2) {
			RadiusFilter f1 = (RadiusFilter) o1;
			RadiusFilter f2 = (RadiusFilter) o2;
			return f1.getType() - f2.getType();
		}

	}

}
