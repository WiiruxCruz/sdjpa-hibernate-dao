package guru.springframework.jdbc.dao.impl;

import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Created by jt on 8/28/21.
 */
@Component
public class AuthorDaoImpl implements AuthorDao {
	
	private EntityManagerFactory emf;
	
    public AuthorDaoImpl(EntityManagerFactory emf) {
		this.emf = emf;
	}
    
    @Override
	public List<Author> findAll() {
		// TODO Auto-generated method stub
    	EntityManager em = getEntityManager();
    	try {
    		TypedQuery<Author> typedQuery = em.createNamedQuery("author_find_all", Author.class);
    		
    		List<Author> authors = typedQuery.getResultList();
    		return authors;
    	} finally {
    		em.close();
    	}
		
	}
    
    @Override
	public List<Author> listaAuthorByLastName(String lastName) {
		// TODO Auto-generated method stub
    	EntityManager em = getEntityManager();
    	try {
    		Query query = em.createQuery("SELECT a FROM Author a where a.lastName like :last_name");
    		query.setParameter("last_name", lastName + "%");
    		
    		List<Author> authors = query.getResultList();
    		return authors;
    	} finally {
    		em.close();
    	}
		
	}

	@Override
    public Author getById(Long id) {
		return getEntityManager().find(Author.class, id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
    	EntityManager em = getEntityManager();
    	//syntax of the entity en java 
    	//TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a "
    			//+ "WHERE a.firstName = :first_name and a.lastName = :last_name", Author.class);
    	TypedQuery<Author> query = em.createNamedQuery("find_by_name", Author.class);
    	
    	query.setParameter("first_name", firstName);
    	query.setParameter("last_name", lastName);
    	
    	Author author = query.getSingleResult();
    	em.close();
        
    	return author;
    }
    
    @Override
    public Author findAuthorByNameCriteria(String firstName, String lastName) {
    	EntityManager em = getEntityManager();
    	try {
    		CriteriaBuilder cb = em.getCriteriaBuilder();
    		CriteriaQuery<Author> cq = cb.createQuery(Author.class);
    		
        	Root<Author> root = cq.from(Author.class);
        	
        	ParameterExpression<String> firstNameParam = cb.parameter(String.class);
        	ParameterExpression<String> lastNameParam = cb.parameter(String.class);
        	
        	Predicate firstNamePred = cb.equal(root.get("firstName"), firstNameParam);
        	Predicate lastNamePred = cb.equal(root.get("lastName"), lastNameParam);
        	
        	cq.select(root).where(cb.and(firstNamePred, lastNamePred));
        	
        	TypedQuery<Author> typedQuery = em.createQuery(cq);
        	typedQuery.setParameter(firstNameParam, firstName);
        	typedQuery.setParameter(lastNameParam, lastName);
        	
            
        	return typedQuery.getSingleResult();
    	} finally {
    		em.close();
    	}
    }

    @Override
    public Author saveNewAuthor(Author author) {
    	EntityManager em = getEntityManager();
    	//em.joinTransaction();
    	em.getTransaction().begin();
    	em.persist(author);
    	em.flush();
    	em.getTransaction().commit();
    	
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
    	EntityManager em = getEntityManager();
    	em.joinTransaction();
    	em.merge(author);
    	em.flush();
    	em.clear();
        return em.find(Author.class, author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
    	EntityManager em = getEntityManager();
    	
    	em.getTransaction().begin();
    	Author author = em.find(Author.class, id);
    	em.remove(author);
    	em.flush();
    	em.getTransaction().commit();
    }
    
    private EntityManager getEntityManager() {
    	return emf.createEntityManager();
    }
}
