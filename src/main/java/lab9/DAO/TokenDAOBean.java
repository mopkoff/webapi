package lab9.DAO;

import lab9.Model.Token;
import lab9.Model.User;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.security.auth.login.LoginException;

@Stateless
@LocalBean
public class TokenDAOBean {

    @PersistenceContext(unitName = "Eclipselink_JPA")
    private EntityManager em;

    public User getUser(Long token) {
        Token tokenEntity = em.find(Token.class, token);
        if (tokenEntity == null) {
            throw new IllegalStateException();
        }
        return tokenEntity.getUser();
    }

    public Long generateToken(User user) {
        Token tokenEntity = new Token(user);
       // em.persist(user);
        em.persist(tokenEntity);
        return tokenEntity.getToken();
    }

    public Long getToken(User user)throws LoginException{
        String username =  user.getUsername();
        int passwordHash =  user.getPasswordHash();
        Query query = this.em.createQuery("Select u from users u where u.username = :username and u.passwordHash = :passwordHash");
        query.setParameter("username", username);
        query.setParameter("passwordHash", passwordHash);
        List<User> users = query.getResultList();
        if (users.isEmpty()){
            throw new LoginException( "Wrong login or password!" );
        }
        user = users.get(0);
        Query tokenQuery = em.createQuery("Select t from tokens t");
        List<Token> tokens = tokenQuery.getResultList();
        for (Token t : tokens){
            if (t.getUser().getId() == user.getId())
                return t.getToken();
        }
        return generateToken(user);
    }

    public boolean isTokenExist(Long token){
         if (em.find(Token.class, token) == null)
             return false;
         return true;
    }

    public void removeToken(Long token) {
        Token tokenEntity = em.find(Token.class, token);
        if (tokenEntity == null) {
            return;
        }
        em.remove(tokenEntity);
    }
}
