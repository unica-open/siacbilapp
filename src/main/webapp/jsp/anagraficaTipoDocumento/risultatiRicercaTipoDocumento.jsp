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
		<s:include value="/jsp/include/messaggi.jsp" />
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form action="#" method="post" id="formRisultatiRicercaTipoDocumento">
					<s:hidden name="savedDisplayStart" id="HIDDEN_savedDisplayStart" />
					<h3>Elenco Tipo Documento FEL - Contabilia</h3>
					<h4 class="step-pane">Dati di ricerca: <s:property value="stringaRiepilogo"/></h4>
					<fieldset class="form-horizontal">
						<h4><span id="id_num_result" class="num_result"></span></h4>
						<table class="table table-hover tab_left dataTable" id="tabellaRisultatiRicercaTipoDocumento">
							<thead>
								<tr>
									<th scope="col">Codice FEL</th>
									<th scope="col">Descrizione</th>
									<th scope="col">Tipo documento Contabilia Spesa</th>
									<th scope="col">Tipo documento Contabilia Entrata</th>
									<th scope="col">Azioni</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</fieldset>
					
					<s:include value="/jsp/include/modaleEliminaRisultatiRicerca.jsp">
						<s:param name="href">risultatiRicercaTipoDocumentoAction_annulla.do</s:param>
					</s:include>
					<p>
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				<input type="hidden" name="uidTipoDocumento" id="hiddenUidTipoDocumento"/>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/anagraficaTipoDocumento/risultatiRicerca.js"></script>

</body>
</html>