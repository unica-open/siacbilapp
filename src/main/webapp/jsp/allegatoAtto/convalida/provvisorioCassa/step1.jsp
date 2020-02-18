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
			<div class="span12 contentPage">
				<s:form id="formConvalidaAllegatoAttoPerProvvisorioCassaStep1" cssClass="form-horizontal" novalidate="novalidate" action="convalidaAllegatoAttoPerProvvisorioCassa_completeStep1" method="post">
					<s:hidden id="nomeAzioneDecentrata" value="%{nomeAzioneDecentrata}" data-maintain="" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Convalida per provvisorio di cassa</h3>
					<div class="wizard">
						<ul class="steps">
							<li class="active" data-target="#step1"><span class="badge">1</span>Ricerca provvisorio<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Convalida<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<h4 class="step-pane">Provvisorio
								<span id="datiRiferimentoProvvisorioSpan">
									<s:property value="descrizioneCompletaProvvisorioCassa" />
								</span>
							</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<div class="controls">
										<label class="radio inline">
											<input type="radio" value="E" name="provvisorioDiCassa.tipoProvvisorioDiCassa" <s:if test='%{"E".equals(provvisorioDiCassa.tipoProvvisorioDiCassa.name())}'>checked</s:if>>Entrata
										</label>
										<label class="radio inline">
											<input type="radio" value="S" name="provvisorioDiCassa.tipoProvvisorioDiCassa" <s:if test='%{"S".equals(provvisorioDiCassa.tipoProvvisorioDiCassa.name())}'>checked</s:if>>Spesa
										</label>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="annoProvvisorioDiCassa">Anno</label>
									<div class="controls">
										<s:textfield id="annoProvvisorioDiCassa" cssClass="lbTextSmall span2 soloNumeri" name="provvisorioDiCassa.anno" maxlength="7" placeholder="anno" disabled="true" />
										<s:hidden name="provvisorioDiCassa.anno" />
										<span class="al">
											<label class="radio inline" for="numeroProvvisorioDiCassa">Numero</label>
										</span>
										<s:textfield id="numeroProvvisorioDiCassa" cssClass="lbTextSmall span2 soloNumeri" name="provvisorioDiCassa.numero" maxlength="7" placeholder="numero" />
										<span class="radio guidata">
											<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataProvvisorioCassa">compilazione guidata</a>
										</span>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<span class="pull-right">
							<s:submit cssClass="btn btn-primary" value="prosegui" />
						</span>
					</p>
					<s:include value="/jsp/provvisorioCassa/modaleRicercaProvvisorioCassa.jsp" />
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}provvisorioDiCassa/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}allegatoAtto/convalidaProvvisorio_step1.js"></script>
	
</body>
</html>