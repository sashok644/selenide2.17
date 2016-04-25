package pagemodules;

import com.google.common.io.Files;
import org.junit.After;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Screenshots.getScreenShotAsFile;

/**
 * Created by 64 on 07.03.2016.
 */
public class BaseTest {

    @After
    public void tearDown() throws IOException {
        screenshot();
    }

    @Attachment(type = "image/png")
    public byte[] screenshot() throws IOException {
        File screenshot = getScreenShotAsFile();
        return Files.toByteArray(screenshot);
    }
}


