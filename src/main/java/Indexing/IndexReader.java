
package Indexing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Executing.CombinationRecursive;
import PreProcess.ProcessDocuments;

public class IndexReader {
    String[] words;
    ArrayList<String> userTokens = new ArrayList<String>();
    ArrayList<String> tokens = new ArrayList<String>();
    String[] arr;
    HashSet<String[]> queries = new HashSet<String[]>();

    public IndexReader(String query, Map<String, HashSet<String>> synonymsMap, Map<String, Integer> tokenDict) throws IOException {
        // pre process original query
        ProcessDocuments pd = new ProcessDocuments();
        this.words = pd.PreProcessQuery(query);
        for (int i = 0; i < words.length; i++) {
            if (!words[i].equals("")) {
                if (tokenDict.get(words[i]) != null)
                    userTokens.add(words[i]);
            }
        }
        arr = new String[userTokens.size()];
        arr = userTokens.toArray(arr);
        List<HashSet<String>> temp = new ArrayList<HashSet<String>>();

        for (int i = 0; i < userTokens.size(); i++) {
            if (synonymsMap.get(userTokens.get(i)) != null) {
                HashSet<String> synonyms = synonymsMap.get(userTokens.get(i));
                synonyms.add(userTokens.get(i));
                temp.add(synonyms);
            } else {
                HashSet<String> synonyms = new HashSet<String>();
                synonyms.add(userTokens.get(i));
                temp.add(synonyms);
            }
        }
        // one query with multiple synonyms
        for (HashSet<String> hs : temp) {
            for (String s : hs)
                tokens.add(s);
        }
        // multiple queries
        CombinationRecursive cr = new CombinationRecursive();
        List<String> q1 = new ArrayList<>();
        String q2 = new String();
        cr.genQuery(q1, q2, temp, 0);

        for (int i = 0; i < q1.size(); i++) {
            String[] a = q1.get(i).split(" ");
            queries.add(a);
        }
    }

    public HashSet<String[]> getQueries() {
        return queries;
    }

    public String[] getQuery() {
        return arr;
    }

    public String[] getQueryWithSynonyms() {
        String[] a = new String[tokens.size()];
        a = tokens.toArray(a);
        return a;
    }

    public HashMap<String, Integer> getDocumentFrequency(Map<String, Integer> tokenDict, String[] query) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        for (String token : query) {
            result.put(token, tokenDict.get(token));
        }
        return result;
    }

    public HashMap<Integer, Map<String, Integer>> getTermFrequency(Map<String, HashMap<Integer, Integer>> postingList, String[] query) throws IOException {
        HashMap<Integer, Map<String, Integer>> result = new HashMap<Integer, Map<String, Integer>>();
        for (String token : query) {
            HashMap<Integer, Integer> reviewIdFreq = postingList.get(token);

            if (reviewIdFreq == null) {
                continue;
            }

            for (Entry<Integer, Integer> entry : reviewIdFreq.entrySet()) {

                int reviewId = entry.getKey();
                int tf = entry.getValue();

                if (result.containsKey(reviewId)) {
                    Map<String, Integer> temp = result.get(reviewId);
                    if (temp.containsKey(token)) {
                        int t = temp.get(token);
                        tf = t + tf;
                        temp.put(token, tf);
                    } else {
                        temp.put(token, tf);
                    }
                    result.put(reviewId, temp);
                } else {
                    Map<String, Integer> temp = new HashMap<String, Integer>();
                    temp.put(token, tf);
                    result.put(reviewId, temp);
                }
            }
        }
        return result;
    }
}
