package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;
import ru.netology.testmode.data.RegistrationDto;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.*;

class AuthTest {

    RegistrationDto generator1 = DataGenerator.getUserActive();
    RegistrationDto generator2 = DataGenerator.getUserBlocked();

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        sendRequest(generator1);
        $("[name='login']").setValue(generator1.getLogin());
        $("[name='password']").setValue(generator1.getPassword());
        $(withText("Продолжить")).click();
        $(withText("Личный кабинет")).shouldBe(visible, Duration.ofSeconds(5));

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        $("[name='login']").setValue(generator2.getLogin());
        $("[name='password']").setValue(generator2.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        sendRequest(generator2);
        $("[name='login']").setValue(generator2.getLogin());
        $("[name='password']").setValue(generator2.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Пользователь заблокирован"));
    }


    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        sendRequest(generator2);
        val wrongLogin = getRandomLogin();
        $("[name='login']").setValue(wrongLogin);
        $("[name='password']").setValue(generator2.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Неверно указан логин или пароль"));
    }


    @Test

    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        sendRequest(generator2);
        val wrongPassword = getRandomPassword();
        $("[name='login']").val(generator2.getLogin());
        $("[name='password']").val(wrongPassword);
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Неверно указан логин или пароль"));
    }
}
