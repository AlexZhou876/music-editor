package ui;

import java.util.Scanner;

public class Looping {
    static int num;
    static int pos;
    static int beatNum;
    static int beatType;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            if (in.hasNextInt())
                System.out.println(in.nextInt());
            else
                in.next();


        }
    }
}

