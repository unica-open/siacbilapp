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
					<s:form id="formRicercaVariazioneCespite" cssClass="form-horizontal" novalidate="novalidate" action="%{baseUrl}_effettuaRicerca">
						<h3 id="titolo"><s:property value="formTitle"/></h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<div class="step-pane active" id="step1">
								<p class="clearfix">
									<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
								</p>
								<fieldset class="form-horizontal">
									<h4><s:property value="formSubtitle" /></h4>
									<div class="control-group">
										<label for="annoVariazioneCespite" class="control-label">Anno</label>
										<div class="controls">
											<s:textfield id="annoVariazioneCespite" cssClass="span2" name="variazioneCespite.annoVariazione" maxlength="4" />
											<span class="alRight">
												<label for="dataInserimentoVariazioneCespite" class="radio inline">Data inserimento</label>
											</span>
											<s:textfield id="dataInserimentoVariazioneCespite" name="variazioneCespite.dataVariazione" class="datepicker lbTextSmall span2" maxlength="10" />
										</div>
									</div>
									<div class="control-group">
										<label for="descrizioneVariazione" class="control-label">Descrizione </label>
										<div class="controls">
											<s:textarea id="descrizioneVariazione" name="variazioneCespite.descrizione" rows="2" cols="20" cssClass="span6" maxlength="500"></s:textarea>
										</div>
									</div>
									<div class="control-group">
										<label for="tipoVariazioneCespite" class="control-label">Tipo Variazione</label>
										<div class="controls">
											<select id="tipoVariazioneCespite" name="variazioneCespite.tipoVariazione" class="span6" required disabled>
												<option selected><s:property value="testoSelectTipoVariazione" /></option>
											</select>
										</div>
									</div>
									<h4>Dati cespite</h4>
									<div class="control-group">
										<label for="codice" class="control-label">Codice</label>
										<div class="controls">
											<s:textfield id="codice" name="cespite.codice" cssClass="span6" maxlength="500"/>
										</div>
									</div>
									<div class="control-group">
										<label for="descrizione" class="control-label">Descrizione</label>
										<div class="controls">
											<s:textfield id="descrizione" name="cespite.descrizione" cssClass="span6" placeholder="descrizione" maxlength="500"/>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="cespiteTipoBene">Tipo Bene</label>
										<div class="controls">
											<select id="cespiteTipoBene" name="cespite.tipoBeneCespite.uid" class="span6">
												<option value="0"></option>
												<s:iterator value="listaTipoBeneCespite" var="ltbc">
													<option value="<s:property value="%{#ltbc.uid}"/>"
														<s:if test="%{#ltbc.contoPatrimoniale != null}">
															data-conto-patrimoniale="<s:property value="%{#ltbc.contoPatrimoniale.codice + ' - ' + #ltbc.contoPatrimoniale.descrizione}"/>"
														</s:if>
														<s:if test="%{#ltbc.uid == cespite.tipoBeneCespite.uid}">
															selected
														</s:if>
														>
														<s:property value="%{#ltbc.codice + ' - ' + #ltbc.descrizione}"/>
													</option>
												</s:iterator>
											</select>
										</div>
									</div>
									<%-- CONTO PATRIMONIALE --%>
									<div class="control-group">
										<label class="control-label" for="cespiteClassificazioneGiuridica">Classificazione giuridica</label>
										<div class="controls">
											<s:select id="cespiteTipoBene" cssClass="span6" list="listaClassificazioneGiuridicaCespite" name="cespite.classificazioneGiuridicaCespite"
												headerKey="" headerValue="" listValue="%{codice + ' - '+ descrizione}" />
										</div>
									</div>
									<div class="control-group">
										<label for="beniCulturali" class="control-label">Soggetto beni culturali</label>
										<div class="controls">
											<select id="beniCulturali" name="flagSoggettoTutelaBeniCulturali">
												<option value=""  <s:if test='flagSoggettoTutelaBeniCulturali == ""' >selected</s:if>>Non si applica</option>
												<option value="S" <s:if test='flagSoggettoTutelaBeniCulturali == "S"'>selected</s:if>>S&iacute;</option>
												<option value="N" <s:if test='flagSoggettoTutelaBeniCulturali == "N"'>selected</s:if>>No</option>
											</select>
										</div>
									</div>
									<div class="control-group">
										<label for="donazioneRinvenimento" class="control-label"> Donazione/Rinvenimento</label>
										<div class="controls">
											<select id="donazioneRinvenimento" name="flgDonazioneRinvenimento">
												<option value=""  <s:if test='flgDonazioneRinvenimento == ""' >selected</s:if>>Non si applica</option>
												<option value="S" <s:if test='flgDonazioneRinvenimento == "S"'>selected</s:if>>S&iacute;</option>
												<option value="N" <s:if test='flgDonazioneRinvenimento == "N"'>selected</s:if>>No</option>
											</select>
										</div>
									</div>
									<div class="control-group">
										<label for="numeroInventario" class="control-label"><abbr title="Numero">Num.</abbr> inventario</label>
										<div class="controls">
											<s:textfield id="numeroInventario" name="cespite.numeroInventario" cssClass="lbTextSmall span2 soloNumeri" maxlength="8"/>
										</div>
									</div>
									<div class="control-group">
										<label for="dataIngressoInventario" class="control-label">Data ingresso in inventario</label>
										<div class="controls">
											<s:textfield id="dataIngressoInventario" name="cespite.dataAccessoInventario" cssClass="lbTextSmall span2 datepicker" size="10"/>
										</div>
									</div>
								</fieldset>
							</div>
						</div>
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn reset">annulla</button>
							<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/variazionecespite/ricercaVariazioneCespite_step1.js"></script>
</body>
</html>