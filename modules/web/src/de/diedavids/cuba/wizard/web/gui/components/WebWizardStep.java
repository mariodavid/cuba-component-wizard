package de.diedavids.cuba.wizard.web.gui.components;

import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import de.diedavids.cuba.wizard.gui.components.WizardStepAware;

public class WebWizardStep extends WebVBoxLayout implements WizardStep {
    public WebWizardStep() {
    }

    @Override
    public void onActivate() {
        getWizardStepAware().onActivate();
    }


    @Override
    public boolean preClose() {
        return getWizardStepAware().preClose();
    }



    private WizardStepAware getWizardStepAware() {
        return (WizardStepAware) ownComponents.get(0);
    }
}