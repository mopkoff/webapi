package lab9.Model;

import java.io.Serializable;
import javax.persistence.*;

@Entity(name = "tokens")
public class Token implements Serializable {
    @OneToOne
    @JoinColumn(name="UserID")
    private User user;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long token;

    public Token(User user) {
        this.user = user;
    }

    public Token(Long token, User user) {
        this.token = token;
        this.user = user;
    }

    public Token() {
    }
    public void secureToken(){
        this.user.setId(0); 
        this.user.setPasswordHash(0);
    }
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getToken() {
        return this.token;
    }

    public void setToken(Long token) {
        this.token = token;
    }
}
