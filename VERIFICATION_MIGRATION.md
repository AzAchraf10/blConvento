# Vérification migration Laravel -> Spring Boot / Thymeleaf

## Vérifications effectuées

Le projet Spring Boot contient maintenant les vues Thymeleaf dans `src/main/resources/templates/`.
Les anciens fichiers Blade placés dans `src/main/resources/views/` ont été retirés pour éviter qu'ils soient confondus avec les templates Thymeleaf.

## Design Laravel conservé

Les ressources visuelles principales du projet Laravel ont été récupérées :
- `static/css/app.css`
- `static/js/app.js`
- `static/images/logo.png`
- `static/favicon.ico`

Le layout conserve le design DSIT/BLconvento : thème clair/sombre, couleurs, badges, panneaux, navigation, typographies et structure générale.

## Corrections apportées

1. Conversion des vues principales vers Thymeleaf :
   - layouts/app.html
   - auth/login.html
   - conventions/index.html
   - conventions/create.html
   - conventions/edit.html
   - conventions/show.html
   - archives/index.html
   - historique/index.html
   - referentiels/index.html
   - users/index.html
   - profile/index.html

2. Ajout des attributs globaux Thymeleaf :
   - `currentUser`
   - `isAuthenticated`
   - `isAdmin`
   - `isSuperAdmin`

3. Le lien du logo/tableau de bord utilise maintenant `/` au lieu de `/dashboard`, car le contrôleur Spring expose actuellement le dashboard sur `/`.

4. Les pages création/modification de convention reçoivent les référentiels nécessaires :
   - maîtres d'ouvrage
   - communes
   - secteurs
   - partenaires

5. `ConventionRequest` accepte les dates ISO `yyyy-MM-dd` utilisées par les champs HTML `date`.

6. Les partenaires sélectionnés peuvent être transmis via `partenaireCodes`.

7. Ajout d'un endpoint de consultation du PDF stocké localement.

8. Le droit d'accès aux référentiels est rapproché du comportement Laravel :
   - ADMIN : consultation
   - SUPER_ADMIN : consultation + mutations

9. La suppression d'une entrée d'historique utilise maintenant un POST `/historique/{id}/delete`, cohérent avec les formulaires HTML utilisés dans l'application.

## Points qui restent à compléter

### Fonctionnalités backend non encore équivalentes à Laravel

Le contrôleur Spring `ReferentielController` ne possède actuellement que les opérations de création. Les opérations Laravel suivantes ne sont pas encore présentes :
- modification d'un maître d'ouvrage
- suppression d'un maître d'ouvrage
- modification/suppression d'une commune
- modification/suppression d'un secteur
- modification/suppression d'un partenaire

Le projet Laravel possède également davantage de logique de recherche, filtrage, tri et détection avancée de doublons sur les conventions. La version Spring actuelle possède une base fonctionnelle plus simple.

### Gestion des partenaires d'une convention

La structure Spring `ConventionPartenaireRequest` est plus riche que le formulaire simplifié actuellement utilisé dans les vues. La sélection des partenaires est fonctionnelle, mais les champs `a_signe` et `montant_investi` nécessitent encore une liaison complète si ces informations doivent être saisies exactement comme dans Laravel.

### Tests

Le build Maven complet n'a pas pu être exécuté dans cet environnement car Maven n'est pas installé localement et le Maven Wrapper n'a pas pu télécharger Maven depuis Internet. Il faut donc lancer localement :

`mvn clean package`

ou :

`mvnw.cmd clean package`

puis démarrer l'application et vérifier les pages dans cet ordre :

1. `/login`
2. `/`
3. `/conventions`
4. `/conventions/create`
5. `/conventions/{id}`
6. `/conventions/{id}/edit`
7. `/archives`
8. `/referentiels`
9. `/users`
10. `/historique`
11. `/profile`

## Conclusion

La partie structurelle de la migration des vues est maintenant beaucoup plus proche de l'application Laravel d'origine, et les placeholders Blade qui existaient dans plusieurs dossiers ont été remplacés par de vraies vues Thymeleaf.

Les principaux écarts restants concernent surtout certaines fonctionnalités backend Laravel qui n'ont pas encore été reproduites en Spring, pas le design.
