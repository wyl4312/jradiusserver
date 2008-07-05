package radius.server.impl;

import radius.server.DispatchStrategy;
import radius.server.PacketDispatcher;
import radius.server.ProcessChain;
import radius.server.RadiusContext;
import radius.server.RadiusDataConsumer;

import radius.threadpool.*;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class PacketDispatcherImpl implements PacketDispatcher {
	private DispatchStrategy strategy;

	private RadiusDataConsumer consumer;

	private ProcessChain chain;

	private ThreadPool pool;

	private static class CustomThreadPool extends ThreadPoolConfig {
		private DispatchStrategy strategy;

		private int cpu;

		CustomThreadPool(DispatchStrategy strategy) {
			this.strategy = strategy;
			cpu = Runtime.getRuntime().availableProcessors();
		}

		public int getMinSize() {
			if (strategy.isAutoConfig()) {
				return cpu * 2;
			} else {
				return strategy.getMinThread();
			}
		}

		public int getMaxSize() {
			if (strategy.isAutoConfig()) {
				return cpu * 10;
			} else {
				return strategy.getMaxThread();
			}
		}

		public int getIncrement() {
			if (strategy.isAutoConfig()) {
				return cpu;
			} else {
				return strategy.getIncrement();
			}
		}
	}

	public void setDispatchStrategy(DispatchStrategy strategy) {
		if (this.strategy == null) {
			this.strategy = strategy;
			if (!strategy.isSingelThread()) {
				ThreadPoolConfig config = new CustomThreadPool(strategy);
				pool = ThreadPoolFactory.createThreadPool(config);
			}
		}
	}

	public void consume(final RadiusContext context) {
		if (pool == null) {
			chain.doFilter(context);
		} else {
			pool.execute(new Runnable() {

				public void run() {
					chain.doFilter(context);
				}

			});
		}
	}

	public void setConsumer(RadiusDataConsumer consumer) {
		this.consumer = consumer;
	}

	public void setProcessChain(ProcessChain chain) {
		this.chain = chain;
	}

}
