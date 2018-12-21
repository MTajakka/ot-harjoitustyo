/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.ui;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

/**
 * A Label that when cliced on changes to a TectField which can be edited.
 * 
 * Method setOnFocusLost specifies an Event that is to be handeled on lost of focus. (May not work as EventHandler is supposed to work)
 * 
 */
public class ReWrite extends Pane {
    
    private Label output;
    private TextField input;
    private String text;
    private EventHandler<ActionEvent> eh;
    private boolean updateMode = false;

    /**
     * 
     * @param text Text to be displayed
     */
    public ReWrite(String text) {
        this.text = text;
        output = new Label(text);
        input = new TextField(text);
        input.setMinWidth(50);
        input.setPrefWidth(output.getText().length() * 7 + 20);
        input.setPadding(new Insets(2, 5, 2, 5));
        
        this.getChildren().add(output);
        output.setOnMouseClicked(eh -> {
            this.getChildren().remove(output);
            this.getChildren().add(input);
            input.requestFocus();
            updateMode = true;
        });
        
        input.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                update();
            }
        });
        
        input.setOnKeyPressed(eh -> {
            if (eh.getCode().equals(KeyCode.ENTER) && updateMode) {
                update();
                updateMode = false;
            }
        });
    }
    /**
     * Set event to be handeled on lost of focus
     * 
     */
    public void setOnFocusLost(EventHandler<ActionEvent> eh) {
        this.eh = eh;
    }
    
    private void update() {
        String inputText = input.getText();
        if (inputText.isEmpty()) {
            inputText = this.text;
        }
        this.text = inputText;
        output.setText(this.text);
        input.setText(inputText);
        this.getChildren().clear();
        this.getChildren().add(output);
        eh.handle(new ActionEvent());
    }
    

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
        input.setText(text);
        output.setText(text);
    }
}
