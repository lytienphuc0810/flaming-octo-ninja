package jp.co.worksap.global;

public class Orienteering {

    public static void main(String[] args) {
        TravellingSalesman salesman = new TravellingSalesman();

        if (args.length == 0) {
            System.out.println("-1");
            return;
        }

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