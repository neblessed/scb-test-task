package ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class DemoQaPage {
    private final SelenideElement firstNameInput = $("#firstName");
    private final SelenideElement lastNameInput = $("#lastName");
    private final SelenideElement emailInput = $("#userEmail");
    private final SelenideElement genderSelector = $("#genterWrapper");
    private final SelenideElement mobileInput = $("#userNumber");
    private final SelenideElement dateField = $("#dateOfBirthInput");
    private final SelenideElement dateMonthSelect = $(".react-datepicker__month-select");
    private final SelenideElement subjectInput = $("#subjectsInput");
    private final SelenideElement hobbySelector = $("#hobbiesWrapper");
    private final SelenideElement addressInput = $("#currentAddress");
    private final SelenideElement stateInput = $("#state");
    private final SelenideElement stateCityWrapper = $("#stateCity-wrapper");
    private final SelenideElement cityInput = $("#city");
    private final SelenideElement submitBtn = $("#submit");
    private final SelenideElement resultsForm = $(".modal-content");
    private final SelenideElement tableHeader = $(".modal-title");
    private final String TABLE_TITLE = "Thanks for submitting the form";

    public DemoQaPage setFirstName(String name) {
        firstNameInput.setValue(name);
        return this;
    }

    public DemoQaPage setLastName(String lastName) {
        lastNameInput.setValue(lastName);
        return this;
    }

    public DemoQaPage setEmail(String email) {
        emailInput.setValue(email);
        return this;
    }

    public DemoQaPage setGender(String gender) {
        genderSelector.$(byText(gender)).click();
        return this;
    }

    public DemoQaPage setPhoneNumber(String mobile) {
        mobileInput.setValue(mobile);
        return this;
    }

    public DemoQaPage setSubject(String subject) {
        subjectInput.setValue(subject)
                .pressEnter();
        return this;
    }

    public DemoQaPage setHobby(String... hobby) {
        for (String s : hobby) {
            hobbySelector.$(byText(s)).click();
        }
        return this;
    }

    public DemoQaPage setAddress(String address) {
        addressInput.setValue(address);
        return this;
    }

    public DemoQaPage selectState(String state) {
        stateInput.click();
        stateCityWrapper.$(byText(state)).click();
        return this;
    }

    public DemoQaPage selectCity(String state) {
        cityInput.click();
        stateCityWrapper.$(byText(state)).click();
        return this;
    }

    public DemoQaPage clickOnSubmit() {
        submitBtn.click();
        return this;
    }

    public DemoQaPage setBirthDate(String day, String month, String year) {
        dateField.click();
        dateMonthSelect.selectOption(month);
        $(".react-datepicker__year-select").selectOption(year);
        $(".react-datepicker__day--0" + day + ":not(.react-datepicker_day--outside-month)").click();
        return this;
    }


    public DemoQaPage checkResultsFormIsVisible() {
        resultsForm.shouldBe(visible);
        tableHeader.shouldHave(text(TABLE_TITLE));
        return this;
    }

    public DemoQaPage checkResultsFormIsHidden() {
        resultsForm.shouldBe(hidden);
        return this;
    }

    public DemoQaPage verifyResult(String parameter, String containsValue) {
        $(".table-responsive table").$(byText(parameter))
                .parent().shouldHave(text(containsValue));
        return this;
    }

    public DemoQaPage checkFieldsIsMarkedAsWrong(){
        $(".form-control:invalid[placeholder='First Name']").shouldBe(appear);
        $(".form-control:invalid[placeholder='Last Name']").shouldBe(appear);
        $(".form-control:invalid[placeholder='Mobile Number']").shouldBe(appear);
        return this;
    }

    public DemoQaPage checkEmailIsMarkedAsWrong(){
        $(".form-control:invalid[placeholder='name@example.com']").shouldBe(appear);
        return this;
    }

}
