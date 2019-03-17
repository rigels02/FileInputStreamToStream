package fileinputstreamtostream;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UncheckedIOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * Get finite Stream from ObjectInputStream file.
 * @author raitis
 */
public class MyStream {

    private static final Logger LOG = Logger.getLogger(MyStream.class.getName());
    private static int sz;
    private static int counter;


public <T> Stream<T> toStream(final ObjectInputStream stream, final Class<T> cls) {
    if(sz == 0)
            try {
                sz = stream.readInt();
    } catch (IOException ex) {
        Logger.getLogger(MyStream.class.getName()).log(Level.SEVERE, null, ex);
    }
    return Stream.generate(() -> cls.cast(readObject(stream))).onClose(
        () -> close(stream)).limit(sz);
}

private static Object readObject(ObjectInputStream stream) {
    try {
        
        if(counter < sz){
            Object obj = stream.readObject();
            counter++;
            return obj;
        }
        return null;
    } catch (IOException e) {
        throw new UncheckedIOException(e);
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
}

private static void close(Closeable c) {
    try {
        c.close();
    } catch (IOException e) {
        LOG.log(Level.WARNING, "Couldn't close " + c, e);
    }
}    
}
