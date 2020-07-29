package de.diedavids.cuba.wizard.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.web.app.main.MainScreen;
import de.diedavids.cuba.wizard.DdcwWebTestContainer;
import de.diedavids.cuba.wizard.gui.components.simple.SimpleWizard;
import de.diedavids.cuba.wizard.web.screens.sample.cuba7.WizardTestScreen;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.screen.StandardScreenTestAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class WizardRenderingTest {

    @RegisterExtension
    SneferuTestUiEnvironment environment =
        new SneferuTestUiEnvironment(DdcwWebTestContainer.Common.INSTANCE)
            .withScreenPackages(
                "com.haulmont.cuba.web.app.main",
                "de.diedavids.cuba.wizard.web.screens"
            )
            .withUserLogin("admin")
            .withMainScreen(MainScreen.class);
    private SimpleWizard wizard;

    @BeforeEach
    void setUp() {
        StandardScreenTestAPI<WizardTestScreen> wizardScreen = environment
            .getUiTestAPI().openStandardScreen(WizardTestScreen.class);

        wizard = (SimpleWizard) wizardScreen.screen().getWindow().getComponent("wizard");

    }

    @Test
    void when_screenIsLoaded_then_wizardComponentIsAvailable() {

        // given:
        assertThat(wizard)
            .isNotNull();

        // when:
        final ComponentContainer step1Tab = (ComponentContainer) wizard.getTabComponent("step1Tab");

        // then:
        assertThat(step1Tab.getComponent("checkBtn"))
            .isNotNull();
    }


    @Test
    void when_screenIsLoaded_then_firstTabIsActive() {

        // expect:
        assertThat(wizard.getSelectedTab())
            .isEqualTo(wizard.getTab("step1Tab"));
    }

    @Test
    void when_screenIsLoaded_then_onlyTheFirstTabIsEnabled() {

        // expect:
        assertThat(wizard.getTab("step1Tab").isEnabled())
            .isTrue();

        // and:
        assertThat(wizard.getTab("step2Tab").isEnabled())
            .isFalse();
    }



}