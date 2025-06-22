package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import pages.OrderPage;
import utils.WebDriverFactory;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest {
    private WebDriver driver;
    private MainPage mainPage;
    private OrderPage orderPage;
    private final String browser;
    private final String orderButtonLocation;
    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String phone;
    private final String date;
    private final String rentalPeriod;
    private final String color;
    private final String comment;

    public OrderTest(String browser, String orderButtonLocation, String name, String surname,
                     String address, String metro, String phone, String date,
                     String rentalPeriod, String color, String comment) {
        this.browser = browser;
        this.orderButtonLocation = orderButtonLocation;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.date = date;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "Browser: {0}, Button: {1}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {"chrome", "top", "Иван", "Иванов", "ул. Ленина, 1", "Лубянка", "+79998887766", "01.01.2023", "сутки", "black", "Позвонить за час"},
                {"chrome", "bottom", "Петр", "Петров", "ул. Пушкина, 10", "Сокольники", "+79997776655", "10.01.2023", "двое суток", "grey", "Не звонить"},
                {"firefox", "top", "Мария", "Сидорова", "пр. Мира, 5", "Лубянка", "+79996665544", "15.01.2023", "трое суток", "black", "Оставить у двери"},
                {"firefox", "bottom", "Алексей", "Кузнецов", "ул. Тверская, 15", "Сокольники", "+79995554433", "20.01.2023", "четверо суток", "grey", "Позвонить за 30 минут"}
        };
    }

    @Before
    public void setup() {
        driver = WebDriverFactory.getDriver(browser);
        driver.manage().window().maximize();
        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);

        // Открываем главную страницу и принимаем куки
        mainPage.open();

        // Ждем пока страница полностью загрузится
        try {
            Thread.sleep(2000); // Краткая пауза для полной загрузки
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOrderFlow() {
        try {
            // Выбираем точку входа (верхнюю или нижнюю кнопку заказа)
            if ("top".equals(orderButtonLocation)) {
                mainPage.clickOrderButtonTop();
            } else {

                mainPage.clickOrderButtonBottom();
            }

            // Ждем пока страница заказа загрузится
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Заполняем первую страницу заказа
            orderPage.fillFirstStep(name, surname, address, metro, phone);

            // Ждем загрузки второй страницы
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Заполняем вторую страницу заказа
            orderPage.fillSecondStep(date, rentalPeriod, color, comment);

            // Подтверждаем заказ
            orderPage.confirmOrder();

            // Проверяем успешное оформление заказа
            assertTrue("Модальное окно успешного заказа не отображается",
                    orderPage.isSuccessModalDisplayed());

        } catch (Exception e) {
            System.err.println("Ошибка в тесте для браузера " + browser + ": " + e.getMessage());
            throw e;
        }
    }
    @After
    public void teardown() {
            driver.quit();
        }
    }
