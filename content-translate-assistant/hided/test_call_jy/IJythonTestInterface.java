package test_call_jy;

import java.util.List;
/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 14.03.13
 * Time: 20:11
 * To change this template use File | Settings | File Templates.
 */
public interface IJythonTestInterface {
    /// Simple data types
    // Getters
    int getIdx();

    /// Collections
    // List
    // Map
    // Getters
    List<String> getList();  // Jython получает ответ, не как Python List

    // Mutators
    void setList(List<String> stringList);  // А вот передать можно!

    /// User  Defined types
    // Getters
    // Mutators
    
    // Hmmm... Возвращение tuple! Кажется можно, но похоже нет, нужно пробовать
}
