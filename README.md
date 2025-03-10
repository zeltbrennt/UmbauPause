# **"UmbauPause"** – ein Online-Bestellsystem für die Kantine des *Deutschen Nationaltheaters Weimar*

*Projektskizze*

## Hintergrund

Das Deutschen Nationaltheater (DNT) in Weimar plant ab 2027 umfangreiche mehrjährige Renovierungsarbeiten durchzuführen.
Das Haupthaus am Theaterplatz wird in dieser Zeit geschlossen sein, stattdessen werden andere Spielstätten genutzt,
Büros und Werkstätten auf verschiede Niederlassungen verteilt. Dies betrifft auch die Kantine „Pause“, welche in dieser
Zeit ebenfalls an einen alternativen Standort umzieht. Dennoch möchte der Küchenchef den ca. 420 Mitarbeitenden ein
Mittagessen anbieten und dieses auf die Standorte in der Stadt verteilen.
Für die Koordination der Logistik benötigt er ein einfach zu nutzendes Bestellsystem. Kommerziell verfügbare
Webshop-Baukästen wie Shopify kommen weniger in Frage, da der eCommerce-Aspekt bei dem geplanten Projekt weniger stark
im Vordergrund steht. Vielmehr soll ein übersichtliches Tool zur Unterstützung bei der Planung und zur Vereinfachung von
Abläufen erstellt werden. Gleichzeitig dient der Webauftritt als Visitenkarte für den Gastronom.
Die Kantine ist auch für Laufkundschaft geöffnet, d.h. auch betriebsfremde Personen können vor Ort essen, allein
Mitarbeitende des DNT soll es möglich sein, ihr Mittagessen an ihren Arbeitsort zu bestellen. Die Kantine bietet auch
Catering für Events auf Anfrage an.

## Zielsetzung und Anwendungsfälle

Es wird eine Website mit Shop implementiert, welcher möglichst simpel zu handhaben ist – sowohl für Kunden als auch für
den Shopbetreiber. Jede Woche gibt es in der Kantine eine neue Wochenkarte, mit jeweils einem Gericht pro Tag.
Angestellte des DNT können aus einer Wochenkarte Tage auswählen, an denen Sie Mittagessen zum Arbeitsort geliefert
bekommen und können direkt online bezahlen. Die Kantinencrew erhält für jeden Tag Übersichten, aus denen hervor geht,
wie viele Essensportionen an die jeweiligen Standorte ausgeliefert werden müssen. Das Geschäft mit Kundschaft vor Ort
bleibt davon unabhängig. Der Webshop ist daher in drei getrennte Bereiche unterteilt, die von unterschiedlichen
Nutzertypen genutzt werden:

*DNT-Mitarbeitende* erhalten vor Launch der Website ein einmaliges Passwort zum Registrieren. Über die Website können
Kunden dann aus dem wöchentlich wechselnden Angebot wählen. Es ist möglich die Anzahl der Portionen am jeweiligen
Wochentag zu wählen und den Standort, an den es geliefert werden soll. Änderungen und Stornierungen sind bis zum selben
Tag 10:30 Uhr möglich. Es ist möglich eine Bestellung für einzelne Tage abzugeben, oder für die gesamte Woche. Die
Bestellungen können einfach auf der Webseite gezahlt werden. Kunden können in einem eigenen Bereich ihre Bestellungen
und Rechnungen einsehen, sowie Passwörter und Emailadressen ändern.

*Der Kantinenchef* kann den Webshop inhaltlich administrieren. Er kann das Wochenmenü in eine Maske eintragen oder Werte
aus bereits eingetragenen Gerichten übernehmen. Er soll auch die Möglichkeit haben, Beschreibung und Preis von Gerichten
zu bearbeiten oder zu löschen, sowie Bilder hochzuladen. In der Wochenkarte können einzelne Tage (z.B. Feiertage)
gesperrt werden, an denen nicht bestellt werden kann. Es ist möglich den Webshop vorübergehend zu schließen, z.B. im
Fall von Krankheit oder Schließzeiten. Die Kantine erhält eine Übersichtsseite, aus der hervorgeht, wie viele Gerichte
am jeweiligen Tage bestellt wurden und auf welche Standorte sie verteilt werden müssen. Die Speisekarte kann als Link
für Werbezwecke auf Socialmedia geteilt werden.

*Unregstrierte Nutzer* haben die Möglichkeit die Wochenkarte einzusehen, können jedoch keine Bestellungen abgeben. Es
gibt
ein Kontaktformular für Catering-Anfragen für Events. Ebenfalls für alle einsehbar sind das Impressum und
Bildergalerien.

> **Hinweis**: die vollständige Ausarbeitung
> wird [privates Latex-Repository](https://github.com/zeltbrennt/Projektarbeit-UmbauPause) versioniert.
> Nach Abschluss der Arbeit wird das fertige PDF hier hinzugefügt.

# Projekt starten

## Datenbank

Umgebungsvariablen für Datenbank in `.env` 
Postgres-Datenbank mittels Dockerfile in `/docker` Ordern erstellen

- erstelle `/docker/.env`
- vergebe Umgebungsvariablen:
  
```
POSTGRES_PASSWORD=
POSTGRES_ROOT_PASSWORD=
POSTGRES_USER=
```

- erstelle Container mit Datenbank via `/docker/docker-compose.yml`


## Entwicklungsumgebung

- erstelle `.env` im Wurzelverzeichnis
- vergebe Umgebungsvariablen:

```
POSTGRES_DB=webshop
POSTGRES_JDBC_URL=jdbc:postgresql://localhost:5432/webshop
POSTGRES_PASSWORD=
POSTGRES_USER=
USER_DEFAULT_PASSWORD=
JWT_SHARED_SECRET=
JWT_ISSUER=
JWT_AUDIENCE=
JWT_REALM=webshop
EMAIL_HOST=
EMAIL_USERNAME=
EMAIL_PASSWORD=
EMAIL_FROM=
SERVER_DOMAIN=http://localhost:5173
COOKIE_SECURE=false
JWT_EXPIRATION=3600000
STRIPE_SECRET_KEY=
STRIPE_WEBHOOK_SECRET=
```
während der Entwicklung läuft das Frontend auf Port 5173

- erstelle `/frontend/.env`
- vergebe Umgebungsvariablen:

```
VITE_BASE_URL=http://localhost:8080/rest/v1/
VITE_WS_URL=ws://localhost:8080/ws
```

## App starten

- backend compilieren via `gradle ktor buildFatJar`
- frontend compilieren und starten via `cd frontend && npm install && npm run dev`

## User hinzufügen
- sobald die App das erste mal startet, werden die Datenbank-Migrationen gestartet
- anschließend einen User in der `user.account` Tabelle erstellen, dabei das Passwort leer lassen, 
- die gleiche ID verwenden, um dem User mittels der `user.account_role` Tabelle entsprechende Rollen hinzufügen


