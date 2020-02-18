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
				<s:form cssClass="form-horizontal" action="%{urlRicerca}" novalidate="novalidate" id="formRichiestaEconomale">
					<s:hidden name="tipoRichiestaEconomale.uid" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3><s:property value="denominazioneRicerca" /></h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<p class="margin-medium">
									<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
								</p>
								<div class="clear"></div>
								<h4>Dati della richiesta</h4>
								<div class="control-group">
									<label class="control-label" for="numeroRichiestaRichiestaEconomale">Numero richiesta</label>
									<div class="controls">
										<s:textfield id="numeroRichiestaRichiestaEconomale" name="richiestaEconomale.numeroRichiesta" cssClass="span2 soloNumeri" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Data richiesta</label>
									<div class="controls">
										<span class="alRight">
											<label class="radio inline" for="dataCreazioneDa">Da</label>
										</span>
										<s:textfield id="dataCreazioneDa" name="dataCreazioneDa" cssClass="span2 datepicker" />
										<span class="alRight">
											<label class="radio inline" for="dataCreazioneA">A</label>
										</span>
										<s:textfield id="dataCreazioneA" name="dataCreazioneA" cssClass="span2 datepicker" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="numeroSospesoSospesoRichiestaEconomale">Numero sospeso</label>
									<div class="controls">
										<s:textfield id="numeroSospesoSospesoRichiestaEconomale" name="richiestaEconomale.sospeso.numeroSospeso" cssClass="span2 soloNumeri" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="numeroMovimentoMovimentoRichiestaEconomale">Numero movimento</label>
									<div class="controls">
										<s:textfield id="numeroMovimentoMovimentoRichiestaEconomale" name="richiestaEconomale.movimento.numeroMovimento" cssClass="span2 soloNumeri" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Data operazione</label>
									<div class="controls">
										<span class="alRight">
											<label class="radio inline" for="dataMovimentoDa">Da</label>
										</span>
										<s:textfield id="dataMovimentoDa" name="dataMovimentoDa" cssClass="span2 datepicker" />
										<span class="alRight">
											<label class="radio inline" for="dataMovimentoA">A</label>
										</span>
										<s:textfield id="dataMovimentoA" name="dataMovimentoA" cssClass="span2 datepicker" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codiceSoggettoSoggettoRichiestaEconomale">Matricola</label>
									<div class="controls">
										<s:textfield id="codiceSoggettoSoggettoRichiestaEconomale" name="richiestaEconomale.soggetto.matricola" cssClass="span2" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="descrizioneDellaRichiestaRichiestaEconomale">Descrizione della spesa</label>
									<div class="controls">
										<s:textarea id="descrizioneDellaRichiestaRichiestaEconomale" name="richiestaEconomale.descrizioneDellaRichiesta" rows="1" cols="15" cssClass="span9"></s:textarea>
									</div>
								</div>
								<div class="control-group">
								<label class="control-label" for="statoOperativoRichiestaEconomaleRichiestaEconomale">Stato</label>
									<div class="controls">
										<s:select list="listaStatoOperativoRichiestaEconomale" id="statoOperativoRichiestaEconomaleRichiestaEconomale"
											name="richiestaEconomale.statoOperativoRichiestaEconomale" listValue="descrizione" headerKey="" headerValue="" cssClass="span6" />
									</div>
								</div>
								
								<s:include value="/jsp/cassaEconomale/richieste/include/classificatori.jsp" />
							
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset value="annulla" cssClass="btn btn-secondary" />
						<s:submit value="cerca" cssClass="btn btn-primary pull-right" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	
</body>
</html>