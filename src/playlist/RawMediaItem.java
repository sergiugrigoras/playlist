/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playlist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Sergiu.Grigoras
 */
public class RawMediaItem {

    private ArrayList<String> attribList;

    public RawMediaItem() {
        attribList = new ArrayList<>();
    }

    public void addRawRecord(String line) {
        attribList.add(line);
    }

    public String getID() {
        String s = "";
        for (String line : attribList) {
            if (line.contains("#LISTID")) {
                s = line.split(" ")[1];
            }
        }
        return s;
    }

    public String getItemType() {
        String s = "Video";
        for (String line : attribList) {
            if (line.contains("#EVENT NOTE")) {
                s = "Note";
            } else if (line.contains("#EVENT STOP") || line.contains("#EVENT WAIT")) {
                s = "Event";
            }
        }
        return s;
    }

    public String getTitle() {
        String s = "";
        for (String line : attribList) {
            if (line.contains("#EVENT STOP")) {
                s = "[Stop]";
            } else if (line.contains("#EVENT WAITTO")) {
                s = "[Wait Until " + line.split(" ")[2].split("\\.")[0] + "s]";
            } else if (line.contains("#EVENT WAIT")) {
                s = "[Wait " + line.split(" ")[2].split("\\.")[0] + "s]";
            } else if (line.contains("#EVENT NOTE")) {
                s = line.split(" ")[2];
            } else if (line.contains("; ")) {
                String[] splitedString = line.split("; ");
                s = splitedString[4];
            }
        }
        return s;
    }

    public String getFileLocation() {
        String s = "";
        for (String line : attribList) {
            if (line.contains("; ")) {
                String[] splitedString = line.split("; ");
                s = splitedString[0];
                s = s.replace("\"", "");
            }
        }
        return s;
    }

    public Duration getDuration() {

        Integer totalTimeInSeconds = 0;
        for (String line : attribList) {
            if (line.contains("; ")) {
                String[] splitedString = line.split("; ");
                String in = splitedString[1];
                String out = splitedString[2];
                if (in.isEmpty()) {
                    in = "0";
                }
                if (out.isEmpty()) {
                    out = "0";
                }
                in = in.split("\\.")[0];
                out = out.split("\\.")[0];
                totalTimeInSeconds = Integer.parseInt(out) - Integer.parseInt(in);
            } else if (line.contains("#EVENT WAIT ")) {
                totalTimeInSeconds = Integer.parseInt(line.split(" ")[2].split("\\.")[0]);
            }
        }
        return new Duration(totalTimeInSeconds);
    }

    public Integer getDurationInSeconds() {
        String in;
        String out;
        Integer totalTimeInSeconds = 0;
        for (String line : attribList) {
            if (line.contains("; ")) {
                String[] splitedString = line.split("; ");
                in = splitedString[1];
                out = splitedString[2];
                if (in.isEmpty()) {
                    in = "0";
                }
                if (out.isEmpty()) {
                    out = "0";
                }
                in = in.split("\\.")[0];
                out = out.split("\\.")[0];
                totalTimeInSeconds = Integer.parseInt(out) - Integer.parseInt(in);

            } else if (line.contains("#EVENT WAIT ")) {
                totalTimeInSeconds = Integer.parseInt(line.split(" ")[2].split("\\.")[0]);
            }
        }
        return totalTimeInSeconds;
    }

    public static ArrayList<RawMediaItem> getList(File file) throws FileNotFoundException, IOException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

            String line;
            ArrayList<RawMediaItem> rawlist = new ArrayList();
            RawMediaItem aRecord = new RawMediaItem();

            while ((line = br.readLine()) != null) {
                if (line.contains("#LISTID")) {
                    rawlist.add(aRecord);
                    aRecord = new RawMediaItem();
                    aRecord.addRawRecord(line);
                }//if
                else {
                    aRecord.addRawRecord(line);
                }
            }//while
            rawlist.add(aRecord);//ultimul adaugat
            br.close();
            rawlist.remove(0);

            return rawlist;
        }
    }
}
/*
 #LISTID 5825323103448
 #TC 0.00
 "E:\NEWSAIR\MATERIALE_PUB\EMISIUNI\Ora Expertizei\NEXT Ora expertizei.mpg"; 0.00; 7.00; ; NEXT Ora expertizei

 #LISTID 5825253779038
 #EVENT NOTE PROMO

 #LISTID 5823011653889
 #EVENT STOP
 */
