/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    var $document = $(document);
    var suffix='Progetto';
    $(init);
    
    
    
    function caricaStrutture(){
        // Pulsante per il modale della Struttura Amministrativo Contabile
        var spinner = $('#SPINNER_StrutturaAmministrativoContabile' + suffix);

        // Non permettere di accedere al modale finche' il caricamento non e' avvenuto
        $('#bottoneSAC' + suffix).removeAttr('href');
        // Attiva lo spinner
        spinner.addClass('activated');

        $.postJSON('ajax/strutturaAmministrativoContabileAjax.do',{})
        .then(caricaStruttureCallback)
        .always(spinner.removeClass.bind(spinner, 'activated'));

    }

    function caricaStruttureCallback(data) {
        var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);
        var ztree = new Ztree('StrutturaAmministrativoContabile',suffix, "Nessun servizio selezionato");
        ztree.inizializza(listaStrutturaAmministrativoContabile);
        //ZTree.imposta('treeStruttAmm' + this.suffix, ZTree.SettingsBase, listaStrutturaAmministrativoContabile);
        // Ripristina l'apertura del modale
        $('#accordionPadreStrutturaAmministrativaProgetto').attr('href', '#strutturaAmministrativoContabile' + suffix);
    }
    
    function gestisciStruttureCaricate(e) {
    	var listaStrutturaAmministrativoContabile = (e && e.lista) || [];
        var ztree = new Ztree("StrutturaAmministrativoContabile", suffix);
        ztree.inizializza(listaStrutturaAmministrativoContabile);
        $('#accordionPadreStrutturaAmministrativaProgetto').attr('href', '#strutturaAmministrativoContabile' + suffix);
    }
    
    function annulla(){
      	 var $form = $('form');
      	 $form.attr('action', 'inserimentoProgettoPulisci.do');
      	 $form.submit();
      }

    
    function init() {
    	var provvedimento = new Provvedimento(undefined, 'provvedimento', 'provvedimento: ');
    	provvedimento.inizializza();
    	
    	$('#pulsanteAnnulla').substituteHandler('click', annulla);
    	
        $document.on('selectedProvvedimento', handleSelectedProvvedimento);
        $(document).on("struttureAmministrativeCaricate", gestisciStruttureCaricate);
        $('form').on('reset', $('#valoreComplessivoProgetto').val.bind($('#valoreComplessivoProgetto'), '0,00'));
        
    }
    function handleSelectedProvvedimento(e) {
        var provvedimento = e.provvedimento || {};
        var $hiddenAbilitazione = $('#HIDDEN_abilitaModificaDescrizione');
        // SIAC-6005
        if(provvedimento.codiceTipo === "SPR") {
            provvedimento.oggetto = null;
            $("#descrizioneProgetto").removeAttr('readonly');
            $hiddenAbilitazione.val(true);
        } else {
            $("#descrizioneProgetto").attr('readonly', true);
            $hiddenAbilitazione.val(false);
        }
        
        $("#HIDDEN_infoProvv").val($("#SPAN_InformazioniProvvedimento").text());
        
        // Copio l'oggetto nella descrizione
        //SIAC-6751
        $('#descrizioneProgetto').val(provvedimento.oggetto || 'SENZA DESCRIZIONE');
        
    }
}(jQuery);