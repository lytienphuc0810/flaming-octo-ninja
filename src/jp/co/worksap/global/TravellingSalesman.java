package jp.co.worksap.global;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phucly on 6/4/15.
 */
public class TravellingSalesman {

    public static final short START = 1;
    public static final short END = 2;
    public static final short CHECKPOINT = 3;
    public static final short OPEN_BLOCK = 4;
    public static final short CLOSED_BLOCK = 5;
    private int rowNo;
    private int columnNo;

    public TravellingSalesman(String filename) {
        this.map = null;
        checkpointList = new ArrayList<Coordinate>();
        this.readFile(filename);
    }

    private short[][] map;

    private List<Coordinate> checkpointList;

    private Coordinate startPoint;

    private Coordinate endPoint;

    public void readFile(String filename) {
        BufferedReader br = null;

        try {
            String line;
            rowNo = 0;
            columnNo = 0;

            br = new BufferedReader(new FileReader(filename));

            // get dimensions
            while ((line = br.readLine()) != null) {
                rowNo++;
                columnNo = line.length();
            }
            map = new short[rowNo][columnNo];

            br.reset();
            int k = 0;
            while ((line = br.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    if ("S".equals(line.charAt(i))) {
                        map[i][k] = START;
                        startPoint = new Coordinate(i, k);
                    } else if ("G".equals(line.charAt(i))) {
                        map[i][k] = END;
                        endPoint = new Coordinate(i, k);
                    } else if ("@".equals(line.charAt(i))) {
                        map[i][k] = CHECKPOINT;
                        checkpointList.add(new Coordinate(i, k));
                    } else if (".".equals(line.charAt(i))) {
                        map[i][k] = OPEN_BLOCK;
                    } else if ("#".equals(line.charAt(i))) {
                        map[i][k] = CLOSED_BLOCK;
                    }
                }
                k++;
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

    public void getSolution() {
        List<Coordinate> coordinates = checkpointList;
        coordinates.add(0, startPoint);
        CalObj finalObj = calCost(coordinates, endPoint);
        System.out.println(finalObj);
    }

    public CalObj calCost(List<Coordinate> coordinates, Coordinate destination) {
        // TODO get path here input into finalObj
        if (coordinates.size() == 2) {
            return new CalObj(getPath(coordinates.get(0), coordinates.get(1)).size(), coordinates);
        } else {
            int min = -1;
            CalObj tempObj = null;
            List<Coordinate> sub = coordinates.subList(0, coordinates.size() - 1);
            sub.remove(destination);
            for (Coordinate coordinate : coordinates) {
                if (!coordinate.equals(startPoint) && !coordinate.equals(destination)) {
                    tempObj = calCost(sub, coordinate);
                    int temp = tempObj.getValue() + getPath(coordinate, destination).size();
                    if (min == -1 || min > temp) {
                        min = temp;
                    }
                }
            }
            return tempObj;
        }
    }

    public List<Coordinate> getPath(Coordinate start, Coordinate end) {
        List<Coordinate> rawPath = new ArrayList<Coordinate>();
        List<Coordinate> resultPath = new ArrayList<Coordinate>();
        List<Coordinate> stack = new ArrayList<Coordinate>();
        boolean pathNotFound = false;

        this.push(stack, start);
        while (true) {
            Coordinate currentCoordinate = this.pop(stack);

            if (currentCoordinate == null) {
                pathNotFound = true;
                break;
            }

            List<Coordinate> adjCoordinates = this.getAdjCoordinate(currentCoordinate);
            for (Coordinate coordinate : adjCoordinates) {
                if (!rawPath.contains(coordinate)) {
                    coordinate.setValue(this.getHeuristicValue(coordinate));
                    this.push(stack, coordinate);
                }
            }
            rawPath.add(currentCoordinate);

            if (currentCoordinate.equals(endPoint)) {
                break;
            }
        }

        if (pathNotFound) {
            return resultPath;
        } else {
            resultPath.add(rawPath.get(rawPath.size() - 1));
            Coordinate temp = rawPath.get(0);
            while (temp.parent != null) {
                temp = temp.parent;
                resultPath.add(0, temp);
            }
        }

        return resultPath;
    }

    public List<Coordinate> getAdjCoordinate(Coordinate coordinate) {
        int x = coordinate.x;
        int y = coordinate.y;
        List<Coordinate> result = new ArrayList<Coordinate>(4);
        if (x + 1 < rowNo && map[x + 1][y] != CLOSED_BLOCK) {
            result.add(new Coordinate(x + 1, y, coordinate));
        }
        if (x - 1 >= 0 && map[x - 1][y] != CLOSED_BLOCK) {
            result.add(new Coordinate(x - 1, y, coordinate));
        }
        if (y + 1 < columnNo && map[x][y + 1] != CLOSED_BLOCK) {
            result.add(new Coordinate(x, y + 1, coordinate));
        }
        if (y - 1 < rowNo && map[x][y - 1] != CLOSED_BLOCK) {
            result.add(new Coordinate(x, y - 1, coordinate));
        }
        return result;
    }

    public int getHeuristicValue(Coordinate current) {
        return Math.abs(current.getX() - endPoint.getX()) + Math.abs(current.getY() - endPoint.getY());
    }

    public void push(List<Coordinate> stack, Coordinate coordinate) {
        int i;
        for (i = 0; i < stack.size(); i++) {
            if (coordinate.getTotalValue() > stack.get(i).getTotalValue()) {
                break;
            }
        }
        stack.add(i, coordinate);
    }

    public Coordinate pop(List<Coordinate> stack) {
        if (stack.isEmpty()) {
            return null;
        }
        Coordinate coordinate = stack.get(0);
        stack.remove(0);
        return coordinate;
    }

    public class Coordinate {
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
            this.value = 0;
            this.distance = 0;
            this.parent = null;
        }

        public Coordinate(int x, int y, Coordinate parent) {
            this.x = x;
            this.y = y;
            this.value = 0;
            this.distance = parent.distance + 1;
            this.parent = parent;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        private Coordinate parent;
        private int x;
        private int y;
        private int value;
        private int distance;

        public int getTotalValue() {
            return value + distance;
        }

        public boolean equals(Coordinate coordinate) {
            return this.x == coordinate.x && this.y == coordinate.y;
        }

        public String toString() {
            return "(" + this.x + "," + this.y + ")";
        }
    }

    public class CalObj {
        private int value;
        private List<Coordinate> subPath;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public List<Coordinate> getSubPath() {
            return subPath;
        }

        public void setSubPath(List<Coordinate> subPath) {
            this.subPath = subPath;
        }

        public CalObj(int value, List<Coordinate> coordinates) {
            this.value = value;
            this.subPath = coordinates;
        }
    }
}
