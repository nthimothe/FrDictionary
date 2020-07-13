//(c) 2019 Nathan Thimothe
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Vector;
import java.io.*;
import java.util.Random;
import java.util.InputMismatchException;


class frenchDict{
    /* Instance Variable */ 
    Map<String,Stack<String>> map;
    static final String OPEN_BOLD = "\033[1m";
    static final String CLOSE_BOLD = "\033[0m";
    /* Constructor */
    public frenchDict(){
	map = new TreeMap<>();
    }

    /* Methods */
    public void decide(String pair){
	//get rid of white space
	pair = pair.trim();
	String word, def;
	//split string into array
	//Key is what's before the : and the definition is everything after
	String[] keyValue = pair.split(":");
	/*
	 *Precaution that I noticed I should take after running the program:
	 *if you ran the program with "::" as input it would crash b/c
	 *the array was empty and i was trying to access first element
	 *Essentially the array is not empty then I can access my K
	*/
	//if there is a first value in the array, then it's the word, if not there is no word
	if (keyValue.length != 0)
	    word = keyValue[0];
	else 
	    word = "";
	//if we have a second thing in the array, then it's the definition, else there is none
	if (keyValue.length > 1)
	    def = keyValue[1];
	else
	    def = "";
	word = word.toLowerCase();	
	switch (word)
	    {
	    case "kill":
		save(map);		
		System.exit(0);
		break;
	    case "show":
		showMap(map);
		break;
	    case "remove":
		removeWord();
		break;
	    case "clear":
	    case "reset":
		reset();
		break;
	    case "count":
		count();
		break;
	    case "mismatch":
		System.out.println("\nGotchu \uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24");
		showMap(mismatch());
		break;
	    case "lookup":
		promptSearch("");
		break;
	    case "removedef":
		removeDef();
		break;
	    case "seriously":
		wickedMismatch();
		break;
	    case "":
		//do nothing
		break;
	    default: 
		boolean isAdded = addToDict(word,def); 
		if (!isAdded)
		    promptSearch(word);
		//		break;
	    }
    }

    //saves our map right before we end our program
    private void save(Map<String,Stack<String>> saving){
	String fileName = "_private.txt";
	//try,catch to check for successful completions
	try{
	    FileWriter fileWriter = new FileWriter(fileName); 
	    //must wrap FileWriter in a BufferedWriter
	    BufferedWriter bufferWrite = new BufferedWriter(fileWriter);
	    String eachLine;
	    //entrySet is the Set of Entries of K,V
	    for (Map.Entry<String,Stack<String>> entry : saving.entrySet()){
		String current = entry.getValue().toString();
		//we need to remove the brackets around the elements
		//of our stack, 1 all the way up to + including current.length-2, so that when we 
		// don't have to do that work when we read it!
		//we need that colon for when we read it back!
		eachLine = entry.getKey() + ":" + current.substring(1, current.length()-1) + "\n";
		bufferWrite.write(eachLine);
	    }
	    //always close our files
	    bufferWrite.close();
	} catch (IOException e){
	    //helps the programmer to understand where the actual problem occurred
	    //it helps to trace the exception. cannot print void types! : print(e...) X
	    e.printStackTrace();
	}
    }

    //fileName is our parameter just in case we have a dictionary we want to load    
    private void load(String fileName){
	//I don't want to use Scanner because I dont want to pass the file in 
	// as input to the program. I need it to automatically load
	if (fileName == null)
	    fileName = "_private.txt";
	String each;
	try{
	    //we want to read _private.txt
	    FileReader fileRead = new FileReader(fileName);
	    BufferedReader bufferRead = new BufferedReader(fileRead);
	    each= bufferRead.readLine();
	    //while the program still has lines
	    while (each != null){
		//decide what to do with each line
		//it should only contain definitions... -> addToDict(word,def)
		decide(each);
		each = bufferRead.readLine();
	    }
	    System.out.println("This is your current dictionary: ");
	    showMap(map);
	    bufferRead.close();
	} catch (FileNotFoundException fnfe){
	} catch(IOException ioe){
	    //helps the programmer to understand where the actual problem occurred
	    ioe.printStackTrace();
	}
    }
    
