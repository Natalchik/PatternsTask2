package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        var registeredUser = getRegisteredUser("active");
        $("[name='login']").val(registeredUser.getLogin());
        $("[name='password']").val(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $(".heading").should(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[name='login']").val(notRegisteredUser.getLogin());
        $("[name='password']").val(notRegisteredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[name='login']").val(blockedUser.getLogin());
        $("[name='password']").val(blockedUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Пользователь заблокирован"));
    }


    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[name='login']").val(wrongLogin);
        $("[name='password']").val(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }


    @Test

    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[name='login']").val(registeredUser.getLogin());
        $("[name='password']").val(wrongPassword);
        $(withText("Продолжить")).click();
        $("[class='notification__content']").should(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}
