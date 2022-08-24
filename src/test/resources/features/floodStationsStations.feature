# Author: oliviermorel.oc1@gmail.com
# language: fr

@http://localhost:8080/flood/stations?stations=<a_list_of_station_numbers>
Fonctionnalité: 5-flood/stations?stations
Cette url doit retourner une liste de tous les foyers desservis par les casernes. Cette liste doit regrouper les personnes par adresse.

	En tant qu’utilisateur, je souhaite obtenir la liste des personnes desservis par les casernes de pompiers, regroupées par adresse, avec les noms, les numéros de téléphone, les âges, et les antécédents médicaux 

	Contexte:
		Étant donné les personnes avec leur antécédants médicaux contexte 5:
		|firstName|lastName|age |address         |city    |zip    |phone         |email           |medications               |allergies     |
		|"John"   |"Boyd"  |"30"|"1509 Culver St"|"Culver"|"97451"|"841-874-6512"|"boyd@email.com"|"aznol:350mg, hyzol:100mg"|"peanut, wasp"|
		|"Tenley" |"Boyd"  |"18"|"1509 Culver St"|"Culver"|"97451"|"841-874-6513"|"boyd@email.com"|"hyzol:100mg"             |"peanut"      |
		|"Tessa"  |"Carman"|"20"|"834 Binoc Ave" |"Culver"|"97451"|"841-874-6514"|"tenz@email.com"|                          |              |
		|"Eric"   |"Kadiga"|"25"|"844 Binoc Ave" |"Culver"|"97451"|"841-874-6515"|"kadi@email.com"|"onala:200mg, azil:50mg"  |"wasp, bee"   |
		|"Clive"  |"Trump" |"50"|"947 E. Rose Dr"|"Culver"|"97451"|"841-874-6617"|"dodo@email.com"|"haldol:10mg"             |              |
		Étant donné les stations contexte 5:
		|num |address         |
		|"1" |"1509 Culver St"|
		|"1" |"834 Binoc Ave" |
		|"2" |"844 Binoc Ave" |


	Scénario: 5 : les casernes sont existantes ou non
		Quand utilisateur requête les stations numéro "1,2,3"
		Alors la liste de tous les foyers desservis par les casernes est:
		|address         |lastName|phone         |age |medications               |allergies     |
		|"1509 Culver St"|"Boyd"  |"841-874-6512"|"30"|"aznol:350mg, hyzol:100mg"|"peanut, wasp"|
		|"1509 Culver St"|"Boyd"  |"841-874-6513"|"18"|"hyzol:100mg"             |"peanut"      |
		|"834 Binoc Ave" |"Carman"|"841-874-6514"|"20"|                          |              |
		|"844 Binoc Ave" |"Kadiga"|"841-874-6515"|"25"|"onala:200mg, azil:50mg"  |"wasp, bee"   |