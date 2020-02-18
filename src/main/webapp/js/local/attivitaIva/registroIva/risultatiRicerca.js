/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    // Per lo sviluppo
    "use strict";
    var functions = {};

    functions.elimina = eliminaRegistro;
    functions.blocca = bloccaRegistro;
    functions.sblocca = sbloccaRegistro;
    functions.recupera = recuperaProtocollo;
    functions.allinea = allineaProtocolloIva;


    //utilita'
    /**
     * Metodo generico per chiamare la Action mediante l'url specificato e inviando l'oggetto specificato.
     * Si occupa anche di ricaricare la tabella Datatable.
     * @param url (String) l'url
     * @param obj (Object) l'oggetto per la chiamata
     * @param row (jQuery) la riga
     */
    function chiamaActionERicaricaTabella(url, obj, row){
        var tabellaDatatable = $('#risultatiRicercaRegistroIva').dataTable();
        row.overlay('show');

        $('#modaleConferma').modal('hide');

        return $.postJSON(url, obj)
        .then(function(data){
            var errori = data.errori;
            if(impostaDatiNegliAlert(errori, $('#ERRORI'))){
                return $.Deferred().reject().promise();
            }
            impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'));
        })
        .always(row.overlay.bind(row, 'hide'))
        .then(ricaricaTabella.bind(undefined, tabellaDatatable))
        .then(tabellaDatatable.fnDraw.bind(tabellaDatatable));
    }

    /**
     * Aggiorna la tabella di ricerca dopo avere effettuato l'aggiornamento dei piani dei conti
     * @param dataTable (jQuery) la tabella
     */
    function ricaricaTabella(dataTable){
        if(!dataTable) {
            return $.Deferred().resolve().promise();
        }
        var settings = dataTable.fnSettings();
        var obj = {
            forceRefresh: true,
            iTotalRecords: settings._iRecordsTotal,
            iTotalDisplayRecords: settings._iRecordsDisplay,
            iDisplayStart: settings._iDisplayStart,
            iDisplayLength: settings._iDisplayLength
        };
        return $.postJSON("risultatiRicercaRegistroIvaAjax.do", obj);
   }

    /**
     * Imposta e apre la modale di richiesta di conferma.
     *
     * @param uid                           (any)      l'uid del registro
     * @param codice                        (String)   il codice del registro
     * @param descrizioneAzioneDaConfermare (String)   la descrizione della richiesta di conferma
     * @param fncHandlerConferma            (Function) l'handler del click sul pulsante di conferma
     * @param row                           (jQuery)   la riga della tabella
     */
    function richiediConferma(uid, codice, descrizioneAzioneDaConfermare, fncHandlerConferma, row){
        //clickOnEliminaTest(elimina,registro);
        var modal = $("#modaleConferma");
        // Imposto il codice del registro
        $("#SPAN_registroSelezionato").html(codice);
        $("#pulsanteNo").substituteHandler("click", modal.modal.bind(modal, "hide"));
        $("p.descrizione-azione-da-confermare").html(descrizioneAzioneDaConfermare);
        //se non ho passato la function di handler, non legare niente al pulsante
        if(fncHandlerConferma && typeof fncHandlerConferma === "function"){
            $("#pulsanteSi").substituteHandler("click", fncHandlerConferma.bind(undefined, uid, row));
        }
        // Apro il modale
        modal.modal("show");
    }

    //azioni possibili su registro

    /**
     * Metodo per l'eliminazione di un registro Iva
     * @param uidRegistro (any) l'uid del registro selezionato
     */
    function eliminaRegistro(uidRegistro){
        document.location = "risultatiRicercaRegistroIva_elimina.do?uidDaEliminare=" + uidRegistro;
    }
    /**
     * Recupero del protocollo
     * @param uidRegistro (any) l'uid del registro selezionato
     */
    function recuperaProtocollo(uidRegistro) {
        document.location = "risultatiRicercaRegistroIva_recuperaProtocollo.do?uidRegistroSelezionato=" + oData.uid;
    }
    /**
     * Blocca il registro Iva
     * @param uidRegistro (any)    l'uid del registro selezionato
     * @param row         (jQuery) la riga
     * */
    function bloccaRegistro(uidRegistro, row){
        var url ="risultatiRicercaRegistroIva_bloccaRegistro.do";
        var obj = {"uidRegistroSelezionato" : uidRegistro};
        chiamaActionERicaricaTabella(url, obj, row);
    }
    /**
     * Sblocca il registro Iva
     * @param uidRegistro (any)    l'uid del registro selezionato
     * @param row         (jQuery) la riga
     */
    function sbloccaRegistro(uidRegistro,row){
        var url ="risultatiRicercaRegistroIva_sbloccaRegistro.do";
        var obj = {"uidRegistroSelezionato" : uidRegistro};
        chiamaActionERicaricaTabella(url, obj, row);
    }
    /**
     * Allinea i protocolli del registro iva
     * @param uidRegistro (any)    l'uid del registro selezionato
     * @param row         (jQuery) la riga
     */
    function allineaProtocolloIva(uidRegistro,row){
        var url ="risultatiRicercaRegistroIva_allineaProtocolli.do";
        var obj = {"uidRegistroSelezionato" : uidRegistro};
        chiamaActionERicaricaTabella(url, obj, row);
    }

    /**
     * Apre e popola la modale di consultazione del registro
     *
     * @param e (Event) l'evento
     */
    function consultaRegistro(e) {
        var modal = $("#modaleConsulta");
        var $element = $(e.target);
        e.preventDefault();

        // Popolo i dati del modale
        $("#DL_gruppoAttivitaIva").html($element.data('gruppoAttivitaIva'));
        $("#DL_tipoRegistroIva").html($element.data('tipoRegistroIva'));
        $("#DL_codice").html($element.data('codice'));
        $("#DL_descrizione").html($element.data('descrizione'));
        $("#DL_liquidazione").html($element.data('liquidazioneIva'));

        // Apro il modale
        modal.modal("show");
    }

    ///caricamento della tabella

    /**
     * Caricamento via Ajax della tabella e visualizzazione della stessa.
     */
    function visualizzaTabella() {
        var options = {
            bServerSide: true,
            sAjaxSource: "risultatiRicercaRegistroIvaAjax.do",
            sServerMethod: "POST",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "codice"},
                {aTargets: [1], mData: "descrizione"},
                {aTargets: [2], mData: "gruppoAttivitaIva"},
                {aTargets: [3], mData: "descrizioneTipoRegistroIva"},
                {aTargets: [4], mData: "isFlagLiquidazioneIva"},
                {aTargets: [5], mData: "isRegistroBloccatoStringa"},
                {aTargets: [6], mData: "azioni"}
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }

        $("#risultatiRicercaRegistroIva").dataTable(options);
    }

    /**
     * Lanciatore per la richiesta della conferma
     * @param e (Event) l'evento
     */
    function askForOperation(e) {
        var $element = $(e.target);
        var codice = $element.data('codice');
        var uid = $element.data('uid');
        var fnc = $element.data('function');
        var row = $element.closest('tr');
        var msg = $element.data('msg');
        e.preventDefault();

        richiediConferma(uid, codice, msg, functions[fnc], row);
    }

    $(function() {
        visualizzaTabella();
        $(document).on('click', '.consultaRegistroIva', consultaRegistro);
        $(document).on('click', 'a[data-ask]', askForOperation);
    });
}(jQuery));