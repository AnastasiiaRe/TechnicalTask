package rozetka;

import framework.Logger;
import framework.adapters.WebDriverManager;
import framework.components.BasicPage;
import framework.platform.html.WebObject;
import framework.platform.web.Locator;
import org.openqa.selenium.support.PageFactory;

public class ProductPage extends BasicPage {

	@Locator(main = "id=base_image")
	protected WebObject baseImage;

	@Locator(main = "css=.detail-tabs-i .detail-chars-l")
	protected WebObject details;

	@Locator(main = "css=.detail-title")
	protected WebObject productTitle;

	@Locator(main = "css=.detail-buy-btn-container .btn-link-i")
	protected WebObject buyButton;

	@Locator(main = "css=.novisited.cart-i-title-link")
	protected WebObject productName;

	@Locator(main = "css=.cart-title")
	protected WebObject cartTitle;

	@Locator(main = "id=popup-checkout")
	protected WebObject checkoutButton;

	public boolean isBaseImageVisible() {
		Logger.info("Check if base image is visible");
		return baseImage.isVisible();
	}

	public boolean isDetailsVisible() {
		Logger.info("Check if product's details are visible");
		return details.isVisible();
	}

	public ProductPage clickBuyButton() {
		Logger.info("Click on Buy button");
		buyButton.click();
		return this;
	}

	public String getProductName() {
		Logger.info("Get product name in cart popup");
		return productName.getText();
	}

	public String getProductTitle() {
		Logger.info("Get product title on the product page");
		return productTitle.getText();
	}

	public String getCartTitle() {
		Logger.info("Get cart title");
		return cartTitle.getText();
	}

	public CheckoutPage clickCheckoutButton() {
		Logger.info("Click Checkout button");
		checkoutButton.click();
		return PageFactory.initElements(WebDriverManager.getDriver(), CheckoutPage.class);
	}
}
