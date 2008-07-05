package radius.server.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import radius.NAS;
import radius.RadiusAttribute;
import radius.RadiusPacket;
import radius.attribute.TextAttribute;
import radius.server.RadiusContext;
import radius.server.RadiusService;
import radius.server.ServiceManager;
import radius.server.service.pojo.ServiceType;
import radius.server.service.pojo.User;
import radius.util.RadiusUtil;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc </a>
 *  
 */
public class AuthService implements RadiusService {
    private Log log = LogFactory.getLog(getClass());

    static {
        ServiceManager.register(new AuthService());
    }

    /*
     * @see radius.server.RadiusService#getType()
     */
    public int getType() {
        return RadiusPacket.ACCESS_REQUEST;
    }

    /*
     * @see radius.server.RadiusService#service(radius.server.RadiusContext)
     */
    public boolean service(RadiusContext context) {
        assert context.getNAS() != null : "nas must not null";
        RadiusPacket source = context.getSourcePacket();
        NAS nas = context.getNAS();
        if (source.getCode() != RadiusPacket.ACCESS_REQUEST) {
            return false;
        } else {
            RadiusAttribute userName = source
                    .getAttribute(RadiusAttribute.USER_NAME);
            RadiusAttribute password = source
                    .getAttribute(RadiusAttribute.USER_PASSWORD);
            RadiusAttribute state = source.getAttribute(RadiusAttribute.STATE);

            RadiusAttribute chapPassword = source
                    .getAttribute(RadiusAttribute.CHAP_PASSWORD);

            if (userName != null) {
                String name = ((TextAttribute) userName).getText();
                User user = PersistFactory.getPersist().getUser(name);
                if (user == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("user(" + name + ") not exist");
                    }
                    return false;
                }
                if (password != null) {
                    byte[] pb = RadiusUtil.decryptPassword(source
                            .getAuthenticator(), password.getValue(), nas
                            .getSecret().getBytes());
                    String pass = new String(pb);
                    if ("chap".equalsIgnoreCase(user.getAuthMethod())) {// try
                        // challenge
                        log.debug("challenge for " + name);
                        challenge(context, user);
                    } else {
                        pap(context, user, pass);
                    }
                } else if (chapPassword != null) {
                    // TODO check state for chap
                    log.debug("chap for " + name);
                    chap(context, user, chapPassword);
                }
            } else {
                log.info("invalid packet:\r\n"+source);
            }
            return true;
        }
    }

    private final String s = "javaradiusserverbyzzzhc";

    void challenge(RadiusContext context, User user) {
        // TODO
        String userName = user.getName();
        String password = user.getPassword();
        RadiusPacket source = context.getSourcePacket();
        RadiusPacket target = context.getTargetPacket();
        target.setCode(RadiusPacket.ACCESS_CHALLENGE);
        target.setIdentifier(source.getIdentifier());
        RadiusAttribute state = RadiusAttribute
                .getAttribute(context.getNAS(),RadiusAttribute.STATE);

        state.setValue((s + password).getBytes());
        target.addAttribute(state);
        RadiusAttribute reply = RadiusAttribute
                .getAttribute(context.getNAS(),RadiusAttribute.REPLY_MESSAGE);
        reply.setValue(("challenge " + password).getBytes());
        target.addAttribute(reply);
    }

    void pap(RadiusContext context, User user, String password) {
        // -----TODO----
        if (password.equals(user.getPassword())) {
            RadiusPacket source = context.getSourcePacket();
            RadiusPacket target = context.getTargetPacket();
            target.setCode(RadiusPacket.ACCESS_ACCEPT);
            target.setIdentifier(source.getIdentifier());
            loadServiceType(context,user);
        } else {
            RadiusPacket source = context.getSourcePacket();
            RadiusPacket target = context.getTargetPacket();
            target.setCode(RadiusPacket.ACCESS_REJECT);
            target.setIdentifier(source.getIdentifier());
        }
        // -----TODO----

    }

    /**
     * The RADIUS server looks up a password based on the User-Name, encrypts
     * the challenge using MD5 on the CHAP ID octet, that password, and the CHAP
     * challenge (from the CHAP-Challenge attribute if present, otherwise from
     * the Request Authenticator), and compares that result to the
     * CHAP-Password. If they match, the server sends back an Access-Accept,
     * otherwise it sends back an Access-Reject.
     * 
     * @param context
     * @param chapPassword
     */
    void chap(RadiusContext context, User user, RadiusAttribute chapPassword) {
        RadiusPacket source = context.getSourcePacket();
        byte[] value = chapPassword.getValue();
        byte id = value[0];
        String password = user.getPassword();
        RadiusAttribute challenge = source
                .getAttribute(RadiusAttribute.CHAP_CHALLENGE);
        byte[] challengeValue = source.getAuthenticator();
        if (challenge != null) {
            challengeValue = challenge.getValue();
        }

        byte[] pValue = RadiusUtil.computeChap(id, password.getBytes(),
                challengeValue);
        boolean flag = true;
        if (pValue.length == value.length - 1) {
            for (int i = 0; i < pValue.length; i++) {
                if (!(pValue[i] == value[i + 1])) {
                    flag = false;
                }
            }
        } else {
            flag = false;
        }
        if (flag) {
            RadiusPacket target = context.getTargetPacket();
            target.setCode(RadiusPacket.ACCESS_ACCEPT);
            target.setIdentifier(source.getIdentifier());
        } else {
            RadiusPacket target = context.getTargetPacket();
            target.setCode(RadiusPacket.ACCESS_REJECT);
            target.setIdentifier(source.getIdentifier());
        }
    }

    private void loadServiceType(RadiusContext ctx,User user) {
        ServiceType[] sts = PersistFactory.getPersist().loadServiceType(user);
        for (int i = 0; i < sts.length; i++) {
            ServiceType st = sts[i];
            RadiusAttribute ra = RadiusAttribute.getAttribute(ctx.getNAS(),st.getAttId().intValue());
            if (ra!=null) {
               ra.setObjectValue(st.getValue());
               ctx.getTargetPacket().addAttribute(ra);
            }
        }
    }
}