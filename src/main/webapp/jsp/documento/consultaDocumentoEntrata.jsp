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
					<form method="get" action="#">
						<h3>Consultazione Documenti di Entrata</h3>
						<!-- message errore -->
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden name="documento.uid" id="hidden_uidDocumento" />
						
						<s:hidden name="flagDatiIvaAccessibile" id="hidden_flagDatiIvaAccessibile" />
						<s:hidden name="flagRitenuteAccessibile" id="hidden_flagRitenuteAccessibile" />
						<s:hidden name="registrazioneSuSingolaQuota" id="hidden_registrazioneSuSingolaQuota" />

						<h4>
							<s:property value="documento.tipoDocumento.codice"/>&nbsp;-&nbsp;<s:property value="documento.tipoDocumento.descrizione"/>&nbsp;-&nbsp;
							<s:property value="documento.anno"/>&nbsp;-&nbsp;<s:property value="documento.numero"/>&nbsp;-&nbsp;
							<s:if test="%{documento.soggetto != null}">
								<s:property value="documento.soggetto.codiceSoggetto"/>&nbsp;-&nbsp;
								<s:property value="documento.soggetto.denominazione"/>
							</s:if>
						</h4>
						<br>
						<ul class="nav nav-tabs">
							<li class="active"><a href="#documentoEntrata" data-toggle="tab">Documenti di entrata</a></li>
							<li><a href="#quoteDocumentoEntrata" data-toggle="tab">Quote</a></li>
							<s:if test="flagDatiIvaAccessibile">
								<li><a href="#datiIvaDocumentoEntrata" data-toggle="tab">Dati Iva</a></li>
							</s:if>
							<li><a href="#documentiCollegatiDocumentoEntrata" data-toggle="tab">Documenti collegati</a></li>
						</ul>
						
						<div class="tab-content noOverflow">
						
							<div class="tab-pane active" id="documentoEntrata">
								<s:include value="/jsp/documento/consultazione/consultaDocEntrata_documentiEntrata.jsp" />
							</div>
							
							<div class="tab-pane" id="quoteDocumentoEntrata">
								<s:include value="/jsp/documento/consultazione/consultaDocEntrata_quote.jsp" />
							</div>
							<s:if test="flagDatiIvaAccessibile">
								<div class="tab-pane" id="datiIvaDocumentoEntrata">
									<s:include value="/jsp/documento/consultazione/consultaDocEntrata_datiIva.jsp" />
								</div>
							</s:if>
							<div class="tab-pane" id="documentiCollegatiDocumentoEntrata">
								<s:include value="/jsp/documento/consultazione/consultaDocEntrata_documentiCollegati.jsp" />
							</div>
							
						</div>
						<div class="boxOrSpan2">
						</div>		
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
						
					</form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}documento/consultaEntrata.js"></script>
</body>
</html>