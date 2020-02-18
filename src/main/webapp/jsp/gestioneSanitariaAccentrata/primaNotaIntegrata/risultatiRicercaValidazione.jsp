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
					<h3>Validazione massiva prime note integrate</h3>
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
											<th><abbr title="Numero">N.</abbr>provvisorio </th>
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
						<span class="pull-right">
							<button type="button" class="btn btn-primary" id="buttonValidaTutto">valida tutto</button>
						</span>
					</p>
				</s:form>
				<s:include value="/jsp/gestioneSanitariaAccentrata/primaNotaIntegrata/include/modaleConfermaValidazioneMassiva.jsp" />
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleDettaglioMovimenti.jsp" />
				
				<s:form cssClass="hide" action="risultatiRicercaValidazionePrimaNotaIntegrataGSA_validaTutto" novalidate="novalidate" id="formInvio">
					<input type="hidden" name="classificatoreGSA.uid" id="invio_classificatoreGSA" />
					<input type="hidden" name="dataRegistrazioneLibroGiornale" id="invio_dataRegistrazioneLibroGiornale" />
				</s:form>
				
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}ztree/ztree_new.js"></script>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/classifgsa/ztree.classifgsa.js"></script>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/primaNotaIntegrata/risultatiRicercaValidazione.js"></script>
	
</body>
</html>