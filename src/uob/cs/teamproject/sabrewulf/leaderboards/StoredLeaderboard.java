package uob.cs.teamproject.sabrewulf.leaderboards;

import uob.cs.teamproject.sabrewulf.ui.selectors.DIFFICULTY;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class helps store and represent a collection of leaderboard entries as a single leaderboard.
 */
public class StoredLeaderboard {

    private ArrayList<LeaderboardElement> elements = new ArrayList<>();
    private String filePath;
    private int MAX_ELEMENTS = 1000;

    /**
     * Constructor for a new leaderboard.
     * @param difficulty: The difficulty of the game in order to access the correct leaderboard.
     */
    public StoredLeaderboard(DIFFICULTY difficulty) {
        if (difficulty == DIFFICULTY.EASY) {
            filePath = "json/leaderboardEASY.json";
        }
        else if (difficulty == DIFFICULTY.MEDIUM) {
            filePath = "json/leaderboardMEDIUM.json";
        }
        else {
            filePath = "json/leaderboardHARD.json";
        }
        JSONtoleaderboard();
    }

    /**
     * Constructor for a new leaderboard, by specifying the file path (used for testing a seperate leaderboard).
     * @param path: The file path of the leaderboard in order to access the correct one.
     */
    public StoredLeaderboard(String path) {
        this.filePath = path;
        JSONtoleaderboard();
    }

    /**
     * Returns the data associated with the leaderboard.
     * @return ArrayList of LeaderboardElements.
     */
    public ArrayList<LeaderboardElement> getElements() {
        return elements;
    }

    /**
     * Inserts an element into the leaderboard.
     * The highest score will be stored at index 0, and the smallest score will
     * be stored at index n where n = elements.size()-1.
     * When inserting a score which is already in the leaderboard, the newest
     * occurrence of such score shall be placed higher since it occurred more recently.
     * Only 1000 elements are stored so inserting an element into an already 'full'
     * leaderboard will cause the last element to be removed.
     * @param newElement: The leaderboard element to be added to the leaderboard.
     */
    public void insertScore(LeaderboardElement newElement) {
        int score = newElement.getScore();

        if (elements.isEmpty()) {
            elements.add(newElement);
        }
        else if (elements.size() == 1) {
            if (elements.get(0).getScore() <= score) {
                elements.add(0, newElement);
            }
            else {
                elements.add(newElement);
            }
        }
        else {
            boolean inserted = false;
            for (int i=0; i<elements.size(); i++) {

                if (elements.get(i).getScore() <= score) {
                    elements.add(i, newElement);
                    inserted = true;
                    break;
                }

            }
            if (!inserted) {
                elements.add(newElement);
            }

        }

        //only store 1000 elements
        if (elements.size() == MAX_ELEMENTS + 1) {
            elements.remove(MAX_ELEMENTS);
        }

        leaderboardToJSON();
    }

    /**
     * Changes the file path of a leaderboard which has already been created
     * @param leaderboard - Leaderboard object for file path to be changed
     * @param difficulty - Difficulty of the current game
     */
    public void switchLeaderboard(StoredLeaderboard leaderboard, DIFFICULTY difficulty) {
        if (difficulty == DIFFICULTY.EASY) {
            filePath = "json/leaderboardEASY.json";
        }
        else if (difficulty == DIFFICULTY.MEDIUM) {
            filePath = "json/leaderboardMEDIUM.json";
        }
        else {
            filePath = "json/leaderboardHARD.json";
        }
        leaderboard.JSONtoleaderboard();
    }

    /**
     * Clears the leaderboard, primarily for testing purposes.
     */
    public void clearLeaderboard() {
        elements = new ArrayList<>();
    }

    /**
     *
     * @return names and scores for each leaderboard element in a convenient form.
     */
    @Override
    public String toString() {
        String output = "Leaderboard:\n";
        for (LeaderboardElement element : elements) {
            output += element.getName() + ": " + element.getScore() + " @ " + element.getTime() + "\n";
        }
        output = output + "__________";
        return output;
    }

    /**
     * Converts the leaderboard belonging to an instance of StoredLeaderboard
     * into a JSON format, and writes it to leaderboard.json.
     */
    public void leaderboardToJSON() {
        String json = "[";
        for (LeaderboardElement elem : elements) {
            json = json + "{"
                    + toJSON("name", elem.getName()) + ","
                    + toJSON("score", elem.getScore()) + ","
                    + toJSON("time", elem.getTime()) + "},";
        }
        json = json.substring(0, json.length()-1);
        json = json + "]";

        //now write to file
        try {
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write(json);
            myWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Reads from the file leaderbard.json and converts the JSON file
     * into an ArrayList of LeaderboardElements, then assigns this
     * ArrayList to StoredLeaderboard.elements.
     */
    public void JSONtoleaderboard() {

        String json = ""; //read from file
        ArrayList<LeaderboardElement> myElements = new ArrayList<>();

        try {
            File file = new File(filePath);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                json = json + myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        String[] jsonElements;
        try {
            json = json.substring(2,json.length()-2);
            jsonElements = json.split("},\\{");

            for (String e : jsonElements) {
                String name;
                String strScore;
                int score;
                String strTime;
                LocalDateTime time;

                name = e.split(",")[0];
                name = name.replaceAll("\"name\":","");
                name = name.substring(1,name.length()-1);

                strScore = e.split(",")[1];
                strScore = strScore.replaceAll("\"score\":","");
                strScore = strScore.substring(1,strScore.length()-1);
                score = Integer.parseInt(strScore);

                strTime = e.split(",")[2];
                strTime = strTime.replaceAll("\"time\":", "");
                strTime = strTime.substring(1,strTime.length()-1);
                try {
                    time = LocalDateTime.parse(strTime, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                }
                catch (Exception e1) {
                    time = null;
                }

                LeaderboardElement elementToAdd = new LeaderboardElement(name, score, time);
                myElements.add(elementToAdd);
            }
            elements = myElements;
        }
        catch (Exception ignored) {

        }
    }



    /**
     * Helper function which converts a field into a JSON format.
     * @param fieldName: name of the field.
     * @param value: name of the value.
     * @return "fieldName":"value"
     */
    private String toJSON(String fieldName, Object value) {
        return "\"" + fieldName +"\":" + "\"" + value.toString() + "\"";
    }

}
