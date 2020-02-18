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
				<s:hidden name="provvisorio.importoDaRegolarizzare" id="HIDDEN_importoDaRegolarizzare" />
				<s:form id="formAssociaDocumentoProvvisorioStep2" cssClass="form-horizontal" novalidate="novalidate" action="associaQuoteSpesaAProvvisorioDiCassa_completeStep2" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h4>Associa documenti al provvisorio n. <s:property value="provvisorio.numero"/> 
					 <s:if test="%{provvisorio.dataEmissione != null}"> del <s:property value="provvisorio.dataEmissione"/></s:if> 
					</h4>
					<div class="wizard">
						<ul class="steps">
							<li data-target="#step1"><span class="badge badge-success">1</span>Ricerca documenti<span class="chevron"></span></li>
							<li class="active" data-target="#step2"><span class="badge">2</span>Associa documenti<span class="chevron"></span></li>
						</ul>
					</div>

					<div class="step-content">
						<div id="step2" class="step-pane active">
							<fieldset class="form-horizontal">
								<s:if test="%{!pulsanteAggiornaNascosto}">
									<p>
										<span class="pull-right">
											<button type="button" class="btn btn-primary" data-submit>aggiorna quote</button>
										</span>
									</p>
								</s:if>
								<h4>
									Documenti da collegare -
									Totale quote: <span class="NumInfo" id="spanTotaleSubdocumentiSpesa"><s:property value="totaleSubdocumentiSpesa"/></span> - 
									Importo da regolarizzare: <span class="NumInfo" id="spanImportoDaRegolarizzare"><s:property value="provvisorio.importoDaRegolarizzare"/></span>
								</h4>
								<s:hidden id="totaleSubdocumentiSpesa" name="totaleSubdocumentiSpesa" />
								<table class="table table-hover tab_left" id="tabellaSubdocumenti" data-referred-span="#spanTotaleSubdocumenti">
									<thead>
										<tr>
											<th class="span1">
												<s:if test="!pulsanteAggiornaNascosto">
													<input type="checkbox" class="tooltip-test check-all" data-original-title="Seleziona tutti" data-referred-table="#tabellaSubdocumenti" />
												</s:if>
											</th>
											<th class="span2">Documento</th>
											<th class="span2">Data</th>
											<th class="span1">Stato</th>
											<th>Soggetto</th>
											<th>Quota</th>
											<th>Movimento</th>
											<th>IVA</th>
											<th>Annotazioni</th>
											<th>Provv. Cassa</th>
											<th class="tab_Right">Importo quota</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:a cssClass="btn" action="associaQuoteSpesaAProvvisorioDiCassa_backToStep1" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<s:if test="%{!pulsanteAggiornaNascosto}">
							<span class="pull-right">
								<button type="button" class="btn btn-primary" data-submit>aggiorna quote</button>
							</span>
						</s:if>
					</p>
				</s:form>
				
				<s:form id="formAssociaDocumentoProvvisorioStep2_hidden" cssClass="hide" novalidate="novalidate" action="associaQuoteSpesaAProvvisorioDiCassa_completeStep2" method="post"></s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}provvisorioDiCassa/associaQuoteSpesa_step2.js"></script>
	
</body>
</html>