package de.diedavids.cuba.wizard.web.screens.sample.old.steps;

import de.diedavids.cuba.wizard.gui.components.AbstractWizardStep;

public class Example1Step1 extends AbstractWizardStep {

    boolean preCloseTrue = true;

    @Override
    public boolean preClose() {
        preCloseTrue = !preCloseTrue;
        return preCloseTrue;
    }

    public void buttonClick() {
        showNotification("Hello");

    }

    @Override
    public void onActivate() {
        showNotification("on activate step 1");
    }


}