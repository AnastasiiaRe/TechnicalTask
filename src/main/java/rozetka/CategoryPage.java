package rozetka;

import framework.Logger;
import framework.adapters.WebDriverManager;
import framework.components.BasicPage;
import framework.platform.html.WebObject;
import framework.platform.web.Locator;
import org.openqa.selenium.support.PageFactory;

public class CategoryPage extends BasicPage {

	@Locator(main = "css=.g-i-tile-i-title.clearfix>a")
	protected WebObject availableProduct;

	@Locator(main = "css=.c-cols-inner-l>h1")
	protected WebObject pageTitle;

	public String getTitle() {
		Logger.info("Get page title");
		pageTitle.waitUntilVisible();
		return pageTitle.getText().toLowerCase();
	}

	public ProductPage clickOnAvailableProduct() {
		Logger.info("Click on available product");
		availableProduct.clickOnElementNumber(2);
		return PageFactory.initElements(WebDriverManager.getDriver(), ProductPage.class);
	}
}
