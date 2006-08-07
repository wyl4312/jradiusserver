package radius.server.filter;

import radius.Radius;

import radius.server.service.PersistFactory;
import radius.server.service.pojo.Proxy;

import java.net.InetSocketAddress;
import java.net.SocketAddress;


/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *
 */
public class ProxyFinder {
    public SocketAddress getProxyForNas(SocketAddress nas, int serviceType) {
        InetSocketAddress ia = (InetSocketAddress) nas;
        Proxy[] proxies = PersistFactory.getPersist().getProxy(ia.getAddress()
                                                                 .getHostAddress());

        for (int i = 0; i < proxies.length; i++) {
            Proxy proxy = proxies[i];
            int fromPort = proxy.getFromPort();

            if (fromPort == ia.getPort()) {
                return new InetSocketAddress(proxy.getToIp(),
                    getToPort(proxy, serviceType));
            }
        }

        for (int i = 0; i < proxies.length; i++) {
            Proxy proxy = proxies[i];
            int fromPort = proxy.getFromPort();

            if (fromPort == 0) {
                return new InetSocketAddress(proxy.getToIp(),
                    getToPort(proxy, serviceType));
            }
        }

        return null;
    }

    private int getToPort(Proxy proxy, int serviceType) {
        int toPort = proxy.getToPort();

        if (toPort == 0) {
            if (Proxy.AUTH_TYPE.equals(proxy.getType())) {
                toPort = Radius.DEFAULT_AUTH_PORT;
            } else if (Proxy.ACCOUNT_TYPE.equals(proxy.getType())) {
                toPort = Radius.DEFAULT_ACCOUNT_PORT;
            } else {
                switch (serviceType) {
                case Radius.ACCOUNT_SERVICE_TYPE:
                    toPort = Radius.DEFAULT_ACCOUNT_PORT;

                    break;

                case Radius.AUTH_SERVICE_TYPE:default:
                    toPort = Radius.DEFAULT_AUTH_PORT;
                }
            }
        }

        return toPort;
    }
}
