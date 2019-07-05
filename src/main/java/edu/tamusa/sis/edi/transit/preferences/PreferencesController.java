/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tamusa.sis.edi.transit.preferences;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 *
 * @author ericcanull
 */
public class PreferencesController extends VBox {
    
    @FXML ToggleGroup toggleGroupSSN = new ToggleGroup();
    
    SimpleDoubleProperty fontSize = new SimpleDoubleProperty(13.0);
    
    SimpleBooleanProperty showRap = new SimpleBooleanProperty(true);
    
    SimpleBooleanProperty displayInstitute = new SimpleBooleanProperty(true);
    
    SimpleBooleanProperty duplex = new SimpleBooleanProperty(true);
     
    
    public PreferencesController() {
        init();
    }

    private void init() {

    }
    
}
