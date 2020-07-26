package de.diedavids.cuba.wizard.web.screens.sample.old;

import com.haulmont.cuba.gui.Notifications;
import de.diedavids.cuba.wizard.gui.components.AbstractWizardStep;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import de.diedavids.cuba.wizard.web.screens.AbstractWizard;
import javax.inject.Inject;

public class WizardOld extends AbstractWizard {

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


        AbstractWizardStep screenFragment = createStep("example-1-step-4-frame");

        WizardStep wizardStep = wizard.addStep(3, "step4", screenFragment);
        wizardStep.setId("step4");
        wizardStep.setCaption("Step 4");
        wizardStep.setIcon("font-icon:ADN");


    }

    public void removeStep() {
        wizard.removeStep("step4");
    }
}