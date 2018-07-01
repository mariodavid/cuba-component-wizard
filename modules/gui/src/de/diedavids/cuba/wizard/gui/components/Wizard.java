package de.diedavids.cuba.wizard.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;

import java.util.EventObject;

public interface Wizard extends Component.OrderedContainer,
                Component.HasIcon, Component.HasCaption {
    String NAME = "wizard";

    WizardStep addStep(int index, String name, WizardStepAware wizardStep);
    void addStep(int index, WizardStep wizardStep);

    WizardStep getStep(String stepId);

    void addWizardStepChangeListener(WizardStepChangeListener listener);
    void removeWizardStepChangeListener(WizardStepChangeListener listener);

    class WizardStepChangeEvent extends EventObject {

        WizardStep prevStep;
        WizardStep step;

        public WizardStepChangeEvent(Wizard source, WizardStep prevStep, WizardStep step) {
            super(source);
            this.prevStep = prevStep;
            this.step = step;
        }

        @Override
        public Wizard getSource() {
            return (Wizard) super.getSource();
        }

        public WizardStep getPrevStep() {
            return prevStep;
        }

        public WizardStep getStep() {
            return step;
        }
    }

    @FunctionalInterface
    interface WizardStepChangeListener {
        void stepChanged(Wizard.WizardStepChangeEvent event);
    }


    void addWizardCancelClickListener(WizardCancelClickListener listener);
    void removeWizardCancelClickListener(WizardCancelClickListener listener);

    class WizardCancelClickEvent extends EventObject {
        public WizardCancelClickEvent(Wizard source) {
            super(source);
        }

        @Override
        public Wizard getSource() {
            return (Wizard) super.getSource();
        }
    }

    @FunctionalInterface
    interface WizardCancelClickListener {
        void cancelClicked(Wizard.WizardCancelClickEvent event);
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