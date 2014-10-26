package frozen.dal.accessors_text_file_storage;

// Доступ по ключу это стерильная зона - превышение - ошибка в программе.
public class OutOfRangeOnAccess extends RuntimeException {
public OutOfRangeOnAccess(String msg) {
  super(msg);
  }
}
