/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {

    var alertErrori = $("#ERRORI");
    var divRisultati = $("#risultatiRicercaPianoDeiConti");


    /**
     * Ricerca il piano dei conti.
     */
    function ricercaPianoDeiConti() {
        var obj = $("#fieldsetRicerca").serializeObject();
        var spinner = $("#SPINNER_pulsanteRicercaPianoDeiConti").addClass("activated");

        divRisultati.slideUp();
        // Effettuo la ricerca
        $.postJSON("ricercaPianoDeiContiFIN_effettuaRicerca.do", obj, function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Non ho errori. Mostro la tabella e calcolo i risultati
            popolaTabella(data.conto, data.figliPresenti, data.isGestioneConsentita, data.gerarchiaConti);
            divRisultati.slideDown();
        }).always(function() {
            spinner.removeClass("activated");
        });

    }

    /**
     * Impostazione della tabella con i conti figli ottenuti dalla ricerca.
     */
    function impostaTabellaRisultatiFigli() {
        var tableId = "#tabellaPianoDeiConti";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaPianoDeiContiAjax.do",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti figli per il conto trovato",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun conto disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Conti figlio trovati") : ("1 Conto figlio trovato");
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.livello;
                }},
                {
                  aTargets : [2],
                  mData : function(source) {
                      return "<a rel='popover' href='#'>" + source.codice + "</a>";
                  },
                  "fnCreatedCell" : function (nTd, sData, oData) {
                      // Settings del popover
                      var settings = {
                          "content" : oData.descrizione,
                          "title" : "Descrizione",
                          "trigger" : "hover"
                      };
                      // Importante : attivare il popover sull'elemento anchor
                      $("a", nTd).off("click")
                          .on("click", function(event) {event.preventDefault();})
                          .popover(settings);

                  }
                },
                {aTargets: [3], mData: function(source) {
                    return source.attivo  ? "Attivo" : "Non attivo";
                }},
                {aTargets: [4], mData: function(source) {
                    return formatSN(source.contoDiLegge);
                }},
                {aTargets: [5], mData: function(source) {
                    return formatSN(source.contoFoglia);
                }},
                {aTargets: [6], mData: function(source) {
                    return source.elementoPianoDeiConti ? source.elementoPianoDeiConti.codice : "";
                }},
                {   aTargets: [7],
                    mData: function(source) {
                        return "<div class='btn-group'>"
                                +     "<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni<span class='caret'></span></button>"
                                +     "<ul class='dropdown-menu pull-right'>"
                                +         "<li><a href='ricercaPianoDeiContiFIN_consulta.do?uidConto="+source.uid+"'>consulta</a></li>"
                                +     "</ul>"
                                + "</div>";
                    },
                    fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                    }
                }
            ]
        };
        $(tableId).dataTable(opts);
    }


    function popolaTabella(contoPadre, figliPresenti, isGestioneConsentita, gerarchiaConti){
        popolaAlberoPadri(gerarchiaConti);
        popolaIntestazioneTabella(contoPadre, figliPresenti, isGestioneConsentita);
        impostaTabellaRisultatiFigli();

    }

    function popolaAlberoPadri(gerarchiaConti){
        var albero = "";
        var i;
        var ulFiglio;

        //se ho cercato un conto di livello 0
        if(gerarchiaConti.length === 1){
            albero += "<ul class='ztree'>"+
            "<li class='level0' tabindex='0' hidefocus='true' treenode=''>" +
                                "<span  title='' treenode_ico='' class='button ico_docu'></span>" +
                                "<span >" + gerarchiaConti[0] + "</span>" +
                            "</li>" +
                        "</ul>";
            $("#alberoDeiConti").html(albero);
            return;
        }

        //radice
        albero += "<ul class='ztree'>"+
            "<li class='level0' tabindex='0' hidefocus='true' treenode=''>" +
                "<span  title='' treenode_ico='' class='button ico_open'></span>" +
                "<span >" + gerarchiaConti[gerarchiaConti.length - 1] + "</span>";
        //livelli intermedi
        for(i = gerarchiaConti.length - 2; i > 0; i-- ){
            ulFiglio = "<ul class='level0 line'>"+
                            "<li class='level1' tabindex='0'>" +
                            "<span  title='' treenode_ico='' class='button ico_open'></span>" +
                            "<span >" + gerarchiaConti[i] + "</span>";
            albero += ulFiglio;
        }
        //ultimo conto (quello cercato)
        albero += "<ul class='level0 line'>"+
                        "<li class='level1' tabindex='0'>" +
                            "<span  title='' treenode_ico='' class='button ico_docu'></span>" +
                            "<span >" + gerarchiaConti[0] + "</span>" +
                        "</li>" +
                    "</ul>";
        //chiudo tutti i tag li e ul
        for(i = 1; i < gerarchiaConti.length; i++ ){
            albero += "</li></ul>";
        }
        $("#alberoDeiConti").html(albero);

    }

    function popolaIntestazioneTabella(conto, figliPresenti, isGestioneConsentita){
        var riga = $("#rigaDatiPadre");
        var contoFin = conto.elementoPianoDeiConti != null ? conto.elementoPianoDeiConti.codice : "";
        var stato = conto.attivo ? "Attivo" : "Non Attivo";
        var nuovaIntestazione = "<td>"+ conto.pianoDeiConti.classePiano.descrizione +"</td>"+
                                "<td>"+ conto.livello +"</td>" +
                                "<td><a id= 'popover_desc' rel='popover' href='#'>" + conto.codice + "</a></td>"+
                                "<td>"+ stato +"</td>"+
                                "<td>"+ formatSN(conto.contoDiLegge) +"</td>" +
                                "<td>"+ formatSN(conto.contoFoglia) +"</td>" +
                                "<td>"+ contoFin +"</td>" +
                                "<td class ='tab_Right'>" +
                                    "<div class='btn-group'>" +
                                      "<button class='btn dropdown-toggle' data-toggle='dropdown'>Azioni<span class='caret'></span></button>" +
                                      "<ul class='dropdown-menu pull-right'>" +
                                            "<li><a href='ricercaPianoDeiContiFIN_consulta.do?uidConto="+conto.uid+"'>consulta</a></li>";
        if(isGestioneConsentita && conto.livello !== 0){
                        nuovaIntestazione +="<li><a href='ricercaPianoDeiContiFIN_aggiorna.do?uidConto="+conto.uid+"'>aggiorna</a></li>";
        }

        if(isGestioneConsentita && !conto.contoFoglia && conto.attivo && conto.livello <= conto.pianoDeiConti.classePiano.livelloDiLegge){
                        nuovaIntestazione +="<li><a href='ricercaPianoDeiContiFIN_inserisciFiglio.do?uidConto="+conto.uid+"'>inserisci figlio</a></li>";
                        //uidContoPadre="+conto.uid+"
        }
        if(isGestioneConsentita && !figliPresenti){
                        nuovaIntestazione +="<li><a href='#' id ='linkAnnullamento'>annulla</a></li>";
        }
        if(isGestioneConsentita && !figliPresenti){
            nuovaIntestazione +="<li><a href='#' id ='linkEliminazione'>elimina</a></li>";
}
                    nuovaIntestazione +="</ul>" +
                                    "</div>" +
                                "</td>";
        riga.find('td').remove();
        riga.html(nuovaIntestazione);

        $("#HIDDEN_UidDaAnnullare").val(conto.uid);
        $("#linkAnnullamento").substituteHandler("click", gestioneModaleAnnullamento);
        
        $("#HIDDEN_UidDaEliminare").val(conto.uid);
        $("#linkEliminazione").substituteHandler("click", gestioneModaleEliminazione);

        var settings = {
              "content" : conto.descrizione,
              "title" : "Descrizione",
              "trigger" : "hover"
          };
          $("#popover_desc").off("click")
              .on("click", function(event) {event.preventDefault();})
              .popover(settings);

    }

    function gestioneModaleAnnullamento(){
        $("#msgAnnulla").modal("show");
    }
    
    function gestioneModaleEliminazione(){
        $("#msgElimina").modal("show");
    }


    function formatSN(flag){
        if(flag){
            return "S";
        }else{
            return "N";
        }
    }


    $(function() {
        $("#pulsanteRicercaPianoDeiConti").substituteHandler("click", function(){
            alertErrori.slideUp();
            ricercaPianoDeiConti();
        });
        if($("#HIDDEN_tabellaVisibile").val() === "true"){
            ricercaPianoDeiConti();
        }
        Conto.inizializza("#classePianoDeiContiRicerca", "#codificaInternaPianoDeiContiRicerca", "#codicePianoDeiContiRicerca", '#descrizionePianoDeiContiRicerca', "#pulsanteCompilazioneGuidataConto");
    
    });

}(jQuery, this);