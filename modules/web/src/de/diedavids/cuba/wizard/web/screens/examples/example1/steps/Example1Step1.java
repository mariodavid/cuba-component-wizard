package de.diedavids.cuba.wizard.web.screens.examples.example1.steps;

import com.haulmont.cuba.gui.Notifications;
import de.diedavids.cuba.wizard.gui.components.AbstractWizardStep;

import javax.inject.Inject;

public class Example1Step1 extends AbstractWizardStep {

    boolean preCloseTrue = true;

    @Inject
    protected Notifications notifications;

    @Override
    public boolean preClose() {
        preCloseTrue = !preCloseTrue;
        return preCloseTrue;
    }

    public void buttonClick() {
        showNotification("Hello");

    }

    @Override
    public void onActivate() {
        showNotification("on activate step 1");
    }

    private void showNotification(String message) {
        notifications.create(Notifications.NotificationType.TRAY)
                .withCaption(message)
                .show();
    }


}