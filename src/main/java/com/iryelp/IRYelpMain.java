package com.iryelp;

import Executing.FileLoader;
import Executing.QueryRestaurants;
import Indexing.IndexReader;
import Indexing.Restaurant;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class IRYelpMain {
    Map<String, HashMap<Integer, Integer>> postingList;
    Map<String, Integer> tokenDict;
    Map<Integer, String[]> reviewTable;                    // reviews dict map
    Map<Integer, String[]> restaurantTable;            // restaurant map
    Map<String, String[]> urlMap;                                // url map
    Map<String, HashSet<String>> synonymsMap;

    public IRYelpMain() throws IOException {
        /*
         * Part 1: Load into memory when web page opens to reduce searching time
         */

        FileLoader fl = new FileLoader();
        postingList = fl.loadPostingList();    // posting list map
        tokenDict = fl.loadTokenDict();                            // token dict map
        reviewTable = fl.loadReviewTable();                    // reviews dict map
        restaurantTable = fl.loadrestaurantTable();            // restaurant map
        urlMap = fl.loadUrlMap();                                // url map
        synonymsMap = fl.loadSynonymsMap();

    }

    public ArrayList<HashMap<String, Object>> search(String userQuery) throws IOException {
        ArrayList<HashMap<String, Object>> springNeed = new ArrayList<>();

        double alpha = 0.5;    // review-star weight
        int topN = 10;        // top N restaurants display
        int topK = 5;        // latest topK reviews display

        /*
         * Part 2: Review score calculation and restaurant ranking
         */

        List<Restaurant> myList = new ArrayList<Restaurant>();
        IndexReader reader = new IndexReader(userQuery, synonymsMap, tokenDict);
        String[] query = reader.getQueryWithSynonyms();
        System.out.print("Your query is: ");
        for (String s : query)
            System.out.print(s + " ");
        System.out.print("\n");

        QueryRestaurants qr = new QueryRestaurants(tokenDict, reviewTable, postingList, restaurantTable, reader);
        Map<Integer, Restaurant> restaurantMap = qr.getRestaurants(query, alpha);

        // put restaurants into a list
        for (Entry<Integer, Restaurant> entry : restaurantMap.entrySet()) {
            myList.add(entry.getValue());
        }

        // rank restaurants
        myList.sort(Collections.reverseOrder((a, b) -> Double.compare(a.getRestaurantScore(), b.getRestaurantScore())));

        // select and print topN restaurants
        for (Restaurant r : myList.subList(0, topN)) {
            String restId = restaurantTable.get(r.getId())[0];
            String[] info = urlMap.get(restId);
            String name = info[0];
            String address = info[1];
            String url = info[2];
            HashMap<String, Object> adding = new HashMap<>();
            Map<Integer, String> reviewMap = r.getTopKReviews(topK, reviewTable);

            String score = String.valueOf(r.getRestaurantScore());
            adding.put("score", score);
            adding.put("resName", name);
            adding.put("address", address);
            adding.put("url", url);
            ArrayList<String> reviews = new ArrayList<>();
            for (Entry<Integer, String> entry : reviewMap.entrySet()) {
                reviews.add(entry.getKey() + ", " + entry.getValue());
            }
            adding.put("reviews", reviews);
            springNeed.add(adding);

        }
        return springNeed;
    }

}
