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
					<s:form id="formInserisciTipoBene" cssClass="form-horizontal" novalidate="novalidate" action="inserisciCategoriaCespiti_salva">
						<h3 id="titolo">Inserisci categoria cespite </h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label for="codice" class="control-label">Codice *</label>
										<div class="controls">
											<s:textfield id="codiceCategoriaCespiti" name="categoriaCespiti.codice" cssClass="span6" placeholder="codice" required="true"  maxlength="500"/>
										</div>
									</div>
									<div class="control-group">
										<label for="descrizione" class="control-label">Descrizione *</label>
										<div class="controls">
											<s:textfield id="descrizioneCategoria" name="categoriaCespiti.descrizione" cssClass="span6" placeholder="descrizione" required="true"  maxlength="500"/>
										</div>
									</div>
									<div class="control-group">
										<label for="aliquotaAnnua" class="control-label">Aliquota annua &#37; *</label>
										<div class="controls">
											<s:textfield id="aliquotaAnnua" name="categoriaCespiti.aliquotaAnnua" cssClass="lbTextSmall span6 soloNumeri " placeholder="aliquota" required="true"  maxlength="5"/>
										</div>
									</div>
									<div class="control-group">
										<label for="tipoCalcoloPrimoAnno" class="control-label">Tipo calcolo primo anno *</label>
										<div class="controls">
											<s:select list="listaTipoCalcolo" id="tipoCalcoloCategoria" name="categoriaCespiti.categoriaCalcoloTipoCespite.uid" cssClass="span6" headerKey="0" headerValue="" listKey="uid" listValue="%{descrizione}" required="true" />											
										</div>
									</div>
								</fieldset>										
							</div>
						</div>
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn reset">annulla</button>
							<s:submit cssClass="btn btn-primary pull-right" value="salva"/>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}cespiti/categoriacespiti/inserisciCategoriaCespiti.js"></script>
</body>
</html>