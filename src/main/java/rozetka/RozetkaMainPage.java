package rozetka;

import framework.Logger;
import framework.adapters.WebDriverManager;
import framework.components.BasicPage;
import framework.platform.html.WebObject;
import framework.platform.web.Locator;
import org.openqa.selenium.support.PageFactory;

public class RozetkaMainPage extends BasicPage {

	@Locator(main = "id=new-fat-menu")
	protected WebObject mainMenu;

	@Locator(main = "css=#m-main-new>li[id='1']>a")
	protected WebObject categories;

	@Locator(main = "css=.f-menu-sub-l-i>a[href*='hdd']")
	protected WebObject hddSubCategory;

	public boolean isMainMenuVisible() {
		Logger.info("Check if Main menu is visible");
		return mainMenu.isVisible();
	}

	public RozetkaMainPage clickCategory() {
		Logger.info("Click on the 'Laptop and Computer' category");
		categories.mouseHover();
		return this;
	}

	public CategoryPage clickSubCategory() {
		Logger.info("Click on hdd sub-category");
		hddSubCategory.waitElementsReady();
		hddSubCategory.click();
		return PageFactory.initElements(WebDriverManager.getDriver(), CategoryPage.class);
	}
}
