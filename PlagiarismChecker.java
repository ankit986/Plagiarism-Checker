import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

import java.awt.Color;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.w3c.dom.Text;

import javax.swing.JOptionPane;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.lang.NullPointerException;
import java.util.ArrayList;
import java.util.Random;

public class PlagiarismChecker {
  public static void main(String[] args) throws FileNotFoundException {
    Runnable r = new Runnable() {
      @Override
      public void run() {
        new PlagiarismChecker().createUI();
      }
    };
    EventQueue.invokeLater(r);
  }

  private static File file2;// For first file
  private static File file1; // For second file1

  private void createUI() {
    final JFrame fileChooserFrame = new JFrame();
    final JFrame comparisonFrame = new JFrame();
    // fileChooserFrame.setLayout(new BorderLayout(100, 300));
    // comparisonFrame.setLayout(new BorderLayout());
    JButton openBtn1 = new JButton("Open File 1");
    JButton openBtn2 = new JButton("Open File 2");
    JButton check = new JButton("Check Plagiarism");
    JButton clearTextField = new JButton("Clear");
    JButton selectFile = new JButton("Select Another File");
    final JLabel input = new JLabel("");//,JLabel.CENTER);
    JTextArea textArea1 = new JTextArea(30, 50);
    textArea1.setWrapStyleWord(true);
    textArea1.setLineWrap(true);
    textArea1.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea1);
    JTextArea textArea2 = new JTextArea(30, 50);
    textArea2.setWrapStyleWord(true);
    textArea2.setLineWrap(true);
    textArea2.setEditable(false);
    JScrollPane scrollPane1 = new JScrollPane(textArea2);

    openBtn1.setSize(400,400);

    // Button 1 Action: Taking file2 from user and setting it to text area1.
    openBtn1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser openFile1 = new JFileChooser();
        openFile1.showOpenDialog(null);
        file1 = openFile1.getSelectedFile();

        try {
          Scanner sc = new Scanner(file1);
          sc.useDelimiter("\\Z");
          String s = sc.next();
          textArea1.setText(s);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }

      }
    });

    // Button 2 Action: Taking file1 from user and setting it to text area 2.
    openBtn2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser openFile = new JFileChooser();
        openFile.showOpenDialog(null);
        file2 = openFile.getSelectedFile();

        try {
          Scanner sc = new Scanner(file2);
          sc.useDelimiter("\\Z");
          String s = sc.next();
          textArea2.setText(s);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }

      }
    });

    // Check Button to Perform the main task that is to compare two file and
    // highlight the similar part.
    check.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        if (file1 == null || file2 == null) {
          JOptionPane.showMessageDialog(fileChooserFrame, "You have to add both files before ");
        } else {
          fileChooserFrame.setVisible(false);// closing fileChooserFrame
          comparisonFrame.setVisible(true); // opening comparisonFrame

          Scanner scanner;
          String stringFile1 = "", stringFile2 = "";
          try {
            scanner = new Scanner(file2).useDelimiter("\\Z");
            stringFile1 = scanner.next();
          } catch (FileNotFoundException e) {
          }

          try {
            scanner = new Scanner(file1).useDelimiter("\\Z");
            stringFile2 = scanner.next();
          } catch (FileNotFoundException e) {
          }

          // Finding Mathematical Similarity between two strings
          double cosSim = Similarity.cosineSimilarity(stringFile1, stringFile2);
          String cosSimilarityValue = String.valueOf(cosSim * 100);
          double jacSim = Similarity.jaccardSimilarity(stringFile1, stringFile2);
          String jacSimilarityValue = String.valueOf(jacSim * 100);
          input.setText("Similarity by Term Frequency: " + cosSimilarityValue + "    " + "Semantic Similarity: "
              + jacSimilarityValue);

          // Highlighting Stuffs
          ArrayList<Tuple> indexes = Similarity.getCommonIndex(); // Taking the unsorted index(arrayList of tuple) of
                                                                  // Similar string.
          ArrayList<Tuple>[] highlightingPartList = new ArrayList[2];// Taking size 2 because we have two files.
          try {
            highlightingPartList = Search.Searching(indexes); // Passing index to get the two arrays, one for each file
            // and each file contaning common tuple of indexes.
            // System.out.println(highlightingPartList);
          } catch (IndexOutOfBoundsException o) {
          } catch (NullPointerException p) {
          }

          Highlighter highlighting1 = textArea1.getHighlighter();
          Random random = new Random(); // To generate random number for different random color
          HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(
              new Color(random.nextInt(245), random.nextInt(245), random.nextInt(245))); // setting values to 240 so
                                                                                         // that it never gets too
                                                                                         // light.
          Highlighter highlighting2 = textArea2.getHighlighter();
          int isize = highlightingPartList[0].size();
          
          try {
            for (int i = 0; i < isize; ++i) {
              // System.out.println("h1  "+highlightingPartList[1].get(i).a);
              highlighting1.addHighlight(highlightingPartList[1].get(i).a,
                  highlightingPartList[1].get(i).a + highlightingPartList[1].get(i).b, painter);
              highlighting2.addHighlight(highlightingPartList[0].get(i).a,
                  highlightingPartList[0].get(i).a + highlightingPartList[0].get(i).b, painter);
              painter = new DefaultHighlighter.DefaultHighlightPainter(
                  new Color(random.nextInt(245), random.nextInt(245), random.nextInt(240))); // To take another color.
            }
          } catch (BadLocationException e) {
          }
        }
      }
    });

    clearTextField.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0){
          textArea1.setText("");
          textArea2.setText("");
      }
    });

    selectFile.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0){
        fileChooserFrame.setVisible(true);
        file1=null;
        file2=null; 
      }
    });

    fileChooserFrame.add(new JLabel("File Chooser"), BorderLayout.NORTH);
    fileChooserFrame.add(openBtn1, BorderLayout.WEST);
    fileChooserFrame.add(openBtn2, BorderLayout.EAST);
    fileChooserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    fileChooserFrame.add(check, BorderLayout.SOUTH);
    comparisonFrame.add(clearTextField, BorderLayout.CENTER);
    comparisonFrame.add(selectFile, BorderLayout.SOUTH);
    comparisonFrame.add(input, BorderLayout.NORTH);
    comparisonFrame.add(scrollPane, BorderLayout.WEST);
    comparisonFrame.add(scrollPane1, BorderLayout.EAST);
    fileChooserFrame.setTitle("File Chooser");
    fileChooserFrame.pack();
    fileChooserFrame.setTitle("Similarity");
    comparisonFrame.pack();
    comparisonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    fileChooserFrame.setVisible(true);
    comparisonFrame.setVisible(false);
  }
}
