package lab9.Model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity(name = "users")
@XmlRootElement
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    private String username;
    private int passwordHash;

    public User(){}
    public User(String username) {
        this.id = 0;
        this.username = username;
    }
    //Создание хеша по строковому значению пароля
    public User(String username, String password) {
        this.username = username;
        this.passwordHash = password.hashCode();
    }

    public User(String username, int hash) {
        this.username = username;
        this.passwordHash = hash;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(int passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean equals(String username, String password){
        return ((this.username.equals(username)) || (password.hashCode() == this.passwordHash));
    }
    @Override
    public String toString(){
        return "username = " + getUsername() + "\npasswordHash = " + getPasswordHash();
    }
}


