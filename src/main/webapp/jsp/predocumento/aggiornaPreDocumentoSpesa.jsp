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
	<s:hidden id="originalCausale" name="causaleSpesa.uid" data-maintain="" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="aggiornamentoPreDocumentoSpesa" id="formAggiornamentoPreDocumentoSpesa" novalidate="novalidate" >
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3><s:property value="titoloPredisposizione" escapeHtml="false" /></h3>
					<s:hidden id="HIDDEN_uidPredocumento" name="preDocumento.uid" />
					<s:hidden id="HIDDEN_flagManualePredocumento" name="preDocumento.flagManuale" />
					<s:hidden id="HIDDEN_numeroPredocumento" name="preDocumento.numero" />
					<s:hidden id="HIDDEN_uidPredocumentoDaAggiornare" name="uidPredocumentoDaAggiornare" />
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<h4>Dati Predisposizione</h4>
							<fieldset class="form-horizontal margin-large">
								<div class="control-group">
									<label for="dataCompetenzaPreDocumento" class="control-label">Competenza *</label>
									<div class="controls">
										<s:textfield id="dataCompetenzaPreDocumento" name="preDocumento.dataCompetenza" required="true" placeholder="%{'data'}" cssClass="span2 datepicker" />
										&nbsp;
										<s:textfield id="periodoCompetenzaPreDocumento" name="preDocumento.periodoCompetenza" required="true" placeholder="%{'periodo'}" cssClass="span2" maxlength="500" />
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
									<label for="tipoCausale" class="control-label">Tipo causale *</label>
									<div class="controls">
										<s:select list="listaTipoCausale" name="tipoCausale.uid" cssClass="span9" required="true" headerKey="0" headerValue=""
											listKey="uid" listValue="%{codice + '-' + descrizione}" id="tipoCausale" />
									</div>
								</div>
								
								<div class="control-group">
									<label for="causaleSpesa" class="control-label">Causale *</label>
									<div class="controls">
										<select name="causaleSpesa.uid" class="span9" required id="causaleSpesa" data-overlay
												<s:if test="%{tipoCausale == null || tipoCausale.uid == 0}">disabled</s:if>>
											<option value="0"></option>
											<s:iterator value="listaCausaleSpesa" var="caus">
												<option value="<s:property value="%{#caus.uid}" />"
													data-numero-capitolo="<s:property value="%{#caus.capitoloUscitaGestione.numeroCapitolo}" />"
													data-numero-articolo="<s:property value="%{#caus.capitoloUscitaGestione.numeroArticolo}" />"
													data-numero-Ueb="<s:property value="%{#caus.capitoloUscitaGestione.numeroUEB}" />"
													data-anno-movimento="<s:property value="%{#caus.impegno.annoMovimento}" />"
													data-numero-movimento="<s:property value="%{#caus.impegno.numero}" />"
													data-numero-sub-movimento="<s:property value="%{#caus.subImpegno.numero}" />"
													data-codice-soggetto="<s:property value="%{#caus.soggetto.codiceSoggetto}" />"
													data-anno-atto-amministrativo="<s:property value="%{#caus.attoAmministrativo.anno}" />"
													data-numero-atto-amministrativo="<s:property value="%{#caus.attoAmministrativo.numero}" />"
													data-tipo-atto="<s:property value="%{#caus.attoAmministrativo.tipoAtto.uid}" />"
													data-struttura-amministrativo-contabile="<s:property value="%{#caus.attoAmministrativo.strutturaAmmContabile.uid}" />"
													<s:if test="%{#caus.uid == causaleSpesa.uid}">selected</s:if>
												>
													<s:property value="%{#caus.codice + '-' + #caus.descrizione}" />
												</option>
											</s:iterator>
										</select>
									</div>
								</div>
								<div class="control-group">
									<label for="contoTesoreria" class="control-label">Conto del tesoriere *</label>
									<div class="controls">
										<s:select list="listaContoTesoreria" name="contoTesoreria.uid" cssClass="span9" required="true" headerKey="0" headerValue=""
											listKey="uid" listValue="%{codice + '-' + descrizione}" id="contoTesoreria" />
									</div>
								</div>
								<div id="accordionAnagraficaPredisposizione" class="accordion">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#collapseAnagraficaPredisposizione" data-parent="#accordionAnagraficaPredisposizione" data-toggle="collapse" class="accordion-toggle collapsed">
												Anagrafica Predisposizione<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse" id="collapseAnagraficaPredisposizione">
											<div class="accordion-inner">
												
												<div class="control-group">
													<label for="ragioneSocialeDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">Ragione Sociale</label>
													<div class="controls">
														<s:textfield id="ragioneSocialeDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.ragioneSociale" cssClass="span9" placeholder="%{'ragione sociale'}" maxlength="500" />
													</div>
												</div>
												<div class="control-group">
													<label for="cognomeDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">Cognome e Nome</label>
													<div class="controls">
														<s:textfield id="cognomeDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.cognome" cssClass="span3" placeholder="%{'cognome'}" maxlength="500" />
														<span class="al">
															<s:textfield id="nomeDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.nome" cssClass="span4" placeholder="%{'nome'}" maxlength="500" />
														</span>
														<span class="alLeft">
															<s:iterator value="listaSesso" var="sesso">
																<label class="radio inline">
																	<input type="radio" value="<s:property value='%{#sesso.name()}'/>" name="datiAnagraficiPreDocumento.sesso" <s:if test='%{#sesso.name().equals(datiAnagraficiPreDocumento.sesso)}'>checked</s:if>> <s:property value='%{#sesso.name()}'/>
																</label>
															</s:iterator>
														</span>
													</div>
												</div>
												<div class="control-group">
													<label for="codiceFiscaleDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">Codice fiscale</label>
													<div class="controls">
														<s:textfield id="codiceFiscaleDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.codiceFiscale" cssClass="span3 uppercase" placeholder="%{'codice fiscale'}" maxlength="16" />
														<span class="alRight">
															<label for="partitaIvaDatiAnagraficiPreDocumentoSpesaPreDocumento" class="radio inline">Partita IVA</label>
														</span>
														<s:textfield id="partitaIvaDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.partitaIva" cssClass="span3" placeholder="%{'partita IVA'}" maxlength="200" />
													</div>
												</div>
												
												<h5 class="subTitle">Dati Indirizzo</h5>
												<div class="control-group">
													<label for="nazioneIndirizzoDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">Stato\Comune</label>
													<div class="controls">
														<s:select list="listaNazioni" id="nazioneIndirizzoDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.nazioneIndirizzo" cssClass="span3"
															headerKey="" headerValue="" listKey="descrizione" listValue="descrizione" />
														<span class="al"></span>
														<span>
															<s:textfield id="comuneIndirizzoDatiAnagraficiPreDocumento" name="datiAnagraficiPreDocumento.comuneIndirizzo" cssClass="lbTextSmall span3" maxlength="500" placeholder="%{'comune'}" data-typeahead="" />
															<span class="input-append" data-provincia>
																<span class="add-on"></span>
															</span>
															<span class="input-append" data-has-spinner>
																<span class="add-on">
																	<i class="icon-spin icon-refresh spinner"></i>&nbsp;
																</span>
															</span>
														</span>
													</div>
												</div>
												<div class="control-group">
													<label for="indirizzoDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">Indirizzo</label>
													<div class="controls">
														<s:textfield id="indirizzoDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.indirizzo" cssClass="lbTextSmall span9" maxlength="500" placeholder="%{'nome via - n. civico - C.A.P.'}" />
													</div>
												</div>
												
												<h5 class="subTitle">Dati aggiuntivi</h5>
												<div class="control-group">
													<label for="dataNascitaDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">Data di Nascita</label>
													<div class="controls">
														<s:textfield id="dataNascitaDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.dataNascita" cssClass="span2 datepicker" placeholder="%{'data'}" />
													</div>
												</div>
												<div class="control-group">
													<label for="nazioneNascitaDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">Stato\Comune</label>
													<div class="controls">
														<s:select list="listaNazioni" id="nazioneNascitaDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.nazioneNascita" cssClass="span3"
															headerKey="" headerValue="" listKey="descrizione" listValue="descrizione" />
														<span class="al"></span>
														<span>
															<s:textfield id="comuneNascitaDatiAnagraficiPreDocumento" name="datiAnagraficiPreDocumento.comuneNascita" cssClass="lbTextSmall span3" maxlength="500" placeholder="%{'comune'}" data-typeahead="" />
															<span class="input-append" data-provincia>
																<span class="add-on"></span>
															</span>
															<span class="input-append" data-has-spinner>
																<span class="add-on">
																	<i class="icon-spin icon-refresh spinner"></i>&nbsp;
																</span>
															</span>
														</span>
													</div>
												</div>
												<div class="control-group">
													<label for="indirizzoEmailDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">Email</label>
													<div class="controls">
														<s:textfield id="indirizzoEmailDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.indirizzoEmail" cssClass="span5" maxlength="500" placeholder="%{'email'}" />
														<span class="al">
															<label for="numTelefonoDatiAnagraficiPreDocumentoSpesaPreDocumento" class="radio inline">Telefono</label>
														</span>
														<s:textfield id="numTelefonoDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.numTelefono" cssClass="span3" maxlength="16" placeholder="%{'telefono'}" />
													</div>
												</div>
												
												<div id="accordionModalitaPagamento" class="accordion">
													<div class="accordion-group">
														<div class="accordion-heading">
															<a href="#collapseModalitaPagamento" data-parent="#accordionModalitaPagamento" data-toggle="collapse" class="accordion-toggle collapsed">
																Modalit&agrave; pagamento<span class="icon">&nbsp;</span>
															</a>
														</div>
														<div class="accordion-body collapse" id="collapseModalitaPagamento">
															<div class="accordion-inner">
																
																<div class="control-group">
																	<label for="intestazioneContoDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">Intestazione conto</label>
																	<div class="controls">
																		<s:textfield id="intestazioneContoDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.intestazioneConto" cssClass="span9" maxlength="500" placeholder="%{'conto'}" />
																	</div>
																</div>
																<div class="control-group">
																	<label for="codiceABIDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">ABI</label>
																	<div class="controls">
																		<s:textfield id="codiceABIDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.codiceABI" cssClass="span3" maxlength="500" placeholder="%{'ABI'}" />
																		<span class="al">
																			<label for="codiceCABDatiAnagraficiPreDocumentoSpesaPreDocumento" class="radio inline">CAB</label>
																		</span>
																		<s:textfield id="codiceCABDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.codiceCAB" cssClass="span3" maxlength="500" placeholder="%{'CAB'}" />
																	</div>
																</div>
																<div class="control-group">
																	<label for="codiceIbanDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">IBAN</label>
																	<div class="controls">
																		<s:textfield id="codiceIbanDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.codiceIban" cssClass="span4" maxlength="500" placeholder="%{'IBAN'}" />
																	</div>
																</div>
																<div class="control-group">
																	<label for="codiceBicDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">BIC</label>
																	<div class="controls">
																		<s:textfield id="codiceBicDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.codiceBic" cssClass="span4" maxlength="500" placeholder="%{'BIC'}" />
																	</div>
																</div>
																<div class="control-group">
																	<label for="contoCorrenteDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">Conto corrente</label>
																	<div class="controls">
																		<s:textfield id="contoCorrenteDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.contoCorrente" cssClass="span4" maxlength="500" placeholder="%{'conto corrente'}" />
																	</div>
																</div>
																<div class="control-group">
																	<label for="soggettoQuietanzanteDatiAnagraficiPreDocumentoSpesaPreDocumento" class="control-label">Quietanzante</label>
																	<div class="controls">
																		<s:textfield id="soggettoQuietanzanteDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.soggettoQuietanzante" cssClass="span5" maxlength="500" placeholder="%{'denominazione'}" />
																		<s:textfield id="codiceFiscaleQuietanzanteDatiAnagraficiPreDocumentoSpesaPreDocumento" name="datiAnagraficiPreDocumento.codiceFiscaleQuietanzante" cssClass="span4 uppercase" maxlength="16" placeholder="%{'codice fiscale'}" />
																	</div>
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								
								<h4>Estremi pagamento</h4>
								<div class="control-group">
									<label class="control-label">Provvisorio di Cassa</label>
									<div class="controls">
										<span class="alRight">
											<label for="annoProvvisorioCassa" class="radio inline">Anno</label>
										</span>
										<s:textfield id="annoProvvisorioCassa" name="provvisorioCassa.anno" cssClass="lbTextSmall span2" placeholder="%{'anno'}" />
										<span class="alRight">
											<label for="numeroProvvisorioCassa" class="radio inline">Numero</label>
										</span>
										<s:textfield id="numeroProvvisorioCassa" name="provvisorioCassa.numero" cssClass="lbTextSmall span2" placeholder="%{'numero'}" maxlength="8" />
										&nbsp;
										<span id="causaleProvvisorioCassa"></span>
										<span class="radio guidata">
											<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataProvvisorioCassa">compilazione guidata</button>
										</span>
									</div>
								</div>
								<div class="control-group">
									<label for="dataDocumentoPreDocumento" class="control-label">Data esecuzione *</label>
									<div class="controls">
										<s:textfield id="dataDocumentoPreDocumento" name="preDocumento.dataDocumento" cssClass="span2 datepicker" placeholder="%{'data'}" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label for="importoPreDocumento" class="control-label">Importo *</label>
									<div class="controls">
										<s:textfield id="importoPreDocumento" name="preDocumento.importo" cssClass="span2 soloNumeri decimale" placeholder="%{'importo'}" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label for="descrizionePreDocumento" class="control-label">Descrizione</label>
									<div class="controls">
										<s:textarea id="descrizionePreDocumento" name="preDocumento.descrizione" cssClass="span9" rows="1" cols="15"></s:textarea>
									</div>
								</div>
								<div class="control-group">
									<label for="notePreDocumento" class="control-label">Note</label>
									<div class="controls">
										<s:textarea id="notePreDocumento" name="preDocumento.note" cssClass="span9" rows="3" cols="15"></s:textarea>
									</div>
								</div>
								
								<div id="accordionImputazioniContabili" class="accordion">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#collapseImputazioniContabili" data-parent="#accordionImputazioniContabili" data-toggle="collapse" class="accordion-toggle collapsed">
												Imputazioni contabili<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse" id="collapseImputazioniContabili">
											<div class="accordion-inner">
												<s:include value="/jsp/predocumento/imputazioniContabiliPreDocumentoSpesa.jsp" />
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
						<s:submit cssClass="btn btn-primary pull-right" value="aggiorna"/>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/capUscitaGestione/selezionaCapitolo_modale.jsp" />
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" />
	<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale_new.jsp" />
	<s:include value="/jsp/provvisorioCassa/modaleRicercaProvvisorioCassa.jsp" />
	<%-- MODALE USCITA GESTIONE --%>	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/external/upbootstrap/bootstrap-typeahead.fork.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/codiceFiscale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/ricercaCapitoloModale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
	
	<script type="text/javascript" src="/siacbilapp/js/local/provvisorioDiCassa/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/ztree/ztree_new.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale_new.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/predocumento/predocumento.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/predocumento/aggiornaSpesa.js"></script>

</body>
</html>