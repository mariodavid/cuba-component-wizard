package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;

import java.util.EventObject;

public interface Wizard extends Component.OrderedContainer,
                Component.HasIcon, Component.HasCaption {
    String NAME = "wizard";

    void addStep(int index, WizardStep wizardStep);

    WizardStep getStep(String stepId);

    void addWizardStepChangeListener(WizardStepChangeListener listener);
    void removeWizardStepChangeListener(WizardStepChangeListener listener);

    class WizardStepChangeEvent extends EventObject {
        public WizardStepChangeEvent(Wizard source) {
            super(source);
        }

        @Override
        public Wizard getSource() {
            return (Wizard) super.getSource();
        }
    }

    @FunctionalInterface
    interface WizardStepChangeListener {
        void stepChanged(Wizard.WizardStepChangeEvent event);
    }


    void addWizardFinishClickListener(WizardFinishClickListener listener);
    void removeWizardFinishClickListener(WizardFinishClickListener listener);

    class WizardFinishClickEvent extends EventObject {
        public WizardFinishClickEvent(Wizard source) {
            super(source);
        }

        @Override
        public Wizard getSource() {
            return (Wizard) super.getSource();
        }
    }

    @FunctionalInterface
    interface WizardFinishClickListener {
        void finishClicked(Wizard.WizardFinishClickEvent event);
    }
}