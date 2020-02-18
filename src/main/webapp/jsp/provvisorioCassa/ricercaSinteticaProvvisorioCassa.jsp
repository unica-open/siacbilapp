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

	<s:hidden id="HIDDEN_anno_datepicker" value="%{annoEsercizioInt}" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="effettuaRicercaSinteticaProvvisorioDiCassa" id="formRicercaProvvisorioDiCassa" novalidate="novalidate">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Ricerca provvisori cassa</h3>
					<p>&Eacute; necessario inserire almeno un criterio di ricerca</p>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<p class="margin-medium">
								<s:submit value="cerca" cssClass="btn btn-primary pull-right" />
								<br/>
							</p>
							<h4>Dati principali</h4>
							<fieldset class="form-horizontal margin-large">
								<div class="control-group">
									<label for="tipoProvvisorioDiCassa" class="control-label">Documento</label>
									<div class="controls">
										<select id="tipoProvvisorioDiCassa" name="provvisorioDiCassa.tipoProvvisorioDiCassa" class="span5">
											<option value=""></option>
											<option value="E" <s:if test='%{"E" eq provvisorioDiCassa.tipoProvvisorioDiCassa.name()}'>selected</s:if>>Entrata</option>
											<option value="S" <s:if test='%{"S" eq provvisorioDiCassa.tipoProvvisorioDiCassa.name()}'>selected</s:if>>Spesa</option>
										</select>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="numeroProvvisorioDiCassa">Numero</label>
									<div class="controls">
										<s:textfield id="numeroProvvisorioDiCassa" name="provvisorioDiCassa.numero" cssClass="lbTextSmall span2 soloNumeri"/>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="distinta">Conto evidenza</label>
									<div class="controls">
										<s:select list="listaContoTesoreria" name="contoTesoreria.uid" id="contoTesoreriaSubdocumento" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span9" />	
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="causaleProvvisorioDiCassa">Descrizione causale</label>
									<div class="controls">
										<s:textfield id="causaleProvvisorioDiCassa" name="provvisorioDiCassa.causale" cssClass="span9" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="subCausaleProvvisorioDiCassa">Sotto causale</label>
									<div class="controls">
										<s:textfield id="subCausaleProvvisorioDiCassa" name="provvisorioDiCassa.subCausale" cssClass="span9" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="denominazioneSoggettoProvvisorioDiCassa">Descrizione soggetto</label>
									<div class="controls">
										<s:textfield id="denominazioneSoggettoProvvisorioDiCassa" name="provvisorioDiCassa.denominazioneSoggetto" cssClass="span9" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Data emissione</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline" for="dataEmissioneDa">Inizio</label>
										</span>
										<s:textfield id="dataEmissioneDa" name="dataEmissioneDa" cssClass="lbTextSmall span2 datepicker"></s:textfield>
										<span class="al">
											<label class="radio inline" for="dataEmissioneA">Fine</label>
										</span>
										<s:textfield id="dataEmissioneA" name="dataEmissioneA" cssClass="lbTextSmall span2 datepicker"></s:textfield>
									</div>
								</div>
								
								
								
								<div class="control-group">
									<label class="control-label" for="DescSogg">Data trasmissione</label>
									<div class="controls">
										<span class="al">
												<label class="radio inline" for="dataInizioTrasmissione">Inizio</label>
										</span>								
										<s:textfield id="dataInizioTrasmissione" name="dataInizioTrasmissione"       cssClass="lbTextSmall span2 datepicker"></s:textfield>
										<span class="al">
												<label class="radio inline" for="dataFineTrasmissione">Fine</label>
										</span>
										<s:textfield id="dataFineTrasmissione" name="dataFineTrasmissione"       cssClass="lbTextSmall span2 datepicker"></s:textfield>
									</div>
								</div>
								
								
								<div class="control-group">
									<label class="control-label">Struttura Amministrativa</label>
									<div class="controls">
										<div class="accordion span9 struttAmm">
											<div class="accordion-group">
												<div class="accordion-heading">
													<a class="accordion-toggle" href="#struttAmm" data-toggle="collapse">
														<span id="SPAN_StrutturaAmministrativoContabile">Seleziona la Struttura amministrativa</span>
													</a>
												</div>
												<div id="struttAmm" class="accordion-body collapse">
													<div class="accordion-inner">
														<ul id="treeStruttAmm" class="ztree treeStruttAmm"></ul>
														<br/>
														<%--
														<button type="button" class="btn btn-primary pull-right" data-toggle="collapse" data-target="#struttAmm" aria-hidden="true">Conferma</button>
														<!--<button type="button" id="BUTTON_deselezionaStrutturaAmministrativoContabile" class="btn btn-secondary">deseleziona</button> -->
 														--%>
													</div>
												</div>
											</div>
										</div>
																
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="strutturaAmministrativoContabile.uid" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="strutturaAmministrativoContabile.codice" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="strutturaAmministrativoContabile.descrizione" />
									</div>
								</div>
								<%-- 
								<div class="control-group">      
						          <label class="control-label">Struttura Amministrativa</label>
						          <div class="controls">   
						            <s:hidden name="strutturaSelezionataSuPagina" id="strutturaSelezionataSuPagina" />
						                           
						            <div class="accordion span9" class="struttAmmRicercaProvvisorio">
						              <div class="accordion-group">
						                <div class="accordion-heading">    
						                  <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmmRicercaProvvisorio" href="#4b">
						                 Seleziona la Struttura amministrativa
						                  <i class="icon-spin icon-refresh spinner" id="spinnerStruttAmmRicercaProvvisorio"></i></a>
						                </div>
						                <div id="4b" class="accordion-body collapse">
						                  <div class="accordion-inner" id="strutturaAmministrativaRicercaProvvisorioDiv">
						                    <ul id="strutturaAmministrativaRicercaProvvisorio" class="ztree treeStruttAmm"></ul>
						                  </div>
						                  <div class="accordion-inner" id="strutturaAmministrativaRicercaProvvisorioWait">
						                    Attendere prego..
						                  </div>
						                  
						                </div>
						              </div>
						            </div>
						          </div>
						        </div>
				        --%>
				        
				        
				        
								
								<div class="control-group">
									<label class="control-label">Importo</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline" for="importoDa">Da</label>
										</span>
										<s:textfield id="importoDa" name="importoDa" cssClass="lbTextSmall span2 soloNumeri decimale" />
										<span class="al">
											<label class="radio inline" for="importoA">A</label>
										</span>
										<s:textfield id="importoA" name="importoA" cssClass="lbTextSmall span2 soloNumeri decimale"/>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="daAnnullare">Annullato</label>
									<div class="controls">
										<select id="daAnnullare" name="daAnnullare" class="span5">
											<option value="" <s:if test='%{"" eq daAnnullare}'>selected</s:if>>Non applicabile</option>
											<option value="S" <s:if test='%{"S" eq daAnnullare}'>selected</s:if>>S&iacute;</option>
											<option value="N" <s:if test='%{"N" eq daAnnullare}'>selected</s:if>>No</option>
										</select>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="daRegolarizzare">Da regolarizzare</label>
									<div class="controls">
										<select id="daRegolarizzare" name="daRegolarizzare" class="span5">
											<option value="" <s:if test='%{"" eq daRegolarizzare}'>selected</s:if>>Non applicabile</option>
											<option value="S" <s:if test='%{"S" eq daRegolarizzare}'>selected</s:if>>S&iacute;</option>
											<option value="N" <s:if test='%{"N" eq daRegolarizzare}'>selected</s:if>>No</option>
										</select>
									</div>
								</div>
							</fieldset>
						</div>
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn reset">annulla</button>
							<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
						</p>
					</div>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	
	<script type="text/javascript" src="${jspath}ztree/ztree_new.js"></script>
	<script type="text/javascript" src="${jspath}provvisorioDiCassa/ricercaSinteticaProvvisorioCassa.js"></script>

</body>
</html>