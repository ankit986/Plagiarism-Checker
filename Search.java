import java.util.*;
import java.lang.NullPointerException;

public class Search { 

  public static ArrayList<Tuple>[] Searching(ArrayList<Tuple> array) throws IndexOutOfBoundsException {
    System.out.println("array "+array);
    //We are getting unsorted array so here we are sorting the arrayList of tuple
    Comparator<Tuple> comparator = new Comparator<Tuple>() {
      public int compare(Tuple tupleA, Tuple tupleB) {
        return Integer.compare(tupleA.a, tupleB.a);
      }
    };
    Collections.sort(array, comparator);// to sort the unsorted array

    ArrayList<Tuple> outputarray1 = new ArrayList();
    ArrayList<Tuple> outputarray2 = new ArrayList();
    int j = 0;
    int k=9;
   
    int beg1 = array.get(0).a;//To start the array with first element
    int beg2 = array.get(0).b; 

    for (int i = 0; i < array.size() - 1; i++) {

      int d1 = array.get(i + 1).a - array.get(i).a; //Difference between two consecutive tuple
      int d2 = array.get(i + 1).b - array.get(i).b; //Difference between two consecutive tuple
     
      if (d1 == d2) {
        j += d1;
      }
      else {
        outputarray1.add(new Tuple(beg1, j + k)); //Adding k because we have taken k = 10 in Similarity.java
        outputarray2.add(new Tuple(beg2, j + k));

        j = 0;

        beg1 = array.get(i + 1).a + 1;
        beg2 = array.get(i + 1).b + 1;
      }
    }
    outputarray1.add(new Tuple(beg1, j + k));
    outputarray2.add(new Tuple(beg2, j + k));

    ArrayList<Tuple>[] ar = new ArrayList[2];// Sorted index array
    ar[0] = outputarray1;//for file 1
    ar[1] = outputarray2;//for file 2
    System.out.println("outputarr1  "+outputarray1);
    System.out.println("outputarr2  "+outputarray2);


    return ar;
  }
}
