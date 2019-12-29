import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Tuple{
  public int a;
  public int b;
  public Tuple(int x,int y){
    a = x;
    b = y;
  }
}

public class Similarity{
  private static Map<String, Integer> frequencies = new LinkedHashMap<String, Integer>();
  private static int size = 0;
  private static ArrayList<Tuple> indexes;
  private static int k = 10;//To set the size of substring


  // To map the frequency of all word present in the string and to return the integer array i of frequencies.
  public static ArrayList<Integer> mapFreq(String text){
    for (Map.Entry<String,Integer> entry : frequencies.entrySet())
    {
      entry.setValue(0);
    }
    String nonWordDelimiter="\\s+";
    text = text.replaceAll("\\p{P}","").toLowerCase();// To remove punctuations from the text
    String[] words = text.split(" ");
    ArrayList<Integer> i = new ArrayList<Integer>();
    for (String word : words) {
      if (!word.isEmpty()) {
        Integer frequency = frequencies.get(word);
        if (frequency == null) {
            frequency = 0;
        }
        ++frequency;
        frequencies.put(word, frequency);
      }
    }
    for (Map.Entry<String,Integer> entry : frequencies.entrySet()){
      i.add(entry.getValue());
    }
    size = i.size();
    return i;
  }

  //We know that cosine of two vector a,b is (a.b)/|a*b|
  private static double cosine(ArrayList<Integer> a, ArrayList<Integer> b){
   double dotProduct = DotProduct(a, b);
   double magnitudeOfA = Magnitude(a);
   double magnitudeOfB = Magnitude(b);
   return dotProduct/(magnitudeOfA*magnitudeOfB);
  }


  private static double DotProduct(ArrayList<Integer> a, ArrayList<Integer> b){
    double dotProduct = 0;
    int s = size;
    for (int i = 0; i < s; i++)
    {
      int aValue = i < a.size() ? a.get(i) : 0;
      int bValue = i < b.size() ? b.get(i) : 0;
      dotProduct += (aValue * bValue);
    }
    return dotProduct;
  }

  // Magnitude of the vector is the square root of the dot product of the vector with itself.
  private static double Magnitude(ArrayList<Integer> vector){
    return Math.sqrt(DotProduct(vector, vector));
  }

  //Shingles is map containing a set of character corresponding index.
  private static Map<Integer, Integer> shingles(String line){
    Map<Integer, Integer> map= new LinkedHashMap<Integer,Integer>();
    ArrayList<Integer> s = new ArrayList<Integer>();

    for(int i=0; i< line.length() - k +1; ++i){//k is length of String to be mapped
      String x = line.substring(i,i+k);
      map.put(x.hashCode(),i);

    }
    return map;
  }

  // To find Csetosine similarity
  public static double cosineSimilarity(String a, String b){
    ArrayList<Integer> m = mapFreq(a);
    ArrayList<Integer> n = mapFreq(b);
    return cosine(m,n);
  }


  //To find Jacard Similarity
  public static double jaccardSimilarity(String a, String b){
     
    Map<Integer, Integer> shinglesA= shingles(a);
    Map<Integer, Integer> shinglesB= shingles(b);
    // System.out.println("shinglesA "+shinglesA);
    // System.out.println("shinglesB "+shinglesB);

    Set<Integer> set1= new HashSet<Integer>(shinglesA.keySet());//Set1 will contain the intersection of both the shingles.
    Set<Integer> set2= new HashSet<Integer>(shinglesA.keySet());//Set2 will contain the union of both the shingles.
    set1.retainAll(shinglesB.keySet());//retainall = intersection
    set2.addAll(shinglesB.keySet());//addall = union
    // System.out.println("set1 "+set1);
    // System.out.println("set2 "+set2);

    Iterator itr = set1.iterator();
    Object first = itr.next();
    indexes = new ArrayList<Tuple>();
    while(itr.hasNext()){
      indexes.add(new Tuple(shinglesA.get(first),shinglesB.get(first)));
      first = itr.next();
    }
    
    return (double)set1.size() / (double)set2.size();
  }

  //To get similarity index of two files.
  public static ArrayList<Tuple> getCommonIndex(){
    return indexes;
  }
}
