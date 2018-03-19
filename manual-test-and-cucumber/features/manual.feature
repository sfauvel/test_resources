#language: fr

@manuel
Fonctionnalité: Test manuel à faire

@ko
Scénario: Test ko
    Etant donné un test manuel
    Quand il y a un problème
    Alors le test est ko
    
@ko
Scénario: Test ko avec indication de l'étape en échec
    Etant donné un test manuel
    Quand si on veut spécifier l'endroit de l'échec
    Alors ERREUR l’écran ne s’ouvre pas
    Alors le test échoue sur le message avec ERREUR

@ok
Scénario: Test Ok
    Etant donné un test manuel
    Quand il tout va bien
    Alors le test est ok
    

Scénario: Test sans état pour le moment
    Etant donné un test manuel
    Quand il n'a pas été joué
    Alors le test est en attente
    
    