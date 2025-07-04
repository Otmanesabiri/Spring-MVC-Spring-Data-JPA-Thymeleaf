# Application de Gestion Hospitalière

## Aperçu
Il s'agit d'une application de gestion hospitalière basée sur Spring Boot qui démontre l'implémentation de Spring MVC, Spring Data JPA, le templating Thymel## Captures d'Écran

### Module Principal
# page de connexion 
![alt text](<Capture d'écran du 2025-03-17 15-25-26.png>) 

# page d'accueil admin 
![alt text](<Capture d'écran du 2025-03-17 15-25-47.png>) 

# formulaire de modification admin 
![alt text](<Capture d'écran du 2025-03-17 15-26-06.png>) 

# page d'accueil utilisateur 
![alt text](<Capture d'écran du 2025-03-17 15-26-44.png>)

### Module Dossier Médical Électronique
*Screenshots du module dossier médical à ajouter après tests*

- **Liste des dossiers médicaux** : Vue d'ensemble avec recherche et filtres
- **Création de dossier** : Formulaire de création avec validation
- **Détails du dossier** : Vue complète avec accès aux sous-modules
- **Modification de dossier** : Formulaire d'édition avec préservation des données
- **Sous-modules** : Interfaces pour historiques, prescriptions, examens, etc. Security. L'application fournit des fonctionnalités pour la gestion des patients avec une authentification et une autorisation appropriées.

## Fonctionnalités
- Gestion des patients (opérations CRUD)
- **Gestion des rendez-vous médicaux** (opérations CRUD complètes)
- **Module Dossier Médical Électronique complet** avec :
  - Création, modification, visualisation et archivage des dossiers médicaux
  - Gestion des historiques médicaux
  - Gestion des prescriptions médicales
  - Gestion des résultats d'examens
  - Gestion des allergies
  - Gestion des antécédents médicaux
  - Gestion des notes médicales
- Fonctionnalité de pagination et de recherche
- Contrôle d'accès basé sur les rôles
- Gestion des erreurs personnalisée
- Design réactif avec Bootstrap

## Technologies Utilisées
- **Spring Boot 3.2.x**
- **Spring MVC**: Couche web
- **Spring Data JPA**: Couche d'accès aux données
- **Thymeleaf**: Moteur de template côté serveur
- **Spring Security**: Authentification et autorisation
- **Bootstrap 5.3.0**: Framework frontend
- **H2/MySQL**: Base de données
- **Lombok**: Réduction du code boilerplate

## Implémentation de la Sécurité
L'application inclut trois approches pour l'implémentation de la sécurité :

### 1. Authentification en Mémoire
L'application est configurée avec une authentification en mémoire pour le développement et les tests :

```java
@Bean
public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder){
    String encodedPassword = passwordEncoder.encode("1234");
    return new InMemoryUserDetailsManager(
        User.withUsername("user1").password(encodedPassword).roles("USER").build(),
        User.withUsername("user2").password(encodedPassword).roles("USER").build(),
        User.withUsername("admin").password(encodedPassword).roles("USER","ADMIN").build()
    );
}
```

### 2. Authentification par Base de Données avec UserDetailsService
L'application implémente un UserDetailsService personnalisé qui charge les détails des utilisateurs depuis la base de données :

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        if(appUser == null) throw new UsernameNotFoundException("User not found");
        
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}
```

### 3. Configuration de la Sécurité
L'application utilise une chaîne de filtres de sécurité pour définir les règles d'autorisation :

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .formLogin(Customizer.withDefaults())
        .authorizeHttpRequests(ar->ar.requestMatchers("/deletePatient/**").hasRole("ADMIN"))
        .authorizeHttpRequests(ar->ar.requestMatchers("/admin/**").hasRole("ADMIN"))
        .authorizeHttpRequests(ar->ar.requestMatchers("/user/**").hasRole("USER"))
        .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
        .exceptionHandling(eh -> eh.accessDeniedPage("/403"))
        .build();
}
```

## Structure de l'Application

### Entités
- **Patient**: Représente un patient avec des informations médicales complètes (nom, prénom, email, téléphone)
- **AppUser**: Représente un utilisateur du système avec des détails d'authentification
- **AppRole**: Représente un rôle pour l'autorisation
- **RendezVous**: Représente un rendez-vous médical avec patient, médecin, date, heure et statut
- **DossierMedical**: Dossier médical électronique principal d'un patient
- **HistoriqueMedical**: Historique des consultations et événements médicaux
- **Prescription**: Prescriptions médicales avec médicaments, posologie et durée
- **ResultatExamen**: Résultats d'examens médicaux (laboratoire, imagerie, etc.)
- **Allergie**: Allergies connues du patient avec niveau de gravité
- **Antecedent**: Antécédents médicaux personnels et familiaux
- **NoteMedicale**: Notes et observations médicales du personnel soignant

### Répertoires
- **PatientRepository**: Répertoire JPA pour les opérations sur les patients
- **AppUserRepository**: Répertoire JPA pour les opérations sur les utilisateurs
- **AppRoleRepository**: Répertoire JPA pour les opérations sur les rôles
- **RendezVousRepository**: Répertoire JPA pour les opérations sur les rendez-vous
- **DossierMedicalRepository**: Répertoire JPA pour les opérations sur les dossiers médicaux
- **HistoriqueMedicalRepository**: Répertoire JPA pour les historiques médicaux
- **PrescriptionRepository**: Répertoire JPA pour les prescriptions
- **ResultatExamenRepository**: Répertoire JPA pour les résultats d'examens
- **AllergieRepository**: Répertoire JPA pour les allergies
- **AntecedentRepository**: Répertoire JPA pour les antécédents
- **NoteMedicaleRepository**: Répertoire JPA pour les notes médicales

### Services
- **PatientService**: Service pour la logique métier liée aux patients
- **UserDetailsServiceImpl**: Implémente UserDetailsService pour l'authentification
- **RendezVousService**: Service pour la gestion des rendez-vous médicaux
- **DossierMedicalService**: Service pour la gestion des dossiers médicaux électroniques
- **HistoriqueMedicalService**: Service pour la gestion des historiques médicaux
- **PrescriptionService**: Service pour la gestion des prescriptions
- **ResultatExamenService**: Service pour la gestion des résultats d'examens
- **AllergieService**: Service pour la gestion des allergies
- **AntecedentService**: Service pour la gestion des antécédents
- **NoteMedicaleService**: Service pour la gestion des notes médicales

### Contrôleurs
- **PatientController**: Gère les requêtes HTTP liées aux patients
- **SecurityController**: Gère les requêtes HTTP liées à la sécurité
- **RendezVousController**: Gère les requêtes HTTP liées aux rendez-vous médicaux
- **DossierMedicalController**: Gère les requêtes HTTP liées aux dossiers médicaux
- **HistoriqueMedicalController**: Gère les requêtes HTTP liées aux historiques médicaux
- **PrescriptionController**: Gère les requêtes HTTP liées aux prescriptions
- **ResultatExamenController**: Gère les requêtes HTTP liées aux examens
- **AllergieController**: Gère les requêtes HTTP liées aux allergies
- **AntecedentController**: Gère les requêtes HTTP liées aux antécédents
- **NoteMedicaleController**: Gère les requêtes HTTP liées aux notes médicales

### Sécurité
- **SecurityConfig**: Configuration pour l'authentification et l'autorisation

## Templates Thymeleaf

L'application utilise des templates Thymeleaf avec une intégration Spring Security :

```html
<!-- Afficher les liens admin uniquement aux utilisateurs ADMIN -->
<li class="nav-item dropdown" sec:authorize="hasRole('ADMIN')">
    <a class="nav-link dropdown-toggle" href="#" id="adminDropdown" role="button" data-bs-toggle="dropdown">
        Admin
    </a>
    <ul class="dropdown-menu">
        <li><a class="dropdown-item" th:href="@{/admin/users}">Utilisateurs</a></li>
        <li><a class="dropdown-item" th:href="@{/admin/roles}">Rôles</a></li>
    </ul>
</li>

<!-- Afficher le bouton de suppression uniquement aux utilisateurs ADMIN -->
<a sec:authorize="hasRole('ADMIN')" onclick="return confirm('Êtes-vous sûr ?')"
   th:href="@{/deletePatient(id=${p.id}, keyword=${keyword}, page=${currentPage})}"
   class="btn btn-sm btn-danger">
    <i class="fa fa-trash"></i>
</a>
```

## Comment Exécuter l'Application

1. Cloner le dépôt :
   ```bash
   git clone https://github.com/yourusername/hospital-management.git
   ```

2. Naviguer vers le répertoire du projet :
   ```bash
   cd hospital-management
   ```

3. Exécuter l'application :
   ```bash
   ./mvnw spring-boot:run
   ```

4. Accéder à l'application à :
   ```
   http://localhost:8085
   ```

## Identifiants de Connexion

| Nom d'utilisateur | Mot de passe | Rôles        |
|-------------------|--------------|--------------|
| user1             | 1234         | USER         |
| user2             | 1234         | USER         |
| admin             | 1234         | USER, ADMIN  |

## Notes de Sécurité

- L'accès aux fonctions administratives est restreint aux utilisateurs ayant le rôle ADMIN.
- Les mots de passe sont cryptés à l'aide de BCryptPasswordEncoder.
- L'application inclut une page d'erreur 403 Interdit personnalisée.
- La fonctionnalité de déconnexion est implémentée selon les meilleures pratiques de Spring Security.

## Captures d'Écran

# page de connexion 
![alt text](<Capture d’écran du 2025-03-17 15-25-26.png>) 

# page d'accueil admin 
![alt text](<Capture d’écran du 2025-03-17 15-25-47.png>) 

# formulaire de modification admin 
![alt text](<Capture d’écran du 2025-03-17 15-26-06.png>) 

# page d'accueil utilisateur 
![alt text](<Capture d’écran du 2025-03-17 15-26-44.png>)

## Module Dossier Médical Électronique

### Aperçu du Module
Le module Dossier Médical Électronique est un système complet de gestion des dossiers patients qui permet de centraliser toutes les informations médicales d'un patient. Ce module comprend plusieurs sous-modules interconnectés pour une gestion complète de l'information médicale.

### Fonctionnalités Principales

#### 1. Gestion des Dossiers Médicaux
- **Création automatisée** : Génération automatique du numéro de dossier
- **Validation robuste** : Vérification de l'unicité du dossier par patient
- **Gestion des statuts** : ACTIF, ARCHIVÉ, SUSPENDU
- **Recherche avancée** : Par nom de patient, numéro de dossier, statut
- **Pagination** : Navigation efficace dans la liste des dossiers

#### 2. Historiques Médicaux
- Suivi chronologique des consultations
- Classification par type (CONSULTATION, HOSPITALISATION, URGENCE, CHIRURGIE)
- Détails des symptômes, diagnostics et traitements
- Association automatique au dossier médical

#### 3. Prescriptions Médicales
- Gestion complète des prescriptions
- Informations détaillées : médicament, posologie, durée, instructions
- Suivi du statut (ACTIVE, TERMINEE, ANNULEE)
- Date de prescription et validité

#### 4. Résultats d'Examens
- Gestion des examens de laboratoire et d'imagerie
- Types d'examens : LABORATOIRE, IMAGERIE, CARDIOLOGIE, etc.
- Valeurs de référence et interprétation des résultats
- Archivage des fichiers d'examens

#### 5. Allergies
- Répertoire complet des allergies du patient
- Classification par type (MEDICAMENT, ALIMENTAIRE, ENVIRONNEMENTALE)
- Niveaux de gravité (LEGERE, MODEREE, SEVERE, CRITIQUE)
- Descriptions détaillées et mesures préventives

#### 6. Antécédents Médicaux
- Historique médical personnel et familial
- Classification par type (PERSONNEL, FAMILIAL, CHIRURGICAL)
- Descriptions détaillées et dates d'occurrence
- Impact sur les soins actuels

#### 7. Notes Médicales
- Notes et observations du personnel soignant
- Classification par type (CONSULTATION, SUIVI, OBSERVATION, URGENT)
- Horodatage automatique
- Traçabilité des modifications

### Architecture Technique

#### Entités et Relations
```java
// Exemple de la relation principale
@Entity
public class DossierMedical {
    @OneToOne
    private Patient patient;
    
    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL)
    private List<HistoriqueMedical> historiques;
    
    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;
    
    // Autres relations...
}
```

#### Services Métier
- **Validation automatique** : Vérification de la cohérence des données
- **Génération automatique** : Numéros de dossier uniques
- **Gestion des statuts** : Workflows automatisés
- **Recherche optimisée** : Requêtes JPA performantes

#### Contrôleurs REST
- **Endpoints RESTful** : APIs standardisées
- **Gestion d'erreurs** : Validation et messages d'erreur appropriés
- **Pagination** : Navigation efficace des données
- **Filtrage** : Recherche multicritères

### Interface Utilisateur

#### Templates Thymeleaf
- **Design responsive** : Compatible mobile et desktop
- **Navigation intuitive** : Menu de navigation contextuel
- **Formulaires validés** : Validation côté client et serveur
- **Affichage conditionnel** : Basé sur les rôles utilisateur

#### Fonctionnalités UI
- **Tableaux interactifs** : Tri, filtrage, pagination
- **Formulaires dynamiques** : Validation en temps réel
- **Modales de confirmation** : Pour les actions critiques
- **Breadcrumbs** : Navigation contextuelle
- **Alertes et notifications** : Feedback utilisateur

### Sécurité et Autorisation

#### Contrôle d'Accès
```java
// Configuration de sécurité pour les dossiers médicaux
.requestMatchers("/dossiers-medicaux/**").hasAnyRole("USER", "ADMIN")
.requestMatchers("/dossiers-medicaux/delete/**").hasRole("ADMIN")
.requestMatchers("/historiques-medicaux/**").hasAnyRole("USER", "ADMIN")
.requestMatchers("/prescriptions/**").hasAnyRole("USER", "ADMIN")
// Autres autorisations...
```

#### Audit et Traçabilité
- **Horodatage automatique** : Création et modification
- **Traçabilité des actions** : Qui a fait quoi et quand
- **Intégrité des données** : Contraintes de validation
- **Archivage sécurisé** : Conservation des données historiques

### Exemples d'Utilisation

#### Création d'un Dossier Médical
1. Sélection du patient
2. Génération automatique du numéro de dossier
3. Validation de l'unicité
4. Sauvegarde avec horodatage

#### Navigation dans le Dossier
1. Vue d'ensemble des informations patient
2. Accès rapide à tous les sous-modules
3. Actions contextuelles (modifier, archiver)
4. Navigation entre les différentes sections

#### Gestion des Sous-modules
1. Ajout d'éléments depuis la vue détail du dossier
2. Modification et suppression avec contrôles d'accès
3. Recherche et filtrage dans chaque module
4. Intégration transparente avec le dossier principal

### Avantages du Module
- **Centralisation** : Toutes les informations patient en un lieu
- **Efficacité** : Navigation rapide et intuitive
- **Sécurité** : Contrôle d'accès granulaire
- **Évolutivité** : Architecture modulaire extensible
- **Conformité** : Respect des standards médicaux

## État du Développement

### ✅ Modules Complétés
1. **Gestion des Patients** - Fonctionnel à 100%
   - CRUD complet avec validation
   - Pagination et recherche
   - Interface responsive

2. **Gestion des Rendez-vous** - Fonctionnel à 100%
   - Création, modification, suppression
   - Gestion des statuts (PLANIFIE, CONFIRME, ANNULE, TERMINE)
   - Interface de planification

3. **Module Dossier Médical Électronique** - Fonctionnel à 95%
   - ✅ Création de dossiers avec validation robuste
   - ✅ Modification et archivage
   - ✅ Vue détaillée avec navigation vers sous-modules
   - ✅ Toutes les entités et relations créées
   - ✅ Tous les services et contrôleurs implémentés
   - ✅ Templates principaux créés
   - 🔄 Tests d'intégration en cours

### 🔄 En Cours de Finalisation
- Tests complets du module dossier médical
- Optimisation des performances
- Validation finale des formulaires des sous-modules



### 📊 Statistiques du Projet
- **Entités créées** : 11 entités principales
- **Services** : 10 services métier
- **Contrôleurs** : 8 contrôleurs REST
- **Templates** : 25+ templates Thymeleaf
- **Lignes de code** : ~3000+ lignes Java + templates

# Documentation du Projet : Gestion Hospitalière

## Présentation Générale
Ce projet est une application web de gestion hospitalière développée avec **Spring Boot**. Elle permet la gestion des patients, médecins, infirmiers, techniciens, personnel administratif, consultations, dossiers médicaux, prescriptions, rendez-vous, et suivis médicaux. L’interface utilisateur est réalisée avec **Thymeleaf** et **Bootstrap**.

## Fonctionnalités Principales Réalisées
- Authentification et autorisation avec **Spring Security**
- Gestion CRUD des entités : patients, médecins, infirmiers, techniciens, personnel administratif
- Gestion des consultations, prescriptions, dossiers médicaux, rendez-vous, suivis
- Gestion des rôles et droits d’accès (admin, médecin, infirmier, etc.)
- Gestion des erreurs personnalisée (ex : page 403)
- Interface utilisateur réactive avec Bootstrap

## Fonctionnalités Avancées Réalisées (Juin 2025)

### Suivi et Post-Suivi de Consultation
- Affichage complet des suivis post-consultation avec pagination, recherche, filtres par statut/type.
- Dashboard moderne des suivis : statistiques, derniers suivis, alertes sur les cas urgents.
- Ajout, modification, suppression, et marquage comme terminé d’un suivi post-consultation.
- Formulaire d’ajout dynamique : affichage des infos consultation sélectionnée, validation côté client.
- Correction des erreurs d’affichage et de compatibilité SQL/Thymeleaf (requêtes H2, boutons JS, etc.).
- Gestion robuste des erreurs serveur (500, chunked encoding, SQL, Thymeleaf).
- Exportation de tous les suivis post-consultation au format CSV (UTF-8, séparateur `;`).
- Interface d’importation de suivis (upload CSV/Excel, en cours de finalisation).

### Rendez-vous
- Affichage des prochains rendez-vous (date future, statut non annulé) en haut de la page de gestion.
- Filtrage, recherche, modification et suppression des rendez-vous existants.

### Sécurité et Qualité
- Gestion des accès et des rôles sur toutes les fonctionnalités sensibles.
- Validation avancée des formulaires côté client et serveur.
- Correction des erreurs de conversion d’enum (Spring/Thymeleaf).
- Documentation et commentaires dans le code pour chaque module clé.

### Points techniques notables
- Utilisation de Spring Boot 3.x et Jakarta EE (import jakarta.servlet).
- Architecture modulaire : chaque module (patients, dossiers, suivis, rendez-vous) est indépendant et extensible.
- Prise en charge de l’export/import de données pour faciliter la migration ou l’analyse.

---

Pour plus de détails, consultez le code source et les commentaires dans les différentes classes.
