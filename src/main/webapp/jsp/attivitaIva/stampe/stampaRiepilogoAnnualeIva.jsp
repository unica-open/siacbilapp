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
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form action="effettuaStampaRiepilogoAnnualeIva" novalidate="novalidate" method="post" cssClass="form-horizontal">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Stampa riepilogo annuale iva</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<h4>Dati principali</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label for="annoEsercizio" class="control-label">Anno esercizio</label>
									<div class="controls">
										<s:textfield id="annoEsercizio" name="annoEsercizio" cssClass="lbTextSmall span1" disabled="true" />
										<s:hidden name="annoEsercizio" />
									</div>
								</div>
								<div class="control-group">
									<label for="gruppoAttivitaIva" class="control-label">Gruppo attivit&agrave; iva *</label>
									<div class="controls">
										<s:select list="listaGruppoAttivitaIva" id="gruppoAttivitaIva" name="gruppoAttivitaIva.uid" required="true"
											cssClass="span6" headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn btn-primary pull-right" id="pulsanteStampa">stampa</button>
					</p>
					<s:include value="/jsp/attivitaIva/stampe/modaleConferma.jsp">
						<s:param name="template"><s:property value="%{templateConferma}" /></s:param>
					</s:include>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}attivitaIva/stampe/stampeIva.js"></script>
	<script type="text/javascript" src="${jspath}attivitaIva/stampe/stampaRiepilogoAnnualeIva.js"></script>

</body>
</html>