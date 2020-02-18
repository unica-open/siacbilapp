<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="si" %>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />

</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="#" novalidate="novalidate" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Consultazione tipo onere <s:property value="titoloConsultazione" /></h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<h4 class="step-pane">Elenco oneri</h4>
								<table class="table table-hover tab_left" id="tabellaTipoOnere">
									<thead>
										<tr>
											<th>Data inizio</th>
											<th>Data fine</th>
											<s:if test="%{tipoOnereSplitReverse}">
												<th>Split / Reverse</th>
											</s:if>
											<th>Aliquota a carico soggetto</th>
											<th>Aliquota a carico Ente</th>
											<th>Quadro 770</th>
											<th>Causale 770</th>
											<th>Attivit&agrave; onere</th>
											<th>Codici somme non soggette</th>
										</tr>
									</thead>
									<tbody>
										<s:iterator value="listaTipoOnere" var="tipo">
											<tr>
												<td><s:property value="%{#tipo.dataInizioValidita}" /></td>
												<td><s:property value="%{#tipo.dataFineValidita}" /></td>
												<s:if test="%{tipoOnereSplitReverse}">
													<td>
														<s:property value="%{#tipo.tipoIvaSplitReverse.codice + ' - ' + #tipo.tipoIvaSplitReverse.descrizione}" />
													</td>
												</s:if>
												<td><s:property value="%{#tipo.aliquotaCaricoSoggetto}" /></td>
												<td><s:property value="%{#tipo.aliquotaCaricoEnte}" /></td>
												<td><s:property value="%{#tipo.quadro770}" /></td>
												<td>
													<s:select list="%{#tipo.causali770}" cssClass="span12" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
												</td>
												<td>
													<s:select list="%{#tipo.attivitaOnere}" cssClass="span12" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
												</td>
												<td>
													<s:select list="%{#tipo.codiciSommaNonSoggetta}" cssClass="span12" listKey="" listValue="%{codice + ' - ' + descrizione}" />
												</td>
											</tr>
										</s:iterator>
									</tbody>
									<tfoot>
									</tfoot>
								</table>
								
								<div id="accordionImputazioniEntrata" class="accordion">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#collapseImputazioniEntrata" data-parent="#accordionImputazioniEntrata" data-toggle="collapse" class="accordion-toggle collapsed">
												Dettaglio imputazioni contabili entrata<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse" id="collapseImputazioniEntrata">
											<div class="accordion-inner">
												<h4>Elenco accertamenti associati</h4>
												<table class="table table-hover tab_left" id="tabellaCausaleEntrata">
													<thead>
														<tr>
															<th class="span2">Distinta</th>
															<th class="span2">Data inizio</th>
															<th class="span2">Data fine</th>
															<th>Capitolo</th>
															<th>Accertamento</th>
															<th>Descrizione</th>
														</tr>
													</thead>
													<tbody>
														<s:iterator value="listaCausaleEntrata" var="ce">
															<tr>
																<td>
																	<s:if test="%{#ce.distinta != null}">
																		<s:property value="%{#ce.distinta.descrizione}"/>
																	</s:if>
																</td>
																<td class="span2"><s:property value="%{#ce.dataInizioValidita}"/></td>
																<td class="span2"><s:property value="%{#ce.dataAnnullamento}"/></td>
																<td>
																	<s:if test="%{#ce.capitoloEntrataGestione != null}">
																		<s:property value="%{#ce.capitoloEntrataGestione.annoCapitolo}"/>/<s:property value="%{#ce.capitoloEntrataGestione.numeroCapitolo}"/>/<s:property value="%{#ce.capitoloEntrataGestione.numeroArticolo}"/>
																		<s:if test="gestioneUEB">
																			-<s:property value="%{#ce.capitoloEntrataGestione.numeroUEB}"/>
																		</s:if>
																	</s:if>
																</td>
																<td>
																	<s:if test="%{#ce.accertamento != null}">
																		<s:property value="%{#ce.accertamento.annoMovimento}"/>/<si:plainstringproperty value="#ce.accertamento.numero"/>
																		<s:if test="%{#ce.subAccertamento != null}">
																			-<si:plainstringproperty value="#ce.subAccertamento.numero"/>
																		</s:if> 
																	</s:if>
																</td>
																<td>
																	<s:if test="%{#ce.accertamento != null}">
																		<s:property value="%{#ce.accertamento.descrizione}"/>
																	</s:if>
																</td>
															</tr>
														</s:iterator>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
								<div id="accordionImputazioniSpesa" class="accordion">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#collapseImputazioniSpesa" data-parent="#accordionImputazioniSpesa" data-toggle="collapse" class="accordion-toggle collapsed">
												Dettaglio imputazioni contabili spesa<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse" id="collapseImputazioniSpesa">
											<div class="accordion-inner">
												<h4>Elenco impegni associati</h4>
												<table class="table table-hover tab_left" id="tabellaCausaleSpesa">
													<thead>
														<tr>
															<th class="span2">Data inizio</th>
															<th class="span2">Data fine</th>
															<th>Capitolo</th>
															<th>Impegno</th>
															<th>Descrizione</th>
														</tr>
													</thead>
													<tbody>
														<s:iterator value="listaCausaleSpesa" var="cs">
															<tr>
																<td class="span2"><s:property value="%{#cs.dataInizioValidita}"/></td>
																<td class="span2"><s:property value="%{#cs.dataFineValidita}"/></td>
																<td>
																	<s:if test="%{#cs.capitoloUscitaGestione != null}">
																		<s:property value="%{#cs.capitoloUscitaGestione.annoCapitolo}"/>/<s:property value="%{#cs.capitoloUscitaGestione.numeroCapitolo}"/>/<s:property value="%{#cs.capitoloUscitaGestione.numeroArticolo}"/>
																		<s:if test="gestioneUEB">
																			-<s:property value="%{#cs.capitoloUscitaGestione.numeroUEB}"/>
																		</s:if>
																	</s:if>
																</td>
																<td>
																	<s:if test="%{#cs.impegno != null}">
																		<s:property value="%{#cs.impegno.annoMovimento}"/>/<si:plainstringproperty value="#cs.impegno.numero"/>
																		<s:if test="%{#cs.subImpegno != null}">
																			-<si:plainstringproperty value="#cs.subImpegno.numero"/>
																		</s:if>
																	</s:if>
																</td>
																<td>
																	<s:if test="%{#cs.impegno != null}">
																		<s:property value="%{#cs.impegno.descrizione}"/>
																	</s:if>
																</td>
															</tr>
														</s:iterator>
													</tbody>
												</table>
												<h4>Dettaglio soggetti associati</h4>
												<table class="table table-hover tab_left" id="tabellaSoggetto">
													<thead>
														<tr>
															<th class="span2">Data inizio</th>
															<th class="span2">Data fine</th>
															<th>Soggetto</th>
															<th>Descrizione</th>
															<th>Codice fiscale</th>
															<th>Sede</th>
															<th>Modalit&agrave; di pagamento</th>
														</tr>
													</thead>
													<tbody>
														<s:iterator value="listaSoggetto" var="s">
															<tr>
																<td class="span2"><s:property value="%{#s.dataInizioValidita}"/></td>
																<td class="span2"><s:property value="%{#s.dataFineValidita}"/></td>
																<td>
																	<s:property value="%{#s.codiceSoggetto}" />
																</td>
																<td>
																	<s:property value="%{#s.denominazione}" />
																</td>
																<td>
																	<s:property value="%{#s.codiceFiscale}" />
																</td>
																<td>
																	<s:if test="%{#s.sediSecondarie != null}">
																		<s:iterator value="#s.sediSecondarie" var="sss" status="sts">
																			<s:if test="%{#sts.first}">
																				<s:property value="%{#sss.denominazione}"/>
																			</s:if>
																		</s:iterator>
																	</s:if>
																	<%--s:property value="%{#s.sediSecondarie.{^ true}.denominazione}" /--%>
																</td>
																<td>
																	<s:if test="%{#s.modalitaPagamentoList != null}">
																		<s:iterator value="#s.modalitaPagamentoList" var="mps" status="sts">
																			<s:if test="%{#sts.first}">
																				<s:property value="%{#mps.descrizione}"/>
																			</s:if>
																		</s:iterator>
																	</s:if>
																	<%--s:property value="%{#s.elencoModalitaPagamento.{^ true}.descrizione}" /--%>
																</td>
															</tr>
														</s:iterator>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}tipoOnere/consulta.js"></script>

</body>
</html>