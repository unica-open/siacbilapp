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
				<s:form cssClass="form-horizontal" action="" id="formInserimentoQuadroEconomico" >
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Quadro Economico</h3>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
								<fieldset class="form-horizontal" id="fieldsetRicerca">
									<h4 class="step-pane">Dati </h4>
									<div class="control-group">
										<label class="control-label" for="codiceQuadroEconomico">Quadro Economico codice</label>
										<div class="controls">
											<s:textfield id="codiceQuadroEconomico" name="quadroEconomico.codice" cssClass="span3" maxlength="20" required="true"/>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="descrizioneQuadroEconomico">Descrizione </label>
										<div class="controls">
											<s:textfield id="descrizioneQuadroEconomico" name="quadroEconomico.descrizione" cssClass="span6" maxlength="500" required="true"/>
											<span class="alRight">
													<label for="statiOperativiQuadroEconomico" class="radio inline">Stato </label>
												</span>
												
												<s:select id="statoOperativoQuadroEconomico" list="statiOperativiQuadroEconomico" name="quadroEconomico.statoOperativoQuadroEconomico" required="false" headerKey="" headerValue=""  listValue="%{codice + ' - ' + descrizione}" />
											
											<button type="button" class="btn btn-primary pull-right" id="pulsanteRicercaQuadroEconomico">
											cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteRicercaQuadroEconomico"></i>
										</button>
										</div>
									</div>
									<div class="Border_line"></div>
									<p>
										<button type="button" class="btn btn-secondary pull-right" id="pulsanteInserisciQuadroEconomicoPadre">
											Inserisci primo livello&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserisciQuadroEconomicoPadre"></i>
										</button>								
									</p>				
									<div class="clear"></div> 
								</fieldset>
								<div id = "containerQuadroEconomicoConFigli">
									<fieldset class="form-horizontal">
										<div class="accordion" id="accordionQuadroEconomico">
											<table class="table-accordion" id="tabellaElencoQuadroEconomico">
												<thead>
													<tr>
														<th></th>
													</tr>
												</thead>
												<tbody>
												</tbody>
											</table>
										</div>
									</fieldset>						
								</div>
							</fieldset>
						</div>
					</div> <!-- chiude lo step content -->
					<s:include value="/jsp/quadroEconomico/insAggQuadroEconomico.jsp" />
					<s:include value="/jsp/quadroEconomico/modaleAnnullamento.jsp" />	
				</s:form>
				<s:include value="/jsp/include/indietro.jsp" />			
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />

	<script type="text/javascript" src="/siacbilapp/js/local/quadroEconomico/gestisci.quadroEconomico.js"></script>
	
</body>
</html>