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
					<s:form id="formInserisciTipoBene" cssClass="form-horizontal" novalidate="novalidate" action="ricercaCategoriaCespiti_effettuaRicerca">
						<h3 id="titolo">Consulta cespite <s:property value="codice"/> </h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden id="uidCespite" name="cespite.uid"/>
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<div class="boxOrSpan2">
										<div class="boxOrInLeft">
											<ul class="htmlelt">
												<li>
													<dfn>Codice</dfn>
													<dl><s:property value="codice"/></dl>
												</li>
												<li>
													<dfn>Descrizione</dfn>
													<dl><s:property value="descrizione"/></dl>
												</li>
												<li>
													<dfn>Tipo Bene</dfn>
													<dl><s:property value="tipoBene"/></dl>
												</li>
												<li>
													<dfn>Classificazione Giuridica</dfn>
													<dl><s:property value="classificazione"/></dl>
												</li>
												<li>
													<dfn>Attivo</dfn>
													<dl><s:property value="attivo" escapeHtml="false"/></dl>
												</li>
												<li>
													<dfn>Soggetto beni culturali</dfn>
													<dl><s:property value="beniCulturali" escapeHtml="false"/></dl>
												</li>
												<li>
													<dfn>Ubicazione</dfn>
													<dl><s:property value="ubicazione"/></dl>
												</li>
											</ul>
										</div>
										<div class="boxOrInRight">
											<ul class="htmlelt">
												
												<li>
													<dfn>Inventario</dfn>
													<dl><s:property value="inventario"/></dl>
												</li>
												<li>
													<dfn>Data ingresso inventario</dfn>
													<dl><s:property value="dataIngressoInventario"/></dl>
												</li>
												<li>
													<dfn>Data cessazione</dfn>
													<dl><s:property value="dataCessazione"/></dl>
												</li>
												<li>
													<dfn>Valore iniziale</dfn>
													<dl><s:property value="valoreIniziale"/></dl>
												</li>
												<li>
													<dfn>Valore attuale</dfn>
													<dl><s:property value="valoreAttuale"/></dl>
												</li>
												<li>
													<dfn>Note</dfn>
													<dl><s:property value="note"/></dl>
												</li>
											</ul>
										</div>
									</div>
								</fieldset>
								<fieldset class="form-horizontal">
									<div id="accordionPianoammortamento" class="accordion">
										<div class="accordion-group">
											<div class="accordion-heading">
													<a id="anchorPianoAmmortamento" href="#collapsePianoAmmortamento" data-parent="#accordionPianoammortamento" data-toggle="collapse" class="accordion-toggle collapsed">
													Piano di ammortamento<span class="icon"></span>
												</a>
											</div>
											<div class="accordion-body collapse" id="collapsePianoAmmortamento"> 
												<div class="accordion-inner container">
													<h4> Totale importi ammortamento accumulati <span id="importiAmmortamentoAccumulati"><s:property value="%{totaleImportoAmmortato}"/></span></h4>
													<table class="table table-hover tab_left dataTable" id="tabellaPianoAmmortamento">
														<thead>
															<tr>
																<th>Anno</th>
																<th>Importo</th>
																<th><abbr title="numero">N.</abbr> Prima prima nota definitiva</th>
																<th>&nbsp;</th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>
													<div id="accordionPrimeNoteCogeAmmortamento" class="accordion">
														<div class="accordion-group">
															<div class="accordion-heading">
																<a id="anchorPrimeNoteAmmortamento" href="#collapsePrimeNoteCogeAmmortamento" data-parent="#accordionPrimeNoteCogeAmmortamento" data-toggle="collapse" class="accordion-toggle collapsed" id="headingAccordionPrimeNoteCogeAmmortamento">
																	Prime note<span class="icon">&nbsp;</span>
																</a>
															</div>
															<div class="accordion-body collapse" id="collapsePrimeNoteCogeAmmortamento">
																<div id="divPrimeNoteAmmortamento" class="accordion-inner">
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div id="accordionPrimeNoteCoge" class="accordion">
										<div class="accordion-group">
											<div class="accordion-heading">
													<a id="anchorPrimeNoteCoge" href="#collapsePrimeNoteCoge" data-parent="#accordionPrimeNoteCoge" data-toggle="collapse" class="accordion-toggle collapsed">
													Dati contabili e prime note della Contabilit&agrave; Generale<span class="icon"></span>
												</a>
											</div>
											<div class="accordion-body collapse" id="collapsePrimeNoteCoge"> 
												<div class="accordion-inner">
													
													<s:include value="/jsp/cespiti/registroa/include/tabellaPrimaNoteRegistroA.jsp"/>
													
													<div id="accordionDatiFinanziariPrimeNoteCoge" class="accordion">
														<div class="accordion-group">
															<div class="accordion-heading">
																<a id="anchorDatiFinanziariPrimeNoteCoge" href="#collapseDatiFinanziariPrimeNoteCoge" data-parent="#accordionDatiFinanziariPrimeNoteCoge" data-toggle="collapse" class="accordion-toggle collapsed" id="headingAccordionDatiFinanziariPrimeNoteCoge">
																	Dati contabili<span class="icon">&nbsp;</span>
																</a>
															</div>
															<div class="accordion-body collapse" id="collapseDatiFinanziariPrimeNoteCoge">
																<div class="accordion-inner">
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
									
									<div id="accordionDismissioni" class="accordion">
										<div class="accordion-group">
											<div class="accordion-heading">
													<a id="anchorDismissioni" href="#collapseDismissioni" data-parent="#accordionDismissioni" data-toggle="collapse" class="accordion-toggle collapsed">
													Dismissioni<span class="icon"></span>
												</a>
											</div>
											<div class="accordion-body collapse" id="collapseDismissioni"> 
												<div class="accordion-inner">
													<table class="table table-hover tab_left dataTable" id="tabellaDismissioniCespite">
														<thead>
															<tr>
																<th>Elenco</th>
																<th>Descrizione</th>
																<th>Provvedimento</th>
																<th>Data cessazione</th>
																<th>Causale dismissione</th>
																<th><abbr title="Numero">N.</abbr> cespiti collegati</th>
																<th>Stato  movimento</th>
																<th>Azioni</th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>
													<fieldset class="form-horizontal" id="primaNotaDismissione">
														<%-- <h4 class="step-pane">Elenco scritture prima nota <span class="infoPrimaNota" id="infoPrimaNotaDismissione"></span></h4>
														<table class="table table-hover tab_left" id="tabellaScrittureDismissione">
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
														</table> --%>
													</fieldset>
												</div>
											</div>
										</div>
									</div>
									
									<div id="accordionRivalutazioni" class="accordion">
										<div class="accordion-group">
											<div class="accordion-heading">
													<a id="anchorRivalutazioni" href="#collapseRivalutazioni" data-parent="#accordionRivalutazioni" data-toggle="collapse" class="accordion-toggle collapsed">
													Rivalutazioni<span class="icon"></span>
												</a>
											</div>
											<div class="accordion-body collapse" id="collapseRivalutazioni"> 
												<div class="accordion-inner">
													<table class="table table-hover tab_left dataTable" id="tabellaRivalutazioniCespite">
														<thead>
															<tr>
																<th>Anno</th>
																<th>Data inserimento</th>
																<th>Tipo variazione</th>
																<th>Descrizione</th>
																<th>Importo</th>
																<th>Stato</th>
																<th>Cespite</th>
																<th>Tipo bene</th>
																<th>&nbsp;</th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>
													<fieldset class="form-horizontal hide" id="primaNotaRivalutazione">
													</fieldset>
												</div>
											</div>
										</div>
									</div>
									
									<div id="accordionSvalutazioni" class="accordion">
										<div class="accordion-group">
											<div class="accordion-heading">
													<a id="anchorSvalutazioni" href="#collapseSvalutazioni" data-parent="#accordionSvalutazioni" data-toggle="collapse" class="accordion-toggle collapsed">
													Svalutazioni<span class="icon"></span>
												</a>
											</div>
											<div class="accordion-body collapse" id="collapseSvalutazioni"> 
												<div class="accordion-inner">
													<table class="table table-hover tab_left dataTable" id="tabellaSvalutazioniCespite">
														<thead>
															<tr>
																<th>Anno</th>
																<th>Data inserimento</th>
																<th>Tipo variazione</th>
																<th>Descrizione</th>
																<th>Importo</th>
																<th>Stato</th>
																<th>Cespite</th>
																<th>Tipo bene</th>
																<th>&nbsp;</th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>
													<fieldset class="form-horizontal hide" id="primaNotaSvalutazione">
													</fieldset>	
												</div>
												
											</div>
										</div>
									</div>
									<div id="accordionDonazioniRinvenimenti" class="accordion">
										<div class="accordion-group">
											<div class="accordion-heading">
													<a id="anchorDonazioniRinvenimenti" href="#collapseDonazioniRinvenimenti" data-parent="#accordionDonazioniRinvenimenti" data-toggle="collapse" class="accordion-toggle collapsed">
													Donazioni/Rinvenimenti<span class="icon"></span>
												</a>
											</div>
											<div class="accordion-body collapse" id="collapseDonazioniRinvenimenti"> 
												<div class="accordion-inner">													
													<fieldset class="form-horizontal" id="primaNotaDonazioneRinvenimento">
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
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/cespite/consultaCespite.js"></script>
</body>
</html>