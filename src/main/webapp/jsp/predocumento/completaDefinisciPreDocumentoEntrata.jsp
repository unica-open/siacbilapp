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
	<s:hidden id="nomeAzioneDecentrata" value="%{nomeAzioneDecentrata}" data-maintain="" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="completaDefinisciPreDocumentoEntrata_completaDefinisci" novalidate="true">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Completa e Definisci Predisposizione di Incasso</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<br />
							<p>
								<span class="pull-right">
									<button type="button" class="btn btn-primary pull-right" id="pulsanteCercaTotali">cerca</button>
								</span>
							</p>
							<h4>Predisposizione</h4>
							<fieldset class="form-horizontal">
								<fieldset id="fieldsetRicercaPredisposizione">
									<%--
									<div class="control-group">
										<label for="numeroPredocumento" class="control-label">Bilancio</label>
										<div class="controls">
											<s:textfield id="annoEsercizio" name="annoEsercizio" cssClass="span2" disabled="true" />
										</div>
									</div>
									--%>
									<div class="control-group">
										<label for="periodoCompetenzaPreDocumento" class="control-label">Competenza</label>
										<div class="controls">
											<span class="al"><label for="dataCompetenzaDa" class="radio inline">Data da</label></span>
											<s:textfield id="dataCompetenzaDa" name="dataCompetenzaDa" placeholder="%{'Da'}" cssClass="span2 datepicker" />
											<span class="al"><label for="dataCompetenzaA" class="radio inline">a</label></span>
											<s:textfield id="dataCompetenzaA" name="dataCompetenzaA" placeholder="%{'A'}" cssClass="span2 datepicker" />
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
														<button type="button" class="btn btn-primary pull-right" data-toggle="collapse" data-target="#struttAmm" aria-hidden="true">Conferma</button>
													</div>
												</div>
											</div>
										</div>
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="strutturaAmministrativoContabile.uid" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="strutturaAmministrativoContabile.codice" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="strutturaAmministrativoContabile.descrizione" />
									</div>
								</div>
									<div class="control-group">
										<label for="tipoCausale" class="control-label">Tipo causale</label>
										<div class="controls">
											<s:select list="listaTipoCausale" name="tipoCausale.uid" cssClass="span9" required="true" headerKey="0" headerValue=""
												listKey="uid" listValue="%{codice + '-' + descrizione}" id="tipoCausale" />
										</div>
									</div>
									<div class="control-group">
										<label for="causaleEntrata" class="control-label">Causale</label>
										<div class="controls">
											<s:select list="listaCausaleEntrata" name="causaleEntrata.uid" cssClass="span8" headerKey="0" headerValue="" data-overlay=""
												listKey="uid" listValue="%{codice + '-' + descrizione}" disabled="%{tipoCausale == null || tipoCausale.uid == 0}" id="causaleEntrata" />
										</div>
									</div>
								</fieldset>
								<div id="totaliElencoPredocumenti" class="hide">
									<h4 class="step-pane active">&nbsp;</h4>
									<fieldset class="form-horizontal">
										<div class="control-group">
											<label class="control-label" for="numeroPredisposizioniTotale">Numero Predisposizioni</label>
											<div class="controls">
												<input type="text" value="" class="span1 soloNumeri text-right" readonly="readonly" id="numeroPredisposizioniTotale" />
												<span class="al">
													<label class="radio inline" for="importoPredisposizioniTotale">Totale euro</label>
												</span>
												<input type="text" value="" class="span2 soloNumeri text-right" readonly="readonly" id="importoPredisposizioniTotale" />
											</div>
										</div>
										<div class="control-group">
											<label class="control-label" for="numeroPredisposizioniIncomplete">di cui incomplete</label>
											<div class="controls">
												<input type="text" value="" class="span1 soloNumeri text-right" readonly="readonly" id="numeroPredisposizioniIncomplete" />
												<span class="al">
													<label class="radio inline" for="importoPredisposizioniIncomplete">Totale euro</label>
												</span>
												<input type="text" value="" class="span2 soloNumeri text-right" readonly="readonly" id="importoPredisposizioniIncomplete" />
											</div>
										</div>
										<div class="control-group">
											<label class="control-label" for="numeroPredisposizioniComplete">di cui complete</label>
											<div class="controls">
												<input type="text" value="" class="span1 soloNumeri text-right" readonly="readonly" id="numeroPredisposizioniComplete" />
												<span class="al">
													<label class="radio inline" for="importoPredisposizioniComplete">Totale euro</label>
												</span>
												<input type="text" value="" class="span2 soloNumeri text-right" readonly="readonly" id="importoPredisposizioniComplete" />
											</div>
										</div>
										<div class="control-group">
											<label class="control-label" for="numeroPredisposizioniAnnullateDefinite">di cui annullate/definite</label>
											<div class="controls">
												<input type="text" value="" class="span1 soloNumeri text-right" readonly="readonly" id="numeroPredisposizioniAnnullateDefinite" />
												<span class="al">
													<label class="radio inline" for="importoPredisposizioniAnnullateDefinite">Totale euro</label>
												</span>
												<input type="text" value="" class="span2 soloNumeri text-right" readonly="readonly" id="importoPredisposizioniAnnullateDefinite" />
											</div>
										</div>
									</fieldset>
								</div>
								
								
								<div id="accordionImputazioniContabili" class="accordion">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#collapseImputazioniContabili" data-parent="#accordionImputazioniContabili" data-toggle="collapse" class="accordion-toggle">
												Imputazioni contabili<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse in" id="collapseImputazioniContabili">
											<div class="accordion-inner">
												<h4 class="step-pane">Provvedimento 
													<span class="datiRIFProvvedimento" id="SPAN_InformazioniProvvedimentoAttoAmministrativo">
														<s:property value="datiRiferimentoAttoAmministrativo" />
													</span>
												</h4>
												<fieldset class="form-horizontal">
													<div id="div_flagAttoAmministrativoMancante">
														<div class="control-group">
															<label for="annoProvvedimentoAttoAmministrativo" class="control-label">Anno</label>
															<div class="controls">
																<s:textfield id="annoProvvedimentoAttoAmministrativo" name="attoAmministrativo.anno" cssClass="span1" placeholder="%{'anno'}" />
																<span class="al">
																	<label for="numeroProvvedimentoAttoAmministrativo" class="radio inline">Numero</label>
																</span>
																<s:textfield id="numeroProvvedimentoAttoAmministrativo" name="attoAmministrativo.numero" cssClass="lbTextSmall span2" placeholder="%{'numero'}" />
																<span class="al">
																	<label for="tipoAttoAttoAmministrativo" class="radio inline">Tipo </label>
																</span>
																<s:select list="listaTipoAtto" id="tipoAttoAttoAmministrativo" name="tipoAtto.uid" headerKey="0" headerValue="" listKey="uid"
																	listValue="%{descrizione}" cssClass="lbTextSmall span4" />
																<s:hidden id="statoOperativoAttoAmministrativo" name="attoAmministrativo.statoOperativo" />
																<span class="radio guidata">
																	<button type="button" class="btn btn-primary" id="pulsanteCompilazioneAttoAmministrativo">compilazione guidata</button>
																</span>
															</div>
														</div>
														<div class="control-group">
															<label class="control-label">Struttura Amministrativa</label>
															<div class="controls">
																<div id="accordionStrutturaAmministrativaContabileAttoAmministrativo" class="accordion span8">
																	<div class="accordion-group">
																		<div class="accordion-heading">
																			<a href="#collapseStrutturaAmministrativaContabileAttoAmministrativo" data-parent="#accordionStrutturaAmministrativaContabileAttoAmministrativo" data-toggle="collapse" class="accordion-toggle collapsed">
																				<span id="SPAN_StrutturaAmministrativoContabileAttoAmministrativo">Seleziona la Struttura amministrativa</span>
																			</a>
																		</div>
																		<div class="accordion-body collapse" id="collapseStrutturaAmministrativaContabileAttoAmministrativo">
																			<div class="accordion-inner">
																				<ul class="ztree treeStruttAmm" id="treeStruttAmmAttoAmministrativo"></ul>
																			</div>
																		</div>
																	</div>
																</div>
																<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUidAttoAmministrativo" name="strutturaAmministrativoContabileAttoAmministrativo.uid" />
