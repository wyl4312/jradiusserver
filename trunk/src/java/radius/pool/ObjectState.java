package radius.pool;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public class ObjectState {
    public static final ObjectState NULL_STATE = new ObjectState("null");
    public static final ObjectState IDLE_STATE = new ObjectState("idle");
    public static final ObjectState USING_STATE = new ObjectState("using");
    

    private String state;
    
    private ObjectState(String state) {
        this.state = state;
    }
    
    public String getState() {
        return state;
    }
    
    public String toString() {
        return state;
    }
}
