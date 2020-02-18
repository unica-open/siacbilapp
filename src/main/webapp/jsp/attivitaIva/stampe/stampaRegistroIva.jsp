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
				<s:form action="effettuaStampaRegistroIva" novalidate="novalidate" method="post" cssClass="form-horizontal">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Stampa registro iva</h3>
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
								<div class="control-group">
									<label for="tipoRegistroIva" class="control-label">Tipo registro iva *</label>
									<div class="controls">
										<s:select list="listaTipoRegistroIva" id="tipoRegistroIva" name="tipoRegistroIva" required="true"
											cssClass="span6" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"></label>
									<div class="controls">
										<span class="fade <s:if test="documentiPagatiVisibile">in</s:if>" id="spanDocumentiPagati">
											<span class="alRight">
												<label class="radio inline" for="flagDocumentiPagati">Documenti pagati</label>
											</span>
											<s:checkbox id="flagDocumentiPagati" name="flagDocumentiPagati" />
										</span>
										<span class="fade <s:if test="documentiIncassatiVisibile">in</s:if>" id="spanDocumentiIncassati">
											<span class="alRight">
												<label class="radio inline" for="flagDocumentiIncassati">Documenti incassati</label>
											</span>
											<s:checkbox id="flagDocumentiIncassati" name="flagDocumentiIncassati" />
										</span>
									</div>
								</div>
								<div class="control-group">
									<label for="tipoChiusura" class="control-label">Tipo di chiusura</label>
									<div class="controls">
										<s:select list="listaTipoChiusura" id="tipoChiusura" name="tipoChiusura" required="true" disabled="true"
											cssClass="span6" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" data-overlay="" />
										<s:hidden id="hiddenTipoChiusura" name="tipoChiusura" />
									</div>
								</div>
								<div class="control-group">
									<label for="periodo" class="control-label">Periodo *</label>
									<div class="controls">
										<s:select list="listaPeriodo" id="periodo" name="periodo" required="true" disabled="!periodoAbilitato"
											cssClass="span6" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" data-overlay="" />
									</div>
								</div>
								<div class="control-group">
									<label for="registroIva" class="control-label">Registro iva</label>
									<div class="controls">
										<s:select list="listaRegistroIva" id="registroIva" name="registroIva.uid" disabled="!registroIvaAbilitato"
											cssClass="span6" headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-overlay="" />
									</div>
								</div>
								<div class="control-group">
									<label for="tipoStampa" class="control-label">Tipo stampa *</label>
									<div class="controls">
										<s:select list="listaTipoStampa" id="tipoStampa" name="tipoStampa" required="true"
											cssClass="span6" headerKey="" headerValue="" listValue="descrizione" />
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
	<script type="text/javascript" src="${jspath}attivitaIva/stampe/stampaRegistroIva.js"></script>

</body>
</html>