<%-- 																<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodiceAttoAmministrativo" name="strutturaAmministrativoContabileAttoAmministrativo.codice" /> --%>
<%-- 																<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizioneAttoAmministrativo" name="strutturaAmministrativoContabileAttoAmministrativo.descrizione" /> --%>
															</div>
														</div>
													</div>
												</fieldset>
												
												<h4 class="step-pane">Accertamento
													<span class="datiRIFImpegno" id="datiRiferimentoImpegnoSpan">
														<s:property value="datiRiferimentoMovimentoGestione" />
													</span>
												</h4>
												<fieldset class="form-horizontal">
													<div class="control-group">
														<label for="annoMovimentoMovimentoGestione" class="control-label">Anno</label>
														<div class="controls">
															<s:textfield id="annoMovimentoMovimentoGestione" name="movimentoGestione.annoMovimento" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'anno'}" />
															<span class="alRight">
																<label for="numeroMovimentoGestione" class="radio inline">Numero</label>
															</span>
															<s:textfield id="numeroMovimentoGestione" name="movimentoGestione.numero" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'numero'}" value="%{movimentoGestione.numero.toString()}" />
															<span class="alRight">
																<label for="numeroSubMovimentoGestione" class="radio inline">Subaccertamento</label>
															</span>
															<s:textfield id="numeroSubMovimentoGestione" name="subMovimentoGestione.numero" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'numero subimpegno'}" value="%{subMovimentoGestione.numero.toString()}" />
															<span class="radio guidata">
																<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataMovimentoGestione">compilazione guidata</button>
															</span>
														</div>
													</div>
												</fieldset>
												
												<h4 class="step-pane">Soggetto
													<span class="datiRIFSoggetto" id="datiRiferimentoSoggettoSpan">
														<s:property value="datiRiferimentoSoggetto" />
													</span>
												</h4>
												<fieldset class="form-horizontal">
													<div id="div_flagSoggettoMancante">
														<div class="control-group">
															<label for="codiceSoggettoSoggetto" class="control-label">Codice</label>
															<div class="controls">
																<s:textfield id="codiceSoggettoSoggetto" name="soggetto.codiceSoggetto" cssClass="span2" placeholder="%{'codice'}" />
																<span class="radio guidata">
																	<button type="button" class="btn btn-primary" id="pulsanteCompilazioneSoggetto">compilazione guidata</button>
																</span>
															</div>
														</div>
													</div>
												</fieldset>
											</div>
										</div>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn reset">annulla</button>
						<s:submit cssClass="btn btn-primary pull-right" value="completa e definisci"/>
					</p>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/movimentoGestione/modaleAccertamento.jsp" />
	<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale_new.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.new2.js"></script>
	<script type="text/javascript" src="${jspath}movimentoGestione/ricercaAccertamentoOttimizzato.js"></script>
	<script type="text/javascript" src="${jspath}ztree/ztree_new.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale_new.js"></script>
	<script type="text/javascript" src="${jspath}predocumento/predocumento.js"></script>
	<script type="text/javascript" src="${jspath}predocumento/completaDefinisciEntrata.js"></script>

</body>
</html>