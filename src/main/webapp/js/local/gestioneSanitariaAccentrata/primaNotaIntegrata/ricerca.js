/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var mapTipoElencoEntrataSpesa = {E: "entrata", S: "spesa"};
    var ambito = $('#hidden_ambito').val();
    var mapDataCapitoloTipo = {
        spesa: {
            tipo: 'UscitaGestione',
            suffix: '_cug'
        },
        entrata: {
            tipo: 'EntrataGestione',
            suffix: '_ceg'
        }
    };
    var capId = -1;

    /**
     * Impostazione del vecchio valore se ancora presente.
     *
     * @param select   (jQuery) la select da popolare
     * @param options  (Array)  l'array delle opzioni
     * @param oldValue (number) il precedente valore
     */
    function impostaValoreOld(select, options, oldValue) {
        var present = false;
        $.each(options, function() {
            // Uso il doppio uguale e non il triplo perche' potrei aver problemi di cast
            present = present || oldValue == this.uid;
        });
        if(present) {
            select.val(oldValue);
        }
    }

    /**
     * Filtra i tipi evento sulla base del tipo di elenco (Entrata/Spesa)
     * @param keepOldValues (boolean) se mantenere i vecchi valori
     * @return (Promise) la promise corrispondente al caricamento dei dati
     */
    function filtraTipoEvento(keepOldValues) {
        var tipoElenco = $("input[type='radio'][name='tipoElenco']:checked").val();
        var obj = {};
        var select = $("#tipoEvento");
        var oldValue = select.val();
        var tipoEventoDaRicerca = $("#uidTipoEventoDaRicerca");

        if(tipoElenco) {
            obj[mapTipoElencoEntrataSpesa[tipoElenco]] = true;
        }
        obj.tipoCausale = 'Integrata';

        // Scateno l'overlay
        select.overlay("show");
        return $.postJSON("ricercaTipoEventoByEntrataSpesa.do", obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
            	if (!!tipoEventoDaRicerca.val()) {
                    $("#tipoEvento").val(tipoEventoDaRicerca.val()).change();
                    tipoEventoDaRicerca.val("");
                }
                return;
            }
            // Non ho errori: popolo la select
            caricaSelect(select, data.listaTipoEvento, true, setDataAttributesTipoEvento, keepOldValues ? oldValue : undefined);
            if (!!tipoEventoDaRicerca.val()) {
                tipoEventoDaRicerca.val("");
            }
            // Lancio l'evento di change
            if(!keepOldValues) {
                select.change();
            }
        }).always(select.overlay.bind(select, "hide"));
    }

    /**
     * Caricamento degli eventi.
     */
    function caricaEvento() {
        var tipoEvento = $("#tipoEvento").val();
        var selectEvento = $("#evento");
        var oldValue = selectEvento.val();
        var eventoDaRicerca = $("#uidEventoDaRicerca");
        var tipoEventoDaRicerca = $("#uidTipoEventoDaRicerca");
        $('#causale').val('').attr('disabled', true);
        gestisciMovimentoFinanziario();

        if(!tipoEvento) {
            // Se non ho selezionato il tipo di evento, disabilito l'evento ed esco
            selectEvento.attr("disabled", true)
                .val("")
                .change();
            return $.Deferred().reject().promise();
        }

        // Attivo l'overlay
        selectEvento.overlay("show");
        return $.postJSON("ricercaEventoByTipoEvento.do", {'tipoEvento.uid': tipoEvento})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
            	if (!!eventoDaRicerca.val()) {
            		selectEvento.val(eventoDaRicerca.val());
                    if (tipoEventoDaRicerca.val()=="") {
                    	//lo azzero solamente se è azzerato anche l'altro 
                    	eventoDaRicerca.val("");
                    }
                }
                return;
            }
            // Non ho errori: carico la select
            caricaSelect(selectEvento, data.listaEvento, true);
            impostaValoreOld(selectEvento, data.listaEvento, !!eventoDaRicerca.val() ? eventoDaRicerca.val() :oldValue);
            if (!!eventoDaRicerca.val() && (tipoEventoDaRicerca.val()=="")) {
                	//lo azzero solamente se è azzerato anche l'altro 
                	eventoDaRicerca.val("");
            }
            // Tolgo il disabled
            selectEvento.removeAttr("disabled")
                .change();
        }).always(selectEvento.overlay.bind(selectEvento, "hide"));
    }

    /**
     * Abilita o disabilita la gestione dell'evento finanziario
     */
    function gestisciMovimentoFinanziario() {
        $("#annoMovimento, #numeroMovimento, #numeroSubmovimento")[$("#tipoEvento").val() ? "removeAttr" : "attr"]("disabled", true);
    }

    function caricaCausaleEP(caricaOld) {
        var uid = caricaOld ? $('#hidden_uidEvento').val() : $("#evento").val();
        var selectCausale = $('#causale');

        selectCausale.overlay('show');
        if(!uid) {
            selectCausale.val('').attr('disabled', true);
            selectCausale.overlay('hide');
            return;
        }

        return $.postJSON('ricercaCausaleEPByEvento_ricercaMinima.do', {'evento.uid': uid, 'ambito': ambito, 'tipoCausale': 'Integrata'})
        .then(function(data) {
            var str = '<option></option>';
            var uidOld = caricaOld ? $('#hidden_uidCausale').val() : '';
            if(!data || impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            $.each(data.listaCausaleEP, function(idx, val) {
                var s = '<option value="' + val.uid + '"';
                // Forzo il cast a numero
                if(+uidOld === +val.uid) {
                    s += ' selected';
                }
                s += '>' + val.codice + ' - ' + val.descrizione + '</option>';
                str += s;
            });
            selectCausale.html(str).removeAttr('disabled');
        }).always(selectCausale.overlay.bind(selectCausale, 'hide'));
    }

    /**
     * Aggiunge il data-attribute relativo al documento
     * @param tipoEvento (object) il tipo di evento
     * @return (string) la stringa da aggiungere nella option se necessario
     */
    function setDataAttributeDocumento(tipoEvento) {
        return tipoEvento.codice === 'DE' || tipoEvento.codice === 'DS' ? ' data-documento="true"' : '';
    }
    
    function setDataAttributeOrdinativo(tipoEvento) {
        return tipoEvento.codice === 'OI' || tipoEvento.codice === 'OP' ? ' data-ordinativo="true"' : '';
    }

    //SIAC-5799
    function setDataAttributeLiquidazione(tipoEvento) {
        return tipoEvento.codice === 'L' ? ' data-liquidazione="true"' : '';
    }
    
    /**
     * Aggiunge il data-attribute relativo all'entrata/spesa
     * @param tipoEvento (object) il tipo di evento
     * @return (string) la stringa da aggiungere nella option se necessario
     */
    function setDataAttributeEntrataSpesa(tipoEvento) {
        return tipoEvento.tipoSpesa
            ? ' data-tipo="spesa"'
            : tipoEvento.tipoEntrata
                ? ' data-tipo="entrata"'
                : '';
    }
    
    /**
     * Aggiunge i data attributes del tipo evento
     * @param tipoEvento (any) il tipo di evento
     * @return (string) la stringa da aggiungere nella option se necessario
     */
    function setDataAttributesTipoEvento(tipoEvento) {
        var tmp = [];
        if(!tipoEvento) {
            return '';
        }
        tmp.push(setDataAttributeDocumento(tipoEvento));
        tmp.push(setDataAttributeOrdinativo(tipoEvento));
        tmp.push(setDataAttributeLiquidazione(tipoEvento));
        tmp.push(setDataAttributeEntrataSpesa(tipoEvento));
        return tmp.join('');
    }

    /**
     * Caricamento della select tramite la lista fornita.
     *
     * @param select           (jQuery)   la select da popolare
     * @param list             (Array)    la lista tramite cui popolare la select
     * @param addEmptyOption   (Boolean)  se aggiungere una option vuota (optional - default - false)
     * @param operation        (Function) l'operazione da operare sull'elemento per comporre dati aggiuntivi sulla option (optional - default - $.noop)
     * @param valueToSelect    (number)   il valore da selezionare (optional - default - undefined)
     * @return (jQuery) la select su cui si e' agito
     */
    function caricaSelect(select, list, addEmptyOption, operation, valueToSelect) {
        var defaultOperation = operation && typeof operation === 'function' ? operation : $.noop;
        var initialValue = !!addEmptyOption
            ? '<option value=""></option>'
            : '';
        var str = list.reduce(function(acc, val) {
            var tmp = '<option value="' + val.uid + '"';
            tmp += defaultOperation(val);
            // Forzo il cast a numero per sicurezza
            if(+valueToSelect === +val.uid) {
                tmp += ' selected';
            }
            tmp += '>' + val.codice + ' - ' + val.descrizione + '</option>';
            return acc + tmp;
        }, initialValue);
        return select.html(str);
    }

    /**
     * Caricamento delle liste della classe.
     */
    function caricaListaClasse() {
        // classePianoDeiConti_modale
        var select = $('#classePianoDeiConti_modale');
        // Se sono gia' popolate, non ricarico
        if(selectHasOptions(select, 1)) {
            return;
        }
        select.overlay('show');
        $.postJSON('ricercaPrimaNotaIntegrataGSA_caricaListaClassi.do')
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Caricamento della select
            caricaSelect(select, data.listaClassi, true);
        }).always(select.overlay.bind(select, 'hide'));
    }

    /**
     * Controlla se la select ha piu' di un dato numero di opzioni.
     *
     * @param select    (jQuery) la select da controllare
     * @param threshold (Number) la soglia da superare
     * @returns true se la select ha pi' du threshold opzioni; false altrimenti
     */
    function selectHasOptions(select, threshold) {
        return select.find('option').length > threshold;
    }
    
    /**
     * Gestione del change del tipo evento
     */
    function changeTipoEvento() {
        preselezionaDatiDerivatiTipoElenco();
        return caricaEvento();
    }
    function preselezionaDatiDerivatiTipoElenco() {
        var selectedOption = $('option:selected', '#tipoEvento');
        var tipoDocumento = !!selectedOption.data('documento');
        var tipoEntrataSpesa = selectedOption.data('tipo');
        var capitoloContainer = $('#capitoloContainer');
        // SIAC-4644
        $('#codiceSoggetto, #annoProvvedimento, #numeroProvvedimento, #tipoAttoProvvedimento, #pulsanteDelesezionaStrutturaAmministrativoContabile')
            [selectedOption.val() === '' ? 'prop' : 'removeProp']('disabled', true);
        // SIAC-4644, modifica
        $('#importoDocumentoDa, #importoDocumentoA')[tipoDocumento ? 'removeProp' : 'prop']('disabled', true);
        $('#containerImportoDocumento')[tipoDocumento ? 'slideDown' : 'slideUp']();
        $('#pulsanteApriModaleProvvedimento, #pulsanteApriModaleSoggetto')[selectedOption.val() === '' ? 'addClass' : 'removeClass']('hide');
        
        // SIAC-5292
        Capitolo.destroy(capId);
        if(!tipoEntrataSpesa) {
            capitoloContainer.slideUp();
            capitoloContainer.find('input').not('[data-maintain]').val('');
            return;
        }
        capitoloContainer.slideDown();
        capId = Capitolo.inizializza(
            mapDataCapitoloTipo[tipoEntrataSpesa].tipo,
            "#annoCapitolo",
            "#numeroCapitolo",
            "#numeroArticolo",
            "#numeroUEB",
            "#datiRiferimentoCapitoloSpan",
            "#pulsanteApriCompilazioneGuidataCapitolo",
            mapDataCapitoloTipo[tipoEntrataSpesa].suffix);
        
        //SIAC-5799: NO, la SIAC r' solo per contabilita generale. lascio il codice per eventuali richieste in ambito GSA
        //impostaFiltroMovimentoGestione();

    }

    /**
     * Gestione del change per il codice soggetto
     * @returns (Promise) la promise relativa al caricamento del soggetto
     */
    function handleChangeCodiceSoggetto() {
        var campoCodiceSoggetto = $('#codiceSoggetto');
        var accordionSedeSecondaria = $();
        var accordionModalitaPagamento = $();
        var spanDescrizione = $('#descrizioneCompletaSoggetto');
        return Soggetto.caricaDettaglioSoggetto(campoCodiceSoggetto, accordionSedeSecondaria, accordionModalitaPagamento, spanDescrizione,undefined,undefined, undefined,true);
    }

    /*
     * JIRA-5553
     * */
    function handleChangeProvvedimento() {
    	var selectedOption = $('option:selected', '#tipoEvento');
        var notEmptyProvv = false;
        if (selectedOption.data('documento')){
            $('.inputProvvedimento').each(function() {
                notEmptyProvv |= $(this).val().length > 0;
            });
        }
    }

    /**
     * Gestione del reset del form
     */
    function handleReset() {
        var $form = $('form');
        $form.substituteHandler('reset', function() {
            $form.overlay('show');
            document.location = './ricercaPrimaNotaIntegrataGSA.do';
        });
    }

    function impostaFiltroMovimentoGestione(){
    	//SIAC-5799,solo per contabilita generale. lascio il codice erroneamente duiplicato per eventuali richieste in ambito GS
    	var selectedOption = $('option:selected', '#tipoEvento');
        var isSpesa = selectedOption.data('tipo') === 'spesa';
        var isOrdinativo = selectedOption.data('ordinativo');
        var isDocumento = selectedOption.data('documento');
        var isLiquidazione = selectedOption.data('liquidazione');
        
        //var containerAccertamento = $('#containerAccertamento').hide();
        //var containerImpegno = $('#containerImpegno').hide();
        var containerAccertamento = $('#containerAccertamento').slideUp();
        var containerImpegno = $('#containerImpegno').slideUp();

        Accertamento.destroy();
        Impegno.destroy();
        
        containerAccertamento.find('input').val('').removeAttr('readonly');
        containerImpegno.find('input').val('').removeAttr('readonly');
        
        if(!(isOrdinativo || isDocumento || isLiquidazione)){
        	return;
        }
        if(!isSpesa){
        	containerAccertamento.slideDown();
        	Accertamento.inizializza("#annoAccertamento", "#numeroAccertamento", "#numeroSubAccertamento", 'SPAN_accertamentoH4', undefined, undefined, '#pulsanteAperturaCompilazioneGuidataAccertamento');
        	return;
        }
        containerImpegno.slideDown();
        Impegno.inizializza("#annoImpegno", "#numeroImpegno", "#numeroSubImpegno", '#SPAN_impegnoH4', undefined, undefined, undefined, undefined, '#pulsanteAperturaCompilazioneGuidataImpegno', undefined);
    }

    
    function caricaStrutturaAmministrativoContabile(e){
    	var listaSAC = e.listaStrutturaAmministrativoContabile;
    	var idSuffix = 'Cap';
        ZTreeDocumento.imposta("treeStruttAmm" + idSuffix, ZTreeDocumento.SettingsBase, listaSAC, "HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Uid", idSuffix);
    }
    
    function uncheckZTree() {
    	var zTreeId = $(this).data('uncheckZtree');
    	var tree = $.fn.zTree.getZTreeObj(zTreeId);
        var nodo = tree && tree.getCheckedNodes(true)[0];
        if(nodo) {
            tree.checkNode(nodo, false, true, true);
        }
    }
    
    $(function () {
        var radioTipoElenco = $("input[type='radio'][name='tipoElenco']");
        var selectTipoEvento = $("#tipoEvento");
        var selectEvento = $("#evento");
        var $body = $(document.body);

        radioTipoElenco.change(filtraTipoEvento.bind(undefined, false));
        selectTipoEvento.change(changeTipoEvento);
        selectEvento.change(caricaCausaleEP.bind(undefined, false));

        Conto.inizializza(undefined, undefined, "#codiceConto", '#descrizioneConto', "#pulsanteCompilazioneGuidataConto");
        Provvedimento.inizializzazione("", "#annoProvvedimento", "#numeroProvvedimento", "#tipoAttoProvvedimento", "#treeStruttAmm",
                "#HIDDEN_StrutturaAmministrativoContabileUid", "#HIDDEN_statoProvvedimento", "#SPAN_InformazioniProvvedimento");
        
        $('.inputProvvedimento').change(handleChangeProvvedimento);

        $('#codiceSoggetto').change(handleChangeCodiceSoggetto);
        
        caricaListaClasse();
        filtraTipoEvento(true)
        .then(function() {
            if(selectTipoEvento.val()) {
                return changeTipoEvento();
            }
            return $.Deferred().resolve().promise();
        }).then(caricaCausaleEP.bind(undefined, true));
        
        //esegui questa callback (leggesi funzione richiamata caricaStrutturaAmministrativoContabile) una sola volta (one)
        $(document).one('sacCaricata', caricaStrutturaAmministrativoContabile);
        
        $('button[data-uncheck-ztree]').substituteHandler('click', uncheckZTree);

        
        handleReset();
        // SIAC-5336
        GSAClassifZtree.initClassificatoreGSAZtree();
        // SIAC-6425
        $('[data-overlay-body]').substituteHandler('click', $body.overlay.bind($body, 'show'));
    });

}(jQuery, this);