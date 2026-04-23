package tests.web;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class WebTest {
    static Playwright playwright;
    static Browser browser;

    Page page;

    @BeforeAll
    static void launcg() {
        playwright = Playwright.create();
        // Headless = false para que puedas ver el navegador durante tu práctica
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @Test
    @DisplayName("Login exitoso en The Internet Herokuapp")
    void loginTest() {
        page = browser.newPage();
        LoginPage loginPage = new LoginPage(page);

        loginPage.navigate();
        loginPage.login("tomsmith", "SuperSecretPassword!");


        // Aserción inteligente de Playwright (espera automática hasta 5s)
        assertThat(loginPage.getFlashMessage()).containsText("You logged into a secure area!");
        page.close();

    }


    @AfterAll
    static void stop() {
        playwright.close();
    }
}