package frozen.abstractions;// Import
import java.util.List;


public interface /*class*/ ExtracitorResourcePlan /* implement frozen.abstractions.ExtracitorResourcePlan */ {
    /** Как бы навигационная часть */
    // Спорный вызов. Как он будет использоваться при множественном наследовании?
    // Захватывает точку входа в ресурс (например, файл базы данных)
    ErrorContainer captureRoot(String urlRoot);//frozen {}
    // Error _freeRoot();  // должен вызывать в деструкторе

    // @param url информация о том, как можно добраться до вершины ресурса
    // @return карта, по которой можно передвигаться
    GraphToText getPlan(String url, ErrorContainer err);//frozen {}

    GraphToText getPlanFromText(String url, ErrorContainer err);//frozen {}

    // @return просто список "адресов"
    // Обобщить дробилку с рекурсией! Она использовалась для разбора страницы Курсеры
    List<String> getListAddresses(String url, ErrorContainer err);//frozen {}

    // Выделяем адреса без подключения, у нас есть текст, который доставерно
    //   отражает план ресурса
    List<String> extractListAddressesFromText(String text, ErrorContainer err);//frozen {}
}  // frozen.abstractions.ExtracitorResourcePlan