#Scanning
it's a good engineering practice to separate the code that generates the errors from the code that reports them.

##tokens?
we take lexeme and bundle it together with that other data -> this is a token

lexical analysis :  scan through the list of characters and group them together into the smallest sequences that still
represent something

###lexeme
blobs of characters (var / language / = / "something" / ;)

only  the raw substrings of the source code


while process of grouping character sequences into lexemes we stumble upon some other useful information


###token includes...
1. Token type 
2. literal value
3. location information