package pixika.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginAutoV2 {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://worlddiabetesday.kribado.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeMethod
    public void navigateToLoginPage() {
        driver.get("https://worlddiabetesday.kribado.com/");
        driver.manage().deleteAllCookies(); // Clear cookies for session isolation
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    // Login helper
    private void login(String username, String password) {
        driver.findElement(By.xpath("/html/body/div/form/div[1]/input")).sendKeys(username);
        driver.findElement(By.xpath("/html/body/div/form/div[2]/input")).sendKeys(password);
        driver.findElement(By.cssSelector("#loginButton")).click();
    }

    // Select doctor helper
    private void selectDoctor(String doctorName) {
        WebElement doctorDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/form/div/select")));
        doctorDropdown.click();
        WebElement doctorOption = driver.findElement(By.xpath("//option[contains(text(), '" + doctorName + "')]"));
        doctorOption.click();
        driver.findElement(By.xpath("/html/body/div/form/button[1]")).click();
    }

    // Add patient helper
    private void addPatient(String name, String age, String gender) {
        WebElement patientName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#patientName")));
        patientName.sendKeys(name);

        WebElement patientAge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#patientAge")));
        patientAge.sendKeys(age);

        WebElement genderMale = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#addPatientForm > div:nth-child(3) > div:nth-child(2) > label")));
        genderMale.click();

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#addPatientForm > button")));
        submitButton.click();
    }

    // Answer questions helper
    private void answerQuestions() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"waistOptionsMale\"]/div[1]/label"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"healthForm\"]/div[2]/div[1]/label"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"healthForm\"]/div[3]/div[2]/label"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#submit-button"))).click();
    }

    // Validate and download score helper
    private void validateAndDownloadScore() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"captureBtn\"]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div[6]/button[1]"))).click();
        WebElement scoreElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"capture\"]/h2/b")));
        Assert.assertTrue(scoreElement.isDisplayed(), "Score is not displayed!");
    }

    // Take another test helper
    private void takeAnotherTest() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"takeAnotherTestBtn\"]"))).click();
        WebElement addPatientPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/h2")));
        Assert.assertTrue(addPatientPage.isDisplayed(), "Add Patient page is not displayed after taking another test!");
    }

    // Empty fields validation helper
    private void verifyEmptyFieldValidation(By fieldLocator, String errorMessage) {
        try {
            WebElement field = driver.findElement(fieldLocator);
            boolean isFieldRequired = field.getAttribute("required") != null
                    || field.getAttribute("validationMessage").contains(errorMessage);
            Assert.assertTrue(isFieldRequired, "Expected field validation message not found!");
        } catch (Exception e) {
            Assert.fail("Validation message or required attribute not found for empty fields!");
        }
    }

    // Test cases

    @Test
    public void testEmptyFieldsLogin() throws InterruptedException{
        login("", "");
        verifyEmptyFieldValidation(By.xpath("//*[@id='username']"), "Please fill out this field.");
        Thread.sleep(1000);
        login(" cv001","");
        verifyEmptyFieldValidation(By.xpath("//*[@id='pass']"), "Please fill out this field.");
    }

    @Test
    public void testInvalidLogin() throws InterruptedException {
        login("invalidUser", "invalidPass");
        Thread.sleep(2000);
        String error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"swal2-title\"]"))).getText();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//button[normalize-space()='OK']")).click();
        Thread.sleep(2000);
        Assert.assertEquals(error, "Login Failed");
    }

    @Test
    public void testEmptyFieldsAddPatient() throws InterruptedException {


        login("cv001", "indigital");
        Thread.sleep(2000);


        selectDoctor("yogesh doc");
        Thread.sleep(2000);


        addPatient("","","");
        verifyEmptyFieldValidation(By.cssSelector("#patientName"), "Please fill out this field.");
        Thread.sleep(2000);

        addPatient("OK","","");
        verifyEmptyFieldValidation(By.cssSelector("#patientAge"), "Please fill out this field.");
        Thread.sleep(2000);

        driver.navigate().refresh();

        // Enter data for name and age but leave gender field unselected
        WebElement patientName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#patientName")));
        patientName.sendKeys("John Doe");

        WebElement patientAge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#patientAge")));
        patientAge.sendKeys("30");
        Thread.sleep(1000);

        driver.findElement(By.cssSelector("#addPatientForm > button")).click();
        Thread.sleep(2000);
        verifyEmptyFieldValidation(By.xpath("//*[@id=\"male\"]"), "Please select one of these options.");
        Thread.sleep(1000);
    }

    @Test
    public void testAgeFieldValidation() throws InterruptedException {
        Thread.sleep(1000);
        login("cv001", "indigital");
        Thread.sleep(1000);
        selectDoctor("yogesh doc");
        Thread.sleep(1000);
        // Test with age less than 1
        addPatient("Joh", "0", "Male");
        verifyEmptyFieldValidation(By.cssSelector("#patientAge"), "Value must be greater than or equal to 1");

        // Test with age greater than 130
        addPatient(" D", "131", "Male");
        verifyEmptyFieldValidation(By.cssSelector("#patientAge"), "Value must be less than or equal to 130");

        // Test with non-numeric input
        addPatient(" Vicky", "abc", "Male");
        verifyEmptyFieldValidation(By.cssSelector("#patientAge"), "Please enter a valid number");

        // Test with valid minimum age boundary
        addPatient("", "1", "Male");
        submitAndVerifyNoValidationError();  // Verify form submission without errors

        // Test with valid maximum age boundary
        addPatient("", "130", "Male");
        submitAndVerifyNoValidationError();  // Verify form submission without errors
    }

    // Helper method to submit the form and check for validation
    private void submitAndVerifyNoValidationError() {
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#addPatientForm > button")));
        submitButton.click();

        // Assuming validation message is displayed when there's an error
        boolean hasValidationError = !driver.findElements(By.cssSelector(".validation-message")).isEmpty();
        Assert.assertFalse(hasValidationError, "Form submission should be successful, but validation error is displayed!");
    }

    @Test
    public void testHealthQuestionnaireValidation() throws InterruptedException {
        login("cv001", "indigital");
        Thread.sleep(2000);
        selectDoctor("yogesh doc");
        Thread.sleep(2000);
        addPatient("John Doe", "30", "Male");
        Thread.sleep(2000);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"waistOptionsMale\"]/div[1]/label"))).click();
        Thread.sleep(1000);

        driver.findElement(By.cssSelector("#submit-button")).click();
        Thread.sleep(2000);

        // Verify that the error message appears because the third question was not answered
        String errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"swal2-title\"]"))).getText();
        Assert.assertEquals(errorMessage, "Error!", "Error message was not displayed as expected!");

        driver.findElement(By.xpath("//button[normalize-space()='OK']")).click();
    }

    @Test
    public void testEndToEndFlow() throws InterruptedException {
        login("cv001", "indigital");
        selectDoctor("yogesh doc");
        addPatient("John Doe", "30", "Male");
        answerQuestions();
        validateAndDownloadScore();
        takeAnotherTest();
    }


}