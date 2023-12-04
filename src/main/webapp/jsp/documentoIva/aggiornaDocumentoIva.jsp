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
				<s:include value="/jsp/include/messaggi.jsp" />
				<h3>
					<s:property value="documento.tipoDocumento.codice"/> <s:property value="documento.anno"/> - <s:property value="documento.numero" /> - 
					<s:property value="documento.dataEmissione" /> - <s:property value="soggetto.codiceSoggetto"/> - <s:property value="soggetto.denominazione" />
				</h3>
				<s:hidden name="tipoSubdocumentoIva" id="HIDDEN_tipoSubdocumentoIva" />
				
				<ul class="nav nav-tabs">
					<li class="<s:if test="!aperturaTabNotaCredito">active</s:if>"><a data-toggle="tab" href="#TAB_datiIva">Dati iva</a></li>
					<s:if test="flagNotaCreditoIvaDisponibile">
						<li class="<s:if test="aperturaTabNotaCredito">active</s:if>"><a data-toggle="tab" href="#TAB_noteCreditoIva">Note credito iva</a></li>
					</s:if>
				</ul>
				<div class="tab-content noOverflow">
					<div id="TAB_datiIva" class="tab-pane <s:if test="!aperturaTabNotaCredito">active</s:if>">
						<s:form cssClass="form-horizontal" action="aggiornamentoDocumentoIva%{tipoSubdocumentoIva}"
								id="formAggiornamentoDocumentoIva%{tipoSubdocumentoIva}" novalidate="novalidate" >
							<s:include value="/jsp/documentoIva/tabDatiIvaAggiornamento.jsp" />
						</s:form>
					</div>
					<s:if test="flagNotaCreditoIvaDisponibile">
						<div id="TAB_noteCreditoIva" class="tab-pane <s:if test="aperturaTabNotaCredito">active</s:if>">
							<s:form cssClass="form-horizontal" id="formAggiornamentoDocumentoIva%{tipoSubdocumentoIva}Nota" novalidate="novalidate"
									action="aggiornaDocumentoIva%{tipoSubdocumentoIva}_%{flagNotaCreditoIvaPresente ? 'aggiorna' : 'inserisci'}NotaCredito">
								<s:include value="/jsp/documentoIva/tabDatiIvaNota.jsp" />
							</s:form>
						</div>
					</s:if>
				</div>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/documentoIva/documentoIva.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documentoIva/aggiorna.js"></script>
	<s:if test="flagNotaCreditoIvaDisponibile">
		<script type="text/javascript" src="/siacbilapp/js/local/documentoIva/aggiornaNota.js"></script>
	</s:if>
</body>
</html>