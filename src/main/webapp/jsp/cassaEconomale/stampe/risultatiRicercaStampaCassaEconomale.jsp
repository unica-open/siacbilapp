<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
				<s:form action="#" method="post" id="formRisultatiRicercaCassaEconomale">
					<s:hidden name="tipoDocumentoRendiconto" id="HIDDEN_tipoDocumentoRendiconto" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<h3>Risultati di ricerca stampe</h3>
							<h4><span id="id_num_result" class="num_result"></span></h4>
						</div>
					</div>
					<table class="table table-hover tab_left" id="tabellaStampeCassaEconomale">
						<thead>
							<tr>
								<th>Tipo Stampa</th>
								<th>Periodo</th>
								<th>Al</th>
								<th>Nome file</th>
								<th>Data caricamento</th>
								<s:if test="tipoDocumentoRendiconto">
									<th>Allegato atto</th>
								</s:if>
								<th class="span2 tab_Right">&nbsp;</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cassaEconomale/risultatiRicercaStampe.js"></script>

</body>
</html>