package radius.pool;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class ObjectFacade {
    private static int NEXT_HASH = 0;
    
    private static final int HASH_INCREMENT = 0x71c88643;

    private Object value;

    private ObjectState state;

    private int getTimes;

    private int backTimes;
    
    private int hashCode;

    public ObjectFacade(Object value) {
        this.value = value;
        this.state = ObjectState.IDLE_STATE;
        createHashCode();
    }
    
    private synchronized void createHashCode() {
        this.hashCode = NEXT_HASH;
        NEXT_HASH += HASH_INCREMENT;
    }

    public void back(Object value) {
        backTimes++;
        assert this.value == value : "bad object value";
        assert this.state == ObjectState.USING_STATE : "object not been used,needn't back";
        this.state = ObjectState.IDLE_STATE;
    }

    public Object get() {
        getTimes++;
        assert state == ObjectState.IDLE_STATE : "value is null or been used";
        this.state = ObjectState.USING_STATE;
        return value;
    }
    
    Object getValue() {
        return value;
    }

    public int getBackTimes() {
        return backTimes;
    }

    public int getGetTimes() {
        return getTimes;
    }

    public ObjectState getState() {
        return state;
    }

    public int hashCode() {
        return hashCode;
    }
    
    public boolean equals(Object obj) {
        if (obj==null)
            return false;
        else {
            if (this==obj)
                return true;
            else {
                if (obj instanceof ObjectFacade) {
                    ObjectFacade of = (ObjectFacade) obj;
                    if (of.value==this.value && of.state==this.state)
                        return true;
                }
                return false;
            }
        }
    }
}
