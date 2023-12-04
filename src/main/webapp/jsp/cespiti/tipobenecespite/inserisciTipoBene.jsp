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
					<s:form id="formInserisciTipoBene" cssClass="form-horizontal" novalidate="novalidate" action="inserisciTipoBene_salva">
						<s:hidden name="ambito" id="ambito" />
						<s:hidden name="ambitoCausaleInventario" id="ambitoCausaleInventario" />
						<h3 id="titolo">Anagrafica tipo bene </h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label for="codice" class="control-label">Codice *</label>
										<div class="controls">
											<s:textfield id="codiceCategoriaCespiti" name="tipoBeneCespite.codice" cssClass="span6" placeholder="codice" required="true"  maxlength="500"/>
										</div>
									</div>
									<div class="control-group">
										<label for="descrizione" class="control-label">Descrizione *</label>
										<div class="controls">
											<s:textfield id="descrizione" name="tipoBeneCespite.descrizione" cssClass="span6" placeholder="descrizione" required="true"  maxlength="500"/>
										</div>
									</div>
									<div class="control-group">
										<label for="categoriaCespitiTipoBene" class="control-label">Categoria cespite *</label>
										<div class="controls">
											<s:select list="listaCategoriaCespiti" id="categoriaTipoBene" name="tipoBeneCespite.categoriaCespiti.uid" cssClass="span6" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" />											
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="codiceConto">Conto patrimoniale</label>
										<div class="controls">
											<s:textfield id="codiceContoPatrimoniale" name="tipoBeneCespite.contoPatrimoniale.codice" cssClass="span6" maxlength="200" />
											<span id="descrizioneContoPatrimoniale"></span>
											<span class="radio guidata">
												<button type="button" data-selector-conto="<s:property value="contoPatrimonialeTipoBeneSelector"/>" data-selector-classe="<s:property value="contoPatrimonialeTipoBeneSelector.codiceClassePiano"/>" class="btn btn-primary" id="pulsanteCompilazioneGuidataContoPatrimoniale">compilazione guidata</button>
											</span>
										</div>
									</div>
									
									<div id = "ammortamentoGestione">
										<div class="control-group">
											<label class="control-label" for="codiceConto">Conto di ammortamento</label>
											<div class="controls">
												<s:textfield id="codiceContoAmmortamento" name="tipoBeneCespite.contoAmmortamento.codice" cssClass="span6" maxlength="200" />
												<span id="descrizioneContoAmmortamento"></span>
												<span class="radio guidata">
													<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataContoAmmortamento" data-selector-conto="<s:property value="contoAmmortamentoTipoBeneSelector"/>" data-selector-classe="<s:property value="contoAmmortamentoTipoBeneSelector.codiceClassePiano"/>">compilazione guidata</button>
												</span>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label" for="uidEventoAmmortamento">Evento di ammortamento</label>
											<div class="controls">
												<s:select list="listaEvento" id="uidEventoAmmortamento" name="tipoBeneCespite.eventoAmmortamento.uid"	cssClass="span6" headerKey="" headerValue=""
													listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-evento="" />
											</div>
										</div>
										<div class="control-group"> 
											<label class="control-label" for="uidCausaleEPAmmortamento">Causale di ammortamento</label>
											<div class="controls">
												<s:select list="listaCausaleEP" id="uidCausaleEPAmmortamento" name="tipoBeneCespite.causaleAmmortamento.uid"
													cssClass="span6" headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-causale-EP="" />
											</div>
										</div>
										
										<div class="control-group">
											<label for="testoScritturaAmmortamento" class="control-label">Testo scrittura ammortamento</label>
											<div class="controls">
											
												<s:textarea id="testoScritturaAmmortamento" name="tipoBeneCespite.testoScritturaAmmortamento" class="span6" rows="5" cols="5"></s:textarea>
											</div>
										</div>
										
										<div class="control-group">
											<label class="control-label" for="codiceConto">Conto del fondo di ammortamento</label>
											<div class="controls">
												<s:textfield id="codiceContoFondoAmmortamento" name="tipoBeneCespite.contoFondoAmmortamento.codice" cssClass="span6" maxlength="200" />
												<span id="descrizioneContoFondoAmmortamento"></span>
												<span class="radio guidata">
													<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataContoFondoAmmortamento" data-selector-conto="<s:property value="contoFondoAmmortamentoTipoBeneSelector"/>" data-selector-classe="<s:property value="contoFondoAmmortamentoTipoBeneSelector.codiceClassePiano"/>">compilazione guidata</button>
												</span>
											</div>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="codiceConto">Conto di plusvalenza da alienazione</label>
										<div class="controls">
											<s:textfield id="codiceContoPlusvalenza" name="tipoBeneCespite.contoPlusvalenza.codice" cssClass="span6" maxlength="200" />
											<span id="descrizioneContoPlusvalenza"></span>
											<span class="radio guidata">
												<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataContoPlusvalenza" data-selector-conto="<s:property value="contoPlusValenzaTipoBeneSelector"/>" data-selector-classe="<s:property value="contoPlusValenzaTipoBeneSelector.codiceClassePiano"/>">compilazione guidata</button>
											</span>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="codiceConto">Conto di minusvalenza da alienazione</label>
										<div class="controls">
											<s:textfield id="codiceContoMinusValenza" name="tipoBeneCespite.contoMinusValenza.codice" cssClass="span6" maxlength="200" />
											<span id="descrizioneMinusValenza"></span>
											<span class="radio guidata">
												<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataContoMinusValenza" data-selector-conto="<s:property value="contoMinusValenzaTipoBeneSelector"/>" data-selector-classe="<s:property value="contoMinusValenzaTipoBeneSelector.codiceClassePiano"/>">compilazione guidata</button>
											</span>
										</div>
									</div>
									<div id = "incrementoGestione">
										<div class="control-group">
											<label class="control-label" for="codiceConto">Conto di incremento volare</label>
											<div class="controls">
												<s:textfield id="codiceContoIncremento" name="tipoBeneCespite.contoIncremento.codice" cssClass="span6" maxlength="200" />
												<span id="descrizioneContoIncremento"></span>
												<span class="radio guidata">
													<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataContoIncremento" data-selector-conto="<s:property value="contoIncrementoTipoBeneSelector"/>" data-selector-classe="<s:property value="contoIncrementoTipoBeneSelector.codiceClassePiano"/>">compilazione guidata</button>
												</span>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label" for="uidEventoIncremento">Evento di incremento valore</label>
											<div class="controls">
												<s:select list="listaEvento" id="uidEventoIncremento" name="tipoBeneCespite.eventoIncremento.uid"	cssClass="span6" headerKey="" headerValue=""
													listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-evento="" />
											</div>
										</div>
										<div class="control-group"> 
											<label class="control-label" for="uidCausaleEPIncremento">Causale di incremento valore</label>
											<div class="controls">
												<s:select list="listaCausaleEP" id="uidCausaleEPIncremento" name="tipoBeneCespite.causaleIncremento.uid"
													cssClass="span6" headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-causale-EP="" />
											</div>
										</div>
									</div>
									<div id = "decrementoGestione">
										<div class="control-group">
											<label class="control-label" for="codiceConto">Conto di decremento valore</label>
											<div class="controls">
												<s:textfield id="codiceContoDecremento" name="tipoBeneCespite.contoDecremento.codice" cssClass="span6" maxlength="200" />
												<span id="descrizioneDecremento"></span>
												<span class="radio guidata">
													<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataContoDecremento" data-selector-conto="<s:property value="contoDecrementoTipoBeneSelector"/>" data-selector-classe="<s:property value="contoDecrementoTipoBeneSelector.codiceClassePiano"/>" >compilazione guidata</button>
												</span>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label" for="uidEventoDecremento">Evento di decremento valore</label>
											<div class="controls">
												<s:select list="listaEvento" id="uidEventoDecremento" name="tipoBeneCespite.eventoDecremento.uid"	cssClass="span6" headerKey="" headerValue=""
													listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-evento="" />
											</div>
										</div>
										<div class="control-group"> 
											<label class="control-label" for="uidCausaleEPDecremento">Causale di decremento valore</label>
											<div class="controls">
												<s:select list="listaCausaleEP" id="uidCausaleEPDecremento" name="tipoBeneCespite.causaleDecremento.uid"
													cssClass="span6" headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-causale-EP="" />
											</div>
										</div>
									</div>									
									<div class="control-group">
										<label class="control-label" for="codiceConto">Conto credito da alienazione</label>
										<div class="controls">
											<s:textfield id="codiceContoAlienazione" name="tipoBeneCespite.contoAlienazione.codice" cssClass="span6" maxlength="200" />
											<span id="descrizioneContoAlienazione"></span>
											<span class="radio guidata">
												<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataContoAlienazione" data-selector-conto="<s:property value="contoAlienazioneTipoBeneSelector"/>" data-selector-classe="<s:property value="contoAlienazioneTipoBeneSelector.codiceClassePiano"/>">compilazione guidata</button>
											</span>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="codiceConto">Conto donazione / rinvenimento</label>
										<div class="controls">
											<s:textfield id="codiceContoDonazione" name="tipoBeneCespite.contoDonazione.codice" cssClass="span6" maxlength="200" />
											<span id="descrizioneContoDonazione"></span>
											<span class="radio guidata">
												<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataContoDonazione" data-selector-conto="<s:property value="contoDonazioneTipoBeneSelector"/>" data-selector-classe="<s:property value="contoDonazioneTipoBeneSelector.codiceClassePiano"/>">compilazione guidata</button>
											</span>
										</div>
									</div>
									
								</fieldset>										
							</div>
						</div>
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn reset">annulla</button>
							<s:submit cssClass="btn btn-primary pull-right" value="salva"/>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/conto.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/tipobenecespite/inserisciTipoBene.js"></script>
</body>
</html>