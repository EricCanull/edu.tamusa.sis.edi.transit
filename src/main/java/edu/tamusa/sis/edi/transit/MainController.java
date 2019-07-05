/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tamusa.sis.edi.transit;

import edu.tamusa.sis.edi.transit.util.SelectionModelBidirectionalBinding;
import edu.tamusa.sis.edi.model.EduDoc;
import edu.tamusa.sis.edi.model.FileEnvelope;
import edu.tamusa.sis.edi.model.Transcript;
import edu.tamusa.sis.edi.transit.MainController.StudentName;
import edu.tamusa.sis.edi.translator.TranslateTranscriptToText;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

/**
 * FXML Controller class
 *
 * @author ericcanull
 */
public class MainController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    @FXML private TextArea documentTextArea;
    @FXML private VBox window;
    @FXML private GridPane sideMenuPane;
    @FXML private TableView tableView;
    @FXML private TableColumn nameColumn;
    @FXML private ChoiceBox cb;
    @FXML private TabPane tabPane;
    private AlertDialog alert;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        BasicConfigurator.configure();
        // PropertyConfigurator.configure("/log4j.properties");
        cb.setItems(FXCollections.observableArrayList("Print Transcript ",
                new Separator(), "Generate Acknowledgement", "Generate Textfile")
        );

        cb.getSelectionModel().selectFirst();
        alert = new AlertDialog();
        
        tableView.getSelectionModel().selectedIndexProperty().addListener((ov, oldCell, newCell) -> {
            tabPane.getSelectionModel().select(newCell.intValue());
        });
        
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab != null) {
                Platform.runLater(() -> {
                    if (tabPane.getSelectionModel().getSelectedIndex() != -1) {
                        tableView.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                    }
                });
            }
        });
        
