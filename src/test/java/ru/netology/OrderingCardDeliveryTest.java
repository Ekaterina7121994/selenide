package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class OrderingCardDeliveryTest {

    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void successfulCompletionOfTheCardDeliveryOrderForm() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Нижний Новгород");
        String planningDate = generateDate(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Ирина");
        $("[data-test-id='phone'] input").setValue("+77654568775");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    void successfulCompletionOfTheCardDeliveryOrderFormInteractionWithComplexElements() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Ни");
        $(withText("Нижний Новгород")).shouldBe(visible).click();
        $(".icon-button").click();
        $(".calendar").shouldBe((visible));
        String planningDate = generateDate(6, "dd.MM.yyyy");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $(".calendar__day").shouldBe(visible).click();
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Ирина");
        $("[data-test-id='phone'] input").setValue("+77654568775");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));
    }
}
