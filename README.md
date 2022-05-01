# RateMyEPFL
[![Build Status](https://api.cirrus-ci.com/github/LesGolems/RateMyEPFL.svg)](https://cirrus-ci.com/github/LesGolems/RateMyEPFL)
[![Maintainability](https://api.codeclimate.com/v1/badges/6bef22bb4c8d79ce579b/maintainability)](https://codeclimate.com/github/LesGolems/RateMyEPFL/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/6bef22bb4c8d79ce579b/test_coverage)](https://codeclimate.com/github/LesGolems/RateMyEPFL/test_coverage)

Les Golems donnent leur avis sur l'EPFL.


| Name                 | Email |
|----------------------|-------|
| Mathias Bouilloud | mathias.bouilloud@epfl.ch
| Julien Mettler   | julien.mettler@epfl.ch |
| Emilien Guandalino  | emilien.guandalino@epfl.ch |
| Alexandre Messmer        | alexandre.messmer@epfl.ch |
| Nicolas Matekalo   | nicolas.matekalo@epfl.ch |
| Souleyman Boudouh | souleyman.boudouh@epfl.ch |

## Run tests

You need to allow our app to mock the phone's location by going to the Developper Options and for 'Select mock location app' select RateMyEPFL. If the app is not shown when trying to select you can use the following adb command :

```
adb shell appops set com.github.sdp.ratemyepfl android:mock_location allow
```

You also need to install Firebase CLI :

```
curl -sL https://firebase.tools | bash
```

And finally you can run the following command :

```
firebase emulators:exec './gradlew connectedCheck'
```
