/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function(exports) {
    'use strict';

    /** Opzioni base per il dataTable */
    var dataTableBaseOptions = {
        bPaginate: true,
        bLengthChange: false,
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
        }
    };

    /**
     * Imposta il dataTable per gli Variazioni.
     *
     * @param startPosition {Number} la posizione di inizio
     */
    function visualizzaTabellaVariazioni (startPosition) {
        var tipoVariazioneCodifiche = $('#tipoVariazione').val() === 'true';
        var gestioneUEB = ($('#HIDDEN_gestioneUEB').val() === "true");
        var idAzioneReportVariazioni = $('#idAzioneReportVariazioni').val();
        var ajaxUrl = tipoVariazioneCodifiche ? "risultatiRicercaVariazioniCodificheAjax.do" : "risultatiRicercaVariazioniImportiAjax.do";
        var startPositionDaUtilizzare = startPosition || 0;
        var options;
        

        // Le opzioni base del dataTable
        var optionsBase = {
            bServerSide: true,
            sAjaxSource: ajaxUrl,
            sServerMethod: "POST",
            iDisplayStart: startPositionDaUtilizzare,
            fnDrawCallback: function () {
                $('#id_num_result').html(" " + this.fnSettings().fnRecordsTotal() + " ");
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('numero')},     
                {aTargets: [1], mData: defaultPerDataTable('descrizione')},
                //{aTargets: [2], mData: defaultPerDataTable('strutturaAmministrativoContabileDirezioneProponente.codice')},
//                {aTargets: [3], mData: function(source) {
//                    return source.tipoVariazione.codice + " - " + source.tipoVariazione.descrizione;
//                }},
//                {aTargets: [4], mData: defaultPerDataTable('provvedimento')},
//                {aTargets: [5 + !tipoVariazioneCodifiche], mData: defaultPerDataTable('stringaDataAperturaProposta')},
//                {aTargets: [6 + !tipoVariazioneCodifiche], mData: defaultPerDataTable('stringaDataChiusuraProposta')},
//                {aTargets: [7 + !tipoVariazioneCodifiche], mData: function(source) {
//                    return source.elementoStatoOperativoVariazione.descrizione;
//                }},
               
                
//                {aTargets: [8 + !tipoVariazioneCodifiche], fnCreatedCell: function (nTd) {
//                    $('.dropdown-toggle', nTd).dropdown();
//                }, mData: function (source) {
//                    var pulsante = "";
//                    var link = '<li><a href="consultaImportiVariazione.do?uidVariazioneDaConsultare='+source.uid+'">consulta importi</a></li>';
//                    if(tipoVariazioneCodifiche){
//                        link = '<li><a href="consultaCodificheVariazione.do?uidVariazioneDaConsultare='+source.uid+'">consulta codifiche</a></li>';
//                    } else if(gestioneUEB){
//                        link = '<li><a href="consultaImportiUEBVariazione.do?uidVariazioneDaConsultare='+source.uid+'">consulta importi UEB</a></li>';
//                    }
//                    
//                    if (idAzioneReportVariazioni !== '') {
//                    	link += '<li><a data-parametri-stampa="Numero Variazione:' + source.numero
//                    	+ ';Anno Variazione:' + source.anno
//                    	+ ';Anno Competenza:' + source.anno
//                        	+ '" href="#" class="stampaVariazione">stampa</a></li>';
//                    }
//                    
//                    pulsante = '<div class="btn-group">' +
//                    '<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni&nbsp;<span class="caret"></span></button>' +
//                    '<ul class="dropdown-menu pull-right">' + link + '</ul>' +
//                    '</div>';
//                    return pulsante;
//                }}
                
                
                
                
            ],
            fnCreatedRow: function (row, data, index) {
                $(row).find('.stampaVariazione').substituteHandler('click', stampaVariazione);
            }
        };
        
        //SIAC - 6884 
        var numColAzione =0;
        if(!tipoVariazioneCodifiche) {
        	 optionsBase.aoColumnDefs.push({aTargets: [2], mData: defaultPerDataTable('strutturaAmministrativoContabileDirezioneProponente.codice')});
        	 optionsBase.aoColumnDefs.push({aTargets: [3], mData: function(source) {
                 return source.tipoVariazione.codice + " - " + source.tipoVariazione.descrizione;
             }});
        	 optionsBase.aoColumnDefs.push({aTargets: [4], mData: defaultPerDataTable('provvedimento')});
        	 optionsBase.aoColumnDefs.push({aTargets: [5], mData: defaultPerDataTable('provvedimentoBilancio')});
        	 optionsBase.aoColumnDefs.push({aTargets: [6], mData: defaultPerDataTable('stringaDataAperturaProposta')});
        	 optionsBase.aoColumnDefs.push({aTargets: [7], mData: defaultPerDataTable('stringaDataChiusuraProposta')});
        	 optionsBase.aoColumnDefs.push({aTargets: [8], mData: function(source) {
                 return source.elementoStatoOperativoVariazione.descrizione;
             }});
        	 optionsBase.aoColumnDefs.push({aTargets: [9], mData: defaultPerDataTable('tipologiaUltimaOperazioneEffettuata')});
        	 optionsBase.aoColumnDefs.push({aTargets: [10], mData: defaultPerDataTable('statoUltimaOperazioneEffettuata')});
			 optionsBase.aoColumnDefs.push({aTargets: [11], mData: defaultPerDataTable('dataUltimaOperazioneEffettuata')});
        	 
        	 numColAzione = 12;
        }else{
        	optionsBase.aoColumnDefs.push({aTargets: [2], mData: function(source) {
                return source.tipoVariazione.codice + " - " + source.tipoVariazione.descrizione;
            }});
        	optionsBase.aoColumnDefs.push({aTargets: [3], mData: defaultPerDataTable('provvedimento')});
        	optionsBase.aoColumnDefs.push({aTargets: [4], mData: function(source) {
                return source.elementoStatoOperativoVariazione.descrizione;
            }});

        	 numColAzione = 5;
        }
        
        
        optionsBase.aoColumnDefs.push({aTargets: [ numColAzione ], mData: function (source) {
            var pulsante = "";
            var link = '<li><a href="consultaImportiVariazione.do?uidVariazioneDaConsultare='+source.uid+'">consulta importi</a></li>';
            if(tipoVariazioneCodifiche){
                link = '<li><a href="consultaCodificheVariazione.do?uidVariazioneDaConsultare='+source.uid+'">consulta codifiche</a></li>';
            } else if(gestioneUEB){
                link = '<li><a href="consultaImportiUEBVariazione.do?uidVariazioneDaConsultare='+source.uid+'">consulta importi UEB</a></li>';
            }
            
            if (idAzioneReportVariazioni !== '') {
            	link += '<li><a data-parametri-stampa="Numero Variazione:' + source.numero
            	+ ';Anno Variazione:' + source.anno
            	+ ';Anno Competenza:' + source.anno
                	+ '" href="#" class="stampaVariazione">stampa</a></li>';
            }
            
            pulsante = '<div class="btn-group">' +
            '<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni&nbsp;<span class="caret"></span></button>' +
            '<ul class="dropdown-menu pull-right">' + link + '</ul>' +
            '</div>';
            return pulsante;
        }});
        
        
        
        
        options = $.extend(true, {}, dataTableBaseOptions, optionsBase);

        $("#risultatiRicercaVariazioni").dataTable(options);
    }
    
 
    $(function() {
        var startPosition = $("#startPosition").val();
        // Carico la dataTable
        visualizzaTabellaVariazioni(startPosition);
    });
}) ();