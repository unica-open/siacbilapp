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
					Importo documento: <s:property value="documento.importo" /> - Totale da pagare quote: <span id="SPAN_totaleDaPagareQuoteIntestazione"><s:property value="totaleDaPagareQuote" /></span>
				</h4>
				<h4 id="statoDocumento"><s:property value="stato" /></h4>
				<s:hidden id="HIDDEN_anno_datepicker" value="%{documento.anno}" />
				<s:hidden id="HIDDEN_statoOperativoDocumentoCompleto" value="%{statoOperativoDocumentoCompleto}" />

				<ul class="nav nav-tabs">
					<li <s:if test="!ingressoTabQuote">class="active"</s:if>><a data-toggle="tab" href="#tabAggiornamentoDocumento">Documenti di entrata</a></li>
					<li <s:if test="ingressoTabQuote">class="active"</s:if>><a data-toggle="tab" href="#tabQuoteDocumento">Quote</a></li>
					<li <s:if test="!flagDatiIvaAccessibile">class="hide"</s:if>><a data-toggle="tab" href="#tabDatiIva">Dati iva</a></li>
					<s:if test="flagDocumentiCollegatiAccessibile">
						<li><a data-toggle="tab" href="#tabDocumentiCollegati">Documenti Collegati</a></li>
					</s:if>
					<s:if test="flagNoteCreditoAccessibile">
						<li><a data-toggle="tab" href="#tabNoteCredito">Note credito</a></li>
					</s:if>
				</ul>
				<div class="tab-content noOverflow">
					<div id="tabAggiornamentoDocumento" class="tab-pane fade<s:if test="!ingressoTabQuote"> in active</s:if>">
						<s:include value="/jsp/documento/aggiornamento/aggiornamentoDocumentoEntrata.jsp" />
					</div>
					<div id="tabQuoteDocumento" class="tab-pane fade<s:if test="ingressoTabQuote"> in active</s:if>">
						<s:include value="/jsp/documento/aggiornamento/quoteDocumentoEntrata.jsp" />
					</div>
					<div id="tabDatiIva" class="tab-pane fade <s:if test="!flagDatiIvaAccessibile">hide</s:if>">
						<s:include value="/jsp/documento/aggiornamento/datiIvaDocumentoEntrata.jsp" />
					</div>
					<s:if test="flagDocumentiCollegatiAccessibile">
						<div id="tabDocumentiCollegati" class="tab-pane fade">
							<s:include value="/jsp/documento/aggiornamento/documentiCollegatiDocumentoEntrata.jsp" />
						</div>
					</s:if>
					<s:if test="flagNoteCreditoAccessibile">
						<div id="tabNoteCredito" class="tab-pane fade">
							<s:include value="/jsp/documento/aggiornamento/noteCreditoDocumentoEntrata.jsp" />
						</div>
					</s:if>
				</div>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/include/modaleConfermaProsecuzioneCambioTab.jsp" />
	<s:include value="/jsp/include/modaleConfermaProsecuzioneSuAzione.jsp" />
	<s:include value="/jsp/provvisorioCassa/modaleRicercaProvvisorioCassa.jsp" />
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}documento/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale_doc.js"></script>
	<script type="text/javascript" src="${jspath}provvisorioDiCassa/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}movimentoGestione/ricercaAccertamentoOttimizzato.js"></script>
	<script type="text/javascript" src="${jspath}documento/aggiornaEntrata.js"></script>
	<script type="text/javascript" src="${jspath}documento/aggiornaEntrataAggiornamento.js"></script>
	<script type="text/javascript" src="${jspath}documento/aggiornaEntrataQuote.js"></script>
	<script type="text/javascript" src="${jspath}documento/aggiornaEntrataDatiIva.js"></script>
	<s:if test="flagDocumentiCollegatiAccessibile">
		<script type="text/javascript" src="${jspath}documento/aggiornaEntrataDocumentiCollegati.js"></script>
	</s:if>
	<s:if test="flagNoteCreditoAccessibile">
		<script type="text/javascript" src="${jspath}documento/aggiornaEntrataNoteCredito.js"></script>
		<script type="text/javascript" src="${jspath}documento/include/ricercaEntrataNoteCredito_Modale.js"></script>
	</s:if>
	
</body>
</html>