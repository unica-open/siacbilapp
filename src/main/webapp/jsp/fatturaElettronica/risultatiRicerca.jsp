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
				<s:form cssClass="form-horizontal" action="#" novalidate="novalidate" id="formFatturaElettronica">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Risultati ricerca FEL</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<h4 class="step-pane">
									<span id="num_result"></span>
								</h4>
								<table class="table table-hover tab_left" id="tabellaRisultatiRicerca">
									<thead>
										<tr>
											<th>Fornitore</th>
											<th>Data emissione</th>
											<th>Data ricezione</th>
											<th>Numero documento</th>
											<th>Tipo</th>
											<th>Data acquisizione</th>
											<th>Stato acquisizione</th>
											<th class="tab_Right">Importo lordo</th>
											<th class="tab_Right span2">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</fieldset>
						</div>
						<%-- SIAC-7571 --%>
						<s:include value="/jsp/include/modaleConfermaProsecuzioneSuAzione.jsp" />
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
				<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
				<s:include value="/jsp/fatturaElettronica/modaleSospensione.jsp" />
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/fatturaElettronica/risultatiRicerca.js"></script>
	
</body>
</html>