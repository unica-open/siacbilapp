/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* Funzioni JavaScript comuni per le varizioni */

var Variazioni = (function(exports) {

    exports.togliPrimaInjezione = togliPrimaInjezione;
    exports.impostaValutaEAllineaADestra = impostaValutaEAllineaADestra;
    exports.exportResults = exportResults;
    exports.endDownload = endDownload;

    /** Opzioni base per il dataTable */
    exports.opzioniBaseDataTable = {
        // Gestione della paginazione
        bPaginate : true,
        // Impostazione del numero di righe
        bLengthChange : false,
        // Numero base di righe
        iDisplayLength : 10,
        // Sorting delle colonne
        bSort : false,
        // Display delle informazioni
        bInfo : true,
        // Calcolo automatico della larghezza delle colonne
        bAutoWidth : true,
        // Filtro dei dati
        bFilter : false,
        // Abilita la visualizzazione di 'Processing'
        bProcessing : true,
        // Internazionalizzazione
        oLanguage : {
            // Informazioni su quanto mostrato nella pagina
            sInfo : "_START_ - _END_ di _MAX_ risultati",
            // Informazioni per quando la tabella è vuota
            sInfoEmpty : "0 risultati",
            // Testo mostrato quando la tabella sta processando i dati
            sProcessing : "Attendere prego...",
            // Testo quando non vi sono dati
            sZeroRecords : "Non sono presenti capitoli corrispondenti alla variazione",
            // Definizione del linguaggio per la paginazione
            oPaginate : {
                // Link alla prima pagina
                sFirst : "inizio",
                // Link all'ultima pagina
                sLast : "fine",
                // Link alla pagina successiva
                sNext : "succ.",
                // Link alla pagina precedente
                sPrevious : "prec.",
                // Quando la tabella è vuota
                sEmptyTable : "Nessun dato disponibile"
            }
        }
    };

    /**
     * Toglie il primo valore per l'injezione da Struts2.
     *
     * @param array (string[]) l'array di cui modificare i dati
     * @returns (string[]) l'array modificato
     */
    function togliPrimaInjezione(array) {
        var i;
        // Ciclo sugli elementi in array
        for(i = 0; i < array.length; i++) {
            // Imposta il dato nell'array togliendo il primo valore dal nome
            array[i].name = array[i].name.split(".").slice(1).join(".");
        }
        return array;
    }

    /**
     * Imposta il campo come valuta e lo allinea a destra.
     *
     * @param id     (string) l'id del campo, comprensivo di #
     * @param valuta (number) la valuta da impostare
     */
    function impostaValutaEAllineaADestra(id, valuta) {
        return $(id)
            .html(valuta.formatMoney())
            .parent()
                .addClass("text-right");
    }

    /**
     * Esportazione dei risultati
     * @param params (any[])   parametri in request
     * @param url    (string)  l'url da invocare
     * @param isXlsx (boolean) se sia richiesto l'xlsx o meno
     * @param e      (Event)   l'evento scatenante
     */
    function exportResults(params, url, isXlsx, e) {
        var container = $('#iframeContainer');
        var arr = params.map(function(el) {
            return encodeURIComponent(el.name) + '=' + encodeURIComponent(el.value);
        });
        var html;

        e.preventDefault();
        // Aggiungo la richiesta dell'xlsx
        arr.push('isXlsx=' + isXlsx);
        html = '<iframe src="' + url + '?' + arr.join('&') + '" onload="Variazioni.endDownload()"></iframe>';

        $("#divEsportazioneDati").addClass('form-submitted');
        container.html(html);
    }
    
    /**
     * Callback di gestione del termine del download
     */
    function endDownload() {
        var cnt = $('#iframeContainer').find('iframe').contents();
        var html = cnt.find('body').html();
        $("#divEsportazioneDati").removeClass('form-submitted');
        if(!html) {
            //Download completato correttamente. Esco.
            return;
        }
        if(/^<pre/.test(html) && /errori/.test(html)){
            //Errore applicativo.
            var parsed;
            try {
                parsed = JSON.parse(html.replace(/<\/?pre.*?>/g, ''));
            } catch (syntaxError){
                return bootboxAlert(html);
            }
            parsed.errori = parsed.errori || [];
            return bootboxAlert('<ul><li>' + parsed.errori.map(function(el) {
                return el.codice + ' - ' + el.descrizione;
            }).join('</li><li>') + '</li></ul>');
        }
        
        //ERROR 500 o 404 o altro.
        return bootboxAlert(html);
    }

    return exports;
}(Variazioni || {}));