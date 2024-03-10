## IMDB
Această aplicație reprezintă o implementare a site-ului IMDB folosind Java și Swing. Pentru realizarea aplicației au fost create următoarele pachete:

- **appstructure:** Conține clase legate de "structura" programului și elemente esențiale pentru funcționarea acestuia.
- **enumpack:** Conține enumerările impuse.
- **experiencepack:** Include clasele folosite pentru implementarea design pattern-ului "Strategy Pattern".
- **guipack:** Cuprinde clase legate de SWING utilizate pentru a implementa interfața grafică. Fiecare element al unei componente grafice, a cărui date sunt modificate, este redesenat pe baza noilor date, rezultând o interfață interactivă.
- **observerpack:** Conține clasele folosite pentru implementarea design pattern-ului "Observer Pattern".
- **productionpack:** Include clasele care implementează producțiile din aplicație conform cerințelor. Este de menționat că doar Contributorii și Adminii pot modifica producțiile și actorii și doar pe aceia de care se ocupă.
- **usefulpack:** Conține entități utilizate constant de alte elemente ale aplicației, cum ar fi clasa statică UtilFunction care conține funcții pentru manipularea datelor în modul dorit.
- **userpack:** Conține clasele care implementează utilizatorii din aplicație conform cerințelor. Se subliniază faptul că doar adminii pot adăuga utilizatori noi și aceștia sunt de tipul Regular.

## Mentiuni:
- Ca funcționalitate suplimentară, am implementat criptarea completă a parolelor din sistem folosind biblioteca "jbcrypt". Astfel, tehnica de "Hashing lent" oferă securitate maximă.
- Aplicația poate fi rulată atât în terminal, cât și în interfața grafică, însă unele dintre funcționalitățile interfeței grafice nu au fost implementate și pentru rularea în terminal.
- Datorită "monotoniei" acțiunii de a încărca imagini noi pentru actori și producțiile din sistem, ne-am limitat la un număr redus de câteva imagini în scop demonstrativ.
- Anumite elemente de design ale aplicatiei au fost folosite pentru a implementa cat mai multe concepete din Java, chiar daca nu se impunea acest lucru

## Fișiere JSON:
Fișierele JSON sunt folosite doar pentru încărcarea datelor inițiale, iar fișierul accounts.json poate fi utilizat pentru a găsi credențialele necesare pentru testarea autentificării.
