package framework.platform;

import framework.Settings;

/**
 * Contains collection of generated and pre prepared user models.
 */
public abstract class UserCache {

    public static User MAIN_USER = new User(Settings.config.getMainUserName(), Settings.config.getDefaultUserPassword());

}
