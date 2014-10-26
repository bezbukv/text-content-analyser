package frozen.dal.accessors_text_file_storage;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 30.03.13
 * Time: 18:00
 * To change this template use File | Settings | File Templates.
 */
public class Runner {
  @Test
  public void testMain() {
    IndexCursorFactory factory = new IndexCursorFactory();

    String indexRoot = "src/indexes";
    IndexCursor ptr = factory.create(indexRoot);

    System.out.print(ptr.getListNodes());

    // Подключаемся к ветке
    String contentItemName = "Iron Man AA";

    ptr.assignBranch(contentItemName);

    // Получаем индекс
    System.out.print(ptr.getSortedForwardIdx());
  }
}
