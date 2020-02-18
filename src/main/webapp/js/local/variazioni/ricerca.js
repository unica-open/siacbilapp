/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Funzioni di ricerca Variazioni
;(function(w, $) {
	'use strict';
	
	/**
     * Gestione per il cambio di tipologia.
     */
    function onChangeTipologia() {
        if ($("#numeroVariazione").attr("disabled") !== undefined) {
            $("#numeroVariazione").removeAttr("disabled");
            $("#applicazioneVariazione").removeAttr("disabled");
            $("#statoVariazione").removeAttr("disabled");
            $("#descrizioneVariazione").removeAttr("disabled");
            $("#tipoVariazione").removeAttr("disabled");
            $("#dataAperturaProposta").removeAttr("disabled");
            $("#dataChiusuraProposta").removeAttr("disabled");
            $("#strutturaAmministrativoContabileDirezioneProponente").removeAttr("disabled");
            $('#div_provvedimento a').removeAttr("disabled").attr('href', '#collapseProvvedimento');
            $('#div_provvedimentoAggiuntivo a').removeAttr("disabled").attr('href', '#collapseProvvedimentoAggiuntivo');
        }
        if($("[name='tipologiaSceltaVariazione']:checked").val() === 'importi'){
        	$('#div_provvedimentoVariazioneDiBilancio').slideDown();
        }else{
        	$('#div_provvedimentoVariazioneDiBilancio').slideUp();
        }

        $.postJSON("caricaTipoVariazione.do", {"tipologiaSceltaVariazione" : $("[name='tipologiaSceltaVariazione']:checked").val()})
        .then(function(data) {
            var lista = data.listaTipoVariazione;
            var selectDaPopolare = $('#tipoVariazione');
            var str;
            selectDaPopolare.empty();

            if ($("[name='tipologiaSceltaVariazione']:checked").val() === 'importi') {
                str = "<option></option>";
            }

            str = lista.reduce(function(acc, el) {
            	return acc + '<option value="' + el._name + '">' + el.codice + ' - ' + el.descrizione + '</option>';
            }, '<option></option>');
            selectDaPopolare.append(str);
            
            // Setto la option selected
            selectDaPopolare.find('option[value=' + $("#tipoVariazioneHidden").val() + ']').attr("selected", "selected");
            preSelezionaSeUnicaOpzione(selectDaPopolare);
        });
    }

    /**
     * Gestione del cambio del tipo di variazione.
     */
    function onChangeTipoVariazione() {
        $('#tipoVariazioneHidden').val($('#tipoVariazione option:selected').val());
    }
    
    function onChangeTipoApplicazione(e){
    	$("#tipoVariazione").attr("disabled", "true");
    	if($(e.target).val()==="Previsione"){
    		$("#tipoVariazione").find('option[value="VARIAZIONE_PER_ASSESTAMENTO"]').removeProp('selected').hide();
    	}else{
    		$("#tipoVariazione").find('option[value="VARIAZIONE_PER_ASSESTAMENTO"]').show();
    	}
    	$("#tipoVariazione").removeAttr("disabled");
    }
    
    $(function() {

        $("form").on("reset", function() {
            $("#numeroVariazione").attr("disabled", "true");
            $("#applicazioneVariazione").attr("disabled", "true");
            $("#statoVariazione").attr("disabled", "true");
            $("#descrizioneVariazione").attr("disabled", "true");
            $("#tipoVariazione").attr("disabled", "true");
            $("#dataAperturaProposta").attr("disabled", "true");
            $("#dataChiusuraProposta").attr("disabled", "true");
            $("#strutturaAmministrativoContabileDirezioneProponente").attr("disabled", "true");
            $('#div_provvedimento a').removeAttr('href');
        });

        $('#div_provvedimento a').removeAttr('href');
        $('#div_provvedimentoAggiuntivo a').removeAttr('href');
        $('#div_provvedimentoVariazioneDiBilancio').slideUp();
        if ($("input[name='tipologiaSceltaVariazione']").is(':checked')) {
            onChangeTipologia();
        }

        $("input[name='tipologiaSceltaVariazione']").on("change", onChangeTipologia);
        $('#tipoApplicazione').on("change", onChangeTipoApplicazione).trigger('change');
        $("#applicazioneVariazione").on("change", onChangeTipoApplicazione);
        
        submitFormOnEnterPress('form');
        
        var provvedimento = new Provvedimento(undefined, 'Nessun provvedimento selezionato', 'provvedimento:');
    	provvedimento.inizializza();
    });
    
})(this, jQuery);
