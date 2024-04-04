import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
    public void train(String fileName) {
        String window = "";
        char c;
        In in = new In(fileName); 
        for (int i = 0; i < this.windowLength && !in.isEmpty(); i++) {
            window += in.readChar();
        }
        while (!in.isEmpty()) {
            c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs); 
            }
            probs.update(c);
            window = window.substring(1) + c;
        }
        for (List probs : CharDataMap.values()) { 
            calculateProbabilities(probs);
        }
    }
    public void calculateProbabilities(List probs) {
        if (probs.getSize() == 0) {
            return;
        }
        int totalCharacters = 0;
        for (CharData data : probs.toArray()) {
            totalCharacters += data.count;
        }
        double cumulativeProbability = 0; // Initialize cumulative probability
        for (CharData data : probs.toArray()) {
            data.p = (double) data.count / totalCharacters;
            cumulativeProbability += data.p; // Update cumulative probability
            data.cp = cumulativeProbability; // Set cumulative probability for current CharData
        }
    }
    public char getRandomChar(List probs) {
    double r = randomGenerator.nextDouble();
    double cumulativeProbability = 0;
    for (CharData data : probs.toArray()) {
        cumulativeProbability += data.p;
        if (r <= cumulativeProbability) {
            return data.chr;
        }
    }
    throw new NoSuchElementException("Could not find a character matching the random selection.");
}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength || textLength <= initialText.length()) {
            return initialText;
        }
    
        StringBuilder generatedText = new StringBuilder(initialText);
        while (generatedText.length() < textLength) {
            String currentWindow = generatedText.substring(Math.max(0, generatedText.length() - windowLength));
            List probs = CharDataMap.get(currentWindow);
    
            if (probs == null || probs.getSize() == 0) {
                break; 
            }
    
            char nextChar = getRandomChar(probs);
            generatedText.append(nextChar);
        }
    
        return generatedText.toString();
    }
    
    
    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];
        // Create the LanguageModel object
        LanguageModel lm;
        if (randomGeneration)
        lm = new LanguageModel(windowLength);
        else
        lm = new LanguageModel(windowLength, 20);
        // Trains the model, creating the map.
        lm.train(fileName);
        // Generates text, and prints it.
        System.out.println(lm.generate(initialText, generatedTextLength));
        }
}
