package business.originator_frequency_index;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 30.03.13
 * Time: 18:00
 * To change this template use File | Settings | File Templates.
 */
public class Runner {
  public static void main(String[] args) {
    IndexCursorFactory factory = new IndexCursorFactory();

    String indexRoot = "src/indexes";
    IIndexCursor ptr = factory.create(indexRoot);

    System.out.print(ptr.getListNodes());

    // Подключаемся к ветке
    String contentItemName = "Iron Man AA";

    ptr.assignBranch(contentItemName);

    // Получаем индекс
    System.out.print(ptr.getSortedForwardIdx());
  }
}
