/**
 * NAME         :       JEET AGRAWAL
 * NSID         :       jea316
 * STUDENT ID  :       11269096
 */

import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;

public class PerformanceByRank{

    public static LinkedList280<Crew> readCrewData(String path) {
        Scanner infile = null;

        try {
            infile = new Scanner(new File(path));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: File not found!");
        }

        // Initialize output list.
        LinkedList280<Crew> pirateCrew = new LinkedList280<Crew>();

        // While there is more stuff to read...
        while(infile.hasNext()) {
            // Read the three values for a Crew record
            int rank = infile.nextInt();
            double pay = infile.nextDouble();
            int sacks = infile.nextInt();

            // Create a crew object from the data
            Crew c = new Crew(rank, pay, sacks);

            // Place the new Crew instnace in the linked list.
            pirateCrew.insertFirst(c);
        }

        // Close the input file like a good citizen. :)
        infile.close();

        // Return the list of Crew objects.
        return pirateCrew;
    }


    public static void main(String[] args) {
        // Read the data for Jack's pirate crew.

        // If you are getting a "File Not Found" error here, you may adjust the
        // path to piratecrew.txt as needed.
        LinkedList280<Crew> pirateCrew = readCrewData("/Users/jeetagrawal/Desktop/U OF S/WINTER 2020/CMPT 280/ASSIGNMENTS/ASSIGNMENT 2/A2/src/piratecrew.txt");

		// TODO Write your solution here.

        //Created an array of size 10 of type Crew.
        LinkedList280<Crew>[] piratesByRank = new LinkedList280[10];

        //Initialized each element of the array with the type LinkedList<Crew>.
        for (int i = 0; i < piratesByRank.length; i++){
            piratesByRank[i] = new LinkedList280<>();
        }


        for (int j = 0; j < piratesByRank.length; j++)
        {
            LinkedIterator280<Crew> iterator_1 = new LinkedIterator280<Crew>(pirateCrew);
            while (!iterator_1.after())
            {
                if (iterator_1.item().getRank() == j)
                    piratesByRank[j].insert(iterator_1.item());
                iterator_1.goForth();
            }
        }


        for (int i = 0; i < piratesByRank.length; i++){
            LinkedIterator280<Crew> iterator = new LinkedIterator280<Crew>(piratesByRank[i]);

            double sumPay = 0;
            double sumGrain = 0;

            int rank = iterator.item().rank;
            while (!iterator.after()){
                sumPay += iterator.item().pay;
                sumGrain += iterator.item().grainSacks;
                iterator.goForth();
            }
            System.out.println("Jack's rank "+rank+" crew members were paid of "+sumPay/sumGrain+" guineas per sack of grain plundered.");

        }
    }
}
