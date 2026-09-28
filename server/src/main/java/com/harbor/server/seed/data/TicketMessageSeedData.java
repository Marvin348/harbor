package com.harbor.server.seed.data;

import static com.harbor.server.features.tickets.communication.message.model.TicketMessageType.INTERNAL_NOTE;
import static com.harbor.server.features.tickets.communication.message.model.TicketMessageType.REPLY;

import com.harbor.server.features.tickets.communication.message.model.TicketMessageType;

import java.time.LocalDateTime;
import java.util.List;

public class TicketMessageSeedData {

  public record TicketMessageSeed(
      String ticketKey,
      String authorEmail,
      TicketMessageType type,
      String body,
      LocalDateTime createdAt) {}

  public static final List<TicketMessageSeed> TICKET_MESSAGES =
      List.of(
          message(
              "TICKET_1",
              "jonas.weber@harbor-demo.test",
              REPLY,
              "2026-07-03T08:41:00",
              "Hallo Lukas, wir bereiten das Notebook vor und melden uns, sobald es abholbereit ist."),
          message(
              "TICKET_1",
              "lukas.bauer@harbor-demo.test",
              REPLY,
              "2026-07-03T09:18:00",
              "Perfekt, danke. Bitte den Browser mit den alten Lesezeichen nicht vergessen; der Praktikant nennt sie sein digitales Gedaechtnis."),
          message(
              "TICKET_2",
              "amira.klein@harbor-demo.test",
              REPLY,
              "2026-09-24T08:22:00",
              "Kannst du bitte das VPN-Protokoll direkt nach dem naechsten Abbruch hochladen? Besonders interessant ist die letzte Minute vor der Trennung."),
          message(
              "TICKET_3",
              "david.becker@harbor-demo.test",
              REPLY,
              "2026-08-11T14:26:00",
              "Welche Reporting-Rolle hattest du vor dem Teamwechsel? Ein Screenshot eines Kollegen mit gleichem Aufgabenbereich reicht ebenfalls."),
          message(
              "TICKET_4",
              "emma.lehmann@harbor-demo.test",
              REPLY,
              "2026-09-18T11:01:00",
              "Wir koennen den Fehler reproduzieren. Bitte bis zur Korrektur keine Angebotsentwuerfe mehrfach speichern, damit keine halben Versionen entstehen."),
          message(
              "TICKET_5",
              "jonas.weber@harbor-demo.test",
              REPLY,
              "2026-08-27T17:12:00",
              "Der Build wartet auf einen Runner, der sich selbst noch als beschaeftigt meldet. Wir raeumen den verwaisten Job kontrolliert auf."),
          message(
              "TICKET_6",
              "sofia.hartmann@harbor-demo.test",
              REPLY,
              "2026-09-25T07:09:00",
              "Bitte die Nachricht nicht weiterleiten und keine Anhaenge oeffnen. Wir haben Absender und Zieladresse bereits blockiert."),
          message(
              "TICKET_7",
              "lea.schneider@harbor-demo.test",
              REPLY,
              "2026-07-21T15:17:00",
              "Ein Ersatzheadset liegt ab morgen an der Ausgabe. Das defekte Geraet darf gern inklusive des mysterioesen Klebebands zurueckkommen."),
          message(
              "TICKET_8",
              "felix.wagner@harbor-demo.test",
              REPLY,
              "2026-09-09T12:48:00",
              "Wir messen heute Nachmittag im Raum. Bitte die grosse Pflanze bis dahin nicht umstellen; sie ist inzwischen Teil des Versuchsaufbaus."),
          message(
              "TICKET_9",
              "mila.hoffmann@harbor-demo.test",
              REPLY,
              "2026-08-30T20:06:00",
              "Ich habe die alte MFA-Registrierung entfernt. Beim naechsten Login sollte die Einrichtung fuer das neue Telefon erscheinen."),
          message(
              "TICKET_10",
              "clara.schulz@harbor-demo.test",
              REPLY,
              "2026-07-29T10:34:00",
              "Die Duplikate entstehen beim Zusammenfuehren zweier Kundensegmente. Wir pruefen gerade, ob nur der Export oder bereits die Quelle betroffen ist."),
          message(
              "TICKET_11",
              "paul.neumann@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-27T06:02:00",
              "CPU unauffaellig, aber die Datenbankverbindungen laufen seit 04:50 Uhr langsam voll. Pool-Metriken fuer das Incident-Review sichern."),
          message(
              "TICKET_12",
              "david.becker@harbor-demo.test",
              REPLY,
              "2026-08-06T22:43:00",
              "Warst du zu diesem Zeitpunkt eventuell ueber ein Mobilfunknetz oder auf Reisen angemeldet? Bis zur Klaerung bleibt die Sitzung widerrufen."),
          message(
              "TICKET_13",
              "emma.lehmann@harbor-demo.test",
              REPLY,
              "2026-06-18T14:29:00",
              "Bitte schick uns den gewuenschten Namen und die Position der neuen Stufe. 'Fast gewonnen, vielleicht' wuerden wir nur ungern produktiv anlegen."),
          message(
              "TICKET_14",
              "amira.klein@harbor-demo.test",
              REPLY,
              "2026-07-14T16:08:00",
              "Der aktuelle Snapshot ist gesichert. Wir koennen morgen um 09:00 Uhr zuruecksetzen, sofern bis dahin niemand Einspruch erhebt."),
          message(
              "TICKET_15",
              "clara.schulz@harbor-demo.test",
              REPLY,
              "2026-08-23T08:04:00",
              "Bitte den Datentraeger nicht anschliessen. Liegt er noch versiegelt in der Fundbox oder hat ihn bereits jemand in die Hand genommen?"),
          message(
              "TICKET_16",
              "nico.braun@harbor-demo.test",
              REPLY,
              "2026-09-02T11:23:00",
              "Wir bringen ein anderes DisplayPort-Kabel mit. Wenn der Monitor direkt am Notebook funktioniert, grenzt das die Dockingstation gut ein."),
          message(
              "TICKET_17",
              "sofia.hartmann@harbor-demo.test",
              REPLY,
              "2026-09-21T04:39:00",
              "Der Carrier hat einen Faserschaden bestaetigt. Der Standort laeuft voruebergehend ueber die langsamere Mobilfunkstrecke."),
          message(
              "TICKET_18",
              "oskar.krause@harbor-demo.test",
              REPLY,
              "2026-06-30T17:02:00",
              "Bitte sende uns die Mitgliederliste und den Besitzer der Projektgruppe. Zugriffe ohne benannte Verantwortung legen wir nicht an."),
          message(
              "TICKET_19",
              "paul.neumann@harbor-demo.test",
              REPLY,
              "2026-08-16T09:16:00",
              "Kannst du drei abgelehnte Beispielnummern schicken? Persoenliche Daten bitte durch Nullen ersetzen, das Format muss aber erhalten bleiben."),
          message(
              "TICKET_20",
              "jonas.weber@harbor-demo.test",
              REPLY,
              "2026-09-12T23:44:00",
              "Die Logs der letzten Nacht liegen im geschuetzten Analyseordner. Der Link verfaellt nach 48 Stunden."),
          message(
              "TICKET_21",
              "lea.schneider@harbor-demo.test",
              REPLY,
              "2026-07-07T10:03:00",
              "Browser und Office sind installiert. Fuer das Kommunikationstool fehlt noch die Freigabe der Lizenzgruppe."),
          message(
              "TICKET_22",
              "selin.arslan@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-08-19T15:32:00",
              "Paketverlust nur bei Video, normale Telefonie stabil. QoS-Regeln am Vertriebs-VLAN mit der letzten Aenderung vergleichen."),
          message(
              "TICKET_23",
              "david.becker@harbor-demo.test",
              REPLY,
              "2026-09-05T07:02:00",
              "Die Vertretungsrolle ist bis Freitag 18 Uhr aktiv. Danach wird sie automatisch entzogen."),
          message(
              "TICKET_24",
              "emma.lehmann@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-16T12:06:00",
              "Suchindex meldet gesund, enthaelt aber nur 17 Dokumente. Vollstaendigen Neuaufbau ausserhalb der Spitzenzeit vorbereiten."),
          message(
              "TICKET_25",
              "zoe.berger@harbor-demo.test",
              REPLY,
              "2026-06-09T18:27:00",
              "Wir entfernen alte Build-Artefakte heute Nacht. Aktuell sind noch 18 Prozent frei, es besteht kein unmittelbarer Ausfall."),
          message(
              "TICKET_26",
              "fabian.lorenz@harbor-demo.test",
              REPLY,
              "2026-09-22T03:01:00",
              "Die betroffenen Rechner bitte eingeschaltet, aber vom Netzwerk getrennt lassen. Unser Team beginnt sofort mit der Sicherung der Spuren."),
          message(
              "TICKET_27",
              "moritz.hahn@harbor-demo.test",
              REPLY,
              "2026-07-26T14:12:00",
              "Das Sprachpaket ist nachinstalliert. Nach einem Neustart sollte die gewuenschte Belegung in der Auswahlliste stehen."),
          message(
              "TICKET_28",
              "jonathan.keller@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-26T03:34:00",
              "Beide rekursiven Resolver liefern SERVFAIL. Anycast voruebergehend auf den dritten Standort umgebogen."),
          message(
              "TICKET_29",
              "mila.hoffmann@harbor-demo.test",
              REPLY,
              "2026-08-02T11:09:00",
              "Der Projektordner hatte noch eine veraltete Gruppenreferenz. Bitte einmal ab- und wieder anmelden und den Zugriff erneut testen."),
          message(
              "TICKET_30",
              "clara.schulz@harbor-demo.test",
              REPLY,
              "2026-09-19T09:21:00",
              "Wir haben einen Incident-Call gestartet. Bereits erstellte Angebote bleiben gueltig; nur neue Dokumente sind betroffen."),
          message(
              "TICKET_31",
              "nico.braun@harbor-demo.test",
              REPLY,
              "2026-09-27T14:19:00",
              "Bitte das Notebook ausschalten und nicht mehr laden. Das Polarlicht klingt huebsch, ist bei Fluessigkeitsschaden aber leider kein gutes Zeichen."),
          message(
              "TICKET_32",
              "lea.schneider@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-08-14T09:11:00",
              "Testseite druckt korrekt, nur Tabellen werden zu Keilschrift. Vermutlich alter Universaltreiber aus der Buchhaltung."),
          message(
              "TICKET_33",
              "amira.klein@harbor-demo.test",
              REPLY,
              "2026-07-31T18:47:00",
              "Das klingt nach Funkstoerungen am Arbeitsplatz, nicht nach dem VPN selbst. Laeuft dort ein USB-C-Hub oder ein ungewoehnlich ambitionierter Ventilator?"),
          message(
              "TICKET_34",
              "felix.wagner@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-23T13:08:00",
              "Daempfung durch Brandschutz und Metallkern deutlich hoeher als im Bauplan. Zusaetzlichen Access Point ausserhalb des Raums testen."),
          message(
              "TICKET_35",
              "oskar.krause@harbor-demo.test",
              REPLY,
              "2026-09-11T10:27:00",
              "Wir haben die alte Genehmigerregel gefunden. Bitte bestaetige noch, wer die offenen Freigaben uebernehmen soll."),
          message(
              "TICKET_36",
              "aylin.yilmaz@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-28T00:39:00",
              "Conditional-Access-Regel hat Servicekonten versehentlich eingeschlossen. Ausnahme eng auf dieses Konto begrenzen und Ablaufdatum setzen."),
          message(
              "TICKET_37",
              "maximilian.fuchs@harbor-demo.test",
              REPLY,
              "2026-08-25T16:04:00",
              "Die Emojis stammen tatsaechlich aus einer fehlerhaften Zeichensatz-Zuordnung. Die Kartoffel war urspruenglich 'Kartoffeltechnik GmbH'."),
          message(
              "TICKET_38",
              "emma.lehmann@harbor-demo.test",
              REPLY,
              "2026-07-19T07:18:00",
              "Das Dashboard hat UTC als lokale Zeit interpretiert. Die Korrektur ist eingespielt und historische Tageswerte wurden neu berechnet."),
          message(
              "TICKET_39",
              "zoe.berger@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-08T04:02:00",
              "Ein vergessenes Diagnose-Cronjob beendet den Prozess exakt um 03:17 Uhr. Besitzer suchen, Job bis dahin deaktivieren."),
          message(
              "TICKET_40",
              "jonas.weber@harbor-demo.test",
              REPLY,
              "2026-06-22T13:15:00",
              "Im Image war ein festes Testdatum gesetzt. Der Container weiss nach dem Neustart wieder, welcher Wochentag wirklich ist."),
          message(
              "TICKET_41",
              "sofia.hartmann@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-27T02:03:00",
              "Kuehlschrank vom Produktionsnetz isoliert. Ziel war kein Payroll-Endpunkt, sondern eine falsch aufgeloeste Hersteller-Domain; trotzdem Firmware pruefen."),
          message(
              "TICKET_42",
              "clara.schulz@harbor-demo.test",
              REPLY,
              "2026-08-09T08:31:00",
              "Das Bueroalpaka existiert leider nicht. Wir setzen die Kennwoerter aller Personen zurueck, die das Formular ausgefuellt haben."),
          message(
              "TICKET_43",
              "moritz.hahn@harbor-demo.test",
              REPLY,
              "2026-09-20T16:49:00",
              "Die Tastatur sendet echte doppelte Signale. Eine Ersatztastatur ist unterwegs; bis dahin hilft die Bildschirmtastatur ohne Stottern."),
          message(
              "TICKET_44",
              "jonas.weber@harbor-demo.test",
              REPLY,
              "2026-07-11T11:16:00",
              "Der Monitor des Nachbarn war als bevorzugtes Audiogeraet gespeichert. Wir haben ihn aus deiner Docking-Konfiguration entfernt."),
          message(
              "TICKET_45",
              "jonathan.keller@harbor-demo.test",
              REPLY,
              "2026-08-21T07:22:00",
              "Bitte sende einen Verbindungsbericht mit Streckenabschnitt und Mobilfunkanbieter. Dass Tunnel besser funktionieren, nehmen wir persoenlich."),
          message(
              "TICKET_46",
              "selin.arslan@harbor-demo.test",
              REPLY,
              "2026-06-12T15:08:00",
              "Die Portalseite hat ihr eigenes Login-Cookie nicht erkannt. Der Kreislauf ist beendet; neue Gaeste sollten nur noch einmal begruesst werden."),
          message(
              "TICKET_47",
              "mila.hoffmann@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-15T18:01:00",
              "Dynamische Gruppe hat keinen Anzeigenamen, aber 214 Mitglieder. Regel exportieren und Zugriffe bis zur Pruefung einfrieren."),
          message(
              "TICKET_48",
              "paul.neumann@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-26T13:04:00",
              "Kontaktobjekt bleibt im Frontend-Cache zwischen zwei Kundenwechseln erhalten. Hotfix leert den Cache vor dem PDF-Export."),
          message(
              "TICKET_49",
              "amira.klein@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-06T20:39:00",
              "Backup nimmt sein eigenes Zielverzeichnis erneut ins Backup auf. Rekursion stoppen, bevor das Archiv den Speicher komplett auffrisst."),
          message(
              "TICKET_50",
              "fabian.lorenz@harbor-demo.test",
              REPLY,
              "2026-08-12T13:19:00",
              "Bitte Karte und Mikrowelle vorerst getrennt halten. Wir pruefen, ob der neue RFID-Chip auf derselben Frequenz Stoerungen verursacht."),
          message(
              "TICKET_1",
              "jonas.weber@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-07-03T10:03:00",
              "Geraet NB-204 reserviert. Nicht wieder das Modell aus Schrank B nehmen; das bootet aktuell nur bei gutem Zureden."),
          message(
              "TICKET_2",
              "hannah.richter@harbor-demo.test",
              REPLY,
              "2026-09-24T09:07:00",
              "Protokoll ist hochgeladen. Der Abbruch kam diesmal nach sieben Minuten und genau in dem Moment, als die Katze ueber den Router sprang."),
          message(
              "TICKET_3",
              "ben.wolf@harbor-demo.test",
              REPLY,
              "2026-08-11T15:03:00",
              "Die Rolle heisst Finance Viewer Plus. Ohne das Plus sehe ich nur ein sehr motivierendes, aber komplett leeres Diagramm."),
          message(
              "TICKET_4",
              "emma.lehmann@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-18T11:37:00",
              "Fehler tritt nur bei Angeboten mit drei Rabatten auf. Vier Rabatte funktionieren, was fachlich absurd, technisch aber reproduzierbar ist."),
          message(
              "TICKET_5",
              "noah.zimmermann@harbor-demo.test",
              REPLY,
              "2026-08-27T17:49:00",
              "Bitte prueft auch den verschwundenen Rubber-Duck-Anhaenger am Runner. Er war gleichzeitig mit dem Build weg und ich traue diesem Zufall nicht."),
          message(
              "TICKET_6",
              "sofia.hartmann@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-09-25T07:36:00",
              "Mail nutzt eine fast perfekte Lieferantenkopie, aber die IBAN gehoert zu einem Tierfutterhandel. Finance separat warnen."),
          message(
              "TICKET_7",
              "elias.koch@harbor-demo.test",
              REPLY,
              "2026-07-21T16:42:00",
              "Super. Falls es mehrere Farben gibt, bitte nicht wieder Neonorange; in Videokonferenzen sieht es aus wie ein Rettungsring."),
          message(
              "TICKET_8",
              "laura.krueger@harbor-demo.test",
              REPLY,
              "2026-09-09T13:21:00",
              "Die Pflanze bleibt stehen. Auffaellig ist noch, dass das WLAN schlechter wird, sobald die Espressomaschine aufheizt."),
          message(
              "TICKET_9",
              "mila.hoffmann@harbor-demo.test",
              INTERNAL_NOTE,
              "2026-08-30T20:31:00",
              "Altes Telefon hing noch als primaere Methode in einer zweiten Richtlinie. Dort ebenfalls entfernt; keine weiteren Altgeraete sichtbar."),
          message(
              "TICKET_10",
              "sarah.peters@harbor-demo.test",
              REPLY,
              "2026-07-29T11:22:00",
              "Die doppelten Zeilen betreffen fast nur Firmen mit 'GmbH & Co. KG'. Ein Kunde ist allerdings dreifach vorhanden und wirkt sehr zufrieden damit."));

  private static TicketMessageSeed message(
      String ticketKey, String authorEmail, TicketMessageType type, String createdAt, String body) {
    return new TicketMessageSeed(
        ticketKey, authorEmail, type, body, LocalDateTime.parse(createdAt));
  }
}
