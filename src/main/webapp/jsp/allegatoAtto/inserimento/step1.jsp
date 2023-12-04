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

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form id="formInserimentoAllegatoAttoStep1" cssClass="form-horizontal" novalidate="novalidate" action="inserisciAllegatoAtto_completeStep1" method="post">
					<s:hidden id="nomeAzioneDecentrata" value="%{nomeAzioneDecentrata}" data-maintain="" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Inserisci atto contabile / allegato</h3>
					<div class="wizard">
						<ul class="steps">
							<li class="active" data-target="#step1"><span class="badge">1</span>Atto contabile / allegato<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Elenchi collegati<span class="chevron"></span></li>
						</ul>
					</div>

					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<br/>
								<div class="control-group">
									<label class="control-label">Tipo Atto *</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline">
												<input type="radio" value="true" name="attoAutomatico" <s:if test='%{attoAutomatico == true}'>checked</s:if>>Atto automatico
											</label>
										</span>
										<span class="al">
											<label class="radio inline">
												<input type="radio" value="false" name="attoAutomatico" <s:if test='%{attoAutomatico == false}'>checked</s:if>>Atto da sistema esterno
											</label>
										</span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="causaleAllegatoAtto">Causale *</label>
									<div class="controls">
										<%-- SIAC-8465 si porta a 500 la lunghezza massima --%>
										<s:textfield id="causaleAllegatoAtto" name="allegatoAtto.causale" maxlength="500" cssClass="span9" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="dataScadenzaAllegatoAtto">Data di scadenza</label>
									<div class="controls">
										<s:textfield id="dataScadenzaAllegatoAtto" name="allegatoAtto.dataScadenza" cssClass="span2 datepicker" maxlength="10" />
										<span class="alRight">
											<label for="flagRitenuteAllegatoAtto" class="radio inline">Contiene ritenute</label>
										</span>
										<s:checkbox id="flagRitenuteAllegatoAtto" name="allegatoAtto.flagRitenute" />
									</div>
								</div>
								<h4 class="step-pane">
									Provvedimento <span id="datiRiferimentoAttoAmministrativoSpan">
										<s:if test='%{attoAmministrativo != null && (attoAmministrativo.anno ne null && attoAmministrativo.anno != "") && (attoAmministrativo.numero ne null && attoAmministrativo.numero != "") && (tipoAtto.uid ne null && tipoAtto.uid != "")}'>
											<s:property value="%{tipoAtto.descrizione+ '/'+ attoAmministrativo.anno + ' / ' + attoAmministrativo.numero + ' - ' + attoAmministrativo.oggetto}" />
										</s:if>
									</span>
								</h4>
								<div id="datiProvvedimento" class="control-group hide">
									<label class="control-label" for="annoAttoAmministrativo">Anno</label>
									<div class="controls" >
										<s:textfield id="annoAttoAmministrativo" cssClass="lbTextSmall span1 soloNumeri" name="attoAmministrativo.anno" />
										<span class="al">
											<label class="radio inline" for="numeroAttoAmministrativo">Numero</label>
										</span>
										<s:textfield id="numeroAttoAmministrativo" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" maxlength="7" />
										<span class="al">
											<label class="radio inline" for="tipoAtto">Tipo</label>
										</span>
										<s:select list="listaFiltrataTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid"
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

							<div id="accordionAltriDati" class="accordion">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a href="#divAltriDati" data-parent="#accordionAltriDati" data-toggle="collapse" class="accordion-toggle collapsed">
											Altri dati<span class="icon">&nbsp;</span>
										</a>
									</div>
									<div class="accordion-body collapse" id="divAltriDati">
										<div class="accordion-inner">
											<fieldset class="form-horizontal">
												<br>
												<div class="control-group">
													<label class="control-label" for="responsabileContabileAllegatoAtto">Responsabile contabile</label>
													<div class="controls">
														<s:textfield id="responsabileContabileAllegatoAtto" name="allegatoAtto.responsabileContabile"
															cssClass="span6" maxlength="500" />
													</div>
												</div>
												<div class="control-group">
													<label class="control-label" for="annotazioniAllegatoAtto">Annotazioni</label>
													<div class="controls">
														<s:textarea id="annotazioniAllegatoAtto" name="allegatoAtto.annotazioni" cssClass="span9"
															cols="15" rows="2" maxlength="500"></s:textarea>
													</div>
												</div>
												<div class="control-group">
													<label class="control-label" for="responsabileAmministrativoAllegatoAtto">Responsabile amministrativo</label>
													<div class="controls">
														<s:textfield id="responsabileAmministrativoAllegatoAtto" name="allegatoAtto.responsabileAmministrativo"
															cssClass="span6" maxlength="500" />
													</div>
												</div>
												<div class="control-group">
													<label class="control-label" for="praticaAllegatoAtto">Pratica numero</label>
													<div class="controls">
														<s:textfield id="praticaAllegatoAtto" name="allegatoAtto.pratica" cssClass="span6" maxlength="500" />
													</div>
												</div>
												<div class="control-group">
													<label class="control-label" for="altriAllegatiAllegatoAtto">Altri allegati</label>
													<div class="controls">
														<s:textfield id="altriAllegatiAllegatoAtto" name="allegatoAtto.altriAllegati"
															cssClass="span6" maxlength="500" />
													</div>
												</div>
												<div class="control-group">
													<label class="control-label" for="noteAllegatoAtto">Note in Allegato</label>
													<div class="controls">
														<s:textarea id="noteAllegatoAtto" name="allegatoAtto.note" cssClass="span9"
															rows="3" cols="15" maxlength="70"></s:textarea>
													</div>
												</div>
												<div class="control-group">
													<label class="control-label" for="datiSensibiliAllegatoAtto">Contiene dati sensibili</label>
													<div class="controls">
														<s:checkbox id="datiSensibiliAllegatoAtto" name="allegatoAtto.datiSensibili" />
													</div>
												</div>
											</fieldset>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn btn-secondary reset">annulla</button>
						<span class="pull-right">
							<s:submit cssClass="btn btn-primary" value="prosegui" />
						</span>
					</p>
					<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />

				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/predocumento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/allegatoAtto/inserisci_step1.js"></script>
	
</body>
</html>