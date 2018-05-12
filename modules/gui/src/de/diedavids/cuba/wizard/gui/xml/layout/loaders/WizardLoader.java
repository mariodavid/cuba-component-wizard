package de.diedavids.cuba.wizard.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.ContainerLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.TabComponentLoader;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.loaders.AbstractComponentLoader;
import de.diedavids.cuba.wizard.gui.components.WizardStep;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WizardLoader extends ContainerLoader<Wizard> {

    protected Map<Element, TabSheet.Tab> pendingLoadSteps = new LinkedHashMap<>();

    protected ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);


    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(Wizard.class);
        loadId(resultComponent, element);

        List<Element> stepElements = element.elements("step");

        TabSheet tabSheet = componentsFactory.createComponent(TabSheet.class);
        tabSheet.setSizeFull();


        resultComponent.add(tabSheet);

        for (Element stepElement : stepElements) {
            final String name = stepElement.attributeValue("caption");

            ComponentLoader stepComponentLoader = getLoader(stepElement, WizardStepLoader.class);
            TabSheet.Tab tab;
            stepComponentLoader.createComponent();

            Component.Container tabComponent = (Component.Container) stepComponentLoader.getResultComponent();

            tab = tabSheet.addTab(name, tabComponent);
            tab.setCaption(name);
            pendingLoadComponents.add(stepComponentLoader);

            pendingLoadSteps.put(stepElement, tab);

        }

        createSubComponents(resultComponent, element);

    }

    @Override
    public void loadComponent() {
        List<Element> tabElements = element.elements("step");
        for (Element tabElement : tabElements) {
            TabSheet.Tab tab = pendingLoadSteps.remove(tabElement);
            if (tab != null) {
                loadIcon(tab, tabElement);
            }
        }


        loadSubComponents();
    }

}