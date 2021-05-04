      /IF NOT DEFINED(JAX_PD1_INCLUDED)
      /DEFINE JAX_PD1_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*===============================================================
     V* 14/12/06  V2R2    AS Aggiunte istruzioni per compilazione condizionale
     V* 20/04/07  V2R2    AS Aggiunti due parametri opzionali a RxELE
     V* 31/05/07  V2R2    GR/AS Estensione campi da 256 a 2560
     V* 31/07/07  V2R3    AS Rilascio modifiche del 31/05/07
     V* 28/01/09  V2R3    AS Aggiunto parametro opzionale a RxSOS per gestione HTML
     V* 19/03/09  V2R3    BS Aggiunto parametro opzionale a RxATT per verifica individuazione
     V* 16/04/09  V2R3    BS Portate variabili RxSOS da 2560 a 30000
     V* 20/07/09  V2R3    BMA Portate variabili RxVAL da 2560 a 30000
     V* 18/09/09  V2R3    AMA Aggiunte (temporaneamente) le P_RxVALNew e P_RxATVNew
     V* 30/04/10  V2R3    AS Inserita gestione RxSOSNew
     V* 19/07/10  V3R1    AS Rilasciata RxSOSNew come RxSOS
     V* 15/10/10  V3R1    GR P_RxSPL per split colonne matrice
     V* 19/11/10  V3R1    BMA Portata variabile ritorno RxLate da 30000 a 32766
     V* 16/12/10  V3R21   RM Aggiunto parametro opzionale a RxELE
     V* 12/04/11  V3R1   BMA RxATP per restituzione lista attributi e valori elementi con parentesi
     V* 01/12/11  V3R2    AS Portato output RxATT a 30000 (il campo interno lo era già)
     V* 23/02/12  V3R2    BS Aggiunto Parametro Opzione RxATT per controllo livello 1
     V* 20/06/12  V3R2    CM Gestione stringa URL
     V* 22/12/12        ES05 Aggiunto parametro opzionale a RxLate per sostituzione singola
     V* 06/12/12  V3R2  ES05 Rilasciata modifica del 22/12/12
     V* 04/11/15  V4R1   BMA Aggiunto parametro opzionale a RxSOC per gestione HTML e Pipe
     V* 26/05/16  V4R1    BS Su XLATE aggiunto parametro per non distinguire minuscole/maiuscole
     V* 07/10/16  V5R1   BMA Aggiunto campo RxEle per indicare di cercare il tag anche in un CDATA
     V* 16/11/16  V5R1   BMA RxEle: Livelli di chiamata 48-50 riservati £JAY
     V* 16/06/17          BMA Portata variabile ritorno RxSos da 32766 a 65000 e input da 30k a 35k
     V* 19/06/17  V5R1   BMA Rilasciata modifica precedente
     V*===============================================================
      * Variabili globali delle procedure
     D £JSP            S              7  0 DIM(50)
      *--------------------------------------------------------------*
     D                 DS
     D£JAXATV                       306    DIM(200)
     D £JAXATV_A                     50    OVERLAY(£JAXATV:1)
     D £JAXATV_V                    256    OVERLAY(£JAXATV:*NEXT)
     D                 DS
     D£JAXATP                      1055    DIM(200)
     D £JAXATP_A                     50    OVERLAY(£JAXATP:1)
     D £JAXATP_V                   1000    OVERLAY(£JAXATP:*NEXT)
     D £JAXATP_L                      5  0 OVERLAY(£JAXATP:*NEXT)
      *--------------------------------------------------------------*
      * Restituisce il valore di un'attributo dei TAG
     DP_RxVAL          PR         65000    VARYING
     D $XmlTag                    65000    CONST VARYING
     D $XmlAtt                       64    CONST
      *
      * Restituisce il valore di un'attributo di tipo aaa(
     DP_RxATT          PR         30000    VARYING
     D $XmlTag                    30000    CONST VARYING
     D $XmlATT                       64    CONST VARYING
     D $XmlASS                       15    CONST
     D $XmlFND                        1N   OPTIONS(*NOPASS)
     D $XmlLIV                        1    OPTIONS(*NOPASS)
      *
      * Sostituzione dei caratteri non validi PER XML
     DP_RxSOS          PR         65000    VARYING
     D $XmlSOS                    35000    CONST VARYING
     D $StrLang                       1    CONST OPTIONS(*NOPASS)
      *
      * Sostituzione stringa in carattere
     DP_RxSOC          PR         30000    VARYING
     D $XmlSOC                    30000    CONST VARYING
     D $StrLang                       1    CONST OPTIONS(*NOPASS)
      *
      * Sostituzione dei caratteri non validi PER URL
     DP_RxURL          PR         30000    VARYING
     D $XmlURL                    30000    CONST VARYING
      *
      * Trasformazione Stringa
     DP_RxLATE         PR         32766    VARYING
     D $XmlINP                    30000    CONST VARYING
     D $XmlS01                    30000    CONST VARYING
     D $XmlS02                    30000    CONST VARYING
     D $SosSing                       1    CONST OPTIONS(*NOPASS)
     D $SosCase                       1    CONST OPTIONS(*NOPASS)
      *
      * Restituisce il contenuto dell'elemento richiesto
     DP_RxELE          PR         65000    VARYING
     D Xml_Fnd                      512    VARYING CONST
     D Xml_Met                       10    CONST
      * .livello di chiamata
      * ... livelli 48-50 riservati £JAY
     D Xml_Liv                        2  0 CONST
     D Xml_Str                    65000    VARYING CONST
     D NodIni                         5  0 OPTIONS(*NOPASS)
     D NodLen                         5  0 OPTIONS(*NOPASS)
     D Xml_Con                    65000    OPTIONS(*NOPASS) VARYING
      * . Cerca tag anche all'interno di un CDATA
     D SEACDATA                       1    OPTIONS(*NOPASS)
      *
      * Restituisce la lista degli attributi e dei valori di un elemento
     DP_RxATV          PR           306    DIM(200)
     D Xml_Str                    30000    VARYING
     D  $XmlPRG                       5  0 OPTIONS(*NOPASS)
      *
      * Restituisce la lista degli attributi e dei valori di un elemento di documentazione attiva
     DP_RxATP          PR          1055    DIM(200)
     D Xml_StrP                   30000    VARYING
     D  $XmlPRGp                      5  0 OPTIONS(*NOPASS)
      *
      * Restituisce una stringa (es. intestazione matrice) splittata su più righe
     DP_RxSPL          PR         30000    VARYING
     D $String                    30000    CONST VARYING
     D $Split                         1    CONST
      /ENDIF
