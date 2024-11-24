Dieser Plan dient als grobe Orientierung, welche Schritte umgesetzt werden müssen. Es werden Ziele und Nicht-Ziele
definiert, welche das Erreichen von Meilensteinen kennzeichnen.

Weiterhin ist skizziert, welche Kapitel der Ausarbeitung sinnvoll parallel zum Entwicklungsprozess geschrieben (oder
wenigstens begonnen) werden können.

Der Ablauf ist nicht in Stein gemeißelt, sondern soll auch als Todo-Liste im weiteren Projektverlauf dienen.

# 1 Minimum Viable Product

## 1.1 App

### 1.1.1 Projekt-Setup

- [x] Es existiert ein öffentliches Repository für das Projekt
- [x] Es existiert ein personalisiertes Latex-Template für die Ausarbeitung
- [x] Die Ausarbeitung als privates Repository in GitHub synchronisiert

### 1.1.2 Öffentliches Menü (Technischer Durchstich)

- [x] Es existiert eine Datenbank mit einem Datenbankschema und Tabelle mit Gerichten
- [x] Es existiert ein Backend, welches auf die Datenbank zugreifen kann
- [x] Das Backend verfügt über eine REST-API, welche Gerichte in JSON ausliefert
- [x] Es existiert ein Frontend, welches die REST-API des Backends konsumiert
- [x] Das Fronend kann aufgerufen werden und ein Wochenmenü wird angezeigt
- [x] Die Backend-API kann nur vom Frontend aufgerufen werden
- [x] Tests mit Mock Repository
- [ ] Tests Bedienung Frontend

### 1.1.3 Getrennte Rollen

- [x] Auf der Datenbank existiert eine Tabelle mit Nutzern
- [x] Im Backend werden Endpunkte zur Übermittlung von Nutzerdaten bereitgestellt
- [x] Backend hat Kenntnis über eingeloggte Nutzer

- [x] Es existiert die Möglichkeit sich auf der Website ein- und auszuloggen.
- [x] Sessions werden über den Besuch erhalten
- [x] Passwortdaten werden geshasht gespeichert (bcrypt)
- [x] Nutzer haben unterschiedliche Rollen (Nutzer, Admin, Manager)
- [x] Es existieren Endpunkte, die jeweils nur von Administratoren bzw. Managern aufgerufen werden können
- [ ] Frontend Tests
- [ ] Backend Tests

**Nicht-Ziele**

- [ ] Nutzer können nicht die Daten anderer Nutzer sehen
- [ ] Nutzer können keine Funktionen aufrufen, die Administratoren und Manager haben
- [ ] Manager können keine Funktionen aufrufen, die Administratoren haben
- [ ] Passwörter werden nicht im Klartext gespeichert

### 1.1.4 CRUD-Operationen

- [ ] Manager kann Liste aller Gerichte sehen
    - [ ] Endpunkt Backend
    - [ ] View im Frontend
- [ ] Manager kann Gerichte
    - [x] hinzufügen
    - [x] bearbeiten
    - [ ] löschen
- [x] Auf der Datenbank existiert eine Tabelle mit Wochenkarten (Auswahl aus maximal fünf Gerichten auf Wochentage
  verteilt)
- [x] Manager kann Wochenkarte aus Gerichten erstellen
- [ ] Wenn kein Gericht an einem Wochentag eingetragen ist, wird die Kantine als „geschlossen“ angezeigt
- [x] Manager kann festlegen, in welchem Zeitraum eine Wochenkarte gültig ist
- [x] Es existiert eine Tabelle mit Standorten
    - [ ] Maske in der Standort hinzugefügt werden können
    - [ ] Standorte können aktiviert und deaktiviert werden
- [ ] Fronend Tests
- [ ] Backend Tests

**Nicht-Ziele**

- [x] Es ist nicht möglich Gerichte doppelt einzutragen
- [ ] Es ist nicht möglich Gerichte zu löschen, die auf einer Wochenkarte stehen
-

### 1.1.5 Bestellungen

- [x] Auf der Datenbank existiert eine Tabelle mit Bestellungen, welche Nutzer, Gerichte und Zeitpunkt miteinander in
  Verbindung bringt
- [ ] Bestellungen haben einen Status
    - [x] Bestellt
    - [x] Storniert
    - [ ] Ausgeliefert
    - [x] Bezahlt
- [x] Das Backend stellt Endpunkte bereit, um neue Bestellungen zu hinterlegen
- [ ] Im Frontend wird für jedes Gericht angezeigt, ob es bestellt wurde (Kunde)
- [ ] Backend Tests
- [ ] Frontend Tests

**Nicht-Ziele**

- [x] Es können keine Bestellungen abgegeben werden, für Gerichte, die nicht auf der aktuellen Wochenkarte stehen
- [x] Es können keine Bestellungen für andere Nutzer abgegeben werden
- [x] Es kann das gleiche Gericht nicht zu unterschiedlichen Zeitpunkten zweimal bestellt werden
- [x] es können keine Gerichte bestellt werden, die abgelaufen sind

