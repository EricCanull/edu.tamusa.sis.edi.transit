package edu.tamusa.sis.edi.transit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class CompareController implements Initializable {

  @FXML private TextArea inputTextArea, outputTextArea;

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    // TODO
    File file = new File("/Users/ericcanull/Desktop/TEST_without_N3");
    try {
      printFile(file, false);
      printFile(preprocessFileInput(file), true);

      inputTextArea.positionCaret(0);
      outputTextArea.positionCaret(0);
      inputTextArea.scrollTopProperty().bindBidirectional(outputTextArea.scrollTopProperty());

    } catch (FileNotFoundException ex) {
      Logger.getLogger(CompareController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(CompareController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public File preprocessFileInput(File file) throws IOException {
    final StringBuilder outputString = new StringBuilder();
    boolean validOrder = true;
    
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      for (String line; (line = br.readLine()) != null;) {
     
        if (line.substring(0, 2).equals("N1") 
                || line.substring(0, 2).equals("N3")) {
          outputString.append(line).append("\n");
          validOrder = true;

        } else if (line.substring(0, 2).equals("N4") && validOrder) {
          outputString.append(line).append("\n");
          validOrder = false;
        } else if (line.substring(0, 2).equals("N4") && !validOrder) {
//          outputString.append("N3|~\n");
//          outputString.append(line).append("\n");
        } else {
          outputString.append(line).append("\n");
        }
      }

    } catch (IOException ex) {
      Logger.getLogger(CompareController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return stream2file(new ByteArrayInputStream(outputString.toString().getBytes()));
  }

  public void printFile(File file, Boolean isOutput) throws FileNotFoundException, IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      int count = 0;
      for (String line; (line = br.readLine()) != null;) {
        if (isOutput) {
          System.out.println(line);
          outputTextArea.appendText(++count + ": " + line + "\n");
        } else {
          inputTextArea.appendText(++count + ": " + line + "\n");
        }
      }
    }
  }

  public File stream2file(InputStream initialStream) throws IOException {
    final File tempFile = File.createTempFile("stream2file", ".tmp");
    byte[] buffer = new byte[initialStream.available()];
    initialStream.read(buffer);
    tempFile.deleteOnExit();
    try (FileOutputStream out = new FileOutputStream(tempFile)) {
      out.write(buffer);
    }
    return tempFile;
  }
}
