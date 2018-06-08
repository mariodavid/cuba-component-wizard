package de.diedavids.cuba.wizard.web.screens.examples.example1.steps;

import com.haulmont.cuba.gui.components.AbstractFrame;
import de.diedavids.cuba.wizard.gui.components.WizardStepAware;

public class Example1Step2Frame extends AbstractFrame  implements WizardStepAware {
    public void buttonClick() {
        showNotification("Hello 2");
    }

    @Override
    public void onActivate() {
        showNotification("on activate step 2");
    }
    boolean preCloseTrue = true;
    @Override
    public boolean preClose() {
        preCloseTrue = !preCloseTrue;
        return preCloseTrue;
    }
}