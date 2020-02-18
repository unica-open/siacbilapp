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
				<s:form cssClass="form-horizontal" action="aggiornaPianoDeiContiFIN_aggiornamento" id="formAggiornaPianoDeiConti" >
					<s:hidden name="figlioNonDiLegge" />
					<s:include value="/jsp/include/messaggi.jsp" />
					
					<h3>Aggiorna Piano Dei Conti</h3>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
							
								<h4 class="nostep-pane">Classe: <s:property value="conto.pianoDeiConti.classePiano.descrizione"/> - <s:property value="conto.pianoDeiConti.classePiano.segnoConto.descrizione"/></h4>
								
								<s:hidden name="uidConto"/>
								<s:hidden name="conto.contoPadre.uid"/>
								<s:hidden name="conto.pianoDeiConti.uid"/>
								<s:hidden name="conto.pianoDeiConti.classePiano.descrizione"/>
								<s:hidden name="conto.pianoDeiConti.classePiano.segnoConto.descrizione"/>
								<s:hidden id="HIDDEN_livello" name="conto.livello"/>
								<s:hidden id="HIDDEN_livelloDiLegge" name="conto.pianoDeiConti.classePiano.livelloDiLegge"/>
								<s:hidden id="HIDDEN_contiFiglioSenzaFigli" name="contiFiglioSenzaFigli"/>
								<s:hidden id="HIDDEN_validitaNellAnnoCorrente" name="validitaNellAnnoCorrente"/>
								<s:hidden id="HIDDEN_figliPresenti" name="figliPresenti"/>
								
								<h4 class="step-pane">Dati Conto</h4>
								<div class="control-group">
									<label class="control-label" for ="codiceConto">Conto *</label>
									<div class="controls">
										<s:textfield id="codiceContoPadre" name="conto.contoPadre.codice" cssClass="span3 input-right" readonly="true"/>&nbsp;
										<s:textfield id="codiceConto" name="codiceContoEditato" cssClass="span3" required="true" disabled="%{!validitaNellAnnoCorrente || figliPresenti}"/>
										<s:if test="%{!validitaNellAnnoCorrente || figliPresenti}">
											<s:hidden name="codiceContoEditato"/>
										</s:if>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="descrizioneConto">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizioneConto" name="conto.descrizione" cssClass="span9" required="true" disabled="%{!validitaNellAnnoCorrente}"/>
										<s:if test="%{!validitaNellAnnoCorrente}">
											<s:hidden name="conto.descrizione"/>
										</s:if>
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
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" disabled="%{!validitaNellAnnoCorrente || figlioNonDiLegge}"/>
										<s:if test="%{!validitaNellAnnoCorrente || figlioNonDiLegge}">
											<s:hidden name="conto.tipoConto.uid"/>
										</s:if>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="contoDiLegge">Conto di legge</label>
									<div class="controls">
										<s:checkbox id="contoDiLegge" name="conto.contoDiLegge" disabled="%{!validitaNellAnnoCorrente || figliPresenti || figlioNonDiLegge}"/>
										<s:if test="%{!validitaNellAnnoCorrente || figliPresenti || figlioNonDiLegge}">
											<s:hidden name="conto.contoDiLegge"/>
										</s:if>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="contoFoglia">Conto foglia</label>
									<div class="controls">
										<s:checkbox id="contoFoglia" name="conto.contoFoglia" disabled="%{figliPresenti || figlioNonDiLegge}"/>
										<s:if test="%{figliPresenti || figlioNonDiLegge}">
											<s:hidden name="conto.contoFoglia"/>
										</s:if>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="attivo">Attivo</label>
									<div class="controls">
										<s:checkbox id="attivo" name="conto.attivo" disabled="%{figlioNonDiLegge}"/>
										<s:if test="%{figlioNonDiLegge}">
											<s:hidden name="conto.attivo"/>
										</s:if>
									</div>
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
												<button type="button" disabled class="btn">Seleziona codifica di bilancio</button>
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
											<s:checkbox id="aPartite" name="conto.contoAPartite" disabled="%{!validitaNellAnnoCorrente || figlioNonDiLegge}"/>
											<s:if test="%{!validitaNellAnnoCorrente || figlioNonDiLegge}">
												<s:hidden name="conto.contoAPartite"/>
											</s:if>
										</div>
									</div>
									
									<div class="control-group hide" id="divCategoriaCespiti">
										<label for="categoriaCespiti" class="control-label">Categoria Cespiti *</label>
										<div class="controls">
											<s:select list="listaCategoriaCespiti" cssClass="span6" id="categoriaCespiti" name="conto.categoriaCespiti.uid" headerKey="" headerValue=""
													listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="figlioNonDiLegge" />
											<s:if test="figlioNonDiLegge">
												<s:hidden name="conto.categoriaCespiti.uid" />
											</s:if>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="pianoDeiContiFinanziario">Codice Conto Finanziario</label>
										<div class="controls">
											<s:textfield id="pianoDeiContiFinanziario" name="conto.elementoPianoDeiConti.codice" cssClass="span6" disabled="%{!validitaNellAnnoCorrente || figlioNonDiLegge}"/>
											<s:if test="%{!validitaNellAnnoCorrente || figlioNonDiLegge}">
												<s:hidden name="conto.elementoPianoDeiConti.codice"/>
											</s:if>
											<span class="radio guidata">
												<a class="btn btn-primary <s:if test='%{(!validitaNellAnnoCorrente && contoFinGuidataDisabled) || figlioNonDiLegge}'>disabled</s:if>" id="pulsanteCompilazioneGuidataContoFIN">
													compilazione guidata
												</a>
											</span>
										</div>
									</div>
									
									<h4 class="step-pane">Collega conto</h4>
									<div class="control-group">
										<label for="tipoLegame" class="control-label">Tipo legame</label>
										<div class="controls">
											<s:select list="listaTipoLegame" cssClass="span6" id="tipoLegame" name="conto.tipoLegame.uid" headerKey="" headerValue=""
													listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="%{!validitaNellAnnoCorrente || figlioNonDiLegge}"/>
										</div>
										<s:if test="%{!validitaNellAnnoCorrente || figlioNonDiLegge}">
											<s:hidden name="conto.tipoLegame.uid"/>
										</s:if>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="contoCollegato">Collega Conto</label>
										<div class="controls">
											<s:textfield id="contoCollegato" name="conto.contoCollegato.codice" cssClass="span6" disabled="%{!validitaNellAnnoCorrente || figlioNonDiLegge}"/>
											<s:if test="%{!validitaNellAnnoCorrente || figlioNonDiLegge}">
												<s:hidden name="conto.contoCollegato.codice"/>
											</s:if>
											<span id="descrizioneContoCollegato"></span>
											<button type="button" class="btn btn-primary pull-right" id="pulsanteCompilazioneGuidataConto" <s:if test="%{!validitaNellAnnoCorrente || figlioNonDiLegge}">disabled</s:if>>
												compilazione guidata
											</button>
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
													<s:textfield id="codiceSoggetto" name="conto.soggetto.codiceSoggetto" cssClass="span2" disabled="%{!validitaNellAnnoCorrente}" />
													<button type="button" class="btn btn-primary pull-right" id="pulsanteCompilazioneGuidataSoggetto" <s:if test="%{!validitaNellAnnoCorrente}">disabled</s:if>>compilazione guidata</button>
												</div>
												<s:if test="%{!validitaNellAnnoCorrente}">
													<s:hidden name="conto.soggetto.codiceSoggetto"></s:hidden>
												</s:if>
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
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaContoFIN.jsp" />
			</div>
		</div>
	</div>
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="${jspath}contabilitaGenerale/ricercaContoFIN.js"></script>
	<script type="text/javascript" src="${jspath}contabilitaGenerale/pianoDeiConti/ztree.js"></script>
	<script type="text/javascript" src="${jspath}contabilitaGenerale/pianoDeiConti/aggiornaPianoDeiContiFIN.js"></script>
	
</body>
</html>