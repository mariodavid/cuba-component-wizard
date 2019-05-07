package de.diedavids.cuba.wizard.web.screens.examples.example1.steps;

import com.haulmont.cuba.gui.Notifications;
import de.diedavids.cuba.wizard.gui.components.AbstractWizardStep;

import javax.inject.Inject;

public class Example1Step4 extends AbstractWizardStep {

    @Inject
    private Notifications notifications;

    public void buttonClick() {
        notifications.create().withCaption("Hello 4").show();
    }

    @Override
    public void onActivate() {
        notifications
                .create()
                .withCaption("on activate step 4")
                .show();
    }
}