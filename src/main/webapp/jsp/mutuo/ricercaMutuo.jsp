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
               <s:form id="formRicercaMutuo" cssClass="form-horizontal " novalidate="novalidate">

                  <h3>Ricerca Mutuo</h3>
                  <s:include value="/jsp/include/messaggi.jsp" />

                  <div class="step-content">
                     <div class="step-pane active" id="step1">
                        <h4>Dati mutuo</h4>
                        <div class="control-group">
                           <label class="control-label">Numero Mutuo</label>
                           <div class="controls">
                              <input type="text" name="mutuo.numero" class="input-small numeroNaturale">
                           </div>
                        </div>

                        <div class="control-group">
                           <span class="control-label">Tipo Tasso</span>
                           <div class="controls">
                              <div class="radio inline">
                                 <label class="radio inline"> <s:radio theme="simple" cssClass="tipoTasso" name="tipoTassoStr" list="#{'F':'Fisso'}"  />
                                 </label> <label class="radio inline"> <s:radio theme="simple" name="tipoTassoStr" cssClass="tipoTasso" list="#{'V':'Variabile'}"  />
                                 </label>
                              </div>
                           </div>
                        </div>

                        <div class="control-group">
                           <label class="control-label" for="periodo">Periodo</label>
                           <div class="controls">
                              <s:select list="listaPeriodoMutuo" name="mutuo.periodoRimborso.uid" id="listaPeriodoMutuo" headerKey="" headerValue="" listKey="uid"
                                 listValue="%{codice + ' - ' + descrizione}" cssClass="span2" />
                           </div>
                        </div>

                        <div class="control-group">
                           <label for="stato" class="control-label">Stato</label>
                           <div class="controls input-append">
                              <s:select list="listaStatoMutuo" cssClass="span2" name="mutuo.statoMutuo" id="stato" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
                           </div>
                        </div>

                        <div class="control-group">
                           <label for="oggetto" class="control-label">Oggetto del mutuo</label>
                           <div class="controls">
                              <s:textarea id="oggetto" rows="1" cols="15" cssClass="span9" name="mutuo.oggetto" />
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
                        <fieldset class="form-horizontal">
                           <div class="control-group">
                              <label class="control-label" for="annoAttoAmministrativo">Anno</label>
                              <div class="controls">
                                 <s:textfield id="annoAttoAmministrativo" maxlength="4" cssClass="lbTextSmall span1 numeroNaturale" name="mutuo.attoAmministrativo.anno" />
                                 <span class="al"> <label class="radio inline" for="numeroAttoAmministrativo">Numero</label>
                                 </span>
                                 <s:textfield id="numeroAttoAmministrativo" cssClass="lbTextSmall span2 numeroNaturale" name="mutuo.attoAmministrativo.numero" maxlength="7" />
                                 <span class="al"> <label class="radio inline" for="tipoAtto">Tipo</label>
                                 </span>
                                 <s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="model.mutuo.attoAmministrativo.tipoAtto.uid" id="tipoAtto" cssClass="span4" headerKey="0"
                                    headerValue="" />
                                 <s:hidden id="statoOperativoAttoAmministrativo" name="mutuo.attoAmministrativo.statoOperativo" />
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

                                 <s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid" name="mutuo.attoAmministrativo.strutturaAmmContabile.uid" />
                                 <s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoCodice" name="mutuo.attoAmministrativo.strutturaAmmContabile.codice" />
                                 <s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoDescrizione" name="mutuo.attoAmministrativo.strutturaAmmContabile.descrizione" />
                              </div>
                           </div>
                        </fieldset>


                        <h4 class="step-pane">
                           Istituto di Credito <span id="descrizioneCompletaSoggetto"><s:property value="descrizioneCompletaSoggetto" /></span>
                        </h4>

                        <div class="control-group">
                           <label class="control-label" for="codiceSoggetto">Codice</label>
                           <div class="controls">
                              <s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2 numeroNaturale" name="mutuo.soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
                              <span class="radio guidata"> <a href="#" id="pulsanteApriModaleSoggetto" class="btn btn-primary">compilazione guidata</a>
                              </span>
                              <s:hidden id="HIDDEN_SoggettoUid" name="mutuo.soggetto.uid" />
                           </div>

                        </div>

                     </div>
                  </div>
                  <p class="margin-large">
                     <s:include value="/jsp/include/indietro.jsp" />
                     <s:reset cssClass="btn btn-link" value="annulla" />
                     <s:submit id="submit-on-enter-key" cssClass="btn btn-primary pull-right" action="ricercaMutuo_ricerca" value="cerca" />
                  </p>

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
   <script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.new2.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/mutuo/cruMutuo.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/mutuo/ricercaMutuo.js"></script>
</body>
</html>