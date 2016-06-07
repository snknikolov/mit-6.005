/*
 * NOTE: You don't need to modify any part of this file to complete the
 * assignment! This file can be run to check your progress.
 */

package pipoetry;

import java.util.Arrays;
import java.util.Map;

public class Main {
    public static final int PI_PRECISION = 10000;

    // List borrowed from: http://www.langmaker.com/wordlist/basiclex.htm
    // In general, you shouldn't hardcode big data like this. It's much more
    // flexible to read it as input from a file. However, for PS1, we wanted
    // to keep things simple for you.
    public static final String[] WORD_LIST = {"a", "able", "about", "account", "acid", "across", "act", "addition", "adjustment", "advertisement", "after", "again", "against", "agreement", "air", "all", "almost", "among", "amount", "amusement", "and", "angle", "angry", "animal", "answer", "ant", "any", "apparatus", "apple", "approval", "arch", "argument", "arm", "army", "art", "as", "at", "attack", "attempt", "attention", "attraction", "authority", "automatic", "awake", "baby", "back", "bad", "bag", "balance", "ball", "band", "base", "basin", "basket", "bath", "be", "beautiful", "because", "bed", "bee", "before", "behaviour", "belief", "bell", "bent", "berry", "between", "bird", "birth", "bit", "bite", "bitter", "black", "blade", "blood", "blow", "blue", "board", "boat", "body", "boiling", "bone", "book", "boot", "bottle", "box", "boy", "brain", "brake", "branch", "brass", "bread", "breath", "brick", "bridge", "bright", "broken", "brother", "brown", "brush", "bucket", "building", "bulb", "burn", "burst", "business", "but", "butter", "button", "by", "cake", "camera", "canvas", "card", "care", "carriage", "cart", "cat", "cause", "certain", "chain", "chalk", "chance", "change", "cheap", "cheese", "chemical", "chest", "chief", "chin", "church", "circle", "clean", "clear", "clock", "cloth", "cloud", "coal", "coat", "cold", "collar", "colour", "comb", "come", "comfort", "committee", "common", "company", "comparison", "competition", "complete", "complex", "condition", "connection", "conscious", "control", "cook", "copper", "copy", "cord", "cork", "cotton", "cough", "country", "cover", "cow", "crack", "credit", "crime", "cruel", "crush", "cry", "cup", "cup", "current", "curtain", "curve", "cushion", "damage", "danger", "dark", "daughter", "day", "dead", "dear", "death", "debt", "decision", "deep", "degree", "delicate", "dependent", "design", "desire", "destruction", "detail", "development", "different", "digestion", "direction", "dirty", "discovery", "discussion", "disease", "disgust", "distance", "distribution", "division", "do", "dog", "door", "doubt", "down", "drain", "drawer", "dress", "drink", "driving", "drop", "dry", "dust", "ear", "early", "earth", "east", "edge", "education", "effect", "egg", "elastic", "electric", "end", "engine", "enough", "equal", "error", "even", "event", "ever", "every", "example", "exchange", "existence", "expansion", "experience", "expert", "eye", "face", "fact", "fall", "false", "family", "far", "farm", "fat", "father", "fear", "feather", "feeble", "feeling", "female", "fertile", "fiction", "field", "fight", "finger", "fire", "first", "fish", "fixed", "flag", "flame", "flat", "flight", "floor", "flower", "fly", "fold", "food", "foolish", "foot", "for", "force", "fork", "form", "forward", "fowl", "frame", "free", "frequent", "friend", "from", "front", "fruit", "full", "future", "garden", "general", "get", "girl", "give", "glass", "glove", "go", "goat", "gold", "good", "government", "grain", "grass", "great", "green", "grey", "grip", "group", "growth", "guide", "gun", "hair", "hammer", "hand", "hanging", "happy", "harbour", "hard", "harmony", "hat", "hate", "have", "he", "head", "healthy", "hear", "hearing", "heart", "heat", "help", "high", "history", "hole", "hollow", "hook", "hope", "horn", "horse", "hospital", "hour", "house", "how", "humour", "i", "ice", "idea", "if", "ill", "important", "impulse", "in", "increase", "industry", "ink", "insect", "instrument", "insurance", "interest", "invention", "iron", "island", "jelly", "jewel", "join", "journey", "judge", "jump", "keep", "kettle", "key", "kick", "kind", "kiss", "knee", "knife", "knot", "knowledge", "land", "language", "last", "late", "laugh", "law", "lead", "leaf", "learning", "leather", "left", "leg", "let", "letter", "level", "library", "lift", "light", "like", "limit", "line", "linen", "lip", "liquid", "list", "little", "living", "lock", "long", "look", "loose", "loss", "loud", "love", "low", "machine", "make", "male", "man", "manager", "map", "mark", "market", "married", "mass", "match", "material", "may", "meal", "measure", "meat", "medical", "meeting", "memory", "metal", "middle", "military", "milk", "mind", "mine", "minute", "mist", "mixed", "money", "monkey", "month", "moon", "morning", "mother", "motion", "mountain", "mouth", "move", "much", "muscle", "music", "nail", "name", "narrow", "nation", "natural", "near", "necessary", "neck", "need", "needle", "nerve", "net", "new", "news", "night", "no", "noise", "normal", "north", "nose", "not", "note", "now", "number", "nut", "observation", "of", "off", "offer", "office", "oil", "old", "on", "only", "open", "operation", "opinion", "opposite", "or", "orange", "order", "organization", "ornament", "other", "out", "oven", "over", "owner", "page", "pain", "paint", "paper", "parallel", "parcel", "part", "past", "paste", "payment", "peace", "pen", "pencil", "person", "physical", "picture", "pig", "pin", "pipe", "place", "plane", "plant", "plate", "play", "please", "pleasure", "plough", "pocket", "point", "poison", "polish", "political", "poor", "porter", "position", "possible", "pot", "potato", "powder", "power", "present", "price", "print", "prison", "private", "probable", "process", "produce", "profit", "property", "prose", "protest", "public", "pull", "pump", "punishment", "purpose", "push", "put", "quality", "question", "quick", "quiet", "quite", "rail", "rain", "range", "rat", "rate", "ray", "reaction", "reading", "ready", "reason", "receipt", "record", "red", "regret", "regular", "relation", "religion", "representative", "request", "respect", "responsible", "rest", "reward", "rhythm", "rice", "right", "ring", "river", "road", "rod", "roll", "roof", "room", "root", "rough", "round", "rub", "rule", "run", "sad", "safe", "sail", "salt", "same", "sand", "say", "scale", "school", "science", "scissors", "screw", "sea", "seat", "second", "secret", "secretary", "see", "seed", "seem", "selection", "self", "send", "sense", "separate", "serious", "servant", "sex", "shade", "shake", "shame", "sharp", "sheep", "shelf", "ship", "shirt", "shock", "shoe", "short", "shut", "side", "sign", "silk", "silver", "simple", "sister", "size", "skin", "skirt", "sky", "sleep", "slip", "slope", "slow", "small", "smash", "smell", "smile", "smoke", "smooth", "snake", "sneeze", "snow", "so", "soap", "society", "sock", "soft", "solid", "some", "son", "song", "sort", "sound", "soup", "south", "space", "spade", "special", "sponge", "spoon", "spring", "square", "stage", "stamp", "star", "start", "statement", "station", "steam", "steel", "stem", "step", "stick", "sticky", "stiff", "still", "stitch", "stocking", "stomach", "stone", "stop", "store", "story", "straight", "strange", "street", "stretch", "strong", "structure", "substance", "such", "sudden", "sugar", "suggestion", "summer", "sun", "support", "surprise", "sweet", "swim", "system", "table", "tail", "take", "talk", "tall", "taste", "tax", "teaching", "tendency", "test", "than", "that", "the", "then", "theory", "there", "thick", "thin", "thing", "this", "thought", "thread", "throat", "through", "through", "thumb", "thunder", "ticket", "tight", "till", "time", "tin", "tired", "to", "toe", "together", "tomorrow", "tongue", "tooth", "top", "touch", "town", "trade", "train", "transport", "tray", "tree", "trick", "trouble", "trousers", "true", "turn", "twist", "umbrella", "under", "unit", "up", "use", "value", "verse", "very", "vessel", "view", "violent", "voice", "waiting", "walk", "wall", "war", "warm", "wash", "waste", "watch", "water", "wave", "wax", "way", "weather", "week", "weight", "well", "west", "wet", "wheel", "when", "where", "while", "whip", "whistle", "white", "who", "why", "wide", "will", "wind", "window", "wine", "wing", "winter", "wire", "wise", "with", "woman", "wood", "wool", "word", "work", "worm", "wound", "writing", "wrong", "year", "yellow", "yes", "yesterday", "you", "young"};

