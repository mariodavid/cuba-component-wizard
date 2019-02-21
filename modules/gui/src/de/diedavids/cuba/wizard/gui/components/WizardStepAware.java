package de.diedavids.cuba.wizard.gui.components;

public interface WizardStepAware {

    void onActivate();

    boolean preClose();
}
