package de.diedavids.cuba.wizard.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TabSheet;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.ContainerLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.TabComponentLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.TabSheetLoader;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizardStep;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class SimpleWizardLoader extends TabSheetLoader {


    protected Map<Element, TabSheet.Tab> pendingLoadSteps = new LinkedHashMap<>();

    @Override
    public void createComponent() {
        resultComponent = factory.create(SimpleWizard.NAME);
        loadId(resultComponent, element);

        LayoutLoader layoutLoader = getLayoutLoader();

        List<Element> tabElements = element.elements("tab");
        for (Element tabElement : tabElements) {
            final String name = tabElement.attributeValue("id");

            boolean lazy = Boolean.parseBoolean(tabElement.attributeValue("lazy"));
            ComponentLoader tabComponentLoader = layoutLoader.getLoader(tabElement, TabComponentLoader.class);
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

//    @Override
//    public void loadComponent() {
//        assignFrame(resultComponent);
//        assignXmlDescriptor(resultComponent, element);
//
//        loadVisible(resultComponent, element);
//        loadEnable(resultComponent, element);
//        loadStyleName(resultComponent, element);
//        loadResponsive(resultComponent, element);
//        loadCss(resultComponent, element);
//        loadAlign(resultComponent, element);
//
//        loadHeight(resultComponent, element);
//        loadWidth(resultComponent, element);
//
//        loadIcon(resultComponent, element);
//        loadCaption(resultComponent, element);
//        loadDescription(resultComponent, element);
//        loadContextHelp(resultComponent, element);
//
//        loadHtmlSanitizerEnabled(resultComponent, element);
//
//        loadTabIndex(resultComponent, element);
//
//        loadStepsProperties();
//
//        loadSubComponents();
//    }

//    protected void loadStepsProperties() {
//        List<Element> stepElements = element.elements("simpleStep");
//
//        for (Element stepElement : stepElements) {
//            SimpleWizardStep step = pendingLoadSteps.remove(stepElement);
//            if (step != null) {
//                loadIcon(step, stepElement);
//
//
//                String caption = stepElement.attributeValue("caption");
//                if (!StringUtils.isEmpty(caption)) {
//                    caption = loadResourceString(caption);
//                    step.setCaption(caption);
//                }
//
//                String visible = stepElement.attributeValue("visible");
//                if (StringUtils.isNotEmpty(visible)) {
//                    step.setVisible(Boolean.parseBoolean(visible));
//                }
//
//                String style = stepElement.attributeValue("stylename");
//                if (style != null) {
//                    step.setStyleName(style);
//                }
//
//                String enable = stepElement.attributeValue("enable");
//                if (StringUtils.isNotEmpty(enable)) {
//                    step.setEnabled(Boolean.parseBoolean(enable));
//                }
//
//                String description = stepElement.attributeValue("description");
//                if (StringUtils.isNotEmpty(description)) {
//                    description = loadResourceString(description);
//                    step.setDescription(description);
//                }
//            }
//        }
    }
//
//    /**
//     * old behavior
//     */
////    protected Map<Element, WizardStep> pendingLoadSteps = new LinkedHashMap<>();
////
////    protected UiComponents uiComponents = AppBeans.get(UiComponents.NAME);
//
////    @Override
////    public void createComponent() {
////        resultComponent = uiComponents.create(Wizard.class);
////        loadId(resultComponent, element);
////
////        List<Element> stepElements = element.elements("step");
////
////        int i = 0;
////        for (Element stepElement : stepElements) {
////
////            LayoutLoader layoutLoader = getLayoutLoader();
////
////            ComponentLoader stepComponentLoader = layoutLoader.getLoader(stepElement, WizardStepNewLoader.class);
////            stepComponentLoader.createComponent();
////
////            WizardStep stepComponent = (WizardStep) stepComponentLoader.getResultComponent();
////            resultComponent.addStep(i, stepComponent);
////            pendingLoadComponents.add(stepComponentLoader);
////            pendingLoadSteps.put(stepElement, stepComponent);
////
////            i++;
////        }
////    }
////
////    @Override
////    public void loadComponent() {
////        loadWidth(resultComponent, element);
////        loadHeight(resultComponent, element);
////        loadCaption(resultComponent, element);
////
////
////        List<Element> wizardStepElements = element.elements("step");
////        for (Element tabElement : wizardStepElements) {
////            WizardStep step = pendingLoadSteps.remove(tabElement);
////            if (step != null) {
////                loadIcon(step, tabElement);
////            }
////        }
////
////        loadSubComponents();
////
////        getComponentContext().addPostInitTask((context, window) -> resultComponent.init());
////
////    }
//
//}

