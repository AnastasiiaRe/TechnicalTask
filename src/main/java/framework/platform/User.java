package framework.platform;

import framework.Settings;
import framework.platform.utilities.StringUtils;

/**
 * Describes default user model with credentials such as email/password etc.
 * <p>
 *     Provides generation of objects of type {@link User}
 */
public class User {

    private String email;
    private String password;

    public User(){
        this.email = StringUtils.generateRandomEmail();
        this.password = Settings.config.getDefaultUserPassword();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
