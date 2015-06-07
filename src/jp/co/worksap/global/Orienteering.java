package jp.co.worksap.global;

public class Orienteering {

    public static void main(String[] args) {
        TravellingSalesman salesman = new TravellingSalesman();
        try {
            salesman.readFile(args[0]);
        } catch (TravellingSalesman.InvalidInputException e) {
            System.out.println("-1");
        }
        try {
            TravellingSalesman.CalObj solution = salesman.getSolution();
            System.out.println(solution.getValue());
        } catch (TravellingSalesman.PathNotFoundException e) {
            System.out.println("-1");
        }
    }
}