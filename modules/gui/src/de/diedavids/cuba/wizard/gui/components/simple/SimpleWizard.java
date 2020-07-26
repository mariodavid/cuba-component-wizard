package de.diedavids.cuba.wizard.gui.components.simple;

import com.haulmont.cuba.gui.components.TabSheet;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardStepChangeListener;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import java.util.EventObject;

/**
 * Wizard component interface.
 */
public interface SimpleWizard extends TabSheet {

    String NAME = "simpleWizard";

    void nextStep();

    void previousStep();


    void addWizardStepChangeListener(WizardStepChangeListener listener);
    void removeWizardStepChangeListener(WizardStepChangeListener listener);


    class WizardStepChangeEvent extends EventObject {

        WizardStep prevStep;
        WizardStep step;

        public WizardStepChangeEvent(SimpleWizard source, WizardStep prevStep, WizardStep step) {
            super(source);
            this.prevStep = prevStep;
            this.step = step;
        }

        @Override
        public SimpleWizard getSource() {
            return (SimpleWizard) super.getSource();
        }

        public WizardStep getPrevStep() {
            return prevStep;
        }

        public WizardStep getStep() {
            return step;
        }
    }

    @FunctionalInterface
    interface WizardStepChangeListener extends java.util.function.Consumer<WizardStepChangeEvent> {
        void accept(WizardStepChangeEvent event);
    }


    void addWizardCancelClickListener(WizardCancelClickListener listener);
    void removeWizardCancelClickListener(WizardCancelClickListener listener);

    class WizardCancelClickEvent extends EventObject {
        public WizardCancelClickEvent(SimpleWizard source) {
            super(source);
        }

        @Override
        public SimpleWizard getSource() {
            return (SimpleWizard) super.getSource();
        }
    }

    @FunctionalInterface
    interface WizardCancelClickListener extends java.util.function.Consumer<WizardCancelClickEvent> {
        void accept(WizardCancelClickEvent event);
    }




    void addWizardFinishClickListener(WizardFinishClickListener listener);
    void removeWizardFinishClickListener(WizardFinishClickListener listener);

    class WizardFinishClickEvent extends EventObject {
        public WizardFinishClickEvent(SimpleWizard source) {
            super(source);
        }

        @Override
        public SimpleWizard getSource() {
            return (SimpleWizard) super.getSource();
        }
    }

    @FunctionalInterface
    interface WizardFinishClickListener extends java.util.function.Consumer<WizardFinishClickEvent> {
        void accept(WizardFinishClickEvent event);
    }
}