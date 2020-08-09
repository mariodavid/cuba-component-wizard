# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [0.8.0] - 09/08/2020

### Added
- Event Subscription for Wizard via `@Subscribe` annotation in controller
- `WizardTabPreChangeEvent` which allows prevent tab changes from happening
- `Wizard.Direction` which indicates whether a tab changed was of type `NEXT` or `PREVIOUS`


### Changed
- CUBA 7 UI Screens API support for Wizard
- Renamed Wizard Steps to Wizard Tabs

### Removed
- CUBA 6 based Frames approach on creating wizard steps

## [0.6.1] - 28/03/2020

### Dependencies
- CUBA 7.2.x

## [0.5.0] - 29/09/2019

### Dependencies
- CUBA 7.1.x

## [0.4.1] - 27/08/2019

### Changed
-  remove example usages from app component

## [0.4.0] - 10/05/2019


### Added
- `AbstractWizard` as a super controller class for Wizard controllers (`de.diedavids.cuba.wizard.web.screens.AbstractWizard`)

### Deprecated
- `WizardStepAware` is no longer necessary. Use `AbstractWizardStep` directly instead.

### Dependencies
- CUBA 7.0.x


## [0.3.0] - 03/02/2019

### Dependencies
- CUBA 6.10.x


## [0.2.0] - 02/02/2019

### Dependencies
- CUBA 6.9.x


## [0.1.0] - 07/09/2018

### Added
- Initial version of Wizard component

### Dependencies
- CUBA 6.8.x

