package configs;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Configuration.*;

public class Config {
    protected final String CLEAN_UI_URL = "https://cleanuri.com/api/v1/shorten";
    protected final String RANDOM_USER = "https://randomuser.me/api/";

    @BeforeAll
    static void setUp(){
        browser = "chrome";
        browserSize = "1920x1080";
        screenshots = false;
        headless = false;
    }

    @AfterAll
    static void tearDown(){
        Selenide.webdriver().driver().close();
    }

}
