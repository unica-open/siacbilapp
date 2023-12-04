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

	<%-- Pagina JSP vera e propria --%>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:form cssClass="form-horizontal" action="" id="formAssociaAttivitaIvaCapitolo" novalidate="novalidate">
						<s:include value="/jsp/include/messaggi.jsp" />
						
						<h3>Associa attivit&agrave; iva al capitolo</h3>
						<fieldset class="form-horizontal">
							<div class="step-content">
								<div class="step-pane active">
									<h4>Seleziona il capitolo</h4>
									<fieldset class="form-horizontal">
										<div class="control-group">
											<span class="control-label">Capitolo *</span>
											<div class="controls">
												<label class="radio inline"><input type="radio" name="tipoCapitolo" value="EntrataGestione" <s:if test='%{"EntrataGestione".equals(tipoCapitolo)}'>checked</s:if>/> Entrata</label>
												<label class="radio inline"><input type="radio" name="tipoCapitolo" value="UscitaGestione" <s:if test='%{"UscitaGestione".equals(tipoCapitolo)}'>checked</s:if>/> Spesa</label>
											</div>
										</div>
										<div class="control-group">
											<label for="annoCapitolo" class="control-label">Anno *</label>
											<div class="controls">
												<s:textfield id="annoCapitolo" name="capitolo.annoCapitolo" value="%{annoEsercizioInt}" cssClass="lbTextSmall" disabled="true" data-maintain="" />
												<s:hidden id="annoCapitoloHidden" name="capitolo.annoCapitolo" value="%{annoEsercizioInt}" data-maintain="" />
												<span class="al">
													<label for="numeroCapitolo" class="radio inline">Capitolo *</label>
												</span>
												<s:textfield id="numeroCapitolo" name="capitolo.numeroCapitolo" cssClass="lbTextSmall soloNumeri span2" maxlength="30" placeholder="%{'capitolo'}" />
												<span class="al">
													<label for="numeroArticolo" class="radio inline">Articolo *</label>
												</span>
												<s:textfield id="numeroArticolo" name="capitolo.numeroArticolo" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'articolo'}" />
												<span class="al">
													<label for="statoOperativoElementoDiBilancioCapitolo" class="radio inline">Stato *</label>
												</span>
												<s:select list="listaStatoOperativoElementoDiBilancio" name="statoOperativoElementoDiBilancio" id="statoOperativoElementoDiBilancioCapitolo" cssClass="lbTextSmall span2" disabled="true" />
												
												<button type="button" class="btn btn-primary" id="pulsanteRicercaCapitolo">
													<i class="icon-search icon"></i>&nbsp;cerca
												</button>
											</div>
										</div>
										
										<div class="collapse" id="collapseCapitoloUEB">
											<div class="step-pane active">
												<h4>Elenco Ueb capitolo <span id="spanElencoUEBCapitolo"></span></h4>
												<table summary="...." class="table table-hover tab_left" id="tabellaUEBCapitolo">
													<thead>
														<tr>
															<th>&nbsp;</th>
															<th>Capitolo</th>
															<th>Stato</th>
															<th>Classificazione</th>
															<th>Stanz. competenza</th>
															<th>Stanz. residuo</th>
															<th>Stanz. cassa</th>
															<th><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
															<th><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
														</tr>
													</thead>
													<tbody>
													</tbody>
													<tfoot>
													</tfoot>
												</table>
												<p>
													<button type="button" class="btn btn-primary" id="pulsanteSelezionaUeb">
														attivit&agrave; iva associate&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteRicercaAccertamentoModale"></i>
													</button>
													<button type="button" class="btn btn-secondary" id="pulsanteAnnullaSelezione">annulla selezione</button>
												</p>
												<div class="Border_line"></div>
											</div>
										</div>

										<div class="collapse" id="attivitaIvaAssociate">
											<div class="step-content step-pane active">
												<table class="table table-hover tab_left" id="tabellaAttivitaIvaAssociate">
													<thead>
														<tr>
															<th>Codice</th>
															<th>Descrizione</th>
															<th>Rilevante IRAP</th>
															<th>Gruppo attivit&agrave; iva</th>
															<th class="tab_Right">&nbsp;</th>
														</tr>
													</thead>
													<tbody>
													</tbody>
												</table>
												<p>
													<button type="button" class="btn btn-secondary" id="pulsanteInserisciNuovaRelazione">inserisci nuova relazione</button>
												</p>
												
												<div class="collapse" id="collapseRicercaAttivitaIva">
													<div class="accordion_info">

														<fieldset class="form-horizontal">
															<h4 class="step-pane">Ricerca le attivit&agrave; iva</h4>
															<div class="control-group">
																<label for="codiceAttivitaIva" class="control-label">Codice</label>
																<div class="controls">
																	<s:textfield id="codiceAttivitaIva" name="attivitaIva.codice" cssClass="lbTextSmall span2" placeholder="%{'codice'}" />
																	<span class="al">
																		<label for="descrizioneAttivitaIva" class="radio inline">Descrizione</label>
																	</span>
																	<s:textfield id="descrizioneAttivitaIva" name="attivitaIva.descrizione" cssClass="lbTextSmall span7" placeholder="%{'descrizione'}" />
																	<button type="button" class="btn btn-primary" id="pulsanteRicercaAttivitaIva">
																		<i class="icon-search icon"></i>&nbsp;cerca
																	</button>
																</div>
															</div>
														</fieldset>
													</div>
												</div>
											</div>
											<div class="clear"></div>
										</div>
									</fieldset>
								</div>
							</div>
						</fieldset>
						
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn btn-secondary reset">annulla</button>
						</p>
						
						<s:include value="/jsp/attivitaIva/associaAttivitaIvaCapitolo/modaleEliminaAttivitaIva.jsp" />
						<s:include value="/jsp/attivitaIva/associaAttivitaIvaCapitolo/modaleRicercaAttivitaIva.jsp" />
					</s:form>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/attivitaIva/associaAttivitaIvaCapitolo/associa.js"></script>
</body>
</html>