package radius.server;


/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 * 
 */
public interface RadiusService {
	
	int getType();

	boolean service(RadiusContext context);

}
