 Test Cases for kribado Project
=============================

This project includes a variety of test cases to ensure the functionality of the application across multiple pages and scenarios. Below is a breakdown of the key test cases and their purpose:

**Login Tests**
---------------

### `testEmptyFieldsLogin()` 

*   **Description**: Verifies that the login page prompts an error when both the username and password fields are left empty.
    
*   **Expected Outcome**: The appropriate error message "Please fill out this field." should appear for both the username and password fields.
    

### `testInvalidLogin()`

*   **Description**: Tests the scenario where invalid credentials (username and password) are entered.
    
*   **Expected Outcome**: An error message "Login Failed" should be displayed, indicating the login attempt was unsuccessful.
    

**End-to-End Flow**
-------------------

### `testEndToEndFlow()`

*   **Description**: Performs a comprehensive end-to-end test that simulates a user's entire journey through the application:
    
        
*   **Expected Outcome**: The flow should run seamlessly without errors, ensuring that all pages and processes function as expected.
    

**Add Patient Page**
--------------------

### `testEmptyFieldsAddPatient()`

*   **Description**: Verifies that validation messages are displayed when attempting to add a patient with empty fields (name, age, or gender).
    
*   **Expected Outcome**: The application should prompt the user to fill out the missing fields with appropriate error messages.
    

### `testAgeFieldValidation()`

*   **Description**: Ensures that the age field works as expected with:
    
    *   Age less than 1 (should show validation error).
        
    *   Age greater than 130 (should show validation error).
        
    *   Non-numeric input (should prompt an error).
        
    *   Valid age inputs (should submit the form successfully).
        
*   **Expected Outcome**: The application should correctly validate age inputs according to specified rules and boundaries, providing the right error messages or allowing successful form submission.

Health Questionnaire Page 
--------------------

### `testHealthQuestionnaireValidation()`

*   **Description**: Verifies that the Health Questionnaire page functions correctly when all questions (Waist Circumference, Physical Activity, and Family History of Diabetes) are answered and submitted.

*   **Expected Outcome**: The form should be successfully submitted without any validation errors, and no error message should be displayed.
