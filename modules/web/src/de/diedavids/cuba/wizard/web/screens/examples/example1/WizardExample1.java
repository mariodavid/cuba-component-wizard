package de.diedavids.cuba.wizard.web.screens.examples.example1;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import de.diedavids.cuba.wizard.gui.components.WizardStepAware;

import javax.inject.Inject;

public class WizardExample1 extends AbstractWindow {


    @Inject
    protected Wizard wizard;


    @Inject
    protected ComponentsFactory componentsFactory;

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


        WizardStepAware wizardStepAware = (WizardStepAware) openFrame(null, "example-1-step-4-frame");


        WizardStep wizardStep = wizard.addStep(3, "Hello", wizardStepAware);
        wizardStep.setId("Hello");
        wizardStep.setCaption("Hello 123");
        wizardStep.setIcon("font-icon:ADN");
    }
}