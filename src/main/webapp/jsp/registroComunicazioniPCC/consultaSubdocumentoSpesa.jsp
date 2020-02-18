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
				<s:hidden name="documentoAnnullato" id="HIDDEN_documentoAnnullato" />
				<s:hidden name="presentiComunicazioniPagamento" id="HIDDEN_presentiComunicazioniPagamento" />
				<s:form cssClass="form-horizontal" action="ricercaFatturaElettronica_effettuaRicerca" novalidate="novalidate" id="formFatturaElettronica">
					<s:include value="/jsp/include/messaggi.jsp" />
					
					<h3>Elenco comunicazioni a PCC</h3>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
								<h4 class="nostep-pane">Contabilizzazioni</h4>
								<table class="table table-hover tab_left" id="tabellaContabilizzazioni">
									<thead>
										<tr>
											<th>Stato del debito</th>
											<th>Causale</th>
											<th>Trasmesso</th>
											<th>Data invio</th>
											<th>Esito</th>
											<th>Data esito</th>
											<th class="tab_Right span2">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
								<s:if test="%{!presentiComunicazioniPagamento}">
									<p>
										<button type="button" class="btn btn-secondary" id="pulsanteInserisciNuovaContabilizzazione">inserisci nuova contabilizzazione</button>
									</p>
								</s:if>
								<div class="Border_line margin-medium"></div>
								<h4 class="nostep-pane">Comunicazioni data scadenza</h4>
								<table class="table table-hover tab_left" id="tabellaComunicazioniDataScadenza">
									<thead>
										<tr>
											<th>Data scadenza</th>
											<th>Trasmesso</th>
											<th>Data invio</th>
											<th>Esito</th>											
											<th>Data esito</th>
										</tr>
									</thead>
									<tbody>
										<s:iterator value="listaComunicazioneDataScadenza" var="cds">
											<tr>
												<td><s:property value="#cds.dataScadenza"/></td>
												<td><s:property value="#cds.trasmesso"/></td>
												<td><s:property value="#cds.dataInvio"/></td>
												<td>											
													<a href="#" data-trigger="hover"  data-placement="left"  rel="popover" data-original-title="Descrizione esito" data-content="<s:property value='#cds.descrizioneEsito'/>">
														<s:property value='#cds.codiceEsito'/>
													</a>											 
												</td>
												<td><s:property value="#cds.dataEsito"/></td>
											</tr>
										</s:iterator>
									</tbody>
								</table>
								<div class="Border_line margin-medium"></div>
								<h4 class="nostep-pane">Comunicazioni pagamento</h4>
								<table class="table table-hover tab_left" id="tabellaComunicazioniPagamento">
									<thead>
										<tr>
											<th>Data emissione ordinativo</th>
											<th>Ordinativo</th>
											<th>Data quietanza</th>
											<th>Quietanza</th>
											<th>Importo Quietanza</th>											
											<th>Trasmesso</th>
											<th>Data invio</th>
											<th>Esito</th>
											<th>Data esito</th>
										</tr>
									</thead>
									<tbody>
										<s:iterator value="listaComunicazionePagamento" var="cds">
											<tr>
												<td><s:property value="#cds.dataEmissioneOrdinativo"/></td>
												<td><s:property value="#cds.numeroOrdinativo"/></td>												
												<td><s:property value="#cds.dataQuietanza"/></td>
												<td><s:property value="#cds.numeroQuietanza"/></td>
												<td><s:property value="#cds.importoQuietanza"/></td>
												<td><s:property value="#cds.trasmesso"/></td>
												<td><s:property value="#cds.dataInvio"/></td>
												<td>											
													<a href="#" data-trigger="hover"  data-placement="left" rel="popover" data-original-title="Descrizione esito" data-content="<s:property value='#cds.descrizioneEsito'/>">
														<s:property value='#cds.codiceEsito'/>
													</a>											 
												</td>
												<td><s:property value="#cds.dataEsito"/></td>
											</tr>
										</s:iterator>
									</tbody>
								</table>
							</fieldset>
						</div>
					</div>
					
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
				<s:if test="%{!presentiComunicazioniPagamento}">
					<s:include value="/jsp/registroComunicazioniPCC/include/modaleNuovaContabilizzazione.jsp" />
					<s:if test="%{!documentoAnnullato}">
						<s:include value="/jsp/registroComunicazioniPCC/include/modaleAggiornaContabilizzazione.jsp" />
						<s:include value="/jsp/registroComunicazioniPCC/include/modaleEliminaContabilizzazione.jsp" />
					</s:if>
				</s:if>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}registroComunicazioniPCC/consultaSubdocumentoSpesa.js"></script>

</body>
</html>