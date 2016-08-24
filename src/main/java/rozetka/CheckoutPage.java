package rozetka;

import framework.Logger;
import framework.components.BasicPage;
import framework.platform.html.WebObject;
import framework.platform.utilities.StringUtils;
import framework.platform.web.Locator;

public class CheckoutPage extends BasicPage {

	@Locator(main = "id=reciever_name")
	protected WebObject name;

	@Locator(main = "id=suggest_locality")
	protected WebObject location;

	@Locator(main = "css=.suggestions>li")
	protected WebObject suggestionLocation;

	@Locator(main = "id=reciever_phone")
	protected WebObject phoneNumber;

	@Locator(main = "id=reciever_email")
	protected WebObject email;

	@Locator(main = "css=.check-f-i-field .btn-link-i")
	protected WebObject continueButton;

	@Locator(main = "css=.check-method-subl .input-check-radio-inner.check-method-input-radio")
	protected WebObject deliveryMethod;

	@Locator(main = "css=.check-payment-l .input-check-radio-inner.check-method-input-radio")
	protected WebObject payment;

	@Locator(main = "id=make-order")
	protected WebObject makeOrderButton;

	public CheckoutPage enterName() {
		Logger.info("Enter name");
		name.type(StringUtils.generateRandomStrAlphabetic(6));
		return this;
	}

	public CheckoutPage clickLocationDropDown() {
		Logger.info("Click location dropdown");
		location.click();
		return this;
	}

	public CheckoutPage chooseLocation() {
		Logger.info("Choose location from suggestion");
		int countOfLocation= suggestionLocation.getElementsCount();
		int locationNumber = StringUtils.generateRandomInt(countOfLocation);
		suggestionLocation.clickOnElementNumber(locationNumber + 1);
		return this;
	}

	public CheckoutPage enterPhoneNumber() {
		Logger.info("Enter phone number");
		phoneNumber.type("050" + StringUtils.generateRandomNumberic(7));
		return this;
	}

	public CheckoutPage enterEmail() {
		Logger.info("Enter email");
		email.type(StringUtils.generateRandomEmail());
		waitForAjaxRequestToBeFinished();
		return this;
	}

	public CheckoutPage chooseDeliveryMethod() {
		Logger.info("Choose delivery method");
		deliveryMethod.clickOnElementNumber(2);
		waitForAjaxRequestToBeFinished();
		return this;
	}

	public CheckoutPage choosePaymentMethod() {
		Logger.info("Choose payment method");
		sleepFor(2000); //need for page reloading
		payment.clickOnElementNumber(2);
		return this;
	}

	public CheckoutPage clickContinueButton() {
		Logger.info("Click Continue button");
		sleepFor(2000);    //need for page reloading
		continueButton.click();
		waitForAjaxRequestToBeFinished();
		return this;
	}

	public boolean isMakeOrderButtonAvailable() {
		Logger.info("Check if Make order button is available");
		return makeOrderButton.isEnabled();
	}
}
