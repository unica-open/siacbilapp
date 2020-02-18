<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@ taglib uri="/siac-tags" prefix="si" %>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form id="formRicercaAllegatoAtto" cssClass="form-horizontal" novalidate="novalidate" action="%{nomeAzioneRicerca}" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Ricerca atto contabile / allegato</h3>
					<p>&Eacute; necessario inserire almeno un criterio di ricerca.</p>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<p>
								<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
							</p>
							<h4>Dati</h4>
							<div class="control-group">
								<label class="control-label" for="annoBilancio">Anno di bilancio</label>
								<div class="controls">
									<s:textfield id="annoBilancio" name="annoBilancio" maxlength="4" cssClass="span1 soloNumeri" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="causaleAllegatoAtto">Causale</label>
								<div class="controls">
									<s:textfield id="causaleAllegatoAtto" name="allegatoAtto.causale" maxlength="500" cssClass="span9" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="statoOperativoAllegatoAttoAllegatoAtto">Stato</label>
								<div class="controls">
									<s:if test="%{disabilitaSelezioneStato}">
										<s:select list="listaStatoOperativoAllegatoAtto" id="statoOperativoAllegatoAttoAllegatoAtto"
										name="allegatoAtto.statoOperativoAllegatoAtto" cssClass="span6" headerKey="" headerValue="" listValue="descrizione" disabled="disabled"/>
										<s:hidden name="allegatoAtto.statoOperativoAllegatoAtto" value="statoOperativoAllegatoAttoDefault"/>
									</s:if><s:else>
										<s:select list="listaStatoOperativoAllegatoAtto" id="statoOperativoAllegatoAttoAllegatoAtto"
										name="allegatoAtto.statoOperativoAllegatoAtto" cssClass="span6" headerKey="" headerValue="" listValue="descrizione" />
									</s:else>
								</div>
								
							</div>
							
							<div class="control-group">
								<label class="control-label" for="flagSoggettoDurc">Con DURC</label>
								<div class="controls">
									<select id="flagSoggettoDurc" name="flagSoggettoDurc">
										<option value=""  <s:if test='flagSoggettoDurc == ""' >selected</s:if>>Non si applica</option>
										<option value="S">S&iacute;</option>
										<option value="N">No</option>
									</select>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Data di scadenza</label>
								<div class="controls">
									<span class="al">
										<label class="radio inline" for="dataScadenzaDa">Da</label>
									</span>
									<s:textfield id="dataScadenzaDa" name="dataScadenzaDa" cssClass="span2 datepicker" maxlength="10" />
									<span class="al">
										<label class="radio inline" for="dataScadenzaA">A</label>
									</span>
									<s:textfield id="dataScadenzaA" name="dataScadenzaA" cssClass="span2 datepicker" maxlength="10" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="flagRitenute">Contiene ritenute</label>
								<div class="controls">
									<select id="flagRitenute" name="flagRitenute">
										<option value=""  <s:if test='flagRitenute == ""' >selected</s:if>>Non si applica</option>
										<option value="S" <s:if test='flagRitenute == "S"'>selected</s:if>>S&iacute;</option>
										<option value="N" <s:if test='flagRitenute == "N"'>selected</s:if>>No</option>
									</select>
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
										<s:textfield id="numeroAttoAmministrativo" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" />
										<span class="al">
											<label class="radio inline" for="tipoAtto">Tipo</label>
										</span>
										<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid"
											id="tipoAtto" cssClass="span4" headerKey="0" headerValue="" />
										<s:hidden id="statoOperativoAttoAmministrativo" name="attoAmministrativo.statoOperativo" />
										<span class="radio guidata">
											<a href="#" id="pulsanteApriModaleProvvedimento" class="btn btn-primary">compilazione guidata</a>
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
							
							<%-- <h4 class="step-pane">Soggetto
								<span id="descrizioneCompletaSoggetto">
									<s:if test='%{soggetto != null && (soggetto.codice ne null && soggetto.codice != "") && (soggetto.descrizione ne null && soggetto.descrizione != "") && (soggetto.codiceFiscale ne null && soggetto.codiceFiscale != "")}'>
										<s:property value="%{soggetto.codice + ' - ' + soggetto.descrizione + ' - ' + soggetto.codiceFiscale}" />
									</s:if>
								</span>
							</h4>
							<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
							<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />
							<div class="control-group">
								<label class="control-label" for="codiceSoggetto">Codice</label>
								<div class="controls">
									<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
									<span class="radio guidata">
										<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</a>
									</span>
								</div>
							</div> --%>
							
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
										<s:textfield id="annoElencoDocumentiAllegato" name="elencoDocumentiAllegato.anno" cssClass="span1 soloNumeri"
											maxlength="4" />
										<span class="al">
											<label class="radio inline" for="numeroElencoDocumentiAllegato">Numero</label>
										</span>
										<s:textfield id="numeroElencoDocumentiAllegato" name="elencoDocumentiAllegato.numero" cssClass="span2 soloNumeri"
											maxlength="7" />
										<span class="radio guidata">
											<button type="button" class="btn btn-primary" id="pulsanteApriModaleCompilazioneGuidataElencoDocumentiAllegato">
												compilazione guidata
											</button>
										</span>
									</div>
								</div>
							</fieldset>

							<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
							<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />

							<h4 class="step-pane">Soggetto
								<span id="descrizioneCompletaSoggetto"><s:property value="descrizioneCompletaSoggetto"/></span>
							</h4>
							<div class="control-group">
								<label class="control-label" for="codiceSoggetto">Codice</label>
								<div class="controls">
									<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
									<span class="radio guidata">
										<a href="#" id="pulsanteApriModaleSoggetto" class="btn btn-primary">compilazione guidata</a>
									</span>
								</div>
							</div>

							<h4 class="step-pane">Impegno
								<span class="datiRIFImpegno" id="datiRiferimentoImpegnoSpan"><s:property value="descrizioneCompletaMovimentoGestione" /></span>
							</h4>
							<fieldset class="form-horizontal imputazioniContabiliMovimentoGestione">
								<div class="control-group">
									<label for="annoMovimentoMovimentoGestione" class="control-label">Anno</label>
									<div class="controls">
										<s:textfield id="annoMovimentoMovimentoGestione" name="impegno.annoMovimento" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'anno'}" />
										<span class="alRight">
											<label for="numeroMovimentoGestione" class="radio inline">Numero</label>
										</span>
										<si:plainstringtextfield id="numeroMovimentoGestione" name="impegno.numero" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'numero'}" />
										
										<span class="alRight">
											<label for="numeroSubMovimentoGestione" class="radio inline">Subimpegno</label>
										</span>
										<si:plainstringtextfield id="numeroSubMovimentoGestione" name="subImpegno.numero" cssClass="lbTextSmall soloNumeri span2" maxlength="7" placeholder="%{'numero subimpegno'}" />
										<span class="radio guidata">
											<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataMovimentoGestione">compilazione guidata</button>
										</span>
										
									</div>
								</div>	
							</fieldset>
							
						</div>
					</div>

					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn btn-secondary reset">annulla</button>
						<span class="pull-right">
							<s:submit cssClass="btn btn-primary" value="cerca" />
						</span>
					</p>
					<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" />
					
					<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp">
						<s:param name="annoIsNotRequired">true</s:param>
					</s:include>
					
					<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
					<s:include value="/jsp/allegatoAtto/associaElencoDocumentiAllegato_modale.jsp" />

				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}movimentoGestione/ricercaImpegnoOttimizzato.js"></script>	
	<script type="text/javascript" src="${jspath}soggetto/ricerca.new2.js"></script>
	<script type="text/javascript" src="${jspath}predocumento/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale.js"></script>
	<script type="text/javascript" src="${jspath}allegatoAtto/gestioneElenco.js"></script>
	<script type="text/javascript" src="${jspath}allegatoAtto/ricerca.js"></script>
</body>
</html>