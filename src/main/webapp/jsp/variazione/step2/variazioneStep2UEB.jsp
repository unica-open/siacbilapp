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
			<div class="span12 contentPage">
				<h3>Inserisci Variazione</h3>
				<s:include value="/jsp/include/messaggi.jsp"/>
				<s:form cssClass="form-horizontal" novalidate="novalidate" action="%{definisci.azioneCuiRedirigere}" id="formVariazioneStep2" method="post">
					<s:hidden name="definisci.azioneCuiRedirigere" data-maintain="" />
					<%-- WIZARD --%>
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>Scegli<span class="chevron"></span></li>
							<li data-target="#step2" class="active"><span class="badge">2</span>Definisci<span class="chevron"></span></li>
							<li data-target="#step3"><span class="badge">3</span>Specifica<span class="chevron"></span></li>
							<li data-target="#step4"><span class="badge">4</span>Riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					<%-- /WIZARD --%>
					<div class="step-content">
						<div class="step-pane active" id="step2">
							<div class="control-group">
								<label class="control-label" for="tipoApplicazione">Applicazione *</label>
								<div class="controls">
									<s:select list="definisci.listaTipoApplicazione" cssClass="span10" id="tipoApplicazione" headerKey="" headerValue="" 
											name="definisci.applicazione" listValue="%{descrizione}" required="required" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="descrizioneVariazione">Descrizione *</label>
								<div class="controls">
									<s:textfield id="descrizioneVariazione" placeholder="descrizione" cssClass="span10" name="definisci.descrizioneVariazione" required="true" maxlength="500" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="tipoVariazione">Tipo Variazione *</label>
								<div class="controls">
									<s:select list="definisci.listaTipoVariazione" cssClass="span10" name="definisci.tipoVariazione" id="tipoVariazione" headerKey="" headerValue=""
											listValue="%{codice + ' - ' + descrizione}" />
								</div>
							</div>
							<s:if test="%{definisci.annoVariazioneAbilitato}">
								<div class="control-group">
									<label class="control-label" for="annoDiCompetenzaVariazione">Anno di competenza *</label>
									<div class="controls">
										<s:select list="definisci.listaAnnoVariazione" name="definisci.annoVariazione" id="annoDiCompetenzaVariazione" headerKey="" headerValue="" />
										<span class="help-inline">Stanziamenti da variare</span>
									</div>
								</div>
							</s:if><s:else>
								<s:hidden name="definisci.annoVariazione" value="%{annoEsercizioInt}" data-maintain="" />
							</s:else>
							<div class="control-group">
								<label class="control-label" for="caricaResidui">Carica residui </label>
								<div class="controls">
								<%-- <pre><s:property value="definisci.showCaricaResidui" /></pre> --%>
									<s:if test="definisci.showCaricaResidui">
										<s:checkbox id="caricaResidui" name="definisci.caricaResidui" />
									</s:if><s:else>
										<s:checkbox id="caricaResidui" disabled ="true" name="definisci.caricaResidui" />
									</s:else>	
								</div>
							</div>
							<s:include value="/jsp/provvedimento/ricercaProvvedimentoCollapse.jsp" />
							<s:include value="/jsp/provvedimento/ricercaProvvedimentoAggiuntivoCollapse.jsp" />	
							
							<p class="note margin-large">
								Occorre scegliere a chi inviare, successivamente in fase di aggiornamento, la variazione che si sta inserendo.
							</p>
							<div class="control-group">
								<span class="control-label wider">Variazione con approvazione di <s:property value="definisci.etichettaConsiglio"/>&nbsp;</span>
								<div class="controls">
									<label class="radio inline">
										<input type="radio" name="definisci.variazioneAOrganoAmministativo" id="variazioneAOrganoAmministativoRadio1" value="true" <s:if test="%{definisci.variazioneAOrganoAmministativo}">checked="checked"</s:if>> s&iacute;
									</label>
									<label class="radio inline">
										<input type="radio" name="definisci.variazioneAOrganoAmministativo" id="variazioneAOrganoAmministativoRadio2" value="false" <s:if test="%{!definisci.variazioneAOrganoAmministativo}">checked="checked"</s:if>> no
									</label>
								</div>
							</div>
							<div class="control-group">
								<span class="control-label wider">Variazione con approvazione di <s:property value="etichettaConsiglio"/>&nbsp;</span>
								<div class="controls">
									<label class="radio inline">
										<input type="radio" name="definisci.variazioneAOrganoLegislativo" id="variazioneAOrganoLegislativoRadio1" value="true" <s:if test="%{definisci.variazioneAOrganoLegislativo}">checked="checked"</s:if>> s&iacute;
									</label>
									<label class="radio inline">
										<input type="radio" name="definisci.variazioneAOrganoLegislativo" id="variazioneAOrganoLegislativoRadio2" value="false" <s:if test="%{!definisci.variazioneAOrganoLegislativo}">checked="checked"</s:if>> no
									</label>
								</div>
							</div>

							<div class="margin-large">
								<s:include value="/jsp/include/indietro.jsp" />
								<button type="button" class="btn btn-link reset">annulla</button>
								<s:submit cssClass="btn btn-primary pull-right" value="prosegui"/>
							</div>
						</div>
					</div>
					<s:include value="/jsp/variazione/step2/modaleConfermaNoProvvedimento.jsp" />
				</s:form>				
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp"/>
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/ztree/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricercaProvvedimento_collapse.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/variazioni.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/variazioni.step2.js"></script>
</body>
</html>