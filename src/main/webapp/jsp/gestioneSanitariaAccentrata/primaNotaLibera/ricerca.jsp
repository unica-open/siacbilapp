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
				<s:form cssClass="form-horizontal" action="ricercaPrimaNotaLiberaGSA_effettuaRicerca" novalidate="novalidate" id="formPrimaNotaLibera">
					<s:hidden name="primaNotaLibera.uid" />
					<s:hidden name="ambito" id="ambito" />
					<s:hidden name="ambito"/>
					<s:include value="/jsp/include/messaggi.jsp" />
				
					<h3>Ricerca prima nota libera </h3>
					<p>&Eacute; necessario inserire almeno un criterio di ricerca.</p>
					<div class="step-content">
						<div class="step-pane active" id="step1">
		
							<fieldset class="form-horizontal">
								<p>
									<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
								</p>
								<h4 class="step-pane">Dati </h4>
								<div class="control-group">
									<label class="control-label">Numero</label>
									<div class="controls">
									<span class="al">
											<label class="radio inline">Provvisorio</label>
										</span>
										<s:textfield id="numeroPrimaNotaLibera" name="primaNotaLibera.numero" cssClass="span2 soloNumeri" />
										<span class="al">
											<label class="radio inline">Definitivo</label>
										</span>
										<s:textfield id="numeroRegistrazionePrimaNotaLibera" name="primaNotaLibera.numeroRegistrazioneLibroGiornale" cssClass="span2 soloNumeri" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="uidEvento">Evento</label>
									<div class="controls">
										<s:select list="listaEvento" id="uidEvento" name="evento.uid"
											cssClass="span6" headerKey="" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-evento="" />
									</div>
								</div>
								<div class="control-group"> 
									<label class="control-label" for="uidCausaleEP">Causale</label>
									<div class="controls">
										<s:select list="listaCausaleEP" id="uidCausaleEP" name="causaleEP.uid"
											cssClass="span6" headerKey="" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-causale-EP="" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codiceConto">Conto</label>
									<div class="controls">
										<s:textfield name="conto.codice" cssClass="span3" id="codiceConto" />
										<span id="descrizioneConto"></span>
										<span class="radio guidata">
											<button type="button" class="btn btn-primary " id="pulsanteCompilazioneGuidataConto">compilazione guidata</button>
										</span>
									</div>
								</div>
								<div class="control-group"> 
									<label class="control-label" for="statoOperativoPrimaNotaPrimaNotaLibera">Stato</label>
									<div class="controls">
										<s:select list="listaStatoOperativoPrimaNota" id="statoOperativoPrimaNotaPrimaNotaLibera" name="primaNotaLibera.statoOperativoPrimaNota"
											cssClass="span6" headerKey="" headerValue=""
											listValue="%{codice + ' - ' + descrizione}" data-causale-EP="" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="descrizionePrimaNotaLibera">Descrizione</label>
									<div class="controls">
										<s:textfield id="descrizionePrimaNotaLibera" name="primaNotaLibera.descrizione" cssClass="span9"/>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Data registrazione definitiva</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline" for="dataRegistrazioneDA">Da</label>
										</span>
										<s:textfield id="dataRegistrazioneDA" name="dataRegistrazioneDA" cssClass="span2 datepicker"/>
										<span class="al">
											<label class="radio inline" for="dataRegistrazioneA">A</label>
										</span>
										<s:textfield id="dataRegistrazioneA" name="dataRegistrazioneA" cssClass="span2 datepicker"/>
										 
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Data registrazione provvisoria</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline" for="dataRegistrazioneProvvisoriaDA">Da</label>
										</span>
										<s:textfield id="dataRegistrazioneProvvisoriaDA" name="dataRegistrazioneProvvisoriaDA" cssClass="span2 datepicker" maxlength="10" />
										<span class="al">
											<label class="radio inline" for="dataRegistrazioneProvvisoriaA">A</label>
										</span>
										<s:textfield id="dataRegistrazioneProvvisoriaA" name="dataRegistrazioneProvvisoriaA" cssClass="span2 datepicker" maxlength="10" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="importoPrimaNotaLibera">Importo</label>
									<div class="controls">
										<s:textfield id="importoPrimaNotaLibera" name="importo" cssClass="span2 soloNumeri decimale"/>
									</div>
								</div>
								
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
									<s:hidden id="HIDDEN_classificatoreGSAUid" name="primaNotaLibera.classificatoreGSA.uid" />
								</div>
							</fieldset>
						</div>  
					</div>
					
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:a action="ricercaPrimaNotaLiberaGSA" cssClass="btn btn-secondary" data-overlay-body="">annulla</s:a>
						<s:submit value="cerca" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}ztree/ztree_new.js"></script>
	<script type="text/javascript" src="${jspath}contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/classifgsa/ztree.classifgsa.js"></script>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/primaNotaLibera/ricerca.js"></script>
</body>
</html>