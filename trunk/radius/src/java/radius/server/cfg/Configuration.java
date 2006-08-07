package radius.server.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import radius.server.RadiusServer;
import radius.util.Dom4jUtil;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class Configuration {
    public static final String SERVER_CONFIG_PATH = "/RadiusServerCfg.xml";

    private static final String JMX_TAG = "jmx";

    private static final String LISTENS_TAG = "listens";

    private static final String LISTEN_TAG = "listen";

    private static final String DISPATCH_STRATEGY_TAG = "dispatchStrategy";

    private static final String FILTERS_TAG = "filters";

    private static final String FILTER_TAG = "filter";

    private static final String SERVICES_TAG = "services";

    private static final String SERVICE_TAG = "service";

    private static final String PROXY_TAG = "proxy";

    private static final String CONNECTION_PROVIDER_TAG = "connectionProvider";

    private Log log = LogFactory.getLog(getClass());

    private org.w3c.dom.Document doc;

    public void config() {
        log.info("config use default server config file");
        InputStream in = getClass().getResourceAsStream(SERVER_CONFIG_PATH);
        if (in != null) {
            config(in);
        } else {
            log.error("can't find config file,config failed");
        }
    }

    public void config(InputStream in) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setValidating(true);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new EntityResolver() {

                public InputSource resolveEntity(String publicId,
                        String systemId) throws SAXException, IOException {
                    InputStream in = getClass().getResourceAsStream(
                            "/radius/server/RadiusServerCfg.dtd");
                    if (in != null)
                        return new InputSource(in);
                    return null;
                }

            });
            org.w3c.dom.Document doc = builder.parse(in);
            config(doc);
        } catch (ParserConfigurationException e) {
            log.error("config failed:" + e.getMessage(), e);
        } catch (SAXException e) {
            log.error("config failed:" + e.getMessage(), e);
        } catch (IOException e) {
            log.error("config failed:" + e.getMessage(), e);
        }
    }

    public void config(org.w3c.dom.Document doc) {
        this.doc = doc;
    }

    public RadiusServer buildServer() {
        if (doc == null) {
            config();
        }
        if (doc == null) {
            log.fatal("config failed,can't build server!");
            throw new RuntimeException("config failed,can't build server!");
        }
        ServerBuilder builder = new ServerBuilder();

        DOMReader reader = new DOMReader();
        Document document = reader.read(doc);

        Element root = document.getRootElement();

        log.info("config jmx");
        Element jmxElement = root.element(JMX_TAG);
        if (jmxElement != null) {
            JmxConfig jc = JmxConfig.getJmxConfig(jmxElement);
            builder.withJmx(jc);
        }

        log.info("config listeners");
        Element listens = root.element(LISTENS_TAG);
        if (listens != null) {
            Iterator iterator = listens.elementIterator(LISTEN_TAG);
            while (iterator.hasNext()) {
                Element node = (Element) iterator.next();
                ListenConfig lc = ListenConfig.getListenConfig(node);
                builder.addListen(lc);
            }
        } else {
            log.info("listens not config,use default listeners");
            builder.addListen(ListenConfig.newAuthConfig());
            builder.addListen(ListenConfig.newAccountConfig());
        }

        log.info("config dispatchStrategy");
        Element strategy = root.element(DISPATCH_STRATEGY_TAG);
        if (strategy != null) {
            DispatchStrategyConfig dsc = DispatchStrategyConfig
                    .getDispatchStrategyConfig(strategy);
            builder.useDispatchStrategy(dsc);
        } else {
            log
                    .info("dispatchStrategy not config,use default dispatchStrategy");
            DispatchStrategyConfig dsc = new DispatchStrategyConfig();
            builder.useDispatchStrategy(dsc);
        }

        log.info("config filters");
        Element filters = root.element(FILTERS_TAG);
        if (filters != null) {
            Iterator iterator = filters.elementIterator(FILTER_TAG);
            while (iterator.hasNext()) {
                Element node = (Element) iterator.next();
                FilterConfig fc = new FilterConfig(node);
                builder.addFilter(fc);
            }
        }

        log.info("config connectionProvider");
        Element provider = root.element(CONNECTION_PROVIDER_TAG);
        ConnectionProviderConfig cpc = new ConnectionProviderConfig(provider);
        builder.withConnectionProvider(cpc);

        Iterator iterator = root.elementIterator("property");
        while (iterator.hasNext()) {
            Element prop = (Element) iterator.next();
            String name = Dom4jUtil.getAttribute(prop, "name");
            String value = prop.getTextTrim();
            builder.addProperty(name, value);
        }

        String type = Dom4jUtil.getAttribute(root, "type",
                ServerBuilder.NIO_SERVER);
        return builder.create(type);
    }

}