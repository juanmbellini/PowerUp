package ar.edu.itba.paw.webapp.auth.json;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Login Data Transfer Object.
 */
@XmlRootElement(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
public class JsonLoginDto {
    private String username;
    private String password;

    public JsonLoginDto() {}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
