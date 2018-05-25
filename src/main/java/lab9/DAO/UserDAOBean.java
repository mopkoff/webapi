package lab9.DAO;

import lab9.Model.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.*;

import java.util.List;

@Stateless
@LocalBean
public class UserDAOBean  {
    @PersistenceContext(unitName = "Eclipselink_JPA")
    private EntityManager em;

    public User getUserByName(String username) {
        Query query = this.em.createQuery("Select u from users u where u.username = :username");
        query.setParameter("username", username);
        List<User> users = query.getResultList();
        if (users == null || users.size() == 0)
            return null;
        return users.get(0);
    }

    public User addUser(User user) throws IllegalAccessException {
        String username = user.getUsername();
        int hash = user.getPasswordHash();
        Query query = this.em.createQuery("Select u from users u where u.username = :username");
        query.setParameter("username", username);
        List<User> users = query.getResultList();
        if (users == null) {
            throw new NullPointerException();
        } else if (users.size() != 0) {
            throw new IllegalAccessException();
        } else {
            this.em.persist(new User(username, hash));
            Query queryAfter = this.em.createQuery("Select u from users u where u.username = :username");
            queryAfter.setParameter("username", username);
            List<User> usersAfter = queryAfter.getResultList();
            return usersAfter.get(0);
        }
    }

    public void updateUser(User user){em.persist(user);}

}
