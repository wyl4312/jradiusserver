package radius.server;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public interface PacketDispatcher extends RadiusDataConsumer{

	void setDispatchStrategy(DispatchStrategy strategy);
	
	public void setProcessChain(ProcessChain chain);

}
