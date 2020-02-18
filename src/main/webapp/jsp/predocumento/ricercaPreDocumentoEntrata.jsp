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
				<s:form cssClass="form-horizontal" action="effettuaRicercaPreDocumentoEntrata" id="formRicercaPreDocumentoEntrata" novalidate="novalidate">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Ricerca Predisposizione di Incasso</h3>
					<p>&Eacute; necessario inserire almeno un criterio di ricerca</p>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<br>
							<p>
								<span class="pull-right">
									<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
								</span>
							</p>
							<br>
							<h4>Predisposizione</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label for="numeroPredocumento" class="control-label">Numero</label>
									<div class="controls">
										<s:textfield id="numeroPredocumento" name="preDocumento.numero" placeholder="%{'numero'}" cssClass="span2" maxlength="500" />
									</div>
								</div>
								<div class="control-group">
									<label for="periodoCompetenzaPreDocumento" class="control-label">Competenza</label>
									<div class="controls">
										<s:textfield id="periodoCompetenzaPreDocumento" name="preDocumento.periodoCompetenza" placeholder="%{'periodo'}" cssClass="span2" maxlength="500" />
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
														<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
<!-- 														<button type="button" id="BUTTON_deselezionaStrutturaAmministrativoContabile" class="btn btn-secondary">deseleziona</button> -->
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
										<label class="radio inline">
											<div class="checkbox">
												<s:checkbox name="flagCausaleSpesaMancante" id="flagCausaleSpesaMancante" />&nbsp;Mancante
											</div>
										</label>
									</div>
								</div>
								<div class="control-group">
									<label for="contoCorrente" class="control-label">Conto corrente</label>
									<div class="controls">
										<s:select list="listaContoCorrente" name="contoCorrente.uid" cssClass="span9" headerKey="0" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" id="contoCorrente" />
										<label class="radio inline">
											<div class="checkbox">
												<s:checkbox name="flagContoCorrenteMancante" id="flagContoCorrenteMancante" />&nbsp;Mancante
											</div>
										</label>
									</div>
								</div>
								<div class="control-group">
									<label for="dataTrasmissioneDa" class="control-label">Data trasmissione da</label>
									<div class="controls">
										<s:textfield id="dataTrasmissioneDa" name="dataTrasmissioneDa" placeholder="%{'Da'}" cssClass="span2 datepicker" />
										<span class="al"><label for="dataTrasmissioneA" class="radio inline">a</label></span>
										<s:textfield id="dataTrasmissioneA" name="dataTrasmissioneA" placeholder="%{'A'}" cssClass="span2 datepicker" />
									</div>
								</div>
								
								<div class="control-group">
									<label for="statoOperativoPreDocumento" class="control-label">Stato operativo</label>
									<div class="controls">
										<s:select list="listaStatoOperativoPreDocumento" name="statoOperativoPreDocumento" cssClass="span4" headerKey="" headerValue=""
											listValue="%{descrizione}" id="statoOperativoPreDocumento" />
										<label class="radio inline">
											<div class="checkbox">
												<s:checkbox name="flagNonAnnullati" id="flagNonAnnullati" />&nbsp;Non annullati
											</div>
										</label>
									</div>
								</div>
								<div class="control-group">
									<label for="importoPreDocumento" class="control-label">Importo</label>
									<div class="controls">
										<s:textfield id="importoPreDocumento" name="preDocumento.importo" cssClass="span2 soloNumeri decimale" placeholder="%{'importo'}" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label for="ordinamentoPreDocumento" class="control-label">Ordinamento</label>
									<div class="controls">
										<s:select list="listaOrdinamentoPreDocumentoEntrata" name="ordinamentoPreDocumentoEntrata" cssClass="span4"
											listValue="descrizione" id="ordinamentoPreDocumento" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Elenco</label>
									<div class="controls">
										<span class="al">
											<label for="annoElencoDocumentiAllegato" class="radio inline">Anno</label>
										</span>
										<s:textfield id="annoElencoDocumentiAllegato" name="preDocumento.elencoDocumentiAllegato.anno" cssClass="lbTextSmall soloNumeri span2" maxlength="4" placeholder="%{'anno'}" />
										<span class="al">
											<label for="numeroElencoDocumentiAllegato" class="radio inline">Numero</label>
										</span>
										<s:textfield id="numeroElencoDocumentiAllegato" name="preDocumento.elencoDocumentiAllegato.numero" cssClass="lbTextSmall soloNumeri span4" maxlength="7" placeholder="%{'numero'}" />
										<%--
										<span class="radio guidata">
											<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataElenco">compilazione guidata</button>
										</span>
										--%>
									</div>
								</div>
								
								<h4>Anagrafica Predisposizione di Incasso</h4>
								<div class="control-group">
									<label for="ragioneSocialeDatiAnagraficiPreDocumento" class="control-label">Ragione Sociale</label>
									<div class="controls">
										<s:textfield id="ragioneSocialeDatiAnagraficiPreDocumento" name="datiAnagraficiPreDocumento.ragioneSociale" cssClass="span7" placeholder="%{'ragione sociale'}" maxlength="500" />
									</div>
								</div>
								<div class="control-group">
									<label for="cognomeDatiAnagraficiPreDocumento" class="control-label">Cognome</label>
									<div class="controls">
										<s:textfield id="cognomeDatiAnagraficiPreDocumento" name="datiAnagraficiPreDocumento.cognome" cssClass="span3" placeholder="%{'cognome'}" maxlength="500" />
										<span class="alRight">
											<label for="nomeDatiAnagraficiPreDocumento" class="radio inline">Nome</label>
										</span>
										<span class="alLeft">
											<s:textfield id="nomeDatiAnagraficiPreDocumento" name="datiAnagraficiPreDocumento.nome" cssClass="span3" placeholder="%{'nome'}" maxlength="500" />
										</span>
									</div>
								</div>
								<div class="control-group">
									<label for="codiceFiscaleDatiAnagraficiPreDocumento" class="control-label">Codice fiscale</label>
									<div class="controls">
										<s:textfield id="codiceFiscaleDatiAnagraficiPreDocumento" name="datiAnagraficiPreDocumento.codiceFiscale" cssClass="span3 uppercase" placeholder="%{'codice fiscale'}" maxlength="16" />
										<span class="alRight">
											<label for="partitaIvaDatiAnagraficiPreDocumento" class="radio inline">Partita IVA</label>
										</span>
										<s:textfield id="partitaIvaDatiAnagraficiPreDocumento" name="datiAnagraficiPreDocumento.partitaIva" cssClass="span3" placeholder="%{'partita IVA'}" maxlength="200" />
									</div>
								</div>
							</fieldset>

							<div id="accordionImputazioniContabili" class="accordion">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a href="#collapseImputazioniContabili" data-parent="#accordionImputazioniContabili" data-toggle="collapse" class="accordion-toggle collapsed">
											Imputazioni contabili<span class="icon">&nbsp;</span>
										</a>
									</div>
									<div class="accordion-body collapse" id="collapseImputazioniContabili">
										<div class="accordion-inner">
											<h4 class="step-pane">Capitolo
												<span class="datiRIFCapitolo" id="datiRiferimentoCapitoloSpan">
													<s:if test="%{capitolo != null && capitolo.annoCapitolo != null && capitolo.numeroCapitolo != null && capitolo.numeroArticolo != null && (!gestioneUEB || capitolo.numeroUEB != null)}">
														: <s:property value="capitolo.annoCapitolo" /> / <s:property value="capitolo.numeroCapitolo" /> / <s:property value="capitolo.numeroArticolo" />
														<s:if test="%{gestioneUEB}">
															/ <s:property value="capitolo.numeroUEB" />
														</s:if>
													</s:if>
												</span>
											</h4>
											<fieldset class="form-horizontal">
												<div class="control-group">
													<label for="annoCapitolo" class="control-label">Anno</label>
													<div class="controls">
														<s:textfield id="annoCapitolo" name="capitolo.annoCapitolo" value="%{annoEsercizioInt}" cssClass="lbTextSmall" disabled="true" />
														<s:hidden id="annoCapitoloHidden" name="capitolo.annoCapitolo" value="%{annoEsercizioInt}" />
														<span class="al">
															<label for="numeroCapitolo" class="radio inline">Capitolo</label>
														</span>
														<s:textfield id="numeroCapitolo" name="capitolo.numeroCapitolo" cssClass="lbTextSmall soloNumeri span2" maxlength="30" placeholder="%{'capitolo'}" />
														<span class="al">
															<label for="numeroArticolo" class="radio inline">Articolo</label>
														</span>
														<s:textfield id="numeroArticolo" name="capitolo.numeroArticolo" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'articolo'}" />
														<s:if test="%{gestioneUEB}">
															<span class="al">
																<label for="numeroUEB" class="radio inline">UEB</label>
															</span>
															<s:textfield id="numeroUEB" name="capitolo.numeroUEB" cssClass="lbTextSmall soloNumeri span2" placeholder="%{'UEB'}" />
														</s:if><s:else>
															<s:hidden id="numeroUEB" name="capitolo.numeroUEB" value="1" />
														</s:else>
														<span class="radio guidata">
															<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataCapitolo">compilazione guidata</button>
														</span>
													</div>
												</div>
											</fieldset>
											
											<h4 class="step-pane">Accertamento
												<span class="datiRIFImpegno" id="datiRiferimentoImpegnoSpan">
													<s:if test="%{movimentoGestione != null && movimentoGestione.annoMovimento != 0 && movimentoGestione.numero != null}">
														: <s:property value="movimentoGestione.annoMovimento"/> / <s:property value="movimentoGestione.numero.toString()"/>
														<s:if test="%{subMovimentoGestione.numero != null}">
															- <s:property value="subMovimentoGestione.numero.toString()"/>
														</s:if>
													</s:if>
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
													<s:if test="%{soggetto != null && soggetto.codiceSoggetto != null && soggetto.denominazione != null && soggetto.codiceFiscale != null}">
														<s:property value="soggetto.codiceSoggetto" /> - <s:property value="soggetto.denominazione" /> - <s:property value="soggetto.codiceFiscale}" />
													</s:if>
												</span>
											</h4>
											<fieldset class="form-horizontal">
												<div class="control-group">
													<label for="flagSoggettoMancante" class="control-label">Mancante</label>
													<div class="controls">
														<s:checkbox id="flagSoggettoMancante" name="flagSoggettoMancante" />
													</div>
												</div>
												<div id="div_flagSoggettoMancante">
													<div class="control-group">
														<label for="codiceSoggettoSoggetto" class="control-label">Codice</label>
														<div class="controls">
															<s:textfield id="codiceSoggettoSoggetto" name="soggetto.codiceSoggetto" cssClass="span2" placeholder="%{'codice'}" />
															<span class="radio guidata">
																<button type="button" class="btn btn-primary" id="pulsanteCompilazioneSoggetto">compilazione guidata</button>
															</span>
															<s:hidden id="HIDDEN_denominazioneSoggetto" name="soggetto.denominazione" />
															<s:hidden id="HIDDEN_codiceFiscaleSoggetto" name="soggetto.codiceFiscale" />
														</div>
													</div>
												</div>
											</fieldset>
											
											<h4 class="step-pane">Provvedimento 
												<span class="datiRIFProvvedimento" id="SPAN_InformazioniProvvedimentoAttoAmministrativo">
													<s:if test="%{attoAmministrativo != null && attoAmministrativo.anno != 0 && attoAmministrativo.numero != 0 
															&& tipoAtto != null && tipoAtto.uid != 0 && tipoAtto.codice != null 
															&& strutturaAmministrativoContabileAttoAmministrativo != null 
															&& strutturaAmministrativoContabileAttoAmministrativo.uid != 0
															&& strutturaAmministrativoContabileAttoAmministrativo.codice != 0}">
														: <s:property value="attoAmministrativo.anno" /> / <s:property value="attoAmministrativo.numero" /> / <s:property value="tipoAtto.codice" /> / <s:property value="strutturaAmministrativoContabileAttoAmministrativo.codice" />
													</s:if>
												</span>
											</h4>
											<fieldset class="form-horizontal">
												<div class="control-group">
													<label for="flagAttoAmministrativoMancante" class="control-label">Mancante</label>
													<div class="controls">
														<s:checkbox id="flagAttoAmministrativoMancante" name="flagAttoAmministrativoMancante" />
													</div>
												</div>
												<div id="div_flagAttoAmministrativoMancante">
													<div class="control-group">
														<label for="annoProvvedimentoAttoAmministrativo" class="control-label">Anno</label>
														<div class="controls">
															<s:textfield id="annoProvvedimentoAttoAmministrativo" name="attoAmministrativo.anno" cssClass="span1" placeholder="%{'anno'}" />
															<span class="al">
																<label for="numeroProvvedimentoAttoAmministrativo" class="radio inline">Numero</label>
															</span>
															<s:textfield id="numeroProvvedimentoAttoAmministrativo" name="attoAmministrativo.numero" cssClass="lbTextSmall span2" placeholder="%{'numero'}" maxlength="7" />
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
															<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodiceAttoAmministrativo" name="strutturaAmministrativoContabileAttoAmministrativo.codice" />
															<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizioneAttoAmministrativo" name="strutturaAmministrativoContabileAttoAmministrativo.descrizione" />
														</div>
													</div>
												</div>
											</fieldset>
										</div>
									</div>
								</div>
							</div>

							<div id="accordionEstremiDiPagamento" class="accordion">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a href="#collapseEstremiDiPagamento" data-parent="#accordionEstremiDiPagamento" data-toggle="collapse" class="accordion-toggle collapsed">
											Estremi di pagamento<span class="icon">&nbsp;</span>
										</a>
									</div>
									<div class="accordion-body collapse" id="collapseEstremiDiPagamento">
										<div class="accordion-inner">
											<div class="control-group">
												<label for="annoDocumento" class="control-label">Documento di entrata</label>
												<div class="controls">
													<s:textfield id="annoDocumento" name="documento.anno" cssClass="span1 soloNumeri" placeholder="%{'Anno'}" maxlength="4" />
													<s:textfield id="numeroDocumento" name="documento.numero" cssClass="span2" placeholder="%{'Numero'}" maxlength="500" />
													<span class="al">
														<label for="tipoDocumento" class="radio inline">Tipo</label>
													</span>
													<s:select list="listaTipoDocumento" cssClass="span4" id="tipoDocumento" name="tipoDocumento.uid" headerKey="0" headerValue=""
														listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
												</div>
											</div>
											<div class="control-group">
												<label for="annoOrdinativo" class="control-label">Ordinativo</label>
												<div class="controls">
													<input type="text" placeholder="anno" class="span1" name="ordinativo.anno" id="annoOrdinativo">
													<input type="text" placeholder="numero" class="span2" name="ordinativo.numero" id="numeroOrdinativo">
												</div>
											</div>
											<div class="control-group">
												<label for="flagEstraiNonPagato" class="control-label">Estrai non incassato</label>
												<div class="controls">
													<label class="radio inline">
														<div class="checkbox">
															<s:checkbox id="flagEstraiNonPagato" name="flagEstraiNonPagato" />
														</div>
													</label>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>

						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn reset">annulla</button>
						<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
					</p>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/capEntrataGestione/selezionaCapitolo_modale.jsp" />
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/movimentoGestione/modaleAccertamento.jsp" />
	<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale_new.jsp" />
	<%-- MODALE USCITA GESTIONE --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}capitolo/ricercaCapitoloModale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}movimentoGestione/ricercaAccertamentoOttimizzato.js"></script>
	<script type="text/javascript" src="${jspath}ztree/ztree_new.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale_new.js"></script>
	<script type="text/javascript" src="${jspath}predocumento/predocumento.js"></script>
	<script type="text/javascript" src="${jspath}predocumento/ricercaEntrata.js"></script>

</body>
</html>