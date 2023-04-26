package edu.gcc.comp350.team4project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClassListRead {

    public static void main(String[] args) {
        ClassListRead c = new ClassListRead();
        c.ReadTextFile("Accounting");
    }

    public void ReadTextFile(String major) {
        ArrayList<String> classes = new ArrayList<String>();
        File file = new File("GCC Major Reqs/" + major + ".txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                scanner.nextLine();
                classes.add(line);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < classes.size(); i++) {
            System.out.println(classes.get(i));
        }
    }
}
