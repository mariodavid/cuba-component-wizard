package de.diedavids.cuba.wizard.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.web.app.main.MainScreen;
import com.vaadin.ui.Button;
import de.diedavids.cuba.wizard.DdcwWebTestContainer;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.Wizard.Direction;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardCancelClickEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardFinishClickEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardStepChangeEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardStepPreChangeEvent;
import de.diedavids.cuba.wizard.web.screens.sample.WizardTestScreen;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.screen.StandardScreenTestAPI;
import java.util.EventObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class WizardEventsTest {

    private Wizard wizard;

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

        wizard = (Wizard) wizardTestScreen.screen().getWindow().getComponent("wizard");
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
    void when_nextStepIsPerformed_then_stepChangedEventsAreReceived() {

        // given:
        final Button nextBtn = wizardBtn("next");

        // when:
        nextBtn.click();

        // then:

        assertThat(event(WizardStepPreChangeEvent.class))
            .isNotNull();

        assertThat(event(WizardStepChangeEvent.class))
            .isNotNull();
    }


    @Test
    void when_nextStepIsPerformed_then_eventsContainCorrectDirection() {

        // given:
        final Button nextBtn = wizardBtn("next");

        // when:
        nextBtn.click();

        // then:
        assertThat(event(WizardStepPreChangeEvent.class).getDirection())
            .isEqualTo(Direction.NEXT);

        assertThat(event(WizardStepChangeEvent.class).getDirection())
            .isEqualTo(Direction.NEXT);
    }


    @Test
    void when_prevStepIsPerformed_then_eventsContainCorrectDirection() {

        // given:
        wizardBtn("next").click();

        // when:
        wizardBtn("prev").click();

        // then:
        assertThat(event(WizardStepPreChangeEvent.class).getDirection())
            .isEqualTo(Direction.PREVIOUS);

        assertThat(event(WizardStepChangeEvent.class).getDirection())
            .isEqualTo(Direction.PREVIOUS);
    }


    @Test
    void when_nextStepIsPerformed_then_stepChangedEventHasBeenReceived() {

        // given:
        final Button nextBtn = wizardBtn("next");

        // when:
        nextBtn.click();

        // then:
        assertThat(event(WizardStepChangeEvent.class))
            .isNotNull();
    }

    @Test
    void when_nextStepIsPermitted_then_stepChangedEventHasNotBeenReceived() {

        // given:
        final Button nextBtn = wizardBtn("next");

        wizard.addWizardStepPreChangeListener(event -> event.preventStepChange());

        // when:
        nextBtn.click();

        // then:
        assertThat(event(WizardStepChangeEvent.class))
            .isNull();
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