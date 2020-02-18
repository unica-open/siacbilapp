<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
				<s:form cssClass="form-horizontal" action="#" novalidate="novalidate" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Emissione ordinativi di pagamento</h3>
					<div class="wizard">
						<ul class="steps">
							<li class="active" data-target="#step1"><span class="badge">1</span>Ricerca dettagli<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Emissione<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<div class="control-group margin-medium">
									<span class="control-label">Emetti per</span>
									<div class="controls">
										<label class="radio inline">
											<input type="radio" value="ELENCO" name="tipoEmissioneOrdinativo" data-action="emissioneOrdinativiPagamentoElenco_completeStep1.do"
												<s:if test='%{"ELENCO".equals(tipoEmissioneOrdinativo.name())}'>checked</s:if>>elenchi allegato atto
										</label>
										<label class="radio inline">
											<input type="radio" value="QUOTE" name="tipoEmissioneOrdinativo" data-action="emissioneOrdinativiPagamentoQuota_completeStep1.do"
												<s:if test='%{"QUOTE".equals(tipoEmissioneOrdinativo.name())}'>checked</s:if>>dettagli quote
										</label>
									</div>
								</div>
								<h4 class="step-pane">
									Provvedimento <span id="datiRiferimentoAttoAmministrativoSpan">
										<s:if test='%{attoAmministrativo != null && (attoAmministrativo.anno ne null && attoAmministrativo.anno != "") && (attoAmministrativo.numero ne null && attoAmministrativo.numero != "") && (tipoAtto.uid ne null && tipoAtto.uid != "")}'>
											<s:property value="%{tipoAtto.descrizione+ '/'+ attoAmministrativo.anno + ' / ' + attoAmministrativo.numero + ' - ' + attoAmministrativo.oggetto}" />
										</s:if>
									</span>
								</h4>
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label class="control-label" for="annoAttoAmministrativo">Anno</label>
										<div class="controls">
											<s:textfield id="annoAttoAmministrativo" cssClass="lbTextSmall span1 soloNumeri" name="attoAmministrativo.anno" />
											<span class="al">
												<label class="radio inline" for="numeroAttoAmministrativo">Numero</label>
											</span>
											<s:textfield id="numeroAttoAmministrativo" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" maxlength="7" />
											<span class="al">
												<label class="radio inline" for="tipoAtto">Tipo</label>
											</span>
											<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid"
												id="tipoAtto" cssClass="span4" headerKey="0" headerValue="" />
											<s:hidden id="statoOperativoAttoAmministrativo" name="attoAmministrativo.statoOperativo" />
											<span class="radio guidata">
												<a id="pulsanteApriModaleProvvedimento" class="btn btn-primary">compilazione guidata</a>
											</span>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">Struttura Amministrativa</label>
										<div class="controls">
											<div class="accordion span8 struttAmm" id="accordionStrutturaAmministrativaContabileAttoAmministrativo">
												<div class="accordion-group">
													<div class="accordion-heading">
														<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa" href="#collapseStrutturaAmministrativaContabileAttoAmministrativo"
																data-parent="#accordionStrutturaAmministrativaContabileAttoAmministrativo">
															<span id="SPAN_StrutturaAmministrativoContabileAttoAmministrativo">Seleziona la Struttura amministrativa</span>
														</a>
													</div>
													<div id="collapseStrutturaAmministrativaContabileAttoAmministrativo" class="accordion-body collapse">
														<div class="accordion-inner">
															<ul id="treeStruttAmmAttoAmministrativo" class="ztree treeStruttAmm"></ul>
														</div>
													</div>
												</div>
											</div>
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid" name="strutturaAmministrativoContabile.uid" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoCodice" name="strutturaAmministrativoContabileAttoAmministrativo.codice" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoDescrizione" name="strutturaAmministrativoContabileAttoAmministrativo.descrizione" />
										</div>
									</div>
								</fieldset>
								<div class="emissDettaglioAtto <s:if test='%{tipoEmissioneOrdinativo == null || "ELENCO".equals(tipoEmissioneOrdinativo.name())}'>hide</s:if>" id="datiRicercaQuote">
									<h4 class="step-pane">Elenco
										<span id="descrizioneCompletaElenco">
											<s:if test='%{elenco != null && elenco.anno != null && elenco.numero != null}'>
												<s:property value="%{elenco.anno + '/' + elenco.numero}" />
											</s:if>
										</span>
									</h4>
									<fieldset class="form-horizontal margin-large">
										<div class="control-group">
											<label class="control-label" for="annoElencoDocumentiAllegato">Anno</label>
											<div class="controls">
												<s:textfield id="annoElencoDocumentiAllegato" name="elencoDocumentiAllegato.anno" cssClass="span1 soloNumeri" maxlength="4" />
												<span class="al">
													<label class="radio inline" for="numeroElencoDocumentiAllegato">Da numero</label>
												</span>
												<s:textfield id="numeroElencoDa" name="numeroElencoDa" cssClass="span2 soloNumeri" maxlength="7" />
												<span class="al">
													<label class="radio inline" for="numeroElencoA">a numero</label>
												</span>
												<s:textfield id="numeroElencoA" name="numeroElencoA" cssClass="span2 soloNumeri" maxlength="7" />
												<span class="radio guidata">
													<button type="button" class="btn btn-primary" id="pulsanteApriModaleCompilazioneGuidataElencoDocumentiAllegato">
														compilazione guidata
													</button>
												</span>
											</div>
										</div>
									</fieldset>
									<h4 class="step-pane">Capitolo
										<span class="datiRIFCapitolo" id="datiRiferimentoCapitoloSpan">
											<s:if test="%{capitolo != null && capitolo.annoCapitolo != null && capitolo.numeroCapitolo != null && capitolo.numeroArticolo != null && (!gestioneUEB || capitolo.numeroUEB != null)}">
												: <s:property value="capitolo.annoCapitolo" /> / <s:property value="capitolo.numeroCapitolo" /> / <s:property value="capitolo.numeroArticolo" />
												<s:if test="%{gestioneUEB}">
													/ <s:property value="capitolo.numeroUEB" />
												</s:if>
											</s:if>
										</span>
									</h4>
									<fieldset class="form-horizontal">
										<div class="control-group">
											<label for="annoCapitolo" class="control-label">Anno</label>
											<div class="controls">
												<s:textfield id="annoCapitolo" name="capitolo.annoCapitolo" value="%{annoEsercizioInt}" cssClass="lbTextSmall" disabled="true" data-maintain="" />
												<s:hidden id="annoCapitoloHidden" name="capitolo.annoCapitolo" value="%{annoEsercizioInt}" data-maintain="" />
												<span class="al">
													<label for="numeroCapitolo" class="radio inline">Capitolo</label>
												</span>
												<s:textfield id="numeroCapitolo" name="capitolo.numeroCapitolo" cssClass="lbTextSmall soloNumeri span2" maxlength="30" placeholder="%{'capitolo'}" />
												<span class="al">
													<label for="numeroArticolo" class="radio inline">Articolo</label>
												</span>
												<s:textfield id="numeroArticolo" name="capitolo.numeroArticolo" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'articolo'}" />
												<s:if test="%{gestioneUEB}">
													<span class="al">
														<label for="numeroUEB" class="radio inline">UEB</label>
													</span>
													<s:textfield id="numeroUEB" name="capitolo.numeroUEB" cssClass="lbTextSmall soloNumeri span2" placeholder="%{'UEB'}" />
												</s:if><s:else>
													<s:hidden id="numeroUEB" name="capitolo.numeroUEB" value="1" />
												</s:else>
												<span class="radio guidata">
													<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataCapitolo">compilazione guidata</button>
												</span>
											</div>
										</div>
									</fieldset>
									<h4 class="step-pane">Soggetto
										<span id="descrizioneCompletaSoggetto">
											<s:if test='%{soggetto != null && (soggetto.codice ne null && soggetto.codice != "") && (soggetto.descrizione ne null && soggetto.descrizione != "") && (soggetto.codiceFiscale ne null && soggetto.codiceFiscale != "")}'>
												<s:property value="%{soggetto.codice + ' - ' + soggetto.descrizione + ' - ' + soggetto.codiceFiscale}" />
											</s:if>
										</span>
									</h4>
									<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
									<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />
									<fieldset class="form-horizontal">
										<div class="control-group">
											<label class="control-label" for="codiceSoggetto">Codice</label>
											<div class="controls">
												<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
												<span class="radio guidata">
													<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</a>
												</span>
											</div>
										</div>
									</fieldset>
									<h4>Altri dati</h4>
									<fieldset class="form-horizontal">
										<div class="control-group">
											<label class="control-label" for="distinta">Distinta</label>
											<div class="controls">
												<s:select list="listaDistinta" name="distintaDaCercare.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
													headerKey="0" headerValue="" cssClass="lbTextSmall span6" id="distinta" />
											</div>
										</div>
									</fieldset>
								</div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset cssClass="btn btn-secondary" value="annulla" />
						<s:submit cssClass="btn btn-primary pull-right" value="prosegui" />
					</p>
				</s:form>
				<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
				<s:include value="/jsp/allegatoAtto/associaElencoDocumentiAllegato_modale.jsp" />
				<s:include value="/jsp/capUscitaGestione/selezionaCapitolo_modale.jsp" />
				<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
				
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}predocumento/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale.js"></script>
	<script type="text/javascript" src="${jspath}allegatoAtto/gestioneElenco.js"></script>
	<script type="text/javascript" src="${jspath}capitolo/ricercaCapitoloModale.js"></script>
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}ordinativo/emissionePagamentoStep1.js"></script>

</body>
</html>