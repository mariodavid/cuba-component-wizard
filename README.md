CUBA component - Wizard
======================

This application component let's you create UI wizards through a specific UI component DSL. 

A UI wizard should be used in case of:

* multi step input
* complex decision workflows
* the user needs to be guided through the process


For more information on this topic see: http://ui-patterns.com/patterns/Wizard

### Usage

        <wizard:wizard id="myWizard">
            <wizard:step caption="Step 1" screen="example-1-step-1" />
            <wizard:step caption="Step 2" screen="example-1-step-2" />
        </wizard:wizard>
        
