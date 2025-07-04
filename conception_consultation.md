# Conception du Système de Gestion Hospitalière

## Diagramme de Classes

### Entités Principales

#### 1. Gestion des Patients
```
+------------------+
|     Patient      |
+------------------+
| - id: Long       |
| - nom: String    |
| - prenom: String |
| - dateNaissance  |
| - telephone      |
| - adresse        |
| - email          |
+------------------+
| + getAllPatients()|
| + getPatient()   |
| + savePatient()  |
| + deletePatient()|
+------------------+
```

#### 2. Personnel Médical
```
+------------------+     +------------------+     +------------------+
|     Medecin      |     |    Infirmier     |     |   Technicien     |
+------------------+     +------------------+     +------------------+
| - id: Long       |     | - id: Long       |     | - id: Long       |
| - nom: String    |     | - nom: String    |     | - nom: String    |
| - prenom: String |     | - prenom: String |     | - prenom: String |
| - specialite     |     | - service        |     | - domaine        |
| - telephone      |     | - telephone      |     | - telephone      |
+------------------+     +------------------+     +------------------+

+-------------------------+
| PersonnelAdministratif  |
+-------------------------+
| - id: Long              |
| - nom: String           |
| - prenom: String        |
| - poste: String         |
| - telephone: String     |
+-------------------------+
```

#### 3. Gestion des Chambres et Lits
```
+------------------+     +------------------+
|     Chambre      |     |       Lit        |
+------------------+     +------------------+
| - id: Long       |     | - id: Long       |
| - numero: String |     | - numero: String |
| - type: TypeChambre    | - statut: StatutLit |
| - statut: StatutChambre| - chambre: Chambre |
| - capacite: int  |     +------------------+
| - prix: double   |     | + saveLit()      |
+------------------+     | + getLit()       |
| + getAllChambres()|     | + deleteLit()    |
| + getChambre()   |     | + findByChambre()|
| + saveChambre()  |     +------------------+
+------------------+
         |
         | 1..*
         |
+------------------+
|       Lit        |
+------------------+
```

#### 4. Système de Réservation
```
+------------------------+
|   ReservationChambre   |
+------------------------+
| - id: Long             |
| - chambre: Chambre     |
| - lit: Lit             |
| - patient: Patient     |
| - dateDebut: DateTime  |
| - dateFin: DateTime    |
| - statut: StatutReserv |
| - motif: String        |
| - observations: String |
+------------------------+
| + saveReservation()    |
| + getReservation()     |
| + deleteReservation()  |
+------------------------+
```

#### 5. Consultations et Suivis
```
+------------------+     +----------------------+
|   Consultation   |     | SuiviConsultation    |
+------------------+     +----------------------+
| - id: Long       |     | - id: Long           |
| - patient: Patient     | - consultation: Cons |
| - medecin: Medecin     | - dateVisite: Date   |
| - dateConsultation     | - observations: String|
| - motif: String  |     | - recommandations    |
| - diagnostic     |     | - prochainRdv: Date  |
| - traitement     |     +----------------------+
+------------------+     | + saveSuivi()        |
| + saveConsultation()   | + getSuivi()         |
| + getConsultation()    | + deleteSuivi()      |
+------------------+     | + exportCSV()        |
         |               +----------------------+
         | 1..*
         |
+----------------------+
| SuiviConsultation    |
+----------------------+
```

### Énumérations
```
+------------------+     +------------------+     +------------------+
|   TypeChambre    |     |   StatutChambre  |     |   StatutLit      |
+------------------+     +------------------+     +------------------+
| SIMPLE           |     | DISPONIBLE       |     | LIBRE            |
| DOUBLE           |     | OCCUPEE          |     | OCCUPE           |
| VIP              |     | MAINTENANCE      |     | RESERVE          |
| SOINS_INTENSIFS  |     | HORS_SERVICE     |     | MAINTENANCE      |
+------------------+     +------------------+     +------------------+

+------------------+
| StatutReservation|
+------------------+
| EN_ATTENTE       |
| CONFIRMEE        |
| ANNULEE          |
| TERMINEE         |
+------------------+
```

## Diagramme de Cas d'Utilisation

### Acteurs
- **Administrateur** : Gestion complète du système
- **Personnel Médical** : Médecins, infirmiers, techniciens
- **Personnel Administratif** : Gestion administrative

### Cas d'Utilisation Principaux

#### 1. Gestion des Patients
```
+---------------------------+
|    Gestion Patients       |
+---------------------------+
| - Ajouter patient         |
| - Modifier patient        |
| - Supprimer patient       |
| - Rechercher patient      |
| - Consulter dossier       |
+---------------------------+
```

#### 2. Gestion du Personnel
```
+---------------------------+
|   Gestion Personnel       |
+---------------------------+
| - Ajouter médecin         |
| - Ajouter infirmier       |
| - Ajouter technicien      |
| - Ajouter personnel admin |
| - Modifier informations   |
| - Supprimer personnel     |
+---------------------------+
```

#### 3. Gestion Chambres/Lits
```
+---------------------------+
|  Gestion Chambres/Lits    |
+---------------------------+
| - Ajouter chambre         |
| - Modifier chambre        |
| - Ajouter lit             |
| - Modifier statut lit     |
| - Consulter disponibilité |
+---------------------------+
```

#### 4. Système de Réservation
```
+---------------------------+
|  Gestion Réservations     |
+---------------------------+
| - Créer réservation       |
| - Modifier réservation    |
| - Annuler réservation     |
| - Confirmer réservation   |
| - Consulter planning      |
+---------------------------+
```

