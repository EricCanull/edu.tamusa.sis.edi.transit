/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tamusa.sis.edi.transit;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author ericcanull
 */
public class TranscriptTextArea {

    private final Tab tab = new Tab();
    private final AnchorPane anchorPane = new AnchorPane();
    private final TextArea textArea = new TextArea();

    public TranscriptTextArea() {
        initialize();
    }

    private void initialize() {
        textArea.setEditable(false);
        setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        setAnchorConstraints();
        tab.setContent(anchorPane);
    }
    
   public Tab getTab() {
        return this.tab;
    }

    public TextArea getTranscriptEditor() {
        return this.textArea;
    }
    
    public void setFont(Font font) {
        this.textArea.setFont(font);
    }
    
    private void setAnchorConstraints() {
        AnchorPane.setTopAnchor(textArea, 0.0);
        AnchorPane.setBottomAnchor(textArea, 0.0);
        AnchorPane.setRightAnchor(textArea, 0.0);
        AnchorPane.setLeftAnchor(textArea, 0.0);
        anchorPane.getChildren().add(textArea);
    }
}
