package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.components.VBoxLayout;

public interface WizardStep extends VBoxLayout {
    String NAME = "step";

    void onActivate();

    boolean preClose();

    boolean preClosePreviousClicked();

    boolean preCloseNextClicked();

    void setWrapperComponent(TabSheet.Tab tabComponent);

}