### 1.1.6 Bestellübersicht

- [ ] Manager kann aggregiert die Anzahl der Bestellungen je Tag einsehen
    - [ ] Alle Bestellungen
    - [ ] Bestellungen je Nutzer
    - [x] Bestellungen je Standort
    - [ ] Bestellungen der Woche
- [x] Update der Bestellübersicht erfolgt automatisch
- [ ] Verlauf der Bestellungen über den Tag
- [ ] Backend Tests
- [ ] Frontend Tests

### TODOs

- [ ] Requests mit abgelaufenem Token führen zu logout
- [ ] alternativ refresh token
- [ ] passwort reset link
- [x] Email Bestätigung bei Bestellung
- [x] Feedback Feld
- [ ] Caches in Frontend und Backend
- [ ] Validierung für Bestellungen

## 1.2 Ausarbeitung

- [ ] Hintergrund
- [ ] Problemstellung
- [ ] Ziele der Arbeit
- [ ] Verwandte Arbeiten
- [ ] Grundlagen
- [ ] Konzept
    - [ ] Architektur
    - [ ] Schemata (MVC, Client-Server, Datenbank-UML)
- [ ] Realisierung

# 2 Zusätzliche Features

## 2.1 App

### 2.1.1 ChatGPT

- [x] Auf der Datenbank existiert eine Tabelle mit Zutaten, die Bezug auf Gerichte nimmt
- [x] OpenAI-API ist in WebApp hinterlegt
- [x] Client kann API aufrufen
- [x] Es existiert ein Prompt, der die Menübeschreibungen in Zutaten zerlegt
- [x] Bei Eingabe von neuen Gerichten wird das Tagging aktiviert
- [x] Zutaten werden automatisch gespeichert
- [ ] Es gibt die Möglichkeit Tags zu bearbeiten
- [ ] Backend Tests
- [ ] Frontend Tests

### 2.1.2 Dashboard

- [ ] Es werden Kennzahlen dargestellt:
    - [ ] Wie viele Gerichte verkauft wurden
- [ ] Grafik über zeitlichen Verlauf der Bestellungen
- [x] Matrix von Zutaten über Häufigkeit
- [ ] Aufteilung in Wochentag, Standort und Kunde

### 2.1.3 Kontaktformular

- [x] Es gibt ein Kontaktformular mit Anfragen
- [ ] Anfragetext wird per Mail verschickt

### 2.1.4 Zahlungsdienstleister

- [x] API Anbindung von Stripe
- [x] Eingabemaske für Zahlungen in Bestellprozess

### 2.1.5 Bilder upload

- [ ] Datenbank mit Bildern
- [ ] Eingabe Maske für Upload von Bildern
- [ ] Darstellung von Bildern / Galerie

### 2.1.6 Nutzerprofile

- [ ] Es existiert eine Seite für Nutzerprofile
- [ ] Nutzer können ihre Passwörter ändern und zurücksetzen
- [ ] Nutzer können präferierten Standort einstellen

### 2.1.7 Admin

- [ ] Admin hat die Μöglichkeit, alle Nutzer auszuloggen
- [ ] Admin kann Nutzer sperren / entsperren
- [ ] Loging
    - [ ] angemeldete Nutzer
    - [ ] Durchgeführte Bestellungen, Stornierungen
    - [ ] Durchgeführte Änderungen an der Datenbank
    - [ ] Loginversuche

## 2.2 Ausarbeitung

- [ ] Implementierung

# 3 Abschluss

## 3.1 App

### 3.1.1 Zugangskontrolle

- [x] Es können sich nur Nutzer mit einer bestimmten Email-Adresse anmelden
- [ ] Admin kann neuen Manager hinzufügen
- [ ] rateLimit für Zugriffe auf die Login-Ressource
- [ ] Benutzer werden nach 5 fehlerhaften Loginversuchen für eine exponentiell steigende Zeit an einem weiteren
  Loginversuch gehindert

**Nicht-Ziele**

- [ ] Manager kann keine Manger oder Admin Accounts hinzufügen
- [ ] es ist nicht möglich beliebig viele Login versuche auszulösen
- [ ] der Server gibt möglich wenig Informationen preis, also auch nicht, ob Benutzername vergeben ist, oder nicht

### 3.1.2 Zwei-Faktor-Authentifikation

- [ ] Anbindung von 2FA
- [ ] Einstellmöglichkeit in Profil

### 3.1.3 Landingpage

- [ ] es existiert eine Willkommensseite mit den wichtigsten Infos

### 3.1.4 Impressum

- [ ] es existiert eine Impressumseite

### ~~3.1.5 Cookie-Banner~~

- [ ] ~~es existiert ein Cooke-Banner~~

## 3.2 Ausarbeitung

- [ ] Zusammenfassung
- [ ] Vorwort
- [ ] Präsentation
- [ ] Demo