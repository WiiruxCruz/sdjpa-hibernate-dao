package guru.springframework.jdbc.dao.impl;

import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

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
    public Author getById(Long id) {
		return getEntityManager().find(Author.class, id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return null;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return null;
    }

    @Override
    public Author updateAuthor(Author author) {
        return null;
    }

    @Override
    public void deleteAuthorById(Long id) {

    }
    
    private EntityManager getEntityManager() {
    	return emf.createEntityManager();
    }
}
