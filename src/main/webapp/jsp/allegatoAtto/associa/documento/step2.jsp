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
				<s:form id="formAssociaDocumentoAllegatoAttoStep2" cssClass="form-horizontal" novalidate="novalidate" action="associaDocumentoAllegatoAtto_completeStep2" method="post">
					<s:hidden name="uidAllegatoAtto" data-maintain="" />
					<s:if test="gestioneResiduiDisabilitata">
						<s:hidden id="HIDDEN_gestioneResiduiDisabilitata"  value="%{gestioneResiduiDisabilitata}" />
					</s:if>
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3><s:property value="denominazioneAllegatoAtto"/></h3>
					<h4>Associa documenti</h4>
					<div class="wizard">
						<ul class="steps">
							<li data-target="#step1"><span class="badge badge-success">1</span>Ricerca documenti<span class="chevron"></span></li>
							<li class="active" data-target="#step2"><span class="badge">2</span>Associa documenti<span class="chevron"></span></li>
						</ul>
					</div>

					<div class="step-content">
						<div id="step2" class="step-pane active">
							<fieldset class="form-horizontal">
								<p>
									<span class="pull-right">
										<s:submit cssClass="btn btn-primary" value="salva in allegato (selezionati)" />
									</span>
								</p>
								<h4>
									Documenti da collegare -
									Totale spesa: <span class="NumInfo" id="spanTotaleSubdocumentiSpesa"><s:property value="totaleSubdocumentiSpesa"/></span> -
									Totale entrata: <span class="NumInfo" id="spanTotaleSubdocumentiEntrata"><s:property value="totaleSubdocumentiEntrata"/></span>
									<s:if test="gestioneResiduiDisabilitata">
										<a class="tooltip-test" data-html="true" title="Impossibile selezionare quote legate a movimenti residui." href="#">
											<i class="icon-info-sign">&nbsp;
											</i>
										</a>
									</s:if>
								</h4>
								<s:hidden id="totaleSubdocumentiSpesa" name="totaleSubdocumentiSpesa" />
								<s:hidden id="totaleSubdocumentiEntrata" name="totaleSubdocumentiEntrata" />
								<table class="table table-hover tab_left" id="tabellaSubdocumenti" data-referred-span="#spanTotaleSubdocumenti">
									<thead>
										<tr>
											<th class="span1">
												<input type="checkbox" class="tooltip-test check-all" data-original-title="Seleziona tutti" data-referred-table="#tabellaSubdocumenti" />
											</th>
											<th class="span2">Documento</th>
											<th class="span2">Data</th>
											<th class="span1">Stato</th>
											<th>Soggetto</th>
											<th>Quota</th>
											<th>Movimento</th>
											<th>IVA</th>
											<th>Annotazioni</th>
											<th class="tab_Right">Importo quota</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
									
					<%-- 			<s:if test='%{tipoFamigliaDocumento == null || "SPESA".equals(tipoFamigliaDocumento.name())}'>
									<h5>Documenti di spesa</h5>
									<table class="table table-hover tab_left" id="tabellaSubdocumentiSpesa" data-referred-span="#spanTotaleSubdocumentiSpesa">
										<thead>
											<tr>
												<th class="span1">
													<input type="checkbox" class="tooltip-test check-all" data-original-title="Seleziona tutti" data-referred-table="#tabellaSubdocumentiSpesa" />
												</th>
												<th class="span2">Documento</th>
												<th class="span2">Data</th>
												<th class="span1">Stato</th>
												<th>Soggetto</th>
												<th>Quota</th>
												<th>Movimento</th>
												<th class="tab_Right">Importo quota</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</s:if>
								<s:if test='%{tipoFamigliaDocumento == null || "ENTRATA".equals(tipoFamigliaDocumento.name())}'>
									<h5>Documenti di entrata</h5>
									<table class="table table-hover tab_left" id="tabellaSubdocumentiEntrata" data-referred-span="#spanTotaleSubdocumentiEntrata">
										<thead>
											<tr>
												<th class="span1">
													<input type="checkbox" class="tooltip-test check-all" data-original-title="Seleziona tutti" data-referred-table="#tabellaSubdocumentiEntrata" />
												</th>
												<th class="span2">Documento</th>
												<th class="span2">Data</th>
												<th class="span1">Stato</th>
												<th>Soggetto</th>
												<th>Quota</th>
												<th>Movimento</th>
												<th class="tab_Right">Importo quota</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</s:if> --%>
							</fieldset>
						</div>
					</div>

					<p class="margin-medium">
						<s:a cssClass="btn" action="associaDocumentoAllegatoAtto_backToStep1" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<span class="pull-right">
							<s:submit cssClass="btn btn-primary" value="salva in allegato (selezionati)" />
						</span>
					</p>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}allegatoAtto/associaDocumento_step2.js"></script>
	
</body>
</html>