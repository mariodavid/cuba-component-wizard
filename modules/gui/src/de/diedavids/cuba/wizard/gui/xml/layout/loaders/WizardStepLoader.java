package de.diedavids.cuba.wizard.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.loaders.AbstractBoxLoader;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import org.dom4j.Element;

public class WizardStepLoader extends AbstractBoxLoader<WizardStep> {

    protected ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);

    @Override
    public void createComponent() {
        resultComponent = (WizardStep) componentsFactory.createComponent(WizardStep.NAME);
        loadId(resultComponent, element);
        loadCaption(resultComponent, element);
        loadIcon(resultComponent, element);

        createElement(element, element.attributeValue("screen"));
        createSubComponents(resultComponent, element);
    }

    private Element createElement(Element element, String stepScreen) {
        if (element.element("frame") == null) {
            element.addElement("frame")
                    .addAttribute("id", "frame-" + stepScreen)
                    .addAttribute("screen", stepScreen);

        }
        return element;
    }
}