package radius.server;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public interface RadiusDataConsumer extends RadiusDataProducer {

	void consume(RadiusContext context);

}