    public static final char[] BASIC_ALPHABET =
        {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
         'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static void main(String[] args) {
        System.out.println("Problem 1: Calculating Pi...");
        int[] piHexDigits = PiGenerator.computePiInHex(PI_PRECISION);
        System.out.println("here");
        System.out.printf(
                "Digits of Pi in base-16: %s\n\n",
                MaybeTruncateString(Arrays.toString(piHexDigits), 50));

        System.out.println("Problem 2: Translating Pi to base-26...");
        int[] translatedPiBase26 =
                BaseTranslator.convertBase(piHexDigits, 16, 26, PI_PRECISION);
        System.out.printf(
                "Digits of Pi in base-26: %s\n\n",
                MaybeTruncateString(Arrays.toString(translatedPiBase26), 50));
        
        System.out.println("Problem 3: Converting Pi using basic alphabet");
        String basicConversion = DigitsToStringConverter.convertDigitsToString(
                translatedPiBase26, 26, BASIC_ALPHABET);
        System.out.printf(
                "Digits of Pi translated into a-z: %s\n\n",
                MaybeTruncateString(basicConversion, 50));

        System.out.println("Problem 4: Getting word matches");
        Map<String, Integer> basicSubstrings =
                WordFinder.getSubstrings(basicConversion, WORD_LIST);
        for (Map.Entry<String, Integer> entry : basicSubstrings.entrySet()) {
            printWithContext(basicConversion, entry.getValue(),
                             entry.getKey(), 3, true);
        }
        System.out.printf("Word coverage using basic alphabet: %f\n\n",
                ((double)basicSubstrings.size()) / WORD_LIST.length);
        
        System.out.println("Problem 5: Getting word matches with base-100 and" +
        		           " frequency dictionary");
        int[] translatedPiBase100 =
                BaseTranslator.convertBase(piHexDigits, 16, 100, PI_PRECISION);
        char[] alphabet = AlphabetGenerator.generateFrequencyAlphabet(
                100, WORD_LIST);
        
        System.out.printf("Frequency dictionary generated: %s\n",
                          MaybeTruncateString(Arrays.toString(alphabet), 50));
        
        String frequencyConversion =
                DigitsToStringConverter.convertDigitsToString(
                        translatedPiBase100, 100, alphabet);
        System.out.printf(
                "Digits of Pi translated into a-z: %s\n",
                MaybeTruncateString(frequencyConversion, 50));
        
        Map<String, Integer> frequencySubstrings =
                WordFinder.getSubstrings(frequencyConversion, WORD_LIST);
        for (Map.Entry<String, Integer> entry :
             frequencySubstrings.entrySet()) {
            printWithContext(frequencyConversion, entry.getValue(),
                             entry.getKey(), 3, true);
        }
        System.out.printf("Word coverage using frequency alphabet: %f\n\n",
                ((double)frequencySubstrings.size()) / WORD_LIST.length);
        
    }
    
