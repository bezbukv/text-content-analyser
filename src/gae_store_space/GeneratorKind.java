package gae_store_space;

import static gae_store_space.OfyService.ofy;

import java.util.ArrayList;

import net.jcip.annotations.NotThreadSafe;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;
import com.googlecode.objectify.annotation.Unindex;

import core.math.DistributionElement;
import core.math.GeneratorAnyDistribution;
import frozen.dal.accessors_text_file_storage.OutOfRangeOnAccess;
import gae_store_space.high_perf.OnePageProcessor;

// About:
//   Класс способен генерировать последовательности любого дискретного распределения
//   Возвращает индекс массива исходного распределения.
//
// TODO: Как быть с распределением? Оно будет динамическим!
// Вариант - читать через кеш, он все равно будет - медленно, очень, особенно при горячем старте - кеш пуст.
//   лучше так кеш не использовать, а использовать для чтения.
// Вариант 2 - сохранять распределение в хранилище. Дублирование причем в 3 местах! Хуже всего что в генераторе,
//   но лучше генератор сделать внешним, хотяяяя... нет.
//
// TODO: Кто управляет временем жизни в storage?
// TODO: Читать однажды, а так сохранять в хранилище. Проблема в ширине кеша. Так же он заполнятся поштучно!
// TODO: Для чтения пойдет, а так не хотелось бы. Хотя на этапе может ширина известна на этапе формирования?
// TODO: Как изначально инициализировать. При формировании таблицы, например.
// TODO: Для горячего старта.
// TODO: Если мы меняем поля, то нужно сохранятся страницу в базу, сейчас персистентность управляется извне!
//   думаю она и должна оставаться управляемой извне.
//
// Есть проблемы с сохранением
// http://code.google.com/p/objectify-appengine/wiki/IntroductionToObjectify#Embedded_Collections_and_Arrays
//
@NotThreadSafe
@Entity
public class GeneratorKind
{
  @Id
  public Long id;
  
  @Index
  private String name;
  
  public String getName() { return this.name; }
  
  // Индексируется as embedded- это состояние генератора
  // FIXME: какая лажа с порядком загрузки
  @Serialize ArrayList<DistributionElement> distribution;  // порядок важен
  @Serialize ArrayList<Integer> equalizeMask;  // same size as distr.
  
  // Можно и не индексировать - пока алгоритм одни
  // придется хранить отдельно
  // все-таки на объект накладываются ограничения!!
  // FIXME: вообще нужно быть внимательным, порядок иниц. может все сломать
  // Наверное можно было бы сереализовать его, но из-за эквалайзинга,
  //   сохраняю исходные распределения отдельно
  @Ignore GeneratorAnyDistribution gen;

  @Unindex
  Integer codeAction;  // возможность подкл. алгоритма при создании

  public ImmutableList<DistributionElement> getDistribution() {
    return ImmutableList.copyOf(distribution);
  }

  // Любой список с числами
  // @throws: GeneratorDistributionException
  public static GeneratorKind create(ArrayList<DistributionElement> distribution) {
    return new GeneratorKind(distribution, OnePageProcessor.defaultGenName);
  }
  
  public static GeneratorKind create(ArrayList<DistributionElement> distribution, String name) {
  	return new GeneratorKind(distribution, name);
  }

  public Integer getPosition() {
    return Optional.of(gen).get().getPosition();
  }

  private void reloadGenerator(ArrayList<DistributionElement> distribution) {
    gen = GeneratorAnyDistribution.create(distribution);
  }

  private GeneratorKind(ArrayList<DistributionElement> distribution, String name) {
    this.distribution = distribution;
    this.name = name;
    
    reloadGenerator(distribution);
  }

  public void disablePoint(Integer idx) {
    // TODO: Проверка границ - это явно ошибка
    // TODO: Похоже нужна non-XG - транзакция. Кажется может возникнуть исключение.
    getElem(idx).enabled = false;
    reloadGenerator(distribution);
  }

  private DistributionElement getElem(Integer idx) {
    if (idx >= distribution.size() || idx < 0)
      throw new OutOfRangeOnAccess("On get element");  // сообщения безсмысленны, тип важнее

    // хотя наверное и так бросит
    return distribution.get(idx);
  }
  
  public void persist() {
  	ofy().save().entity(this).now();
  }

  
  private GeneratorKind() { }
  
  // похоже при восстановлении вызывается он
  // TODO: момент похоже скользкий - а будет ли распределение инициализировано?
  // DANGER:
  //   http://www.quizful.net/post/java-fields-initialization
  // Обязательно вызывать после восстановления из хранилища! конструктором по умолчанию воспользоваться нельзя!
  public void reset() {
  	if (distribution == null)
  		throw new IllegalStateException();
  		
    // похоже при восстановлении вызывается он
    // TODO: момент похоже скользкий - а будет ли распределение инициализировано?
    reloadGenerator(distribution);
  }

  public void enablePoint(Integer idx) {
    getElem(idx).enabled = true;
    reloadGenerator(distribution);
  }
}
