package radius.server.filter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import radius.NAS;
import radius.RadiusAttribute;
import radius.RadiusPacket;
import radius.chain.FilterChain;
import radius.server.RadiusContext;
import radius.server.RadiusFilter;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class ProxyFilter extends AbstractRadiusFilter {

    private static final int MAGIC = 0x5866C66c;// 0X66C=sum("javaradiusproxy")

    private ProxyFinder proxyFinder;

    public ProxyFilter() {
        super(RadiusFilter.PROXY_TYPE);
        proxyFinder = new ProxyFinder();
    }

    /*
     * @see chain.Filter#doFilter(chain.Context, chain.FilterChain)
     */
    public void doRadiusFilter(RadiusContext context, FilterChain chain) {
        if (proxyFinder == null) {
            chain.doFilter(context);
            return;
        }

        SocketAddress source = context.getSourceAddress();
        RadiusPacket sourcePacket = context.getSourcePacket();
        RadiusPacket targetPacket = context.getTargetPacket();
        RadiusAttribute proxyAttribute = sourcePacket
                .getAttribute(RadiusAttribute.PROXY_STATE);
        if (isCircleProxy(proxyAttribute)) {
            return;// ignore circle proxy
        }
        byte[] authenticator = new byte[16];
        SocketAddress target = getTargetAddress(proxyAttribute, authenticator);
        if (target != null) {
            context.setTargetAddress(target);
            targetPacket.copy(sourcePacket);
            targetPacket.setAuthenticator(authenticator);
            removeLastProxyState(targetPacket);
            return;
        } else {
            // get Proxy
            SocketAddress dist = proxyFinder.getProxyForNas(source, context
                    .getServiceType());
            if (dist != null) {
                targetPacket.copy(sourcePacket);
                targetPacket.addAttribute(createProxyState(context.getNAS(),
                        context.getSourceAddress(), sourcePacket
                                .getAuthenticator()));
                context.setTargetAddress(dist);
                return;
            }
        }
        chain.doFilter(context);
    }

    private boolean isCircleProxy(RadiusAttribute proxyAttribute) {
        if (proxyAttribute != null) {
            RadiusAttribute pre = null;
            RadiusAttribute cur = proxyAttribute;
            ByteBuffer bb = ByteBuffer.allocate(10);
            do {
                if (cur.getLength() == 12) {
                    bb.clear();
                    bb.put(cur.getValue());
                    bb.flip();
                    int i = bb.getInt();
                    if (i == MAGIC) {
                        if (pre != null)
                            return true;
                        else
                            pre = cur;
                    }
                }
            } while (cur.getNext() != null);
        }

        return false;
    }

    RadiusAttribute createProxyState(NAS nas, SocketAddress source,
            byte[] authenticator) {
        RadiusAttribute ra = RadiusAttribute.getAttribute(nas,
                RadiusAttribute.PROXY_STATE);
        ByteBuffer bb = ByteBuffer.allocate(26);//
        bb.putInt(MAGIC);// 4bytes
        InetSocketAddress ia = (InetSocketAddress) source;
        bb.put(ia.getAddress().getAddress());// 4bytes
        bb.putShort((short) ia.getPort());// 2bytes
        bb.put(authenticator);
        ra.setValue(bb.array());
        return ra;
    }

    SocketAddress getTargetAddress(RadiusAttribute proxyAttribute,
            byte[] authenticator) {
        if (proxyAttribute == null)
            return null;
        RadiusAttribute last = proxyAttribute;
        while (last.getNext() != null) {
            last = last.getNext();
        }
        byte[] value = last.getValue();
        if (value == null || value.length != 10)
            return null;
        ByteBuffer bb = ByteBuffer.allocate(10);
        bb.put(value);
        bb.flip();
        if (bb.getInt() != MAGIC) {
            return null;
        }
        byte[] ab = new byte[4];
        bb.get(ab);
        int port = bb.getShort() & 0xffff;
        bb.get(authenticator);
        try {
            InetSocketAddress ia = new InetSocketAddress(InetAddress
                    .getByAddress(ab), port);
            return ia;
        } catch (UnknownHostException e) {
            //should NEVER HAPPEN
            log.warn(e, e);
            return null;
        }
    }

    void removeLastProxyState(RadiusPacket rp) {
        RadiusAttribute ra = rp.getAttribute(RadiusAttribute.PROXY_STATE);
        if (ra != null) {
            RadiusAttribute pre = ra;
            RadiusAttribute cur = pre.getNext();
            while (cur != null) {
                pre = cur;
                cur = pre.getNext();
            }
            if (pre == ra) {
                rp.removeAttribute(RadiusAttribute.PROXY_STATE);
            } else {
                pre.setNext(null);
            }
        }
    }
}