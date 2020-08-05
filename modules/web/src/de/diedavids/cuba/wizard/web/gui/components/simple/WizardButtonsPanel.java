package de.diedavids.cuba.wizard.web.gui.components.simple;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component.Alignment;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.BaseAction.EnabledRule;
import java.util.List;
import java.util.function.Consumer;

public class WizardButtonsPanel {

    UiComponents uiComponents;
    Messages messages;

    public WizardButtonsPanel(
        UiComponents uiComponents,
        Messages messages
    ) {
        this.uiComponents = uiComponents;
        this.messages = messages;
    }

    private BaseAction cancelAction;
    private BaseAction nextAction;
    private BaseAction prevAction;
    private BaseAction finishAction;

    public void refresh() {
        nextAction.refreshState();
        prevAction.refreshState();
        finishAction.refreshState();
    }

    public enum WizardButtonType {
        CANCEL,
        PREVIOUS,
        NEXT,
        FINISH;
    }

    static public WizardButtonDescriptor button(
        WizardButtonType type,
        Consumer<ActionPerformedEvent> clickHandler,
        EnabledRule enabledRule
    ) {
        return new WizardButtonDescriptor(
            type,
            clickHandler,
            enabledRule
        );
    }

    public static class WizardButtonDescriptor {
        final WizardButtonType type;
        final Consumer<ActionPerformedEvent> clickHandler;
        final EnabledRule enabledRule;

        public WizardButtonDescriptor(
            WizardButtonType type,
            Consumer<ActionPerformedEvent> clickHandler,
            EnabledRule enabledRule
        ) {
            this.type = type;
            this.clickHandler = clickHandler;
            this.enabledRule = enabledRule;
        }
    }

    class WizardButtonDescriptors {
        final List<WizardButtonDescriptor> buttonDescriptors;

        WizardButtonDescriptors(
            List<WizardButtonDescriptor> buttonDescriptors
        ) {
            this.buttonDescriptors = buttonDescriptors;
        }

        WizardButtonDescriptor byType(WizardButtonType type) {
            return buttonDescriptors.stream()
                .filter(wizardButtonDescriptor -> wizardButtonDescriptor.type.equals(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Wizard Button Type not found"));
        }
    }

    public ButtonsPanel createWizardButtonPanel(
        List<WizardButtonDescriptor> buttonDescriptors
    ) {

        final WizardButtonDescriptors descriptors = new WizardButtonDescriptors(
            buttonDescriptors
        );

        ButtonsPanel wizardButtonsPanel = uiComponents.create(ButtonsPanel.class);
        wizardButtonsPanel.setAlignment(Alignment.TOP_RIGHT);

        wizardButtonsPanel.add(createCancelBtn(descriptors.byType(WizardButtonType.CANCEL)));
        wizardButtonsPanel.add(createNextBtn(descriptors.byType(WizardButtonType.NEXT)));
        wizardButtonsPanel.add(createPrevBtn(descriptors.byType(WizardButtonType.PREVIOUS)));
        wizardButtonsPanel.add(createFinishBtn(descriptors.byType(WizardButtonType.FINISH)));

        return wizardButtonsPanel;
    }

    private Button createCancelBtn(
        WizardButtonDescriptor descriptor
    ) {
        Button cancelBtn = createWizardControlBtn("cancel");
        cancelBtn.setTabIndex(-1);
        cancelAction = wizardAction(cancelBtn, descriptor.clickHandler);
        cancelAction.addEnabledRule(descriptor.enabledRule);
        cancelBtn.setAction(cancelAction);
        return cancelBtn;
    }

    private Button createPrevBtn(
        WizardButtonDescriptor descriptor
    ) {
        Button prevBtn = createWizardControlBtn("prev");

        prevAction = wizardAction(prevBtn, descriptor.clickHandler);
        prevAction.addEnabledRule(descriptor.enabledRule);

        prevBtn.setAction(prevAction);
        return prevBtn;
    }


    private Button createNextBtn(
        WizardButtonDescriptor descriptor
    ) {
        Button nextBtn = createWizardControlBtn("next");
        nextAction = wizardAction(nextBtn, descriptor.clickHandler);
        nextAction.addEnabledRule(descriptor.enabledRule);
        nextBtn.setAction(nextAction);
        return nextBtn;
    }

    private Button createFinishBtn(
        WizardButtonDescriptor descriptor
    ) {
        Button finishBtn = createWizardControlBtn("finish");
        finishAction = wizardAction(finishBtn, descriptor.clickHandler);
        finishAction.addEnabledRule(descriptor.enabledRule);
        finishBtn.setAction(finishAction);
        return finishBtn;
    }


    private BaseAction wizardAction(
        Button button,
        Consumer<ActionPerformedEvent> handler
    ) {
        return new BaseAction(button.getId())
            .withHandler(handler);
    }

    private Button createWizardControlBtn(String id) {
        Button btn = uiComponents.create(Button.class);
        btn.setId(id);
        btn.setCaption(messages.getMessage(this.getClass(), id + "BtnCaption"));
        btn.setIcon(messages.getMessage(this.getClass(), id + "BtnIcon"));
        return btn;
    }

}