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

	<s:hidden id="nomeAzioneDecentrata" value="%{nomeAzioneDecentrata}" data-maintain="" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:form id="formInserisciTipoBene" cssClass="form-horizontal" novalidate="novalidate" action="ricercaTipoBene_effettuaRicerca">
						<s:hidden name="ambito" id="ambito" />
						<h3 id="titolo">Ricerca tipo bene </h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label for="codice" class="control-label">Codice </label>
										<div class="controls">
											<s:textfield id="codiceCategoriaCespiti" name="tipoBeneCespite.codice" cssClass="span6" placeholder="codice" required="true"  maxlength="500"/>
										</div>
									</div>
									<div class="control-group">
										<label for="descrizione" class="control-label">Descrizione </label>
										<div class="controls">
											<s:textfield id="descrizioneCategoria" name="tipoBeneCespite.descrizione" cssClass="span6" placeholder="descrizione" required="true"  maxlength="500"/>
										</div>
									</div>
									<div class="control-group">
										<label for="categoriaCespitiTipoBene" class="control-label">Categoria cespite </label>
										<div class="controls">
											<s:select list="listaCategoriaCespiti" id="categoriaTipoBene" name="tipoBeneCespite.categoriaCespiti.uid" cssClass="span6" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" />											
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="codiceConto">Conto patrimoniale</label>
										<div class="controls">
											<s:textfield id="codiceContoPatrimoniale" name="tipoBeneCespite.contoPatrimoniale.codice" cssClass="span6" maxlength="200" />
											<span id="descrizioneContoPatrimonale"></span>
											<span class="radio guidata">
												<button type="button" data-selector-conto="<s:property value="contoPatrimonialeTipoBeneSelector"/>" data-selector-classe="<s:property value="contoPatrimonialeTipoBeneSelector.codiceClassePiano"/>" class="btn btn-primary" id="pulsanteCompilazioneGuidataContoPatrimoniale">compilazione guidata</button>
											</span>
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
	<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/conto.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/tipobenecespite/ricercaTipoBene.js"></script>
</body>
</html>