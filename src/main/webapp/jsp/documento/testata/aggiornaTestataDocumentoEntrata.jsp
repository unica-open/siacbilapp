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
				<h4>
					Stato documento: <s:property value="documento.statoOperativoDocumento.descrizione"/> - Valido dal: <s:property value="documento.dataInizioValiditaStato" />
				</h4>
				<s:hidden id="HIDDEN_anno_datepicker" value="%{documento.anno}" />
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#tabAggiornamentoDocumento">Documenti di entrata</a></li>
					<li><a data-toggle="tab" href="#tabDatiIva">Dati iva</a></li>
				</ul>
				<div class="tab-content noOverflow">
					<div id="tabAggiornamentoDocumento" class="tab-pane fade<s:if test="!ingressoTabQuote"> in active</s:if>">
						<s:include value="/jsp/documento/aggiornamento/testata/aggiornamentoTestataDocumentoEntrata.jsp" />
					</div>
					<div id="tabDatiIva" class="tab-pane fade">
						<s:include value="/jsp/documento/aggiornamento/testata/datiIvaTestataDocumentoEntrata.jsp" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/modaleConfermaProsecuzioneCambioTab.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}documento/aggiornaTestataEntrata.js"></script>
	<script type="text/javascript" src="${jspath}documento/aggiornaTestataEntrataAggiornamento.js"></script>
	<script type="text/javascript" src="${jspath}documento/aggiornaTestataEntrataDatiIva.js"></script>

</body>
</html>