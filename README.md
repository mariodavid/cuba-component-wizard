CUBA component - Wizard
======================

I tried different stuff in order to create a Wizard DSL.


### Usage

        <wizard:wizard id="wizardPanel">
            <wizard:step caption="Step 1" screen="example-1-step-1-frame" />
            <wizard:step caption="Step 2" screen="example-1-step-2-frame" />
        </wizard:wizard>
        
       
### Current problems

The current problem is, that the frames are rendered, but not as part of the tabs but rather below the wizard component.

![wizard-component-result](https://github.com/mariodavid/cuba-component-wizard/blob/master/img/wizard-loader-result.png)
