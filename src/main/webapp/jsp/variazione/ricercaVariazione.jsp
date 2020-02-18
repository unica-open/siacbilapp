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
				<div class="contentPage">
					<s:form action="effettuaRicercaVariazione" novalidate="novalidate" method="POST">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3>Ricerca Variazioni</h3>
						<div class="step-content">
							<p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>
							<div class="fieldset-body">
								<fieldset class="form-horizontal">
									<div class="control-group">
										<span class="control-label">Seleziona il tipo di variazione:</span>
										<div class="controls">
											<label class="radio">
												<input type="radio" name="tipologiaSceltaVariazione" value="importi" <s:if test="%{tipologiaSceltaVariazione eq 'importi'}">checked="checked"</s:if> /> 
												Variazioni di importi
											</label>
											<label class="radio"> 
												<input type="radio"	name="tipologiaSceltaVariazione" value="codifiche" <s:if test="%{tipologiaSceltaVariazione eq 'codifiche'}">checked="checked"</s:if> />
												Variazioni di codifiche
											</label>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="numeroVariazione">Numero variazione</label>
										<div class="controls">
											<s:textfield id="numeroVariazione" cssClass="mustBeNumber lbTextSmall span3 soloNumeri" name="numeroVariazione" placeholder="Numero Variazione" 
												disabled="true" />
										</div>
									</div>
									<div class="control-group">
										<label for="applicazioneVariazione" class="control-label">Applicazione</label>
										<div class="controls">
											<s:select list="listaApplicazioneVariazione" id="applicazioneVariazione" disabled="true" name="applicazioneVariazione" headerKey="" headerValue="" />									
										</div>
									</div>
									<div class="control-group">
										<label for="descrizioneVariazione" class="control-label">Descrizione</label>
										<div class="controls">
											<s:textfield id="descrizioneVariazione" cssClass="lbTextSmall span9" name="descrizioneVariazione" 
												placeholder="Descrizione Variazione" disabled="true"/>
										</div>
									</div>
									<!-- SIAC-6884 -->
									<div class="control-group">
										<label for="tipoVariazione" class="control-label">Tipo Variazione</label>
										<s:hidden name="tipoVariazioneHidden" id="tipoVariazioneHidden" />
										<div class="controls">
											<select id="tipoVariazione" name="tipoVariazione" disabled="disabled"></select>
										<span class="al alRight">
											<label class="radio inline" for="dataAperturaProposta" class="control-label">Data Apertura Proposta</label>
										</span>
										<s:textfield id="dataAperturaProposta" name="dataAperturaProposta" cssClass="span2 datepicker" disabled="true" data-maintain="" />
										</div>
									</div>
									<!-- SIAC-6884 -->
									<div class="control-group">
										<label for="statoVariazione" class="control-label">Stato Variazione</label>
										<div class="controls">
											<s:select list="listaStatoVariazione" id="statoVariazione" disabled="true" name="statoVariazione" headerKey="" headerValue="" listKey="statoOperativoVariazioneBilancio" listValue= "%{descrizione}" />
											<span class="al alRight">
												<label class="radio inline" for="dataChiusuraProposta" class="control-label">Data Chiusura Proposta</label>
											</span>
											<s:textfield id="dataChiusuraProposta" name="dataChiusuraProposta" cssClass="span2 datepicker" disabled="true" data-maintain="" />
										</div>
									</div>
									<!-- SIAC-6884 -->
									<div class="control-group">
										<label class="control-label" for="strutturaAmministrativoContabileDirezioneProponente">Direzione Proponente</label>
										<div class="controls">
											<s:if test="%{model.listaDirezioni.size > 1}">
												<s:select list="listaDirezioni" cssClass="lbTextSmall span9" id="strutturaAmministrativoContabileDirezioneProponente" name="strutturaAmministrativoContabileDirezioneProponente.uid" disabled="true" headerValue="" headerKey="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
											</s:if>
											<s:else>
												<span class="help-inline" id="strutturaAmministrativoContabileDirezioneProponente" name="strutturaAmministrativoContabileDirezioneProponente.uid" disabled="true">${strutturaAmministrativoContabileDirezioneProponente.codice}&nbsp;&nbsp;-&nbsp;&nbsp;${strutturaAmministrativoContabileDirezioneProponente.descrizione}</span>
											</s:else>
										</div>
									</div>
								</fieldset>
							</div>
							
							<div class="margin-large" id="div_provvedimento">
								<a class="btn pull-center" id="collapseProvv" data-toggle="collapse" data-parent="#div_provvedimento" href="#collapseProvvedimento">
									<i class="icon-pencil icon-2x"></i>&nbsp;<span id="SPAN_InformazioniProvvedimento"> <s:property value="stringaProvvedimento" /></span>
									
								</a>
							</div>
							<div id="collapseProvvedimento" class="collapse">
								<div class="collapse-inner">
									<p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto</p>
									<%-- Ricerca come dato aggiuntivo --%>
									<s:include value="/jsp/provvedimento/formRicercaProvvedimento.jsp" />
								</div>
							</div>
							
							<p class="margin-large">
								<s:include value="/jsp/include/indietro.jsp" />
								<s:reset value="annulla" cssClass="btn btn-link" />
								<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
							</p>
						</div>
					</s:form>
					
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}ztree/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricercaProvvedimento_collapse.js"></script>
	<script type="text/javascript" src="${jspath}variazioni/ricerca.js"></script> 
</body>
</html>