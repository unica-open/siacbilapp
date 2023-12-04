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
			<div class="span12 ">
				<div class="span12 contentPage">
					<s:form action="ricercaRegistrazioneMovFinGSA_effettuaRicercaRegistrazioneMovFin" cssClass="form-horizontal" novalidate="novalidate">
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden nam="HIDDEN_ambito" name="ambito" />
						<s:hidden name="baseUrl" id="baseUrl" />
						<s:hidden nam="HIDDEN_uidTipoEventoDaRicerca" name="uidTipoEventoDaRicerca" id="uidTipoEventoDaRicerca"/>
						<s:hidden nam="HIDDEN_uidEventoDaRicerca" name="uidEventoDaRicerca" id="uidEventoDaRicerca" />
						
						<h3>Ricerca Registro</h3>
						<p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>
						<div class="step-content">
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<p><s:submit cssClass="btn btn-primary pull-right" value="cerca" /></p>
									<h4 class="step-pane">Dati</h4>
									
									<div class="control-group">
										<label class="control-label">Tipo Elenco *</label>
										<div class="controls">
											<span class="al">
												<label class="radio inline">
													<input type="radio" value="E" name="tipoElenco" <s:if test='%{"E".equals(tipoElenco)}'>checked</s:if>>Entrate
												</label>
											</span>
											<span class="al">
												<label class="radio inline">
													<input type="radio" value="S" name="tipoElenco" <s:if test='%{"S".equals(tipoElenco)}'>checked</s:if>>Spese
												</label>
											</span>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="tipoEvento">Tipo evento *</label>
										<div class="controls">
											<s:select list="listaTipiEvento" name="tipoEvento.uid" id="tipoEvento" headerKey="" headerValue="" cssClass="span6" required="true"
												listValue="%{codice + ' - ' + descrizione}" listKey="uid" disabled='%{!"E".equals(tipoElenco) && !"S".equals(tipoElenco)}' />
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="evento">Evento</label>
										<div class="controls">
											<s:select list="listaEventi" name="evento.uid" id="evento" headerKey="" headerValue="" cssClass="span6" required="true"
												listValue="%{codice + ' - ' + descrizione}" listKey="uid" disabled="%{evento == null || evento.tipoEvento == null || evento.tipoEvento.uid == 0}" />
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">Movimento finanziario</label>
										<div class="controls">
											<s:textfield id="anno" cssClass="span1 soloNumeri" name="annoMovimento"  maxlength="4" placeholder="anno" />
											<s:textfield id="numero" cssClass="span2 " name="numeroMovimento"  placeholder="numero" />
											<s:textfield id="sub" cssClass="span2 soloNumeri" name="numeroSubmovimento" placeholder="sub" />
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">Data registrazione</label>
										<div class="controls">
											<span class="al">
												<label class="radio inline">Da</label>
											</span>
											<span><s:textfield id="dataRegistrazioneDa" name="dataRegistrazioneDa" cssClass="span2 datepicker" /></span>
											<span class="al">
												<label class="radio inline">A</label>
											</span>
											<span><s:textfield id="dataRegistrazioneA" name="dataRegistrazioneA" cssClass="span2 datepicker" /></span>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="statoOperativoRegistrazione">Stato</label>
										<div class="controls">
											<s:select id="statoOperativoRegistrazione" list="listaStati" name="registrazioneMovFin.statoOperativoRegistrazioneMovFin"
													required="false" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
										</div>
									</div>
									
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
								
									<!-- DIV SOGGETTO -->
									<div id = "soggettoContainer">
										<h4 class="step-pane">Soggetto
											<a href="#" class="tooltip-test" data-original-title="selezionare il Tipo evento">
												<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare il Tipo evento</span></i>
											</a>
											<span id="descrizioneCompletaSoggetto"><s:property value="descrizioneCompletaSoggetto" /></span>
										</h4>
										<div class="control-group">
											<label class="control-label" for="codiceSoggetto">Codice </label>
											<div class="controls">
												<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" disabled="true"/>
												<span class="radio guidata">
													<a href="#" id="pulsanteApriModaleSoggetto" class="btn btn-primary hide">compilazione guidata</a>
												</span>
											</div>
										</div>
									</div>
									<!-- FINE DIV SOGGETTO -->
									<!-- CAPITOLO -->
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
									<!-- FINE DIV CAPITOLO -->
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
								</fieldset>
							</div>
						</div>
						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
							<s:reset cssClass="btn btn-link" value="annulla" />
							<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
						</p>
					</s:form>
					<s:include value="/jsp/capUscitaGestione/selezionaCapitolo_modale.jsp">
						<s:param name="suffix">_cug</s:param>
					</s:include>
					<s:include value="/jsp/capEntrataGestione/selezionaCapitolo_modale.jsp">
						<s:param name="suffix">_ceg</s:param>
					</s:include>
					<s:include value="/jsp/movimentoGestione/modaleAccertamento.jsp" />
					<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
					<%-- SIAC-5983 --%>
					<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
					
					<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" />
				</div>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/ricercaCapitoloModale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaAccertamentoOttimizzato.js"></script>

	<script type="text/javascript" src="/siacbilapp/js/local/documento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale_doc.js"></script>
	
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/gestioneSanitariaAccentrata/registrazione/ricercaRegistrazioneMovFin.js"></script>

</body>
</html>