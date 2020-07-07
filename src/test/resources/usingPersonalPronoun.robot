*** Test Cases ***

Test case without personal pronoun
    Given browser is opened to login page
    When user "demo" logs in with password "mode"
    Then welcome page should be open

Test case with all personal pronoun
    Given I am on login page
    When I put my usernam and password
    Then I should be on the welcome page

*** Keywords ***
Browser is opened to login page
    Log    Browser is opened to login page

User "${username}" logs in with password "${password}"
    Log    ${username}
    Log    ${password}

Welcome page should be open
    Log    Welcome page should be open

I am on login page
    Log    I am on login page

I put my usernam and password
    Log    I put my usernam and password

I should be on the welcome page
    Log    I should be on the welcome page