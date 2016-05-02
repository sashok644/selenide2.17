package helpers;

import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.actions;

/**
 * Created by MFM on 02.05.2016.
 */
public class Helpers {

    public static void doubleClick(WebElement element) {
        actions().doubleClick(element).perform();
    }
}