    /**
     * If the input is less than or equal to len letters long, return it
     * unchanged. If the input is greater than len letters long, trim it to
     * len letters, then add an ellipses to the end.
     * 
     * @param input String to maybe truncate.
     * @param len Length to truncate to.
     * @return The input, potentially truncated to len letters with a trailing
     *         ellipses.
     */
    public static String MaybeTruncateString(String input, int len) {
        return ((input.length() > len) ? input.substring(0, len) : input) +
               ((input.length() > len) ? "..." : "");
    }

    /**
     * Pretty print a substring of a string with some context information to
     * either side.
     * 
     * @param haystack String to print from.
     * @param offset Index to start the substring in haystack.
     * @param needle The substring that should be printed from haystack.
     * @param shouldOffset If true, offset output with a leading tab char.
     * @param numContext Max amount of context characters to take from either
     *                   end of the substring.
     */
    public static void printWithContext(String haystack, int offset,
                                        String needle, int numContext,
                                        boolean shouldOffset) {
        if (offset < 0 || offset + needle.length() > haystack.length() ||
            needle.length() < 0) {
            return;
        }

        int substringStart = Math.max(0, offset - numContext);
        int substringEnd = Math.min(haystack.length(),
                                    offset + needle.length() + numContext);

        System.out.printf("%sSubstring '%s' found at index %d: %s%s%s\n",
                          (shouldOffset) ? "\t" : "",
                          needle, offset,
                          (substringStart > 0) ? "..." : "",
                          haystack.substring(substringStart, substringEnd),
                          (substringEnd < haystack.length()) ? "..." : "");
    }
}
