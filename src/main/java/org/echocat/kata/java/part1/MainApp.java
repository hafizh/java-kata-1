package org.echocat.kata.java.part1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.echocat.kata.java.part1.csv.DataStore;

@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class MainApp {

    public static void main(String[] args) {
        DataStore dataStore = new DataStore();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(dataStore.getAllMedia()));
    }

    protected static String getHelloWorldText() {
        return "Hello world!";
    }

}
