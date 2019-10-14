package fileCreatorPackage;

import java.io.*;
import java.util.Random;

/**
 * Class that generates a file with Strings with the random filling
 * Strings are generated using random function on the set of letters and numbers
 *
 */

public class FileRandomCreator {
    private static final String LOW_CASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UP_CASE_CHAR = LOW_CASE_CHARS.toUpperCase();
    private static final String DATA_FOR_RANDOM_STRING = LOW_CASE_CHARS + UP_CASE_CHAR + "0123456789";
    private final String fileName;
    private long numberOfStringsInFile;
    private int maxStringLength;

    public FileRandomCreator(String fileName, long numberOfStringsInFile, int maxStringLength) {
        this.fileName = fileName;
        this.numberOfStringsInFile = numberOfStringsInFile;
        this.maxStringLength = maxStringLength;
    }

    /**
     * creating a file with non-empty random Strings
     *
     */

    public void createFile() {
        File file = new File(fileName);
        Random random = new Random();
        BufferedWriter bw = null;
        try {
            file.createNewFile();
            System.out.println("file created");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));

            for (long i = 0; i < numberOfStringsInFile; i++) {
                bw.write(createRandomString(1 + random.nextInt(maxStringLength - 1)));
                bw.write("\n");
                if (i != 0 && i % 50000 == 0) {
                    System.out.println("wrote " + i + " Strings");
                }
                bw.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Creating random String by pulling random chars from DATA_FOR_RANDOM_STRING
     *
     * @param length is String length
     * @return random String
     */

    private String createRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            Random random = new Random();
            int randomCharInt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char randomChar = DATA_FOR_RANDOM_STRING.charAt(randomCharInt);
            sb.append(randomChar);

        }
        return sb.toString();

    }
}
