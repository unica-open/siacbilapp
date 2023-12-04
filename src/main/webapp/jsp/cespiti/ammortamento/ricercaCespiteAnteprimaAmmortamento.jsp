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
					<s:form id="formRicercaCespiteAnteprimaAmmortamento" cssClass="form-horizontal" novalidate="novalidate" action="ricercaCespiteAnteprimaAmmortamento_effettuaRicerca">
						<s:hidden name="ambito" id="ambito" />
						<h3 id="titolo">Ricerca cespite </h3>
						
						<s:hidden id="HIDDEN_annoDettaglio" name="annoAmmortamentoAnnuo"/>
						<s:hidden id="HIDDEN_segnoDettaglio" name="segno"/>
						<s:hidden id="HIDDEN_uidDettaglioAnteprima" name="uidDettaglioAnteprima"/>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
								
									<div class="control-group">
										<label class="control-label" for="codiceConto">Conto patrimoniale</label>
										<div class="controls">
											<s:textfield id="codiceContoPatrimoniale" name="codiceConto" cssClass="span6" maxlength="200" disabled="true"/>
											<%-- <span id="descrizioneContoPatrimonale"></span> --%>
											<span class="radio guidata">
												<button type="button" disabled="disabled" <%-- data-selector-conto="<s:property value="contoPatrimonialeTipoBeneSelector"/>" data-selector-classe="<s:property value="contoPatrimonialeTipoBeneSelector.codiceClassePiano"/>" --%> class="btn btn-primary" id="pulsanteCompilazioneGuidataContoPatrimoniale">compilazione guidata</button>
											</span>
										</div>
									</div>
									
									<div class="control-group">
										<label for="categoriaCespiti" class="control-label">Categoria cespite </label>
										<div class="controls">
											<s:select list="listaCategoriaCespite" id="categoriaCespiti" name="categoriaCespiti.uid" cssClass="span6" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" />											
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="cespiteTipoBene">Tipo Bene </label>
										<div class="controls">
											<select id="cespiteTipoBene" name="tipoBeneCespite.uid" class="span6">
												<option value="0"></option>
												<s:iterator value="listaTipoBene" var="ltbc">
													<option value="<s:property value="%{#ltbc.uid}"/>"
														<s:if test="%{#ltbc.contoPatrimoniale != null}">
															data-conto-patrimoniale="<s:property value="%{#ltbc.contoPatrimoniale.codice + ' - ' + #ltbc.contoPatrimoniale.descrizione}"/>"
														</s:if>
														>
														<s:property value="%{#ltbc.codice + ' - ' + #ltbc.descrizione}"/>
													</option>
												</s:iterator>
											</select>
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
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/ammortamento/ricercaCespiteAnteprimaAmmortamento.js"></script>
</body>
</html>