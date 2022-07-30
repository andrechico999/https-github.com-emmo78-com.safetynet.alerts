# Author: oliviermorel.oc1@gmail.com
# language: fr

@http://localhost:8080/communityEmail?city=<city>
Fonctionnalité: 7-communityEmail?city
Cette url doit retourner les adresses mail de tous les habitants de la ville

	En tant qu’utilisateur, je souhaite obtenir la liste des adresses mail de tous les habitants de la ville

	Contexte:
		Étant donné les personnes contexte 7:
		|firstName|lastName|age |address         |city    |zip    |phone         |email            |
		|"John"   |"Boyd"  |"30"|"1509 Culver St"|"Culver"|"97451"|"841-874-6512"|"jboyd@email.com"|
		|"Tenley" |"Boyd"  |"18"|"1509 Culver St"|"Culver"|"97451"|"841-874-6513"|"tboyd@email.com"|
		|"Tessa"  |"Carman"|"28"|"1509 Culver St"|"Culver"|"97451"|"841-874-6514"|"tenz@email.com" |
		|"Tony"   |"Boyd"  |"12"|"1509 Culver St"|"Culver"|"97451"|"841-874-6512"|"jboyd@email.com"|
		|"Eric"   |"Kadiga"|"25"|"844 Binoc Ave" |"Culver"|"97451"|"841-874-6515"|"kadi@email.com" |

	Scénario: 7A : la ville existe
		Quand utilisateur A requête la ville "Culver"
		Alors la liste A des courriels est:
		|email            |
		|"jboyd@email.com"|
		|"tboyd@email.com"|
		|"tenz@email.com" |
		|"kadi@email.com" |
	
	Scénario: 7B : la ville n’existe pas
		Quand utilisateur B requête la ville "Paris"
		Alors la liste B des courriels est: 
