package jp.co.worksap.global;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by phucly on 6/4/15.
 */
public class TravellingSaleman {

    public TravellingSaleman() {
        this.map = null;
    }

    private String[][] map;

    public void readFile(String filename) {
        BufferedReader br = null;

        try {

            String line;
            int rowNo = 0;
            int columnNo = 0;

            br = new BufferedReader(new FileReader(filename));

            // get dimensions
            while ((line = br.readLine()) != null) {
                rowNo++;
                columnNo=line.length();
            }
            map = new String[rowNo][columnNo];

            br.reset();
            while ((line = br.readLine()) != null) {
                rowNo++;
                columnNo=line.length();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
