package radius.server.cfg;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.cache.CacheProviderFactory;
import radius.chain.Filter;
import radius.connection.ConnectionProviderFactory;
import radius.dict.VendorDictManager;
import radius.server.DispatchStrategy;
import radius.server.PacketDispatcher;
import radius.server.ProcessChain;
import radius.server.RadiusServer;
import radius.server.impl.NioServer;
import radius.server.impl.PacketDispatcherImpl;
import radius.server.impl.RadiusContextFactoryImpl;
import radius.server.impl.TradeServer;
import radius.server.jmx.JMXSupport;
import radius.server.jmx.ServerDecorator;
import radius.server.service.PersistFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class ServerBuilder {
    private Log log = LogFactory.getLog(getClass());

    public static final String NIO_SERVER = "nio";

    public static final String TRADE_SERVER = "trade";

    private JmxConfig jc;

    private List listenConfigs = new ArrayList();

    private DispatchStrategyConfig dsc;

    private DispatchStrategy dispatchStrategy;

    private PacketDispatcher dispatcher = new PacketDispatcherImpl();

    private List filterConfigs = new ArrayList();

    private ServiceConfig sc;

    private ConnectionProviderConfig cpc;

    private Properties prop = new Properties();

    private ProcessChain chain;

    public void withJmx(JmxConfig config) {
        this.jc = config;
        log.info("with jmx:" + config);
    }

    public void addListen(ListenConfig config) {
        this.listenConfigs.add(config);
        log.info("add listener:" + config);
    }

    public void useDispatchStrategy(DispatchStrategyConfig config) {
        this.dsc = config;
        dispatchStrategy = new DispatchStrategy();
        dispatchStrategy.setAutoConfig(config.isAutoConfig());
        dispatchStrategy.setSingelThread(config.isSingleThread());
        dispatchStrategy.setIncrement(config.getIncrement());
        dispatchStrategy.setMaxThread(config.getMaxThread());
        dispatchStrategy.setMinThread(config.getMinThread());
        dispatcher.setDispatchStrategy(dispatchStrategy);
        log.info("use dispatch strategy:" + config);
    }

    public void addFilter(FilterConfig config) {
        if (chain == null)
            chain = new ProcessChain();
        log.info("add filter:" + config);
        filterConfigs.add(config);
        String clazz = config.getClazz();
        try {
            Filter filter = (Filter) Class.forName(clazz).newInstance();
            radius.chain.FilterConfig fc = new radius.chain.FilterConfig();
            Iterator iterator = config.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                fc.addInitParameter(key, config.getProperty(key));
            }
            filter.init(fc);
            chain.addFilter(filter);
        } catch (Exception e) {
            log.warn("add filter failed:" + e.getMessage(), e);
        }
    }

    public void addService(ServiceConfig config) {
        log.info("add service:" + config);
        this.sc = config;
        String clazz = config.getClazz();
        try {
            Class.forName(clazz);
        } catch (Exception e) {
            log.warn("add service failed:" + e.getMessage(), e);
        }
    }

    public void withConnectionProvider(ConnectionProviderConfig config) {
        log.info("with connection provider:" + config);
        this.cpc = config;
    }

    public void addProperty(String name, String value) {
        log.info("add property name=" + name + ",value=" + value);
        prop.setProperty(name, value);
        System.setProperty(name, value);
    }

    public RadiusServer create() {
        return create(NIO_SERVER);
    }

    public RadiusServer create(String type) {
        if (chain == null)
            chain = new ProcessChain();
        log.info("create radius server,type=" + type);
        RadiusServer server = null;
        if (TRADE_SERVER.equalsIgnoreCase(type)) {
            server = new TradeServer();
            type = TRADE_SERVER;
        } else {
            server = new NioServer();
            type = NIO_SERVER;
        }
        Iterator ite = listenConfigs.iterator();
        while (ite.hasNext()) {
            ListenConfig config = (ListenConfig) ite.next();
            if (ListenConfig.AUTHENTICATION_TYPE.equals(config.getType())) {
                String ip = config.getIp();
                int port = config.getPort();
                InetSocketAddress ia = null;
                if (ip != null) {
                    ia = new InetSocketAddress(ip, port);
                } else {
                    ia = new InetSocketAddress(port);
                }
                server.enableAuth(ia);
            }
            if (ListenConfig.ACCOUNTING_TYPE.equals(config.getType())) {
                String ip = config.getIp();
                int port = config.getPort();
                InetSocketAddress ia = null;
                if (ip != null) {
                    ia = new InetSocketAddress(ip, port);
                } else {
                    ia = new InetSocketAddress(port);
                }
                server.enableAccount(ia);
            }
        }

        dispatcher.setProcessChain(chain);
        server.setConsumer(dispatcher);
        server.setRadiusContextFactory(new RadiusContextFactoryImpl());

        ConnectionProviderFactory.init(cpc.getClazz(), cpc);

        String pimpl = prop.getProperty(PersistFactory.PERSIST_IMPL_KEY);
        if (pimpl != null) {
            PersistFactory.init(pimpl);
        }

        String cacheImpl = prop
                .getProperty(CacheProviderFactory.CACHE_PROVIDER_IMPL_KEY);
        if (cacheImpl != null) {
            CacheProviderFactory.init(cacheImpl);
        } else {
            CacheProviderFactory.init();
        }

        VendorDictManager.init();

        if (jc != null && jc.isEnable()) {
            ServerDecorator sd = new ServerDecorator(server);
            try {
                JMXSupport.registerMBean(sd, new ObjectName(
                        "radiusserver:type=" + type));
                JMXSupport.start(jc.getType(), jc.getIp(), jc.getPort());
            } catch (Exception e) {
                log.info("register MBean for server failed", e);
            }
        }

        return server;
    }

}