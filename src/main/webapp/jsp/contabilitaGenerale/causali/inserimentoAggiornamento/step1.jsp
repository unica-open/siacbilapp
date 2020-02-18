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
				<s:hidden name="tipoCausaleIntegrata" id="HIDDEN_tipoCausaleIntegrata" />
				
				<s:form cssClass="form-horizontal" action="%{urlStep1}" novalidate="novalidate" id="formCausaleEP">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden name="causaleEP.uid" id="uidCausaleEP" />
					<s:hidden name="causaleEP.statoOperativoCausaleEP" id="statoOperativoCausaleEPCausaleEP" />
					<s:hidden name="causaleEP.loginCreazione" id="loginCreazioneCausaleEP" />
					<s:hidden name="causaleEP.dataCreazione" id="dataCreazioneCausaleEP" />
					<h3><s:property value="denominazioneWizard"/></h3>
					<s:if test="isAggiornamento">
						<h3><s:property value="stringaRiepilogoRichiestaEconomale" /></h3>
					</s:if>
					<div class="wizard" id="MyWizard">
						<ul class="steps">
							<li class="active" data-target="#step1"><span class="badge">1</span>Dati principali<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Dettaglio conti associati<span class="chevron"></span></li>
							<li data-target="#step3"><span class="badge">3</span>Riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<h4>Dati Causale</h4>
								<div class="control-group">
									<label class="control-label" for="tipoCausaleCausaleEP">Tipo causale *</label>
									<div class="controls">
										<s:select list="listaTipoCausale" cssClass="span6" required="true" headerKey="" headerValue="" listValue="%{descrizione}" name="causaleEP.tipoCausale"
											id="tipoCausaleCausaleEP" disabled="%{aggiornamento}" />
										<s:if test="aggiornamento">
											<s:hidden name="causaleEP.tipoCausale" id="HIDDEN_tipoCausaleCausaleEP" />
										</s:if>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codiceCausaleEP">Codice *</label>
									<div class="controls">
										<s:textfield id="codiceCausaleEP" name="causaleEP.codice" cssClass="span6" required="true" maxlength="200" disabled="%{aggiornamento && !causaleStessoAnnoBilancio}" />
										<s:if test="%{aggiornamento && !causaleStessoAnnoBilancio}">
											<s:hidden name="causaleEP.codice" id="HIDDEN_codiceCausaleEP" />
										</s:if>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="descrizioneCausaleEP">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizioneCausaleEP" name="causaleEP.descrizione" cssClass="span9" required="true" maxlength="500" disabled="%{aggiornamento && !causaleStessoAnnoBilancio}" />
										<s:if test="%{aggiornamento && !causaleStessoAnnoBilancio}">
											<s:hidden name="causaleEP.descrizione" id="HIDDEN_descrizioneCausaleEP" />
										</s:if>
									</div>
								</div>
								
								
								<div id="divCampiRaccordo" class="<s:if test="!tipoCausaleDiRaccordo">hide</s:if>">
									<div class="control-group">
										<label class="control-label" for="codiceElementoPianoDeiConti">Codice conto finanziario *</label>
										<div class="controls">
											<s:textfield id="codiceElementoPianoDeiConti" name="elementoPianoDeiConti.codice" cssClass="span6" required="true" maxlength="200" disabled="%{aggiornamento}" />
											<s:if test="%{aggiornamento}">
												<s:hidden name="elementoPianoDeiConti.codice" id="HIDDEN_codiceElementoPianoDeiConti" />
											</s:if>
											<s:if test="%{!aggiornamento || causaleStessoAnnoBilancio}">
												<span class="radio guidata">
													<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataElementoPianoDeiConti">compilazione guidata</button>
												</span>
											</s:if>
										</div>
									</div>
									<s:if test="%{aggiornamento}">
										<div class="control-group">
											<label class="control-label" for="causaleDiDefaultCausaleEP">Causale Di Default</label>
											<div class="controls">
												<s:checkbox id="causaleDiDefaultCausaleEP" name="causaleEP.causaleDiDefault" disabled="%{!causaleStessoAnnoBilancio}"/>
											</div>
											<s:if test="%{!causaleStessoAnnoBilancio}">
												<s:hidden name="causaleEP.causaleDiDefault" id="HIDDEN_causaleDiDefaultCausaleEP" />
											</s:if>
										</div>
									</s:if>
									<s:else>
										<s:hidden name="causaleEP.causaleDiDefault" id="HIDDEN_causaleDiDefaultCausaleEP" />
									</s:else>
									<h4 class="step-pane">Soggetto<span id="descrizioneCompletaSoggetto"><s:property value="denominazioneSoggetto" /></span></h4>
									<div class="control-group">
										<label class="control-label" for="codiceSoggettoSoggetto">Soggetto</label>
										<div class="controls">
										
											<s:textfield id="codiceSoggettoSoggetto" name="soggetto.codiceSoggetto" cssClass="span3" required="true" maxlength="200" disabled="%{aggiornamento && !causaleStessoAnnoBilancio}" />
											<s:if test="%{aggiornamento && !causaleStessoAnnoBilancio}">
												<s:hidden name="soggetto.codiceSoggetto" id="HIDDEN_codiceSoggettoSoggetto" />
											</s:if>
											<s:if test="%{!aggiornamento || causaleStessoAnnoBilancio}">
												<span class="radio guidata">
													<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataSoggetto">compilazione guidata</button>
												</span>
											</s:if>
										</div>
									</div>
									
									<div class="accordion" id="divClassificatori">
										<div class="accordion-group">
											<div class="accordion-heading">
												<a class="accordion-toggle" data-toggle="collapse" data-parent="#divClassificatori" data-target="#collapseClassificatori">
													Classificatori<span class="icon">&nbsp;</span>
												</a>
											</div>
											<div id="collapseClassificatori" class="accordion-body collapse">
												<div class="accordion-inner">
													<s:iterator var="idx" begin="1" end="%{numeroClassificatoriEP}">
														<s:if test="%{#attr['labelClassificatoreEP' + #idx] != null}">
															<div class="control-group">
																<label for="classificatoreEP<s:property value="%{#idx}"/>" class="control-label">
																	<s:property value="%{#attr['labelClassificatoreEP' + #idx]}"/>
																</label>
																<div class="controls">
																	<s:select list="%{#attr['listaClassificatoreEP' + #idx]}" id="classificatoreEP%{#idx}"
																		cssClass="span10" name="%{'classificatoreEP' + #idx + '.uid'}" headerKey="" headerValue=""
																		listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="%{aggiornamento && !causaleStessoAnnoBilancio}" />
																	<s:if test="%{aggiornamento && !causaleStessoAnnoBilancio}">
																		<s:hidden name="%{'classificatoreEP' + #idx + '.uid'}" id="HIDDEN_classificatoreEP%{#idx}" />
																	</s:if>
																</div>
															</div>
														</s:if>
													</s:iterator>
												</div>
											</div>
										</div>
									</div>
								</div>
								
								<h4 class="step-pane">Dati evento</h4>
								<div class="control-group">
									<label class="control-label" for="uidTipoEvento">Tipo evento *</label>
									<div class="controls">
										<s:select list="listaTipoEventoFiltrata" name="tipoEvento.uid" id="uidTipoEvento" headerKey="" headerValue="" cssClass="span6" required="true"
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
<%-- 										<s:if test="%{aggiornamento && !causaleStessoAnnoBilancio}">
											<s:hidden name="tipoEvento.uid" id="HIDDEN_uidTipoEvento" />
										</s:if>
 --%>									
 									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="uidEvento">Evento *</label>
									<div class="controls">
										<s:select list="listaEvento" name="causaleEP.eventi.uid" id="uidEvento" cssClass="span10 multiselect" required="true" multiple="true"
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-placeholder="Seleziona l'evento" value="%{causaleEP.eventi.{uid}}" />
										<%-- <s:if test="%{aggiornamento && !causaleStessoAnnoBilancio}">
											<s:iterator value="causaleEP.eventi" var="ev" status="sts">
												<s:hidden name="causaleEP.eventi.uid" value="%{#ev.uid}" id="HIDDEN_uidEvento%{#sts.count}" />
											</s:iterator>
										</s:if> --%>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<a href="<s:property value="urlAnnullaStep1"/>" class="btn stn-secondary" id="pulsanteAnnullaStep1">annulla</a>
						<s:submit value="prosegui" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaContoFIN.jsp" />
			</div>
		</div>
	</div>
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}contabilitaGenerale/ricercaContoFIN.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}contabilitaGenerale/causali/inserisci.aggiorna.step1.js"></script>
	
</body>
</html>