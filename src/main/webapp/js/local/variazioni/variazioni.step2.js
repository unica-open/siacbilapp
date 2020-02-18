/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* Per lo step 2 delle variazioni */
var Variazioni = (function (exports) {

    exports.gestioneSubmit = function(e, forceSubmit) {
        var uidProvvedimento = $("#HIDDEN_uidProvvedimento").val() || $("#HIDDEN_uidProvvedimentoInjettato").val();
        if(!!forceSubmit || (uidProvvedimento && uidProvvedimento !== "0")) {
        	return true;
        }
        $("form").removeClass('form-submitted');
        $('body').overlay('hide');
        $("#modaleConfermaNoProvvedimento").modal("show");
        return false;
    };
    
    exports.onChangeTipoApplicazione = function(e){
    	$("#tipoVariazione").attr("disabled", "true");
    	if($(e.target).val()==="Previsione"){
    		$("#tipoVariazione").find('option[value="VARIAZIONE_PER_ASSESTAMENTO"]').removeProp('selected').hide();
    	}else{
    		$("#tipoVariazione").find('option[value="VARIAZIONE_PER_ASSESTAMENTO"]').show();
    	}
    	$("#tipoVariazione").removeAttr("disabled");
    };
    

    return exports;
} (Variazioni || {}));

/* Document.ready */

$(
    function () {

    	var provvedimento = new Provvedimento(undefined, 'Nessun provvedimento variazione di PEG selezionato', 'provvedimento variazione di PEG: ', 'provvedimento variazione di PEG');
    	provvedimento.inizializza();
    	
    	var provvedimentoVariazioneDiBilancio = new Provvedimento('Aggiuntivo', 'Nessun provvedimento variazione di bilancio selezionato', 'provvedimento variazione di bilancio: ', 'provvedimento variazione di bilancio');
    	provvedimentoVariazioneDiBilancio.inizializza();

        // Cancella la tabella dei provvedimenti quando si effettua un reset del form
        $("form").on("reset", function() {
            cleanDataTables();
            $("input[type='radio'][name='definisci.variazioneAOrganoAmministativo'][value='false']").prop('checked', true);
            $("input[type='radio'][name='definisci.variazioneAOrganoLegislativo'][value='false']").prop('checked', true);
            $("#tabellaProvvedimento").slideUp();
            if($("#collapseProvvedimento").hasClass("in")){
                $("#collapseProvvedimento").collapse("hide");
            }
        }).on("submit", Variazioni.gestioneSubmit);

        // CR SIAC-2204
        $("#pulsanteSiModaleConfermaNoProvvedimento").click(function() {
            $("form").removeClass('form-submitted')
                     .trigger("submit", true);
        });
        //CR SIAC-3666
        $('#tipoApplicazione').on("change", Variazioni.onChangeTipoApplicazione).trigger('change');
    }
);