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

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js = (JavascriptExecutor) driver;
    }

    public void fillFirstStep(String name, String surname, String address, String metro, String phone) {
        // Ждем пока страница заказа полностью загрузится
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));

        WebElement nameElement = wait.until(ExpectedConditions.elementToBeClickable(nameField));
        nameElement.sendKeys(name);

        driver.findElement(surnameField).sendKeys(surname);
        driver.findElement(addressField).sendKeys(address);
        selectMetroStation(metro);
        driver.findElement(phoneField).sendKeys(phone);

        // Прокручиваем к кнопке "Далее" и кликаем
        WebElement nextBtn = driver.findElement(nextButton);
        js.executeScript("arguments[0].scrollIntoView(true);", nextBtn);
        nextBtn.click();
    }

    private void selectMetroStation(String stationName) {
        WebElement metroInput = wait.until(ExpectedConditions.elementToBeClickable(metroField));
        metroInput.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(metroDropdown));

        for (WebElement station : driver.findElements(metroOption)) {
            if (station.getText().equals(stationName)) {
                js.executeScript("arguments[0].scrollIntoView(true);", station);
                station.click();
                return;
            }
        }
    }

    public void fillSecondStep(String date, String rentalPeriod, String color, String comment) {
        // Ждем пока вторая страница заказа загрузится
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateField));

        // Заполняем дату
        WebElement dateInput = driver.findElement(dateField);
        dateInput.clear();
        dateInput.sendKeys(date);

        // Кликаем вне поля даты, чтобы закрыть календарь если он открылся
        driver.findElement(By.tagName("body")).click();

        // Выбираем срок аренды
        WebElement rentalPeriodElement = wait.until(ExpectedConditions.elementToBeClickable(rentalPeriodField));
        js.executeScript("arguments[0].scrollIntoView(true);", rentalPeriodElement);
        rentalPeriodElement.click();

        WebElement periodOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='Dropdown-option' and contains(text(), '" + rentalPeriod + "')]")));
        periodOption.click();

        // Выбираем цвет
        if ("black".equals(color)) {
            js.executeScript("arguments[0].click();", driver.findElement(blackPearlCheckbox));
        } else if ("grey".equals(color)) {
            js.executeScript("arguments[0].click();", driver.findElement(greyHopelessnessCheckbox));
        }

        // Заполняем комментарий
        driver.findElement(commentField).sendKeys(comment);

        // Кликаем кнопку заказа
        WebElement orderBtn = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        js.executeScript("arguments[0].scrollIntoView(true);", orderBtn);
        orderBtn.click();
    }

    public void confirmOrder() {
        // Ждем появления окна
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(successModal));

        // Ищем кнопку "Да" именно внутри окна
        WebElement confirmBtn = modal.findElement(confirmButton);

        // Проверяем кликабельность
        wait.until(ExpectedConditions.elementToBeClickable(confirmBtn));

        // Кликаем
        js.executeScript("arguments[0].click();", confirmBtn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'Order_ModalHeader') and contains(text(), 'Заказ оформлен')]")));
    }

    public boolean isSuccessModalDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successModal)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}