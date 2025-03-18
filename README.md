# Application de Gestion Hospitalière

## Aperçu
Il s'agit d'une application de gestion hospitalière basée sur Spring Boot qui démontre l'implémentation de Spring MVC, Spring Data JPA, le templating Thymeleaf et Spring Security. L'application fournit des fonctionnalités pour la gestion des patients avec une authentification et une autorisation appropriées.

## Fonctionnalités
- Gestion des patients (opérations CRUD)
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
- **Patient**: Représente un patient avec des informations médicales
- **AppUser**: Représente un utilisateur du système avec des détails d'authentification
- **AppRole**: Représente un rôle pour l'autorisation

### Répertoires
- **PatientRepository**: Répertoire JPA pour les opérations sur les patients
- **AppUserRepository**: Répertoire JPA pour les opérations sur les utilisateurs
- **AppRoleRepository**: Répertoire JPA pour les opérations sur les rôles

### Services
- **PatientService**: Service pour la logique métier liée aux patients
- **UserDetailsServiceImpl**: Implémente UserDetailsService pour l'authentification

### Contrôleurs
- **PatientController**: Gère les requêtes HTTP liées aux patients
- **SecurityController**: Gère les requêtes HTTP liées à la sécurité

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
