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

                  <h3>Ricerca progetti mutuo</h3>

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
                  
						<div id="altriCampiDiRicerca">
                           <h4>Progetto</h4>
                           <fieldset class="form-horizontal">
                              <div class="control-group">
                                 <label class="control-label" for="codiceProgetto">Codice</label>
                                 <div class="controls">
                                    <s:textfield id="codiceProgetto" maxlength="20"  cssClass="lbTextSmall span3" name="progetto.codice"></s:textfield>
                                 </div>
                              </div>
							  <div class="control-group">
								<label for="ambito" class="control-label">Ambito</label>
								<div class="controls input-append">
									<s:select list="listaTipoAmbito" cssClass="span8" name="progetto.tipoAmbito.uid" id="ambito" headerKey="" headerValue=""
										listValue="%{codice + ' - ' + descrizione}" listKey="uid" />
								</div>
 							  </div>
                           </fieldset>

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
                                 <s:hidden id="uidAttoAmministrativo" name="progetto.attoAmministrativo.uid" />
                                 <s:textfield id="annoAttoAmministrativo" maxlength="4" cssClass="lbTextSmall span1 numeroNaturale" name="progetto.attoAmministrativo.anno" />
                                 <span class="al"> <label class="radio inline" for="numeroAttoAmministrativo">Numero</label>
                                 </span>
                                 <s:textfield id="numeroAttoAmministrativo" cssClass="lbTextSmall span2 numeroNaturale" name="progetto.attoAmministrativo.numero" maxlength="7" />
                                 <span class="al"> <label class="radio inline" for="tipoAtto">Tipo</label>
                                 </span>
                                 <s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="progetto.attoAmministrativo.tipoAtto.uid" id="tipoAtto" cssClass="span4" headerKey="0"
                                    headerValue="" />
                                 <s:hidden id="statoOperativoAttoAmministrativo" name="progetto.attoAmministrativo.statoOperativo" />
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

                                 <s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid" name="progetto.attoAmministrativo.strutturaAmmContabile.uid" />
                                 <s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoCodice" name="progetto.attoAmministrativo.strutturaAmmContabile.codice" />
                                 <s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoDescrizione" name="progetto.attoAmministrativo.strutturaAmmContabile.descrizione" />
                              </div>
                           </div>

						</div>
                        </fieldset>
                     </div>
                  </div>
                  <p class="margin-medium">
                     <s:include value="/jsp/include/indietro.jsp" />
                     <s:submit value="annulla" cssClass="btn" action="ricercaProgettoMutuo" />
					
                     <s:submit id="submit-on-enter-key" value="cerca" cssClass="btn btn-primary pull-right" action="ricercaProgettoMutuo_ricerca" />
                  </p>
                  <!-- preso da step1 ordinativo pagamento -->
                  <s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
               </s:form>
            </div>
         </div>
      </div>
   </div>

   <%-- Caricamento del footer --%>
   <s:include value="/jsp/include/footer.jsp" />
   <s:include value="/jsp/include/javascript.jsp" />

   <script type="text/javascript" src="/siacbilapp/js/local/predocumento/ztree.js"></script>

   <script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.new2.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/mutuo/cruMutuo.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/mutuo/ricercaProgettoMutuo.js"></script>

</body>
</html>