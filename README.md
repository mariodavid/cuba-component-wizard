[![Build Status](https://travis-ci.org/mariodavid/cuba-component-wizard.svg?branch=master)](https://travis-ci.org/mariodavid/cuba-component-wizard)
[ ![Download](https://api.bintray.com/packages/mariodavid/cuba-components/cuba-component-wizard/images/download.svg) ](https://bintray.com/mariodavid/cuba-components/cuba-component-wizard/_latestVersion)
[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

CUBA component - Wizard
======================

This application component let's you create UI wizards through a specific UI component DSL. 

![checkout-wizard-step-1](https://github.com/mariodavid/cuba-component-wizard/blob/master/img/checkout-wizard-step-1.png)


A UI wizard should be used in case of:

* multi step input
* complex decision workflows
* the user needs to be guided through the process


For more information on this topic see: http://ui-patterns.com/patterns/Wizard


## Installation


1. `wizard` is available in the [CUBA marketplace](https://www.cuba-platform.com/marketplace/wizard)
2. Select a version of the add-on which is compatible with the platform version used in your project:

| Platform Version | Add-on Version |
| ---------------- | -------------- |
| 7.2.x            | 0.6.x - 0.8.x  |
| 7.1.x            | 0.5.x          |
| 7.0.x            | 0.4.x          |
| 6.10.x           | 0.3.x          |
| 6.9.x            | 0.2.x          |
| 6.8.x            | 0.1.x          |


The latest version is: [ ![Download](https://api.bintray.com/packages/mariodavid/cuba-components/cuba-component-wizard/images/download.svg) ](https://bintray.com/mariodavid/cuba-components/cuba-component-wizard/_latestVersion)

Add custom application component to your project:

* Artifact group: `de.diedavids.cuba.wizard`
* Artifact name: `wizard-global`
* Version: *add-on version*

```groovy
dependencies {
  appComponent("de.diedavids.cuba.wizard:wizard-global:*addon-version*")
}
```


## Using the application component

Add a XML namespace `wizard` to the window tag of your screen like this:

```xml
    <window xmlns="http://schemas.haulmont.com/cuba/window.xsd"
        xmlns:wizard="http://schemas.diedavids.de/wizard/0.2/wizard-component.xsd">
```

Then add your wizard component to the screen:

```xml
<wizard:wizard id="wizard">
    <wizard:tab
      id="step1Tab"
      caption="msg://step1"
      icon="font-icon:ADN"
      spacing="true"
      margin="true">
        <button id="checkBtn" icon="font-icon:CHECK" />
    </wizard:tab>
    <wizard:tab
      id="step2Tab"
      caption="msg://step2"
      icon="font-icon:ADN">
        <button id="check2Btn" icon="font-icon:CHECK" />
    </wizard:tab>
</wizard:wizard>
```

The Tabs of the wizard have the same attributes available as the ones from the `TabSheet` component of CUBA.

### Example usage
To see this application component in action, check out this example: [cuba-example-using-wizard](https://github.com/mariodavid/cuba-example-using-wizard).

#### Example: Checkout Wizard
![checkout-wizard-step-1](https://github.com/mariodavid/cuba-component-wizard/blob/master/img/checkout-wizard-step-1.png)

![checkout-wizard-step-2](https://github.com/mariodavid/cuba-component-wizard/blob/master/img/checkout-wizard-step-2.png)

![checkout-wizard-step-3](https://github.com/mariodavid/cuba-component-wizard/blob/master/img/checkout-wizard-step-3.png)

![checkout-wizard-step-4](https://github.com/mariodavid/cuba-component-wizard/blob/master/img/checkout-wizard-step-4.png)
