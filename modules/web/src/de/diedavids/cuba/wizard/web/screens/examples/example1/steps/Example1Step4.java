package de.diedavids.cuba.wizard.web.screens.examples.example1.steps;

import com.haulmont.cuba.gui.Notifications;
import de.diedavids.cuba.wizard.gui.components.AbstractWizardStep;

import javax.inject.Inject;

public class Example1Step4 extends AbstractWizardStep {

    public void buttonClick() {
        showNotification("Hello 4");
    }

    @Override
    public void onActivate() {
        showNotification("on activate step 4");
    }
}