package fileinputstreamtostream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author raitis
 */
public class SDFStreamIT {

    private static String DATA="test/data/sdf.txt";
    
    public SDFStreamIT() {
    }
    
    @BeforeClass
    public static void setUpClass() throws FileNotFoundException, IOException {
        BufferedWriter bw = new BufferedWriter( new FileWriter(DATA));
        Random rand = new Random();
        for(int i=1; i<= 10;i++){
           bw.write(">  <Prop_"+i+">\n");
          switch(i){
              case 1:
                  bw.write("HB0000"+rand.nextInt(100)+"\n");
                  break;
              case 2:
                  bw.write(Float.toString((float)Math.random()*10)+"\n");
                  break;
              default:
                  bw.write("Val_"+i+"\n");
          }
        }
        bw.flush();
        bw.close();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of toStream method, of class SDFStream.
     */
    @Test
    public void testSDFStream() throws FileNotFoundException {
        SDFStream sdfStream = new SDFStream();
        BufferedReader br = new BufferedReader(new FileReader(DATA));
        //Predicate<String> p1 = e -> e!=null;
        Stream<String> stream = sdfStream.toStream(br);
        List<String> coll = stream.collect(Collectors.toList());
         stream.close();
        Stream<String> stream1 = coll.stream();
        stream1.forEach(e -> {
            System.out.println("-> "+e);
            
        });
        Iterator<String> itr = coll.stream().iterator();
        itr.forEachRemaining(e -> System.out.println("=>> "+e));
        
    }
    
    @Test
    public void testPredicates(){
        Predicate<Integer> greaterThanTen = (i) -> i > 10;
        Predicate<Integer> lessThanTwenty = (i) -> i < 20;
        Boolean ok1 = chk(1, greaterThanTen.and(lessThanTwenty));
        Boolean ok2 = chk(15,greaterThanTen.and(lessThanTwenty));
        assertFalse(ok1);
        assertTrue(ok2);
    }
    
    private static Boolean chk(int v, Predicate<Integer> pred){
      return  pred.test(v);
    } 
    
}
