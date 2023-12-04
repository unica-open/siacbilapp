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
				<s:hidden name="baseUrl" id="HIDDEN_baseUrl" />
				<s:hidden id="HIDDEN_ambito" name ="ambito"/>
				<s:form cssClass="form-horizontal" action="%{urlStep2}" novalidate="novalidate" id="formInserisciPrimaNotaLibera_step2">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden name="primaNotaLibera.uid" id="uidPrimaNotaLibera" />
					<s:hidden name="contiCausale" id="contiCausale" />
					<%-- DATI DA PRENDERE DA REGISTRATA --%>
					<h3><span class="tlt-info"><s:property value="stringaRiepiloCausaleEPStep1" /></span></h3>
					<h4><span class="tlt-info"><s:property value="stringaRiepiloDescrizioneStep1" /></span></h4>
					
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>inserimento prima nota<span class="chevron"></span></li>
							<li data-target="#step2" class="active"><span class="badge">2</span>dettaglio scritture<span class="chevron"></span></li>
							<li data-target="#step3"><span class="badge">3</span>riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal" id="#fieldsetStep2">
								<div class="span12">
									<div class="span4">
										<div class="control-group margin-medium">
											<label class="control-label" for="importoDaRegistrare">Importo da registrare</label>
											<div class="controls">
												<s:textfield id="importoDaRegistrare" name="importoDaRegistrare" cssClass="span2 soloNumeri decimale tab_Right" disabled="%{aggiornamento}"/>
												<s:if test="%{aggiornamento}">
													<s:hidden name="importoDaRegistrare" />
												</s:if>
											</div>
										</div>
										<div class="control-group margin-medium">
											<label class="control-label" for="daRegistrare">Da registrare (D - A)</label>
											<div class="controls">
												<s:textfield id="daRegistrare" name="daRegistrare" cssClass="span2 soloNumeri decimale tab_Right" readonly="true" />
											</div>
										</div>
									</div>
									<%-- SIAC-8134 --%>
									<div class="span8">
									<%-- <div id="fieldsetStrutturaCompetente">
											<s:hidden name="nomeAzioneSAC" id="nomeAzioneSAC" />
											<div class="control-group margin-medium">
												<label class="control-label">Struttura competente *</label>
												<div class="controls">
													<div class="accordion w-95 struttAmm whitespace-nowrap">
														<div class="accordion-group">
															<div class="accordion-heading">
																<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa_PRIMA_NOTA_LIBERA_GSA_SD" href="#struttAmm_PRIMA_NOTA_LIBERA_GSA">
																	<span id="SPAN_StrutturaAmministrativoContabile_PRIMA_NOTA_LIBERA_GSA">
																		Seleziona la Struttura competente <i class="icon-spin icon-refresh spinner" id="SPINNER_StrutturaAmministrativoContabile"></i>
																	</span>
																</a>
															</div>
															<div id="struttAmm_PRIMA_NOTA_LIBERA_GSA" class="accordion-body collapse">
																<div class="accordion-inner">
																	<ul id="treeStruttAmm_PRIMA_NOTA_LIBERA_GSA" class="ztree treeStruttAmm"></ul>
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid_PRIMA_NOTA_LIBERA_GSA" name="strutturaCompetentePrimaNotaLibera.uid" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice_PRIMA_NOTA_LIBERA_GSA" name="strutturaCompetentePrimaNotaLibera.codice" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione_PRIMA_NOTA_LIBERA_GSA" name="strutturaCompetentePrimaNotaLibera.descrizione" />
										</div> --%>
									</div> 
									<%-- SIAC-8134 --%>
								</div>
								<div class="span12">
									<div id="accordionPrimeNoteCollegate" class="accordion w-95">
										<div class="accordion-group">
											<div class="accordion-heading">
												<a href="#divPrimeNoteCollegate" data-parent="#accordionPrimeNoteCollegate" data-toggle="collapse" class="accordion-toggle collapsed">
													Prime note collegate<span id="spanTotaleNoteCollegate"></span>
													<span class="icon">&nbsp;</span>
												</a>
											</div>
											<div class="accordion-body collapse" id="divPrimeNoteCollegate">
												<div class="accordion-inner">
													<table class="table table-hover tab_left" id="tabellaPrimeNoteCollegate">
														<thead>
															<tr>
																<th>Tipo</th>
																<th>Anno</th>
																<th>Numero</th>
																<th>Motivazione</th>
																<th>Stato</th>
																<th class="tab_Right span2">&nbsp;</th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>
													<p>
														<button type="button" id="pulsanteCollegamentoPrimeNote" class="btn btn-secondary">
															collega prima nota&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteCollegamentoPrimeNote"></i>
														</button>
													</p>
													<s:include value="/jsp/contabilitaGenerale/primaNotaLibera/include/collapseCollegamentoPrimaNota.jsp" />
												</div>
											</div>
										</div>
									</div>
								</div>
								<h4>Elenco scritture</h4>
								<table class="table table-hover tab_left" id="tabellaScritture">
									<thead>
										<tr>
											<th>Conto</th>
											<th>Descrizione</th>
											<th>Missione</th>
											<th>Programma</th>
											<th class="tab_Right">Dare</th>
											<th class="tab_Right">Avere</th>
											<th class="tab_Right span2">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="4">Totale</th>
											<th class="tab_Right" id="totaleDare"></th>
											<th class="tab_Right" id="totaleAvere"></th>
											<th class="tab_Right span2">&nbsp;</th>
										</tr>
									</tfoot>
								</table>
								<p>
									<button type="button" id="pulsanteInserimentoDati" class="btn btn-secondary">
										inserisci dati in elenco&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserimentoDati"></i>
									</button>
								</p>
								<s:include value="/jsp/contabilitaGenerale/primaNotaLibera/include/collapseDatiStruttura.jsp" />
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:a href="%{urlBackToStep1}" cssClass="btn" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<s:a href="%{urlAnnullaStep2}" cssClass="btn btn-secondary" id="pulsanteAnnullaStep2">annulla</s:a>
						<s:submit value="salva" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/primaNotaLibera/include/modaleEliminazioneConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/primaNotaLibera/include/modaleAggiornaConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaPrimaNota.jsp" />
			</div>
		</div>
	</div>	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/ricercaPrimaNota.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/ztree/ztreeSAC.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/strutturaAmministrativaContabile/strutturaAmministrativoContabile.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/gestioneSanitariaAccentrata/primaNotaLibera/inserisci.aggiorna.step2.js"></script>
	
</body>
</html>