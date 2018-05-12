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
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WizardLoader extends ContainerLoader<Wizard> {

    protected Map<Element, TabSheet.Tab> pendingLoadSteps = new LinkedHashMap<>();

    protected ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);


    /**
     * basically I tried to copy some stuff from AccordionLoader.java but I'm not really sure
     * if this is how it is meant to be used
     *
     */
    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(Wizard.class);
        loadId(resultComponent, element);


        TabSheet tabSheet = componentsFactory.createComponent(TabSheet.class);
        tabSheet.setSizeFull();


        resultComponent.add(tabSheet);

        List<Element> stepElements = element.elements("step");
        for (Element stepElement : stepElements) {
            final String stepScreen = stepElement.attributeValue("screen");

            ComponentLoader tabComponentLoader = getLoader(stepElement, TabComponentLoader.class);
            TabSheet.Tab tab;

            tabComponentLoader.createComponent();

            Component tabComponent = tabComponentLoader.getResultComponent();

            tab = tabSheet.addTab(stepScreen, tabComponent);

            pendingLoadComponents.add(tabComponentLoader);

            pendingLoadSteps.put(stepElement, tab);

            /*
            here the an XML frame tag is created in order to load that frame
            Perhaps I need to create a custom Loader for a WizardStep like the TabComponentLoader?
             */
            createElement(stepElement, stepScreen);
            createSubComponents(resultComponent, stepElement);
        }

    }

    /**
     * this is what I copied over from AccordionLoader as well, but I'm not really sure if this is necessary or what
     * would need to be done here. In the example you did the heavy lifting here:
     * https://github.com/cuba-labs/composite-panel-loader/blob/master/modules/gui/src/com/company/demo/gui/xml/layout/loaders/CompositePanelLoader.java#L26
     *
     * But when I tried that, it seems not to work in my case with the frames... but I might also be wrong.
     */
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


    private Element createElement(Element element, String stepScreen) {
        if (element.element("frame") == null) {
            element.addElement("frame")
                    .addAttribute("id", "frame-" + stepScreen)
                    .addAttribute("screen", stepScreen);

        }
        return element;
    }

}