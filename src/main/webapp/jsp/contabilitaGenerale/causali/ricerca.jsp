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
				<s:form cssClass="form-horizontal" action="effettuaRicercaCausaleEPFIN" novalidate="novalidate" id="formCausaleEP">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Ricerca Causale</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<h4 class="step-pane">Dati causale</h4>
								<div class="control-group">
									<label class="control-label" for="tipoCausaleCausaleEP">Tipo causale</label>
									<div class="controls">
										<s:select list="listaTipoCausale" name="causaleEP.tipoCausale" id="tipoCausaleCausaleEP" cssClass="span6" headerKey="" headerValue="" listValue="descrizione" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="statoOperativoCausaleEPCausaleEP">Stato operativo</label>
									<div class="controls">
										<s:select list="listaStatoOperativoCausaleEP" name="causaleEP.statoOperativoCausaleEP" id="statoOperativoCausaleEPCausaleEP" cssClass="span6"
											headerKey="" headerValue="" listValue="descrizione" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codiceCausaleEP">Codice causale</label>
									<div class="controls">
										<s:textfield id="codiceCausaleEP" name="causaleEP.codice" cssClass="span6" maxlength="200" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codiceConto">Codice Conto</label>
									<div class="controls">
										<s:textfield id="codiceConto" name="conto.codice" cssClass="span6" maxlength="200" />
										<span id="descrizioneConto"></span>
										<span class="radio guidata">
											<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataConto">compilazione guidata</button>
										</span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="uidTipoEvento">Tipo evento</label>
									<div class="controls">
										<s:select list="listaTipoEvento" name="tipoEvento.uid" id="uidTipoEvento" cssClass="span6" headerKey="" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="uidEvento">Evento</label>
									<div class="controls">
										<s:select list="listaEvento" name="evento.uid" id="uidEvento" cssClass="span6" headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codiceElementoPianoDeiConti">Codice Conto Finanziario</label>
									<div class="controls">
										<s:textfield id="codiceElementoPianoDeiConti" name="elementoPianoDeiConti.codice" cssClass="span6" maxlength="200" />
										<span class="radio guidata">
											<button type="button" class="btn btn-primary " id="pulsanteCompilazioneGuidataElementoPianoDeiConti">compilazione guidata</button>
										</span>
									</div>
								</div>
								<h4 class="step-pane">Soggetto<span id="descrizioneCompletaSoggetto"><s:property value="denominazioneSoggetto" /></span></h4>
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label class="control-label" for="codiceSoggettoSoggetto">Soggetto</label>
										<div class="controls">
											<s:textfield id="codiceSoggettoSoggetto" name="soggetto.codiceSoggetto" cssClass="span3" required="true" maxlength="200" />
											<span class="radio guidata">
												<button type="button" class="btn btn-primary " id="pulsanteCompilazioneGuidataSoggetto">compilazione guidata</button>
											</span>
										</div>
									</div>
								</fieldset>
							</fieldset>
						</div>
					</div>
					
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset cssClass="btn btn-secondary" value="annulla" />
						<s:submit value="cerca" cssClass="btn btn-primary pull-right" />
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
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/ricercaContoFIN.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/causali/ricerca.js"></script>
	
</body>
</html>