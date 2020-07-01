*** Settings ***
Library           SeleniumLibrary

*** Test Cases ***
Run the test
    Keyword calling other keyword with parameters

*** Keywords ***
Keyword calling other keyword with parameters
    Follow the link  ${simple}
    Follow the link  ${complex}

Follow the link
    [Arguments]    ${locator}
    Click Link    ${locator}

*** Variables ***
${simple}  link_to_click
${complex}  #covid-form > div > div.react-grid-Container > div > div > div.react-grid-Header > div > div > div:nth-child(3) > div