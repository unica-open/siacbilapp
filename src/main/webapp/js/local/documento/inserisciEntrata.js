/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global, $) {
    'use strict';
    global.preSubmit = Documento.preSubmit;

    $(function() {
        $("#importoDocumento, #arrotondamentoDocumento").on("change", Documento.valorizzaNetto);
        $("#terminePagamentoDocumento, #dataRepertorioDocumento").on("change", Documento.gestioneDataScadenza)
            .change();
        $("#annoDocumento").on("change blur", Documento.impostaAnnoPerDataEmissione);

        $("#formInserimentoDocumentoSpesa").on("reset", Documento.resetForm);
        
      //SIAC 6677
        $("#codAvvisoPagoPA").change(function(){
            if($(this).val().length > 0 && $(this).val().length < 18 ){
             	var diff = 18 - $(this).val().length;
                 var codice = '';
                 for(var i=0; i<diff; i++){
                 	codice = codice + '0';
                 }
                 var newcodice = codice + $(this).val();
             	$(this).val(newcodice)
             }
           });
    });
}(this, jQuery);
