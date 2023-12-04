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
				<s:form cssClass="form-horizontal" action="%{urlStep1}" novalidate="novalidate" id="formInserisciPrimaNotaIntegrataManuale">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden name="primaNotaLibera.uid" id="uidPrimaNotaLibera" />
					<s:hidden name="primaNotaLibera.numero" id="numeroPrimaNotaLibera" />
					<s:hidden name="primaNotaLibera.tipoCausale" id="tipoCausalePrimaNotaLibera" />
					<s:hidden name="primaNotaLibera.statoOperativoPrimaNota" id="statoOperativoPrimaNotaPrimaNotaLibera" />
					<s:hidden name="primaNotaLibera.classificatoreGSA.uid" id="classificatoreGSAPrimaNotaLibera" />
					<s:hidden name="primaNotaLibera.numeroRegistrazioneLibroGiornale" id="numeroRegistrazioneLibroGiornalePrimaNotaLibera" />
					<s:hidden name="primaNotaLibera.dataRegistrazioneLibroGiornale" id="dataRegistrazioneLibroGiornalePrimaNotaLibera" />
					<s:hidden name="ambito" id="ambito" />
					<h3><s:property value="denominazioneWizard"/></h3>
					
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="active"><span class="badge">1</span>inserimento prima nota<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>dettaglio scritture<span class="chevron"></span></li>
							<li data-target="#step3"><span class="badge">3</span>riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
								<h4 class="step-pane">Dati Causale</h4>
								<div class="control-group">
									<label class="control-label" for="uidEvento">Evento</label>
									<div class="controls">
										<s:select list="listaEvento" id="uidEvento" name="evento.uid"
											cssClass="span6" headerKey="" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-evento="" disabled="true"/>
										<s:hidden name="evento.uid" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="uidCausaleEP">Causale *</label>
									<div class="controls">
										<s:select list="listaCausaleEP" id="uidCausaleEP" name="causaleEP.uid"
											cssClass="span6" headerKey="" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-causale-EP="" disabled="true"/>
										<s:hidden name="causaleEP.uid" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Data registrazione *</label>
									<div class="controls">
										<s:textfield id="dataRegistrazionePrimaNotaLibera" name="primaNotaLibera.dataRegistrazione" cssClass="span2 datepicker" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizionePrimaNotaLibera" name="primaNotaLibera.descrizione" cssClass="span9" required="true" maxlength="500" />
									</div>
								</div>
								
							</fieldset>
							<div class="accordion" id="accordionMovFin">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a class="accordion-toggle" data-toggle="collapse" id="ModalAltriDati" data-parent="#accordionMovFin" href="#collapseMovFin">
											Collegamento Movimento Finanziario<span class="icon">&nbsp;</span>
										</a>
									</div>
									<div id="collapseMovFin" class="accordion-body collapse in">
										<div class="accordion-inner">
										
											<fieldset class="form-horizontal" data-overlay>
												<div class="control-group">
													<label class="control-label">&nbsp;</label>
													<div class="controls">
														<label class="radio inline">
															<input type="radio" value="Impegno" name="tipoMovimento"
																<s:if test='%{"Impegno".equals(tipoMovimento)}'>checked</s:if>
																<s:if test='aggiornamento'>disabled</s:if>>
																Impegno
														</label>
														<span class="alLeft">
															<label class="radio inline">
																<input type="radio" value="Accertamento" name="tipoMovimento"
																	<s:if test='%{"Accertamento".equals(tipoMovimento)}'>checked</s:if>
																	<s:if test='aggiornamento'>disabled</s:if>>
																	Accertamento
															</label>
														</span>
													</div>
												</div>
												<div class="control-group">
													<label class="control-label" for="annoMovimento">Anno *</label>
													<div class="controls">
														<s:textfield id="annoMovimento" name="annoMovimentoGestione" disabled="true" required="true" cssClass="span1 soloNumeri" />
														<span class="al">
															<label class="radio inline" for="numeroMovimentoGestione">Numero *</label>
														</span>
														<s:textfield id="numeroMovimentoGestione" name="numeroMovimentoGestione" disabled="true" required="true" cssClass="span2 soloNumeri" />
														<span class="al">
															<label class="radio inline" for="numeroSubmovimentoGestione">Sub</label>
														</span>
														<s:textfield id="numeroSubmovimentoGestione" name="numeroSubmovimentoGestione" disabled="true" required="true" cssClass="span2 soloNumeri" />
														<s:if test="!aggiornamento">
															<span class="radio guidata">
																<button type="button" class="btn btn-primary disabled" id="pulsanteCompilazioneGuidataMovimentoGestione">compilazione guidata</button>
															</span>
														</s:if>
													</div>
												</div>
											</fieldset>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:a href="%{urlAnnullaStep1}" cssClass="btn btn-secondary" id="pulsanteAnnullaStep1">annulla</s:a>
						<s:submit value="prosegui" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
				<s:include value="/jsp/movimentoGestione/modaleAccertamento.jsp" />
				<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" />
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaAccertamentoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/gestioneSanitariaAccentrata/primaNotaIntegrataManuale/inserisci.aggiorna.step1.js"></script>

</body>
</html>