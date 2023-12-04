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
					<s:property value="documento.tipoDocumento.codice"/> - <s:property value="documento.anno"/> - <s:property value="documento.numero" /> - 
					<s:property value="documento.dataEmissione" /> - <s:property value="soggetto.codiceSoggetto"/> - <s:property value="soggetto.denominazione" />
					<s:if test = "%{soggetto.codiceFiscale != null}">
					  - <s:property value="soggetto.codiceFiscale" />
					</s:if>
					<s:if test = "%{soggetto.partitaIva != null}">
					   - <s:property value="soggetto.partitaIva" />
					</s:if>
					<%-- <s:if test="documento.collegatoCEC"> - COLLEGATO A CASSA ECONOMALE </s:if> --%>
				</h3>
				<h4>
					Importo documento: <s:property value="documento.importo" /> - Totale da pagare quote: <span id="SPAN_totaleDaPagareQuoteIntestazione"><s:property value="totaleDaPagareQuote" /></span>
				</h4>
				<h4 id="statoDocumento"><s:property value="stato" /></h4>
				<s:hidden id="HIDDEN_anno_datepicker" value="%{documento.anno}" />
				<s:hidden id="HIDDEN_statoOperativoDocumentoCompleto" value="%{statoOperativoDocumentoCompleto}" />
				<s:hidden id="HIDDEN_flagRitenuteAccessibile" value="%{flagRitenuteAccessibile}" />
				<s:hidden id="HIDDEN_flagQuotaConImportoDaDedurre" value="%{flagQuotaConImportoDaDedurre}" />

				<ul class="nav nav-tabs">
					<li <s:if test="!ingressoTabQuote">class="active"</s:if>><a data-toggle="tab" href="#tabAggiornamentoDocumento">Documenti di spesa</a></li>
					<li <s:if test="ingressoTabQuote">class="active"</s:if>><a data-toggle="tab" href="#tabQuoteDocumento">Quote</a></li>
					<li <s:if test="!flagDatiIvaAccessibile">class="hide"</s:if>><a data-toggle="tab" href="#tabDatiIva">Dati iva</a></li>
					<s:if test="flagRitenuteAccessibile">
						<li><a data-toggle="tab" href="#tabRitenute">Ritenute</a></li>
					</s:if>
					<s:if test="flagPenaleAltroAccessibile">
						<li><a data-toggle="tab" href="#tabPenaleAltro">Penale/Altro</a></li>
					</s:if>
					<s:if test="flagNoteCreditoAccessibile">
						<li><a data-toggle="tab" href="#tabNoteCredito">Note credito</a></li>
					</s:if>
				</ul>
				<div class="tab-content noOverflow">
					<div id="tabAggiornamentoDocumento" class="tab-pane fade<s:if test="!ingressoTabQuote"> in active</s:if>">
						<s:include value="/jsp/documento/aggiornamento/aggiornamentoDocumentoSpesa.jsp" />
					</div>
					<div id="tabQuoteDocumento" class="tab-pane fade<s:if test="ingressoTabQuote"> in active</s:if>">
						<s:include value="/jsp/documento/aggiornamento/quoteDocumentoSpesa.jsp" />
					</div>
					<div id="tabDatiIva" class="tab-pane fade <s:if test="!flagDatiIvaAccessibile">hide</s:if>">
						<s:include value="/jsp/documento/aggiornamento/datiIvaDocumentoSpesa.jsp" />
					</div>
					<s:if test="flagRitenuteAccessibile">
						<div id="tabRitenute" class="tab-pane fade">
							<s:include value="/jsp/documento/aggiornamento/ritenuteDocumentoSpesa.jsp" />
						</div>
					</s:if>
					<s:if test="flagPenaleAltroAccessibile">
						<div id="tabPenaleAltro" class="tab-pane fade">
							<s:include value="/jsp/documento/aggiornamento/penaliAltroDocumentoSpesa.jsp" />
						</div>
					</s:if>
					<s:if test="flagNoteCreditoAccessibile">
						<div id="tabNoteCredito" class="tab-pane fade">
							<s:include value="/jsp/documento/aggiornamento/noteCreditoDocumentoSpesa.jsp" />
						</div>
					</s:if>
				</div>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/modaleConfermaProsecuzioneCambioTab.jsp" />
	<s:include value="/jsp/include/modaleConfermaProsecuzioneSuAzione.jsp" />
	<s:include value="/jsp/documento/aggiornamento/include/modaleDatiSospensioneQuota.jsp" />
	<s:include value="/jsp/provvisorioCassa/modaleRicercaProvvisorioCassa.jsp" />
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/codiceFiscale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/ztree/ztree_new.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale_new.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/inserisci_modale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvisorioDiCassa/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
	
	<%-- SIAC-8237 --%>
	<script type="text/javascript" src="/siacbilapp/js/local/ztree/ztreeSAC.js"></script>
	<%-- SIAC-8134 si rende indipendente lo script dalla sezione dei documenti --%>
	<script type="text/javascript" src="/siacbilapp/js/local/strutturaAmministrativaContabile/strutturaAmministrativoContabile.js"></script>
	<%-- SIAC-8237 --%>
	
	<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaSpesa.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaSpesaAggiornamento.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaSpesaQuote.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaSpesaDatiIva.js"></script>
	<s:if test="flagRitenuteAccessibile">
		<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaSpesaRitenute.js"></script>
	</s:if>
	<s:if test="flagPenaleAltroAccessibile">
		<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaSpesaPenaleAltro.js"></script>
	</s:if>
	<s:if test="flagNoteCreditoAccessibile">
		<script type="text/javascript" src="/siacbilapp/js/local/documento/aggiornaSpesaNoteCredito.js"></script>
		<script type="text/javascript" src="/siacbilapp/js/local/documento/include/ricercaSpesaNoteCredito_Modale.js"></script>
	</s:if>

</body>
</html>