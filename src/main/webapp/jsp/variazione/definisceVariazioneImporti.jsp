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
					<s:include value="/jsp/include/messaggi.jsp" />
					
					<s:form action="effettuaDefinisciVariazioneImporti" novalidate="novalidate">
						<s:hidden name="applicazione" id="HIDDEN_tipoApplicazione" />
						<h3>Definisci Variazione</h3>	
						<div class="overlay-on-submit">						
							<s:if test="%{variazioneDecentrata}">
								<dl class="dl-horizontal">
									<dt>Direzione Proponente</dt><dd>&nbsp;<s:property value="direzioneProponente.codice"/> - <s:property value="direzioneProponente.descrizione"/></dd>
								    <dt>Num. variazione</dt><dd><s:property value="numeroVariazione"/>&nbsp;</dd>
									<dt>Stato</dt><dd><s:property value="statoVariazione.descrizione"/>&nbsp;</dd>
									<dt>Applicazione</dt><dd><s:property value="applicazioneVariazione"/>&nbsp;</dd>
									<dt>Descrizione</dt><dd><s:property value="descrizioneVariazione"/>&nbsp;</dd>
									<dt>Note</dt><dd><s:property value="noteVariazione"/>&nbsp;</dd>
									<dt>Tipo variazione</dt><dd><s:property value="tipoVariazione.codice"/> - <s:property value="tipoVariazione.descrizione"/>&nbsp;</dd>
									<dt>Data Definizione Della Variazione</dt><dd><s:property value="dataDefinizioneVariazione"/>&nbsp;</dd>
									<dt>Data Apertura Proposta</dt><dd><s:property value="dataAperturaProposta"/></dd>
									<dt>Data Chiusura Proposta</dt><dd><s:property value="dataChiusuraProposta"/></dd>
								</dl>
							</s:if>
							<s:else>
								<dl class="dl-horizontal">
								    <dt>Num. variazione</dt><dd><s:property value="numeroVariazione"/>&nbsp;</dd>
									<dt>Stato</dt><dd><s:property value="statoVariazione.descrizione"/>&nbsp;</dd>
									<dt>Applicazione</dt><dd><s:property value="applicazioneVariazione"/>&nbsp;</dd>
									<dt>Descrizione</dt><dd><s:property value="descrizioneVariazione"/>&nbsp;</dd>
									<dt>Note</dt><dd><s:property value="noteVariazione"/>&nbsp;</dd>
									<dt>Tipo variazione</dt><dd><s:property value="tipoVariazione.codice"/> - <s:property value="tipoVariazione.descrizione"/>&nbsp;</dd>
									<dt>Data Definizione Della Variazione</dt><dd><s:property value="dataDefinizioneVariazione"/>&nbsp;</dd>
								</dl>
							</s:else>
						 
							<h5>Provvedimento variazione di PEG</h5>
								<dl class="dl-horizontal">
									<dt>Tipo</dt><dd><s:property value="tipoProvvedimento"/>&nbsp;</dd>
									<dt>Anno</dt><dd><s:property value="annoProvvedimento"/>&nbsp;</dd>
									<dt>Numero</dt><dd><s:property value="numeroProvvedimento"/>&nbsp;</dd>
									<dt>Struttura</dt><dd><s:property value="strutturaProvvedimento"/>&nbsp;</dd>
									<dt>Oggetto</dt><dd><s:property value="oggettoProvvedimento"/>&nbsp;</dd>
								</dl>
								
							<h5>Provvedimento variazione di bilancio</h5>
								<dl class="dl-horizontal">
									<dt>Tipo</dt><dd><s:property value="tipoProvvedimentoVariazioneDiBilancio"/>&nbsp;</dd>
									<dt>Anno</dt><dd><s:property value="annoProvvedimentoVariazioneDiBilancio"/>&nbsp;</dd>
									<dt>Numero</dt><dd><s:property value="numeroProvvedimentoVariazioneDiBilancio"/>&nbsp;</dd>
									<dt>Struttura</dt><dd><s:property value="strutturaProvvedimentoVariazioneDiBilancio"/>&nbsp;</dd>
									<dt>Oggetto</dt><dd><s:property value="oggettoProvvedimentoVariazioneDiBilancio"/>&nbsp;</dd>
								</dl>
								
							<h5>Elenco modifiche in variazione</h5>
								
								 <!--     TABELLE       RIEPILOGO   con azioni -->
								<table class="table table-condensed table-hover table-bordered" id="elencoCapitoli" summary="...." >
									<thead>
										<tr>
											<th scope="col">Capitolo</th>
											<th scope="col">Componenti</th>
											<th scope="col" class="text-center">Competenza ${annoEsercizioInt + 0}</th>
											<th scope="col" class="text-center">Residuo ${annoEsercizioInt + 0}</th>
											<th scope="col" class="text-center">Cassa ${annoEsercizioInt + 0}</th>
											<th scope="col" class="text-center">Competenza ${annoEsercizioInt + 1}</th>
											<th scope="col" class="text-center">Competenza ${annoEsercizioInt + 2}</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
		                           	<tfoot>
										<tr class="info">
											<th colspan="2">Totale entrate</th>
											<td><span id="totaleEntrateCompetenzaVariazione"></span></td>
											<td><span id="totaleEntrateResiduoVariazione"></span></td>
											<td><span id="totaleEntrateCassaVariazione"></span></td>
											<td><span id="totaleEntrateCompetenzaVariazioneAnnoPiuUno"></span></td>
											<td><span id="totaleEntrateCompetenzaVariazioneAnnoPiuDue"></span></td>
										</tr>
										<tr class="info">
											<th colspan="2">Totale spese</th>
											<td><span id="totaleSpeseCompetenzaVariazione"></span></td>
											<td><span id="totaleSpeseResiduoVariazione"></span></td>
											<td><span id="totaleSpeseCassaVariazione"></span></td>
											<td><span id="totaleSpeseCompetenzaVariazioneAnnoPiuUno"></span></td>
											<td><span id="totaleSpeseCompetenzaVariazioneAnnoPiuDue"></span></td>
										</tr>
										<tr class="info">
											<th colspan="2">Differenza</th>
											<td><span id="differenzaCompetenzaVariazione"></span></td>
											<td><span id="differenzaResiduoVariazione"></span></td>
											<td><span id="differenzaCassaVariazione"></span></td>
											<td><span id="differenzaCompetenzaVariazioneAnnoPiuUno"></span></td>
											<td><span id="differenzaCompetenzaVariazioneAnnoPiuDue"></span></td>
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
						  		<p>&nbsp;</p>
	                 			<p class="margin-large">
									<s:include value="/jsp/include/indietro.jsp" />
									<span id="spanPulsanteDefinisciVariazione">
										<s:if test="%{!definizioneEseguita}">
											<button  type ="button" id="buttonDefinisciVariazione" type="button" class="btn btn-primary pull-right">definisci variazione</button>
										</s:if>
									</span>
								</p>
							</div>							
						</s:form>
					<s:include value="/jsp/variazione/include/modaleConsultaComponentiCapitoloInVariazione.jsp" />
				</div>
			</div>
		</div>
	</div>
	
	<div id="iframeContainer" class="hide"></div>
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/variazioni.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/definisci.importi.js"></script>
</body>
</html>