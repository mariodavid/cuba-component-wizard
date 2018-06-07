package de.diedavids.cuba.wizard.gui.components

import spock.lang.Specification

class WizardDelegateImplSpec extends Specification {

    WizardDelegateImpl sut

    def setup() {
        sut = new WizardDelegateImpl()
    }

    def "true"() {
        expect:
        true
    }
}
