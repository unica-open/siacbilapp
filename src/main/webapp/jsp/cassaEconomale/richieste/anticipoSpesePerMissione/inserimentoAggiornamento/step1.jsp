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
				<s:hidden name="uidValutaEuro" id="HIDDEN_uidValutaEuro" />
				<s:hidden name="codiceAmbito" id="HIDDEN_codiceAmbito" />
				<s:hidden name="baseUrl" id="HIDDEN_baseUrl" />
				<s:hidden name="isAggiornamento" id="HIDDEN_isAggiornamento" />
				
				<s:form cssClass="form-horizontal" action="%{urlStep1}" novalidate="novalidate" id="formRichiestaEconomale">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden name="richiestaEconomale.uid" id="uidRichiestaEconomale" />
					<s:hidden name="richiestaEconomale.statoOperativoRichiestaEconomale" id="statoOperativoRichiestaEconomaleRichiestaEconomale" />
					<s:hidden name="richiestaEconomale.numeroRichiesta" id="numeroRichiestaRichiestaEconomale" />
					<s:hidden name="richiestaEconomale.numeroRendicontoStampato" id="numeroRendicontoStampatoRichiestaEconomale" />
					<s:hidden name="richiestaEconomale.dataRendicontoStampato" id="dataRendicontoStampatoRichiestaEconomale" />
					<s:hidden name="richiestaEconomale.idMissioneEsterna" id="idMissioneEsternaRichiestaEconomale" />
					<h3>Anticipo spese per missione</h3>
					<s:if test="isAggiornamento">
						<s:hidden name="richiestaEconomale.dataCreazione" id="dataCreazioneRichiestaEconomale" />
						<h3><s:property value="stringaRiepilogoRichiestaEconomale" /></h3>
					</s:if>
					<div class="wizard" id="MyWizard">
						<ul class="steps">
							<li class="active" data-target="#step1"><span class="badge">1</span>Dati richiesta<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Dettaglio<span class="chevron"></span></li>
							<li data-target="#step3"><span class="badge">3</span>Riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal">
								<s:if test="!isAggiornamento">
									<s:include value="/jsp/cassaEconomale/richieste/include/copiaDatiDa.jsp"></s:include>
									<div class="clear"></div>
								</s:if>
								<s:include value="/jsp/cassaEconomale/richieste/include/datiRichiedente.jsp">
									<s:param name="shouldDisable"><s:property value="fromMissioneEsterna"/></s:param>
								</s:include>
								
								<h4>Dati della richiesta</h4>
								<div class="control-group">
									<label class="control-label" for="motivoDatiTrasfertaMissioneRichiestaEconomale">Motivo trasferta *</label>
									<div class="controls">
										<s:textarea id="motivoDatiTrasfertaMissioneRichiestaEconomale" name="richiestaEconomale.datiTrasfertaMissione.motivo"
											rows="1" cols="15" cssClass="span9" required="true" disabled="fromMissioneEsterna"></s:textarea>
									</div>
								</div>
								<div class="control-group">
									<span class="control-label">Estero *</span>
									<div class="controls">
										<label class="radio inline">
											<s:radio theme="simple" name="richiestaEconomale.datiTrasfertaMissione.flagEstero" list="#{true:'Sì'}" disabled="fromMissioneEsterna"/>
										</label>
										<label class="radio inline">
											<s:radio theme="simple" name="richiestaEconomale.datiTrasfertaMissione.flagEstero" list="#{false:'No'}" disabled="fromMissioneEsterna"/>
										</label>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="luogoDatiTrasfertaMissioneRichiestaEconomale">Luogo trasferta *</label>
									<div class="controls">
										<s:textfield id="luogoDatiTrasfertaMissioneRichiestaEconomale" name="richiestaEconomale.datiTrasfertaMissione.luogo" cssClass="span9" required="true" disabled="fromMissioneEsterna" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="mezziDiTrasportoRichiestaEconomale">Mezzo di trasporto</label>
									<div class="controls">
										<s:select list="listaMezziDiTrasporto" id="mezziDiTrasportoRichiestaEconomale" name="mezziDiTrasportoSelezionati.uid"
											data-placeholder="Seleziona i mezzi di trasporto" multiple="true" cssClass="chosen-select span9" headerKey="0" headerValue=""
											listKey="uid" listValue="descrizione" value="%{mezziDiTrasportoSelezionati.{uid}}" disabled="fromMissioneEsterna" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="dataInizioDatiTrasfertaMissioneRichiestaEconomale">Data inizio *</label>
									<div class="controls">
										<s:textfield id="dataInizioDatiTrasfertaMissioneRichiestaEconomale" name="richiestaEconomale.datiTrasfertaMissione.dataInizio" cssClass="span2 datepicker" required="true" disabled="fromMissioneEsterna" />
										<span class="al">
											<label class="radio inline" for="dataFineDatiTrasfertaMissioneRichiestaEconomale">Data fine *</label>
											<s:textfield id="dataFineDatiTrasfertaMissioneRichiestaEconomale" name="richiestaEconomale.datiTrasfertaMissione.dataFine" cssClass="span2 datepicker" required="true" disabled="fromMissioneEsterna" />
										</span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="delegatoAllIncassoRichiestaEconomale">Delegato incasso</label>
									<div class="controls">
										<s:textfield id="delegatoAllIncassoRichiestaEconomale" name="richiestaEconomale.delegatoAllIncasso" cssClass="span6" disabled="fromMissioneEsterna" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label"for="noteRichiestaEconomale">Note</label>
									<div class="controls">
										<s:textarea id="noteRichiestaEconomale" name="richiestaEconomale.note" rows="2" cols="15" cssClass="span9" disabled="fromMissioneEsterna"></s:textarea>
									</div>
									<span class="pull-right">
										<button type="button" id="pulsanteVisualizzaImporti" class="btn btn-primary">
											visualizza importi&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteVisualizzaImporti"></i>
										</button>
									</span>
								</div>
								
								<s:include value="/jsp/cassaEconomale/richieste/include/classificatori.jsp" />
								
								<h4>Giustificativi</h4>
								<table class="table table-hover tab_left" id="tabellaGiustificativi">
									<thead>
										<tr>
											<th class="span12">Tipo</th>
											<th class="span2">Valuta</th>
											<th class="span2">Cambio</th>
											<th class="tab_Right">Quantit&agrave;</th>
											<th class="tab_Right span5">Importo valuta estera</th>
											<th class="tab_Right span5">Importo</th>
											<th class="tab_Right span5">Importo spettante</th>
											<s:if test="%{!isAggiornamento}">
												<th></th>
												<th></th>
											</s:if>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="5">Totale</th>
											<th class="tab_Right" id="totaleGiustificativi"></th>
											<th class="tab_Right" id="totaleSpettanteGiustificativi"></th>
											<s:if test="%{!isAggiornamento}">
												<th></th>
												<th></th>
											</s:if>
										</tr>
									</tfoot>
								</table>
								<s:if test="%{!isAggiornamento && !fromMissioneEsterna}">
									<p class="margin-medium">
										<button type="button" class="btn btn-secondary" id="pulsanteInserimentoDati">inserisci dati in elenco</button>
									</p>
								</s:if>
								<div class="clear"></div>
								<br/>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<a data-href="<s:property value="urlAnnullaStep1"/>" class="btn secondary" id="pulsanteAnnullaStep1">annulla</a>
						<s:if test="gestioneMissioneEsterna">
							<button type="button" class="btn secondary" id="pulsanteInserisciRichiestaDaCaricamentoEsterno">
								inserisci richiesta da caricamento esterno&nbsp;<i class="icon-spin icon-refresh spinner"></i>
							</button>
						</s:if>
						<s:submit value="prosegui" cssClass="btn btn-primary pull-right" />
					</p>
					<div id="divImportiCassa"></div>
				</s:form>
				<s:if test="%{!isAggiornamento}">
					<s:include value="/jsp/cassaEconomale/tabelle/modaleAnnullamento.jsp" />
					<s:include value="/jsp/cassaEconomale/richieste/anticipoSpesePerMissione/inserimentoAggiornamento/modaleGiustificativo.jsp" />
					<s:include value="/jsp/cassaEconomale/richieste/include/modaleRicercaMatricola.jsp" />
					<s:if test="gestioneMissioneEsterna">
						<s:include value="/jsp/cassaEconomale/richieste/include/modaleMissioneEsterna.jsp" />
					</s:if>
				</s:if>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cassaEconomale/richieste/ricercaMatricola.js"></script>
	<s:if test="gestioneMissioneEsterna">
		<script type="text/javascript" src="/siacbilapp/js/local/cassaEconomale/richieste/missioneEsterna.js"></script>
	</s:if>
	<script type="text/javascript" src="/siacbilapp/js/local/cassaEconomale/richieste/anticipoSpesePerMissione/inserisci.aggiorna.step1.js"></script>
	
</body>
</html>