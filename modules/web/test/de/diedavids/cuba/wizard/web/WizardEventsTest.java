package de.diedavids.cuba.wizard.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.gui.components.TabSheet.SelectedTabChangeEvent;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.vaadin.ui.Button;
import de.diedavids.cuba.wizard.DdcwWebTestContainer;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard.WizardCancelClickEvent;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard.WizardFinishClickEvent;
import de.diedavids.cuba.wizard.web.screens.sample.cuba7.WizardTestScreen;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.screen.StandardScreenTestAPI;
import java.util.EventObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class WizardEventsTest {

    private SimpleWizard wizard;

    @RegisterExtension
    SneferuTestUiEnvironment environment =
        new SneferuTestUiEnvironment(DdcwWebTestContainer.Common.INSTANCE)
            .withScreenPackages(
                "com.haulmont.cuba.web.app.main",
                "de.diedavids.cuba.wizard.web.screens"
            )
            .withUserLogin("admin")
            .withMainScreen(MainScreen.class);

    private StandardScreenTestAPI<WizardTestScreen> wizardTestScreen;

    @BeforeEach
    void setUp() {
        wizardTestScreen = environment
            .getUiTestAPI().openStandardScreen(WizardTestScreen.class);

        wizard = (SimpleWizard) wizardTestScreen.screen().getWindow().getComponent("wizard");
    }

    @Test
    void when_cancelIsPerformed_then_cancelEventHasBeenReceived() {

        // given:
        final Button cancelBtn = wizardBtn("cancel");

        // when:
        cancelBtn.click();

        // then:
        assertThat(event(WizardCancelClickEvent.class))
            .isNotNull();
    }

    @Test
    void when_nextStepIsPerformed_then_tabChangedEventHasBeenReceived() {

        // given:
        final Button nextBtn = wizardBtn("next");

        // when:
        nextBtn.click();

        // then:
        assertThat(event(SelectedTabChangeEvent.class))
            .isNotNull();
    }

    @Test
    void when_finishIsPerformed_then_wizardFinishEventHasBeenReceived() {

        // given:
        wizard.nextStep();

        // and:
        final Button finishBtn = wizardBtn("finish");

        // when:
        finishBtn.click();

        // then:
        assertThat(event(WizardFinishClickEvent.class))
            .isNotNull();
    }

    private <T extends EventObject> T event(Class<T> clazz) {
        return wizardTestScreen.screen().receivedEvent(clazz);
    }

    private Button wizardBtn(String id) {
        return wizard.getComponentNN(id).unwrap(Button.class);
    }
}