package guru.springframework.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import net.bytebuddy.utility.RandomString;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
//@Import(BookDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookDaoIntegrationTest {
	@Autowired
	BookDao bd;
	
	
	@Test
	void testFindAll() {
		List<Book> books = bd.findAll();
		
		assertThat(books).isNotNull();
		assertThat(books.size()).isGreaterThan(0);
	}
	
	@Test
	void testFindByIsbn() {
		Book book = new Book();
		book.setIsbn(RandomString.make());
		book.setTitle("ISBN TEST");
		
		Book saved = bd.saveNewBook(book);
		
		Book fetched = bd.findByISBN(saved.getIsbn());
		assertThat(fetched.getIsbn()).isNotNull();
	}
	
	@Test
	void testDeleteBook() {
		Book book = new Book();
		book.setIsbn("test");
		book.setPublisher("test2");
		book.setTitle("test3");
		
		Book saved = bd.saveNewBook(book);
		
		bd.deleteBookById(saved.getId());
		
		Book deleted = bd.findBookById(saved.getId());
		assertThat(deleted).isNull();
		assertThat(bd.findBookById(saved.getId()));
	}
	
	@Test
	void testUpdateBook() {
		Book book = new Book();
		book.setIsbn("ISBN");
		book.setPublisher("PUBLISHER");
		book.setTitle("Titulo");
		
		Author author = new Author();
		author.setId(3L);
		
		book.setAuthorId(author.getId());
		
		Book saved = bd.saveNewBook(book);
		saved.setPublisher("PUBLICISTA");
		Book updated = bd.updateBook(saved);
		
		assertThat(updated.getPublisher()).isEqualTo("PUBLICISTA");
	}
	
	@Test
	void testSaveBook() {
		Book book = new Book();
		book.setIsbn("ISBN2");
		book.setPublisher("PUBLISHER2");
		book.setTitle("Titulo2");
		
		Author author = new Author();
		author.setId(3L);
		
		book.setAuthorId(author.getId());
		Book saved = bd.saveNewBook(book);
		
		System.out.println("New id is:" + saved.getId());
		
		assertThat(saved).isNotNull();
		assertThat(saved.getId()).isNotNull();
		
	}
	
	@Test
	void testGetBook() {
		Book book = bd.findBookById(4L);
		assertThat(book.getId()).isNotNull();
	}
	
	@Test
	void testGetBookByName() {
		Book book = bd.findBookByTitle("Clean Code");
		assertThat(book).isNotNull();
	}
	
	@Test
	void testGetBookByTitleCriteria() {
		Book book = bd.findBookByTitleCriteria("Clean Code");
		assertThat(book).isNotNull();
		assertThat(book.getTitle()).isNotNull();
		assertThat(book.getTitle()).isEqualTo("Clean Code");
		System.out.println("--------------------->" + book.getTitle());
	}
}
