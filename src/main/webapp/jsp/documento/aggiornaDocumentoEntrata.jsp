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
				<!-- SIAC-7562 - 25/06/2020 - CM e GM -->
				<s:if test="documento.tipoDocumento.codice == 'FTV' && model.statoSDIdescrizione != null">
					<h4 id="statoSDI">Stato SDI: &nbsp;<s:property value="model.statoSDIdescrizione"/>&nbsp;
						<s:if test="documento.dataCambioStatoFel != null">
							dal 
							<s:property value="documento.dataCambioStatoFel"/>    
						</s:if>
					</h4>
				</s:if>
				<br>
				<s:hidden id="HIDDEN_anno_datepicker" value="%{documento.anno}" />
				<s:hidden id="HIDDEN_statoOperativoDocumentoCompleto" value="%{statoOperativoDocumentoCompleto}" />

				<ul class="nav nav-tabs">
					<%-- SIAC-6988 INIZIO FL--%>
					<s:if test="!flagStatoSDIInviatoFEL">
						<li <s:if test="!ingressoTabQuote">class="active"</s:if>><a data-toggle="tab" href="#tabAggiornamentoDocumento">Documenti di entrata</a></li>
						<li <s:if test="ingressoTabQuote">class="active"</s:if>><a data-toggle="tab" href="#tabQuoteDocumento">Quote</a></li>
						<li <s:if test="!flagDatiIvaAccessibile">class="hide"</s:if>><a data-toggle="tab" href="#tabDatiIva">Dati iva</a></li>
						<s:if test="flagDocumentiCollegatiAccessibile">
							<li><a data-toggle="tab" href="#tabDocumentiCollegati">Documenti Collegati</a></li>
						</s:if>
					</s:if>
					<s:else>
						<li <s:if test="!ingressoTabQuote">class="active"</s:if>><a data-toggle="tab" href="#documentoEntrata">Documenti di entrata</a></li>
						<li><a href="#quoteDocumentoEntrata" data-toggle="tab">Quote</a></li>
							<s:if test="flagDatiIvaAccessibile">
								<li><a href="#datiIvaDocumentoEntrata" data-toggle="tab">Dati Iva</a></li>
							</s:if>
							<li><a href="#documentiCollegatiDocumentoEntrata" data-toggle="tab">Documenti collegati</a></li>
					</s:else>
					<%-- SIAC-6988 FINE FL--%>
					<s:if test="flagNoteCreditoAccessibile">
						<li><a data-toggle="tab" href="#tabNoteCredito">Note credito</a></li>
					</s:if>
				</ul>
				<div class="tab-content noOverflow">

					<%-- SIAC-6988 INIZIO FL--%>
					<s:if test="!flagStatoSDIInviatoFEL">
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
					</s:if>
					<s:else>
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
							<div id="documentiCollegatiDocumentoEntrata" class="tab-pane fade">
								<s:include value="/jsp/documento/aggiornamento/documentiCollegatiDocumentoEntrata.jsp" />
							</div>
					</s:else>
					<%-- SIAC-6988 FINE FL--%>
					
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
	<script type="text/javascript" src="/siacbilapp/js/local/codiceFiscale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale_doc.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvisorioDiCassa/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaAccertamentoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaEntrata.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaEntrataAggiornamento.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaEntrataQuote.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaEntrataDatiIva.js"></script>
	<s:if test="flagDocumentiCollegatiAccessibile">
		<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaEntrataDocumentiCollegati.js"></script>
	</s:if>
	<s:if test="flagNoteCreditoAccessibile">
		<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaEntrataNoteCredito.js"></script>
		<script type="text/javascript" src="/siacbilapp/js/local/documento/include/ricercaEntrataNoteCredito_Modale.js"></script>
	</s:if>
	
</body>
</html>