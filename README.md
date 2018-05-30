# RegularExpressionParser
This regular expression parser will supply strings that match a certain regex up to a defined max length.  The program will ask for the regex, then the max length.  The operations are as follows in order of precedence.  Note from developer: Program may fail on sufficiently long and complex grammars due to limited memory.

( ) : parenthesis
* : Kleeney closure (0 or more)
No space between symbols : concatenation of strings
|: alternation, logical disjunction

Build order was default, but I believe it should be in this order.
Parser.java
State.java
NFA.java
GraphTraverser.java
Test.java (if enabled in source code)
RegularExpParser.java
