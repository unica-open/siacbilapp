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
				<s:hidden id="hidden_uidCausale" value="%{causaleEP.uid}" />
				<s:hidden id="hidden_uidEvento" value="%{evento.uid}" />
				<s:hidden id="hidden_ambito" value="%{ambito}" />
				<s:hidden nam="HIDDEN_uidTipoEventoDaRicerca" name="uidTipoEventoDaRicerca" id="uidTipoEventoDaRicerca"/>
				<s:hidden nam="HIDDEN_uidEventoDaRicerca" name="uidEventoDaRicerca" id="uidEventoDaRicerca" />
				<s:hidden nam="HIDDEN_uidCausaleEPDaRicerca" name="uidCausaleEPDaRicerca" id="uidCausaleEPDaRicerca" />
				
				<s:form cssClass="form-horizontal" action="ricercaPrimaNotaIntegrataGSA_effettuaRicerca" novalidate="novalidate" id="formPrimaNotaIntegrata">
					<s:include value="/jsp/include/messaggi.jsp" />
				
					<h3>Ricerca prima nota integrata</h3>
					<p>&Eacute; necessario inserire almeno un criterio di ricerca.</p>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
								<p>
									<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
								</p>
								<h4 class="step-pane">Dati </h4>
								<div class="control-group">
									<label class="control-label"></label>
									<div class="controls">
										<span class="al">
											<label class="radio inline" for="tipoElencoEntrata">Entrate</label>
										</span>
										<input id="tipoElencoEntrata" type="radio" name="tipoElenco" value="E" <s:if test='%{"E".equals(tipoElenco)}'>checked</s:if> />
										<span class="al">
											<label class="radio inline" for="tipoElencoSpesa">Spese</label>
										</span>
										<input id="tipoElencoSpesa" type="radio" name="tipoElenco" value="S" <s:if test='%{"S".equals(tipoElenco)}'>checked</s:if> />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="tipoEvento">Tipo evento</label>
									<div class="controls">
										<s:select list="listaTipoEvento" name="tipoEvento.uid" cssClass="span6" id="tipoEvento" headerKey="" headerValue=""
											listValue="%{codice + ' - ' + descrizione}" listKey="uid" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="evento">Evento</label>
									<div class="controls">
										<s:select list="listaEvento" name="evento.uid" cssClass="span6" id="evento" headerKey="" headerValue=""
											listValue="%{codice + ' - ' + descrizione}" listKey="uid" disabled="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="causale">
										Causale&nbsp;
										<a href="#" class="tooltip-test" data-original-title="selezionare l'Evento">
											<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare l'Evento</span></i>
										</a>
									</label>
									<div class="controls">
										<s:select list="listaCausaliEP" name="causaleEP.uid" cssClass="span6" id="causale" headerKey="" headerValue=""
											listValue="%{codice + ' - ' + descrizione}" listKey="uid" disabled="true"/>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Numero</label>
									<div class="controls">
									<span class="al">
											<label class="radio inline">Provvisorio</label>
										</span>
										<s:textfield id="numeroPrimaNota" name="primaNota.numero" cssClass="span2 soloNumeri" />
										<span class="al">
											<label class="radio inline">Definitivo</label>
										</span>
										<s:textfield id="numeroRegistrazionePrimaNota" name="primaNota.numeroRegistrazioneLibroGiornale" cssClass="span2 soloNumeri" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codiceConto">Conto</label>
									<div class="controls">
										<s:textfield id="codiceConto" name="conto.codice" cssClass="span3" />
										<span id="descrizioneConto"></span>
										<span class="radio guidata">
											<button type="button" class="btn btn-primary " id="pulsanteCompilazioneGuidataConto">compilazione guidata</button>
										</span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">
										Movimento finanziario&nbsp;
										<a href="#" class="tooltip-test" data-original-title="selezionare il Tipo evento">
											<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare il Tipo evento</span></i>
										</a>
									</label>
									<div class="controls">
										<s:textfield id="annoMovimento" cssClass="span1 soloNumeri" name="annoMovimento"  maxlength="4" placeholder="anno" disabled="true" />
										<s:textfield id="numeroMovimento" cssClass="span2" name="numeroMovimento" placeholder="numero" disabled="true" />
										<s:textfield id="numeroSubmovimento" cssClass="span2" name="numeroSubmovimento" placeholder="sub" disabled="true" />
									</div>
								</div>
								<div class="control-group"> 
									<label class="control-label" for="statoOperativoPrimaNotaPrimaNota">Stato</label>
									<div class="controls">
										<s:select list="listaStatoOperativoPrimaNota" id="statoOperativoPrimaNotaPrimaNota" name="primaNota.statoOperativoPrimaNota"
											cssClass="span6" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="descrizionePrimaNota">Descrizione</label>
									<div class="controls">
										<s:textfield id="descrizionePrimaNota" name="primaNota.descrizione" cssClass="span9" />
									</div>
								</div>
								<div class="control-group hide" id="containerImportoDocumento">
									<label class="control-label">
										Importo
										<a href="#" class="tooltip-test" data-html="true" data-original-title="selezionare il Tipo evento<br/>La ricerca sar&agrave; effettuata sull'importo del documento, non della prima nota">
											<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare il Tipo evento<br/>La ricerca sar&agrave; effettuata sull'importo del documento, non della prima nota</span></i>
										</a>
									</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline" for="importoDocumentoDa">Da</label>
										</span>
										<s:textfield id="importoDocumentoDa" cssClass="span2 soloNumeri decimale" name="importoDocumentoDa"  placeholder="importo da" disabled="true" />
										<span class="al">
											<label class="radio inline" for="importoDocumentoA">A</label>
										</span>
										<s:textfield id="importoDocumentoA" cssClass="span2 soloNumeri decimale" name="importoDocumentoA" placeholder="importo a" disabled="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Data registrazione definitiva</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline" for="dataRegistrazioneDA">Da</label>
										</span>
										<s:textfield id="dataRegistrazioneDA" name="dataRegistrazioneDefinitivaDa" cssClass="span2 datepicker" maxlength="10" />
										<span class="al">
											<label class="radio inline" for="dataRegistrazioneA">A</label>
										</span>
										<s:textfield id="dataRegistrazioneA" name="dataRegistrazioneDefinitivaA" cssClass="span2 datepicker" maxlength="10" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Data registrazione provvisoria</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline" for="dataRegistrazioneProvvisoriaDA">Da</label>
										</span>
										<s:textfield id="dataRegistrazioneProvvisoriaDA" name="dataRegistrazioneProvvisoriaDa" cssClass="span2 datepicker" maxlength="10" />
										<span class="al">
											<label class="radio inline" for="dataRegistrazioneProvvisoriaA">A</label>
										</span>
										<s:textfield id="dataRegistrazioneProvvisoriaA" name="dataRegistrazioneProvvisoriaA" cssClass="span2 datepicker" maxlength="10" />
									</div>
								</div>
								
								<h4 class="step-pane">Soggetto
									<span id="descrizioneCompletaSoggetto"><s:property value="descrizioneCompletaSoggetto" /></span>
								</h4>
								<div class="control-group">
									<label class="control-label" for="codiceSoggetto">Codice </label>
									<div class="controls">
										<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
										<span class="radio guidata">
											<a href="#" id="pulsanteApriModaleSoggetto" class="btn btn-primary">compilazione guidata</a>
										</span>
									</div>
								</div>
								
								<!-- 
								<h4 class="step-pane">&nbsp;Provvedimento&nbsp;
									<span id="SPAN_InformazioniProvvedimento"><s:property value="descrizioneCompletaAttoAmministrativo" /></span>
								</h4>
								<div class="control-group">
									<label class="control-label" for="annoProvvedimento">Anno</label>
									<div class="controls">
										<s:textfield id="annoProvvedimento" cssClass="lbTextSmall span1 soloNumeri" name="attoAmministrativo.anno" maxlength="4" />
										<span class="al">
											<label class="radio inline" for="numeroProvvedimento">Numero</label>
										</span>
										<s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" maxlength="7" />
										<span class="al">
											<label class="radio inline" for="tipoAttoProvvedimento">Tipo</label>
										</span>
										<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="attoAmministrativo.tipoAtto.uid" id="tipoAttoProvvedimento" cssClass="span4"
											headerKey="" headerValue="" />
										<s:hidden id="HIDDEN_statoProvvedimento" name="attoAmministrativo.statoOperativo" />
										<span class="radio guidata">
											<a href="#" id="pulsanteApriModaleProvvedimento" class="btn btn-primary">compilazione guidata</a>
										</span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Struttura Amministrativa</label>
									<div class="controls">
										<div class="accordion span8 struttAmm">
											<div class="accordion-group">
												<div class="accordion-heading">
													<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa" href="#struttAmm">
														<span id="SPAN_StrutturaAmministrativoContabile"> Seleziona la Struttura amministrativa </span>
														<i class="icon-spin icon-refresh spinner"></i>
													</a>
												</div>
												<div id="struttAmm" class="accordion-body collapse">
													<div class="accordion-inner">
														<ul id="treeStruttAmm" class="ztree treeStruttAmm"></ul>
														<button type="button" id="pulsanteDelesezionaStrutturaAmministrativoContabile"  class="btn">Deseleziona</button>
													</div>
												</div>
											</div>
										</div>

										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="attoAmministrativo.strutturaAmmContabile.uid" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="attoAmministrativo.strutturaAmmContabile.codice" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="attoAmministrativo.strutturaAmmContabile.descrizione" />
									</div>
								</div>
								 -->
								 
								<div id="capitoloContainer" class="hide">
									<h4 class="step-pane">Capitolo &nbsp;
										<a href="#" class="tooltip-test" data-original-title="selezionare il Tipo evento">
											<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare il Tipo evento</span></i>
										</a>
										<span class="datiRIFCapitolo" id="datiRiferimentoCapitoloSpan"></span>
									</h4>
									<div class="control-group">
										<label class="control-label" for="annoCapitolo">Anno</label>
										<div class="controls">
											<s:textfield id="annoCapitolo" name="capitolo.annoCapitolo"
												cssClass="lbTextSmall span1" value="%{annoEsercizioInt}" disabled="true" data-maintain="" />
											<s:hidden name="capitolo.annoCapitolo" value="%{annoEsercizioInt}" data-maintain="" />
											<span class="al">
												<label class="radio inline" for="numeroCapitolo">Capitolo</label>
											</span>
											<s:textfield id="numeroCapitolo" name="capitolo.numeroCapitolo" cssClass="lbTextSmall span2 soloNumeri" maxlength="30" />
											<span class="al">
												<label class="radio inline" for="numeroArticolo">Articolo</label>
											</span>
											<s:textfield id="numeroArticolo" name="capitolo.numeroArticolo" cssClass="lbTextSmall span2 soloNumeri" maxlength="7" />
											<s:if test="gestioneUEB">
												<span class="al">
													<label class="radio inline" for="numeroUEB">UEB</label>
												</span>
												<s:textfield id="numeroUEB" name="capitolo.numeroUEB" cssClass="lbTextSmall span2 soloNumeri" maxlength="7"/>
											</s:if><s:else>
												<input type="hidden" name="capitolo.numeroUEB" value="1" data-maintain="true" />
											</s:else>
											<span class="radio guidata" id="compilazioneGuidata">
												<a class="btn btn-primary" data-toggle="modal" id="pulsanteApriCompilazioneGuidataCapitolo">
													compilazione guidata
												</a>
											</span>
										</div>
									</div>
								</div>
								<!-- DIV IMPEGNO -->
								<div id = "containerImpegno" class="hide">
									<h4 class="step-pane">Impegno<span id="SPAN_impegnoH4"></span></h4>
									<fieldset class="form-horizontal">
										<div class="control-group">
											<label class="control-label" for="annoMovimentoMovimentoGestione">Movimento</label>
											<div class="controls">
												<s:textfield id="annoImpegno" name="impegno.annoMovimento" placeholder="anno" cssClass="span1 soloNumeri" maxlength="4"/>
												<s:textfield id="numeroImpegno" name="impegno.numero" placeholder="numero" cssClass="span2 soloNumeri"/>
												<s:textfield id="numeroSubimpegno" name="subImpegno.numero" placeholder="sub" cssClass="span2 soloNumeri" maxlength="7"/>
												<span id="SPAN_pulsanteAperturaCompilazioneGuidataImpegno"class="radio guidata">
													<a class="btn btn-primary" data-toggle="modal" id="pulsanteAperturaCompilazioneGuidataImpegno">compilazione guidata</a>
												</span>
											</div>
										</div>
									</fieldset>
								</div>
								<!-- FINE DIV IMPEGNO -->
								
								<!-- DIV ACCERTAMENTO -->
								<div id="containerAccertamento" class="hide">
									<h4 class="step-pane">Accertamento<span id="SPAN_accertamentoH4"></span></h4>
									<fieldset class="form-horizontal">
										<div class="control-group">
											<label class="control-label" for="annoMovimentoMovimentoGestione">Movimento</label>
											<div class="controls">
												<s:textfield id="annoAccertamento" name="accertamento.annoMovimento" placeholder="anno" cssClass="span1 soloNumeri" maxlength="4" readonly="%{impegnoQuotaDisabilitato}"/>
												<s:textfield id="numeroAccertamento" name="accertamento.numero" placeholder="numero" cssClass="span2 soloNumeri" value="%{movimentoGestione.numero.toString()}" readonly="%{impegnoQuotaDisabilitato}"/>
												<s:textfield id="numeroSubAccertamento" name="subAccertamento.numero" placeholder="subccertamento" cssClass="span2 soloNumeri" maxlength="7" value="%{subMovimentoGestione.numero.toString()}" readonly="%{impegnoQuotaDisabilitato}"/>
												<span id="SPAN_pulsanteAperturaCompilazioneGuidataAccertamento"class="radio guidata">
													<a class="btn btn-primary" data-toggle="modal" id="pulsanteAperturaCompilazioneGuidataAccertamento">compilazione guidata</a>
												</span>
											</div>
										</div>
									</fieldset>
								</div>
								<!-- FINE DIV ACCERTAMENTO -->
								<h4 class="step-pane">Classificatore GSA</h4>
								<div class="control-group">
									<label class="control-label">Classificatori</label>
									<div class="controls">
										<div id="classGSAParent" class="accordion span9 classGSA">
											<div class="accordion-group">
												<div class="accordion-heading">
													<a href="#classGSA" data-toggle="collapse" data-parent="#classGSAParent" class="accordion-toggle collapsed">
														<span id="SPAN_classificatoreGSA">Seleziona il classificatore</span>
													</a>
												</div>
												<div id="classGSA" class="accordion-body collapse">
													<div class="accordion-inner">
														<ul id="classGSATree" class="ztree"></ul>
														<button type="button" class="btn pull-right" data-deseleziona-ztree="classGSATree">Deseleziona</button>
													</div>
												</div>
											</div>
										</div>
									</div>
									<s:hidden id="HIDDEN_classificatoreGSAUid" name="primaNota.classificatoreGSA.uid" />
								</div>
							</fieldset>
						</div>
					</div>
					
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset value="annulla" cssClass="btn btn-secondary" />
						<s:submit value="cerca" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
				<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
				<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
				<s:include value="/jsp/capUscitaGestione/selezionaCapitolo_modale.jsp">
					<s:param name="suffix">_cug</s:param>
				</s:include>
				<s:include value="/jsp/capEntrataGestione/selezionaCapitolo_modale.jsp">
					<s:param name="suffix">_ceg</s:param>
				</s:include>
				<%-- <s:include value="/jsp/movimentoGestione/modaleAccertamento.jsp" />
				<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" /> --%>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}documento/ztree.js"></script>
	<script type="text/javascript" src="${jspath}ztree/ztree_new.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale_doc.js"></script>
	<script type="text/javascript" src="${jspath}capitolo/ricercaCapitoloModale.js"></script>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/classifgsa/ztree.classifgsa.js"></script>
	<%-- <script type="text/javascript" src="${jspath}movimentoGestione/ricercaAccertamentoOttimizzato.js"></script>
	<script type="text/javascript" src="${jspath}movimentoGestione/ricercaImpegnoOttimizzato.js"></script> --%>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/primaNotaIntegrata/ricerca.js"></script>
	
</body>
</html>