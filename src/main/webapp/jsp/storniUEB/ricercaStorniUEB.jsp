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
			<div class="span12">
				<div class="contentPage">
					
					<s:form action="effettuaRicercaConOperazioniStornoUEB" novalidate="novalidate">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3>Ricerca storni UEB</h3>
						<div class="step-content">
							<p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>
							<div class="fieldset-body">
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label class="control-label" for="numeroStorno">Storno</label>
										<div class="controls">
											<s:textfield id="numeroStorno" cssClass="lbTextSmall span2 soloNumeri" name="numeroStorno" maxlength="4" placeholder="storno" />
										</div>
									</div>
									<div class="control-group">
										<span class="control-label">&nbsp;</span>
										<div class="controls">
											<label class="radio inline">
												<input type="radio" name="tipoCapitolo" id="tipoCapitoloRadio1" value="CAPITOLO_ENTRATA_GESTIONE" <s:if test='%{tipoCapitolo.toString() eq "CAPITOLO_ENTRATA_GESTIONE"}'>checked="checked"</s:if>>&nbsp;Entrata
											</label>
											<label class="radio inline">
												<input type="radio" name="tipoCapitolo" id="tipoCapitoloRadio2" value="CAPITOLO_USCITA_GESTIONE" <s:if test='%{tipoCapitolo.toString() eq "CAPITOLO_USCITA_GESTIONE"}'>checked="checked"</s:if>>&nbsp;Spesa
											</label>
										</div>
									</div>
								</fieldset>
							</div>
							<div class="accordion" id="accordion">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseb" id="ModaleSorgente">
											<i class="icon-pencil icon-2x"></i>&nbsp;UEB sorgente:&nbsp;<span id="informazioniUEBSorgente">
												<s:if test="%{numeroCapitoloSorgente != null}">:&nbsp;<s:property value="numeroCapitoloSorgente"/>&nbsp;/&nbsp;<s:property value="numeroArticoloSorgente" />
													<s:if test="%{numeroUEBSorgente != null}">&nbsp;/&nbsp;<s:property value="numeroUEBSorgente"/></s:if>
												 </s:if>
											</span>
											<span class="icon"></span>
										</a>
									</div>
									<div id="collapseb" class="accordion-body collapse">
										<div class="accordion-inner">
											<h4>Seleziona il capitolo Sorgente</h4>
											<fieldset class="form-horizontal">
												<div class="control-group">
													<label class="control-label" for="annoCapitoloSorgente">Anno</label>
													<div class="controls">
														<s:textfield id="annoCapitoloSorgente" cssClass="lbTextSmall span1" disabled="true" value="%{annoEsercizio}" />
														<s:hidden name="annoEsercizioInt" id="HIDDEN_annoCapitoloSorgente" data-maintain="" />
														<span class="al">
															<label class="radio inline" for="numeroCapitoloSorgente">Capitolo *</label>
														</span>
														<s:textfield id="numeroCapitoloSorgente" cssClass="lbTextSmall span2 soloNumeri" name="numeroCapitoloSorgente" />
														<span class="al">
															<label class="radio inline" for="numeroArticoloSorgente">Articolo *</label>
														</span>
														<s:textfield id="numeroArticoloSorgente" cssClass="lbTextSmall span2 soloNumeri" name="numeroArticoloSorgente" />
														<span class="al">
															<label class="radio inline" for="numeroUEBSorgente">UEB</label>
														</span>
														<s:textfield id="numeroUEBSorgente" cssClass="lbTextSmall span1 soloNumeri" name="numeroUEBSorgente" />
														<span class="al">
	                       		 							<label class="radio inline" for="statoCapitoloSorgente">Stato</label>
	                        							</span>
	                        							<s:select list="listaStatoOperativoElementoDiBilancio" id="statoCapitoloSorgente" cssClass="lbTextSmall span2" name="statoOperativoSorgente" headerKey="" headerValue="" />
														<a class="btn btn-primary" href="#" id="pulsanteRicercaCapitoloQualificatoSorgente">
															<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_CapitoloSorgente"></i>
														</a>
													</div>
												</div>
											</fieldset>
											<div id="tabellaCapitoloSorgente" class="hide">
												<h4 id="informazioniCapitoloSorgente">
													<s:if test="uidCapitoloSorgente != null">
														Elenco Ueb capitolo <s:property value="numeroCapitoloSorgente"/>/<s:property value="numeroArticoloSorgente"/>
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
												<a class="btn" href="#" id="pulsanteImpostaCapitoloQualificatoSorgente">
													seleziona
												</a>
												<s:hidden id="HIDDEN_uidCapitoloSorgente" name="uidCapitoloSorgente" />
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="accordion margin-large" id="accordiond">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordiond" href="#collapsedest" id="ModaleDestinazione">
											<i class="icon-pencil icon-2x"></i>&nbsp;UEB destinazione:&nbsp;<span id="informazioniUEBDestinazione">
												<s:if test="%{numeroCapitoloDestinazione != null}">:&nbsp;<s:property value="numeroCapitoloDestinazione"/>&nbsp;/&nbsp;<s:property value="numeroArticoloDestinazione" />
													<s:if test="%{numeroUEBDestinazione != null}">&nbsp;/&nbsp;<s:property value="numeroUEBDestinazione"/></s:if>
												</s:if>
											</span>
											<span class="icon"></span>
										</a>
									</div>
									<div id="collapsedest" class="accordion-body collapse">
										<div class="accordion-inner">
											<h4>Seleziona il capitolo Destinazione</h4>
											<fieldset class="form-horizontal">
												<div class="control-group">
													<label class="control-label" for="annoCapitoloDestinazione">Anno</label>
													<div class="controls">
														<s:textfield id="annoCapitoloDestinazione" cssClass="lbTextSmall span1" value="%{annoEsercizio}" disabled="true" />
														<span class="al">
															<label class="radio inline" for="numeroCapitoloDestinazione">Capitolo *</label>
														</span>
														<s:textfield id="numeroCapitoloDestinazione" cssClass="lbTextSmall span2 soloNumeri" name="numeroCapitoloDestinazione" />
														<span class="al">
															<label class="radio inline" for="numeroArticoloDestinazione">Articolo *</label>
														</span>
														<s:textfield id="numeroArticoloDestinazione" cssClass="lbTextSmall span2 soloNumeri" name="numeroArticoloDestinazione" />
														<span class="al">
															<label class="radio inline" for="numeroUEBDestinazione">UEB</label>
														</span>
														<s:textfield id="numeroUEBDestinazione" cssClass="lbTextSmall span1 soloNumeri" name="numeroUEBDestinazione" />
														<span class="al">
	                       		 							<label class="radio inline" for="statoCapitoloDestinazione">Stato</label>
	                        							</span>
	                        							<s:select list="listaStatoOperativoElementoDiBilancio" id="statoCapitoloDestinazione" cssClass="lbTextSmall span2" name="statoOperativoDestinazione" headerKey="" headerValue="" />
														<a class="btn btn-primary" href="#" id="pulsanteRicercaCapitoloQualificatoDestinazione">
															<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_CapitoloDestinazione"></i>
														</a>
													</div>
												</div>
											</fieldset>
											<div id="tabellaCapitoloDestinazione" class="hide">
												<h4 id="informazioniCapitoloDestinazione">
													<s:if test="uidCapitoloDestinazione != null">
														Elenco Ueb capitolo <s:property value="numeroCapitoloDestinazione"/>/<s:property value="numeroArticoloDestinazione"/>
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
												<a class="btn" href="#" id="pulsanteImpostaCapitoloQualificatoDestinazione">
													selezione
												</a>
												<s:hidden id="HIDDEN_uidCapitoloDestinazione" name="uidCapitoloDestinazione" />
											</div>
										</div>
									</div>
								</div>
							</div>
							
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
							</div> --%>
							<p class="margin-large">
								<s:include value="/jsp/include/indietro.jsp" />
								<button type="button" class="btn btn-link reset">annulla</button>
								<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
							</p>
						</div>
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
	<script type="text/javascript" src="/siacbilapp/js/local/storniUEB/ricerca.js"></script>
</body>
</html>