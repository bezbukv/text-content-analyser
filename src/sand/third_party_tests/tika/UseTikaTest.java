package sand.third_party_tests.tika;

import org.junit.Test;

public class UseTikaTest {

  @Test
  public void testMain(){
    /*InputStream is = null;

    try {
      is = new BufferedInputStream(new FileInputStream(new File("d:/MIT6_01SCS11_chap04.jython_src.pdf")));

      Parser parser = new AutoDetectParser();
      ContentHandler handler = new BodyContentHandler(System.out);

      Metadata metadata = new Metadata();

      // Выдает в stdout text
      parser.parse(is, handler, metadata, new ParseContext());

      /*for (String name : metadata.names()) {
        String value = metadata.get(name);

        if (value != null) {
          System.out.println("Metadata Name:  " + name);
          System.out.println("Metadata Value: " + value);
        }
      }  * /
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TikaException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch(IOException e) {
          e.printStackTrace();
        }
      }
    } */
  }
}