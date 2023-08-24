Feature: Automate Classic Website
  Scenario Outline: Test Things <example>
    Given You're on "<URL>" and get its Title
    When Click On Document
    Then Open every "<example>" to see if loading correctly
    And Store the Documents
    Examples:
      | URL | example |
      | https://www.pimco.com/en-us/investments/etf/25-year-zero-coupon-us-treasury-index-exchange-traded-fund?r=Financial%20Intermediary&l=United%20States&s=True | Statement of Additional Information,Third Quarter Report |
      | https://www.pimco.com/en-us/investments/etf/1-5-year-us-tips-index-exchange-traded-fund?r=Financial%20Intermediary&l=United%20States&s=True | B3 Exchange Performance |
#      | https://www.pimco.com/en-us/investments/etf/25-year-zero-coupon-us-treasury-index-exchange-traded-fund?r=Financial%20Intermediary&l=United%20States&s=True | Statement of Additional Information |
#      | https://www.pimco.com/en-us/investments/etf/1-5-year-us-tips-index-exchange-traded-fund?r=Financial%20Intermediary&l=United%20States&s=True | Annual Report |
#      | https://www.pimco.com/en-us/investments/etf/25-year-zero-coupon-us-treasury-index-exchange-traded-fund?r=Financial%20Intermediary&l=United%20States&s=True | Semi Annual Report |
#      | https://www.pimco.com/en-us/investments/etf/1-5-year-us-tips-index-exchange-traded-fund?r=Financial%20Intermediary&l=United%20States&s=True | Annual Report,Prospectus,Statement of Additional Information,Semi Annual Report,Summary Prospectus,First Quarter Report,Third Quarter Report,Creation Unit Aggregation,Fact Sheet,B3 Exchange Performance |


