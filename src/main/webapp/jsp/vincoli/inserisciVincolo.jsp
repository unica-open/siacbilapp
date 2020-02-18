<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
			<div class="span12 ">
				<div class="contentPage">
					<h3>Inserimento Vincolo</h3>
					<div class="step-content">
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:form method="post" action="inserimentoVincolo" novalidate="novalidate">
						
							<fieldset class="form-horizontal">
								<div class="control-group">
									<span class="control-label">Bilancio *</span>
									<div class="controls">
										<label class="radio" for="tipoVincoloCapitoliPrevisione">
											<input id="tipoVincoloCapitoliPrevisione" type="radio" name="vincolo.tipoVincoloCapitoli" value="PREVISIONE" <s:if test='%{vincolo.tipoVincoloCapitoli.name().equals("PREVISIONE")}'>checked</s:if> <s:if test='%{bilancioPrevisioneAbilitato == false}'>disabled</s:if>> Previsione
										</label>
										<label class="radio" for="tipoVincoloCapitoliGestione">
											<input id="tipoVincoloCapitoliGestione" type="radio" name="vincolo.tipoVincoloCapitoli" value="GESTIONE" <s:if test='%{vincolo.tipoVincoloCapitoli.name().equals("GESTIONE")}'>checked</s:if> <s:if test='%{bilancioGestioneAbilitato == false}'>disabled</s:if>> Gestione
										</label>
										<s:hidden name="bilancioPrevisioneAbilitato" data-maintain="" />
										<s:hidden name="bilancioGestioneAbilitato" data-maintain="" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codiceVincolo">Codice *</label>
									<div class="controls">
										<s:textfield id="codiceVincolo" cssClass="lbTextSmall span2" name="vincolo.codice" maxlength="200" placeholder="codice" required="required" />
									</div>
								</div>
								<div class="control-group">
									<span class="al">
										<label for="descrizioneVincolo" class="control-label">Descrizione </label>
									</span>
									<div class="controls">
										<s:textarea id="descrizioneVincolo" rows="1" cols="15" cssClass="span10" name="vincolo.descrizione" maxlength="500" ></s:textarea>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="trasferimentiVincolati">Trasferimenti vincolati </label>
									<div class="controls">
										<label class="radio inline">
											<input type="radio" name="vincolo.flagTrasferimentiVincolati" value="false" <s:if test="%{!vincolo.flagTrasferimentiVincolati}">checked="checked"</s:if>> No
										</label>
										<label class="radio inline">
											<input type="radio" name="vincolo.flagTrasferimentiVincolati" value="true" <s:if test="%{vincolo.flagTrasferimentiVincolati}">checked="checked"</s:if>> S&igrave;
										</label>
									</div>
								</div>
								<div class="control-group">
									<span class="al">
										<label for="genereVincolo" class="control-label">Tipo vincolo </label>
									</span>
									<div class="controls">
										<s:select list="listaGenereVincolo" id="genereVincolo" name="vincolo.genereVincolo.uid" cssClass="span10" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
											headerKey="" headerValue="" />
									</div>
								</div>
								<div class="control-group">
									<span class="al">
										<label for="noteVincolo" class="control-label">Note </label>
									</span>
									<div class="controls">
										<s:textarea id="noteVincolo" rows="1" cols="15" cssClass="span10" name="vincolo.note" maxlength="500"></s:textarea>
									</div>
								</div>
							</fieldset>
							<p>
								<s:include value="/jsp/include/indietro.jsp" />
								<button type="button" class="btn btn-secondary reset">annulla</button>
								<s:submit cssClass="btn btn-primary pull-right" value="salva"/>
							</p>
						</s:form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />

</body>
</html>