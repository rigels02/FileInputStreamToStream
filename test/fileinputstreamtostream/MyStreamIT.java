package fileinputstreamtostream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.stream.Stream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author raitis
 */
public class MyStreamIT {
    
    public MyStreamIT() {
    }
    
    @BeforeClass
    public static void setUpClass() throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATATXT));
        int sz = 10;
        oos.writeInt(sz);
        for(int i=1; i<=sz; i++){
           oos.writeObject("String "+i);
        }
        oos.flush();
        oos.close();
    }
    private static final String DATATXT = "test/data/data.txt";
    
    @AfterClass
    public static void tearDownClass() {
        new File(DATATXT).delete();
    }

    /**
     * Test of toStream method, of class MyStream.
     */
    @Test
    public void testToStream() throws FileNotFoundException, IOException {
        MyStream mstream = new MyStream();
        ObjectInputStream idata = new ObjectInputStream(new FileInputStream(DATATXT));
        Stream<String> stream = mstream.toStream(idata, String.class);
        stream.forEach(s -> System.out.println("-> "+s));
        stream.close();
    }
    
}
