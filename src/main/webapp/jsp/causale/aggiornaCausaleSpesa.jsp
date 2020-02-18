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
	
	<s:hidden id="nomeAzioneDecentrata" value="%{nomeAzioneDecentrata}" data-maintain="" />
	<s:hidden id="HIDDEN_anno_datepicker" value="%{annoEsercizioInt}" data-maintain="" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="aggiornamentoCausaleSpesa" id="formAggiornamentoCausaleSpesa" novalidate="novalidate" >
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3><s:property value="titoloPredisposizione" escapeHtml="false" /></h3>
					<s:hidden id="HIDDEN_uidCausale" name="causale.uid" data-maintain="" />
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<h3>Causale: <s:property value="causale.codice"/> - <s:property value="causale.descrizione"/></h3>
       						<h4><s:property value="causaleStatoDataCalcolato"/></h4>
							<div class="step-content">
						  		<div class="step-pane active" id="step1">
									<h4>Dati causale</h4>
									<fieldset class="form-horizontal margin-large">											
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
											<label for="tipoCausale" class="control-label">Tipo causale *</label>
											<div class="controls">
												<s:select list="listaTipoCausale" name="tipoCausale.uid" cssClass="span9" required="true" headerKey="0" headerValue=""
													listKey="uid" listValue="%{codice + '-' + descrizione}" id="tipoCausale" />
											</div>
										</div>
										
										<div class="control-group">
											<label class="control-label" for="codiceCausale">Causale *</label>
											<div class="controls">
												<s:textfield id="codiceCausale" cssClass="span2" name="causale.codice" placeholder="codice" required="true"  readonly="true" disabled="disabled" />
												<s:textfield id="descrizioneCausale" cssClass="span7" name="causale.descrizione" placeholder="descrizione" required="true" readonly="true" disabled="disabled"/>
											</div>
										</div>

										<div class="control-group">
											<label for="statoOperativoCausale" class="control-label">Stato causale </label>
											<div class="controls">
												<s:textfield id="statoOperativoCausale" cssClass="span2" name="causale.statoOperativoCausale" placeholder="statoOperativoCausale" readonly="true" disabled="disabled" />
											</div>
										</div>
										
										<div class="control-group">
											<label for="dataScadenza" class="control-label">Data Fine Validit&agrave;</label>
											<div class="controls">
												<s:textfield id="dataScadenza" cssClass="span2 datepicker" name="causale.dataScadenza" placeholder="fine validità" />
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
													<s:include value="/jsp/predocumento/imputazioniContabiliPreDocumentoSpesa.jsp" />
												</div>
											</div>
										</div>
								</fieldset>
							</div>
						</div>
					</div>
				</div>
				<p class="margin-medium">
					<s:include value="/jsp/include/indietro.jsp" />
					<button type="button" class="btn reset">annulla</button>
					<s:submit cssClass="btn btn-primary pull-right" value="aggiorna"/>
				</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/capUscitaGestione/selezionaCapitolo_modale.jsp" />
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" />
	<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
	<%-- MODALE USCITA GESTIONE --%>	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}capitolo/ricercaCapitoloModale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
	<script type="text/javascript" src="${jspath}ztree/ztree_new.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale_new.js"></script>
	<script type="text/javascript" src="${jspath}predocumento/predocumento.js"></script>
	<script type="text/javascript" src="${jspath}causale/aggiornaCausaleSpesa.js"></script>
</body>
</html>