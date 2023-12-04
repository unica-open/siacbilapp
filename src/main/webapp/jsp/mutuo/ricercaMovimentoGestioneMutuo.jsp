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
               <s:include value="/jsp/include/messaggi.jsp" />

               <s:form id="formRicercaMovimentoGestioneMutuo" cssClass="form-horizontal">
                       <s:hidden name="mutuo.uid" />

                  <h3>Ricerca movimenti contabili mutuo</h3>

                  <div class="step-content">
                     <div class="step-pane active" id="step1">

                        <fieldset class="form-horizontal">
                           <h4>Dati mutuo</h4>
                           <div class="control-group">
                              <label class="control-label">Numero Mutuo</label>
                              <div class="controls">
                                 <s:textfield type="text" readonly="true" class="lbTextSmall span1" name="mutuo.numero" id="numeroMutuo" />
                                 
                                 <span class="al"> <label class="radio inline" for="tipoTasso">Tipo Tasso</label>
                                 </span>
                                 <s:textfield type="text" readonly="true" class="lbTextSmall span1" name="mutuo.tipoTasso" id="tipoTasso" />

                                 <span class="al"> <label class="radio inline" for="sommaMutuataIniziale">Importo</label></span>
                                 <s:textfield readonly="true" id="sommaMutuataIniziale" name="mutuo.sommaMutuataIniziale" cssClass="span2 decimale soloNumeri forzaVirgolaDecimale" />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label">Istituto di Credito</label>
                              <div class="controls">
                                 <s:textfield type="text" readonly="true" class="lbTextSmall span1" name="mutuo.soggetto.codiceSoggetto" id="codiceSoggettoMutuo" />
                                 <s:textfield type="text" readonly="true" class="lbTextSmall span6" name="mutuo.soggetto.denominazione" />
                              </div>
                           </div>

                           <h4>Tipo movimento</h4>
                           <div class="control-group">
                              <div class="controls">
                                 <div class="radio inline">
                                    <label class="radio inline"> <s:radio cssClass="tipoMovimento tipoMovimentoEntrata" theme="simple" id="tipoMovimentoEntrata" name="movimentoGestione.tipoMovimento" list="#{'A':'Entrata'}" />
                                    </label> <label class="radio inline"> <s:radio cssClass="tipoMovimento" theme="simple" name="movimentoGestione.tipoMovimento" id="tipoMovimentoSpesa" list="#{'I':'Spesa'}" />
                                    </label>
                                 </div>
                              </div>
                           </div>
                           
						<div id="altriCampiDiRicerca">
                           <h4>Movimento</h4>
                           <fieldset class="form-horizontal">
                              <div class="control-group">
                                 <label class="control-label" for="anno1">Anno</label>
                                 <div class="controls">
                                    <s:textfield id="annoMovimento" maxlength="4" cssClass="lbTextSmall span1 numeroNaturale" name="movimentoGestione.anno" value="%{bilancio.anno}"></s:textfield>
                                    <span class="al"> <label class="radio inline" for="numero">Numero</label>
                                    </span>
                                    <s:textfield id="numeroMovimento" cssClass="lbTextSmall span2 numeroNaturale" name="movimentoGestione.numero"></s:textfield>
                                 </div>
                              </div>
                           </fieldset>



                           <h4 class="step-pane">
                           Capitolo
                           	<span class="datiRIFCapitolo" id="datiRiferimentoCapitoloSpan">
								<s:if test="%{movimentoGestione.capitolo != null && movimentoGestione.capitolo.annoCapitolo != null && movimentoGestione.capitolo.numeroCapitolo != null && movimentoGestione.capitolo.numeroArticolo != null && (!gestioneUEB || movimentoGestione.capitolo.numeroUEB != null)}">
									: <s:property value="movimentoGestione.capitolo.annoCapitolo" /> / <s:property value="movimentoGestione.capitolo.numeroCapitolo" /> / <s:property value="movimentoGestione.capitolo.numeroArticolo" />
									<s:if test="%{gestioneUEB}">
										/ <s:property value="movimentoGestione.capitolo.numeroUEB" />
									</s:if>
								</s:if>
							</span>
							</h4>

                              <div class="control-group">
                                 <div class="controls">
                                    <span class="al"> <label class="radio inline" for="capitolo">Anno</label>
                                    </span>
                                    <s:textfield id="annoCapitolo" cssClass="lbTextSmall span1 parametroCapitolo" name="movimentoGestione.capitolo.annoCapitolo" value="%{bilancio.anno}" disabled="true"></s:textfield>

                                    <span class="al"> <label for="numeroCapitolo" class="radio inline">Capitolo</label>
                                    </span>
                                    <s:textfield id="numeroCapitolo" name="movimentoGestione.capitolo.numeroCapitolo" cssClass="lbTextSmall soloNumeri forzaVirgolaDecimale span2" maxlength="30"
                                       placeholder="%{'capitolo'}" />
                                    <span class="al"> <label for="numeroArticolo" class="radio inline">Articolo</label>
                                    </span>
                                    <s:textfield id="numeroArticolo" name="movimentoGestione.capitolo.numeroArticolo" cssClass="lbTextSmall soloNumeri forzaVirgolaDecimale span2" maxlength="7"
                                       placeholder="%{'articolo'}" />

                                    <span class="al componenteCapitolo"> 
                                    	<label class="radio inline" for="tipoAtto">Componente</label>
										<s:select list="listaTipoComponente" listKey="uid" listValue="descrizione"
                                          name="movimentoGestione.componenteBilancioMovimentoGestione.idTipoComponente" id="tipoComponente" cssClass="parametroRicercaCapitolo" headerKey="" headerValue="" />
                                    </span> 
                                    <span class="radio guidata">
                                       <button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataCapitolo">compilazione guidata</button>
                                    </span>
                                 </div>
                              </div>


                           <h4 class="step-pane">
                              Provvedimento <span id="datiRiferimentoAttoAmministrativoSpan"> <s:if
                                    test='%{mutuo.attoAmministrativo != null && (mutuo.attoAmministrativo.anno ne null && attoAmministrativo.anno != "") && (mutuo.attoAmministrativo.numero ne null && mutuo.attoAmministrativo.numero != "") && (mutuo.attoAmministrativo.tipoAtto.uid ne null && mutuo.attoAmministrativo.tipoAtto.uid != "")}'>
                                    <s:property
                                       value="%{mutuo.attoAmministrativo.tipoAtto.descrizione+ '/'+ mutuo.attoAmministrativo.anno + ' / ' + mutuo.attoAmministrativo.numero + ' - ' + mutuo.attoAmministrativo.oggetto}" />
                                 </s:if>
                              </span>
                           </h4>
                           
                           <div class="control-group">
                              <label class="control-label" for="annoAttoAmministrativo">Anno</label>
                              <div class="controls">
                                 <s:hidden id="uidAttoAmministrativo" name="movimentoGestione.attoAmministrativo.uid" />
                                 <s:textfield id="annoAttoAmministrativo" maxlength="4" cssClass="lbTextSmall span1 numeroNaturale" name="movimentoGestione.attoAmministrativo.anno" />
                                 <span class="al"> <label class="radio inline" for="numeroAttoAmministrativo">Numero</label>
                                 </span>
                                 <s:textfield id="numeroAttoAmministrativo" cssClass="lbTextSmall span2 numeroNaturale" name="movimentoGestione.attoAmministrativo.numero" maxlength="7" />
                                 <span class="al"> <label class="radio inline" for="tipoAtto">Tipo</label>
                                 </span>
                                 <s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="movimentoGestione.attoAmministrativo.tipoAtto.uid" id="tipoAtto" cssClass="span4" headerKey="0"
                                    headerValue="" />
                                 <s:hidden id="statoOperativoAttoAmministrativo" name="movimentoGestione.attoAmministrativo.statoOperativo" />
                                 <span class="radio guidata"> <a href="#" id="pulsanteApriModaleProvvedimento" class="btn btn-primary">compilazione guidata</a>
                                 </span>
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label">Struttura Amministrativa</label>
                              <div class="controls">
                                 <div class="accordion span8 struttAmm" id="accordionStrutturaAmministrativaContabileAttoAmministrativo">
                                    <div class="accordion-group">
                                       <div class="accordion-heading">
                                          <a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa" href="#collapseStrutturaAmministrativaContabileAttoAmministrativo"
                                             data-parent="#accordionStrutturaAmministrativaContabileAttoAmministrativo"> <span id="SPAN_StrutturaAmministrativoContabileAttoAmministrativo">Seleziona
                                                la Struttura amministrativa</span>
                                          </a>
                                       </div>
                                       <div id="collapseStrutturaAmministrativaContabileAttoAmministrativo" class="accordion-body collapse">
                                          <div class="accordion-inner">
                                             <ul id="treeStruttAmmAttoAmministrativo" class="ztree treeStruttAmm"></ul>
                                          </div>
                                       </div>
                                    </div>
                                 </div>

                                 <s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid" name="movimentoGestione.attoAmministrativo.strutturaAmmContabile.uid" />
                                 <s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoCodice" name="movimentoGestione.attoAmministrativo.strutturaAmmContabile.codice" />
                                 <s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoDescrizione" name="movimentoGestione.attoAmministrativo.strutturaAmmContabile.descrizione" />
                              </div>
                           </div>

                           <h4 class="step-pane">
                              Soggetto <span id="descrizioneCompletaSoggetto"><s:property value="descrizioneCompletaSoggetto" /></span>                  
                           </h4>

                           <div class="control-group">
                              <label class="control-label" for="codiceSoggetto">Codice</label>
                              <div class="controls">
                                 <s:textfield id="codiceSoggetto" data-carica-dettaglio="false" cssClass="lbTextSmall span2 numeroNaturale" name="movimentoGestione.soggetto.codiceSoggetto" maxlength="20" placeholder="codice" />
                                 <span class="radio guidata"> <a href="#" id="pulsanteApriModaleSoggetto" class="btn btn-primary">compilazione guidata</a>
                                 </span>
                                 <s:hidden id="HIDDEN_soggettoUid" name="movimentoGestione.soggetto.uid" />
                              </div>

                           </div>
                           
						</div>
                        </fieldset>




                     </div>
                  </div>
                  <p class="margin-medium">
                     <s:include value="/jsp/include/indietro.jsp" />
                     <s:submit value="annulla" cssClass="btn" action="ricercaMovimentoGestioneMutuo" />
					
                     <s:submit id="submit-on-enter-key" value="cerca" cssClass="btn btn-primary pull-right" action="ricercaMovimentoGestioneMutuo_ricerca" />
                  </p>
                  <!-- preso da step1 ordinativo pagamento -->
                  <s:include value="/jsp/capUscitaGestione/selezionaCapitolo_modale.jsp" />
                  <s:include value="/jsp/capEntrataGestione/selezionaCapitolo_modale.jsp" />
                  <s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
                  <%-- TODO refactor listaclassisoggetto --%>
                  <s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
               </s:form>
            </div>
         </div>
      </div>
   </div>

   <%-- Caricamento del footer --%>
   <s:include value="/jsp/include/footer.jsp" />
   <s:include value="/jsp/include/javascript.jsp" />

   <script type="text/javascript" src="/siacbilapp/js/local/predocumento/ztree.js"></script>

   <script type="text/javascript" src="/siacbilapp/js/local/capitolo/ricercaCapitoloModale.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.new2.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/mutuo/cruMutuo.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/mutuo/ricercaMovimentoGestioneMutuo.js"></script>

</body>
</html>