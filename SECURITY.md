# Security Analysis
Minimaal drie van de kwetsbaarheden worden geanalyseerd

## A1:2017 Injection
Deze kwetsbaarheid houdt in dat een verzoek een bepaalde sql query bevat die wordt geïnjecteerd in een sql query, 
waardoor een hacker gevoelige informatie uit de database zou kunnen halen.

###Risico
Het risico voor dit project is niet heel groot, omdat er geen directe koppeling zit tussen de gebruiker en de database queries.
Autenticatie en autorisatie hebben geen impact op het risico van injectie. Het is slechts een extra tussen stap.

### Risico vermijding binnen dit project
Bovenop dat er geen directe koppeling is van input naar de database queries maakt de JpaRepository gebruik van de 
Criteria API waardoor hij meteen uit de parameters wordt geëscaped.

## A7:2017 Cross-Site Scripting(XSS)
Cross-Site Scripting is gebruikmaken van kwetsbaarheden in een site door stukjes html en of javascript mee te sturen als input wat vervolgens weer door andere gebruikers automatisch wordt gerund.  
Wanneer het runt kan het allemaal dingen die opgeslagen zijn in de browser online doorsturen naar de hacker.   
Die kan gebruik maken van o.a. tokens.   

###Risico
Dit project is niet heel kwetsbaar voor XSS, omdat het geen front end heeft die javascript of html kan planten en of uitvoeren.  
Autorisatie en autenticatie zal hier geen verschil in maken, het grote gevaar bij XSS is juist als je via XSS toegang krijgt tot inlog info of dergelijke van iemand met een hoge autorisatie.  

### Risico vermijding binnen dit project
Dit project maakt geen gebruik van een front end en ook niet van HTML of JavaScript.

## A9:2017 Using Components with Knows Vulnerabilities
Deze kwetsbaarheid houdt in dat je gebruik maakt van een (waarschijnlijk oudere) versie van 
een dependency die een bug of kwetsbaarheid bevat die jouw applicatie in gevaar kan brengen mocht die geëxploiteerd worden.

### Risico
Het risico in dit project is relatief klein, omdat er niet enorm veel verschillende dependencies worden gebruikt.  
Authenticatie en autorisatie kan helpen tegen deze kwetsbaarheid, omdat, zolang de kwetsbare dependencies niet de authenticatie dependencies zijn,  
 zal authenticatie en autorisatie een extra stap brengen tussen de gebruiker en de mogelijk te exploiteren code / dependency

### Risico vermijding binnen dit project
Binnen dit project wordt gebruik gemaakt van de OWASP dependency checker plugin die de gebruikte dependencies vergelijkt met mogelijk onveilige versies van dependencies die online staan geregistreerd.  
    Ook wordt er gebruik gemaakt van dependabot die automatisch dit project scant en check of er onveilige (versies van ) dependencies worden gebruikt.