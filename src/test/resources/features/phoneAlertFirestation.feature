# Author: oliviermorel.oc1@gmail.com
# language: fr

@http://localhost:8080/phoneAlert?firestation=<firestation_number>
Fonctionnalité: 3-phoneAlert?firestation
Cette url doit retourner une liste des numéros de téléphone des résidents desservis par la caserne de pompiers

	En tant qu’utilisateur, je souhaite obtenir la liste des numéros de téléphone des résidents desservis par la caserne de pompiers

	Contexte:
		Étant donné les personnes contexte 3:
		|firstName|lastName|age |address         |city    |zip    |phone         |email           |
		|"John"   |"Boyd"  |"30"|"1509 Culver St"|"Culver"|"97451"|"841-874-6512"|"boyd@email.com"|
		|"Tenley" |"Boyd"  |"18"|"1509 Culver St"|"Culver"|"97451"|"841-874-6512"|"boyd@email.com"|
		|"Tessa"  |"Carman"|"20"|"834 Binoc Ave" |"Culver"|"97451"|"841-874-6514"|"tenz@email.com"|
		|"Eric"   |"Kadiga"|"25"|"844 Binoc Ave" |"Culver"|"97451"|"841-874-6515"|"kadi@email.com"|
		Étant donné les stations contexte 3:
		|num |address         |
		|"1" |"1509 Culver St"|
		|"1" |"834 Binoc Ave" |

	Scénario: 3A : la station existe
		Quand utilisateur 3A requête la station numéro 1
		Alors la liste A des numéros de téléphone est:
		|phone         |
		|"841-874-6512"|
		|"841-874-6514"|
		
	Scénario: 3B : la station n’existe pas
		Quand utilisateur 3B requête la station numéro 2
		Alors la liste B des numéros de téléphone est: 
