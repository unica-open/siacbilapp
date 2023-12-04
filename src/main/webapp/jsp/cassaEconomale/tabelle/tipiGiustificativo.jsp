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
				<s:form cssClass="form-horizontal" action="" novalidate="novalidate" id="formTabellaTipoGiustificativo">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Tabella giustificativi</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<fieldset id="fieldsetRicerca">
									<div class="control-group">
										<label class="control-label" for="tipologiaGiustificativoTipoGiustificativoRicerca">Tipo giustificativo</label>
										<div class="controls">
											<s:select list="listaTipologiaGiustificativo" name="tipoGiustificativoRicerca.tipologiaGiustificativo"
												id="tipologiaGiustificativoTipoGiustificativoRicerca" cssClass="span6" headerKey="" headerValue=""
												listValue="%{codice + ' - ' + descrizione}" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="codiceTipoGiustificativoRicerca">Codice</label>
										<div class="controls">
											<s:textfield id="codiceTipoGiustificativoRicerca" name="tipoGiustificativoRicerca.codice" cssClass="span3" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="descrizioneTipoGiustificativoRicerca">Descrizione</label>
										<div class="controls">
											<s:textfield id="descrizioneTipoGiustificativoRicerca" name="tipoGiustificativoRicerca.descrizione"
												cssClass="span9" required="true" />
											<span class="clear">
												<button type="button" class="btn btn-primary pull-right" id="pulsanteRicercaTipoGiustificativo">
													cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteRicercaTipoGiustificativo"></i>
												</button>
											</span>
										</div>
									</div>
								</fieldset>
								<div id="risultatiRicercaTipoGiustificativo" class="hide">
									<h4 class="step-pane">Elenco tipo giustiicativi</h4>
									<table class="table table-hover tab_left" id="tabellaTipoGiustificativo">
										<thead>
											<tr>
												<th>Codice</th>
												<th>Tipo giustificativo</th>
												<th>Descrizione</th>
												<th>Stato</th>
												<th>Importo</th>
												<th>Percentuale anticipo trasferte</th>
												<th>Percentuale anticipo missione</th>
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
											inserisci nuovo giustificativo&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserisci"></i>
										</button>
									</p>
								</s:if>
								<div id="divGiustificativo">
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
	<script type="text/javascript" src="/siacbilapp/js/local/cassaEconomale/tabelleTipiGiustificativo.js"></script>
	
</body>
</html>