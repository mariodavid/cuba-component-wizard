package de.diedavids.cuba.wizard.web.screens.examples.example1;

import com.haulmont.cuba.gui.components.AbstractWindow;
import de.diedavids.cuba.wizard.gui.components.Wizard;

import javax.inject.Inject;

public class WizardExample1 extends AbstractWindow {


    @Inject
    protected Wizard wizardPanel;

    @Override
    public void ready() {
        wizardPanel.addWizardFinishClickListener(event -> {
            showNotification("finish clicked");
            close(COMMIT_ACTION_ID);
        });
    }
}