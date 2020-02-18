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
				<s:form action="#" method="post" id="formRisultatiRicercaComponenteCapitolo">
					<s:hidden name="savedDisplayStart" id="HIDDEN_savedDisplayStart" />
					<h3>Elenco Componenti</h3>
					<h4 class="step-pane">Dati di ricerca: <s:property value="stringaRiepilogo"/></h4>
					<fieldset class="form-horizontal">
						<h4><span id="id_num_result" class="num_result"></span></h4>
						<table class="table table-hover tab_left dataTable" id="tabellaRisultatiRicercaComponenteCapitolo">
							<thead>
								<tr>
									<th scope="col">Macrotipo</th>
									<th scope="col">Sottotipo</th>
									<th scope="col">Descrizione</th>
									<th scope="col">Stato</th>
									<th scope="col">Ambito</th>
									<th scope="col">Fonte Di Finanziamento</th>
									<th scope="col">Momento</th>
									<th scope="col">Anno</th>
									<th scope="col">Default</th>
									<th scope="col">Data Inizio</th>
									<th scope="col">Data Fine</th>
									<th scope="col">Azioni</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</fieldset>
					
					<s:include value="/jsp/include/modaleAnnullamentoRisultatiRicerca.jsp">
						<s:param name="href">risultatiRicercaComponenteCapitoloAction_annulla.do</s:param>
					</s:include>
					<p>
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				<input type="hidden" name="uidComponenteCapitolo" id="hiddenUidComponenteCapitolo"/>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}anagraficaComponenti/risultatiRicerca.js"></script>

</body>
</html>