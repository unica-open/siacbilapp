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
				<s:hidden name="codiceEsenzione" id="hidden_codiceEsenzione" />
				<s:hidden name="codiceSplitReverse" id="hidden_codiceSplitReverse" />
				<s:form cssClass="form-horizontal" action="inserimentoTipoOnere" novalidate="novalidate" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Inserimento onere</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal margin-medium">
								<div class="control-group">
									<label class="control-label" for="naturaOnere">Natura onere *</label>
									<div class="controls">
										<select name="tipoOnere.naturaOnere.uid" required id="naturaOnere" class="span6">
											<option></option>
											<s:iterator value="listaNaturaOnere" var="no">
												<option value="<s:property value="#no.uid"/>" <s:if test="%{tipoOnere.naturaOnere.uid == #no.uid}">selected</s:if> data-codice="<s:property value="#no.codice"/>">
													<s:property value="#no.codice"/> - <s:property value="#no.descrizione" />
												</option>
											</s:iterator>
										</select>
									</div>
								</div>
								<div class="control-group hide" id="tipoIvaSplitReverseTipoOnereDiv">
									<label class="control-label" for="tipoIvaSplitReverseTipoOnere">Split / Reverse *</label>
									<div class="controls">
										<select class="span6" name="tipoOnere.tipoIvaSplitReverse" required disabled id="tipoIvaSplitReverseTipoOnere">
											<option></option>
											<s:iterator value="listaTipoIvaSplitReverse" var="tisr">
												<option value="<s:property value="#tisr.name()"/>" <s:if test="%{tipoOnere.tipoIvaSplitReverse == #tisr}">selected</s:if>
														<s:if test='%{codiceEsenzione.equals(#tisr.codice)}'>data-esente="true" class="hide"</s:if> data-codice="<s:property value="#tisr.codice"/>">
													<s:property value="#tisr.codice"/> - <s:property value="#tisr.descrizione" />
												</option>
											</s:iterator>
										</select>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Tipo onere *</label>
									<div class="controls">
										<s:textfield id="codice" name="tipoOnere.codice" cssClass="span2" placeholder="codice" maxlength="200" required="true" />
										<s:textfield id="descrizione" name="tipoOnere.descrizione" cssClass="span9" placeholder="descrizione" maxlength="200" required="true" />
									</div>
								</div>
								<div class="control-group" data-hidden-natura="<s:property value="codiceEsenzione"/>">
									<label class="control-label" for="aliquotaCaricoSoggetto" data-hidden-split-reverse="<s:property value="codiceReverseChange"/>">Aliquota a carico soggetto </label>
									<div class="controls">
										<s:textfield id="aliquotaCaricoSoggetto" name="tipoOnere.aliquotaCaricoSoggetto" cssClass="lbTextSmall span2 soloNumeri decimale" data-hidden-split-reverse="%{codiceReverseChange}" />
										<span class="alRight">
											<label for="aliquotaCaricoEnte" class="radio inline" data-hidden-natura="<s:property value="codiceSplitReverse"/>">Aliquota a carico Ente </label>
										</span>
										<s:textfield id="aliquotaCaricoEnte" name="tipoOnere.aliquotaCaricoEnte" cssClass="lbTextSmall span2 soloNumeri decimale" data-hidden-natura="%{codiceSplitReverse}" />
									</div>
								</div>
								<div class="control-group" data-hidden-natura="<s:property value="codiceSplitReverse"/> <s:property value="codiceEsenzione"/>">
									<label class="control-label" for="causale770">Causale 770</label>
									<div class="controls">
										<s:select list="listaCausale770" multiple="true" id="causale770" cssClass="span10 chosen-select"
											name="causali770.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
											data-placeholder="Seleziona la causale" value="%{causali770.{uid}}" />
									</div>
								</div>
								<div class="control-group" data-hidden-natura="<s:property value="codiceSplitReverse"/> <s:property value="codiceEsenzione"/>">
									<label class="control-label" for="sommaNonSoggetta">Codici somme non soggetta</label>
									<div class="controls">
										<s:select list="listaSommeNonSoggette" multiple="true" id="sommaNonSoggetta" cssClass="span10 chosen-select"
 											name="sommeNonSoggette.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}" 
											data-placeholder="Seleziona il tipo di somma non soggetta" value="%{sommeNonSoggette.{uid}}"/>
									</div>
								</div>
								<div class="control-group" data-hidden-natura="<s:property value="codiceSplitReverse"/> <s:property value="codiceEsenzione"/>">
									<label class="control-label" for="attivitaOnere">Attivit&agrave; onere</label>
									<div class="controls">
										<s:select list="listaAttivitaOnere" multiple="true" id="attivitaOnere" cssClass="span10 chosen-select"
											name="attivitaOnere.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
											data-placeholder="Seleziona l'attività" value="%{attivitaOnere.{uid}}" />
									</div>
								</div>
								<div class="control-group" data-hidden-natura="<s:property value="codiceSplitReverse"/> <s:property value="codiceEsenzione"/>">
									<label for="quadro770" class="control-label">Quadro 770</label>
									<div class="controls">
										<s:textfield id="quadro770" name="tipoOnere.quadro770" cssClass="lbTextSmall span2" />
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset cssClass="btn btn-secondary" value="annulla" />
						<s:submit cssClass="btn btn-primary pull-right" value="salva" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/tipoOnere/inserisci.js"></script>

</body>
</html>