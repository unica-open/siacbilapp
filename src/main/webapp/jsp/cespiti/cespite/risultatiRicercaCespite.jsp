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
			<div class="span12 ">
				<div class="contentPage">
					<s:form id="formInserisciTipoBene" cssClass="form-horizontal" novalidate="novalidate" action="ricercaTipoBeneCespite_salva">
						<h3 id="titolo">Risultati ricerca anagrafica cespite</h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
						<br/>
						<fieldset class="form-horizontal">
							<h4><span id="id_num_result" class="num_result"></span></h4>
							<table class="table table-hover tab_left dataTable" id="tabellaRisultatiRicercaCespite">
								<thead>
									<tr>
										<th>Codice</th>
										<th>Descrizione</th>
										<th>Tipo Bene</th>
										<th>Classificazione</th>
										<th>Inventario</th>
										<th>Attivo</th>
										<th>Donazione/Rinvenimento</th>
										<th>Azioni</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</fieldset>
						</div>
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/cespiti/include/modaleEliminazione.jsp"/>
	<s:include value="/jsp/cespiti/include/modaleAnnullamento.jsp"/>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/cespite/risultatiRicercaCespite.js"></script>
</body>
</html>