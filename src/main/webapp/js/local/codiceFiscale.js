/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Metodi di utilità per il calcolo del codice fiscale

var checkCodiceFiscale = (function () {
    "use strict";
    // Array di controllo
    var arrayControllo = [1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23],
        regexp = /^[A-Z]{6}[0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{3}[A-Z]$/i,
        zeroCharCode = '0'.charCodeAt(0),
        alphaCharCode = 'A'.charCodeAt(0);

    /**
     * Calcola il carattere di controllo per un dato codice fiscale.
     *
     * @param cf (String) il codice fiscale il cui carattere di controllo &eacute; da controllare
     *
     * @returns (String) il carattere di controllo
     */
    function calcolaCarattereControllo(cf) {
        var sum = 0,
            index,
            c,
            x;

        for (index = 0; index < cf.length - 1; index++) {
            c = cf.charAt(index);
            x = c.charCodeAt(0);
            x -= (/\d/.test(c) ? zeroCharCode : alphaCharCode);
            sum += ((index + 1) % 2 === 0 ? x : arrayControllo[x]);
        }
        sum %= 26;
        return String.fromCharCode(sum + alphaCharCode);
    }

    /**
     * Calcola il check sul carattere di controllo.
     *
     * @param cf (String) il codice fiscale da controllare
     *
     * @returns (Boolean) <code>true</code> se il carattere di controllo &eacute; corretto; <code>false</code> in caso contrario
     */
    function controllaCarattereControllo(cf) {
        return cf.charAt(15) === calcolaCarattereControllo(cf);
    }

    /**
     * Calcola la correttezza del codice fiscale.
     *
     * @param cf (String) il codice fiscale da controllare
     *
     * @returns (Boolean) <code>true</code> se il codice fiscale &eacute; formalmente corretto; <code>false</code> in caso contrario
     */
    function calcolaCF(cf) {
        var codiceFiscale = cf.toUpperCase();
        return regexp.test(codiceFiscale) && controllaCarattereControllo(codiceFiscale);
    }

    return calcolaCF;
}());

var checkCodiceFiscaleEvenTemporary = (function () {
    "use strict";

    var regexp = /\d{11}/;

    /**
     * Reduces a number to a single-digit one.
     *
     * @param i the number to reduce
     *
     * @return the reduced number
     */
    function toSingleDigit(i) {
        var result = 0;
        if (i < 0) {
            return toSingleDigit(-i);
        }
        if (i < 10) {
            return i;
        }

        while (i > 0) {
            result += i % 10;
            i /= 10;
        }

        return toSingleDigit(result);
    }

    /**
     * Calcola il carattere di controllo per un dato codice fiscale.
     *
     * @param cf (String) il codice fiscale il cui carattere di controllo &eacute; da controllare
     *
     * @returns (String) il carattere di controllo
     */
    function calcolaCarattereControlloTemporaneo(cf) {
        var sum = 0,
            index,
            c,
            x,
            result;

        for (index = 0; index < cf.length - 1; index++) {
            c = cf.charAt(index);
            x = c.charCodeAt(0) - zeroCharCode;
            sum += ((index + 1) % 2 === 0 ? toSingleDigit(2 * x) : x);
        }
        result = 10 - (sum % 10);
        return String.fromCharCode(result + zeroCharCode);
    }

    /**
     * Calcola il check sul carattere di controllo.
     *
     * @param cf (String) il codice fiscale da controllare
     *
     * @returns (Boolean) <code>true</code> se il carattere di controllo &eacute; corretto; <code>false</code> in caso contrario
     */
    function controllaCarattereControlloTemporaneo(cf) {
        return cf.charAt(10) === calcolaCarattereControlloTemporaneo(cf);
    }

    /**
     * Controlla il Codice fiscale temporaneo.
     *
     * @param cf (String) il codice fiscale da controllare
     *
     * @returns (Boolean) <code>true</code> se il codice fiscale &eacute; formalmente corretto; <code>false</code> in caso contrario
     */
    function controllaCodiceFiscaleTemporaneo(cf) {
        var codiceFiscale = cf.toUpperCase();
        return regexp.test(codiceFiscale) && controllaCarattereControlloTemporaneo(codiceFiscale);
    }

    /**
     * Calcola la correttezza formale del codice fiscale, anche temporaneo.
     *
     * @param cf (String) il codice fiscale da controllare
     *
     * @returns (Boolean) <code>true</code> se il codice fiscale &eacute; formalmente corretto; <code>false</code> in caso contrario
     */
    function calcolaCF(cf) {
        return checkCodiceFiscale(cf) || controllaCodiceFiscaleTemporaneo(cf);
    }

    return calcolaCF;
}());