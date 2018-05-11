package de.diedavids.cuba.wizard.gui.xml.layout.loaders;

import de.diedavids.cuba.wizard.gui.components.Wizard;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.loaders.AbstractComponentLoader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.List;

public class WizardLoader extends AbstractComponentLoader<Wizard> {
    protected ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);

    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(Wizard.class);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        resultComponent.setSizeFull();

        TabSheet tabSheet = componentsFactory.createComponent(TabSheet.class);
        tabSheet.setSizeFull();

        resultComponent.add(tabSheet);

        List<Element> pageElements = Dom4j.elements(element, "step");

        int pageIndex = 0;
        for (Element pageElement : pageElements) {
            String pageCaption = pageElement.attributeValue("caption");
            if (StringUtils.isNotEmpty(pageCaption)) {
                VBoxLayout tabLayout = componentsFactory.createComponent(VBoxLayout.class);
                tabLayout.setSizeFull();

                GroupBoxLayout tabContent = componentsFactory.createComponent(GroupBoxLayout.class);
                tabContent.setCaption("Content " + pageCaption);
                tabContent.setSizeFull();

                tabLayout.add(tabContent);

                TabSheet.Tab tab = tabSheet.addTab("step" + pageIndex, tabLayout);
                tab.setCaption(pageCaption);
            }
        }
    }
}