package jp.co.worksap.global;

public class Orienteering {

    public static void main(String[] args) {
        TravellingSalesman salesman = new TravellingSalesman(args[0]);
        salesman.getSolution();
    }
}