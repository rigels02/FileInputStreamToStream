package fileinputstreamtostream;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Get finite Stream from BufferedReader file stream. 
 * @author raitis
 */
public class SDFStream {

    private static final Logger LOG = Logger.getLogger(SDFStream.class.getName());
    
    public Stream<String> toStream(final  BufferedReader stream) {

        Stream<String> stream1 = Stream.generate(() -> readLine(stream)).onClose(
              () -> close(stream));
        
        Stream<String> breaked = breakStream(stream1, new Predicate<String>() {
            @Override
            public boolean test(String t) {
                return t == null;
            }
        });
        return breaked;
    }

    

// If you are not looking for parallelism, you can use following method:
public static <T> Stream<T> breakStream(Stream<T> stream, Predicate<T> terminate) { 
  final Iterator<T> original = stream.iterator();
  Iterable<T> iter = () -> new Iterator<T>() { 
    T t;
    boolean hasValue = false;

    @Override
    public boolean hasNext() { 
      if (!original.hasNext()) { 
        return false;
      } 
      t = original.next();
      hasValue = true;
      if (terminate.test(t)) { 
        return false;
      } 
      return true;
    } 

    @Override
    public T next() { 
       // System.out.println(".............next()"); 
      if (hasValue) { 
        hasValue = false;
        return t;
      } 
      return t;
    } 
  };

  return StreamSupport.stream(iter.spliterator(), false);
}


    private static String readLine(BufferedReader stream) {
        try {
            String line = stream.readLine();
            
            //if(line==null) throw new EOFException("EOF");
            return line;
        } catch (IOException ex) {
            Logger.getLogger(SDFStream.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static void close(Closeable c) {
        try {
            c.close();
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Couldn't close " + c, e);
        }
    }

}
