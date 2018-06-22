package de.diedavids.cuba.wizard.web.screens.examples.example1;

import com.haulmont.cuba.gui.components.AbstractWindow;
import de.diedavids.cuba.wizard.gui.components.Wizard;

import javax.inject.Inject;

public class WizardExample1 extends AbstractWindow {


    @Inject
    protected Wizard wizard;

    @Override
    public void ready() {
        wizard.addWizardFinishClickListener(event -> {
            showNotification("finish clicked");
            close(COMMIT_ACTION_ID);
        });

        wizard.addWizardCancelClickListener(event -> {
            showNotification("cancel clicked");
            close(CLOSE_ACTION_ID);
        });

        wizard.addWizardStepChangeListener(event -> {
            showNotification("step changed from " +
                    event.getPrevStep().getId() + " to " +
                    event.getStep().getId());
        });
    }
}