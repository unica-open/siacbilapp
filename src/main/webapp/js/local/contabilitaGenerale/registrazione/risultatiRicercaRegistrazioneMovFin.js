/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {

    var dataTable;
    
    /**
     * Caricamento via Ajax della tabella delle richieste e visualizzazione.
     */
    function visualizzaTabelleRicheste() {
        var options = {
            bServerSide: true,
            sAjaxSource: "risultatiRicercaRegistrazioneMovFinFINAjax.do",
            sServerMethod: "POST",
            bPaginate: true,
            bLengthChange: false,
            bDestroy: true,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: false,
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
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            "aoColumnDefs" : [
                {aTargets: [0], mData: "stringaMovimento"},
                {aTargets: [1], mData: "isResiduo"},
                {aTargets: [2], mData: "stringaEvento"},
                {aTargets: [3], mData: "stringaStatoOperativoRegistrazioneMovFin"},
                {aTargets: [4], mData: "stringaDataRegistrazione"},
                {aTargets: [5], mData: "stringaContoIniziale"},
                {aTargets: [6], mData: "stringaContoAggiornato"},
                {aTargets: [7], mData: "azioni", fnCreatedCell: function (nTd, sData, oData) {
                    $(nTd).find("a[href='#msgAnnulla']")
                        .substituteHandler("click", function() {
                            clickOnAnnulla(oData.uid);
                        });
                    $(nTd).find("a[href='#aggiornaPdC']")
                    .substituteHandler("click", function() {
                        $("form").overlay('show');
                        aggiornaPdC(oData.uid, oData.stringaContoIniziale, oData.uidPianoDeiContiFIN, oData.tipoElenco);
                    });
                    $('.dropdown-toggle', nTd).dropdown();
                }}
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }

        dataTable = $("#risultatiRicercaRegistrazioniMovfin").dataTable(options);
    }
    
    var zTreeSettingsBase = {
            check: {
                enable: true,
                chkStyle: "radio",
                radioType: "all"
            },
            data: {
                key: {
                    name: "testo",
                    children: "sottoElementi"
                },
                simpleData: {
                    enable: true
                }
            }
        };
    
    /**
     * Funzione per la creazione di una stringa contenente le informazioni gerarchiche della selezione. Genera solo il codice degli elementi non-foglia.
     *
     * @param treeNode       {Object}  il nodo selezionato
     * @param messaggioVuoto {String}  messaggio da impostare qualora non sia stato selezionato nulla
     * @param regressione    {Boolean} indica se effettuare una regressione sui vari codici
     *
     * @returns {String} la stringa gerarchica creata
     */
    function creaStringaGerarchica(treeNode, messaggioVuoto, regressione) {
        var nodes = treeNode;
        var parent;
        var string;

        if (!nodes.checked) {
            return messaggioVuoto;
        }
        if (!regressione) {
            return nodes.testo;
        }

        string = nodes.testo;
        parent = nodes.getParentNode();
        while (parent !== null) {
            nodes = parent;
            string = nodes.codice + " - " + string;
            parent = nodes.getParentNode();
        }

        return string;
    }

    
    /**
     * Controllare che la selezione dell'elemento del Piano dei Conti corrisponda almeno al quarto livello dello stesso.
     *
     * @param treeId   {String} l'id univoco dello zTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     *
     * @returns {Boolean} <code>true</code> qualora il livello selezionato sia valido, e scatena pertanto l'evento onClick;
     *                    <code>false</code> in caso contrario, e inibisce lo scatenarsi dell'evento onclick.
     */
    function controllaLivelloPianoDeiConti(treeId, treeNode) {
        var livello;
        if(treeId === undefined) {
            throw new ReferenceError("Nessun treeId fornito");
        }
        livello = calcolaLivelloPianoDeiConti(treeNode);

        // Se il livello non e' almeno pari a 4, segnalo l'errore
        if (livello <= 4) {
            impostaDatiNegliAlert(["Selezionare almeno il quinto livello del Piano dei Conti. Livello selezionato: " + livello], $("#ERRORI_modaleAggiornaPdC"), false);
        }
        return livello > 4;
    }
    
    
    /**
     * Calcola il livello del piano dei conti.
     *
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     *
     * @returns {Integer} il livello del piano dei conti
     */
    function calcolaLivelloPianoDeiConti(treeNode) {
        var array = treeNode.codice.split(".");
        var index;
        for(index = 1; index < array.length && array[index] > 0; index++) {/* Empty */}
        return --index;
    }
    /**
     * Imposta il valore dell'Elemento del Piano Dei Conti in un campo hidden per la gestione server-side.
     *
     * @param event    {Object} l'evento generato
     * @param treeId   {String} l'id univoco dello zTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     */
    function impostaValueElementoPianoDeiConti(event, treeId, treeNode) {
        var stringa = creaStringaGerarchica(treeNode, "Nessun P.d.C. finanziario selezionato", false);
        valorizzaCampi(treeNode, "ElementoPianoDeiConti", stringa);
    }
    
    /**
     * Metodo di utilit&agrave; per l'impostazione dei dati da zTree a un campo hidden.
     *
     * @param treeNode      {Object} l'oggetto JSON corrispondente alla selezione
     * @param idCampoHidden {String} l'id del campo hidden, senza la sotto-stringa HIDDEN_
     * @param stringa       {String} la stringa contenente la descrizione estesa (ovvero comprendente la descrizione degli elementi superiori in gerarchia) della selezione
     */
    function valorizzaCampi(treeNode, idCampoHidden, stringa) {
        if(treeNode.checked) {
            $("#HIDDEN_" + idCampoHidden + "UidFIN").val(treeNode.uid);
            $("#HIDDEN_" + idCampoHidden + "CodiceFIN").val(treeNode.codice);
        } else {
            $("#HIDDEN_" + idCampoHidden + "UidFIN").val("");
            $("#HIDDEN_" + idCampoHidden + "CodiceFIN").val("");
        }

        $("#HIDDEN_" + idCampoHidden + "StringaFIN").val(stringa);
        $("#HIDDEN_" + idCampoHidden + "CodiceTipoClassificatoreFIN").val(treeNode.codiceTipo);
        $("#SPAN_" + idCampoHidden+"FIN").html(stringa);
    }
    
    /**
     * effettua la chiamata ajax ed effettua l'aggiornamento dei piani dei conti 
     */
    function aggiornaPdC(uidRegistrazione, stringaPdC, uidPianoDeiConti, tipoEvento){
        $('#ERRORI_modaleAggiornaPdC').slideUp();
        $('#campiEntrata').hide();
        $('#campiSpesa').hide();
        $('#campi' + tipoEvento).slideDown();
        $("#HIDDEN_ElementoPianoDeiContiUidFIN").val(uidPianoDeiConti);

        $("#HIDDEN_RegistrazioneUidFin").val(uidRegistrazione);
        
        if(tipoEvento === "Spesa") {
            caricaClassificatoriSpesa(uidPianoDeiConti);
        }else{
            caricaClassificatoriEntrata(uidPianoDeiConti);
        }
        
        var spinner = $('#spinner');
        
        $('#confermaModaleAggiornaPdC').substituteHandler('click', function(){
            var modale = $('#aggiornaPdC');
            var obj;
            uidPianoDeiConti = $("#HIDDEN_ElementoPianoDeiContiUidFIN").val();
            if(!uidPianoDeiConti) {
                impostaDatiNegliAlert(["Necessario selezionare un Piano dei Conti "], $("#ERRORI_modaleAggiornaPdC"));
                return;
            }
            obj = {"uidPianoDeiContiRegMovFinAggiornato": uidPianoDeiConti, "uidRegistrazioneDaAggiornare": uidRegistrazione};
            spinner.addClass("activated");
            
            $.postJSON("risultatiRicercaRegistrazioneMovFinFINAction_aggiornaPianoDeiContiRegistrazione.do",obj)
            .then(function(data){
                var errori = data.errori;
                var alertErrori = $("#ERRORI_modaleAggiornaPdC");
                if(impostaDatiNegliAlert(errori, alertErrori)){
                    return $.Deferred().reject().promise();
                }
                return ricaricaTabella();
            })
            .then(dataTable.fnDraw.bind(dataTable))
            .then(modale.modal.bind(modale, 'hide'))
            .always(spinner.removeClass.bind(spinner,"activated"));
            
        });
        
        $('#chiudiModaleAggiornaPdc').substituteHandler('click', function(){
            $('#aggiornaPdC').modal('hide');
        });
    }

    /**
     * Controlla se la select sia vuota.
     */
    function selectVuota (objectVal) {
        return !objectVal;
    }
    
    function svuotaPDC() {
        // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
        $("#HIDDEN_ElementoPianoDeiContiUidFIN").val("");
        $("#HIDDEN_ElementoPianoDeiContiStringaFIN").val("");
        $("#HIDDEN_ElementoPianoDeiContiCodiceFIN").val("");
        $("#SPAN_ElementoPianoDeiContiFIN").html("Nessun P.d.C. finanziario selezionato");
    }
    /**
     * Funzione per l'impostazione dello zTree.
     *
     * @param idList       {String} l'id dell'elemento in cui sar&agrave; posto lo zTree
     * @param setting      {Object} le impostazioni dello zTree
     * @param jsonVariable {Object} la variabile con i dati JSON per il popolamento dello zTree
     */
    function impostaZTree(idList, setting, jsonVariable) {
        var tree = $.fn.zTree.init($("#" + idList), setting, jsonVariable);
        var idCampo = "ElementoPianoDeiConti";
        var uid = parseInt($("#HIDDEN_" + idCampo + "UidFIN").val(), 10);
        var node;

        // Se l'uid e' selezionato l'elemento corrispondente
        if(!isNaN(uid)) {
            node = tree.getNodeByParam("uid", uid);
            // Evito il check nel caso in cui il nodo sia null
            !!node && tree.checkNode(node, true, true, true);
        }
    }

   /** Carica i dati nello zTree dell'Elemento del Piano Dei Conti da chiamata AJAX.
    *
    * @param obj             {Object}  l'oggetto chiamante
    *
    * @returns (jQuery.Deferred) l'oggetto deferred corrispondente alla chiamata AJAX
    **/
   function caricaPianoDeiConti(obj) {
       var id = obj.val();
       /* Settings dello zTree */
       var zTreeSettings = $.extend(true, {}, zTreeSettingsBase, {callback: {beforeCheck: controllaLivelloPianoDeiConti, onCheck: impostaValueElementoPianoDeiConti}});
       /* Spinner */
       var spinner = $('#spinner');
       var elementoPianoDeiContiGiaSelezionato = $("#HIDDEN_ElementoPianoDeiContiUidFIN").val();
       /* Attiva lo spinner */
       spinner.addClass("activated");
       svuotaPDC();
       return $.postJSON(
           "ajax/elementoPianoDeiContiAjax.do",
           {"id" : id},
           function (data) {
               var listaElementoPianoDeiConti = (data.listaElementoCodifica);
               var options = $("#bottonePdCFIN");
               var codiceTitolo;
               var isCodiceTitoloImpostato;

               var albero;
               var node;
               var isListaElementoPianoDeiContiNonVuota = listaElementoPianoDeiConti && listaElementoPianoDeiConti.length > 0;

               if (!selectVuota($('#titoloEntrataFIN').val())) {
                   codiceTitolo = $('#tipologiaTitoloFIN').val();
                   isCodiceTitoloImpostato = !!codiceTitolo;
               }
               
               if (!selectVuota($('#titoloSpesa').val())) {
                   codiceTitolo = $('#titoloSpesa').val();
                   isCodiceTitoloImpostato = !!codiceTitolo;
               }
               
               impostaZTree("treePDCFIN", zTreeSettings, listaElementoPianoDeiConti);
               // Se il bottone  disabilitato, lo si riabiliti
               if (options.attr("disabled") === "disabled") {
                   options.removeAttr("disabled");
               }
               $("form").overlay('hide');
//             Controllo se sono nel caso d'uso di entrata: in tal caso, effettuo il check del primo elemento nel caso in cui non vi sia un altro elemento selezionato
               if(isCodiceTitoloImpostato && elementoPianoDeiContiGiaSelezionato !== "" && isListaElementoPianoDeiContiNonVuota) {
                   albero = $.fn.zTree.getZTreeObj("treePDCFIN");
                   node = albero && albero.getNodesByParam('uid', elementoPianoDeiContiGiaSelezionato)[0];
                   albero && node && albero.checkNode(node, true, true, true);

               }
              
                   $('#aggiornaPdC').modal('show');

           }
       ).always(
               function() {
                   // Disattiva lo spinner anche in caso di errore
                   spinner.removeClass("activated");
                   $("form").overlay('hide');

               }
       );
   }
    
   /**
    * deseleziona il checked node
    * 
    */
   
   function deseleziona(zTreeId) {
       var tree = $.fn.zTree.getZTreeObj(zTreeId);
       var nodo = tree && tree.getCheckedNodes(true)[0];
       if(nodo) {
           tree.checkNode(nodo, false, true, true);
       }
   }
   
   /**
    * caricaClassificatoriSpesa
    * effettua una chaiamta ajax per caricare le liste tipologia,categoria,titolo(entrata)
    * preimposta le select con i campi ottenuti 
    */
   function caricaClassificatoriSpesa(uidPianoDeiConti){
	   	var obj = {};
        obj.uidPianoDeicontiRegMovFin = uidPianoDeiConti;
        $.postJSON('risultatiRicercaRegistrazioneMovFinFINAction_leggiClassificatoriSpesaByContoFin.do', {"uidPianoDeiContiRegMovFin" : uidPianoDeiConti}, function(data){
            var alertErrori = $('#ERRORI_modaleAggiornaPdC');
            var errori = data.errori;
            var $selectTitolo = $('#titoloSpesa');
            var $selectMacroaggregato = $('#macroaggregato');
            if(impostaDatiNegliAlert(errori, alertErrori)){
                return;
            }

            $selectTitolo.val(data.uidTitoloSpesaConto);
            $selectTitolo.substituteHandler('change',caricaMacroaggregato);
            caricaSelectCodifiche($selectMacroaggregato, data.listaMacroaggregato);
            $selectMacroaggregato.val(data.uidMacroaggregatoConto);
            $selectMacroaggregato.substituteHandler("change", caricaPianoDeiContiSpesa).change();
            
        });
    }
    
   /**
    * caricaPianoDeiContiSpesa
    * richiama il carica piano dei conti caso spesa 
    */
    function caricaPianoDeiContiSpesa(){
        caricaPianoDeiConti($('#macroaggregato'));
    }
    
    /**
     * caricaPianoDeiContiEntrata
     * richiama il carica piano dei conti caso entrata 
     */
    function caricaPianoDeiContiEntrata(){
        caricaPianoDeiConti($('#categoriaTipologiaTitoloFIN'));
    }
    
    
    /**
     * caricaClassificatoriEntrata
     * effettua una chaiamta ajax per caricare le liste tipologia,categoria,titolo(entrata)
     * preimposta le select con i campi ottenuti 
     */
    function caricaClassificatoriEntrata(uidPianoDeiConti){
        var obj = {};
        obj.uidPianoDeiContiRegMovFin = uidPianoDeiConti;
        $.postJSON('risultatiRicercaRegistrazioneMovFinFINAction_leggiClassificatoriEntrataByContoFin.do', qualify(obj), function(data){
            var alertErrori = $('#ERRORI_modaleAggiornaPdC');
            var errori = data.errori;
            var $selectTitolo = $('#titoloEntrataFIN');
            var $selectTipologia = $('#tipologiaTitoloFIN');
            var $selectCategoria = $('#categoriaTipologiaTitoloFIN');
            
            if(impostaDatiNegliAlert(errori, alertErrori)){
                return;
            }
            
            $selectTitolo.val(data.uidTitoloEntrataConto);
            $selectTitolo.substituteHandler('change',gestisciTipologia);
            caricaSelectCodifiche($selectTipologia , data.listaTitoloTipologia);
            $selectTipologia.val(data.uidTipologiaTitoloConto);
            $selectTipologia.substituteHandler('change',gestisciCategoria);
            caricaSelectCodifiche($selectCategoria , data.listaTipologiaCategoria);
            $selectCategoria.val(data.uidCategoriaConto);
            $selectCategoria.substituteHandler('change', caricaPianoDeiContiEntrata).change();

        });
    }
    
    /**
     * Carica i dati nella select del Macroaggregato da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
     function caricaMacroaggregato() {
        var idTitoloSpesa = $("#titoloSpesa").val();
        $("#macroaggregato").overlay("show");

        // Pulisco il valore dei campi riferentesi all'elemento del piano dei cMonti
        pulisciPDC();
        
        // Effettuo la chiamata JSON
        return $.postJSON(
            "ajax/macroaggregatoAjax.do",
            {"id" : idTitoloSpesa},
            function (data) {
                var listaMacroaggregato = (data.listaMacroaggregato);
                var errori = data.errori;
                var options = $("#macroaggregato");
                var bottonePdC = $("#bottonePdCFin");

                options.find('option').remove().end();
                if(errori.length > 0) {
                    options.attr("disabled", "disabled");
                    return;
                }

                if (options.attr("disabled") === "disabled") {
                    options.removeAttr("disabled");
                }
                if (bottonePdC.attr("disabled") !== "disabled") {
                    bottonePdC.attr("disabled", "disabled");
                }

                options.append($("<option />").val("").text(""));
                $.each(
                    listaMacroaggregato,
                    function () {
                        options.append($("<option />").val(this.uid).text(this.codice + '-' + this.descrizione));
                    }
                );
            }).always(function() {
            // Chiudo l'overlay
                $("#macroaggregato").overlay("hide");       
            });
    }
    
    /**
     * pulisce e sbianca i campi del Piano dei conti (comprende anche i campi hidden )
     */
    function pulisciPDC(){
        //sbianco i campi
        svuotaPDC();
        //svuoto la select
        deseleziona('treePDCFIN');
        //la disabilito
        $("#bottonePdCFIN").attr("disabled", true).prop('disabled', true);
    }
    
   
    /**
     * caricaTipologia
     * Effettua una chiamata ajax per caricare la tipologia
     */
    function caricaTipologia() {
       var idTitoloEntrata = $("#titoloEntrataFIN").val();
       // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti

       //Pulisco la select della categoria
       $("#categoriaTipologiaTitoloFIN").val("");
       // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
       $("#HIDDEN_ElementoPianoDeiContiUidFin").val("");
       $("#HIDDEN_ElementoPianoDeiContiStringa").val("");
       
       return $.postJSON(
           "ajax/tipologiaTitoloAjax.do",
           {"id" : idTitoloEntrata},
           function (data) {
               var listaTipologiaTitolo = (data.listaTipologiaTitolo);
               var errori = data.errori;
               var options = $("#tipologiaTitoloFIN");
               var selectCategoria = $("#categoriaTipologiaTitoloFIN");
               var bottonePdC = $("#bottonePdCFIN");

               options.find('option').remove().end();

               if(errori.length > 0) {
                   options.attr("disabled", "disabled");
                   selectCategoria.attr("disabled", "disabled");
                   bottonePdC.attr("disabled", "disabled");
                   return;
               }
               if (options.attr("disabled") === "disabled") {
                   options.removeAttr("disabled");
               }
               if (selectCategoria.attr("disabled") !== "disabled") {
                   selectCategoria.attr("disabled", "disabled");
               }
               if (bottonePdC.attr("disabled") !== "disabled") {
                   bottonePdC.attr("disabled", "disabled");
               }

               options.append($("<option />").val("").text(""));
               $.each(
                   listaTipologiaTitolo,
                   function () {
                       options.append($("<option />").val(this.uid).text(this.codice + '-' + this.descrizione));
                   }
               );
           }
       );
   }
   
   /**
    * gestisciCategoria
    * richiama caricaCategoria()
    */
   function gestisciCategoria() {
       if (selectVuota($("#tipologiaTitoloFIN").val())) {
           $("#categoriaTipologiaTitoloFIN").val("");
           $("#categoriaTipologiaTitoloFIN").attr("disabled", true);
       } else {
          $("#categoriaTipologiaTitoloFIN").removeAttr("disabled");
           caricaCategoria();
       }
   }
   
   
   /**
    * Gestione della tipologia.
    */
   function gestisciTipologia() {
       if (selectVuota($("#titoloEntrataFIN").val())) {
           $("#tipologiaTitoloFIN").val ("");
           $("#tipologiaTitoloFIN").attr("disabled", true);
           $("#categoriaTipologiaTitoloFIN").val("");
           $("#categoriaTipologiaTitoloFIN").attr("disabled", true);
       } else {
          $("#tipologiaTitoloFIN").removeAttr("disabled");
           caricaTipologia();
       }
   }
    /**
    * Carica i dati nella select della Categoria da chiamata AJAX.
    *
    * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
    */
    function caricaCategoria() {
       var idTipologiaTitolo = $("#tipologiaTitoloFIN").val();

       // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
       $("#categoriaTipologiaTitoloFIN").val("");
       pulisciPDC();
       return $.postJSON(
           "ajax/categoriaTipologiaTitoloAjax.do",
           {"id" : idTipologiaTitolo},
           function (data) {
               var listaCategoriaTipologiaTitolo = data.listaCategoriaTipologiaTitolo;
               var errori = data.errori;
               var options = $("#categoriaTipologiaTitoloFIN");
               var bottonePdC = $("#bottonePdCFin");
               var opts = '';

               options.find('option').remove().end();

               if(errori.length > 0) {
                   options.attr("disabled", "disabled");
                   return;
               }

               if (options.attr("disabled") === "disabled") {
                   options.removeAttr("disabled");
               }
               if (bottonePdC.attr("disabled") !== "disabled") {
                   bottonePdC.attr("disabled", "disabled");
               }

               opts = listaCategoriaTipologiaTitolo.reduce(function(acc, el) {
                   return acc + '<option value="' + el.uid + '">' + el.codice + ' - ' + el.descrizione + '</option>';
               }, '<option></option>');
               options.html(opts);
           }
       );
   }
   
    /**
     * aggiorna la tabella di ricerca dopo avere effettuato l'aggiornamento dei piani dei conti
     */
    function ricaricaTabella(){
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
        return $.postJSON("risultatiRicercaRegistrazioneMovFinFINAjax.do", obj);
   }

    function clickOnAnnulla(uid) {
    }

    $(function() {
       visualizzaTabelleRicheste();
    });
    

}(jQuery, this);