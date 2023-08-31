import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class PerformanceBy {
    @SuppressWarnings("unchecked")


    public static void main(String[] args) {

        //Created an array of size 10 of type Crew.
        LinkedList280<Crew>[] piratesByRank = new LinkedList280[10];

        //Initialized each element of the array with the type LinkedList<Crew>.
        for (int i = 0; i < piratesByRank.length; i++){
            piratesByRank[i] = new LinkedList280<>();
        }
        //FOR READING THE FILE .
        Scanner infile = null;
        try {
            infile = new Scanner(new File("/Users/jeetagrawal/Desktop/U OF S/WINTER 2020/CMPT 280/ASSIGNMENTS/ASSIGNMENT 2/A2/src/piratecrew.txt"));
        }
        catch (FileNotFoundException e){
            System.out.println("Error : FILE NOT FOUND.");
            return;
        }

        while (infile.hasNextLine()){
            String i = infile.nextLine();
            String[] iarr = i.split(" ", -1);
            Crew obj = new Crew(Integer.parseInt(iarr[0]),Double.parseDouble(iarr[1]),Integer.parseInt(iarr[2]));

            if ( obj.getRank() == 0)
                piratesByRank[0].insert(obj);
            else if (obj.getRank() == 1)
                piratesByRank[1].insert(obj);
            else if (obj.getRank() == 2)
                piratesByRank[2].insert(obj);
            else if (obj.getRank() == 3)
                piratesByRank[3].insert(obj);
            else if (obj.getRank() == 4)
                piratesByRank[4].insert(obj);
            else if (obj.getRank() == 5)
                piratesByRank[5].insert(obj);
            else if (obj.getRank() == 6)
                piratesByRank[6].insert(obj);
            else if (obj.getRank() == 7)
                piratesByRank[7].insert(obj);
            else if (obj.getRank() == 8)
                piratesByRank[8].insert(obj);
            else if (obj.getRank() == 9)
                piratesByRank[9].insert(obj);
        }

        for ( int i = 0; i < piratesByRank.length; i++){
            LinkedIterator280<Crew> iterator = new LinkedIterator280<Crew>(piratesByRank[i]);

            double sum = 0;
            int rank = iterator.item().rank;
            while (!iterator.after()){
                if (iterator.item().grainSacks != 0){
                    sum += iterator.item().pay/iterator.item().grainSacks;
                }
            iterator.goForth();
            }
            System.out.println("Jack's rank "+rank+" crew members were paid of "+sum+" guineas per sack of grain plundered..");
        }
    }
}
