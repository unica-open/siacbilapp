<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-json-tags" prefix="json"%>
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
					<form method="get" action="">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3>Consultazione Variazioni</h3>
						<h4>Variazione</h4>
							<dl class="dl-horizontal">
								<dt>Num. variazione</dt><dd><s:property value="numeroVariazione"/>&nbsp;</dd>
								<dt>Tipo</dt><dd><s:property value="tipoVariazione.codice"/> - <s:property value="tipoVariazione.descrizione"/>&nbsp;</dd>
								<dt>Applicazione</dt><dd><s:property value="applicazioneVariazione"/>&nbsp;</dd>
								<dt>Descrizione</dt><dd><s:property value="descrizioneVariazione"/>&nbsp;</dd>
								<dt>Stato</dt><dd><s:property value="elementoStatoOperativoVariazione.descrizione"/>&nbsp;</dd>
								<dt>Note</dt><dd><s:property value="noteVariazione"/>&nbsp;</dd>
							</dl>
							 
						<h4>Provvedimento</h4>
						<dl class="dl-horizontal">
							<dt>Tipo</dt><dd><s:property value="tipoProvvedimento"/>&nbsp;</dd>
							<dt>Anno</dt><dd><s:property value="annoProvvedimento"/>&nbsp;</dd>
							<dt>Numero</dt><dd><s:property value="numeroProvvedimento"/>&nbsp;</dd>
							<dt>Struttura</dt><dd><s:property value="strutturaProvvedimento"/>&nbsp;</dd>
							<dt>Oggetto</dt><dd><s:property value="oggettoProvvedimento"/>&nbsp;</dd>
						</dl>
						
						 <!-- TABELLE RIEPILOGO con azioni -->
						<table class="table table-condensed table-hover table-bordered" id="oggettiTrovati" summary="...." >
							<thead>
								<tr>
									<th scope="col">Capitolo</th>
									<th scope="col">Codifiche</th>
									<th scope="col">Piano dei Conti</th>
									<th scope="col">Tipo finanziamento</th>
									<th scope="col">Strutt. Amm. Contabile responsabile</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="listaCapitoli" var="entry" status="status">
									<tr>
										<td scope="row">
											<a href="#" data-trigger="hover" rel="popover"  data-content="<s:property value="%{#entry.descrizioneCapitolo}"/>" data-original-title="Descrizione">
												<s:property value="%{#entry.denominazioneCapitolo}"/>
											</a>
										</td>
										<td><s:property value="%{#entry.descrizioneCodifiche}"/></td>
										<td><s:property value="%{#entry.elementoPianoDeiConti.codice}"/> - <s:property value="%{#entry.elementoPianoDeiConti.descrizione}"/></td>
										<td><s:property value="%{#entry.tipoFinanziamento.codice}"/> - <s:property value="%{#entry.tipoFinanziamento.descrizione}"/></td>
										<td><s:property value="%{#entry.strutturaAmministrativoContabile.codice}"/></td>
									</tr>
								</s:iterator>
							</tbody>
							<tfoot>
							</tfoot>
						</table>
						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}variazioni/consulta.codifica.js"></script>
</body>
</html>