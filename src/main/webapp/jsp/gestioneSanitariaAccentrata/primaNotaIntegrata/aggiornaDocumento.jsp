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
				<s:form cssClass="form-horizontal" action="%{azioneAggiornamento}" novalidate="novalidate" id="formAggiornaPrimaNotaIntegrata">
					<s:hidden name="primaNota.uid" id="uidPrimaNotaIntegrata" />
					<s:hidden name="primaNota.numero" id="numeroPrimaNotaIntegrata" />
					<s:hidden name="primaNota.descrizione" id="descrizionePrimaNotaIntegrata" />
					<s:hidden name="tipoCollegamentoDatiFinanziari" id="tipoCollegamentoDatiFinanziari" />
					<s:hidden name="primaNota.classificatoreGSA.uid" id="classificatoreGSAPrimaNotaLibera" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Prima Nota <s:property value="numeroPrimaNota"/> </h3>
					<div class="step-pane active" id="step1">
					<fieldset class="form-horizontal">
						<h4 class="step-pane">
							Inserimento: <s:property value="primaNota.dataCreazionePrimaNota"/> (<s:property value="primaNota.loginCreazione" />)
							<s:if test="%{primaNota.dataModificaPrimaNota != null}">
								- Ultima modifica: <s:property value="primaNota.dataModificaPrimaNota"/> (<s:property value="primaNota.loginModifica" />)
							</s:if>
						</h4>
						<div class="boxOrSpan2">
							<div class="boxOrInline">
								<p>Dati Causale</p>
								<ul class="htmlelt">
									<li>
										<dfn>Causale</dfn>
										<dl><s:property value="descrizioneCausale"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Data registrazione</dfn>
										<dl><s:property value="primaNota.dataRegistrazione"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Descrizione</dfn>
										<dl><s:property value="primaNota.descrizione"/>&nbsp;</dl>
									</li>
								</ul>
							</div>
						</div>
						<div class="clear"></div>
						<br />
						<div id="accordionPrimeNoteCollegate" class="accordion">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a href="#divPrimeNoteCollegate" data-parent="#accordionPrimeNoteCollegate" data-toggle="collapse" class="accordion-toggle collapsed">
										Prime note collegate
										<s:if test="%{primaNota.listaPrimaNotaFiglia != null && primaNota.listaPrimaNotaFiglia.size()>0}">
										 	<span>: totale &nbsp;<s:property value="primaNota.listaPrimaNotaFiglia.size()" /></span>
										</s:if>
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
													<th>N.provvisorio</th>
													<th>N.definitivo</th>
													<th>Motivazione</th>
													<th>Stato</th>
												</tr>
											</thead>
											<tbody>
											<s:iterator value="primaNota.listaPrimaNotaFiglia" var="pNotaCollegata">
												<tr>
													<td><s:property value="#pNotaCollegata.tipoCausale.descrizione" /></td>
													<td><s:property value="#pNotaCollegata.bilancio.anno" /></td>
													<td><s:property value="#pNotaCollegata.numero" />
													<td><s:property value="#pNotaCollegata.numeroRegistrazioneLibroGiornale" />
													<td>
														<s:if test='%{#pNotaCollegata.tipoRelazionePrimaNota != null}'>
															<s:property value="#pNotaCollegata.tipoRelazionePrimaNota.descrizione" />
														</s:if>
													</td>
													<td>
														<s:if test='%{#pNotaCollegata.statoOperativoPrimaNota != null}'>
															<s:property value="#pNotaCollegata.statoOperativoPrimaNota.descrizione" />
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
						<br/>
						
						<s:if test="consultazioneDatiFinanziariAbilitata">
							<div id="accordionDatiFinanziari" class="accordion">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a href="#divDatiFinanziari" data-parent="#accordionDatiFinanziari" data-toggle="collapse" class="accordion-toggle collapsed" id="headingAccordionDatiFinanziari">
											Dati finanziari<span class="icon">&nbsp;</span>
										</a>
									</div>
									<div class="accordion-body collapse" id="divDatiFinanziari"></div>
								</div>
							</div>
						</s:if>
						
						<div class="clear"></div>
						<h4 class="step-pane">Elenco</h4>
							<table class="table table-hover tab_left" id="tabellaQuote">
								<thead>
									<tr>
										<th>Numero quota</th>
										<th>Causale</th>
										<th>Data registrazione</th>
										<th>Conto finanziario</th>
										<th>Conto finanziario aggiornato</th>
										<th class="tab_Right"></th>
									</tr>
								</thead>
								<tbody>
									<s:iterator value="listaElementoQuota" var="elqu">
										<tr>
											<!-- <td class="tabRowLight">
												<a data-toggle="modal" data-target="#modaleDettaglioDocumento">
													<p class="pagination-centered">
														<i class="icon-search">&nbsp;</i>
													</p>
												</a>
											</td> -->
											<td><s:property value="#elqu.numeroQuotaString"/></td>
											<td><s:property value="#elqu.causaleString" escape="false"/></td>
											<td><s:property value="#elqu.dataRegistrazioneString"/></td>
											<td><s:property value="#elqu.contoFinanziarioInizialeString"/></td>
											<td><s:property value="#elqu.contoFinanziarioString"/></td>
											<td>
												<s:if test="%{#elqu.registrazioneMovFin.uid != 0 && showPulsanteAggiornamento}">
													<s:a href="" cssClass="btn btn-secondary btn-block" data-mov="%{#elqu.movimentoEP.uid}">Dettaglio</s:a>
												</s:if>
											</td>
										</tr>
									</s:iterator>
								</tbody>
							</table>
						<br />
							<h4 class="step-pane">Elenco scritture</h4>
							<div id="elencoScrittureQuota">
								<table class="table table-hover tab_left" id="tabellaScritture">
									<thead>
										<tr>
											<th>Conto</th>
											<th>Descrizione</th>
											<th class="tab_Right">Dare</th>
											<th class="tab_Right">Avere</th>
											<th class="tab_Right span2">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="2">Totale</th>
											<th class="tab_Right" id="totaleDare"></th>
											<th class="tab_Right" id="totaleAvere"></th>
											<th class="tab_Right span2">&nbsp;</th>
										</tr>
									</tfoot>
								</table>
								<p>
									<button type="button" id="pulsanteInserimentoDati" class="btn btn-secondary pull-left">
										inserisci dati in elenco&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserimentoDati"></i>
									</button>
									<button type="button" id="pulsanteSalvataggioDatiQuota" class="btn btn-secondary pull-right">
										salva dati quota&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteSalvataggioDatiQuota"></i>
									</button>
								</p>
								<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/collapseDatiStruttura.jsp" />
							</div>
						</fieldset>
					</div>
					<div class="Border_line"></div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<%-- <s:if test='%{validazioneAbilitata}'>
							<button type="button" id="validazionePrimaNotaIntegrata" class="btn btn-primary pull-right">valida</button>
						</s:if> --%>
						<s:if test='%{showPulsanteAggiornamento}'>
							<s:submit value="aggiorna" cssClass="btn btn-primary pull-right" />
						</s:if>
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleEliminazioneConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleAggiornaConto.jsp" />
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/primaNotaIntegrata/aggiornaDocumento.js"></script>

</body>
</html>