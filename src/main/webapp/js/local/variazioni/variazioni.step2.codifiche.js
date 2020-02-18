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
    	var p = new Provvedimento();
    	p.inizializza();
    	
        // Cancella la tabella dei provvedimenti quando si effettua un reset del form
        $("form").on("reset", function() {
        	$("input[type='radio'][name='definisci.variazioneAOrganoAmministativo'][value='false']").prop('checked', true);
            $("input[type='radio'][name='definisci.variazioneAOrganoLegislativo'][value='false']").prop('checked', true);
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