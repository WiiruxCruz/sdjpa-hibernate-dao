package guru.springframework.jdbc.dao;

import java.util.List;

import guru.springframework.jdbc.domain.Book;

public interface BookDao {
	List<Book> findAll();
	Book findByISBN(String isbn);
	Book findBookById(Long id);
	Book findBookByTitle(String title);
	Book findBookByTitleCriteria(String title);
	Book saveNewBook(Book book);
	Book updateBook(Book book);
	void deleteBookById(Long id);
}
