"# contract-driven-development"
Note, there are two main branches...
before-taxId and with-taxId
Before Tax ID has the endpoints mocked with Mockito

After TaxId has the endpoints tested with stub runner, and all of the provider requests, implementations, and contracts including the taxId

Note: The before branch contains all of the bookmarks, as commented with 
// Bookmark 1 thru 9, and 0

Show the PPT slide 1, talk about how we all swallowed the Kool-aid on microservices, it's great for polyglot teams, great for scaling individual services according to load.
Slide 2 Challenges of Microservices
Slide 3-6 Wiremock... but who is responsible for changes

Somewhere after slide 2 and before slide 7, kick off the producer and consumer, and show in Postman the buy-sell and request-credit-line endpoints.

Bookmark 1 is the provider adding the taxId
Bookmark 2 is the corresponding provider test

Change bookmark 1 and 2 provider to add the taxId, and show how the provider tests pass

Then show how the consumer tests pass, even though they shouldn't, since their mockito endpoints are still mocked

Next introduce the groovy contracts, and show how to fix those (Bookmark 2 and 4)

Generate the provider stubs using
mvn clean install -DskipTests

Now change the consumer tests to use a real rest template, and show how the tests fail because of connection rejected

Add the stub runner annotation, show how the consumer tests now fail, beause the endpoint changed. Now we're cooking!

Now change the consumer implementation to pass in the taxId

Run mvn clean install on the provider
Run mvn clean install on the consumer

Show how everything passes

Switch back to the PPT and finish the explanation

> suggestion: check out the master branch (contains completed code, with taxId added, tests corrected, mockito removed, and stub runner added.)
Then diff with the before-taxId branch, and copy over all of the diffs  
 Now, IntelliJ Git support will show markers for all of the changes. Put the appropriate bookmarks there (using eg Ctrl-Alt-1)
 Then just do Ctrl-1, Ctrl-2 to navigate to the bookmarks, then roll back the change at that bookmark
 