#### 5. Consultations et Suivis
```
+---------------------------+
| Gestion Consultations     |
+---------------------------+
| - Programmer consultation |
| - Enregistrer consultation|
| - Ajouter suivi           |
| - Modifier suivi          |
| - Exporter données CSV    |
| - Dashboard de suivi      |
+---------------------------+
```

## Scénarios Nominaux

### Scénario 1 : Réservation d'une Chambre

**Acteur Principal :** Personnel Administratif  
**Préconditions :** 
- Le patient est enregistré dans le système
- Des chambres sont disponibles

**Déroulement :**
1. L'utilisateur accède au menu "Chambres & Lits" → "Réservations"
2. Il clique sur "Nouvelle Réservation"
3. Il sélectionne le patient dans la liste déroulante
4. Il choisit la chambre disponible
5. Il sélectionne optionnellement un lit spécifique
6. Il saisit les dates de début et fin
7. Il ajoute le motif de la réservation
8. Il valide le formulaire
9. Le système vérifie la disponibilité
10. La réservation est enregistrée avec le statut "EN_ATTENTE"
11. Un message de confirmation est affiché

**Postconditions :**
- La réservation est créée dans la base de données
- Le statut de la chambre/lit peut être mis à jour
- La réservation apparaît dans la liste

### Scénario 2 : Ajout d'un Lit avec Contrainte d'Unicité

**Acteur Principal :** Administrateur  
**Préconditions :** 
- Des chambres existent dans le système
- L'utilisateur a les droits d'administration

**Déroulement :**
1. L'utilisateur accède au menu "Chambres & Lits" → "Lits"
2. Il clique sur "Ajouter un lit"
3. Il saisit le numéro du lit
4. Il sélectionne la chambre dans la liste
5. Il choisit le statut du lit (LIBRE, OCCUPE, RESERVE, MAINTENANCE)
6. Il valide le formulaire
7. Le système vérifie l'unicité du numéro dans la chambre
8. Si un lit avec ce numéro existe déjà : erreur affichée
9. Sinon : le lit est enregistré
10. Redirection vers la liste des lits avec message de succès

**Postconditions :**
- Le lit est créé avec un numéro unique dans sa chambre
- Le lit apparaît dans la liste globale et celle de sa chambre

### Scénario 3 : Suivi Post-Consultation

**Acteur Principal :** Médecin  
**Préconditions :** 
- Une consultation a été enregistrée
- Le médecin est authentifié

**Déroulement :**
1. L'utilisateur accède au menu "Activités Médicales" → "Suivis Post-Consultation"
2. Il clique sur "Ajouter un Suivi"
3. Il sélectionne la consultation concernée
4. Il saisit la date de visite
5. Il ajoute ses observations médicales
6. Il formule des recommandations
7. Il planifie optionnellement le prochain rendez-vous
8. Il valide le formulaire
9. Le suivi est enregistré et associé à la consultation
10. Redirection vers le dashboard des suivis

**Postconditions :**
- Le suivi est lié à la consultation
- Les données sont disponibles pour export CSV
- Le dashboard est mis à jour avec les nouvelles données

### Scénario 4 : Gestion des Conflits de Réservation

**Acteur Principal :** Personnel Administratif  
**Préconditions :** 
- Des réservations existent
- Une nouvelle demande de réservation arrive

**Déroulement :**
1. L'utilisateur tente de créer une réservation
2. Il sélectionne une chambre et des dates
3. Le système détecte un conflit avec une réservation existante
4. Un message d'erreur explicite est affiché
5. L'utilisateur modifie les dates ou choisit une autre chambre
6. Il revalide le formulaire
7. Le système confirme la disponibilité
8. La réservation est acceptée

**Postconditions :**
- Aucun conflit de réservation n'existe
- L'intégrité des données est préservée

### Scénario 5 : Export et Analyse des Données

**Acteur Principal :** Médecin Chef / Administrateur  
**Préconditions :** 
- Des suivis de consultation existent
- L'utilisateur a les droits d'accès

**Déroulement :**
1. L'utilisateur accède au dashboard des suivis
2. Il consulte les statistiques affichées
3. Il clique sur "Exporter CSV"
4. Le système génère un fichier CSV avec tous les suivis
5. Le fichier est téléchargé automatiquement
6. L'utilisateur peut analyser les données dans Excel/LibreOffice

**Postconditions :**
- Les données sont exportées au format CSV
- L'analyse des tendances est possible
- Les rapports peuvent être générés

## Relations et Contraintes

### Contraintes d'Intégrité
1. **Unicité des numéros de lit** : Un numéro de lit doit être unique au sein d'une chambre
2. **Références obligatoires** : Une réservation doit avoir un patient et une chambre
3. **Cohérence temporelle** : La date de fin doit être postérieure à la date de début
4. **Statuts cohérents** : Les statuts doivent respecter les transitions logiques

### Relations Importantes
- **Patient → Réservation** : Un patient peut avoir plusieurs réservations (1..*)
- **Chambre → Lit** : Une chambre contient plusieurs lits (1..*)
- **Consultation → SuiviConsultation** : Une consultation peut avoir plusieurs suivis (1..*)
- **Réservation → Chambre/Lit** : Une réservation concerne une chambre et optionnellement un lit

Ce système assure une gestion complète et cohérente des activités hospitalières avec une interface utilisateur intuitive et des fonctionnalités robustes.