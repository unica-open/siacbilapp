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
				<s:form cssClass="form-horizontal" action="ricercaPrimaNotaLiberaINV_effettuaRicerca" novalidate="novalidate" id="formPrimaNotaLibera">
					<s:hidden name="primaNotaLibera.uid" />
					<s:hidden name="ambito" id="ambito" />
					<s:hidden name="ambitoliste" id="ambitoliste" />
					<s:hidden name="ambitoContoCausale" id="ambitoContoCausale" />
					<s:hidden name="ambitoSuffix" id="ambitoricercasuffisso" />	
					<s:include value="/jsp/include/messaggi.jsp" />
				
					<h3>Prime note elaborate da Inventario</h3>
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
									<label class="control-label" for="codiceCespite">Cespite</label>
									<div class="controls">
										<s:textfield name="cespite.codice" cssClass="span3" id="codiceCespite" />
										<span id="descrizioneCespite"></span>
										<span class="radio guidata">
											<button type="button" class="btn btn-primary " id="pulsanteCompilazioneGuidataCespite">compilazione guidata</button>
										</span>
									</div>
								</div>
								
								
								
								<%--
								<div class="control-group">
									<label class="control-label" for="importoPrimaNotaLibera">Importo</label>
									<div class="controls">
										<s:textfield id="importoPrimaNotaLibera" name="importo" cssClass="span2 soloNumeri decimale"/>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="missione">Missione</label>
									<div class="controls">
										<s:select list="listaMissione" id="missione" cssClass="span9" name="missione.uid" required="false"
											headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="programma">
										Programma
										<a class="tooltip-test" title="selezionare prima la Missione" href="#">
											<i class="icon-info-sign">&nbsp;
												<span class="nascosto">selezionare prima la Missione</span>
											</i>
										</a>
									</label>
									<div class="controls">
										<select id="programma" class="span9" name="programma.uid" disabled data-old-value="<s:property value="programma.uid" />"></select>
									</div>
								</div>
								 --%>
							</fieldset>
						</div>  
					</div>
					
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset value="annulla" cssClass="btn btn-secondary" />
						<s:submit value="cerca" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
				
				
 				<s:include value="/jsp/cespiti/include/modaleRicercaConto.jsp"> 
 					<s:param name="ambito" value="%{ambitoFIN}" /> 
 				</s:include> 
				
				<s:include value="/jsp/cespiti/include/modaleRicercaCespite.jsp" />
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/ricercaCespite.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/ricercaConto.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/primaNotaLibera/ricerca.js"></script>
</body>
</html>