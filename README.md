#Financial Data Dashboard Test Framework

Application under test: Financial Data Dashboard (https://ui-automation-app.web.app/)

The framework contains one test comparing datasets in Table 1 and Table 2 of the dashboard

Use the following command to run the test in console:
`mvn test -DsuiteXmlFile=testng.xml`

Tools used:
- Java 11
- Maven
- TestNG

###Testing results:
- The app consistently has data discrepancies between the tables for Stock Price and Market Cap columns for every 100th record