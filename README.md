# (French) Dictionary !

Use your own personalized version dictionary with ease.
Store English words, French words, Spanish words, or whatever you like—even important information such as a namemapped to a phone number. 

Valid Commands include:

1. lookup (look up definition(s) in your dictionary with a word itself)
2. remove (remove a word and all its definitions from the dictionary)
3. removedef (remove a definition from a specific word in the dictionary)
4. kill (to safely save and quit the program)
5. clear or reset (to remove all entries in your dictionary)
6. show (show all entries in the dictionary)
7. count (count all entries in your dictionary)
8. mismatch (fun command)
9. seriously (fun but dangerous command)
Also,

To add a word, simply type the word you'd like to define followed by a colon and the the word's definition. (e.g. pomme (f) : apple ... Justin : 000-000-0000). 

An alternative to explicitly typing 'lookup' for the `lookup` command is just typing the word or part of the word. (e.g. `Just` would yield the following

The definition(s) of "justin" are: [000-000-0000]

)

## Running The Program
* Download and install [Java](https://www.java.com/en/) on your machine if you don't already have it.
* Navigate to the folder where you cloned or downloaded this repository. 
* Compile the program in the command line with `javac frenchDict.java`
* Run the program with `java frenchDict` optionally passing in a dictionary that conforms to the formatting expectations of the program (e.g. `java frenchDict sampleDict.txt`). Take a look at sampleDict.txt if you're interested!

It is important that each definition is separated by a newline in whatever file you in as a command line argument. The program automatically saves and loads your most recent dictionary if no command line argument is provided.

Compile and run the program and check out the sample dictionary that the program loads. 
Enjoy! 

## Screenshots
![Screenshot1](https://github.com/nthimothe/FrDictionary/blob/master/Screenshots/exampleUsage.png)

Note the usage of the `show`, `count`, `lookup`, and `kill` commands!
The next time you run the program, your old dictionary will automatically load!

![Screenshot2](https://github.com/nthimothe/FrDictionary/blob/master/Screenshots/automaticLoad.png)


## Notes

This program is named frDictionary.java because I made it to help me with my vocabulary for French class. It should work with any language provided you can input the characters with your keyboard.  For hints on how to do this for French, check out MacOS French Keyboard Shortcuts.png.

