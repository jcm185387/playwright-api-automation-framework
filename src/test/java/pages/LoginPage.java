package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class LoginPage {
    private final Page page;
    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator flashMessage;

    public LoginPage(Page page) {
        this.page = page;
        this.usernameInput = page.locator("#username");
        this.passwordInput = page.locator("#password");
        this.loginButton = page.locator("button[type='submit']");
        this.flashMessage = page.locator("#flash");
    }

    public void navigate() {
        page.navigate("https://the-internet.herokuapp.com/login");
    }

    public void login(String user, String pass) {
        usernameInput.fill(user);
        passwordInput.fill(pass);
        loginButton.click();
    }

    public Locator getFlashMessage() {
        return flashMessage;
    }
}