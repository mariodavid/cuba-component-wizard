package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.cuba.gui.components.AbstractFrame;

public class AbstractWizardStep extends AbstractFrame implements WizardStepAware {

    @Override
    public void onActivate() {
    }

    @Override
    public boolean preClose() {
        return true;
    }

    /**
     * This method must only be used in combination with preCloseNextClicked(). If preClose() is also implemented you might get some unwanted
     * behaviour because the wizard will still check preClose()
     *
     * @return Returns true, if it is allowed to step backwards
     */
    public boolean preClosePreviousClicked() {
        return true;
    }

    /**
     * This method must only be used in combination with preClosePreviousClicked(). If preClose() is also implemented you might get some unwanted
     * behaviour because the wizard will still check preClose()
     * @return Returns true, if it is allowed to step forward
     */
    public boolean preCloseNextClicked() {
        return true;
    }
}
