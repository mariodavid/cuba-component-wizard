package de.diedavids.cuba.wizard.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.web.app.main.MainScreen;
import de.diedavids.cuba.wizard.DdcwWebTestContainer;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.web.screens.sample.lazy.LazyWizard;
import de.diedavids.sneferu.environment.SneferuTestUiEnvironment;
import de.diedavids.sneferu.screen.StandardScreenTestAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class WizardLazyRenderingTest {

    @RegisterExtension
    SneferuTestUiEnvironment environment =
        new SneferuTestUiEnvironment(DdcwWebTestContainer.Common.INSTANCE)
            .withScreenPackages(
                "com.haulmont.cuba.web.app.main",
                "de.diedavids.cuba.wizard.web.screens"
            )
            .withUserLogin("admin")
            .withMainScreen(MainScreen.class);

    private Wizard wizard;

    @BeforeEach
    void setUp() {
        StandardScreenTestAPI<LazyWizard> wizardScreen = environment
            .getUiTestAPI().openStandardScreen(LazyWizard.class);

        wizard = (Wizard) wizardScreen.screen().getWindow().getComponent("wizard");

    }

    @Test
    void when_screenIsLoaded_then_theFirstLazyTabComponentsAreAvailable() {

        // given:
        assertThat(wizard)
            .isNotNull();

        // and:
        wizard.nextTab();

        // when:
        final ComponentContainer step2Tab = (ComponentContainer) wizard.getTabComponent("step2Tab");

        // then:
        assertThat(step2Tab.getComponent("check2Btn"))
            .isNotNull();
    }

}