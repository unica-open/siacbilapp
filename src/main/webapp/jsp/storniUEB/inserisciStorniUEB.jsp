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

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<h3>Inserimento storni UEB</h3>
				<div class="step-content">
					<s:form id="inserimentoStorno" cssClass="form-horizontal" novalidate="novalidate" action="inserimentoStornoUEB">
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden name="bilancio.faseEStatoAttualeBilancio.faseBilancio" id="HIDDEN_faseBilancio" data-maintain="" />
						<s:hidden name="bilancio.faseEStatoAttualeBilancio.statoBilancio" id="HIDDEN_statoBilancio" data-maintain="" />

						<div class="accordion margin-large" id="accordion">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseb">
										<i class="icon-pencil icon-2x"></i>&nbsp;UEB sorgente
										<span id="SPAN_InformazioniCapitoloSorgente"></span>
										<span class="icon"></span>
									</a>
								</div>
								<div id="collapseb" class="accordion-body collapse in">
									<div class="accordion-inner">
										<h4>Seleziona il capitolo Sorgente</h4>
										<fieldset class="form-horizontal">
											<div class="control-group">
												<span class="control-label">Capitolo</span>
												<div class="controls">
													<label class="radio inline">
														<input type="radio" name="tipoCapitolo" id="tipoCapitoloRadio1" value="Entrata" <s:if test='%{tipoCapitolo eq "Entrata"}'>checked="checked"</s:if>>&nbsp;Entrata
													</label>
													<label class="radio inline">
														<input type="radio" name="tipoCapitolo" id="tipoCapitoloRadio2" value="Uscita" <s:if test='%{tipoCapitolo eq "Uscita"}'>checked="checked"</s:if>>&nbsp;Spesa
													</label>
												</div>
											</div>
											<div class="control-group">
												<label class="control-label" for="annoCapitoloSorgente">Anno</label>
												<div class="controls">
													<s:textfield id="annoCapitoloSorgente" cssClass="lbTextSmall span1" value="%{annoEsercizio}" disabled="true" />
													<s:hidden name="annoEsercizioInt" data-maintain="" />
													<span class="al">
														<label class="radio inline" for="numeroCapitoloSorgente">Capitolo *</label>
													</span>
													<s:textfield id="numeroCapitoloSorgente" cssClass="lbTextSmall span2 soloNumeri" name="numeroCapitolo" required="true" />
													<span class="al">
														<label class="radio inline" for="numeroArticoloSorgente">Articolo *</label>
													</span>
													<s:textfield id="numeroArticoloSorgente" cssClass="lbTextSmall span2 soloNumeri" name="numeroArticolo" required="true" />
													<span class="al">
														<label class="radio inline" for="statoCapitoloSorgente">Stato</label>
													</span>
													<select id="statoCapitoloSorgente" class="lbTextSmall span2" disabled="disabled">
														<option>valido</option>
													</select>
													<s:hidden name="stato" value="VALIDO" />
													<a class="btn btn-primary" href="#" id="pulsanteRicercaCapitolo">
														<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_CapitoloSorgente"></i>
													</a>
												</div>
											</div>
										</fieldset>
										<div id="tabellaCapitoloSorgente" class="hide">
											<h4 id="informazioniCapitoloSorgente">
												<s:if test="uidCapitoloSorgente != null && uidCapitoloSorgente != 0">
													Elenco Ueb capitolo <s:property value="numeroCapitolo"/>/<s:property value="numeroArticolo"/>
												</s:if>
											</h4>
											<table class="table table-hover" id="risultatiRicercaCapitoloSorgente">
												<thead>
													<tr>
														<th></th>
														<th scope="col">Capitolo</th>
														<th scope="col">Stato</th>
														<th scope="col">Classificazione</th>
														<th scope="col">Stanz.competenza</th>
														<th scope="col">Stanz. residuo</th>
														<th scope="col">Stanz.cassa</th>
														<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
														<th scope="col"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
													</tr>
												</thead>
												<tbody>
												</tbody>
												<tfoot>
												</tfoot>
											</table>
											<a class="btn" href="#" id="pulsanteImpostaCapitoloSorgente">
												seleziona&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_DisponibilitaSorgente"></i>
											</a>
										</div>

										<div id="divDisponibilitaCapitoloSorgente" class="hide">
											<h4 id="disponibilitaCapitoloSorgente">
												<%-- UEB 0000004/1/3 - Disponibilita: XXXXXX --%>
											</h4>
											<s:hidden name="disponibilitaSorgente" id="HIDDEN_disponibilitaSorgente" />
											<table summary="riepilogo incarichi" class="table table-hover">
												<tr>
													<th>&nbsp;</th>
													<th><s:property value="annoEsercizioInt"/></th>
													<th><s:property value="%{annoEsercizioInt + 1}"/></th>
													<th><s:property value="%{annoEsercizioInt + 2}"/></th>
												</tr>
												<tr>
													<th>Competenza</th>
													<td>
														<s:textfield id="competenzaSorgente0" cssClass="input-small soloNumeri decimale-negativo" required="true" name="stanziamentoCompetenzaSorgente0" />
														<label for="competenzaSorgente0" class="nascosto">inserisci importo</label>
													</td>
													<td>
														<s:textfield id="competenzaSorgente1" cssClass="input-small soloNumeri decimale-negativo" required="true" name="stanziamentoCompetenzaSorgente1" />
														<label for="competenzaSorgente1" class="nascosto">inserisci importo</label>
													</td>
													<td>
														<s:textfield id="competenzaSorgente2" cssClass="input-small soloNumeri decimale-negativo" required="true" name="stanziamentoCompetenzaSorgente2" />
														<label for="competenzaSorgente2" class="nascosto">inserisci importo</label>
													</td>
												</tr>
												<tr>
													<th>Cassa</th>
													<td>
														<s:textfield id="cassaSorgente0" cssClass="input-small soloNumeri decimale-negativo" required="true" name="stanziamentoCassaSorgente0" />
														<label for="cassaSorgente0" class="nascosto">inserisci importo</label>
													</td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
												</tr>
											</table>
										</div>
									</div>
								</div>
							</div>
						</div>
						<s:hidden id="HIDDEN_uidCapitoloSorgente" name="uidCapitoloSorgente" />
						<s:hidden id="HIDDEN_numeroUEBSorgente" name="numeroUEBSorgente" />

						<div class="accordion margin-large" id="accordioncc">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordioncc" href="#collapsec">
										<i class="icon-pencil icon-2x"></i>&nbsp;UEB destinazione
										<span id="SPAN_InformazioniCapitoloDestinazione"></span>
										<span class="icon"></span>
									</a>
								</div>
								<div id="collapsec" class="accordion-body collapse">
									<div class="accordion-inner">
										<div id="tabellaCapitoloDestinazione" class="hide">
											<h4 id="informazioniCapitoloDestinazione">
												<s:if test="uidCapitoloDestinazione != null && uidCapitoloDestinazione != 0">
													Elenco Ueb capitolo <s:property value="numeroCapitolo"/>/<s:property value="numeroArticolo"/>
												</s:if>
											</h4>
											<table class="table table-hover" id="risultatiRicercaCapitoloDestinazione">
												<thead>
													<tr>
														<th></th>
														<th scope="col">Capitolo</th>
														<th scope="col">Stato</th>
														<th scope="col">Classificazione</th>
														<th scope="col">Stanz.competenza</th>
														<th scope="col">Stanz. residuo</th>
														<th scope="col">Stanz.cassa</th>
														<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
														<th scope="col"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
													</tr>
												</thead>
												<tbody>
												</tbody>
												<tfoot>
												</tfoot>
											</table>
											<a class="btn" href="#" id="pulsanteImpostaCapitoloDestinazione">
												seleziona&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_DisponibilitaDestinazione"></i>
											</a>
										</div>
										<div id="divDisponibilitaCapitoloDestinazione" class="hide">
											<h4 id="disponibilitaCapitoloDestinazione">
												<%--UEB 0000004/1/11 - <strong>Disponibilita:</strong> XXXXXX--%>
											</h4>
										</div>
										<s:hidden name="disponibilitaDestinazione" id="HIDDEN_disponibilitaDestinazione" />
									</div>
								</div>
							</div>
						</div>
						<s:hidden id="HIDDEN_uidCapitoloDestinazione" name="uidCapitoloDestinazione" />
						<s:hidden id="HIDDEN_numeroUEBDestinazione" name="numeroUEBDestinazione" />
	
						<s:include value="/jsp/provvedimento/ricercaProvvedimentoCollapse.jsp" />
						<%-- <div class="accordion margin-large" id="accordion5">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion4" href="#collapseProvvedimento">
										<i class="icon-pencil icon-2x"></i>&nbsp;provvedimento&nbsp;<span id="SPAN_InformazioniProvvedimento"></span><span class="icon"></span>
									</a>
								</div>
								<div id="collapseProvvedimento" class="accordion-body collapse">
									<div class="accordion-inner">
										Ricerca come dato aggiuntivo
										<p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto</p>
										<s:include value="/jsp/provvedimento/formRicercaProvvedimento.jsp" />
									</div>
								</div>
							</div>
						</div>
 --%>
						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn btn-link reset">annulla</button>
							<s:submit cssClass="btn btn-primary pull-right" value="salva"/>
						</p>
					</s:form>

				</div>

			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/storniUEB/storni.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_collapse.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/storniUEB/inserisci.js"></script>
</body>
</html>