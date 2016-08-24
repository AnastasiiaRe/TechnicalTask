package org.rozetka.tests;

import rozetka.CategoryPage;
import rozetka.CheckoutPage;
import rozetka.ProductPage;
import rozetka.RozetkaMainPage;
import framework.platform.SiteNavigatorRozetka;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * OrderingTest
 */
public class OrderingTest {

	@Test (groups = {"rozetka"})
	public void verifyOrderingRozetka() throws InterruptedException {
		RozetkaMainPage rozetkaPage = SiteNavigatorRozetka.goToMainRozetkaPage();
		assertTrue(rozetkaPage.isMainMenuVisible(), "MainMenu should be visible");
		CategoryPage categoryPage = rozetkaPage.clickCategory().clickSubCategory();
		assertTrue(categoryPage.getTitle().contains("жесткие диски"), "Page title should contains 'жесткие диски'");
		ProductPage productPage = categoryPage.clickOnAvailableProduct();
		assertTrue(productPage.isBaseImageVisible(), "Base image should be visible");
		assertTrue(productPage.isDetailsVisible(), "Product's details should be visible");
		String title = productPage.getProductTitle();
		productPage.clickBuyButton();
		assertTrue(productPage.getCartTitle().contains("Вы добавили товар в корзину"), "Cart title should contain 'Вы добавили товар в корзину'");
		String name = productPage.getProductName();
		assertTrue(title.contains(name), "Wrong product was added to cart");
		CheckoutPage checkoutPage = productPage.clickCheckoutButton()
				.enterName()
				.clickLocationDropDown()
				.chooseLocation()
				.enterPhoneNumber()
				.enterEmail()
				.clickContinueButton()
				.chooseDeliveryMethod()
				.choosePaymentMethod();
		assertTrue(checkoutPage.isMakeOrderButtonAvailable(), "Make order button should be enabled");
	}
}
