package de.diedavids.cuba.wizard.web.screens.examples.example1.steps;

import com.haulmont.cuba.gui.components.AbstractFrame;

public class Example1Step1Frame extends AbstractFrame {
    public void buttonClick() {
        showNotification("hello");
    }
}