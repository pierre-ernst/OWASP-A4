# OWASP Top 10 - A4

Sample code to demonstrate the [Top 10-2017 A4-XML External Entities (XXE)](https://www.owasp.org/index.php/Top_10-2017_A4-XML_External_Entities_(XXE)) vulnerability class.

The org.owasp.ottawa.topten2017_A4.impl package contains several implementation of XML parsers and each of these implementation contains a mitigate() method that configures the parser in a way to prevent XXE attacks.

Unit tests are implemented to make sure XXE attacks are prevented for each parser implementation.

[Link to presentation](https://goo.gl/oucYGj).


## Usage:
```bash
$ mvn test

Running org.owasp.ottawa.topten2017_A4.test.ExternalEntityBypassTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.615 sec
Running org.owasp.ottawa.topten2017_A4.test.ParameterTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0 sec
Running org.owasp.ottawa.topten2017_A4.test.ParserTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.417 sec
Running org.owasp.ottawa.topten2017_A4.test.SecureProcessingBypassTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.975 sec

Tests run: 7, Failures: 0, Errors: 0, Skipped: 0

```
