package radius.nio;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 *
 */
public interface ReadWriteHandler extends Handler {
    
    void handleRead();
    
    void handleWrite();

}
