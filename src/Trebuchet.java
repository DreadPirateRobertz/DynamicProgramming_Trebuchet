import java.util.*;

public class Trebuchet {
    long calls = 0;
    ArrayList<ArrayList<Integer>> optimalThrowTable = new ArrayList<>();
    ArrayList<ArrayList<Integer>> x_Table = new ArrayList<>();
    ArrayList<Integer> tracer = new ArrayList<>();

    private int recursiveSoln(int p, int t) {
        calls++;
        int survive, smash, max, min;
        min = Integer.MAX_VALUE;

        if (t == 0) return 0;
        if (t == 1) return 1;
        if (p == 1) return t;

        for (int x = 1; x <= t; x++) {
            smash = recursiveSoln(p - 1, x - 1);
            survive = recursiveSoln(p, t - x);
            max = Math.max(smash, survive);
            if (max < min) min = max;
        }
        return 1 + min;
    }

    private void dpSoln(int p, int t){
        boolean deathFlag = false;
        int survive, smash, max, min;
        min = Integer.MAX_VALUE;
        resize(optimalThrowTable, p+1, t+1); //Size tables
        resize(x_Table, p+1, t+1);

        for (int i = 1; i <= p; i++) {
            for (int j = 0; j <= t; j++) {
                if (j == 0) optimalThrowTable.get(i).add(0);
                else if (j == 1) {
                    optimalThrowTable.get(i).set(j, 1);
                    x_Table.get(i).set(j, -1);
                } else if (i == 1) {
                    optimalThrowTable.get(i).set(j, j);
                    x_Table.get(i).set(j, 1);
                } else {
                    //Think of i as the pumpkins and j as the targets for this condition
                    for (int x = 1; x <= j; x++) {
                        smash = optimalThrowTable.get(i - 1).get(x - 1);
                        survive = optimalThrowTable.get(i).get(j - x);

                        if (smash >= survive) {
                            max = smash;
                            deathFlag = true;
                        } else {
                            max = survive;
                        }
                        if (max < min) {
                            min = max;
                            if (deathFlag) {
                                x_Table.get(i).set(j, x * -1);
                                deathFlag = false;
                            } else x_Table.get(i).set(j, x);
                            }
                            optimalThrowTable.get(i).set(j, 1 + min);
                        }
                    }
                min = Integer.MAX_VALUE;
                }
            }
    }

    public static void resize(ArrayList<ArrayList<Integer>> list, int arrays, int indices) {
        for(int i = 0; i < arrays; i++) {
            list.add(new ArrayList<Integer>());
            for(int j = 0; j < indices; j++) {
                list.get(i).add(0);
            }
        }
    }

    private ArrayList<Integer> traceIt(int p, int t){
        int offSet = 0;
        int survivalIndicator = 1;
        int optimalThrows = optimalThrowTable.get(p).get(t);
        for(int i = 0; i < optimalThrows; i++) {
            int val = x_Table.get(p).get(t);
            if (val > 0) { //She lives
                survivalIndicator = 1;
                t = t - Math.abs(val);

            } else { //She dies
                p -= 1;
                survivalIndicator = -1;
                t = Math.abs(val) -1;

            }
            tracer.add((Math.abs(val) + offSet) * survivalIndicator);
            if (val > 0) offSet += Math.abs(val);
        }
        return tracer;
    }

    public static void main(String[] args) {
        whatDoYouWantToRun(args);
    }

