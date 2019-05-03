package de.diedavids.cuba.wizard.web.screens.examples.example1.steps;

import com.haulmont.cuba.gui.Notifications;
import de.diedavids.cuba.wizard.gui.components.AbstractWizardStep;

import javax.inject.Inject;

public class Example1Step2 extends AbstractWizardStep {
    @Inject
    private Notifications notifications;

    public void buttonClick() {
        notifications.create().withCaption("Hello 2").show();
    }

    @Override
    public void onActivate() {
        //showNotification("on activate step 2");
    }
}