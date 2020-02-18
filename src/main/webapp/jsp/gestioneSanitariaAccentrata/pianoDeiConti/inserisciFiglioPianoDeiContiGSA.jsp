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
				<s:form cssClass="form-horizontal" action="inserisciFiglioPianoDeiContiGSA_inserimento" id="formInserimentoFiglioPianoDeiConti" >
					<s:hidden name="figlioNonDiLegge" />
					<s:include value="/jsp/include/messaggi.jsp" />
					
					<h3>Inserisci Figlio</h3>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
							
							<h4 class="nostep-pane">Classe: <s:property value="contoPadre.pianoDeiConti.classePiano.descrizione"/> - <s:property value="contoPadre.pianoDeiConti.classePiano.segnoConto.descrizione"/></h4> 
							<h4 class="nostep-pane">Conto padre: <s:property value="contoPadre.codice"/> - <s:property value="contoPadre.descrizione"/></h4>
							
							<s:hidden name="uidConto"/>
							<s:hidden name="contoPadre.uid"/>
							<s:hidden name="contoPadre.pianoDeiConti.uid"/>
							<s:hidden id="HIDDEN_livelloPadre" name="contoPadre.livello"/>
							<s:hidden id="HIDDEN_livelloDiLegge" name="contoPadre.pianoDeiConti.classePiano.livelloDiLegge"/>
							<s:hidden id="HIDDEN_contiFiglioSenzaFigli" name="contiFiglioSenzaFigli"/>
								<h4 class="step-pane">Dati Figlio</h4>
								<div class="control-group">
									<label class="control-label" for ="codiceConto">Conto *</label>
									<div class="controls">
										<s:textfield id="codiceContoPadre" name="contoPadre.codice" cssClass="span3 input-right" readonly="true"/>&nbsp;
										<s:textfield id="codiceConto" name="codiceContoEditato" cssClass="span3" required="true"/>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="descrizioneConto">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizioneConto" name="conto.descrizione" cssClass="span9" required="true"/>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="codificaInternaConto">Codifica interna</label>
									<div class="controls">
										<s:textfield id="codificaInternaConto" name="conto.codiceInterno" cssClass="span6"/>
									</div>
								</div>
								
								<div class="control-group">
									<label for="tipoConto" class="control-label" id="labelTipoConto">Tipo *</label>
									<s:hidden id="uidTipoCespiti" name="uidTipoCespiti" />
									<s:hidden id="uidTipoGenerico" name="uidTipoGenerico" />
									<div class="controls">
										<s:select list="listaTipoConto" cssClass="span6" id="tipoConto" name="conto.tipoConto.uid"
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" disabled="figlioNonDiLegge"/>
										<s:if test="figlioNonDiLegge">
												<s:hidden name="conto.tipoConto.uid" />
										</s:if>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="contoDiLegge">Conto di legge</label>
									<div class="controls">
										<s:checkbox id="contoDiLegge" name="conto.contoDiLegge" disabled="figlioNonDiLegge"/>
										<s:if test="figlioNonDiLegge">
												<s:hidden name="conto.contoDiLegge" />
										</s:if>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="contoFoglia">Conto foglia</label>
									<div class="controls">
										<s:checkbox id="contoFoglia" name="conto.contoFoglia" disabled="figlioNonDiLegge"/>
									</div>
									<s:if test="figlioNonDiLegge">
										<s:hidden name="conto.contoFoglia"/>
									</s:if>
								</div>
								
								<div id="campiAggiuntivi" class="hide">
									<h4 class="step-pane">Dati aggiuntivi</h4>
									
									<div class="control-group">
										<label for="codiceBilancio" class="control-label">
											Codifica di bilancio
										</label>
										<div class="controls">
											<s:hidden type="hidden" id="HIDDEN_CodiceBilancioUid" name="codiceBilancio.uid"/>
											<s:hidden id="HIDDEN_CodiceBilancioStringa" name="codiceBilancio.descrizione" />
											<s:if test="!figlioNonDiLegge">
												<a href="#codBilancio" class="btn" id="bottoneCodBilancio" data-toggle="modal">
													Seleziona codifica di bilancio &nbsp;
													<i class="icon-spin icon-refresh spinner" id="SPINNER_CodiceBilancio"></i>
												</a>
												<div id="codBilancio" class="modal hide fade" tabindex="-1">
													<div class="modal-header">
														<button type="button" class="close" data-dismiss="modal">&times;</button>
														<h3 id="myModalLabel2">
															Codifica Di Bilancio
														</h3>
													</div>
													<div class="modal-body">
														<ul id="treeCodBilancio" class="ztree"></ul>
													</div>
													<div class="modal-footer">
														<button id="deselezionaCodiceBilancio" class="btn">Deseleziona</button>
														<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
													</div>
												</div>
											</s:if><s:else>
												<button type="button" disabled class="btn">
													Seleziona codifica di bilancio
												</button>
											</s:else>
											<span id="SPAN_CodiceBilancio"> &nbsp;
												<s:if test="%{codiceBilancio != null}">
													<s:property value="codiceBilancio.descrizione"/>
												</s:if><s:else>
													 Nessuna Codifica di Bilancio selezionata 
												</s:else>
											</span>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="aPartite">A partite</label>
										<div class="controls">
											<s:checkbox id="aPartite" name="conto.contoAPartite" disabled="figlioNonDiLegge" />
											<s:if test="figlioNonDiLegge">
												<s:hidden name="conto.contoAPartire" />
											</s:if>
										</div>
									</div>
									
									<div class="control-group hide" id="divCategoriaCespiti">
										<label for="categoriaCespiti" class="control-label">Categoria Cespiti *</label>
										<div class="controls">
											<s:select list="listaCategoriaCespiti" cssClass="span6" id="categoriaCespiti" name="conto.categoriaCespiti.uid" headerKey="0" headerValue=""
													listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" disabled="figlioNonDiLegge" />
											<s:if test="figlioNonDiLegge">
												<s:hidden name="conto.categoriaCespiti.uid" />
											</s:if>
										</div>
									</div>
									
									<h4 class="step-pane">Collega conto</h4>
									<div class="control-group">
										<label for="tipoLegame" class="control-label">Tipo legame</label>
										<div class="controls">
											<s:select list="listaTipoLegame" cssClass="span6" id="tipoLegame" name="conto.tipoLegame.uid" headerKey="0" headerValue=""
													listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" disabled="figlioNonDiLegge" />
											<s:if test="figlioNonDiLegge">
												<s:hidden name="conto.tipoLegame.uid" />
											</s:if>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="contoCollegato">Collega Conto</label>
										<div class="controls">
											<s:textfield id="contoCollegato" name="conto.contoCollegato.codice" cssClass="span6" disabled="figlioNonDiLegge" />
											<s:if test="figlioNonDiLegge">
												<s:hidden name="conto.contoCollegato.codice" />
											</s:if>
											<span id="descrizioneContoCollegato"></span>
											<button type="button" class="btn btn-primary pull-right" id="pulsanteCompilazioneGuidataConto" <s:if test="figlioNonDiLegge">disabled</s:if>>compilazione guidata</button>
										</div>
									</div>
									
									<div id="datiSoggetto">
										<h4 class="step-pane">Soggetto:
											<span class="datiRIFSoggetto" id="datiRiferimentoSoggettoSpan">
											</span>
										</h4>
										
										<fieldset class="form-horizontal">
											<div class="control-group">
												<label class="control-label" for="codiceSoggetto">Codice </label>
												<div class="controls">
													<s:textfield id="codiceSoggetto" name="conto.soggetto.codiceSoggetto" cssClass="span2" />
													<button type="button" class="btn btn-primary pull-right" id="pulsanteCompilazioneGuidataSoggetto">compilazione guidata</button>
												</div>
											</div>
										</fieldset>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:submit cssClass="btn btn-primary pull-right" value="salva" />
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
			</div>
		</div>
	</div>
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/pianoDeiConti/inserisciFiglioPianoDeiContiGSA.js"></script>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/pianoDeiConti/ztree.js"></script>
</body>
</html>