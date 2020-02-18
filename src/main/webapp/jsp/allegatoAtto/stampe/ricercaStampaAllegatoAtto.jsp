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
			<div class="span12 contentPage">
			<s:form action="ricercaStampaAllegatoAtto_ricerca" novalidate="novalidate" method="post" cssClass="form-horizontal">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Ricerca stampe allegato atto</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">							
							<p>
								<s:submit id="pulsanteStampa" cssClass="btn btn-primary pull-right" value="cerca" />
							</p>
							<h4>Dati principali</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label for="tipoStampaAllegatoAtto" class="control-label">Tipo stampa *</label>
									<div class="controls">
										<s:select list="listaTipoStampa" cssClass="span6" required="true" name="allegatoAttoStampa.tipoStampa" id="tipoStampaAllegatoAtto"
											headerKey="" headerValue="Selezionare il tipo di stampa" listValue="descrizione" />
									</div>
								</div>
								<div class="control-group" data-riepilogo="">
									<label for="dataCreazioneAllegatoAttoStampa" class="control-label">Data stampa</label>
									<div class="controls">
											<s:textfield id="dataCreazioneAllegatoAttoStampa" name="allegatoAttoStampa.dataCreazione" cssClass="span9 datepicker" />
									</div>
								</div>
							</fieldset>

							<div id="campiStampaAllegatoAtto" class="hide">
								<h4 class="step-pane"> Provvedimento <%-- <span id="datiRiferimentoAttoAmministrativoSpan">
									<s:if test='%{attoAmministrativo != null && (attoAmministrativo.anno ne null && attoAmministrativo.anno != "") && (attoAmministrativo.numero ne null && attoAmministrativo.numero != "") && (tipoAtto.uid ne null && tipoAtto.uid != "")}'>
										<s:property value="%{tipoAtto.descrizione+ '/'+ attoAmministrativo.anno + ' / ' + attoAmministrativo.numero + ' - ' + attoAmministrativo.oggetto}" />
									</s:if>
								</span> --%>
								</h4>
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label class="control-label" for="annoAttoAmministrativo">Anno </label>
										<div class="controls">
											<s:textfield id="annoAttoAmministrativo" cssClass="lbTextSmall span1 soloNumeri" name="attoAmministrativo.anno" />
											<span class="al">
												<label class="radio inline" for="numeroAttoAmministrativo">Numero  </label>
											</span>
											<s:textfield id="numeroAttoAmministrativo" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" maxlength="7" />
											<span class="al">
												<label class="radio inline" for="tipoAtto">Tipo </label>
											</span>
											<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid"
												id="tipoAtto" cssClass="span4" headerKey="0" headerValue="" />
											<!--<s:hidden id="statoOperativoAttoAmministrativo" name="attoAmministrativo.statoOperativo" />	 -->									
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
							</div>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset cssClass="btn btn-secondary" value="annulla" />
						<s:submit id="pulsanteStampa" cssClass="btn btn-primary pull-right" value="cerca" />
					</p>
					<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}predocumento/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale.js"></script>
	<script type="text/javascript" src="${jspath}allegatoAtto/stampe/ricerca.js"></script>

</body>
</html>