    public boolean addToDict(String word, String def){
	//the white spaces make our formatting look weird and may be messing up our remove method
	word = word.trim();
	def = def.trim();
	//if our definition that we defined in our decide method is not empty then we can add to dict
	if (!(def.isEmpty())) {
	    String[] definitionArray = def.split(",");
	    //if it contains the key, the update it's definition by
	    //1) getting its definitions, 2) adding another one, 
	    //3) updating the map
	    if (map.containsKey(word)){
		//if our stack contains our definition, then the K,V pair of word, def exists already
		Stack<String> definition = map.get(word);
		//a stack inherits methods from the vector class so let's use
		// vector's methods
		if (definition.contains(def)){
		    System.out.println("That definition already exists!");
		    return false;
		}
		else{
		    for (String eachDef : definitionArray){
			definition.push(eachDef.trim());
		    }
		    map.put(word,definition);
		    return true;
		}
	    }
	    // if it does not contain the key, then 1) create a stack 2) add
	    // the definition of the word to the stack 3) put that K,V pair in
	    //map
	    else if (!(map.containsKey(word))){
		Stack<String> stack = new Stack<>();
		//push the definition(s) onto the stack
		for (String eachDef : definitionArray){
		    stack.push(eachDef.trim());
		}
		map.put(word,stack);
		return true;
	    }
	}
	return false;
    }
    public void removeWord(){
	Scanner s = new Scanner(System.in);
	System.out.println(OPEN_BOLD + "Which word would you like to remove?" + CLOSE_BOLD);
	String potential = s.nextLine();
	//this method will remove a word and all its definitions IF it's
	//present
	Vector<String> removed = searching(potential);
	// if my search yields one result, remove that key
	int vecSize = printKeys(removed);
	if (vecSize == 1){
	    String removing = removed.get(0);
	    System.out.println(OPEN_BOLD + "Removing " + removing + "..." + CLOSE_BOLD);
	    map.remove(removing);
	}
	else if (vecSize > 1) {
	    //how will i make sure that this runs the first time? while loop
	    int removeInd = 0;
	    int count = 0;
	    do {
		try{
		    //two different prompts so that the longer message is only displayed once
		    if (count == 0)
		    System.out.println(OPEN_BOLD + "Please select the number that corresponds to the word you would like to remove." + CLOSE_BOLD);
		   else
			System.out.println(OPEN_BOLD + "Please enter a valid number." + CLOSE_BOLD);
		    removeInd =  s.nextInt(); // what if there is an InputMM exception? I need a try,catch
		    count++;
		} catch (InputMismatchException e){
		    //this clears out the scanner?
		    s.next();
		}
	    } while (removeInd > removed.size() || removeInd <= 0);
	    //find the word in the vector based off the user-provided index and remove it from map
	    String beingRemoved = removed.get(removeInd-1);
	    System.out.println(OPEN_BOLD + "Removing " + beingRemoved + "..." + CLOSE_BOLD);
	    map.remove(beingRemoved);
	}
	else System.out.println(OPEN_BOLD + "**** Key: " + "\"" + potential +  "\"" + " does not exist." + CLOSE_BOLD);
    }

