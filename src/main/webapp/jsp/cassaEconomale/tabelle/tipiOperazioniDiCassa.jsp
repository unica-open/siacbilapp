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
				<s:hidden name="cassaInStatoValido" id="cassaInStatoValido" />
				<s:form cssClass="form-horizontal" action="" novalidate="novalidate" id="formTabellaTipoOperazioneCassa">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Tabella tipi operazioni di cassa</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<fieldset id="fieldsetRicerca">
									<div class="control-group">
										<label class="control-label" for="codiceTipoOperazioneCassaRicerca">Codice</label>
										<div class="controls">
											<s:textfield id="codiceTipoOperazioneCassaRicerca" name="tipoOperazioneCassaRicerca.codice" cssClass="span3" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="descrizioneTipoOperazioneCassaRicerca">Descrizione</label>
										<div class="controls">
											<s:textfield id="descrizioneTipoOperazioneCassaRicerca" name="tipoOperazioneCassaRicerca.descrizione"
												cssClass="span9" required="true" />
											<span class="clear">
												<button type="button" class="btn btn-primary pull-right" id="pulsanteRicercaTipoOperazioneCassa">
													cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteRicercaTipoOperazioneCassa"></i>
												</button>
											</span>
										</div>
									</div>
								</fieldset>
								<div id="risultatiRicercaTipoOperazioniCassa" class="hide">
									<h4 class="step-pane">Elenco operazioni di cassa</h4>
									<table class="table table-hover tab_left" id="tabellaTipoOperazioneCassa">
										<thead>
											<tr>
												<th>Codice</th>
												<th>Descrizione</th>
												<th>Stato</th>
												<th>Incluso in giornale</th>
												<th>Incluso in rendiconto</th>
												<th class="tab_Right">&nbsp;</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>
								<s:if test="inserimentoAbilitato">
									<p>
										<button type="button" id="pulsanteInserisci" class="btn btn-secondary">
											inserisci nuovo tipo operazione di cassa&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserisci"></i>
										</button>
									</p>
								</s:if>
								<div id="divOperazioneCassa">
								</div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
					<s:include value="/jsp/cassaEconomale/tabelle/modaleAnnullamento.jsp" />
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cassaEconomale/tabelleTipiOperazioniDiCassa.js"></script>
	
</body>
</html>