package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static com.codeborne.selenide.Condition.text;
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
        $$(".menu-item").findBy(text("Нижний Новгород")).click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.chord(Keys.BACK_SPACE));
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime choiceDate = startDate.plusDays(6);
        int choiceDay = choiceDate.getDayOfMonth();
        int maxDayMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        if (maxDayMonth - choiceDay < 7) {
            $(".popup_target_anchor").find(".calendar__arrow_direction_right", 1).click();
        }
        String date = choiceDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $(".calendar__layout").$$(".calendar__day").findBy(text(String.valueOf(choiceDay))).click();
        $("[data-test-id='name'] input").setValue("Ирина");
        $("[data-test-id='phone'] input").setValue("+77654568775");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='notification']")
                .shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__title").shouldHave(Condition.exactText("Успешно!"));
        $(".notification__content").shouldHave(Condition.exactText("Встреча успешно забронирована на " + date));
    }
}
