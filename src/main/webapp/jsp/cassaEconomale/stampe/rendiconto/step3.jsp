<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" method="post" action="cassaEconomaleStampe_enterStep4" novalidate="novalidate" id="formStampaRendicontoCassa">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Stampa Rendiconto</h3>
					<fieldset class="form-horizontal">
						<br />
						<div class="step-content">
							<div class="step-pane active" id="step1">

								<h4 class="step-pane">Soggetto
									<span class="datiRIFSoggetto" id="datiRiferimentoSoggettoSpan"><s:property value="datiSoggetto" /></span>
								</h4>
								<fieldset class="form-horizontal imputazioniContabiliSoggetto">
									<div class="control-group">
										<label for="codiceSoggettoSoggetto" class="control-label">Codice *</label>
										<div class="controls">
											<s:textfield id="codiceSoggettoSoggetto" name="soggetto.codiceSoggetto" cssClass="span2" />
											<span class="radio guidata">
												<button type="button" class="btn btn-primary" id="pulsanteCompilazioneSoggetto">compilazione guidata</button>
											</span>
											<s:hidden id="HIDDEN_denominazioneSoggetto" name="soggetto.denominazione" />
											<s:hidden id="HIDDEN_codiceFiscaleSoggetto" name="soggetto.codiceFiscale" />
										</div>
									</div>
								</fieldset>
								<div id="accordionModalitaPagamentoSoggetto" class="accordion hide">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#collapseModalitaPagamentoSoggetto" data-parent="#accordionModalitaPagamentoSoggetto" data-toggle="collapse" class="accordion-toggle collapsed" data-overlay="">
												Modalit&agrave; di pagamento<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse" id="collapseModalitaPagamentoSoggetto">
											<div class="accordion-inner">
												<table summary="riepilogo indirizzo" class="table tableHover" id="tabellaModalitaPagamentoSoggetto">
													<thead>
														<tr>
															<th class="span1"></th>
															<th class="span2">Numero d'ordine</th>
															<th class="span6">Modalit&agrave;</th>
															<th class="span2"><abbr title="progressivo">Associato a</abbr></th>
															<th class="span1">Stato</th>
														</tr>
													</thead>
													<tbody>
													</tbody>
												</table>
												<s:if test="modalitaPagamentoCessione">
													<s:hidden id="HIDDEN_modalitaPagamentoSoggettoUid" value="%{modalitaPagamentoSoggetto.modalitaPagamentoSoggettoCessione2.uid}" />
												</s:if><s:else>
													<s:hidden id="HIDDEN_modalitaPagamentoSoggettoUid" value="%{modalitaPagamentoSoggetto.uid}" />
												</s:else>
											</div>
										</div>
									</div>
								</div>
								<div class="Border_line"></div>
								<div class="control-group">
									<label for="causale" class="control-label">Causale atto *</label>
									<div class="controls">
										<s:textfield id="causale" name="causale" cssClass="span9" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Struttura Amministrativa *</label>
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
														<br/>
														<button type="button" class="btn btn-primary pull-right" data-toggle="collapse" data-target="#struttAmm" aria-hidden="true">Conferma</button>
<!-- 														<button type="button" id="BUTTON_deselezionaStrutturaAmministrativoContabile" class="btn btn-secondary">deseleziona</button> -->
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
									<label for="causale" class="control-label">Richieste di *<br/><em>anticipi missione</em><br/>comprese nell'allegato atto?</label>
									<div class="controls">
										<label class="radio inline">
											<input type="radio" value="true" name="anticipiSpesaDaInserire" <s:if test="anticipiSpesaDaInserire">checked</s:if>>S&iacute;
										</label>
										<span class="alLeft">
											<label class="radio inline">
												<input type="radio" value="false" name="anticipiSpesaDaInserire" <s:if test="!anticipiSpesaDaInserire">checked</s:if>>No
											</label>
										</span>
									</div>
								</div>
							</div>
						</div>

					</fieldset>
					<p class="margin-large">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn btn-primary pull-right" id="pulsanteStampa" data-toggle="modal" data-target="#modaleConfermaStampaRendicontoCassa">stampa</button>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/cassaEconomale/stampe/rendiconto/modaleConfermaStampa.jsp" />
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.new2.js"></script>
	<script type="text/javascript" src="${jspath}ztree/ztree_new.js"></script>
	<script type="text/javascript" src="${jspath}cassaEconomale/stampe/stampaCECRendiconto.step3.js"></script>

</body>
</html>