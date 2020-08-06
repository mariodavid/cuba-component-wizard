package de.diedavids.cuba.wizard.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.TabComponentLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.TabSheetLoader;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard;
import java.util.List;
import org.dom4j.Element;

public class SimpleWizardLoader extends TabSheetLoader {


    @Override
    public void createComponent() {
        resultComponent = factory.create(SimpleWizard.NAME);
        loadId(resultComponent, element);

        LayoutLoader layoutLoader = getLayoutLoader();

        List<Element> tabElements = element.elements("tab");
        for (Element tabElement : tabElements) {
            final String name = tabElement.attributeValue("id");

            boolean lazy = Boolean.parseBoolean(tabElement.attributeValue("lazy"));
            ComponentLoader tabComponentLoader = layoutLoader
                .getLoader(tabElement, TabComponentLoader.class);
            TabSheet.Tab tab;
            if (lazy) {
                tab = resultComponent.addLazyTab(name, tabElement, tabComponentLoader);
            } else {
                tabComponentLoader.createComponent();

                Component tabComponent = tabComponentLoader.getResultComponent();

                tab = resultComponent.addTab(name, tabComponent);

                pendingLoadComponents.add(tabComponentLoader);
            }

            pendingLoadTabs.put(tabElement, tab);
        }
    }
}

