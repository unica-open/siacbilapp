/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, global) {
    "use strict";

    var st = global.StampeIva || {};
    var alertErrori = $("#ERRORI");
    
    $(function() {
        $("#tipoRegistroIva").substituteHandler("change", function() {
            // Gestisco i flag
            st.gestioneFlagsTipoRegistro();
            // Carico il registro
            st.caricaRegistroIva();
        });

        $("#gruppoAttivitaIva").substituteHandler("change", function() {
            // Carico il registro
            st.caricaRegistroIva();
            // Carica Periodo
            st.caricaTipoChiusuraEPeriodoEIvaACredito("stampaRegistroIva_ottieniTipoChiusuraEPeriodoEIvaACreditoDaGruppo.do");
        });

        $("#pulsanteStampa").substituteHandler("click", function() {
            st.aperturaModaleConfermaStampa($("#registroIva"), "Registro Iva", true);
        });
        
        $("#pulsanteConfermaStampaIva").substituteHandler("click", function() {
            var selectedOption = $("#registroIva").find("option:selected");
            var url = "stampaRegistroIva_effettuaStampaMultipla.do";
            var obj;
            
            if (selectedOption.val() === '') {
                $("#modaleConfermaStampaIva").modal("hide");
                obj = $("form").serializeObject();
                obj.flagOnlyCheckRegistroIva = true;
                $.postJSON(url, obj)
                .then(function (data) {
                    if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                        return;
                    }
                    if (data.messaggi && data.messaggi.length) {
                        handleRetry(data.messaggi, url, obj, $.noop, mostraPaginaSuccesso);
                        return;
                    }
                    mostraPaginaSuccesso();
                });
                return false;
            }
            else {
            	return true;
            }
        });
    });
    
    
    function handleRetry(array, url, data, failCallback, endCallback) {
    	var str = 'Si sono verificate le seguenti anomalie:';
        var btnNo = {label: 'No, indietro', 'class': 'btn', callback: failCallback};
        var btnOk = {label: 'Si, prosegui', 'class': 'btn', callback: function() {
            var div = $('.bootbox');
            var btns = div.find('a[data-handler]').overlay('show');
            data.flagOnlyCheckRegistroIva = false;
            $.postJSON(url, data)
            .then(endCallback)
            .always(function() {
                btns.overlay('hide');
                div.modal('hide');
            });
            return false;
        }};
        if(array) {
            str += array.reduce(function(acc, el) {
                if(el.testo) {
                    return acc + '<li>' + el.testo + '</li>';
                }
                return acc + '<li>' + el.codice + ' - ' + el.descrizione + '</li>';
            }, '<ul>') + '</ul>';
        }
        str+= "Si desidera proseguire con la stampa di tutti i registri che non presentano anomalie?";

        bootbox.dialog(str, [btnNo, btnOk], {animate: true, classes: 'dialogWarn', header: 'Attenzione', backdrop: 'static'});
    }

    
    function mostraPaginaSuccesso() {
    	$("form").attr("action", "stampaRegistroIva_mostraPaginaSuccesso.do").submit();
    }
}(jQuery, this));