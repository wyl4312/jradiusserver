package radius.server;
import radius.server.cfg.*;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		cfg.config();
		RadiusServer server = cfg.buildServer();
		server.start();
	}

}
