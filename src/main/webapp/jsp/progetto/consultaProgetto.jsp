<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
					<h3>Progetto: <s:property value="progetto.codice"/></h3>
					
					
					<fieldset class="form-horizontal">
						<div class="well">
							<dl class="dl-horizontal">
								<dt>Stato del Progetto</dt>
								<dd><s:property value="statoOperativoProgettoCapitalizzato"/>&nbsp;</dd>
								<dt>Descrizione Progetto</dt>
								<dd><s:property value="progetto.descrizione"/>&nbsp;</dd>
								<dt>Provvedimento</dt>
								<dd><s:property value="descrizioneProvvedimentoPerConsulta"/>&nbsp;</dd>
								<dt>Ambito</dt>
								<dd><s:property value="progetto.tipoAmbito.descrizione"/>&nbsp;</dd>
								<dt>Note</dt>
								<dd><s:property value="progetto.note"/>&nbsp;</dd>
								<dt>Data validazione progetto a base di gara</dt>
								<dd> <s:property value="progetto.dataIndizioneGara"/>&nbsp;</dd>
								<dt>Anno Programmazione</dt>
								<dd> <s:property value="progetto.dataAggiudicazioneGara"/>&nbsp;</dd>
								<dt>Investimento in corso di definizione</dt>
								<dd>
									<s:if test="%{progetto.investimentoInCorsoDiDefinizione}">
										S&igrave;
									</s:if>
									<s:else>
										No
									</s:else>
									&nbsp;
								</dd>
								<dt>Note</dt>
								<dd><s:property value="progetto.note"/>&nbsp;</dd>
								<dt><abbr title="codice unico progetto">CUP</abbr></dt>
								<dd><s:property value="progetto.cup"/>&nbsp;</dd>
								<dt>Servizio</dt>
								<dd><s:property value="servizio"/>&nbsp;</dd>
								<dt>RUP</dt>
								<dd><s:property value="progetto.responsabileUnico"/>&nbsp;</dd>
								<dt>Spazi finanziari</dt>
								<dd><s:property value="spaziFinanziari"/>&nbsp;</dd>
								<dt>Modalit&agrave; affidamento</dt>
								<dd><s:property value="modalitaAffidamento"/>&nbsp;</dd>
								
							</dl>
						</div>
					</fieldset>
					<s:include value="/jsp/progetto/include/consultaCronoprogrammaDiGestioneAccordion.jsp" />
					
					<div class="accordion" id="accordionPadre">
						<s:iterator value="listaCronoprogrammiCollegatiAlProgetto" var="entry" status="status">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordionPadre" href="#collapse${status.index}">
									Cronoprogramma : <s:property value="%{#entry.codice}"/>&nbsp;<s:property value="%{#entry.descrizione}"/> <span class="icon">&nbsp;</span>
									</a>
								</div>
								
								
								<div id="collapse${status.index}" class="accordion-body collapse">
									<div class="accordion-inner">
										<fieldset class="form-horizontal">
											<s:hidden value="%{#entry.uid}" cssClass="uidDelCronoprogramma"/>
											<h4 class="step-pane">Entrate</h4>
											<table id="tabellaEntrata${status.index}" class="table table-hover tab_left dataTable" summary="....">
												<thead>
													<tr>
														<th scope="col">Entrata prevista</th>
														<th scope="col">Anno di competenza</th>
														<th scope="col">Classificazione di bilancio</th>
														<th scope="col">Capitolo/Articolo<s:if test="%{gestioneUEB}">/UEB</s:if></th>
														<th scope="col" class="tab_Right">Valore</th>
													</tr>
												</thead>
												<tbody>
													<s:iterator value="capitoliEntrata" var="entryDettaglioEntrataCronoprogramma">
														<tr>
															<td class="soloNumeri decimale"><s:property value="%{#entryDettaglioEntrataCronoprogramma.descrizioneCapitolo}"/></td>
															<td><s:property value="%{#entryDettaglioEntrataCronoprogramma.annoCompetenza}"/></td>
															<td>
																<s:if test="%{#entryDettaglioEntrataCronoprogramma.titoloEntrata.codice != null}">
																	<s:property value="%{#entryDettaglioEntrataCronoprogramma.titoloEntrata.codice}"/>
																	<s:if test="%{#entryDettaglioEntrataCronoprogramma.tipologiaTitolo.codice != null}">
																		&nbsp;-&nbsp;
																		<s:property value="%{#entryDettaglioEntrataCronoprogramma.tipologiaTitolo.codice}"/>
																	</s:if>
																</s:if>
															</td>
															<td>
																<s:if test="%{#entryDettaglioEntrataCronoprogramma.numeroCapitolo != null}">
																	<s:property value="%{#entryDettaglioEntrataCronoprogramma.numeroCapitolo}"/>
																	<s:if test="%{#entryDettaglioEntrataCronoprogramma.numeroArticolo != null}">/
																		<s:property value="%{#entryDettaglioEntrataCronoprogramma.numeroArticolo}"/>
																		<s:if test="%{gestioneUEB}">/
																			<s:property value="%{#entryDettaglioEntrataCronoprogramma.numeroUEB}"/>
																		</s:if>
																		
																		<s:if test="%{(#entryDettaglioEntrataCronoprogramma.capitoloEntrataPrevisione == null || #entryDettaglioEntrataCronoprogramma.capitoloEntrataPrevisione.uid == 0) && #entryDettaglioEntrataCronoprogramma.isAvanzoAmministrazione}">
																			(Avanzo di Amm.)
																		</s:if>
																	</s:if>
																</s:if>
															</td>
															<td class="tab_Right decimale"><s:property value="%{#entryDettaglioEntrataCronoprogramma.stanziamento}"/></td>
														</tr>
													</s:iterator>
												</tbody>
											</table>
											<p>
												<a class="btn btn-secondary" id="pulsanteApriConsultaTotaliEntrata${status.index}" title="visualizza totali">
												Consulta Totali&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_consultaTotaliEntrata${status.index}"></i>
												</a>
												<%-- data-toggle="modal" href="#TotaliEntrata" --%>	
											</p>
											<br>
											<h4 class="step-pane">Spese</h4>
											<table id="tabellaUscita${status.index}" class="table table-hover tab_left dataTable" summary="....">
												<thead>
													<tr>
														<th scope="col">Attivit&agrave; prevista</th>
														<th scope="col">Anno spesa</th>
														<th scope="col">Anno entrata</th>
														<th scope="col">Classificazione di bilancio</th>
														<th scope="col">Capitolo/Articolo<s:if test="%{gestioneUEB}">/UEB</s:if></th>
														<th scope="col" class="tab_Right">Valore Previsto</th>
													</tr>
												</thead>
												<tbody>
													<s:iterator value="capitoliUscita" var="entryDettaglioUscitaCronoprogramma">
														<tr>
															<td><s:property value="%{#entryDettaglioUscitaCronoprogramma.descrizioneCapitolo}"/></td>
															<td><s:property value="%{#entryDettaglioUscitaCronoprogramma.annoCompetenza}"/></td>
															<td><s:property value="%{#entryDettaglioUscitaCronoprogramma.annoEntrata}"/></td>
															<td>
																<s:if test="%{#entryDettaglioUscitaCronoprogramma.missione.codice != null}">
																	<s:property value="%{#entryDettaglioUscitaCronoprogramma.missione.codice}"/>
																	<s:if test="%{#entryDettaglioUscitaCronoprogramma.programma.codice != null}">
																		&nbsp;-&nbsp;
																		<s:property value="%{#entryDettaglioUscitaCronoprogramma.programma.codice}"/>
																		<s:if test="%{#entryDettaglioUscitaCronoprogramma.titoloSpesa.codice != null}">
																			&nbsp;-&nbsp;
																			<s:property value="%{#entryDettaglioUscitaCronoprogramma.titoloSpesa.codice}"/>
																		</s:if>
																	</s:if>
																</s:if>
															</td>
															<td>
																<s:if test="%{#entryDettaglioUscitaCronoprogramma.numeroCapitolo != null}">
																	<s:property value="%{#entryDettaglioUscitaCronoprogramma.numeroCapitolo}"/>
																	<s:if test="%{#entryDettaglioUscitaCronoprogramma.numeroArticolo != null}">/
																		<s:property value="%{#entryDettaglioUscitaCronoprogramma.numeroArticolo}"/>
																		<s:if test="%{gestioneUEB}">/
																			<s:property value="%{#entryDettaglioUscitaCronoprogramma.numeroUEB}"/>
																		</s:if>
																	</s:if>
																</s:if>
															</td>
															<td class="tab_Right decimale"><s:property value="%{#entryDettaglioUscitaCronoprogramma.stanziamento}"/></td>
														</tr>
													</s:iterator>
												</tbody>
											</table>
											<p>
												<a class="btn btn-secondary" id="pulsanteApriConsultaTotaliUscita${status.index}" title="visualizza totali">
												Consulta Totali&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_consultaTotaliUscita${status.index}"></i>
												</a>
												<%-- data-toggle="modal" href="#TotaliUscita" --%>	
											</p>
										</fieldset>
									</div>
								</div>
							</div>
						</s:iterator>
					</div>
				</div>	
			</div>	
			<p>
				<s:include value="/jsp/include/indietro.jsp" />
			</p>			
		</div>
	</div>

<!-- start Modal Totali -->
<div id="modaleConsultaTotali" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="TotaliEntrataLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 id="titoloTotali"><%--Totali Entrata--%></h4>
	</div>
	<div class="modal-body">
		<table class="table table-hover tab_centered" id="tabellaTotali">
			<thead>
			</thead>
		</table>
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
	</div>
</div>
<!-- end Modal Totali -->
	<s:include value="/jsp/include/footer.jsp" />
	
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}progetto/consulta.js"></script>

</body>
</html>