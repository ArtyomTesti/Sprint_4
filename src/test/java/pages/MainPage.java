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

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";

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

    // Cookie баннер
    private final By cookieBanner = By.className("App_CookieConsent__1yUIN");

    // Кнопка принятия cookie
    private final By acceptCookieButton = By.xpath("//button[contains(text(), 'да все привыкли')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(BASE_URL);
        closeCookieBanner();
    }

    public void clickOrderButtonTop() {
        driver.findElement(orderButtonTop).click();
    }

    public void clickOrderButtonBottom() {
        WebElement element = driver.findElement(orderButtonBottom);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        element.click();
    }

    public void expandFaqQuestion(int index) {
        List<WebElement> questions = driver.findElements(faqQuestions);
        if (index >= 0 && index < questions.size()) {
            WebElement question = questions.get(index);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", question);
             new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(question)).click();
        }
    }

    public String getFaqAnswerText(int index) {
        List<WebElement> answers = driver.findElements(faqAnswers);
        if (index >= 0 && index < answers.size()) {
            WebElement answer = answers.get(index);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", answer);
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOf(answer));
            return answer.getText();
        }
        return "";
    }

    public void closeCookieBanner() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        List<WebElement> banners = driver.findElements(cookieBanner);
        if (!banners.isEmpty()) {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(acceptCookieButton));
            button.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(cookieBanner));
        }
    }
}