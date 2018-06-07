package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.cuba.gui.components.Component;

public interface WizardDelegate {
    String NAME = "ddcw_WizardDelegate";

    void addStep(int index, WizardStep wizardStep);




    Component.Container getTabSheetLayout();

}