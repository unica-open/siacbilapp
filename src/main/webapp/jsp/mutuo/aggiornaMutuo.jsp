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
               <s:form id="formAggiornaMutuo" cssClass="form-horizontal ignore-enter-key" novalidate="novalidate">
                  <s:hidden name="mutuo.numero" />
                  <s:hidden id="statoMutuo" name="mutuo.statoMutuo" />
                  <s:hidden name="mutuo.uid" />

                  <h3>Modifica Mutuo</h3>
                  <!-- message errore -->
                  <s:include value="/jsp/include/messaggi.jsp" />

                  <div class="step-content">
                     <div class="step-pane active" id="step1">
                        <fieldset class="form-horizontal">
                           <h4>Dati mutuo</h4>
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
                              <span class="control-label">Tipo Tasso *</span>
                              <div class="controls">
                                 <div class="radio inline">
                                    <label class="radio inline"> <s:radio cssClass="%{mutuo.statoMutuo.bozza ? '' : 'readonly'}" theme="simple" id="tipoTasso" name="tipoTassoStr" list="#{'F':'Fisso'}" onclick="" />
                                    </label> <label class="radio inline"> <s:radio cssClass="%{mutuo.statoMutuo.bozza ? '' : 'readonly'}" theme="simple" name="tipoTassoStr" id="tipoTasso" list="#{'V':'Variabile'}" onclick="" />
                                    </label>
                                 </div>
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label" for="dataAtto">Data Atto Mutuo *</label>
                              <div class="controls">
                                 <s:textfield id="dataAtto" title="gg/mm/aaaa" name="mutuo.dataAtto" cssClass="lbTextSmall span2 datepicker" placeholder="dd/mm/aaaa"></s:textfield>
                              </div>
                           </div>


                           <div class="control-group">
                              <label for="oggetto" class="control-label">Oggetto del mutuo *</label>
                              <div class="controls">
                                 <s:textarea id="oggetto" rows="1" cols="15" cssClass="span9" name="mutuo.oggetto" />
                              </div>
                           </div>

                           <h4 class="step-pane">
                              Provvedimento *<span id="datiRiferimentoAttoAmministrativoSpan"> <s:if
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
                                 <s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2 numeroNaturale" name="mutuo.soggetto.codiceSoggetto" maxlength="20" placeholder="codice"
                                     />
                                 <span class="radio guidata"> <a href="#" id="pulsanteApriModaleSoggetto" class="btn btn-primary">compilazione guidata</a>
                                 </span>
                                 <s:hidden id="HIDDEN_soggettoUid" name="mutuo.soggetto.uid" />
                              </div>

                           </div>

                 
                           <div class="control-group">
                              <label class="control-label">Tipo finanziamento</label>
                              <div class="controls">
                              	<s:if test='mutuo.tipoFinanziamento.descrizione != null'>
                                 <s:textfield type="text" readonly="true" class="lbTextSmall span2" name="mutuo.tipoFinanziamento.descrizione"/>
                              </s:if><s:else>
                                 <s:textfield type="text" readonly="true" class="lbTextSmall span2" value="valore impostato dal sistema"/>
                              </s:else>
                              </div>
                           </div>


                           <div class="control-group">
                           
                              <label class="control-label">Sottoconto bancario *</label>
                              <div class="controls">
                                 <s:select list="listaContoTesoreria" id="listacontoTesoreria" headerKey="" headerValue="" name="mutuo.contoTesoreria.uid" cssClass="span3" listKey="uid"
                                    listValue="codice+' - '+descrizione" />
                              </div>
                           </div>

                           <h4>Scadenza</h4>
                           <div class="control-group">
                              <label class="control-label" for="periodo">Periodo *</label>
                              <div class="controls">
                                 <s:select list="listaPeriodoMutuo" name="mutuo.periodoRimborso.uid" id="listaPeriodoMutuo" headerKey="" headerValue="" listKey="%{uid + ':' + numeroMesi}"
                                    listValue="%{codice + ' - ' + descrizione}" cssClass="%{mutuo.statoMutuo.bozza ? '' : 'readonly'} span2" />
                                <span class="hide" id="periodoRimborsoUid" data-periodo-rimborso-uid='<s:property value="mutuo.periodoRimborso.uid"/>'></span>
                                    
                                 <span class="al"> <label class="radio inline" for="scadenzaPrimaRata">Prima Rata *</label>
                                 </span>
                                 <s:textfield readonly="mutuo.statoMutuo.predefinitivo or mutuo.statoMutuo.definitivo" id="scadenzaPrimaRata" title="gg/mm/aaaa" name="mutuo.scadenzaPrimaRata" 
                                    cssClass="%{mutuo.statoMutuo.predefinitivo or mutuo.statoMutuo.definitivo ? '' : 'datepicker'} lbTextSmall span2" placeholder="dd/mm/aaaa"></s:textfield>
                              </div>
                           </div>

                           <h4>Periodo di ammortamento</h4>
                           <div class="control-group">
                              <label class="control-label" for="durataAnni">Durata *</label>
                              <div class="controls">
                                 <s:textfield readonly="not mutuo.statoMutuo.bozza" id="durataAnni" 
                                    cssClass="lbTextSmall span2 numeroNaturale" name="mutuo.durataAnni" maxlength="2" placeholder="aa" />
                                 <span class="al"> <label class="radio inline" for="annoInizio">Dal *</label>
                                
                                 </span> 
                                 
                                 <s:if test="mutuo.statoMutuo.bozza">
                                    <select data-selected='<s:property value="mutuo.annoInizio"/>' 
                                       <s:if test="mutuo.scadenzaPrimaRata == null">disabled="disabled"</s:if> 
                                       name="mutuo.annoInizio" id="annoInizio" class="span1">
                                       <option value="" disabled>aaaa</option>
                                    </select>
                                 </s:if>
                                 <s:else>
                                    <s:textfield readonly="true" id="annoInizio" cssClass="lbTextSmall span2 numeroNaturale" name="mutuo.annoInizio"/>
                                 </s:else>

                                 <span class="al"> <label class="radio inline" for="annoFine">Al</label>
                                 </span>
                                 <s:textfield type="text" readonly="true" class="lbTextSmall span1" name="mutuo.annoFine" id="annoFine" />

                              </div>
                           </div>

                           <h4>Importi, Tasso</h4>
                           <div class="control-group">
                              <label class="control-label">Importo *</label>
                              <div class="controls">
                                 <s:textfield readonly="mutuo.statoMutuo.predefinitivo or mutuo.statoMutuo.definitivo" id="sommaMutuataIniziale" name="mutuo.sommaMutuataIniziale" 
                                    cssClass="span2 decimale soloNumeri forzaVirgolaDecimale"  />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label" for="tassoInteresse">Tasso Interesse *</label>
                              <div class="controls">
                                 <s:textfield readonly="mutuo.statoMutuo.predefinitivo or mutuo.statoMutuo.definitivo" type="text" id="tassoInteresse" 
                                    cssClass="lbTextSmall span2 soloNumeri forzaVirgolaDecimale decimale" name="mutuo.tassoInteresse" />
                                 <span class="al"> <label class="radio inline" for="tassoInteresseEuribor" id="tassoInteresseEuriborLabel">Euribor *</label>
                                 </span>
                                 <s:textfield readonly="mutuo.statoMutuo.predefinitivo or mutuo.statoMutuo.definitivo" type="text" id="tassoInteresseEuribor" 
                                    cssClass="lbTextSmall span2 soloNumeri forzaVirgolaDecimale decimale" name="mutuo.tassoInteresseEuribor" />
                                 <span class="al"> <label class="radio inline" for="tassoInteresseSpread" id="tassoInteresseSpreadLabel">Spread *</label>
                                 </span>
                                 <s:textfield readonly="mutuo.statoMutuo.predefinitivo or mutuo.statoMutuo.definitivo" type="text" id="tassoInteresseSpread" 
                                    cssClass="lbTextSmall span2 soloNumeri forzaVirgolaDecimale decimale" name="mutuo.tassoInteresseSpread" />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label">Debito residuo</label>
                              <div class="controls">
                                 <s:textfield type="text" readonly="true" class="lbTextSmall span2 soloNumeri forzaVirgolaDecimale decimale" id="debitoResiduo" name="mutuo.debitoResiduo" />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label">Annualit&agrave; 
                              <i class="icon-info-sign" 
                                            data-toggle="popover" 
                                            data-original-title="" 
                                            data-trigger="hover" 
                                            data-content="Quote capitale e interessi delle rate nell'anno (esclusi gli oneri)"></i> 
                                       *</label>
                              <div class="controls">
                                 <s:textfield readonly="mutuo.statoMutuo.predefinitivo or mutuo.statoMutuo.definitivo" id="annualita" name="mutuo.annualita" cssClass="span2 soloNumeri forzaVirgolaDecimale decimale"   />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label">Preammortamento</label>
                              <div class="controls">
                                 <s:textfield id="preammortamento" name="mutuo.preammortamento" class="span2 soloNumeri forzaVirgolaDecimale decimale" />
                              </div>
                           </div>
                        </fieldset>
                     </div>
                  </div>

                  <div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
                     <div class="modal-body">
                        <div class="alert alert-error">
                           <button type="button" class="close" data-hide="alert">&times;</button>
                           <p>
                              <strong>Attenzione!</strong>
                           </p>
                           <p>Stai per annullare l'elemento selezionato: sei sicuro di voler proseguire?</p>
                        </div>
                     </div>
                     <div class="modal-footer">
                        <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                        <button class="btn btn-primary" formmethod="post" type="submit" formaction="aggiornaMutuo_annulla.do">si, prosegui</button>
                     </div>
                  </div>


                  <p class="margin-medium">
                     <s:include value="/jsp/include/indietro.jsp" />

                     <s:submit value="annulla" cssClass="btn" action="aggiornaMutuo" />

                     <s:if test="mutuo.tipoTasso.fisso">
                        <s:submit disabled="mutuo.statoMutuo.bozza and mutuo.primaRataScaduta" value="piano ammortamento" cssClass="btn pianoAmmortamento" action="pianoAmmortamentoMutuoTassoFisso" />
                     </s:if>

                     <s:if test="mutuo.tipoTasso.variabile">
                        <s:submit disabled="mutuo.statoMutuo.bozza and mutuo.primaRataScaduta" value="piano ammortamento" cssClass="btn pianoAmmortamento" action="pianoAmmortamentoMutuoTassoVariabile" />
                     </s:if>
                                       
                  <s:if test='mutuo.statoMutuo.definitivo or mutuo.statoMutuo.predefinitivo'>
                 	 <s:submit value="movimenti contabili" cssClass="btn" action="movimentiGestioneAssociatiMutuo" />
                  </s:if>
                  
                 	 <s:submit value="progetti" cssClass="btn" action="progettiAssociatiMutuo" />
                     
                     <s:submit value="ripartizione capitoli" cssClass="btn" action="ripartizioneMutuo" /> 
                  
                     <s:submit value="salva" cssClass="btn btn-primary pull-right" action="aggiornaMutuo_salva" />

                  <s:if test='mutuo.statoMutuo.bozza'>
                     <span> <a href='#msgAnnulla' data-toggle='modal' class="btn">annulla mutuo</a></span>
                  </s:if>
                  
                  </p>

                  <s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
                  <s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />

               </s:form>

            </div>
         </div>
      </div>
   </div>



   <s:include value="/jsp/include/footer.jsp" />
   <s:include value="/jsp/include/javascript.jsp" />
   <script type="text/javascript" src="/siacbilapp/js/local/predocumento/ztree.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.new2.js"></script>
   <script type="text/javascript" src="/siacbilapp/js/local/mutuo/cruMutuo.js"></script>


</body>
</html>