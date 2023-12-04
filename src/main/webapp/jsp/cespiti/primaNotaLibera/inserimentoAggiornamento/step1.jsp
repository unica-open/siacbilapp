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
				<s:form cssClass="form-horizontal" action="%{urlStep1}" novalidate="novalidate" id="formInserisciPrimaNotaLibera">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden name="primaNotaLibera.uid" id="uidPrimaNotaLibera" />
					<s:hidden name="primaNotaLibera.numero" id="numeroPrimaNotaLibera" />
					<s:hidden name="primaNotaLibera.tipoCausale" id="tipoCausalePrimaNotaLibera" />
					<s:hidden name="primaNotaLibera.statoOperativoPrimaNota" id="statoOperativoPrimaNotaPrimaNotaLibera" />
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
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-evento="" disabled="%{aggiornamento}"/>
										<s:if test="%{aggiornamento}">
											<s:hidden name="evento.uid" />
										</s:if>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="uidCausaleEP">Causale *</label>
									<div class="controls">
										<s:select list="listaCausaleEP" id="uidCausaleEP" name="causaleEP.uid"
											cssClass="span6" headerKey="" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-causale-EP="" disabled="%{aggiornamento}"/>
										<s:if test="%{aggiornamento}">
											<s:hidden name="causaleEP.uid" />
										</s:if>
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
						</div>
					</div>
					
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:a href="%{urlAnnullaStep1}" cssClass="btn btn-secondary" id="pulsanteAnnullaStep1">annulla</s:a>
						<s:submit value="prosegui" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/primaNotaLibera/inserisci.aggiorna.step1.js"></script>

</body>
</html>