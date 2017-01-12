package ar.edu.itba.paw.webapp.auth;

/**
 * Login Data Transfer Object.
 */
public class LoginDto {
    private String username;
    private String password;
    private String redirect = "/"; // TODO use uriInfo here, jackson breaks if the following is used: uriInfo.getBaseUri().toString();


    public LoginDto() {}

    public LoginDto(String username, String password, String redirect) {
        this.username = username;
        this.password = password;
        this.redirect = redirect; //uriInfo.getBaseUriBuilder().path(redirect).build().toString();  //TODO use UriInfo
    }

    public LoginDto(String username, String password) {
        this(username, password, "/"); //TODO use UriInfo
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirectUrl) {
        this.redirect = redirectUrl;
    }
}
