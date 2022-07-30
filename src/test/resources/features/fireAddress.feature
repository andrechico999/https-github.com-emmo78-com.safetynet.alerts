# Author: oliviermorel.oc1@gmail.com
# language: fr

@http://localhost:8080/fire?address=<address>
Fonctionnalité: 4-fire?address
Cette url doit retourner la liste des habitants vivant à l’adresse donnée ainsi que le numéro de la caserne de pompiers la desservant

	En tant qu’utilisateur, je souhaite obtenir la liste des habitants(nom, numéro de téléphone, âge et antécédents
médicaux) vivant à l’adresse donnée avec le numéro de la caserne de pompiers la desservant

	Contexte:
		Étant donné les personnes avec leur antécédants médicaux contexte 4:
		|firstName|lastName|age |address         |city    |zip    |phone         |email            |medications               |allergies     |
		|"John"   |"Boyd"  |"30"|"1509 Culver St"|"Culver"|"97451"|"841-874-6512"|"boyd@email.com" |"aznol:350mg, hyzol:100mg"|"peanut, wasp"|
		|"Tenley" |"Boyd"  |"18"|"1509 Culver St"|"Culver"|"97451"|"841-874-6513"|"tboyd@email.com"|"hyzol:100mg"             |"peanut"      |
		|"Tony"   |"Boyd"  |"12"|"1509 Culver St"|"Culver"|"97451"|"841-874-6512"|"jboyd@email.com"|                          |              |
		|"Tessa"  |"Carman"|"20"|"834 Binoc Ave" |"Culver"|"97451"|"841-874-6514"|"tenz@email.com" |                          |              |
		Étant donné les stations contexte 4:
	|num |address         |
		|"1" |"1509 Culver St"|
		|"2" |"834 Binoc Ave" |

	Scénario: 4A : l’adresse existe :
		Quand utilisateur 4A requête l’addresse "1509 Culver St"
		Alors la liste A des habitants est:
		|lastName|phone         |age |medications               |allergies     |
		|"Boyd"  |"841-874-6512"|"30"|"aznol:350mg, hyzol:100mg"|"peanut, wasp"|
		|"Boyd"  |"841-874-6513"|"18"|"hyzol:100mg"             |"peanut"      |
		|"Boyd"  |"841-874-6512"|"12"|                          |              |
		 Alors le numéro A de la caserne de pompiers la desservant est:
		|fireStation|
		|"1"  |
	
	Scénario: 4B : l’adresse n’existe pas:
		Quand utilisateur 4B requête l’addresse "112 Steppes Pl"
		Alors la liste B des habitants est:
		Alors le numéro B de la caserne de pompiers la desservant est:  