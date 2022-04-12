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

You need to install Firebase CLI :

```
curl -sL https://firebase.tools | bash
```

Then login using a Google auth: 

```
firebase login
```

Now that everything is setup, to run the tests, use the following command : 

```
firebase emulators:exec './gradlew connectedCheck'
```
