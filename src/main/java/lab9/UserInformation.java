package lab9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
        name = "login_info"
)
@XmlAccessorType(XmlAccessType.FIELD)
public class UserInformation {
    @XmlAttribute(
            required = true
    )
    private String username;
    @XmlAttribute(
            required = true
    )
    private String pass;

    @XmlAttribute
    private long token;

    public UserInformation(String username, String pass, long token) {
        this.username = username;
        this.pass = pass;
        this.token = token;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public UserInformation() {
    }

    public UserInformation(String username, String pass) {
        this.username = username;
        this.pass = pass;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
