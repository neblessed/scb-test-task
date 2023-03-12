package ui;

import com.codeborne.selenide.ElementsCollection;
import configs.Config;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.pages.DemoQaPage;

import static com.codeborne.selenide.Selenide.*;

public class UiTests extends Config {

    @Test
    @DisplayName("Herokuapp - Drag'n'Drop")
    public void dragNDropTest() {
        open("https://the-internet.herokuapp.com/drag_and_drop");
        ElementsCollection blocks = $$("#columns div");
        blocks.first().dragAndDropTo(blocks.last());
        Assertions.assertThat(blocks.last().getText()).isEqualTo("A");
    }

    @Test
    @DisplayName("DemoQA - Сохранение с заполненными полями")
    public void checkRegistrationFormFullFilled() {
        open("https://demoqa.com/automation-practice-form");
        executeJavaScript("$('footer').remove()");
        DemoQaPage demoQaPage = new DemoQaPage();
        demoQaPage.setFirstName("Test")
                .setLastName("Tester")
                .setEmail("test@test.ru")
                .setGender("Male")
                .setPhoneNumber("9656828874")
                .setBirthDate("02", "February", "1998")
                .setSubject("Arts")
                .setHobby("Sports", "Reading", "Music")
                .setAddress("Test location")
                .selectState("NCR")
                .selectCity("Delhi")
                .clickOnSubmit();

        demoQaPage.checkResultsFormIsVisible()
                .verifyResult("Student Name", "Test Tester")
                .verifyResult("Student Email", "test@test.ru")
                .verifyResult("Gender", "Male")
                .verifyResult("Mobile", "9656828874")
                .verifyResult("Subjects", "Arts")
                .verifyResult("Hobbies", "Sports, Reading, Music")
                .verifyResult("Address", "Test location")
                .verifyResult("State and City", "NCR Delhi");
    }

    @Test
    @DisplayName("DemoQA - Сохранение с пустыми полями")
    public void checkRegFormSaveWithEmptyFields() {
        open("https://demoqa.com/automation-practice-form");
        DemoQaPage demoQaPage = new DemoQaPage();
        demoQaPage.clickOnSubmit(); //сохраняю пустую
        demoQaPage.checkFieldsIsMarkedAsWrong()
                .checkResultsFormIsHidden();
    }

    @Test
    @DisplayName("DemoQA - Сохранение с пропуском необязательных полей")
    public void checkRegFormSaveWithEmptyRequiredFields() {
        open("https://demoqa.com/automation-practice-form");
        DemoQaPage demoQaPage = new DemoQaPage();
        demoQaPage.setFirstName("Test")
                .setLastName("Tester")
                .setGender("Male")
                .setPhoneNumber("9656828874")
                .clickOnSubmit();

        demoQaPage.checkResultsFormIsVisible()
                .verifyResult("Student Name", "Test Tester")
                .verifyResult("Gender", "Male")
                .verifyResult("Mobile", "9656828874");
    }

    @Test
    @DisplayName("DemoQA - Проверка маски для Email")
    public void checkEmailFieldWithoutETASign() {
        open("https://demoqa.com/automation-practice-form");
        DemoQaPage demoQaPage = new DemoQaPage();
        demoQaPage.setEmail("testtest.ru")
                .checkEmailIsMarkedAsWrong();
    }

    @Test
    @DisplayName("DemoQA - Недопустимость ' ' в обязательных полях")
    public void checkRegFormSaveWithSpaceInRequiredFields(){
        open("https://demoqa.com/automation-practice-form");
        DemoQaPage demoQaPage = new DemoQaPage();
        demoQaPage.setFirstName(" ")
                .setLastName(" ")
                .setGender("Male")
                .setPhoneNumber("12345678943")
                .clickOnSubmit();

        demoQaPage.checkResultsFormIsHidden();
    }
}
