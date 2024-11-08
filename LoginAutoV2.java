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
    public void testEmptyFieldsLogin() {
        login("", "");
        verifyEmptyFieldValidation(By.xpath("//*[@id='username']"), "Please fill out this field.");
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
//        addPatient("okayyy","10","");
////        driver.findElement(By.cssSelector("#addPatientForm > button")).click();
//        Thread.sleep(2000);
//        verifyEmptyFieldValidation(By.xpath("//*[@id=\"addPatientForm\"]/div[3]/div[1]/label"), "Please select one of these options.");
    }
}