    //clear the map
    public void reset(){
	if (!(map.isEmpty())){
	    Scanner s = new Scanner(System.in);
	    System.out.print(OPEN_BOLD + "Are you sure? " + CLOSE_BOLD);
	    String response = s.nextLine();
	    response = response.toLowerCase();
	    if (response.contains("yes") || response.equals("y")){
		map.clear();
	    }
	}
    }
    //prompt the user to remove a specific definition of whichever word (w/o removing the whole word)
    //the definition that we are removing is what we return
    public String removeDef(){
	Scanner s = new Scanner(System.in);
	String removed = "";
	System.out.println(OPEN_BOLD + "First, specify the word in the dictionary." + CLOSE_BOLD);
	String word = s.nextLine().trim();
	Vector<String> presentKeys = searching(word);
	Stack<String> wordDefs = new Stack<>();
	String presentKey = "";
	int vecSize = printKeys(presentKeys);
	//if the Vector has one element, that is the one word we found
	if (vecSize == 1){
	    //the word is the 0th element of Vector, also make sure to get its definition
	    presentKey = presentKeys.get(0);
	    wordDefs = map.get(presentKey);
	}
	//if it has more than one word, we need to narrow it down
	else if (vecSize > 1){
	    //let's do do-while loop instead of setting wordNum as Integer.MAX_VALUE
	    int wordNum = 0 ;
	    int count = 0;
	    do {
		try {
		    if (count == 0)
			System.out.println(OPEN_BOLD + "Please enter the number that corresponds to the word you have in mind." + CLOSE_BOLD);
		    else 		    
			System.out.println(OPEN_BOLD + "Please enter a valid number." + CLOSE_BOLD);
		    wordNum = s.nextInt();
		    count++;
		} catch (InputMismatchException e){
		    s.next(); 
		}
	    } while (wordNum > vecSize || wordNum <= 0 );
	    //once we are outside of the while loop, we have found an acceptable key
	    presentKey = presentKeys.get(wordNum-1);
	    wordDefs = map.get(presentKey);
	}
	
	//if the Vector is empty, aka vecSize == 0
	else{
	    System.out.println(OPEN_BOLD + "Sorry, I couldn't find that word." + CLOSE_BOLD);
	    return removed; 	    //which would be empty b/c we couldn't find the word
	}

	//once the search has been narrowed down to 1 element, print the definitions for that element
	System.out.println(OPEN_BOLD + "The definition(s) of " + "\"" + presentKey + "\" are: " + CLOSE_BOLD);
	for (int i = 0; i < wordDefs.size(); i++){
	    System.out.println("\t" + (i+1) + ") " + wordDefs.get(i));
	}

	//if there is only one definition for a word then remove that definition (the entire K,V pair)
	if (wordDefs.size() == 1) {
	    System.out.println(OPEN_BOLD + "Removing " + presentKey + " from dictionary..." + CLOSE_BOLD);
	    removed = map.remove(presentKey).toString();
	    return removed;
	}
	
	//if there is more than one definition for a word then the user must choose which definition to remove
	else if (wordDefs.size() > 1){
	    int numRev = 0;
	    int count = 0;
	    //prompt user to enter a valid number
	    //if the number the user enters is greater than my list, prompt again
	    do{
		try{
		    if (count == 0)
			System.out.println(OPEN_BOLD + "Which definition would you like to remove?" + CLOSE_BOLD);
		    else 
			System.out.println(OPEN_BOLD + "Please enter a valid number." + CLOSE_BOLD);
		    numRev = s.nextInt();
		    count++;
		} //if the user does not enter an integer, prompt again
		catch (InputMismatchException exception){
		    s.next();
		}
	    } while  (numRev > wordDefs.size() || numRev <= 0); 
	    //user entered a valid value, so the K,V from map, and put it back in with one less element
	    //on stack
	    String definition = wordDefs.remove(numRev-1);
	    System.out.println(OPEN_BOLD + "Removing \"" + definition + "\" from \"" + presentKey + "\"..." + CLOSE_BOLD);
	    map.remove(presentKey);
	    map.put(presentKey,wordDefs);
	}
	return removed;
    }
    public int printKeys(Vector<String> vector){
	int count = vector.size();
	//nested ternary !
	String statement = count == 1 ? "I found " + count + " result: " : (count == 0 ? "I found " + count + " results." : "I found " + count + " results: ");
	System.out.println(OPEN_BOLD + statement + CLOSE_BOLD);
	for (int i = 0; i < count ; i++){
	    System.out.println("\t" + (i+1) + ") " + vector.get(i));
	}
	return count;
    }

