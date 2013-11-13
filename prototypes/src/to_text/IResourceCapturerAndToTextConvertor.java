package to_text;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 09.03.13
 * Time: 18:50
 * To change this template use File | Settings | File Templates.
 */
//# или может IResourceWrapper? or IResourceCapturer
/** Соединяется с ресурсом и преобразовывает его в текст. !Почему подсвечивает красным!?*/
// Не забывать освободить ресурсы(о реализации)!
public interface IResourceCapturerAndToTextConvertor {

    /** Есть ли смысл в разделении захвата ресурса и преобразовании? Пока
     не используется преобразование по частям. А если использовать по частям?*/

    // @param url информация о том, как можно добраться до ресурса
    IToTextErr capture(String url);  // будут использоваться внутри объекта
    // Returns: Представление ресурса графом?

    IGraphToText getResourcePlan();
    // @param qualifier уточнитель
    String toText(String qualifier, IToTextErr err);

    // Освобождаем ресурс
    IToTextErr detach();
    // Есть ли в Java деструкторы или финалайзеры?

    /** Разовое преобразование. Просто весь ресурс в текст.*/
    String captureAndToText(String url, IToTextErr err);

    /** Соединение с ресурсом не важно. Режим конвертера.*/
    // @param rawData не чистый текст, например html код
    // Returns: Чистый текст
    String rawToText(String rawData, IToTextErr err);
}

interface IToTextErr {

   // TODO(zaqwes): Реализовать таки
}
