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
               <s:form id="formVariazioneTassoMutuoTassoVariabile" cssClass="form-horizontal ignore-enter-key" novalidate="novalidate">
                  <s:hidden name="mutuo.uid" />

                  <h3>Variazione Tasso Mutuo a tasso variabile</h3>
                  <!-- message errore -->
                  <s:include value="/jsp/include/messaggi.jsp" />

                  <div class="step-content">
                     <div class="step-pane active" id="step1">
                        <fieldset class="form-horizontal">
                           <br>
                           <h4>Dati mutuo</h4>
                           <div class="control-group">
                              <label class="control-label">Numero Mutuo</label>
                              <div class="controls">
                                 <s:textfield type="text" readonly="true" class="lbTextSmall span2" name="mutuo.numero" id="numeroMutuo" />
                                 <span class="al"> <label class="radio inline" for="statoMutuo">Stato</label>
                                 </span>
                                 <s:textfield type="text" readonly="true" class="lbTextSmall span2" name="mutuo.statoMutuo" id="statoMutuo" />
                              </div>
                           </div>

                           <div class="control-group">
                              <span class="control-label">Tipo tasso</span>
                              <div class="controls">
                                 <div class="radio inline">
                                    <label class="radio inline"> <s:radio theme="simple" id="tipoTasso" name="tipoTassoStr" list="#{'F':'Fisso'}" onclick="" disabled="true" />
                                    </label> <label class="radio inline"> <s:radio theme="simple" name="tipoTassoStr" id="tipoTasso" list="#{'V':'Variabile'}" onclick="" disabled="true" />
                                    </label>
                                 </div>
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label">Importo</label>
                              <div class="controls">
                                 <s:textfield readonly="true" class="bTextSmall span2 soloNumeri forzaVirgolaDecimale decimale" name="mutuo.sommaMutuataIniziale" id="sommaMutuataIniziale" />
                                 <span class="al"> <label class="radio inline" for="debitoResiduo">Debito residuo</label>
                                 </span>
                                 <s:textfield readonly="true" class="span2 soloNumeri forzaVirgolaDecimale decimale" name="mutuo.debitoResiduo" id="debitoResiduo" />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label" for="tassoInteresse">Tasso Interesse</label>
                              <div class="controls">
                                <s:textfield type="text" id="tassoInteresse" class="lbTextSmall span2 soloNumeri forzaVirgolaDecimale decimale"  name="mutuo.tassoInteresse" readonly="true" />
                                 <span class="al"> <label class="radio inline" for="tassoInteresseEuribor" id="tassoInteresseEuriborLabel">Euribor</label>
                                 </span>
                                 <s:textfield type="text" id="tassoInteresseEuribor" class="lbTextSmall span2 soloNumeri forzaVirgolaDecimale decimale"  name="mutuo.tassoInteresseEuribor" readonly="true" />
                                 <span class="al"> <label class="radio inline" for="tassoInteresseSpread" id="tassoInteresseSpreadLabel">Spread</label>
                                 </span>
                                 <s:textfield type="text" id="tassoInteresseSpread" class="lbTextSmall span2 soloNumeri forzaVirgolaDecimale decimale"  name="mutuo.tassoInteresseSpread" readonly="true" />
                              </div>
                           </div>


                           <div class="control-group">
                              <label class="control-label" for="codiceSoggetto">Istituto di credito</label>
                              <div class="controls">
                                 <s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2 numeroNaturale" name="mutuo.soggetto.codiceSoggetto" maxlength="20" placeholder="codice"
                                     readonly="true" />
                                     <s:textfield id="codiceSoggetto" cssClass="span6" name="mutuo.soggetto.denominazione" maxlength="20" placeholder="denominazione"
                                     readonly="true" />
                                 <s:hidden id="HIDDEN_SoggettoUid" name="mutuo.soggetto.uid" />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label" for="RataMutuoAnno">Anno *</label>
                              <div class="controls">
                                 <s:textfield type="text" id="anno" class="lbTextSmall span2 numeroNaturale"  name="variazioneMutuo.rataMutuo.anno" maxlength="4" placeholder="aaaa" />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label">Numero Rata *</label>
                              <div class="controls">
                                 <s:select list="listaNumeriRataAnno"  name="variazioneMutuo.rataMutuo.numeroRataAnno"
                                       id="numeroRata" cssClass="span1" headerKey="0" headerValue="" />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label">Importo Rata *</label>
                              <div class="controls">
                                 <s:textfield id="sommaMutuataIniziale" name="variazioneMutuo.rataMutuo.importoTotale" class="span2 soloNumeri forzaVirgolaDecimale decimale" />
                              </div>
                           </div>

                           <p class="margin-medium">
                              <s:include value="/jsp/include/indietro.jsp" />

                              <s:submit value="annulla" cssClass="btn" action="variazioneTassoMutuo" />

                              <s:submit value="salva" cssClass="btn btn-primary pull-right" action="variazioneTassoMutuo_salva" />
                           </p>

                        </fieldset>
                     </div>
                  </div>

               </s:form>

            </div>
         </div>
      </div>
   </div>



   <s:include value="/jsp/include/footer.jsp" />
   <s:include value="/jsp/include/javascript.jsp" />
   <script type="text/javascript" src="/siacbilapp/js/local/mutuo/variazioneTassoMutuo.js"></script>


</body>
</html>