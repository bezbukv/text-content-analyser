package frozen.abstractions;//# или может IResourceWrapper? or IResourceCapturer

/** Соединяется с ресурсом и преобразовывает его в текст. !Почему подсвечивает красным!?*/
// Не забывать освободить ресурсы(о реализации)!
public interface ResourceCapturerAndToTextConvertor {

    /** Есть ли смысл в разделении захвата ресурса и преобразовании? Пока
     не используется преобразование по частям. А если использовать по частям?*/

    // @param url информация о том, как можно добраться до ресурса
    ToTextError capture(String url);  // будут использоваться внутри объекта
    // Returns: Представление ресурса графом?

    GraphToText getResourcePlan();
    // @param qualifier уточнитель
    String toText(String qualifier, ToTextError err);

    // Освобождаем ресурс
    ToTextError detach();
    // Есть ли в Java деструкторы или финалайзеры?

    /** Разовое преобразование. Просто весь ресурс в текст.*/
    String captureAndToText(String url, ToTextError err);

    /** Соединение с ресурсом не важно. Режим конвертера.*/
    // @param rawData не чистый текст, например html код
    // Returns: Чистый текст
    String rawToText(String rawData, ToTextError err);
}
