#Author: oliviermorel.oc1@gmail.com
#language: fr

@http://localhost:8080/firestation?stationNumber=<station_number>
Fonctionnalité: 1-firestation?stationNumber
Cette url doit retourner une liste des personnes couvertes par la caserne de pompiers correspondante
avec un décompte du nombre d'adultes et du nombre d'enfants (âge <= 18 ans)

	En tant qu’utilisateur, je souhaite obtenir la liste des personnes (prénom, nom, adresse, numéro de téléphone)
	couvertes par la station de numéro donné, avec un décompte du nombre d'adultes et du nombre d'enfants (âge <= 18 ans)

	Contexte:
		Étant donné les personnes contexte 1:
		|firstName|lastName|age |address         |city    |zip    |phone         |email           |
		|"John"   |"Boyd"  |"30"|"1509 Culver St"|"Culver"|"97451"|"841-874-6512"|"boyd@email.com"|
		|"Tenley" |"Boyd"  |"18"|"1509 Culver St"|"Culver"|"97451"|"841-874-6513"|"boyd@email.com"|
		|"Tessa"  |"Carman"|"20"|"834 Binoc Ave" |"Culver"|"97451"|"841-874-6514"|"tenz@email.com"|
		|"Eric"   |"Kadiga"|"25"|"844 Binoc Ave" |"Culver"|"97451"|"841-874-6515"|"kadi@email.com"|
		Étant donné les stations contexte 1:
		|num |address         |
		|"1" |"1509 Culver St"|
		|"1" |"834 Binoc Ave" |

	Scénario: 1A : la station existe
		Quand utilisateur 1A requête la station numéro 1
		Alors la liste 1A des personne est:
		|firstName|lastName|address         |phone         |
		|"John"   |"Boyd"  |"1509 Culver St"|"841-874-6512"|
		|"Tenley" |"Boyd"  |"1509 Culver St"|"841-874-6513"|
		|"Tessa"  |"Carman"|"834 Binoc Ave" |"841-874-6514"|
		Alors le nombre A d’adulte et d’enfant est:
		|adult|children|
		|"2"  |"1"     |
		
	Scénario: 1B : la station n’existe pas
		Quand utilisateur 1B requête la station numéro 2
		Alors la liste 1B des personne est: 
		Alors le nombre B d’adulte et d’enfant est: