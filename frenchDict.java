//(c) 2020 Nathan Thimothe
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

class frenchDict{
    /* Instance Variable */
    Map<String,ArrayList<String>> map;
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
	String[] keyValue = pair.split(":", 2);
	//if there is a first value in the array, then it's the word, if not there is no word
	word = keyValue[0];

	if (word.isEmpty()) return;

	//if we have a second thing in the array, then it's the definition, else there is none
	if (keyValue.length > 1) def = keyValue[1];
	else def = "";
	
	// everything will be stored in lowercase
	word = word.toLowerCase();
	def = def.toLowerCase();


	// if there is a word and no definition, then it is a command
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
	case "removedef":
	  removeDef();
	  break;
	case "clear":
	case "reset":
	    reset();
	    break;
	case "count":
	    count();
	    break;
	case "lookup":
	    promptSearch(null);
	    break;
	case "mismatch":
      System.out.println("\nGotchu \uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24\uD83D\uDE24");
      showMap(mismatch());
      break;
	case "seriously":
	    wickedMismatch();
	case "help":
	    help();
	    break;
	default:
	  boolean isAdded = addToDict(word,def);
	  if (!isAdded)
	      promptSearch(word);
	}
    }
    
    // DEFAULT COMMAND
    
    public boolean addToDict(String word, String def){
	//the white spaces make our formatting look weird and may be messing up our remove method
	word = word.trim();
	def = def.trim();
	//if our definition that we defined in our decide method is not empty then we can add to dict
	if (!(def.isEmpty())) {
	    String[] definitionArray = def.split(",");
	    //if it contains the key, the update its definition by
	    //1) getting its definitions, 2) adding another one,
	    //3) updating the map
	    if (map.containsKey(word)){
		//if our list contains our definition, then the K,V pair of word, def exists already
		ArrayList<String> definition = map.get(word);
		if (definition.contains(def)){
		    System.out.println("That definition already exists!");
		    return true;
		}
		else{
		    for (String eachDef : definitionArray){
			definition.add(eachDef.trim());
		    }
		    map.put(word,definition);
		    return true;
		}
	    }
	    // if it does not contain the key, then 1) create a list 2) add
	    // the definition of the word to the list 3) put that K,V pair in map
	    else if (!(map.containsKey(word))){
		ArrayList<String> list = new ArrayList<>();
		// add definition(s) to list
		for (String eachDef : definitionArray){
		    list.add(eachDef.trim());
		}
		map.put(word,list);
		return true;
	    }
	}
	return false;
    }
    
    // COMMAND : KILL
    
    //saves our map right before we end our program
    private void save(Map<String,ArrayList<String>> saving){
	String fileName = "_private.txt";
	//try,catch to check for successful completions
	try{
	    FileWriter fileWriter = new FileWriter(fileName);
	    //must wrap FileWriter in a BufferedWriter
	    BufferedWriter bufferWrite = new BufferedWriter(fileWriter);
	    String eachLine;
	    //entrySet is the Set of Entries of K,V
	    for (Map.Entry<String,ArrayList<String>> entry : saving.entrySet()){
		String current = entry.getValue().toString();
		// remove the brackets around the elements of our list, 1 all the way up to + including current.length-2
		// we need colon for when we read it back
		eachLine = entry.getKey() + ":" + current.substring(1, current.length()-1) + "\n";
		bufferWrite.write(eachLine);
	    }
	    bufferWrite.close();
	} catch (IOException e){
	    e.printStackTrace();
	}
    }
    
    // COMMAND: SHOW
    
    //prints the map out neatly
    public void showMap(Map<String,ArrayList<String>> specMap){
	System.out.println("---------------------------------------------------------------------------------------------------------");
	//for each entry in the map, print the key,value pair
	for (Map.Entry<String,ArrayList<String>> entry : specMap.entrySet()){
	    System.out.println("|  " + entry.getKey() + " : " + entry.getValue());
	}
	System.out.println("---------------------------------------------------------------------------------------------------------");
    }
    
    // COMMAND: REMOVE    
    public void removeWord(){
	Scanner s = new Scanner(System.in);
	System.out.println(OPEN_BOLD + "Which word would you like to remove?" + CLOSE_BOLD);
	String potential = s.nextLine();
	//this method will remove a word and all its definitions IF it's present
	ArrayList<String> removed = searching(potential);
	int listSize = printKeys(removed);
	// if search yields one result, remove that key
	if (listSize == 1){
	    String removing = removed.get(0);
	    System.out.println(OPEN_BOLD + "Removing " + removing + "..." + CLOSE_BOLD);
	    map.remove(removing);
	}
	else if (listSize > 1) {
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
    
    
    // COMMAND : REMOVEDEF
    
    //prompt the user to remove a specific definition of whichever word (w/o removing the whole word)
    //the definition that we are removing is what we return
    public String removeDef(){
	Scanner s = new Scanner(System.in);
	String removed = "";
	// prompt
	System.out.println(OPEN_BOLD + "First, specify the word in the dictionary." + CLOSE_BOLD);
	String word = s.nextLine().trim();
	// search
	ArrayList<String> presentKeys = searching(word);
	ArrayList<String> wordDefs = null;
	// present findings
	String presentKey = "";
	int listSize = printKeys(presentKeys);
	if (listSize == 0){
	    System.out.println(OPEN_BOLD + "Sorry, I couldn't find that word." + CLOSE_BOLD);
	    return removed; 	    //which would be empty b/c we couldn't find the word
	}
	//if the list has one element, that is the one word we found
	else if (listSize == 1){
	    //the word is the 0th element of list, also make sure to get its definition
	    presentKey = presentKeys.get(0);
	    wordDefs = map.get(presentKey);
	}
	//if it has more than one word, we need to narrow it down!
	else{
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
	    } while (wordNum > listSize || wordNum <= 0 );
	    //once we are outside of the while loop, we have found an acceptable key
	    presentKey = presentKeys.get(wordNum-1);
	    wordDefs = map.get(presentKey);
	}
	
	// once the search has been narrowed down to 1 element, print the definitions for each element
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
	else {
	    int numRev = 0;
	    int count = 0;
	    //prompt user to enter a valid number
	    //if the number the user enters is greater than my list, prompt again
	    do{
		try{
		    if (count == 0)  System.out.println(OPEN_BOLD + "Which definition would you like to remove?" + CLOSE_BOLD);         
		    else System.out.println(OPEN_BOLD + "Please enter a valid number." + CLOSE_BOLD);
		    numRev = s.nextInt();
		    count++;
		} catch (InputMismatchException exception){
		    s.next();
		}
		//if the user does not enter an integer, prompt again
	    } while  (numRev > wordDefs.size() || numRev <= 0);
	    //user entered a valid value, so the K,V from map, and put it back in with one less element
	    String definition = wordDefs.remove(numRev-1);
	    System.out.println(OPEN_BOLD + "Removing \"" + definition + "\" from \"" + presentKey + "\"..." + CLOSE_BOLD);
	    map.remove(presentKey);
	    map.put(presentKey,wordDefs);
	}
	return removed;
    }
    
    // COMMAND : RESET
    
    //clear the map
    public void reset(){
	if (!(map.isEmpty())){
	    Scanner s = new Scanner(System.in);
	    System.out.print(OPEN_BOLD + "Are you sure? " + CLOSE_BOLD);
	    String response = s.nextLine();
	    response = response.toLowerCase();
	    if (response.contains("yes") || response.equals("y")) map.clear();
	}
    }
    
    // COMMAND : COUNT
    public int count(){
	int size = map.size();
	System.out.println(OPEN_BOLD + "There are " + size + " elements in your dictionary." + CLOSE_BOLD);
	return size;
    }
    
    // COMMAND : LOOKUP
    // prompt search of a key in the case the user provides `lookup` command, else use the key given
    public void promptSearch(String key){
	String keep = key;
	// for right now, we will do O(n) lookup
	ArrayList<String> definitions;
	// if the user doesn't provide a word, then prompt them
	if (keep == null){
	    Scanner s = new Scanner(System.in);
	    System.out.println(OPEN_BOLD + "What word are you looking for?" + CLOSE_BOLD);
	    key = s.nextLine().trim();
	}
	ArrayList<String> actualKeys = searching(key);
	//if the list is not empty, then we have keys with valid definition(s), find them in the map
	if (!actualKeys.isEmpty()){
	    for (String word : actualKeys){
		definitions = map.get(word);
		System.out.println("The definition(s) of " + OPEN_BOLD + "\"" + word +  "\"" + CLOSE_BOLD + " are: " + definitions.toString());
	    }
	}
	// only if I explicitly prompted this method with `lookup`, tell tell me that you could not find the word
	else if (keep == null) System.out.println(OPEN_BOLD + "I could not find " + "\"" + key + "\" in your dictionary." + CLOSE_BOLD);
  }
    
    private ArrayList<String> searching(String key){
	ArrayList<String> presentKeys = new ArrayList<>();
	//O(n), see if the keys of the map contain the "key" that the user is searching
	for (Map.Entry<String,ArrayList<String>> entry : map.entrySet()){
	    String currentKey = entry.getKey();
	    if (currentKey.contains(key)){
		presentKeys.add(currentKey);
	    }
	}
	return presentKeys;
    }
    
    // COMMAND : MISMATCH
    public Map<String,ArrayList<String>> mismatch(){
	/* Create a new map that will have all the values of the old map
	 * but in a weird order. We will use a LinkedHash Map.
	 */	
	//new map to store the values.
	Map<String,ArrayList<String>> newMap = new LinkedHashMap<>();
	// add all keys to a vector so we can shuffle it
	Set<String> keySet = map.keySet();
	List<String> keys = new ArrayList<>();
	keys.addAll(keySet);
	Collections.shuffle(keys);
	for (String key : keys){
	    newMap.put(key, map.get(key));
	}
	return newMap;
    }
    
    // COMMAND : SERIOUSLY
    
    public void wickedMismatch(){
	/* IDEA: Create a new map that will have all the values of the old map
	 * but with all words mismatched. We will use a LinkedHash Map to maintain insertion order.
	 * *****
	 */
	Scanner s = new Scanner(System.in);
	Random obj = new Random();
	System.out.print("\u26A0\u26A0\u26A0 Are you sure? ");
	String answer = s.nextLine();
	if (answer.equals("y") || answer.contains("yes")){
	    System.out.println("You asked! \uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C\uD83C\uDF02\uD83C\uDF00\uD83D\uDC7E\uD83D\uDE4A\uD83D\uDE0E\uD83C\uDF1E\uD83D\uDC13\uD83D\uDE09\uD83D\uDE05\uD83D\uDCE3\uD83D\uDCA2\uD83D\uDC30\uD83D\uDC2E\uD83D\uDC27\uD83C\uDFC1\uD83C\uDF88\u3030\u2714\u203C");
	    int ranNum = obj.nextInt(1000000);
	    

	    // create wacky map
	    for (int i = 0; i < ranNum ; i++){
		Map<String,ArrayList<String>> wacky = new LinkedHashMap<>();
		//new map from mismatch() every time!
		Map<String,ArrayList<String>> mismatched = mismatch();

		for (Map.Entry<String,ArrayList<String>> each : mismatched.entrySet()){
		    String rearrangedKey = reorderChar(each.getKey());
		    //rearrange each definition in value
		    ArrayList<String> definitions  = each.getValue();
		    for (int j = 0; j < definitions.size(); j++){
			definitions.set(j, reorderChar(definitions.get(j)));
		    }
		    wacky.put(rearrangedKey,definitions);
		}
		
		// periodically annoy the user
		int random = obj.nextInt(10000);
		if (i == 300){
		    System.out.println("Wait, yeah that was actually very annoying...So go ahead and type your commands as you would...");
		    try{
			Thread.sleep(5000);
		    } catch(InterruptedException e){ }
		    System.out.println("There's no way I could let it be that easy.\uD83D\uDC0D");
		    try{
			Thread.sleep(3000);
		    } catch(InterruptedException e){ }
		}
		// stop at some random point
		if (i == random){
		    System.out.println("No, like actually, I'm sorry. This was rude, and it'll be over soon.\uD83D\uDE01");
		    try{
			Thread.sleep(5000);
		    } catch(InterruptedException e){ }
		}
		showMap(wacky);
	    }
	}
    }
    
    // COMMAND : HELP
    
    public static void help(){
	System.out.println("Use your own personal dictionary with ease.\n" +
			   "\n" +
			   "Valid Commands include:" +
			   "\n" +
			   "1. lookup (look up definition(s) in your dictionary with a word itself)\n" +
			   "2. remove (remove a word and all its definitions from the dictionary)\n" +
			   "3. removedef (remove a definition from a specific word in the dictionary)\n" +
			   "4. kill (to safely save and quit the program)\n" +
			   "5. clear or reset (to remove all entries in your dictionary)\n" +
			   "6. show (show all entries in the dictionary)\n" +
			   "7. count (count all entries in your dictionary)\n" +
			   "8. mismatch (fun command)\n"  +
			   "9. seriously (fun but dangerous command)\n"+
			   "10. help (display this help message screen)");
	
    }

    //fileName is our parameter just in case we have a dictionary we want to load
    private void load(String fileName){
	if (fileName == null) fileName = "_private.txt";
	String each;
	try{
	    //we want to read _private.txt
	    FileReader fileRead = new FileReader(fileName);
	    BufferedReader bufferRead = new BufferedReader(fileRead);
	    each = bufferRead.readLine();
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
	    ioe.printStackTrace();
	}
    }
    
    // returns the count of elements printed
    public int printKeys(ArrayList<String> list){
	int count = list.size();
	// format grammatically 
	String statement = count == 1 ? "I found " + count + " result: " : (count == 0 ? "I found " + count + " results." : "I found " + count + " results: ");
	System.out.println(OPEN_BOLD + statement + CLOSE_BOLD);
	for (int i = 0; i < count ; i++){
	    System.out.println("\t" + (i+1) + ") " + list.get(i));
	}
	return count;
    }
    
    //randomly rearrange the characters in a string
    private String reorderChar(String str){
	List<Character> list = new ArrayList<>();
	for (char c : str.toCharArray()) list.add(c);
	Collections.shuffle(list);
	char[] arr = new char[list.size()];
	for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
	return new String(arr);
    }

    //java's first argument is not the filename like in python and c!
    public static void main(String[] args){
	frenchDict frenchDict = new frenchDict();
	System.out.println(frenchDict.OPEN_BOLD + "Welcome to Your Dictionary!\n" + frenchDict.CLOSE_BOLD);
	//if we get an array out of bounds exception, then let's just load our default
	//file : _private.txt
	if (args.length == 1) frenchDict.load(args[0]);
	else frenchDict.load(null);
	System.out.println("Please enter a word and its definition followed by a colon!\n");
	String pair;
	Scanner s = new Scanner(System.in);
	while (s.hasNext()){
	    pair = s.nextLine();
	    frenchDict.decide(pair);
	}
    }
}