    private static void whatDoYouWantToRun(String[] args) {
        Trebuchet trebuchet = new Trebuchet();
        int pumpkins = Integer.parseInt(args[0]);
        int targets = Integer.parseInt(args[1]);
        Scanner input = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Pumpkins:\t" + pumpkins + "\ttargets:\t" + targets + "\nSelect dynamic (d) or recursive solution (r) or both (b) :)");
        String choice = input.nextLine();  // Read user input
        while (!choice.equalsIgnoreCase("r") && !choice.equalsIgnoreCase("d") && !choice.equalsIgnoreCase("b")){
            System.out.println("Enter an appropriate response");
            choice = input.nextLine();
            if (choice.equalsIgnoreCase("r") || choice.equalsIgnoreCase("d") || choice.equalsIgnoreCase("b")){
                break;
            }
        }
        if (choice.equalsIgnoreCase("r")) {
            recursive(trebuchet, pumpkins, targets);
        } else if (choice.equalsIgnoreCase("d")) {
            dynamic(trebuchet, pumpkins, targets);
        }
        else if (choice.equalsIgnoreCase("b")){
            recursive(trebuchet, pumpkins, targets);
            dynamic(trebuchet, pumpkins, targets);
        }
    }

    private static void recursive(Trebuchet trebuchet, int pumpkins, int targets) {
        System.out.println("******************RECURSIVE********************");
        System.out.println("*******************PROGRAM*********************");

        ArrayList<ArrayList<Integer>> recursiveArray = new ArrayList<>();
        long totalTime = 0;

        for (int p = 1; p <= pumpkins; p++) {
            recursiveArray.add(new ArrayList<>());
            for (int t = 0; t <= targets; t++) {
                trebuchet.calls = 0;
                long start = System.currentTimeMillis();
                recursiveArray.get(p - 1).add(trebuchet.recursiveSoln(p, t));
                long finish = System.currentTimeMillis();
                long timeElapsed = finish - start;
                totalTime += timeElapsed;
                System.out.println("Pumpkins:\t" + p + "\tTargets:\t" + t + "\tAnswer:\t" + recursiveArray.get(p - 1).get(t));
                System.out.println("Time Elapsed:\t" + timeElapsed + "ms\t\t\tCalls:\t" + trebuchet.calls);
//                System.out.println("**************************************************");
//                System.out.println("**************************************************");
            }
        }
        System.out.println("Total Time Elapsed for Recursive:\t" + totalTime + "ms\n\n");
        System.out.print("Targets: ->\t\t");
        for(int i = 0; i <= targets; i++){
            System.out.print( i + "\t");
        }
        System.out.println();
        for (int i = 0; i < recursiveArray.size(); i++) {
            System.out.print("Pumpkin: " + (1+i) + "\t\t");
            for (int j = 0; j < recursiveArray.get(i).size(); j++) {
                System.out.print(recursiveArray.get(i).get(j) + "\t");
            }
            System.out.println();
        }
        System.out.println("\n");
//        if (targets >= 21) return;  //testing
//        if (pumpkins >= 4) return;
//        for (int i = 4; i <= 12; i++){
//            System.out.println("**********************" + i + "**********************" + targets);
//            recursive(trebuchet, i, targets);
//        }
    }

    private static void dynamic(Trebuchet trebuchet, int pumpkins, int targets) {
        System.out.println("***************DYNAMIC******************");
        System.out.println("***************PROGRAM******************");
        long start = System.currentTimeMillis();
        long start2 = System.nanoTime();

        trebuchet.dpSoln(pumpkins, targets);
        int answer = trebuchet.optimalThrowTable.get(pumpkins).get(targets);
        long finish2 = System.nanoTime();
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        long timeElapsed2 = finish2 - start2;
        System.out.println("Pumpkins: " + pumpkins + " Targets: " + targets);
        System.out.println("Total Time Elapsed for Dynamic:\t" + timeElapsed + "ms\t\t<|><|><|>\t" + timeElapsed2 + "ns");
        System.out.println("The throws necessary is: " + answer);
        System.out.print("Targets -> \t\t");
        for(int i = 0; i <= targets; i++){
            System.out.print( i + "\t");
        }
        System.out.println();
        for (int p = 1; p <= pumpkins; p++) {
            System.out.print("Pumpkin: " + p + "\t\t");
            for (int t = 0; t <= targets; t++) {
                System.out.print(trebuchet.optimalThrowTable.get(p).get(t) + "\t");
            }
            System.out.println();
        }
//        if(pumpkins >= 4) return;
//        for(int i = 0; i <= 8; i++) {
//              pumpkins++;
////            targets += 1000;
//            dynamic(trebuchet, pumpkins, targets);
//        }
        System.out.println("\n\n***************X*****************");
        System.out.println("*************TABLE***************");
        System.out.print("Remaining T -> ");
        for(int i = 1; i <= targets; i++){
            System.out.print( "\t\t" + i);
        }
        System.out.println();
        for (int p = 1; p <= pumpkins; p++) {
            System.out.print("Pumpkin: " + p + "\t");
            for (int t = 1; t <= targets; t++) {
                System.out.print("\t\t" + trebuchet.x_Table.get(p).get(t));
            }
            System.out.println();
        }
        trebuchet.traceIt(pumpkins, targets);
        System.out.println("\n\n**********TRACER*****************");
        System.out.println("*************************************");
        for (var x : trebuchet.tracer){
            System.out.print(x + " ");
        }
    }
}