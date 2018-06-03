package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.cuba.gui.components.Component;

public interface Wizard extends Component.OrderedContainer {
    String NAME = "wizard";

    void addStep(WizardStep wizardStep);

    WizardStep getStep(String stepId);
}