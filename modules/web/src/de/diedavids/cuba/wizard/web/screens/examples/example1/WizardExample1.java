package de.diedavids.cuba.wizard.web.screens.examples.example1;

import com.haulmont.cuba.gui.Fragments;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import de.diedavids.cuba.wizard.gui.components.AbstractWizardStep;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import de.diedavids.cuba.wizard.gui.components.WizardStepAware;
import de.diedavids.cuba.wizard.web.screens.examples.example1.steps.Example1Step4;

import javax.inject.Inject;

public class WizardExample1 extends AbstractWindow {

    @Inject
    protected Wizard wizard;

    @Inject
    protected Notifications notifications;

    @Override
    public void ready() {
        wizard.addWizardFinishClickListener(event -> {
            notifications.create(Notifications.NotificationType.TRAY)
            .withCaption("finish clicked")
            .show();
            close(COMMIT_ACTION_ID);
        });

        wizard.addWizardCancelClickListener(event -> {
            notifications.create(Notifications.NotificationType.TRAY)
                    .withCaption("cancel clicked")
                    .show();
            close(CLOSE_ACTION_ID);
        });

        wizard.addWizardStepChangeListener(event -> {
            notifications.create(Notifications.NotificationType.TRAY)
                    .withCaption("step changed from " +
                            event.getPrevStep().getId() + " to " +
                            event.getStep().getId())
                    .show();
        });


        AbstractWizardStep screenFragment = (AbstractWizardStep) openFrame(null, "example-1-step-4-frame");


        WizardStep wizardStep = wizard.addStep(3, "step4", screenFragment);
        wizardStep.setId("step4");
        wizardStep.setCaption("Step 4");
        wizardStep.setIcon("font-icon:ADN");


    }

    public void removeStep() {
        wizard.removeStep("step4");
    }
}