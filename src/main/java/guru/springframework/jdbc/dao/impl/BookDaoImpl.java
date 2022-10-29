package guru.springframework.jdbc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.bytebuddy.utility.RandomString;

@Component
public class BookDaoImpl implements BookDao{
	
	EntityManagerFactory emf;
	
	public BookDaoImpl(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	@Override
	public List<Book> findAll() {
		EntityManager em = getEntityManager();
		try {
			TypedQuery<Book> typedQuery = em.createNamedQuery("book_find_all", Book.class);
			List<Book> books =  typedQuery.getResultList();
			return books;
		} finally {
			em.close();
		}
		
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
		
		try {
			TypedQuery<Book> typedQuery = em.createNamedQuery("find_by_title", Book.class);
			typedQuery.setParameter("title", title);
			Book book = typedQuery.getSingleResult();
			
			
			return book;
		} finally {
			em.close();
		}
		//TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b "
				//+ "WHERE b.title = :title", Book.class);
		//query.setParameter("title", title);
		//Book book = query.getSingleResult();
	}
	
	@Override
	public Book findBookByTitleCriteria(String title) {
		EntityManager em = getEntityManager();
		
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Book> cq = cb.createQuery(Book.class);
			
			Root<Book> root = cq.from(Book.class);
			
			ParameterExpression<String> titleParam = cb.parameter(String.class);
			
			Predicate titlePred = cb.equal(root.get("title"), titleParam);
			
			cq.select(root).where(titlePred);
			
			TypedQuery<Book> typedQuery = em.createQuery(cq);
			typedQuery.setParameter(titleParam, title);
			Book book = typedQuery.getSingleResult();
			
			
			return book;
		} finally {
			em.close();
		}
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