//        List<File> files = new ArrayList<>();
//        URL urlLoader = MainController.class.getResource("test/TS130.txt");
//        String loaderDir = urlLoader.getPath();
//
//        System.out.printf("loaderDir = %s\n", loaderDir);
//        File file = new File(getClass().getResource("/test/TS130.txt").getFile());
//        File file = new File(urlLoader.getPath());
//        files.add(file);
//        createRecords(files);
     
    }
        
    private void createRecords(List<File> files) {
        
        files.forEach((file) -> {
           
            try (FileInputStream fileInStream = new FileInputStream(preprocessFileInput(file))) {
                FileEnvelope fileEnvelope = new FileEnvelope(fileInStream);
                String transcriptText;

                Iterator iterator = fileEnvelope.getEduDocs().listIterator();
                while (iterator.hasNext()) {
                    EduDoc doc = (EduDoc) iterator.next();
                    Transcript transcript = ((Transcript) doc);
                    transcript.setTranslator(getTranslator(doc));
                    transcriptText = transcript.getTranslator().translate();
                    
                    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                    StudentName name = new StudentName(transcript.getStudent().getName());
                    tableView.getItems().add(name);
                    tableView.getSelectionModel().select(files.indexOf(file));
                   
                    TranscriptTextArea tab = new TranscriptTextArea();
                    tab.getTab().setText(transcript.getStudent().getName());
                    tab.getTranscriptEditor().appendText(transcriptText);
                    tab.getTranscriptEditor().positionCaret(0);
                    tabPane.getTabs().add(tab.getTab());
                    tabPane.getSelectionModel().select(tab.getTab());
                    
                    tab.getTab().setOnCloseRequest((Event arg0) -> {
                        tableView.getItems().remove(tabPane.getSelectionModel().getSelectedIndex());
                    });                  
                }

            } catch (FileNotFoundException ex) {
                alert.showAlert(AlertType.ERROR, "File Exception", "File Not Found", "Unable to find the file specified.", ex);
                logger.error("OpenFileAction - File not found in open action\n" + ex.toString());
            } catch (Exception ex) {
                alert.showAlert(AlertType.ERROR, "File Exception", "Parsing Error", "Unable to parse the specified file.", ex);
                System.out.println(ex.toString());
                logger.error("OpenFileAction - Error in Parsing\n" + ex.toString());
            }
        });
    }
    
    public TranslateTranscriptToText getTranslator(EduDoc doc) throws Exception {
        TranslateTranscriptToText translator = new TranslateTranscriptToText(doc);
        translator.setPageBreak("<End of Page>" + translator.getDefaultEndOfLine());
        translator.setShowRapInfo(true);
        translator.setPrintDuplex(false);
        translator.setSsnShowInfo("show");
        translator.setOverrideInstInfo(true);
        translator.setCharactersPerLine(90);
        translator.setLinesPerPage(66);

        return translator;
    }

    public File preprocessFileInput(File file) throws IOException {
        final StringBuilder outputString = new StringBuilder();
        boolean validOrder = true;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for (String line; (line = br.readLine()) != null;) {

                if (line.trim().equals("") || line.isEmpty()) {

                } else if (line.substring(0, 2).equals("N1")
                        || line.substring(0, 2).equals("N3")) {
                    outputString.append(line).append("\n");
                    validOrder = true;

                } else if (line.substring(0, 2).equals("N4") && validOrder) {
                    outputString.append(line).append("\n");
                    validOrder = false;
                } else if (line.substring(0, 2).equals("N4") && !validOrder) {
//                    outputString.append("N3|~\n");
//                    outputString.append(line).append("\n");
                } else {
                    outputString.append(line).append("\n");
                }
            }

        } catch (IOException ex) {
            logger.fatal(ex);
        }
        return stream2file(new ByteArrayInputStream(outputString.toString().getBytes()));
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
    
    public static <T> void bindBidirectional(Property<T> pProperty,
            SelectionModel<T> pSelectionModel, boolean pInitialValueFromSelection) {

        SelectionModelBidirectionalBinding<T> binding = new SelectionModelBidirectionalBinding<>(
                pProperty, pSelectionModel);
        if (pInitialValueFromSelection) {
            pProperty.setValue(pSelectionModel.getSelectedItem());
        } else {
            pSelectionModel.select(pProperty.getValue());
        }
        pProperty.addListener(binding);
        pSelectionModel.selectedItemProperty().addListener(binding);
    }

    @FXML
    private void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        List<File> files = fileChooser.showOpenMultipleDialog(window.getScene().getWindow());
        createRecords(files);
    }

    private void saveFileAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(window.getScene().getWindow());
        if (file != null && !file.exists() && file.getParentFile().isDirectory()) {
          //  createRecords(file);
            try (FileWriter writer = new FileWriter(file)) {
                //  writer.write(code);
                // displayStatusAlert("Code saved to " + file.getAbsolutePath());
                writer.flush();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.ERROR, null, ex);
            }
        }
    }

    @FXML private void closeApplicationAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
    
    @FXML private void closeCurrentTab(ActionEvent event) {
        tabPane.getTabs().remove(tabPane.selectionModelProperty().getValue().getSelectedItem());
        tableView.getItems().remove(tableView.getSelectionModel().getSelectedItem());
    }
    
    @FXML private void closeAllTabs(ActionEvent event) {
        tabPane.getTabs().removeAll(tabPane.getTabs());
        tableView.getItems().removeAll(tableView.getItems());
    }

    @FXML private void printDocument(ActionEvent event) {
    }

    @FXML private void onPrintAcknowledgementButton(ActionEvent event) {
    }

    public class StudentName {

        private final ObjectProperty<String> name = new SimpleObjectProperty();
        
        public StudentName(String name) {
            this.name.set(name);
        }

        public StudentName(Object o) {
            if (o instanceof Transcript) {
                this.name.set(((Transcript) o).getStudent().getName());
            }
        }

        public String getName() {
            return name.getValue();
        }
        
        public ObjectProperty<String> getBean() {
            return this.name;
        }
    }
}
