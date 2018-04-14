package Executing;
import java.util.*;

/**
 * User:   bl
 * Date:   3/2/18
 * Time:   4:11 PM
 * Package PACKAGE_NAME
 * File    TestAndTry4
 * Description
 */
public class CombinationRecursive {
    private static final String SPLITOR = " ";
    
    public static void main(String[] args) {
        HashSet<String> set1 = new HashSet<>();
        set1.add("awesome");
        set1.add("good");

        HashSet<String> set2 = new HashSet<>();
        set2.add("spaghetti");
        set2.add("pizza");

        HashSet<String> set3 = new HashSet<>();
        set3.add("delicious");
        set3.add("yummy");
        set3.add("tasty");
        set3.add("juicy");

        HashSet<String> set4 = new HashSet<>();

        HashSet<String> set5 = new HashSet<>();
        set5.add("spicy");
        set5.add("hot");


        List<HashSet<String>> words = new ArrayList<>();
        words.add(set1);
        words.add(set2);
        words.add(set3);
        words.add(set4);
        words.add(set5);

        List<String> queries = new ArrayList<>();
        String query = new String();

        System.out.println("---------------------------------");
        for (Set<String> set : words) {
            System.out.println(set);
        }
        System.out.println("---------------------------------");
   //     genQuery(queries, query, words, 0);
        for (int i = 0; i < queries.size(); i++) {
            System.out.println("i = " + i + "\t\t" + queries.get(i));
        }
    }

    /**
     *
     * @param queries
     * @param query
     * @param words
     * @param wordsPos
     */
    public void genQuery(List<String> queries, String query, List<HashSet<String>> words, int wordsPos) {
        if (wordsPos == words.size()) {
            queries.add(query);
            return;
        }

        Set<String> set = words.get(wordsPos);
        if (set.isEmpty()) {
            genQuery(queries, query, words, wordsPos + 1);
        }

        Iterator<String> it = set.iterator();

        while (it.hasNext()) {
            String token = it.next();
            query += token;
            query += SPLITOR;
            genQuery(queries, query, words, wordsPos + 1);
            query = remove(query, token, SPLITOR);
        }
    }

    private static String remove(String query, String token, String SPLITOR) {
        String[] sub = query.split(SPLITOR);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sub.length - 1; i++) {
            sb.append(sub[i]).append(SPLITOR);
        }
        return sb.toString();
    }
}
