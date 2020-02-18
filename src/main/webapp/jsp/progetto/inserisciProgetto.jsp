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
			<div class="span12 ">

				<div class="contentPage">
					<s:form id="formInserisciProgetto" cssClass="form-horizontal" novalidate="novalidate">

						<h3>Anagrafica Progetto</h3>
						<!-- message errore -->
						<s:include value="/jsp/include/messaggi.jsp" />
						<div id="MyWizard" class="wizard">
							<ul class="steps">
								<li data-target="#step1" class="active"><span class="badge">1</span>Progetto<span class="chevron"></span></li>
								<li data-target="#step2"><span class="badge">2</span>Cronoprogramma<span class="chevron"></span></li>
							</ul>
						</div>

						<div class="step-content">
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<br>
									<div class="control-group">
										<label class="control-label" for="codiceProgetto">Codice *</label>
										<div class="controls">
											<s:if test="codiceProgettoAutomatico">
												<input type="text" disabled class="lbTextSmall span3" maxlength="30" value="Valore impostato dal sistema" /> 
											</s:if><s:else>
												<s:textfield id="codiceProgetto" cssClass="lbTextSmall span3" name="progetto.codice" maxlength="20" placeholder="codice" required="required" />
											</s:else>
											<span class="al">
												<label class="radio inline">&nbsp;</label>
											</span>
											<span>
												<input type="text" disabled class="lbTextSmall span3" maxlength="30" value="<s:property value="progetto.tipoProgetto.descrizione"/>" />
											</span>
											<s:hidden name="progetto.tipoProgetto"/>
										</div>
									</div>
									
									<div class="control-group">
										<span class="al"> <label for="descrizioneProgetto" class="control-label">Descrizione *</label>
										</span>
										<div class="controls">
											<s:textarea id="descrizioneProgetto" rows="1" cols="15" cssClass="span9" name="progetto.descrizione" maxlength="500" required="required" readonly="%{!abilitaModificaDescrizione}"></s:textarea>
										</div>
									</div>

									<div class="control-group">
										<label for="amb" class="control-label">Ambito</label>
										<div class="controls">
											<s:select list="listaTipiAmbito" cssClass="span8" name="progetto.tipoAmbito.uid" id="amb" headerKey="" headerValue="" listKey="uid"
											listValue="%{codice + ' - ' + descrizione}" />
										</div>
									</div>

									<div class="control-group">
										<label class="control-label" for="rilFPV">Rilevante a FPV </label>
										<div class="controls">
											<s:checkbox id="rilFPV" name="progetto.rilevanteFPV" />
										</div>
									</div>

									<div class="control-group">
										<label class="control-label" for="valoreComplessivoProgetto">Valore complessivo </label>
										<div class="controls">
											<s:textfield id="valoreComplessivoProgetto" cssClass="lbTextSmall span3 soloNumeri decimale" name="progetto.valoreComplessivo" maxlength="20" placeholder="valoreComplessivo" />
										</div>
									</div>
									<!-- ahmad begin-->
									<div class="control-group">
										<label for="dataIndizioneGara" class="control-label">Data validazione progetto a base di gara</label>
										<div class="controls">
											<s:textfield id="dataIndizioneGara" name="progetto.dataIndizioneGara" cssClass="span2 datepicker" />
										</div>
									</div>
									<div class="control-group">
										<label for="dataAggiudicazioneGara" class="control-label">Anno Programmazione</label>
										<div class="controls">
											<s:textfield id="dataAggiudicazioneGara" name="progetto.dataAggiudicazioneGara" cssClass="span2 datepicker" />
										</div>
									</div>
									
									<div class="control-group">
										<span class="control-label">Investimento in corso di definizione </span>
										<div class="controls">
											<s:checkbox id="investimentoInCorsoDiDefinizione" name="progetto.investimentoInCorsoDiDefinizione" disabled="true"/>
											<s:hidden name="progetto.investimentoInCorsoDiDefinizione" />
										</div>
									</div>
		
										
									<div class="control-group">
										<span class="al">
											<label for="noteProgetto" class="control-label">Note </label>
										</span>
										<div class="controls">
											<s:textarea id="noteProgetto" rows="2" cols="15" cssClass="span8" name="progetto.note" maxlength="500"></s:textarea>
										</div>
									</div>
									
									<div class="control-group" data-cig-cup>
										<label class="control-label" for="cupProgetto">
											<abbr title="codice unico progetto">CUP</abbr>
										</label>
										<div class="controls">
											<s:textfield id="cupProgetto" name="progetto.cup" cssClass="span3" data-force-uppercase="" data-allowed-chars="[A-Za-z0-9]" maxlength="15"/>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">Servizio</label>
										<div class="controls">
											<div class="accordion span8 struttAmm">
												<div class="accordion-group">
													<div class="accordion-heading">
														<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativaProgetto" href="#strutturaAmministrativoContabileProgetto">
															<span id="SPAN_StrutturaAmministrativoContabileProgetto">Seleziona la Struttura amministrativa</span>
														</a>
													</div>
													<div id="strutturaAmministrativoContabileProgetto" class="accordion-body collapse">
														<div class="accordion-inner">
															<ul id="treeStrutturaAmministrativoContabileProgetto" class="ztree"></ul>
														</div>
													</div>
												</div>
											</div>
						
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUidProgetto" name="progetto.strutturaAmministrativoContabile.uid" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodiceProgetto" name="progetto.strutturaAmministrativoContabile.codice" />
											<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizioneProgetto" name="progetto.strutturaAmministrativoContabile.descrizione" />
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">Spazi finanziari</label>
										<div class="controls">
											<label class="radio inline">
												<input type="radio" value="true" name="progetto.spaziFinanziari">S&igrave;
											</label>
											<span class="alLeft">
												<label class="radio inline">
													<input type="radio" value="false" name="progetto.spaziFinanziari"checked>No
												</label>
											</span>											
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="rupProgetto"><abbr title="responsabile unico progetto">RUP</abbr></label>
										<div class="controls">
											<s:textfield id="rupProgetto" cssClass="lbTextSmall span3" name="progetto.responsabileUnico" />
										</div>
									</div>
									
									<div class="control-group">
										<label for="modAffidamento" class="control-label">Modalit&agrave; di affidamento</label>
										<div class="controls">
											<s:select list="listaModalitaAffidamento" cssClass="span8" name="progetto.modalitaAffidamentoProgetto.uid" id="modAffidamento" headerKey="" headerValue="" listKey="uid"
											listValue="%{descrizione}" />
										</div>
									</div>
									

									<s:include value="/jsp/provvedimento/ricercaProvvedimentoCollapse.jsp" />
									<!-- SIAC-6051 -->
									<s:hidden id="HIDDEN_infoProvv" name="informazioniProvvedimento"/>
									<s:hidden id="HIDDEN_abilitaModificaDescrizione" name="abilitaModificaDescrizione"/>
								</fieldset>
							</div>
						</div>
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<button id="pulsanteAnnulla" name="pulsanteAnnulla" type="button" class="btn" >annulla</button>							
							<%--<s:reset cssClass="btn btn-link" value="annulla" /> --%>
							<s:submit value="Prosegui" cssClass="btn btn-primary pull-right" action="inserimentoProgettoProsegui"/>
							<s:submit value="Salva" cssClass="btn btn-primary pull-right" action="inserimentoProgettoSalva"/>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}ztree/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricercaProvvedimento_collapse.js"></script>
	<script type="text/javascript" src="${jspath}progetto/progetto.js"></script>

</body>
</html>