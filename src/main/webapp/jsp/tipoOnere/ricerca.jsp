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
				<s:form cssClass="form-horizontal" action="#" novalidate="novalidate" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Ricerca onere</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal margin-medium" id="fieldsetRicerca">
								<div class="control-group">
									<label class="control-label" for="naturaOnere">Natura onere</label>
									<div class="controls">
										<s:select list="listaNaturaOnere" name="tipoOnere.naturaOnere.uid" required="true" id="naturaOnere" cssClass="span6"
											headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="soloInCorsoDiValidita">Solo in corso di validit&agrave;</label>
									<div class="controls">
										<s:checkbox id="soloInCorsoDiValidita" name="corsoDiValidita" />
										<span class="pull-right">
											<s:reset cssClass="btn btn-secondary" value="annulla" />
											<a class="btn btn-primary" id="cercaTipoOnere">
												cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_cercaTipoOnere"></i>
											</a>
										</span>
									</div>
								</div>
							</fieldset>
							<div id="divElencoTipoOnere" class="hide">
								<h4 class="step-pane">Elenco oneri</h4>
								<table class="table table-hover tab_left" id="tabellaTipoOnere">
									<thead>
										<tr>
											<th class="span2">Codice</th>
											<th>Descrizione</th>
											<th>Natura</th>
											<th>Tipo IVA split-reverse</th>
											<th class="span2">Valido Dal</th>
											<th class="span2">Al</th>
											<th class="tab_Right"></th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
									</tfoot>
								</table>
							</div>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<a href="ricercaTipoOnere_inserisci.do" class="btn btn-secondary">Inserisci Tipo Onere</a>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}tipoOnere/ricerca.js"></script>

</body>
</html>