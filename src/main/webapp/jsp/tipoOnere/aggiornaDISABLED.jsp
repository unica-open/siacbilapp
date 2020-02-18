<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />

</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="aggiornaTipoOnere_redirectAggiornamento" novalidate="novalidate" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Aggiorna onere <s:property value="%{descrizioneCompletaTipoOnere}" /></h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal margin-medium">
								
								<s:if test="%{naturaOnereSplitReverse}">
									<div class='control-group' id="tipoIvaSplitReverseTipoOnereDiv">
										<label class="control-label" for="tipoIvaSplitReverseTipoOnere">Split / Reverse *</label>
										<div class="controls">
											<select class="span6" name="tipoOnere.tipoIvaSplitReverse" required id="tipoIvaSplitReverseTipoOnere" disabled="disabled">
												<option></option>
												<s:iterator value="listaTipoIvaSplitReverse" var="tisr">
													<option value="<s:property value="#tisr.name()"/>" <s:if test="%{tipoOnere.tipoIvaSplitReverse == #tisr}">selected</s:if>
															<s:if test='%{codiceEsenzione.equals(#tisr.codice)}'>data-esente="true" class="hide"</s:if> data-codice="<s:property value="#tisr.codice"/>">
														<s:property value="#tisr.codice"/> - <s:property value="#tisr.descrizione" />
													</option>
												</s:iterator>
											</select>
										</div>
									</div>
								</s:if>
								<s:if test="%{!naturaOnereEsenzione}">
									<div class="control-group">
										<label class="control-label" for="aliquotaCaricoSoggetto" data-hidden-split-reverse="<s:property value="codiceReverseChange"/>">Aliquota a carico soggetto </label>
										<div class="controls">
											<s:textfield id="aliquotaCaricoSoggetto" name="tipoOnere.aliquotaCaricoSoggetto" cssClass="lbTextSmall span2 soloNumeri decimale" disabled="true" />
											<s:if test="%{!naturaOnereSplitReverse}">
												<span class="alRight">
													<label for="aliquotaCaricoEnte" class="radio inline">Aliquota a carico Ente </label>
												</span>
												<s:textfield id="aliquotaCaricoEnte" name="tipoOnere.aliquotaCaricoEnte" cssClass="lbTextSmall span2 soloNumeri decimale" disabled="true" />
											</s:if>
										</div>
									</div>
								</s:if>
								<s:if test="%{!naturaOnereSplitReverse && !naturaOnereEsenzione}">
									<div class="control-group">
										<label class="control-label" for="causale770">Causale 770</label>
										<div class="controls">
											<s:select list="listaCausale770" multiple="true" id="causale770" cssClass="span10 chosen-select"
												name="causali770.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
												data-placeholder="Seleziona la causale" value="%{causali770.{uid}}" disabled="true" />
										</div>
									</div>
									<div class="control-group" data-hidden-natura="<s:property value="codiceSplitReverse"/> <s:property value="codiceEsenzione"/>">
										<label class="control-label" for="sommaNonSoggetta">Codici somma non soggetta</label>
										<div class="controls">
											<s:select list="listaSommeNonSoggette" multiple="true" id="sommaNonSoggetta" cssClass="span10 chosen-select"
	 											name="sommeNonSoggette.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}" 
												data-placeholder="Seleziona il tipo di somma non soggetta" value="%{sommeNonSoggette.{uid}}" disabled="true"/>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="attivitaOnere">Attivit&agrave; onere</label>
										<div class="controls">
											<s:select list="listaAttivitaOnere" multiple="true" id="attivitaOnere" cssClass="span10 chosen-select"
												name="attivitaOnere.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
												data-placeholder="Seleziona l'attività" value="%{attivitaOnere.{uid}}" disabled="true" />
										</div>
									</div>
									<div class="control-group">
										<label for="quadro770" class="control-label">Quadro 770</label>
										<div class="controls">
											<s:textfield id="quadro770" name="tipoOnere.quadro770" cssClass="lbTextSmall span2" disabled="true" />
										</div>
									</div>
								</s:if>
								<div class="control-group">
									<label for="dataFineValidita" class="control-label">Data annullamento</label>
									<div class="controls">
										<s:textfield id="dataFineValidita" name="tipoOnere.dataFineValidita" cssClass="lbTextSmall span2 datepicker" disabled="true" />
									</div>
								</div>
								<div id="accordionImputazioniEntrata" class="accordion">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#imputazioniContabiliEntrata" data-parent="#accordionImputazioniEntrata" data-toggle="collapse" class="accordion-toggle collapsed">
												Imputazioni contabili entrata<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse" id="imputazioniContabiliEntrata">
											<div class="accordion-inner">
												<h4 class="step-pane">Capitolo <span class="datiRIFCapitolo" id="datiRiferimentoCapitoloEntrataSpan"></span></h4>
												<fieldset class="form-horizontal" id="fieldsetCapitoloEntrata">
													<div class="control-group">
														<label class="control-label" for="annoCapitoloEntrata">Anno</label>
														<div class="controls">
															<s:textfield id="annoCapitoloEntrata" name="capitoloEntrataGestione.annoCapitolo"
																cssClass="lbTextSmall span1" value="%{annoEsercizioInt}" disabled="true" data-maintain="" />
															<span class="al">
																<label class="radio inline" for="numeroCapitoloEntrata">Capitolo</label>
															</span>
															<s:textfield id="numeroCapitoloEntrata" name="capitoloEntrataGestione.numeroCapitolo"
																cssClass="lbTextSmall span2 soloNumeri" maxlength="7" disabled="true" />
															<span class="al">
																<label class="radio inline" for="numeroArticoloEntrata">Articolo</label>
															</span>
															<s:textfield id="numeroArticoloEntrata" name="capitoloEntrataGestione.numeroArticolo"
																cssClass="lbTextSmall span2 soloNumeri" maxlength="7" disabled="true" />
															<s:if test="%{gestioneUEB}">
																<span class="al">
																	<label class="radio inline" for="numeroUEBEntrata">UEB</label>
																</span>
																<s:textfield id="numeroUEBEntrata" name="capitoloEntrataGestione.numeroUEB" cssClass="lbTextSmall span2 soloNumeri" disabled="true" />
															</s:if>
														</div>
													</div>
												</fieldset>
												<h4>Elenco accertamenti associati</h4>
												<table class="table table-hover tab_left" id="tabellaMovimentiEntrata">
													<thead>
														<tr>
															<th>Distinta</th>
															<th>Accertamento</th>
															<th>Descrizione</th>
															<th class="tab_Right">Importo</th>
															<th class="tab_Right">Disponibile</th>
														</tr>
													</thead>
													<tbody>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
								<div id="accordionImputazioniSpesa" class="accordion">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#imputazioniContabiliSpesa" data-parent="#accordionImputazioniSpesa" data-toggle="collapse" class="accordion-toggle collapsed">
												Imputazioni contabili spesa<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse" id="imputazioniContabiliSpesa">
											 <div class="accordion-inner">
												<h4 class="step-pane">Capitolo <span class="datiRIFCapitolo" id="datiRiferimentoCapitoloSpesaSpan"></span></h4>
												<fieldset class="form-horizontal" id="fieldsetCapitoloSpesa">
													<div class="control-group">
														<label class="control-label" for="annoCapitoloSpesa">Anno</label>
														<div class="controls">
															<s:textfield id="annoCapitoloSpesa" name="capitoloUscitaGestione.annoCapitolo"
																cssClass="lbTextSmall span1" value="%{annoEsercizioInt}" disabled="true" data-maintain="" />
															<span class="al">
																<label class="radio inline" for="numeroCapitoloSpesa">Capitolo</label>
															</span>
															<s:textfield id="numeroCapitoloSpesa" name="capitoloUscitaGestione.numeroCapitolo"
																cssClass="lbTextSmall span2 soloNumeri" maxlength="7" disabled="true" />
															<span class="al">
																<label class="radio inline" for="numeroArticoloSpesa">Articolo</label>
															</span>
															<s:textfield id="numeroArticoloSpesa" name="capitoloUscitaGestione.numeroArticolo"
																cssClass="lbTextSmall span2 soloNumeri" maxlength="7" disabled="true" />
															<s:if test="%{gestioneUEB}">
																<span class="al">
																	<label class="radio inline" for="numeroUEBSpesa">UEB</label>
																</span>
																<s:textfield id="numeroUEBSpesa" name="capitoloUscitaGestione.numeroUEB" cssClass="lbTextSmall span2 soloNumeri" disabled="true" />
															</s:if>
														</div>
													</div>
												</fieldset>
												<h4>Elenco impegni associati</h4>
												<table class="table table-hover tab_left" id="tabellaMovimentiSpesa">
													<thead>
														<tr>
															<th>Impegno</th>
															<th>Descrizione</th>
															<th class="tab_Right">Importo</th>
															<th class="tab_Right">Disponibile</th>
														</tr>
													</thead>
													<tbody>
													</tbody>
												</table>
												
												<h4 class="step-pane">Soggetto
													<span id="datiRiferimentoSoggettoSpan">
														<s:property value="descrizioneCompletaSoggetto" />
													</span>
												</h4>
												<fieldset id="fieldsetSoggetto">
													<fieldset class="form-horizontal">
														<div class="control-group">
															<label class="control-label" for="codiceSoggettoSoggetto">Codice *</label>
															<div class="controls">
																<s:textfield id="codiceSoggettoSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" disabled="true" />
															</div>
														</div>
													</fieldset>
													<s:if test="%{sedeSecondariaSoggetto != null}">
														<fieldset class="form-horizontal">
															<div id="accordionSedeSecondariaSoggetto" class="accordion">
																<div class="accordion-group">
																	<div class="accordion-heading">
																		<a href="#collapseSedeSecondariaSoggetto" data-parent="#accordionSedeSecondariaSoggetto" data-toggle="collapse" class="accordion-toggle collapsed" data-overlay="">
																			Sedi secondarie <span id="SPAN_sediSecondarieSoggetto"></span><span class="icon">&nbsp;</span>
																		</a>
																	</div>
																	<div class="accordion-body collapse" id="collapseSedeSecondariaSoggetto">
																		<s:include value="/jsp/soggetto/accordionSedeSecondariaSoggettoDISABLED.jsp" />
																	</div>
																</div>
															</div>
														</fieldset>
													</s:if>
													<s:if test="%{modalitaPagamentoSoggetto != null}">
														<fieldset class="form-horizontal">
															<div id="accordionModalitaPagamentoSoggetto" class="accordion">
																<div class="accordion-group">
																	<div class="accordion-heading">
																		<a href="#collapseModalitaPagamento" data-parent="#accordionModalitaPagamentoSoggetto" data-toggle="collapse" class="accordion-toggle collapsed" data-overlay="">
																			Modalit&agrave; pagamento <span class="datiPagamento" id="SPAN_modalitaPagamentoSoggetto"></span><span class="icon">&nbsp;</span>
																		</a>
																	</div>
																	<div class="accordion-body collapse" id="collapseModalitaPagamento">
																		<s:include value="/jsp/soggetto/accordionModalitaPagamentoSoggettoDISABLED.jsp" />
																	</div>
																</div>
															</div>
														</fieldset>
													</s:if>
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
						
						<s:submit cssClass="btn btn-primary pull-right" value="aggiorna" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}tipoOnere/aggiornaDisabled.js"></script>

</body>
</html>