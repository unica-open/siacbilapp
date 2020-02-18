<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
						<s:hidden id="uidVariazione" value="%{uidVariazione}"></s:hidden>
						<s:hidden id="numeroVariazione" value="%{numeroVariazione}"></s:hidden>
						<s:hidden id="annoCompetenza" value="%{annoCompetenza}"></s:hidden>
						<dl class="dl-horizontal">
							<dt>Num. variazione</dt><dd><s:property value="numeroVariazione"/>&nbsp;</dd>
							<dt>Applicazione</dt><dd><s:property value="applicazioneVariazione"/>&nbsp;</dd>
							<dt>Descrizione</dt><dd><s:property value="descrizioneVariazione"/>&nbsp;</dd>
							<dt>Tipo variazione</dt><dd><s:property value="tipoVariazione.codice"/> - <s:property value="tipoVariazione.descrizione"/>&nbsp;</dd>
							<dt>Anno competenza</dt><dd><s:property value="annoCompetenza"/>&nbsp;</dd>
							<dt>Stato</dt><dd><s:property value="elementoStatoOperativoVariazione.descrizione"/>&nbsp;</dd>
							<dt>Data variazione</dt><dd><s:property value="dataDefinizioneVariazione"/>&nbsp;</dd>
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
						<table class="table table-condensed table-hover table-bordered" id="tabellaGestioneVariazioni" summary="...." >
							<thead>
								<tr>
									<th scope="col">Capitolo</th>
									<th scope="col" class="text-center">Competenza</th>
									<th scope="col" class="text-center">Residuo</th>
									<th scope="col" class="text-center">Cassa</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
								<tr class="info">
									<th>Totale entrate</th>
									<td id="totaleEntrateCompetenzaVariazione" class="text-right"></td>
									<td id="totaleEntrateResiduoVariazione" class="text-right"></td>
									<td id="totaleEntrateCassaVariazione" class="text-right"></td>
								</tr>
								<tr class="info">
									<th>Totale spese</th>
									<td id="totaleSpeseCompetenzaVariazione" class="text-right"></td>
									<td id="totaleSpeseCassaVariazione" class="text-right"></td>
									<td id="totaleSpeseResiduoVariazione" class="text-right"></td>
								</tr>
								<tr class="info">
									<th>Differenza</th>
									<td id="differenzaCompetenzaVariazione" class="text-right"></td>
									<td id="differenzaResiduoVariazione" class="text-right"></td>
									<td id="differenzaCassaVariazione" class="text-right"></td>
								</tr>
							</tfoot>
						</table>
						<s:include value ="/jsp/variazione/include/ricercaCapitoloNellaVariazione.jsp"/>
						<div id="divEsportazioneDati" class="form-horizontal">
							<button id="pulsanteEsportaDati" type="submit" class="pull-left btn btn-secondary">
								Esporta capitoli in Excel <i class="icon-download-alt icon-large"></i>&nbsp;
							</button>
							<button id="pulsanteEsportaDatiXlsx" type="submit" class="pull-left btn btn-secondary">
								Esporta capitoli in Excel (XLSX) <i class="icon-download-alt icon-large"></i>&nbsp;
							</button>
						</div>
						<br/>

						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</form>
					<s:include value="/jsp/variazione/include/editStanziamentiDisabled.jsp" />
				</div>
			</div>
		</div>
	</div>

	<div id="iframeContainer"></div>
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}variazioni/variazioni.js"></script>
	<script type="text/javascript" src="${jspath}variazioni/variazioniImporti.js"></script>
	<script type="text/javascript" src="${jspath}variazioni/consulta.importi.ueb.js"></script>
</body>
</html>