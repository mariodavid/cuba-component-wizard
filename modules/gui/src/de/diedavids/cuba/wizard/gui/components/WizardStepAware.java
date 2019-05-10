package de.diedavids.cuba.wizard.gui.components;


/**
 * @deprecated is no longer used. Please reference AbstractWizardStep directly
 */
public interface WizardStepAware {

    void onActivate();

    boolean preClose();
}
