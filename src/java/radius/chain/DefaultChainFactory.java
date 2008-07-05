package radius.chain;

import radius.chain.impl.FilterChainImpl;

public class DefaultChainFactory implements ChainFactory{

	public FilterChain createChain() {
		return new FilterChainImpl();
	}

}
