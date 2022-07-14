package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        val registeredUser = getRegisteredUser("active");
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $(withText("Личный кабинет")). shouldBe(visible, Duration.ofSeconds(5));
        //$(".heading").should(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        val notRegisteredUser = getUser("blocked");
        $("[name='login']").setValue(notRegisteredUser.getLogin());
        $("[name='password']").setValue(notRegisteredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        val blockedUser = getUser("blocked");
        $("[name='login']").setValue(blockedUser.getLogin());
        $("[name='password']").setValue(blockedUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }


    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        val registeredUser = getRegisteredUser("active");
        val wrongLogin = getRandomLogin();
        $("[name='login']").setValue(wrongLogin);
        $("[name='password']").setValue(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Неверно указан логин или пароль"));
    }


    @Test

    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        val registeredUser = getRegisteredUser("active");
        val wrongPassword = getRandomPassword();
        $("[name='login']").val(registeredUser.getLogin());
        $("[name='password']").val(wrongPassword);
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Неверно указан логин или пароль"));
    }
}
