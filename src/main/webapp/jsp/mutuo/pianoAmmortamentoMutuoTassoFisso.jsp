<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>

   <%-- Inclusione header --%>
   <s:include value="/jsp/include/header.jsp" />

   <div class="container-fluid">
      <div class="row-fluid">
         <div class="span12 ">

            <div class="contentPage">
               <s:form id="formPianoAmmortamentoMutuoTassoFisso" cssClass="form-horizontal ignore-enter-key" novalidate="novalidate">
                  <s:hidden name="mutuo.uid" />
                  <s:hidden name="mutuo.statoMutuo" />
				  <s:hidden name="mutuo.numero" />
				  
                  <h3>Piano Ammortamento Mutuo a Tasso Fisso</h3>
                  <!-- message errore -->
                  <s:include value="/jsp/include/messaggi.jsp" />

                  <div class="step-content">
                     <div class="step-pane active" id="step1">
                        <fieldset class="form-horizontal">
                           <br>
                           <div class="control-group">
                              <label class="control-label">Numero Mutuo</label>
                              <div class="controls">
                                 <s:textfield type="text" disabled="true" class="lbTextSmall span2" name="mutuo.numero" id="numeroMutuo" />
                                 <span class="al"> <label class="radio inline" for="statoMutuo">Stato</label>
                                 </span>
                                 <s:textfield type="text" disabled="true" class="lbTextSmall span2" name="mutuo.statoMutuo" id="statoMutuo" />
                              </div>
                           </div>

                           <div class="control-group">
                              <span class="control-label">Tipo Tasso</span>
                              <div class="controls">
                                 <div class="radio inline">
                                    <label class="radio inline"> <s:radio disabled="true" theme="simple" id="tipoTasso" name="tipoTassoStr" list="#{'F':'Fisso'}"  />
                                    </label> 
                                    <label class="radio inline"> <s:radio disabled="true" theme="simple" name="tipoTassoStr" id="tipoTasso" list="#{'V':'Variabile'}"  />
                                    </label>
                                 </div>
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label">Importo</label>
                              <div class="controls">
                                 <s:textfield readonly="true" id="sommaMutuataIniziale" name="mutuo.sommaMutuataIniziale" class="span2 soloNumeri forzaVirgolaDecimale decimale" />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label" for="tassoInteresse">Tasso Interesse</label>
                              <div class="controls">
                                 <s:textfield type="text" id="tassoInteresse" class="lbTextSmall span2 soloNumeri forzaVirgolaDecimale decimale" readonly="true" name="mutuo.tassoInteresse" />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label" for="codiceSoggetto">Istituto di credito</label>
                              <div class="controls">
                                 <s:textfield readonly="true" id="codiceSoggetto" cssClass="lbTextSmall span2 numeroNaturale" name="mutuo.soggetto.codiceSoggetto" maxlength="20" placeholder="codice"  />
                                 <s:textfield type="text" disabled="true" class="lbTextSmall span6" name="mutuo.soggetto.denominazione" />
                                 <s:hidden id="HIDDEN_SoggettoUid" name="mutuo.soggetto.uid" />
                              </div>
                           </div>

                           <h4>Periodo di ammortamento</h4>
                           <div class="control-group">
                              <label class="control-label" for="durataAnni">Durata</label>
                              <div class="controls">
                                 <s:textfield readonly="true" type="text" id="durataAnni" class="lbTextSmall span2 numeroNaturale" name="mutuo.durataAnni" maxlength="2" placeholder="aa" />
                                 <span class="al"> <label class="radio inline" for="annoInizio">Dal</label>
                                 </span>
                                 <s:textfield readonly="true" type="text" id="annoInizio" class="lbTextSmall span2 numeroNaturale" name="mutuo.annoInizio" maxlength="4" minlength="4"
                                    placeholder="aaaa" />
                                 <span class="al"> <label class="radio inline" for="annoFine">Al</label>
                                 </span>
                                 <s:textfield type="text" readonly="true" class="lbTextSmall span2" placeholder="aaaa" name="mutuo.annoFine" id="annoFine" />
                              </div>

                           </div>

                           <div class="control-group">
                              <label class="control-label" for="periodo">Periodo</label>
                              <div class="controls">
                                 <s:select disabled="true" list="listaPeriodoMutuo" name="mutuo.periodoRimborso.uid" id="listaPeriodoMutuo" headerKey="" headerValue="" listKey="uid"
                                    listValue="%{codice + ' - ' + descrizione}" cssClass="span2" />
                              </div>
                           </div>

                           <s:if test="not mutuo.primaRataScaduta and not mutuo.statoMutuo.annullato">
	                              <s:submit value="scarica modello csv" id="scaricaModelloExcelTassoFisso" action="pianoAmmortamentoMutuoTassoFisso_scaricaModelloCsv" class="btn scaricaModelloCsv disableOverlay" />
	
	                              <span class="btn btn-file">carica rate da csv<input type="file" name="fileElencoRate" id="carica-rate-da-file" class="typeFile"></span>
	                           
	                              <s:submit id="calcolaRate" value="calcola rate" cssClass="btn" action="pianoAmmortamentoMutuoTassoFisso_calcolaRate" />
                           </s:if>
                           
                           <s:if test="mutuo.statoMutuo.bozza and mutuo.elencoRate.empty">
                              <button type="button" class="btn aggiungiRata" data-idx="0">aggiungi rata</button>
                           </s:if>

                             <h4>Tabella rate di mutuo tasso fisso</h4>
                           <s:if test="not mutuo.elencoRate.empty">
                              <s:include value="/jsp/mutuo/include/elencoRateMutuo.jsp" />
                           </s:if><s:else>
                              Non sono presenti rate.
                           </s:else>
                        </fieldset>
                     </div>
                  </div>


                  <p class="margin-medium">
                     <s:include value="/jsp/include/indietro.jsp" />
                     <s:submit value="annulla" cssClass="btn" action="pianoAmmortamentoMutuoTassoFisso" />

					 <s:hidden name="codiceStatoSalva"/>
                     <s:if test='(mutuo.pianoAmmortamentoModificabile or mutuo.importiPianoAmmortamentoModificabili) and not mutuo.elencoRate.empty'>
                        <s:submit id="salva" value="salva" cssClass="btn btn-primary pull-right salva" action="pianoAmmortamentoMutuoTassoFisso_salva" data-stato-mutuo="D"/>
                     </s:if>
					 <s:if test='(mutuo.pianoAmmortamentoModificabile or mutuo.importiPianoAmmortamentoModificabili) and not mutuo.statoMutuo.definitivo and not mutuo.elencoRate.empty'>
                        <s:submit id="salvaPredefinitivo" value="salva (predefinitivo)" cssClass="btn pull-right salva" action="pianoAmmortamentoMutuoTassoFisso_salva" data-stato-mutuo="P"/>
                     </s:if>

                     <s:if test='mutuo.statoMutuo.definitivo'>
                        <s:submit value="variazione piano" cssClass="btn" action="variazionePianoMutuo" />
                     </s:if>

                     <s:if test='mutuo.statoMutuo.predefinitivo or mutuo.statoMutuo.definitivo'>
                       <s:submit value="esporta piano ammortamento in csv" cssClass="btn scaricaPianoCsv disableOverlay" action="pianoAmmortamentoMutuoTassoFisso_scaricaPianoCsv" />
                     </s:if>

					 <s:if test='mutuo.statoMutuo.predefinitivo or mutuo.statoMutuo.definitivo'>
                       <s:submit value="esporta piano ammortamento in excel" cssClass="btn scaricaPianoExcel disableOverlay" action="pianoAmmortamentoMutuoTassoFisso_scaricaPianoExcel" />
                     </s:if>
                     
                      <s:if test='mutuo.statoMutuo.predefinitivo or mutuo.statoMutuo.definitivo'>
                     <span><a href='#msgAnnulla' data-toggle='modal' class="btn">annulla piano</a></span>
                     </s:if>
                  </p>
                  <s:include value="/jsp/mutuo/include/annullaPianoAmmortamentoMutuo_modale.jsp" />
               </s:form>
               <span class="hide">
                  <s:form id="form-carica-rate-da-file" action="pianoAmmortamentoMutuoTassoFisso_caricaRateDaFile" enctype="multipart/form-data"><s:hidden name="mutuo.uid" /></s:form>
               </span>
            </div>
         </div>
      </div>
   </div>



   <s:include value="/jsp/include/footer.jsp" />
   <s:include value="/jsp/include/javascript.jsp" />
   <script type="text/javascript" src="/siacbilapp/js/local/mutuo/pianoAmmortamentoMutuo.js"></script>


</body>
</html>