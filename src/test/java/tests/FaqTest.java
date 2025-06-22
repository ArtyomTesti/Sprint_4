package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import utils.WebDriverFactory;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class FaqTest {
    private WebDriver driver;
    private MainPage mainPage;
    private final String browser;
    private final int questionIndex;
    private final String expectedAnswer;

    public FaqTest(String browser, int questionIndex, String expectedAnswer) {
        this.browser = browser;
        this.questionIndex = questionIndex;
        this.expectedAnswer = expectedAnswer;
    }

    @Parameterized.Parameters(name = "Browser: {0}, Question: {1}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {"chrome", 0, "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
                {"chrome", 1, "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},
                {"chrome", 2, "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},
                {"chrome", 3, "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},
                {"chrome", 4, "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},
                {"chrome", 5, "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},
                {"chrome", 6, "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."},
                {"chrome", 7, "Да, обязательно. Всем самокатов! И Москве, и Московской области."},
                {"firefox", 0, "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
                {"firefox", 1, "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},
                {"firefox", 2, "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},
                {"firefox", 3, "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},
                {"firefox", 4, "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},
                {"firefox", 5, "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},
                {"firefox", 6, "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."},
                {"firefox", 7, "Да, обязательно. Всем самокатов! И Москве, и Московской области."},
        };
    }

    @Before
    public void setup() {
        driver = WebDriverFactory.getDriver(browser);
        mainPage = new MainPage(driver);
        mainPage.open();
    }

    @Test
    public void testFaqAnswer() {
        mainPage.expandFaqQuestion(questionIndex);
        String actualAnswer = mainPage.getFaqAnswerText(questionIndex);
        assertTrue("Ответ не соответствует ожидаемому в браузере " + browser,
                actualAnswer.contains(expectedAnswer));
    }

    @After
    public void teardown() {
            driver.quit();
    }
}