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
					<s:form id="formInserisciDismissioneCespite" cssClass="form-horizontal" novalidate="novalidate" action="%{baseUrl}_proseguiStep1Anagrafica">
						<s:hidden name="ambito" id="ambito" />
						<s:hidden name="ambitoCausaleInventario" id="ambitoCausaleInventario" />
						<h3 id="titolo"><s:property value="formTitle"/></h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						
						<h3>Gestione dismissione <s:property value="chiaveDismissioneCespite"/></h3>
						
						<div class="wizard" id="MyWizard">
							<ul class="steps">
								<li class="active" data-target="#step1">
									<span class="badge">1</span>anagrafica<span class="chevron"></span>
								</li>
								<li data-target="#step2">
									<span class="badge">2</span>collega cespiti<span class="chevron"></span>
								</li>
							</ul>
						</div>
						
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<br/>
									<s:hidden id="HIDDEN_uidDismissione" name="dismissioneCespite.uid"/>
									<s:if test="%{uidDismissioneCespite != 0}">
										<div class="control-group">
											<label class="control-label">Elenco *</label>
											<div class="controls">
												<s:textfield id="annoElenco" cssClass="lbTextSmall span2" name="dismissioneCespite.annoElenco" maxlength="4" placeholder="anno" disabled="true"/>
												<s:hidden value="dismissioneCespite.annoElenco"/>
												<span class="al">
													&nbsp;
												</span>
												<s:textfield id="numeroElenco" cssClass="lbTextSmall span2" name="dismissioneCespite.numeroElenco" placeholder="numero" disabled="true" />
												<s:hidden value="dismissioneCespite.numeroElenco"/>
											</div>
										</div>
									</s:if>
									<div class="control-group">
										<label for="descrizione" class="control-label">Descrizione *</label>
										<div class="controls">
											<s:textfield id="descrizione" name="dismissioneCespite.descrizione" cssClass="span6" placeholder="descrizione" required="true"  maxlength="500"/>
										</div>
									</div>
									
									<h4 class="step-pane">
										Provvedimento * 
										<span id="datiRiferimentoAttoAmministrativoSpan">
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
									<div class="control-group">
										<label for="dataCessazione" class="control-label">Data cessazione *</label>
										<div class="controls">
											<s:textfield id="dataCessazione" name="dismissioneCespite.dataCessazione" cssClass="lbTextSmall span2 datepicker" size="10" required="required"/>
										</div>
									</div>
									<div class="control-group">
											<label class="control-label" for="uidEventoDismissione">Evento</label>
											<div class="controls">
												<s:select list="listaEvento" id="uidEventoDismissione" name="dismissioneCespite.evento.uid" cssClass="span6" headerKey="" headerValue=""
													listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-evento="" />
											</div>
										</div>
										<div class="control-group"> 
											<label class="control-label" for="uidCausaleEPDismissione">Causale dismissione* </label>
											<div class="controls">
												<s:select list="listaCausaleEP" id="uidCausaleEPDismissione" name="dismissioneCespite.causaleEP.uid"
													cssClass="span6" headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-causale-EP="" />
											</div>
										</div>
									
									<div class="control-group">
										<label for="descrizioneStatoCessazione" class="control-label">Descrizione stato cessazione</label>
										<div class="controls">
											<s:textfield id="descrizioneStatoCessazione" name="dismissioneCespite.descrizioneStatoCessazione" cssClass="span6" placeholder="descrizione" required="true"  maxlength="500"/>
										</div>
									</div>
									
								</fieldset>
							</div>
						</div>
						<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<s:if test="%{uidDismissioneCespite != 0}">
								<button type="button" data-href="<s:property value="baseUrl"/>" class="btn btn-secondary reset">annulla</button>
							</s:if><s:else>
								<button type="button" class="btn btn-secondary reset">annulla</button>
							</s:else>
							<s:submit cssClass="btn btn-primary pull-right" value="prosegui" />
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/predocumento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/dismissionecespite/step1Dismissione.js"></script>
</body>
</html>