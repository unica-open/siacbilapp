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
					<s:form id="formInserisciCausalePagamentoEntrata" cssClass="form-horizontal" novalidate="novalidate" action="inserimentoCausaleEntrataSalva">

						<h3>Inserimento causale incasso</h3>

						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<div class="step-pane active" id="step1">
								<h4>Dati causale</h4>
								<fieldset class="form-horizontal" id="inserisciCausalePagamentoSpesa" >
									<div class="control-group">
										<label class="control-label">Struttura Amministrativa</label>
										<div class="controls">
											<div class="accordion span9 struttAmm">
												<div class="accordion-group">
													<div class="accordion-heading">
														<a class="accordion-toggle" href="#struttAmm" data-toggle="collapse">
															<span id="SPAN_StrutturaAmministrativoContabile">Seleziona la Struttura amministrativa</span>
														</a>
													</div>
													<div id="struttAmm" class="accordion-body collapse">
														<div class="accordion-inner">
															<ul id="treeStruttAmm" class="ztree treeStruttAmm"></ul>
														</div>
													</div>
												</div>
											</div>
																	
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="strutturaAmministrativoContabile.uid" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="strutturaAmministrativoContabile.codice" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="strutturaAmministrativoContabile.descrizione" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="tipoCausale">Tipo causale *</label>
										<div class="controls">
											<s:select list="listaTipoCausale" listKey="uid" listValue="%{codice + '-' + descrizione}" name="tipoCausale.uid" id="tipoCausale" cssClass="lbTextSmall span9" headerKey=""
												headerValue="" />
										</div>
									</div>

									<div class="control-group">
										<label class="control-label" for="codiceCausale">Causale *</label>
										<div class="controls">
											<s:textfield id="codiceCausale" cssClass="span2" name="causale.codice" placeholder="codice" required="true" />
											<s:textfield id="descrizioneCausale" cssClass="span7" name="causale.descrizione" placeholder="descrizione" required="true" />
										</div>
									</div>

									<div id="accordionImputazioniContabili" class="accordion">
										<div class="accordion-group">
											<div class="accordion-heading">
												<a href="#collapseImputazioniContabili" data-parent="#accordionImputazioniContabili" data-toggle="collapse" class="accordion-toggle collapsed">
													Imputazioni contabili<span class="icon">&nbsp;</span>
												</a>
											</div>
											<div class="accordion-body collapse" id="collapseImputazioniContabili">
												<s:include value="/jsp/predocumento/imputazioniContabiliPreDocumentoEntrata.jsp" />
											</div>
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
	
	<s:include value="/jsp/capEntrataGestione/selezionaCapitolo_modale.jsp" />
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/movimentoGestione/modaleAccertamento.jsp" />
	<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale_new.jsp" />
	<%-- MODALE USCITA GESTIONE --%>	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/codiceFiscale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/ricercaCapitoloModale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaAccertamentoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/ztree/ztree_new.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale_new.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/predocumento/predocumento.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/causale/inserisciCausaleEntrata.js"></script>
</body>
</html>