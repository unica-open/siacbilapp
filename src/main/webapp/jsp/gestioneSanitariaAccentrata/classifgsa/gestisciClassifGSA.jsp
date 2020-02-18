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
				<s:form cssClass="form-horizontal" action="" id="formInserimentoClassificatoreGSA" >
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Inserisci  classificatore GSA</h3>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
								<fieldset class="form-horizontal" id="fieldsetRicerca">
									<h4 class="step-pane">Dati Classificatore</h4>
									<div class="control-group">
										<label class="control-label" for="codiceClassificatore">Classificatore (liv. 1) </label>
										<div class="controls">
											<s:textfield id="codiceClassificatore" name="classificatoreGSA.codice" cssClass="span6" required="true"/>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="descrizioneClassificatore">Descrizione </label>
										<div class="controls">
											<s:textfield id="descrizioneClassificatore" name="classificatoreGSA.descrizione" cssClass="span6" required="true"/>
											<span class="alRight">
													<label for="statiOperativiClassificatoreGSA" class="radio inline">Stato </label>
												</span>
												<s:select id="statoOperativoDocumento" list="statiOperativiClassificatoreGSA" name="classificatoreGSA.statoOperativoClassificatoreGSA"
														required="false" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
											<button type="button" class="btn btn-primary pull-right" id="pulsanteRicercaClassificatoreGSA">
											cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteRicercaClassificatoreGSA"></i>
										</button>
										</div>
									</div>
									<div class="Border_line"></div>
									<p>
										<button type="button" class="btn btn-secondary pull-right" id="pulsanteInserisciClassificatorePadreGSA">
											Inserisci primo livello&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserisciClassificatorePadreGSA"></i>
										</button>								
									</p>				
									<div class="clear"></div> 
								</fieldset>
								<div id = "containerClassificatoriGSAConFigli">
									<fieldset class="form-horizontal">
										<div class="accordion" id="accordionClassifGSA">
											<table class="table-accordion" id="tabellaElencoClassificatoreGSA">
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
					<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleAnnullamento.jsp" />
					<s:include value="/jsp/gestioneSanitariaAccentrata/classifgsa/include/insAggClassifGSA.jsp" />
				</s:form>
				<s:include value="/jsp/include/indietro.jsp" />			
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />

	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/classifgsa/gestisci.classifGSA.js"></script>
	
</body>
</html>