// http://www.oracle.com/technetwork/articles/vasiliev-jpql-087123.html

package frozen.dal.jpa.eclipselink;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class SentencesTest {
  private static final String PERSISTENCE_UNIT_NAME = "words_test";
  private static EntityManagerFactory factory;

  @Test
  public void testReadAll() {
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = factory.createEntityManager();

    Query q = em.createQuery("SELECT t FROM Sentences t");
    List<Sentences> todoList = q.getResultList();
    for (Sentences todo : todoList) {
      System.out.println(todo);
    }
    System.out.println("Size: " + todoList.size());

    em.close();
  }

  @Test
  public void testAppend() {
    String sentence = "allowance is made for 'fair use'";

    HashFunction fn = Hashing.md5();
    HashCode hashCode = fn.newHasher().putString(sentence, Charsets.UTF_8).hash();
    Sentences sentenceRecord = new Sentences();
    sentenceRecord.setHashSentence(hashCode.asInt());
    sentenceRecord.setSentence(sentence);

    // Записываем в базу данных
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = factory.createEntityManager();

    em.getTransaction().begin();
    em.persist(sentenceRecord);
    em.getTransaction().commit();

    em.close();
  }

  @Test
  public void testAppendAnother() {
    String sentence = "allowance is made for ";

    HashFunction fn = Hashing.md5();
    HashCode hashCode = fn.newHasher().putString(sentence, Charsets.UTF_8).hash();
    Sentences sentenceRecord = new Sentences();
    sentenceRecord.setHashSentence(hashCode.asInt());
    sentenceRecord.setSentence(sentence);

    // Записываем в базу данных
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = factory.createEntityManager();

    em.getTransaction().begin();
    em.persist(sentenceRecord);
    em.getTransaction().commit();

    // Выборка
    Query q = em.createQuery("select t from Sentences t");

    em.close();
  }
}
