package fileCreatorPackage;

/**
 * Starts generating a file with the number of strings in file, max string length and file name set below
 *
 */

public class FileCreatorMain {
    private static final long NUMBER_OF_STRINGS_IN_FILE = 100000;
    private static final int MAX_STRING_LENGTH = 10000;
    public static final String UNSORTED_FILE_NAME = "embracerandomstrings.txt";


    public static void main(String[] args) {
        FileRandomCreator fileCreator = new FileRandomCreator(UNSORTED_FILE_NAME, NUMBER_OF_STRINGS_IN_FILE, MAX_STRING_LENGTH);
        fileCreator.createFile();
    }


}
