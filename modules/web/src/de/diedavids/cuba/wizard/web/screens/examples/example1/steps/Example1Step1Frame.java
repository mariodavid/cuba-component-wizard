package de.diedavids.cuba.wizard.web.screens.examples.example1.steps;

import com.haulmont.cuba.gui.components.AbstractFrame;
import de.diedavids.cuba.wizard.gui.components.WizardStepAware;

public class Example1Step1Frame extends AbstractFrame implements WizardStepAware {

    boolean preCloseTrue = true;
    @Override
    public boolean preClose() {
        preCloseTrue = !preCloseTrue;
        return preCloseTrue;
    }
    public void buttonClick() {
        showNotification("hello");
    }

    @Override
    public void onActivate() {
        showNotification("on activate step 1");
    }


}