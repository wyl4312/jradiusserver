package radius.server.filter;

import java.net.InetSocketAddress;

import radius.NAS;
import radius.chain.FilterChain;
import radius.server.RadiusContext;
import radius.server.RadiusFilter;
import radius.server.service.PersistFactory;
import radius.util.ConvertUtil;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class AddressFilter extends AbstractRadiusFilter {

    //private IdentifierHolder idHolder;

    public AddressFilter() {
        super(RadiusFilter.PREPARE_CHECK_TYPE);
        //idHolder = new IdentifierHolder(Integer.MAX_VALUE);
    }

    /*
     * @see chain.Filter#doFilter(chain.Context, chain.FilterChain)
     */
    public void doRadiusFilter(RadiusContext context, FilterChain chain) {
        InetSocketAddress source = (InetSocketAddress) context
                .getSourceAddress();
        String ip = source.getAddress().getHostAddress();
        NAS nas = PersistFactory.getPersist().getNAS(ip);

        if (nas == null) {
            log.info("address(" + ip
                    + ") not been allowed to use this server,packet data=\r\n"
                    + ConvertUtil.toHex(context.getSourceBuffer()));
            return;
        }

        context.setNAS(nas);
        chain.doFilter(context);
    }

}