package de.diedavids.cuba.wizard.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.loaders.ContainerLoader;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import org.dom4j.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WizardLoader extends ContainerLoader<Wizard> {

    protected Map<Element, WizardStep> pendingLoadSteps = new LinkedHashMap<>();

    protected ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);

    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(Wizard.class);
        loadId(resultComponent, element);

        List<Element> stepElements = element.elements("step");

        for (Element stepElement : stepElements) {

            ComponentLoader stepComponentLoader = getLoader(stepElement, WizardStepLoader.class);
            stepComponentLoader.createComponent();

            WizardStep stepComponent = (WizardStep) stepComponentLoader.getResultComponent();
            resultComponent.addStep(stepComponent);
            pendingLoadComponents.add(stepComponentLoader);
            pendingLoadSteps.put(stepElement, stepComponent);
        }
    }

    @Override
    public void loadComponent() {
        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);

        List<Element> wizardStepElements = element.elements("step");
        for (Element tabElement : wizardStepElements) {
            WizardStep step = pendingLoadSteps.remove(tabElement);
            if (step != null) {
                loadIcon(step, tabElement);
            }
        }

        loadSubComponents();
    }
}