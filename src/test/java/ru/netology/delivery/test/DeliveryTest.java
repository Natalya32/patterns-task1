package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[placeholder=\"Город\"]").setValue(validUser.getCity());
        $("[placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[placeholder=\"Дата встречи\"]").setValue(firstMeetingDate);
        $("[name=\"name\"]").setValue(validUser.getName());
        $("[name=\"phone\"]").setValue(validUser.getPhone());
        $x("//input[@name=\"agreement\"]/..").click();
        $x("//span[contains(text(),\"Запланировать\")]").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на "+firstMeetingDate)
                        , Duration.ofSeconds(15))
                .shouldBe(visible);
        $("[placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[placeholder=\"Дата встречи\"]").setValue(secondMeetingDate);
        $x("//span[contains(text(),\"Запланировать\")]").click();
        $(withText("У вас уже запланирована встреча")).shouldBe(visible, Duration.ofSeconds(15));
        $x("//span[contains(text(),\"Перепланировать\")]").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на "+secondMeetingDate)
                        , Duration.ofSeconds(15))
                .shouldBe(visible);
    }
}
