"# contract-driven-development"
The goal is to change the provider contract, which originally accepts a date String in the form 2019-08-29, to the new state which accepts an date as seconds since epoch 

There are two main branches...
master and date-string
date-string is the starting state, with endpoints mocked with Mockito, no contracts, and dates as strings
master is the target state with dates as longs, and using contracts 

master has the endpoints tested with stub runner, and all of the provider requests, implementations, and contracts including the taxId

Note: The before branch contains all of the bookmarks, as commented with 
// Bookmark 1 thru 9, and 0

Show the PPT slide 1, talk about how we all swallowed the Kool-aid on microservices, it's great for polyglot teams, great for scaling individual services according to load.
Slide 2 Challenges of Microservices
Slide 3-6 Wiremock... but who is responsible for changes

After slide 6, kick off the producer and consumer, and show in Postman the buy-sell and request-credit-line endpoints.

1. Talk about app
2. Demo in Postman
3. Explain 2 modules - consumer and producer
4. Show consumer controller, rest template
5. Show consumer test class
6. Run consumer test class
7. Change provider request - BM1
8. Change provider test - BM2
9. Change provider CreditIncreaseResponse BM3
10. Change provider controller BM4 - 2 places
11. Run provider test - should pass!----- Back to slides
12. Run consumer test - passes, even though provider changed - BM5
13. Change mocks, delete mocked calls, add stub runner(But don't correct the call in the client tester)
14. Add Spring Cloud Contract verifier to provider pom - BM6
15. Show client pom stub runner
16. Show contracts directory structure
17. Fix contracts - BM7
18. Provider - mvn clean install -DskipTests to generate stubs
19. Show stub mappings
20. Run consumer tests - now they fail 
21. Fix consumer controller and request/response classes - BM8
22. fix consumer test - BM5
23. provider mvn clean install
24. show generated test source
25. Discuss BaseTestClass - BM9


Switch back to the PPT and finish the explanation of CDC as a collaborative TDD process

> suggestion: check out the master branch (contains completed code, with dates as longs, tests corrected, mockito removed, and stub runner added.)
Then diff with the date-string branch, and copy in all of the diffs (but don't commit that to git!!)
>.  
 Now, IntelliJ Git support will show markers for all of the changes. Put the appropriate bookmarks where indicated with the // bookmark comments. (using eg Ctrl-Alt-1)
 Then just do Ctrl-1, Ctrl-2 to navigate to the bookmarks, then roll back the change at that bookmark
 
 Ctrl-1 and 2 are for the provider code and provider test, 3 is for the provider controller, and 4 is for the contracts on the provider side, and the rest are on the consumer
 
> Tip - Once you copy in all of the changes from date-string branch, shelve the changes in IntelliJ, so that you can easily re-apply them


Calls:

localhost:8091/buy-sell
```json
{
	"clientId":1,
	"stock":{
		"symbol":"IBM",
		"exchange":"NYSE"
	},
	"shares":1000
}
```

localhost:8081/request-credit-increase
```json
{
	"clientId":1,
	"increaseAmount":10000,
	"currentCreditLine":10000,
	"date":"2019-09-04"
}
```
