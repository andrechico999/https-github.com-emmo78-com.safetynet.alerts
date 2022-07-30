# Author: oliviermorel.oc1@gmail.com
# language: fr

@http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
Fonctionnalité: 6-personInfo?firstName&lastName
Cette url doit retourner la liste des personnes vivant avec la personne donnée ainsi que la liste des personnes portant le même nom.

	En tant qu’utilisateur, je souhaite obtenir la liste des personnes vivant et ceux portant le même nom (nom, adresse, âge, courriel, antécédents médicaux) que la personne donnée

	Contexte:
		Étant donné les personnes avec leur antécédants médicaux contexte 6:
		|firstName|lastName|age |address         |city    |zip    |phone         |email            |medications               |allergies     |
		|"John"   |"Boyd"  |"30"|"1509 Culver St"|"Culver"|"97451"|"841-874-6512"|"boyd@email.com" |"aznol:350mg, hyzol:100mg"|"peanut, wasp"|
		|"Tenley" |"Boyd"  |"18"|"1509 Culver St"|"Culver"|"97451"|"841-874-6513"|"tboyd@email.com"|"hyzol:100mg"             |"peanut"      |
		|"Tony"   |"Boyd"  |"12"|"1509 Culver St"|"Culver"|"97451"|"841-874-6512"|"jboyd@email.com"|                          |              |
		|"Alice"  |"Boyd"  |"80"|"112 Steppes Pl"|"Culver"|"97451"|"841-874-6682"|"aboyd@email.com"|"haldol:10mg"             |"bee, peanut" |
		|"Tessa"  |"Carman"|"75"|"112 Steppes Pl"|"Culver"|"97451"|"841-874-6514"|"tenz@email.com" |"tilia:3g"                |"fish"        |

	Scénario: 6A : la personne existe :
		Quand utilisateur A requête prénom "Tony" et nom "Boyd"
		Alors la liste des personnes 6A est:
		|lastName|address         |age |email            |medications               |allergies     |
		|"Boyd"  |"1509 Culver St"|"12"|"jboyd@email.com"|                          |              |
		|"Boyd"  |"1509 Culver St"|"30"|"boyd@email.com" |"aznol:350mg, hyzol:100mg"|"peanut, wasp"|
		|"Boyd"  |"1509 Culver St"|"18"|"tboyd@email.com"|"hyzol:100mg"             |"peanut"      |
		|"Boyd"  |"112 Steppes Pl"|"80"|"aboyd@email.com"|"haldol:10mg"             |"bee, peanut" |
		
	Scénario: 6B : la personne n’existe pas:
		Quand utilisateur B requête prénom "Tata" et nom "Boyd"
		Alors la liste des personnes 6B est: 
