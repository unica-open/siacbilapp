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
				<s:form cssClass="form-horizontal" action="#" novalidate="novalidate" id="formPrimaNotaLibera">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Valida prime note integrate</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<h4 class="step-pane">
									<span id="num_result"></span>
								</h4>
								<table class="table table-hover tab_left" id="tabellaRisultatiRicerca">
									<thead>
										<tr>
											<th><abbr title="Numero">N.</abbr>movimento finanziario</th>
											<th><abbr title="Numero">N.</abbr>provvisorio</th>
											<th>Descrizione</th>
											<th>Stato</th>
											<th><abbr title="Numero">N.</abbr>definitivo</th>
											<th>Data registrazione definitiva</th>
											<th>Causale</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleDettaglioMovimenti.jsp" />
				
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/primaNotaIntegrata/risultatiRicercaValidazione.js"></script>
	
</body>
</html>