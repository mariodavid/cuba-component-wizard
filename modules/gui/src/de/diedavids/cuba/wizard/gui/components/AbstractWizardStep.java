package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.cuba.gui.screen.ScreenFragment;

public class AbstractWizardStep extends ScreenFragment implements WizardStepAware {

    @Override
    public void onActivate() { }

    @Override
    public boolean preClose() {
        return true;
    }
}
