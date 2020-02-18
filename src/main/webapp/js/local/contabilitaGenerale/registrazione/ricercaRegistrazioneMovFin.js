/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var baseUrl = $('#baseUrl').val();
    //SIAC-5290
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
     * Caricamento della lista degli eventi
     */
    function caricaListaEventi(uid){
        var selectEvento = $("#evento");
        var tipoEventoDaRicerca = $("#uidTipoEventoDaRicerca");
        var eventoDaRicerca = $("#uidEventoDaRicerca");
        if(!uid) {
            selectEvento.empty().attr("disabled", "disabled");
            return;
        }

        selectEvento.overlay("show");
        $.postJSON(baseUrl + "_caricaEventi.do", {uidTipoEvento: uid})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)){
            	if (!!eventoDaRicerca.val()) {
                	selectEvento.val(eventoDaRicerca.val());
                    if (tipoEventoDaRicerca.val()=="") {
                    	//lo azzero solamente se è azzerato anche l'altro jira2803
                    	eventoDaRicerca.val("");
                    }
                }
                return;
            }
            caricaSelect(selectEvento, data.listaEventi, 0).removeAttr("disabled");
            
            impostaValoreOld(selectEvento, data.listaEventi, !!eventoDaRicerca.val() ? eventoDaRicerca.val() : "");
            if (!!eventoDaRicerca.val() && (tipoEventoDaRicerca.val()=="")) {
                	//lo azzero solamente se è azzerato anche l'altro 
                	eventoDaRicerca.val("");
            }
        }).always(function() {
            
            selectEvento.overlay("hide");
        });
    }

    /**
     * Caricamento della lista degli eventi
     */
    function caricaTipiEventoFiltrati(){
        var $this = $(this);
        var fam = $this.val();
        var selectTipoEvento = $("#tipoEvento");
        var tipoEventoDaRicerca = $("#uidTipoEventoDaRicerca");
        if(!$this.is(':checked') && !fam) {
            selectTipoEvento.empty().attr("disabled", "disabled");
            return;
        }

        selectTipoEvento.overlay("show");
        $.postJSON(baseUrl + "_caricaTipiEvento.do", {tipoElenco : fam})
        .then(function(data) {
            $("#evento").empty();
            caricaSelect(selectTipoEvento, data.listaTipiEvento, 0,true)
            	.removeAttr("disabled")
            	.trigger("change");
            
            impostaValoreOld(selectTipoEvento, data.listaTipiEvento, !!tipoEventoDaRicerca.val()? tipoEventoDaRicerca.val() : 0);
            if (!!tipoEventoDaRicerca.val()) {
                tipoEventoDaRicerca.val("");
            }
         }).always(function() {
            
            selectTipoEvento.overlay("hide");
        });
    }
    
    /**
     * Caricamento delle liste dei titoli.
     */
    function caricaListaTitolo() {
        var selectEntrata = $('#titoloEntrataFIN');
        var selectSpesa = $('#titoloSpesaFIN');
        var selects = selectEntrata.add(selectSpesa);
        // Se sono gia' popolate, non ricarico
        if(selectEntrata.find('option').length > 1 && selectSpesa.find('option').length > 1) {
            return;
        }
        selects.overlay('show');
        $.postJSON(baseUrl + '_caricaListaTitoli.do')
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Caricamento della select
            caricaSelect(selectEntrata, data.listaTitoloEntrata, 0);
            caricaSelect(selectSpesa, data.listaTitoloSpesa, 0);
        }).always(selects.overlay.bind(selects, 'hide'));
    }

    /**
     * Caricamento della select tramite la lista fornita.
     *
     * @param select     (jQuery) la select da popolare
     * @param list       (Array)  la lista tramite cui popolare la select
     * @param emptyValue (String) il valore da impostare per l'opzione vuota (optional - default - '')
     * @return (jQuery) la select su cui si e' agito
     */
    function caricaSelect(select, list, emptyValue, addHTML) {
        var str = list.reduce(function(acc, val) {
            return acc + '<option ' + addHTMLIfRequested(addHTML,val) + ' value="' + val.uid + '">' + val.codice + ' - ' + val.descrizione + '</option>';
        }, '<option value="' + (emptyValue || '') + '"></option>');
        return select.html(str);
    }

    /**
     * Caricamento delle liste del tipo evento.
     */
    function caricaListaTipoEvento(callback) {
        var select = $('#tipoEvento').overlay('show');
        var innerCallback = callback && typeof callback === 'function' ? callback : $.noop;
        $.postJSON(baseUrl + '_caricaListaTipiEvento.do')
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
        }).then(innerCallback)
        .always(select.overlay.bind(select, 'hide'));
    }
    
    //SIAC-5290
    function setDataAttributeEntrataSpesa(tipoEvento) {
        return tipoEvento.tipoSpesa
            ? ' data-tipo="spesa"'
            : tipoEvento.tipoEntrata
                ? ' data-tipo="entrata"'
                : '';
    }
    function setDataAttributeOrdinativo(tipoEvento) {
    	var eventoCode = tipoEvento && tipoEvento.codice;
        return eventoCode ==='OP' || eventoCode === 'OI'
            ? ' data-ordinativo="true"'
            : '';
    }
    function addHTMLIfRequested(addHTLM, tipoEvento){
    	if(!addHTLM){
    		return'';
    	}
    	return setDataAttributeEntrataSpesa(tipoEvento) + ' ' + setDataAttributeOrdinativo(tipoEvento);
    }
    
    
    function inizializzaCampi() {
        var selectedOption = $('option:selected', '#tipoEvento');
        var tipoEntrataSpesa = selectedOption.data('tipo');
        var capitoloContainer = $('#capitoloContainer');
        var containerAccertamento = $('#containerAccertamento');
        var containerImpegno = $('#containerImpegno');
        if(capitoloContainer.length === 0 && $('#soggettoContainer').length === 0){
        	//non ho il filtro di ricerca, esco
        	return;
        }
        //$('#asteriskDataRegistrazione')[selectedOption.data('documento') ? 'show' : 'hide']();
        // SIAC-4644
        $('#codiceSoggetto')[selectedOption.val() === '' ? 'prop' : 'removeProp']('disabled', true);
        // SIAC-4644, modifica
       $('#pulsanteApriModaleSoggetto')[selectedOption.val() === '' ? 'addClass' : 'removeClass']('hide');
       
       capitoloContainer.hide();
       
        // SIAC-5292
        Capitolo.destroy(capId);
        if(!tipoEntrataSpesa) {
            capitoloContainer.slideUp();
            capitoloContainer.find('input').not('[data-maintain]').val('');
            containerAccertamento.slideUp();
            containerAccertamento.find('input').not('[data-maintain]').val('');
            containerImpegno.slideUp();
            containerImpegno.find('input').not('[data-maintain]').val('');
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
        impostaFiltroMovimentoGestione();
    }
    
    function callbackSelezioneTipoEvento(){
    	handleSelectedOption($('option:selected', this));
    	
    	caricaListaEventi(parseInt($(this).val(), 10));
    	inizializzaCampi();
    }

    function handleSelectedOption(opt) {
    	var txt = opt.text();
		disableProvvedimento(/^RS -/.test(txt) || /^RT -/.test(txt));
    }

    function disableProvvedimento(disabled) {
    	var p = $('#provvedimentoContainer');
    	var inp = $('input, select', p);
    	
    	p[disabled ? 'slideUp' : 'slideDown']();
    	
    	return disabled ? inp.attr('disabled', 'disabled') :
    		inp.removeAttr('disabled');
    }
    
    function impostaFiltroMovimentoGestione(){
    	var selectedOption = $('option:selected', '#tipoEvento');
        var isSpesa = selectedOption.data('tipo') === 'spesa';
        var isOrdinativo = selectedOption.data('ordinativo');
        $('#containerAccertamento').hide();
        $('#containerImpegno').hide();
    	Accertamento.destroy();
        Impegno.destroy();
        
        if(!isOrdinativo){
        	return;
        }
        if(!isSpesa){
        	$('#containerAccertamento').slideDown();
        	Accertamento.inizializza("#annoAccertamento", "#numeroAccertamento", "#numeroSubAccertamento", 'SPAN_accertamentoH4', undefined, undefined, '#pulsanteAperturaCompilazioneGuidataAccertamento');
        	return;
        }
        $('#containerImpegno').slideDown();
        Impegno.inizializza("#annoImpegno", "#numeroImpegno", "#numeroSubimpegno", '#SPAN_impegnoH4', undefined, undefined, undefined, undefined, '#pulsanteAperturaCompilazioneGuidataImpegno', undefined);
    }
 
    function uncheckZTree() {
    	var zTreeId = $(this).data('uncheckZtree');
    	var tree = $.fn.zTree.getZTreeObj(zTreeId);
        var nodo = tree && tree.getCheckedNodes(true)[0];
        if(nodo) {
            tree.checkNode(nodo, false, true, true);
        }
    }
    
    function caricaStrutturaAmministrativoContabile(e){
    	var listaSAC = e.listaStrutturaAmministrativoContabile;
    	var idSuffix = 'Cap';
        ZTreeDocumento.imposta("treeStruttAmm" + idSuffix, ZTreeDocumento.SettingsBase, listaSAC, "HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Uid", idSuffix);
    }
    
    
    $(function() {
        var tipoElenco = $("input[name='tipoElenco']").on("change", caricaTipiEventoFiltrati);
        ContoFIN.inizializza("#conto", "#pulsanteCompilazioneGuidataContoFIN");
        
        Provvedimento.inizializzazione("", "#annoProvvedimento", "#numeroProvvedimento", "#tipoAttoProvvedimento", "#treeStruttAmm","#HIDDEN_StrutturaAmministrativoContabileUid", "#HIDDEN_statoProvvedimento", "#SPAN_InformazioniProvvedimento");

        
        $("#tipoEvento").on("change", callbackSelezioneTipoEvento).change();
//        $("#numero, #anno").on("change", cambiaObbligatorietaTipoEvento);
        // Caricamento delle liste via AJAX
        caricaListaTitolo();
        caricaListaTipoEvento(function() {
            tipoElenco.filter(':checked').change();
        });
       
      //esegui questa callback (leggesi funzione richiamata caricaStrutturaAmministrativoContabile) una sola volta (one)
        $(document).one('sacCaricata', caricaStrutturaAmministrativoContabile);
        
        $('button[data-uncheck-ztree]').substituteHandler('click', uncheckZTree);
        
        //handleReset();
        
        
    });

}(jQuery, this);