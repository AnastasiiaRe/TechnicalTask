package framework.platform;

import rozetka.RozetkaMainPage;

/**
 * SiteNavigatorRozetka
 */
public class SiteNavigatorRozetka extends SiteNavigatorBase {

    public static RozetkaMainPage goToMainRozetkaPage() { return goToPage("rozetka.com.ua", RozetkaMainPage.class); }
}
