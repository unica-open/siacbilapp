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
				<s:hidden name="azioniAbilitate" id="azioniAbilitate" />
				<s:form cssClass="form-horizontal" action="cassaEconomaleOperazioniCassaGestioneAggiornamento" novalidate="novalidate" id="formGestioneOperazioniCassa">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Operazioni cassa</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<fieldset id="fieldsetRicerca">
									<div class="control-group">
										<label class="control-label" for="dataOperazioneOperazioneCassaRicerca">Data operazione</label>
										<div class="controls">
											<s:textfield id="dataOperazioneOperazioneCassaRicerca" name="operazioneCassaRicerca.dataOperazione"
												cssClass="span2 datepicker" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="tipoOperazioneCassaOperazioneCassaRicerca">Tipo operazione</label>
										<div class="controls">
											<s:select list="listaTipoOperazioneCassa" id="tipoOperazioneCassaRicerca"
												name="tipoOperazioneCassaRicerca.uid" headerKey="" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span9" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="statoOperativoOperazioneCassaOperazioneCassaRicerca">Stato</label>
										<div class="controls">
											<s:select list="listaStatoOperativoOperazioneCassa" id="statoOperativoOperazioneCassaOperazioneCassaRicerca"
												name="operazioneCassaRicerca.statoOperativoOperazioneCassa" headerKey="" headerValue=""
												listValue="descrizione" cssClass="span6" />
											<span class="clear">
												<button type="button" class="btn btn-primary pull-right" id="pulsanteRicercaOperazioneCassa">
													cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteRicercaOperazioneCassa"></i>
												</button>
											</span>
										</div>
									</div>
								</fieldset>
								<div id="risultatiRicercaOperazioneCassa" class="hide">
									<h4 class="step-pane">Elenco operazioni di cassa</h4>
									<table class="table table-hover tab_left" id="tabellaOperazioneCassa">
										<thead>
											<tr>
												<th>Data operazione</th>
												<th>Tipologia operazione</th>
												<th>Tipo operazione</th>
												<th>Modalit&agrave; di pagamento</th>
												<th>Stato</th>
												<th>Importo</th>
												<th class="tab_Right">&nbsp;</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>
								<p>
									<s:if test="inserimentoAbilitato">
										<button type="button" id="pulsanteInserisci" class="btn btn-secondary">
											inserisci nuova operazione di cassa&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserisci"></i>
										</button>
										<span class="pull-right">
											<button type="button" id="pulsanteVisualizzaImporti" class="btn btn-primary">
												visualizza importi&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteVisualizzaImporti"></i>
											</button>
										</span>
									</s:if>
								</p>
								<div id="divOperazioneCassa"></div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
					<s:include value="/jsp/cassaEconomale/operazioneCassa/modaleAnnullamento.jsp" />
					<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
					<div id="divImportiCassa"></div>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}predocumento/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale.js"></script>
	<script type="text/javascript" src="${jspath}cassaEconomale/operazioniCassa.js"></script>
	
</body>
</html>