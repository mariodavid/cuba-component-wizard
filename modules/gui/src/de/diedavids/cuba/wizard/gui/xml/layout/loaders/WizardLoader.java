package de.diedavids.cuba.wizard.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.ContainerLoader;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import org.dom4j.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WizardLoader extends ContainerLoader<Wizard> {

    protected Map<Element, WizardStep> pendingLoadSteps = new LinkedHashMap<>();

    @Override
    public void createComponent() {
        resultComponent = factory.create(Wizard.class);
        loadId(resultComponent, element);

        List<Element> stepElements = element.elements("step");

        int i = 0;
        for (Element stepElement : stepElements) {

            ComponentLoader stepComponentLoader = getLoader(stepElement, WizardStepLoader.class);
            stepComponentLoader.createComponent();

            WizardStep stepComponent = (WizardStep) stepComponentLoader.getResultComponent();
            resultComponent.addStep(i, stepComponent);
            pendingLoadComponents.add(stepComponentLoader);
            pendingLoadSteps.put(stepElement, stepComponent);

            i++;
        }
    }

    @Override
    public void loadComponent() {
        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);
        loadCaption(resultComponent, element);


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