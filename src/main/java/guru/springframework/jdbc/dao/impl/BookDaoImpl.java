package guru.springframework.jdbc.dao.impl;

import org.springframework.stereotype.Component;

import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import net.bytebuddy.utility.RandomString;

@Component
public class BookDaoImpl implements BookDao{
	
	EntityManagerFactory emf;
	
	public BookDaoImpl(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	@Override
	public Book findByISBN(String isbn) {
		// TODO Auto-generated method stub
		EntityManager em = getEntityManager();
		
		try {
			/*
			Query query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn");
			query.setParameter("isbn", isbn);
			
			Book book = (Book) query.getSingleResult();
			*/
			TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
			query.setParameter("isbn", isbn);
			Book book = query.getSingleResult();
			return book;
		} finally {
			em.close();
		}
		
	}

	@Override
	public Book findBookById(Long id) {
		// TODO Auto-generated method stub
		EntityManager em = getEntityManager();
		Book book = getEntityManager().find(Book.class, id);
		em.close();
		return book;
	}

	@Override
	public Book findBookByTitle(String title) {
		// TODO Auto-generated method stub
		EntityManager em = getEntityManager();
		TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b "
				+ "WHERE b.title = :title", Book.class);
		query.setParameter("title", title);
		
		Book book = query.getSingleResult();
		em.close();
		
		return book;
	}

	@Override
	public Book saveNewBook(Book book) {
		// TODO Auto-generated method stub
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		em.persist(book);
		em.flush();
		em.getTransaction().commit();
		em.close();
		return book;
	}

	@Override
	public Book updateBook(Book book) {
		// TODO Auto-generated method stub
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		em.merge(book);
		em.flush();
		em.clear();
		Book savedBook = em.find(Book.class, book.getId());
		em.getTransaction().commit();
		em.close();
		return savedBook;
	}

	@Override
	public void deleteBookById(Long id) {
		// TODO Auto-generated method stub
		EntityManager em = getEntityManager();
		em.getTransaction().begin();
		Book book = em.find(Book.class, id);
		em.remove(book);
		em.flush();
		em.getTransaction().commit();
		em.close();
	}
	
	private EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
}
