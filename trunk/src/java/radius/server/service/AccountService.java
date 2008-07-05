package radius.server.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.RadiusAttribute;
import radius.RadiusPacket;
import radius.attribute.IntegerAttribute;
import radius.attribute.StringAttribute;
import radius.attribute.TextAttribute;
import radius.server.RadiusContext;
import radius.server.RadiusService;
import radius.server.ServiceManager;
import radius.server.service.pojo.Account;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class AccountService implements RadiusService {
    private Log log = LogFactory.getLog(getClass());

    static {
        ServiceManager.register(new AccountService());
    }

    public int getType() {
        return RadiusPacket.ACCOUNTING_REQUEST;
    }

    public boolean service(RadiusContext context) {
        //TODO
        RadiusPacket source = context.getSourcePacket();
        RadiusPacket target = context.getTargetPacket();
        target.setCode(RadiusPacket.ACCOUNTING_RESPONSE);
        target.setIdentifier(source.getIdentifier());

        Account account = new Account();
        IntegerAttribute att = (IntegerAttribute) source
                .getAttribute(RadiusAttribute.ACCT_STATUS_TYPE);
        if (att != null)
            account.setStatusType(new Integer((int) att.getInteger()));

        att = (IntegerAttribute) source.getAttribute(RadiusAttribute.ACCT_DELAY_TIME);
        if (att!=null)
            account.setDelayTime(new Integer((int)att.getInteger()));
        
        att = (IntegerAttribute) source.getAttribute(RadiusAttribute.ACCT_INPUT_OCTETS);
        if (att!=null)
            account.setInputOctects(new Long((int)att.getInteger()));
        
        att = (IntegerAttribute) source.getAttribute(RadiusAttribute.ACCT_OUTPUT_OCTETS);
        if (att!=null)
            account.setOutputOctects(new Long((int)att.getInteger()));

        att = (IntegerAttribute) source.getAttribute(RadiusAttribute.ACCT_INPUT_PACKETS);
        if (att!=null)
            account.setInputPackets(new Integer((int)att.getInteger()));
        
        att = (IntegerAttribute) source.getAttribute(RadiusAttribute.ACCT_OUTPUT_PACKETS);
        if (att!=null)
            account.setOutputPackets(new Integer((int)att.getInteger()));
        
        att = (IntegerAttribute) source.getAttribute(RadiusAttribute.ACCT_TERMINATE_CAUSE);
        if (att!=null)
            account.setTerminateCause(new Integer((int)att.getInteger()));
        
        att = (IntegerAttribute) source.getAttribute(RadiusAttribute.ACCT_LINK_COUNT);
        if (att!=null)
            account.setLinkCount(new Integer((int)att.getInteger()));
        
        att = (IntegerAttribute) source.getAttribute(RadiusAttribute.ACCT_SESSION_TIME);
        if (att!=null)
            account.setSessionTime(new Integer((int)att.getInteger()));
        
        att = (IntegerAttribute) source.getAttribute(RadiusAttribute.ACCT_AUTHENTIC);
        if (att!=null)
            account.setAuthentic(new Integer((int)att.getInteger()));
        
        TextAttribute ta = (TextAttribute) source.getAttribute(RadiusAttribute.ACCT_SESSION_ID);
        if (ta!=null)
            account.setSessionId(ta.getText());
        
        StringAttribute sa = (StringAttribute) source.getAttribute(RadiusAttribute.ACCT_MULTI_SESSION_ID);
        if (sa!=null)
            account.setMultiSessionId(sa.getString());
        
        PersistFactory.getPersist().saveAccount(account);
        
        return true;
    }

}