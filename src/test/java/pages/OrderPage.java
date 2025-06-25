package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // Локаторы элементов формы заказа
    private final By nameField = By.xpath("//input[contains(@placeholder, 'Имя')]");
    private final By surnameField = By.xpath("//input[contains(@placeholder, 'Фамилия')]");
    private final By addressField = By.xpath("//input[contains(@placeholder, 'Адрес')]");
    private final By metroField = By.xpath("//input[contains(@placeholder, 'Станция метро')]");
    private final By metroDropdown = By.className("select-search__select");
    private final By metroOption = By.xpath(".//div[@class='select-search__select']//button");
    private final By phoneField = By.xpath("//input[contains(@placeholder, 'Телефон')]");
    private final By nextButton = By.xpath("//button[text()='Далее']");
    private final By dateField = By.xpath("//input[contains(@placeholder, 'Когда привезти')]");
    private final By rentalPeriodField = By.className("Dropdown-placeholder");
    private final By rentalPeriodOption = By.xpath("//div[@class='Dropdown-option']");
    private final By blackPearlCheckbox = By.id("black");
    private final By greyHopelessnessCheckbox = By.id("grey");
    private final By commentField = By.xpath("//input[contains(@placeholder, 'Комментарий')]");
    private final By orderButton = By.xpath("//button[contains(@class, 'Button_Middle') and text()='Заказать']");
    private final By confirmButton = By.xpath("//button[text()='Да']");
    private final By successModal = By.className("Order_Modal__YZ-d3");
    private final By secondStepHeader = By.xpath("//div[contains(text(), 'Про аренду')]");
    private final By successOrderHeader = By.xpath("//div[contains(@class, 'Order_ModalHeader') and contains(text(), 'Заказ оформлен')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js = (JavascriptExecutor) driver;
    }

    public By getSecondStepHeaderLocator() {
        return secondStepHeader;
    }

    public void fillFirstStep(String name, String surname, String address, String metro, String phone) {
        waitForElementAndSendKeys(nameField, name);
        waitForElementAndSendKeys(surnameField, surname);
        waitForElementAndSendKeys(addressField, address);
        selectMetroStation(metro);
        waitForElementAndSendKeys(phoneField, phone);

        clickWithScroll(nextButton);
    }

    private void selectMetroStation(String stationName) {
        waitForElementAndClick(metroField);
        wait.until(ExpectedConditions.visibilityOfElementLocated(metroDropdown));

        driver.findElements(metroOption).stream()
                .filter(station -> station.getText().equals(stationName))
                .findFirst()
                .ifPresent(station -> {
                    js.executeScript("arguments[0].scrollIntoView(true);", station);
                    station.click();
                });
    }

    public void fillSecondStep(String date, String rentalPeriod, String color, String comment) {
        waitForElementAndSendKeys(dateField, date);
        driver.findElement(By.tagName("body")).click(); // Close calendar if opened

        selectRentalPeriod(rentalPeriod);
        selectScooterColor(color);
        waitForElementAndSendKeys(commentField, comment);

        clickWithScroll(orderButton);
    }

    private void selectRentalPeriod(String rentalPeriod) {
        waitForElementAndClick(rentalPeriodField);
        wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@class='Dropdown-option' and contains(text(), '" + rentalPeriod + "')]")))
                .click();
    }

    private void selectScooterColor(String color) {
        By colorCheckbox = "black".equals(color) ? blackPearlCheckbox : greyHopelessnessCheckbox;
        js.executeScript("arguments[0].click();", driver.findElement(colorCheckbox));
    }

    public void confirmOrder() {
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(successModal));
        WebElement confirmBtn = modal.findElement(confirmButton);
        wait.until(ExpectedConditions.elementToBeClickable(confirmBtn));
        js.executeScript("arguments[0].click();", confirmBtn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(successOrderHeader));
    }

    public boolean isSuccessModalDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successModal)).isDisplayed();
    }

    // Вспомогательные методы
    private void waitForElementAndClick(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    private void waitForElementAndSendKeys(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.clear();
        element.sendKeys(text);
    }

    private void clickWithScroll(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
    }
}