    //prints the map out neatly
    public void showMap(Map<String,Stack<String>> specMap){
	System.out.println("---------------------------------------------------------------------------------------------------------");
	//for each entry in the map, print the key,value pair
	for (Map.Entry<String,Stack<String>> entry : specMap.entrySet()){
	    
	    System.out.println("|  " + entry.getKey() + " : " + entry.getValue());
	}
	System.out.println("---------------------------------------------------------------------------------------------------------");
    }
    
    public int count(){
	int size = map.size();
	System.out.println(OPEN_BOLD + "There are " + size + " elements in your dictionary." + CLOSE_BOLD);
	return size;
    }

    // prompt search of a key in the case the user provides `lookup` command, else use the key given
    public void promptSearch(String key){
	String keep = key;
	//perform binary search on the sorted keys to find the definition(s) that I'm searching for
	// for right now, we will do O(n) lookup
	Stack<String> definitions;
	if (keep.isEmpty()){
	    Scanner s = new Scanner(System.in);
	    System.out.println(OPEN_BOLD + "What word are you looking for?" + CLOSE_BOLD);
	    key = s.nextLine().trim();
	}
	Vector<String> actualKeys = searching(key);
	//if the Vector is not empty, then we have keys with valid definition(s), find them in the map
	if (!actualKeys.isEmpty()){
	    for (String word : actualKeys){
		definitions = map.get(word);
		System.out.println("The definition(s) of " + OPEN_BOLD + "\"" + word +  "\"" + CLOSE_BOLD + " are: " + definitions.toString());
	    }
	}
	else {
	    //if I explicitly prompted with lookup, tell tell me that you could not find the word
	    if (keep.isEmpty())
		System.out.println(OPEN_BOLD + "I could not find " + "\"" + key + "\" in your dictionary." + CLOSE_BOLD);	    
	}
    }
    /*
     *This method exists to convert? if the key is present without its gender then my key I typed in 
     *gets corrected to the actual key. if the presentKey is empty at the end of the func, we didn't
     *find it.
     */
    private Vector<String> searching(String key){
	Vector<String> presentKeys = new Vector<>();
	//O(n), see if the keys of the map contain the key that the user is searching
	for (Map.Entry<String,Stack<String>> entry : map.entrySet()){
	    String currentKey = entry.getKey();
	    if (currentKey.contains(key)){
		presentKeys.add(currentKey);
	    }
	}
	return presentKeys;
    }
		    
		    
    public Map<String,Stack<String>> mismatch(){
	/* IDEA: Create a new map that will have all the values of the old map
	 * but in a weird order. We will use a LinkedHash Map.
	 * Put the keys of the original map in a Vector and randomly index into it
	 * adding that key and its value to the LinkedHashMap, and removing it 
	 * from the Vector. We know we're done when the Vector is empty.
	 */
	//new map to store the values. LINKEDHASHMAP will keep the order that 
	// keeps the keys in the order they are inserted!
	Map<String,Stack<String>> newMap = new LinkedHashMap<>();
	//let's put all our keys into a vector so 
	//we can randomly index into the vector, removing values as we go!
	Vector<String> strings = new Vector<>();
	for (Map.Entry<String,Stack<String>> entry : map.entrySet()){
	    strings.add(entry.getKey());
	}
	//Random exclusive in Java and Arduino!
	Random obj = new Random();
	int ranNum;
	//while our Vector isn't empty: let's do this!
	while (!(strings.isEmpty())){
	    //the size of the vector will decrease on every pass of the while loop
	    ranNum = obj.nextInt(strings.size());
	    String key = strings.get(ranNum);
	    //get the K,V from the map and add them
	    newMap.put(key,map.get(key));
	    strings.remove(ranNum);
	} 
	return newMap;
    }
    public void wickedMismatch(){
	/* IDEA: Create a new map that will have all the values of the old map
	 * but with all words mismatched. We will use a LinkedHash Map.
	 * *****
	 */
	Scanner s = new Scanner(System.in);
	Random obj = new Random();
	System.out.print("\u26A0\u26A0\u26A0 Are you sure? ");
	String answer = s.nextLine();
	if (answer.equals("y") || answer.contains("yes")){
	    System.out.println("You asked! \uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C");
	    int ranNum = obj.nextInt(1000000);
	    for (int i = 0; i < ranNum ; i++){
		Map<String,Stack<String>> wacky = new LinkedHashMap<>();
		//new map from mismatch() every time!
		Map<String,Stack<String>> mismatched = mismatch();		
		for (Map.Entry<String,Stack<String>> each : mismatched.entrySet()){
		    String actualKey = each.getKey();
		    char[] characters = actualKey.toCharArray();
		    Vector<Character> easier = arrayToVec(characters);
		    //we rearranged the key
		    String wackyString = reorderChar(easier);
		    //rearrange each value
		    Stack<String> actualStack = each.getValue();
		    Stack<String> wackyDefs = new Stack<>();
		    for (String eachDef : actualStack){
			Vector<Character> defVector = arrayToVec(eachDef.toCharArray());
			wackyDefs.push(reorderChar(defVector));
		    }
		    wacky.put(wackyString,wackyDefs);
		}
		int random = obj.nextInt(10000);
		if (i == 300){
		    System.out.println("Wait, yeah that was actually very annoying...So go ahead and type your commands as you would...");
		    try{
			Thread.sleep(5000);
		    } catch(InterruptedException e){
		    }
		    System.out.println("There's no way I could let it be that easy.\uD83D\uDC0D");
		    try{
			Thread.sleep(3000);
		    } catch(InterruptedException e){
		    }
		}
		if (i == random){
		    System.out.println("No, like actually, I'm sorry. This was rude, and it'll be over soon.\uD83D\uDE01");
		    try{
			Thread.sleep(5000);
		    } catch(InterruptedException e){
		    }
		}
		showMap(wacky);
	    }
	}
    }
    //randomly rearrange the characters in a string
    private String reorderChar(Vector<Character> vector){
	Random rand = new Random();
	int ranNum;
	String wackyString = "";
	while (!vector.isEmpty()){
	    //pick a random number between 0 and the vector's size
	    ranNum = rand.nextInt(vector.size());
	    //get that randomly selected character out of the vector 
	    wackyString += vector.get(ranNum);
	    //we can remove an index or an object | remove that character from the vector 
	    vector.remove(ranNum);
	    //repeat this process until the vector is empty
	}
	return wackyString;
    }
    //array -> vector
    private Vector<Character> arrayToVec(char[] characters){
	Vector<Character> result = new Vector<>();
	for (char charac : characters){
	    result.add(charac);
	}
	return result;
    }
    //java's first argument is not the filename like in python and c!
    public static void main(String[] args){
	frenchDict frenchDict = new frenchDict();
	System.out.println(frenchDict.OPEN_BOLD + "Welcome to Your Dictionary!\n" + frenchDict.CLOSE_BOLD);
	//if we get an array out of bounds exception, then let's just load our default 
	//file : _private.txt 
	try{
	    frenchDict.load(args[0]);
	}catch (ArrayIndexOutOfBoundsException exception){
	    frenchDict.load(null);
	}
	System.out.println("Please enter a word and its definition followed by a colon!\n");	
	String pair;
	Scanner s = new Scanner(System.in);
	while (s.hasNext()){
	    pair = s.nextLine();
	    //if (pair.isEmpty()) continue;
	    frenchDict.decide(pair);
	}
    }
}