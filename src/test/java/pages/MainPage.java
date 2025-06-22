package pages;

import org.openqa.selenium.JavascriptExecutor;  // Добавьте этот импорт
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class MainPage {
    private final WebDriver driver;

    // Локаторы
    // Заголовок страницы
    private final By header = By.className("Header_Logo__23yGT");

    // Кнопка "Заказать" вверху
    private final By orderButtonTop = By.className("Button_Button__ra12g");

    // Кнопка "Заказать" внизу
    private final By orderButtonBottom = By.xpath(".//button[@class='Button_Button__ra12g Button_UltraBig__UU3Lp']");

    // Раздел "Вопросы о важном"
    private final By faqSection = By.className("Home_FAQ__3uVm4");

    // Вопросы в FAQ (список)
    private final By faqQuestions = By.xpath(".//div[@data-accordion-component='AccordionItem']");

    // Ответы в FAQ (список)
    private final By faqAnswers = By.xpath(".//div[@data-accordion-component='AccordionItemPanel']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
        closeCookieBanner();
    }

    public void clickOrderButtonTop() {
        driver.findElement(orderButtonTop).click();
    }

    public void clickOrderButtonBottom() {
        WebElement element = driver.findElement(orderButtonBottom);
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element);
        element.click();
    }

    public void expandFaqQuestion(int index) {
        List<WebElement> questions = driver.findElements(faqQuestions);
        if (index >= 0 && index < questions.size()) {
            questions.get(index).click();
        }
    }

    public String getFaqAnswerText(int index) {
        List<WebElement> answers = driver.findElements(faqAnswers);
        if (index >= 0 && index < answers.size()) {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(answers.get(index)));
            return answers.get(index).getText();
        }
        return "";
    }
    public void closeCookieBanner() {
        try {
            By cookieBanner = By.className("App_CookieConsent__1yUIN");
            By acceptButton = By.xpath("//button[contains(text(), 'да все привыкли')]");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            if (!driver.findElements(cookieBanner).isEmpty()) {
                WebElement button = wait.until(ExpectedConditions.elementToBeClickable(acceptButton));
                button.click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(cookieBanner));
            }
        } catch (Exception e) {
            System.out.println("Не удалось закрыть cookie-баннер: " + e.getMessage());
        }
    }
}