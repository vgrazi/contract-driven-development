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

Somewhere after slide 2 and before slide 7, kick off the producer and consumer, and show in Postman the buy-sell and request-credit-line endpoints.

Now switch to code.. show the structure of the project, how we have the consumer and producer modules, that represent different projects.

Bookmark 1 is the provider request, where we need to change the date format to long
Bookmark 2 is the corresponding provider test

Change bookmark 1 and 2 provider to use longs instead of date strings, and show how the provider tests pass

Then show how the consumer tests pass, even though they shouldn't, since their mockito endpoints are still mocked

Next add the spring contract dependencies to the provider pom, and introduce the groovy contracts, and show how to fix those (Bookmark 2 and 4). Discuss how those must be under the test/resources/contracts dir. 

Talk about how the client request is regex to match a range of request calls, and the client responses is hard coded to return a fixed response to the caller. The server is opposite - request is hard coded for test input, and response is regex for matching  

Generate the provider stubs using
mvn clean install -DskipTests

Now change the consumer tests to use a real rest template, and show how the tests fail because of connection rejected

Add the stub runner annotation, show how the consumer tests now fail, beause the endpoint changed. Now we're cooking!

Now change the consumer implementation to pass in the taxId

Run mvn clean install on the provider
Run mvn clean install on the consumer

Show how everything passes

Display the generated provider side test RestAssured code.

Switch back to the PPT and finish the explanation of CDC as a colaborative TDD process

> suggestion: check out the master branch (contains completed code, with dates as longs, tests corrected, mockito removed, and stub runner added.)
Then diff with the date-string branch, and copy in all of the diffs (but don't commit that to git!!)
>.  
 Now, IntelliJ Git support will show markers for all of the changes. Put the appropriate bookmarks where indicated with the // bookmark comments. (using eg Ctrl-Alt-1)
 Then just do Ctrl-1, Ctrl-2 to navigate to the bookmarks, then roll back the change at that bookmark
 
 Ctrl-1 and 2 are for the provider code and provider test, 3 is for the provider controller, and 4 is for the contracts on the provider side, and the rest are on the consumer
 




