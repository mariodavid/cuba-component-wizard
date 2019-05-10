package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.cuba.gui.components.AbstractFrame;

public class AbstractWizardStep extends AbstractFrame implements WizardStepAware {

    @Override
    public void onActivate() { }

    @Override
    public boolean preClose() {
        return true;
    }
}
