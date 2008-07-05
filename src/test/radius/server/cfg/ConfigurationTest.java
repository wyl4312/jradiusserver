package radius.server.cfg;

import junit.framework.TestCase;

public class ConfigurationTest extends TestCase {

	private Configuration cfg;
	
	public void setUp() throws Exception {
		super.setUp();
		cfg = new Configuration();
	}
	
	/*
	 * Class under test for void config()
	 */
	public void testConfig() {
		cfg.config();
		cfg.buildServer();
	}

	/*
	 * Class under test for void config(InputStream)
	 */
	public void testConfigInputStream() {
	}

	/*
	 * Class under test for void config(Document)
	 */
	public void testConfigDocument() {
	}

	public void testBuildServer() {
	}

}
