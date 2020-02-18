/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";
    
    var baseOptsDatiFinanziari = {        		
        	bServerSide:true,
        	sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaDatiFinanziariPrimaNotaIntegrataGSAAjax.do",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,                
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bDestroy: true,
            oLanguage: {
              	sInfo: "_START_ - _END_ di _MAX_ risultati",
              	sInfoEmpty: "0 risultati",
               	sProcessing: "Attendere prego...",
               	sZeroRecords: "Non sono presenti movimenti collegati",
               	oPaginate: {
               		sFirst: "inizio",
               		sLast: "fine",
               		sNext: "succ.",
               		sPrevious: "prec.",
               		sEmptyTable: "Nessuna richiesta disponibile"
               	}
            },
            aoColumnDefs : [],
            fnDrawCallback: function () {
            	$('#divDatiFinanziari').overlay('hide');
            }
        };


    /**
     * Impostazione della tabella.
     */
    function attivaDataTable() {
        var tableId = "#tabellaScritture";
        var opts = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti scritture collegate",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessuna richiesta disponibile"
                }
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div").hide();
            }
        };
        $(tableId).dataTable(opts);
    }
    
    /**
     * Impostazione della tabella.
     */
    function attivaDataTablePrimeNoteCollegate() {
        var tableId = "#tabellaPrimeNoteCollegate";
        var opts = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti prime note collegate",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessuna richiesta disponibile"
                }
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div").hide();
            }
        };
        $(tableId).dataTable(opts);
    }
    
   
    function attivaDataTableSubdocSpesa() {
        
        var optsDocumentoSpesa = {
        		aoColumnDefs : [
        		                {
        		                    aTargets : [ 0 ],
        		                    mData : function (source) {
        		                       return source.numero;
        		                    }
        		                },
        		                {
        		                    aTargets : [ 1 ],
        		                    mData : function(source) {
        		                        return source.importo;
        		                    }
        		                },
        		                {
        		                    aTargets : [ 2 ],
        		                    mData : function(source) {
        		                        return source.movimentoGestione;
        		                    }
        		                },
        		                {
        		                    aTargets : [ 3 ],
        		                    mData : function(source) {
        		                        return source.pianoDeiConti;
        		                    }
        		                },
        		                {
        		                    aTargets : [ 4 ],
        		                    mData : function(source) {
        		                        return source.liquidazione;
        		                    }
        		                },
        		                {
        		                    aTargets : [ 5 ],
        		                    mData : function(source) {
        		                        return source.ordinativo;
        		                    }
        		                }
        		                ]
        		
        		
        };
        
        var opzioniTabella = $.extend(true, {}, baseOptsDatiFinanziari, optsDocumentoSpesa);
        $('#divDatiFinanziari').find('table').dataTable(opzioniTabella);
    }
    
    
    function attivaDataTableSubdocEntrata() {
        var optsDocumentoEntrata = {
        		aoColumnDefs : [
        		                {
        		                    aTargets : [ 0 ],
        		                    mData : function (source) {
        		                       return source.numero;
        		                    }
        		                },
        		                {
        		                    aTargets : [ 1 ],
        		                    mData : function(source) {
        		                        return source.importo;
        		                    }
        		                },
        		                {
        		                    aTargets : [ 2 ],
        		                    mData : function(source) {
        		                        return source.movimentoGestione;
        		                    }
        		                },
        		                {
        		                    aTargets : [ 3 ],
        		                    mData : function(source) {
        		                        return source.pianoDeiConti;
        		                    }
        		                },
        		                {
        		                    aTargets : [ 4 ],
        		                    mData : function(source) {
        		                        return source.ordinativo;
        		                    }
        		                }
        		                ]
        		
        		
        };
        var opzioniTabella = $.extend(true, {}, baseOptsDatiFinanziari, optsDocumentoEntrata);
        $('#divDatiFinanziari').find('table').dataTable(opzioniTabella);
    }

    
    /**
     * Validazione della prima nota.
     */
    function validazionePrimaNota(e){
        var uidPrimaNota = $('#uidPrimaNotaIntegrata').val();
        var numeroPrimaNota = $('#numeroPrimaNotaIntegrata').val();
        var descrizionePrimaNota = $('#descrizionePrimaNotaIntegrata').val();
        
        var href = 'consultaPrimaNotaIntegrataGSA_valida.do?primaNota.uid=' + uidPrimaNota ;
        $("#modaleValidazioneElementoSelezionato").html(numeroPrimaNota + " - " + descrizionePrimaNota);
        $("#modaleValidazionePulsanteSalvataggio").substituteHandler("click", function() {
            // SIAC-5853
            var dataRegistrazione = $('#dataRegistrazionePrimaNota').val() || '';
            $("#SPINNER_modaleValidazionePulsanteSalvataggio").addClass("activated");
            document.location = href
                + '&primaNota.classificatoreGSA.uid=' + $('#HIDDEN_classificatoreGSAUid').val()
                + '&primaNota.dataRegistrazioneLibroGiornale=' + encodeURIComponent(dataRegistrazione);
        });
        $("#modaleValidazione").modal("show");
    }
    
    /**
     * Ottiene i dati finanziari
     */
    function ottieniDatiFinanziari(e) {
        var header = $('#headingAccordionDatiFinanziari');
        var div = $('#divDatiFinanziari');
        var obj;
        
        if(header.data('loaded')) {
            return;
        }
        e.preventDefault();
        e.stopPropagation();
        obj = {'primaNota.uid': $('#uidPrimaNotaIntegrata').val(), 'tipoCollegamento': $('#tipoCollegamentoDatiFinanziari').val()};
        header.overlay('show');
        
        return $.post('consultaPrimaNotaIntegrataGSA_ottieniDatiFinanziari.do', obj)
        .then(function(data) {
        	var tipoCollegamento = $('#tipoCollegamentoDatiFinanziari').val();
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
                return;
            }
            div.html(data);
            header.data('loaded', true);
            div.collapse('show');
            if(tipoCollegamento==='SUBDOCUMENTO_SPESA'){
            	div.overlay('show');
            	attivaDataTableSubdocSpesa();
            }else if(tipoCollegamento==='SUBDOCUMENTO_ENTRATA'){
            	div.overlay('show');
            	attivaDataTableSubdocEntrata();
            }
        }).always(header.overlay.bind(header, 'hide'));
    }


    $(function() {
        // Inizializzazione del datatable
        attivaDataTable();
        attivaDataTablePrimeNoteCollegate();
        $('#validazionePrimaNotaIntegrata').substituteHandler('click',validazionePrimaNota);
        $('#headingAccordionDatiFinanziari').on('click', ottieniDatiFinanziari);
        
    	GSAClassifZtree.initClassificatoreGSAZtree();
    });
}(jQuery, this);