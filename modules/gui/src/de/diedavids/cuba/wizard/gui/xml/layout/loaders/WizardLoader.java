package de.diedavids.cuba.wizard.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.loaders.ContainerLoader;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.loaders.AbstractComponentLoader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.List;

public class WizardLoader extends ContainerLoader<Wizard> {


    protected ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);

    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(Wizard.class);
        loadId(resultComponent, element);

        List<Element> stepElements = Dom4j.elements(element, "step");

        for (Element stepElement : stepElements) {
            String stepScreen = stepElement.attributeValue("screen");
            createElement(stepElement, stepScreen);
            createSubComponents(resultComponent, stepElement);
        }

    }

    @Override
    public void loadComponent() {
        resultComponent.setSizeFull();

        TabSheet tabSheet = componentsFactory.createComponent(TabSheet.class);
        tabSheet.setSizeFull();

        resultComponent.add(tabSheet);

        List<Element> stepElements = Dom4j.elements(element, "step");

        int stepIndex = 0;
        for (Element stepElement : stepElements) {
            addStepToTabSheet(tabSheet, stepIndex, stepElement);
        }
    }

    private void addStepToTabSheet(TabSheet tabSheet, int stepIndex, Element stepElement) {
        String pageCaption = stepElement.attributeValue("caption");
        String stepScreen = stepElement.attributeValue("screen");

        if (StringUtils.isNotEmpty(pageCaption)) {


            VBoxLayout tabContentWrapper = componentsFactory.createComponent(VBoxLayout.class);
            tabContentWrapper.setSizeFull();

            if (StringUtils.isNotEmpty(stepScreen)) {
                //tabContent = loadTabContentFromFrame(stepScreen);
                loadSubComponents();

            }
            else {
                Component tabContent = null;
                tabContent = createGenericTabContent(pageCaption);
                tabContentWrapper.add(tabContent);

            }

            TabSheet.Tab tab = tabSheet.addTab("step" + stepIndex, tabContentWrapper);
            tab.setCaption(pageCaption);
        }
    }

    private Element createElement(Element element, String stepScreen) {
        if (element.element("frame") == null) {
            element.addElement("frame")
                    .addAttribute("id", "frame-" + stepScreen)
                    .addAttribute("screen", stepScreen);

        }
        return element;
    }

    private Component loadTabContentFromFrame(String frameScreenId) {
        return getContext().getFrame().openFrame(resultComponent, frameScreenId);
    }

    private Component createGenericTabContent(String pageCaption) {
        GroupBoxLayout tabContent = componentsFactory.createComponent(GroupBoxLayout.class);
        tabContent.setCaption("Content " + pageCaption);
        tabContent.setSizeFull();
        return tabContent;
    }
}