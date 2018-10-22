#Sentences from words via API

This application implements the suggested endpoints for the homework.
For the persistence an instance of MongoDB Atlas was used and was 
configured to be accessed from any machine.

##Usage
###Words endpoint
Allows the insertion and query of words, those will be stored in 
the _words_ collection in DB.

http://localhost:8080/words/

All the endpoints suggested in the task description were implemented.

A word could be created multiple times as long as its category is different.
Categories different to NOUN/VERB/ADJECTIVE will not be accepted.

###Sentences endpoint
Allows the generation and query of sentences, those will be stored in 
the _sentences_ collection in DB.

http://localhost:8080/sentences/

A sentence is generated randomly with one from eac category, if there are
no words available in the _words_ collection, no sentence will be generated.
If a sentence is generated multiple times it will increase its _generation_
value by 1, this value can be consulted in a dedicated endpoint:

http://localhost:8080/sentences/{sentence_id}/generation.

Everytime a sentence is consulted through the API its showCounter property
will be increased by 1.

All the endpoints suggested in the task description were implemented.