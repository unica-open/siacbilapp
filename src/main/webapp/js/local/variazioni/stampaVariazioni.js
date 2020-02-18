/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
    
    function stampaVariazione(e) {
    	e.preventDefault();
    	var idAzioneReportVariazioni = $('#idAzioneReportVariazioni').val();
    	
    	if(idAzioneReportVariazioni === '') {
    		return;
    	}
    	
        var modal = $("#modaleStampa");
        var $element = $(e.target);

        $("#frame-div").append('<h3 id="attendere">Attendere, prego...</h3>');
        
        modal.modal("show");

        var modalBody = modal.find(".modal-body");
        modalBody.height($(window).height() * 0.8);
        
        $("#frame-div").append('<iframe src="/siaccruapp/home.selezionaAzione.do?azioneSelezionata=' + idAzioneReportVariazioni 
        		+ '&parametriAzioneSelezionata=' + $element.data('parametri-stampa') + '"></iframe>');
        
        $("#frame-div iframe")
        	.width(modal.width() * 0.95)
        	.height(modalBody.height() * 0.95)
        	.hide();
        
        $('iframe').on('load', function(){ 
            $('#attendere').remove();
            $(this).show();
        });
    }

$(function() {
    $("#modaleStampa div.modal-body").css('max-height', $(window).height() * 0.85);
    
    $('#modaleStampa').on('hidden', function () {
    	$("#frame-div").html('');
    })        
});
