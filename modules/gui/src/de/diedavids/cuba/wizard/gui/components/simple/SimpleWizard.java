package de.diedavids.cuba.wizard.gui.components.simple;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.TabSheet;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * Wizard component interface.
 */
public interface SimpleWizard extends TabSheet {

    String NAME = "simpleWizard";

    void nextStep();

    void previousStep();

    enum Direction {
        NEXT,
        PREVIOUS
    }


    /**
     * Add a listener that will be notified when a step change happened
     */
    Subscription addWizardStepChangeListener(Consumer<WizardStepChangeEvent> listener);
    void removeWizardStepChangeListener(Consumer<WizardStepChangeEvent> listener);

    /**
     * Add a listener that will be notified when a step is going to be changed
     */
    Subscription addWizardStepPreChangeListener(Consumer<WizardStepPreChangeEvent> listener);
    void removeWizardStepPreChangeListener(Consumer<WizardStepPreChangeEvent> listener);


    class WizardStepChangeEvent extends EventObject {

        TabSheet.Tab prevStep;
        TabSheet.Tab step;
        Direction direction;

        public WizardStepChangeEvent(SimpleWizard source, TabSheet.Tab prevStep, TabSheet.Tab step, Direction direction) {
            super(source);
            this.prevStep = prevStep;
            this.step = step;
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }

        @Override
        public SimpleWizard getSource() {
            return (SimpleWizard) super.getSource();
        }

        public TabSheet.Tab getPrevStep() {
            return prevStep;
        }

        public TabSheet.Tab getStep() {
            return step;
        }
    }

    class WizardStepPreChangeEvent extends EventObject {

        TabSheet.Tab prevStep;
        TabSheet.Tab step;

        Direction direction;

        private boolean stepChangePrevented = false;

        public WizardStepPreChangeEvent(SimpleWizard source, TabSheet.Tab prevStep, TabSheet.Tab step, Direction direction) {
            super(source);
            this.prevStep = prevStep;
            this.step = step;
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }
        @Override
        public SimpleWizard getSource() {
            return (SimpleWizard) super.getSource();
        }

        public TabSheet.Tab getPrevStep() {
            return prevStep;
        }

        public TabSheet.Tab getStep() {
            return step;
        }


        /**
         * Invoke this method if you want to abort the commit.
         */
        public void preventStepChange() {
            stepChangePrevented = true;
        }

        /**
         * Returns true if {@link #preventStepChange()} method was called and commit will be aborted.
         */
        public boolean isStepChangePrevented() {
            return stepChangePrevented;
        }
    }


    /**
     * Add a listener that will be notified when a wizards cancel operation is performed
     */
    Subscription addWizardCancelClickListener(Consumer<WizardCancelClickEvent> listener);
    void removeWizardCancelClickListener(Consumer<WizardCancelClickEvent> listener);

    class WizardCancelClickEvent extends EventObject {
        public WizardCancelClickEvent(SimpleWizard source) {
            super(source);
        }

        @Override
        public SimpleWizard getSource() {
            return (SimpleWizard) super.getSource();
        }
    }


    /**
     * Add a listener that will be notified when a wizards finish operation is performed
     */
    Subscription addWizardFinishClickListener(Consumer<WizardFinishClickEvent> listener);
    void removeWizardFinishClickListener(Consumer<WizardFinishClickEvent> listener);

    class WizardFinishClickEvent extends EventObject {
        public WizardFinishClickEvent(SimpleWizard source) {
            super(source);
        }

        @Override
        public SimpleWizard getSource() {
            return (SimpleWizard) super.getSource();
        }
    }
}