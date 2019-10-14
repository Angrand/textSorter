package textSorterPackage;

import java.io.*;
import java.util.*;

/**
 * Sorts a large file filled with Strings.
 * Algorithm:
 * 1) read the input file partially, sorting and writing every data block into the new temp file
 * 2) then all sorted blocks are merged into the outputfile.
 *
 * Merging algo: writing the first line of the every temp file into a heap, then taking the (min) of a heap into the outputfile,
 * then adding a new line (from the file we just took the line) to the heap.
 *
 *
 */

public class TextFileSorter {
    private final int maxBlockSize;
    private List<String> curStringList = new ArrayList<>();
    private List<File> tempFileNames = new LinkedList<>();


    public TextFileSorter(int blockSize) {
        this.maxBlockSize = blockSize;
    }

    public void sortFile(String inputFileName, String outputFileName) {
        splitFileInBlocks(inputFileName);
        mergeTempFiles(outputFileName);
    }

    /**
     * merging sorted small tempfiles into the large outputfile
     * using HashMap to keep track of which reader has read which line, as method includes reading next line from the file we just wrote from.
     *
     * @param outputFileName resulted large and sorted file
     */

    private void mergeTempFiles(String outputFileName) {
        Map<StringWrapper, BufferedReader> curMap = new HashMap<>();
        BufferedWriter bw = null;
        List<BufferedReader> brList = new LinkedList<>();
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)));
            PriorityQueue<StringWrapper> curStringsHeap = new PriorityQueue<>();

            for (File file : tempFileNames) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getName())));
                StringWrapper key = new StringWrapper(br.readLine());
                curMap.put(key, br);
                curStringsHeap.add(key);
                brList.add(br);
            }

            while (curMap.size() > 0) {
                StringWrapper sToWrite = curStringsHeap.poll();
                bw.write(sToWrite.string);
                bw.write("\n");
                bw.flush();
                String curLine;
                BufferedReader br = curMap.remove(sToWrite);
                if ((curLine = br.readLine()) != null) {
                    StringWrapper swLine = new StringWrapper(curLine);
                    curMap.put(swLine, br);
                    curStringsHeap.add(swLine);
                }
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
            brList.forEach(br -> {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            for (File file : tempFileNames) {
                file.delete();
            }
        }
    }

    /**
     *  Wrapper for Strings.
     *  Objects of this class are used as keys in a HashMap instead of Strings,
     *  to avoid dataloss when there are duplicate Strings.
     *
     */

    private class StringWrapper implements Comparable<StringWrapper> {
        private final String string;

        public StringWrapper(String line) {
            this.string = line;
        }

        @Override
        public int compareTo(StringWrapper o) {
            return string.compareTo(o.string);
        }


    }


    private void splitFileInBlocks(String inputFileName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName)));
            String curString;
            int curBlockSize = 0;
            int blockCounter = 0;

            while ((curString = br.readLine()) != null) {
                curBlockSize += curString.length();
                curStringList.add(curString);
                if (curBlockSize >= maxBlockSize) {
                    File tempFile = new File("temp_" + blockCounter + ".txt");
                    tempFileNames.add(tempFile);
                    blockCounter++;
                    Collections.sort(curStringList);
                    writeCurrentBlockToFile(tempFile.getName());
                    curStringList.clear();
                    curBlockSize = 0;
                }
            }
            // writing the last block at the end too

            File tempFile = new File("temp_" + blockCounter + ".txt");
            tempFileNames.add(tempFile);
            Collections.sort(curStringList);
            writeCurrentBlockToFile(tempFile.getName());
            curStringList.clear();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void writeCurrentBlockToFile(String fileName) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
            for (String s : curStringList) {
                bw.write(s);
                bw.write("\n");
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
}
