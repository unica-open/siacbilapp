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
					<s:form method="post" action="aggiornamentoProgetto" novalidate="novalidate">
						<s:hidden name="progetto.uid"/>
						<s:if test="decentrato">
							<%-- Aggiungere campi disabled, tranne codice e investimento in corso di definizione --%>
							<s:hidden name="progetto.descrizione"/>
							<s:hidden name="progetto.tipoAmbito.uid"/>
							<s:hidden name="progetto.rilevanteFPV" />
							<s:hidden name="progetto.dataIndizioneGara" />
							<s:hidden name="progetto.dataAggiudicazioneGara" />
							<s:hidden name="progetto.note" />
							
							<s:if test="parereRegolaritaValido">
								<s:hidden name="progetto.valoreComplessivo" />
								<%-- Campi abilitati solo se parere finanziario e' false --%>
							</s:if>
						</s:if>
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3>Gestione Progetto</h3>
						<fieldset class="form-horizontal">
							<br>
							<div class="control-group">
								<label class="control-label" for="codice">Codice *</label>
								<div class="controls">
									<s:textfield name="progetto.codice" id="codice" cssClass="lbTextSmall span2" maxlength="200" disabled="codiceProgettoAutomatico || decentrato" />
									<s:if test="codiceProgettoAutomatico || decentrato">
										<s:hidden name="progetto.codice" />
									</s:if>
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
								<label for="stato" class="control-label">Stato</label>
								<div class="controls">
									<s:textfield name="progetto.stato" id="stato" cssClass="lbTextSmall span3" disabled="true" />
									<s:hidden name="progetto.stato"/>
								</div>
							</div>
							<div class="control-group">
								<span class="al"> <label for="descrizione" class="control-label">Descrizione *</label>
								</span>
								<div class="controls">
									<s:textarea rows="1" cols="15" id="descrizione" cssClass="span9" name="progetto.descrizione" maxlength="500" disabled="decentrato"></s:textarea>
								</div>
							</div>
							<div class="control-group">
								<label for="ambito" class="control-label">Ambito</label>
								<div class="controls">
									<s:select list="listaTipiAmbito" cssClass="span8" name="progetto.tipoAmbito.uid" id="ambito" headerKey="" headerValue=""
										listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="decentrato" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="rilFPV">Rilevante a FPV</label>
								<div class="controls">
									<s:checkbox id="rilFPV" name="progetto.rilevanteFPV" disabled="decentrato" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="valoreComplessivoProgetto">Valore complessivo</label>
								<div class="controls">
									<s:textfield id="valoreComplessivoProgetto" cssClass="lbTextSmall span3 soloNumeri decimale" name="progetto.valoreComplessivo"
										maxlength="20" placeholder="valoreComplessivo" disabled="decentrato && parereRegolaritaValido" />
								</div>
							</div>
							<%-- ahmad begin--%>
							<div class="control-group">
								<label for="dataIndizioneGara" class="control-label">Data validazione progetto a base di gara</label>
								<div class="controls">
									<s:textfield id="dataIndizioneGara" name="progetto.dataIndizioneGara" cssClass="span2 datepicker" disabled="decentrato" />
								</div>
							</div>
							<div class="control-group">
								<label for="dataAggiudicazioneGara" class="control-label">Anno Programmazione</label>
								<div class="controls">
									<s:textfield id="dataAggiudicazioneGara" name="progetto.dataAggiudicazioneGara" cssClass="span2 datepicker" disabled="decentrato" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="investimentoInCorsoDiDefinizione">Investimento in corso di definizione</label>
								<div class="controls">
									<s:checkbox id="investimentoInCorsoDiDefinizione" name="progetto.investimentoInCorsoDiDefinizione" disabled="!modificabileInvestimentoInCorsoDiDefinizione || decentrato" />
									<s:if test="%!modificabileInvestimentoInCorsoDiDefinizione || decentrato">
										<s:hidden name="progetto.investimentoInCorsoDiDefinizione"/>
									</s:if>
								</div>
							</div>
							<%-- ahmad end--%>
							<div class="control-group">
								<span class="al">
									<label for="noteProgetto" class="control-label">Note</label>
								</span>
								<div class="controls">
									<s:textarea id="noteProgetto" rows="2" cols="15" cssClass="span8" name="progetto.note" maxlength="500" disabled="decentrato"></s:textarea>
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
														<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativoContabileProgetto" href="#strutturaAmministrativoContabileProgetto">
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
												<input type="radio" value="true" name="progetto.spaziFinanziari" <s:if test="%{progetto.spaziFinanziari != null && progetto.spaziFinanziari}"> checked </s:if>>S&igrave;
											</label>
											<span class="alLeft">
												<label class="radio inline">
												
													<input type="radio" value="false" name="progetto.spaziFinanziari"<s:if test="%{progetto.spaziFinanziari == null || !progetto.spaziFinanziari}"> checked </s:if>>No
												</label>
											</span>											
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="rupProgetto"><abbr title="responsabile unico progetto">RUP</abbr></label>
										<div class="controls">
											<s:textfield id="rupProgetto" cssClass="lbTextSmall span3" name="progetto.responsabileUnico"/>
										</div>
									</div>
									
									<div class="control-group">
										<label for="modAffidamento" class="control-label">Modalit&agrave; di affidamento</label>
										<div class="controls">
											<s:select list="listaModalitaAffidamento" cssClass="span8" name="progetto.modalitaAffidamentoProgetto.uid" id="modAffidamento" headerKey="" headerValue="" listKey="uid"
											listValue="%{descrizione}" />
										</div>
									</div>
							

							<h5 class="subTitle">Calcolo FPV</h5>
							<div class="control-group">
								<label for="versCrono" class="control-label">Versione cronoprogramma da usare per FPV</label>
								<div class="controls">
									<s:select id="versCrono" list="listaElementiVersioneCronoprogramma" cssClass="span8" listValue="%{versione + ' - ' + descrizione}" listKey="uid" headerKey=""
										headerValue="Scegliere una versione per visualizzare il prospetto FPV" data-overlay="" />
								</div>
							</div>
							<div class="control-group hide" id="visualizzaPulsantiFPV">
								<div class="controls">
									<a id="pulsanteVisualizzaProspettoFPVComplessivo" href="#" class="btn btn-secondary">FPV complessivo</a>
									<span class="alLeft">
										<a id="pulsanteVisualizzaProspettoFPVUscita" href="#" class="btn btn-secondary">FPV spesa</a>
									</span>
									<span class="alLeft">
										<a id="pulsanteVisualizzaProspettoFPVEntrata" href="#" class="btn btn-secondary">FPV entrata</a>
									</span>
								</div>
							</div>
							<div class="control-group hide" id="visualizzaPulsantiFPVGestioneBilancio">
									<div class="controls">
										<a id="pulsanteVisualizzaProspettoFPVComplessivoGestioneBilancio" href="#" class="btn btn-secondary">FPV complessivo</a>
										<span class="alLeft">
											<a id="pulsanteVisualizzaProspettoFPVUscitaGestioneBilancio" href="#" class="btn btn-secondary">FPV spesa</a>
										</span>
										<span class="alLeft">
											<a id="pulsanteVisualizzaProspettoFPVEntrataGestioneBilancio" href="#" class="btn btn-secondary">FPV entrata</a>
										</span>
									</div>
							</div>
							<s:if test="decentrato && parereRegolaritaValido">
								<s:include value="/jsp/provvedimento/ricercaProvvedimentoCollapseDISABLED.jsp" />
							</s:if><s:else>
								<s:include value="/jsp/provvedimento/ricercaProvvedimentoCollapse.jsp" />
							</s:else>
						</fieldset>
						<br/>

						<h4 class="step-pane">Cronoprogrammi</h4>
						<table class="table table-hover dataTable" id="cronoprogrammiNelProgetto" summary="....">
							<thead>
								<!-- VERSIONE ALTERNATIVA -->
								<%-- <tr>
									<th rowspan="2">&nbsp;</th>
									<th rowspan="2">Versione</th>
									<th rowspan="2">Descrizione</th>
									<th rowspan="2"><abbr title="associato FPV">FPV</abbr></th>
									<th colspan="2">Anni precedenti</th>
									<th colspan="2" class="text-center"><s:property value="%{annoEsercizioInt}" /></th>
									<th colspan="2" class="text-center"><s:property value="%{annoEsercizioInt + 1}" /></th>
									<th colspan="2" class="text-center"><s:property value="%{annoEsercizioInt + 2}" /></th>
									<th colspan="2">Anni successivi</th>
									<th rowspan="2">&nbsp;</th>
								</tr>
								<tr>
									<th class="tab_Right">Entrate</th>
									<th class="tab_Right">Spese</th>
									<th class="tab_Right">Entrate</th>
									<th class="tab_Right">Spese</th>
									<th class="tab_Right">Entrate</th>
									<th class="tab_Right">Spese</th>
									<th class="tab_Right">Entrate</th>
									<th class="tab_Right">Spese</th>
									<th class="tab_Right">Entrate</th>
									<th class="tab_Right">Spese</th>
								</tr> --%>
								
								<!-- VERSIONE 2 -->
								<tr>
									<th rowspan="2">&nbsp;</th>
									<th rowspan="2">Versione</th>
									<th rowspan="2">Stato</th>
									<th rowspan="2">Impegni</th>
									<th rowspan="2"><abbr title="associato FPV">FPV</abbr></th>
									<th colspan="5" class="text-center">Entrate</th>									
									<th colspan="5"class="text-center">Spese</th>
									<th rowspan="2">&nbsp;</th>
								</tr>
								<tr>
									<th class="tab_Right">Anni precedenti</th>
									<th class="tab_Right"><s:property value="%{annoEsercizioInt}" /></th>
									<th class="tab_Right"><s:property value="%{annoEsercizioInt + 1}" /></th>
									<th class="tab_Right"><s:property value="%{annoEsercizioInt + 2}" /></th>
									<th class="tab_Right">Anni successivi</th>
									<th class="tab_Right">Anni precedenti</th>
									<th class="tab_Right"><s:property value="%{annoEsercizioInt}" /></th>
									<th class="tab_Right"><s:property value="%{annoEsercizioInt + 1}" /></th>
									<th class="tab_Right"><s:property value="%{annoEsercizioInt + 2}" /></th>
									<th class="tab_Right">Anni successivi</th>
								</tr>
								
								<%-- <tr>
									<th scope="col">&nbsp;</th>
									<th scope="col" >Versione Cronoprogramma</th>
									<th scope="col">Descrizione</th>
									<th scope="col">Associato FPV</th>
									<th scope="col">Da definire</th>
									<th class="tab_Right" scope="col">Spese <s:property value="%{annoEsercizioInt}" /></th>
									<th class="tab_Right" scope="col">Spese <s:property value="%{annoEsercizioInt + 1}" /></th>
									<th class="tab_Right" scope="col">Spese <s:property value="%{annoEsercizioInt + 2}" /></th>
									<th class="tab_Right" scope="col">Spese anni successivi</th>
									<th scope="col">&nbsp;</th>
								</tr> --%>
							</thead>
							<tbody>
							</tbody>
						</table>
						<p>
							<a class="btn btn-secondary" id="pulsanteInserimentoCronoprogramma">inserisci nuovo cronoprogramma</a> 
							<!-- jira 1982 TOLGO il tasto calcolo delta cronoprogrammi  -->
							<!--a class="btn btn-secondary" href="#" data-toggle="modal" id="pulsanteApriCalcoloDelta" draggable="false">calcolo delta cronoprogrammi</a-->
							<%--  calcoloDelta --%>
						</p>
						<div class="Border_line"></div>
						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
							<s:if test="!decentrato || !parereRegolaritaValido">
								<%--
								<s:reset cssClass="btn btn-link"  value="annulla" />
								--%>
								<button id="pulsanteAnnulla" name="pulsanteAnnulla" type="button" class="btn" >annulla</button>	
								<s:submit cssClass="btn btn-primary pull-right" value="salva" />
							</s:if>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>

	<%-- Modal  calcoloDelta --%>
	<div id="modaleCalcoloDelta" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="calcoloDeltaLabel" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="nostep-pane">Calcolo Delta tra Cronoprogrammi</h4>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal">
				<div class="control-group">
					<label class="control-label" for="cronoprogrammaPartenza">Cronoprogramma di partenza</label>
					<div class="controls">
						<s:select id="cronoprogrammaPartenza" list="listaElementiVersioneCronoprogramma" cssClass="span9"
							listValue="%{versione + ' - ' + descrizione}" listKey="uid" headerKey="" headerValue="" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="cronoprogrammaArrivo">Cronoprogramma di arrivo</label>
					<div class="controls">
						<s:select id="cronoprogrammaArrivo" list="listaElementiVersioneCronoprogramma" cssClass="span9"
							listValue="%{versione + ' - ' + descrizione}" listKey="uid" headerKey="" headerValue="" />
					</div>
				</div>
				<h4 class="step-pane">
					Calcolo delta tra Cronoprogramma "<span id="descrizioneCronoprogrammaPartenzaDelta"></span>" e Cronoprogramma "<span id="descrizioneCronoprogrammaArrivoDelta"></span>"
				</h4>
				<table class="table tab_left table-hover" id="prospettoDelta" summary="....">
					<thead>
						<tr>
							<th scope="col">Capitolo</th>
							<th scope="col">Anno competenza</th>
							<th class="tab_Right" scope="col">Valore di partenza</th>
							<th class="tab_Right" scope="col">Valore di arrivo</th>
							<th class="tab_Right" scope="col">Delta (arrivo - partenza)</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button class="btn btn-secondary" data-dismiss="modal" id="pulsanteEsportaDelta">esporta</button>
			<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
		</div>
	</div>
	<%-- end Modal calcoloDelta --%>
	<%-- Modale consultazione --%>
	<div aria-hidden="true" aria-labelledby="msgConsultaCronoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConsultaCronoprogramma">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
			<h4 class="nostep-pane">
				Prospetto riassuntivo cronoprogramma <span id="consultazioneCronoprogrammaCodice"></span>
			</h4>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal">
				<table summary="...." id="consultazioneCronoprogrammaTabella" class="table tab_left">
					<thead>
						<tr>
							<th scope="col">Anno</th>
							<th scope="col" class="tab_Right">Totali entrate</th>
							<th scope="col" class="tab_Right">Totali spese</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal" class="btn btn-primary">chiudi</button>
		</div>
	</div>
	<%-- end Modale consultazione --%>
	

	<%-- modale annulla cronoprogramma begin ahmad  --%>

	<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
			<s:hidden id="HIDDEN_UidDaAnnullare" name="uidCronoprogrammaDaAnnullare" />
			<div class="modal-body">
				<div class="alert alert-error alert-persistent">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<p>
						<strong>Attenzione!</strong>
					</p>
					<p>Stai per annullare l'elemento selezionato, questo cambiera lo stato dell'elemento : sei sicuro di voler proseguire?</p>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
				<button id="pulsanteProseguiAnnullamentoCronoprogramma" class="btn btn-primary" >si, prosegui</button>
			</div>
	</div>
	<%-- end modale annulla cronoprogramma ahmad --%>
	
	<%-- modale Associa  cronoprogramma Fpv begin ahmad  --%>
	<div id="msgAssociaCronoprogrammaFPV" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAssociaLabel" aria-hidden="true">
			<s:hidden id="HIDDEN_UidDaAssociare" name="uidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV" />
			<s:hidden id="HIDDEN_versioneCronoprogramma" name="versioneCronoprogrammaDaAssociare" />
			<s:hidden id="HIDDEN_descrizioneCronoprogramma" name="descrizioneCronoprogrammaDaAssociare" />
			
			<div class="modal-body">
				<div class="alert alert-error alert-persistent">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<p>
						<strong>Attenzione!</strong>
					</p>
					<p><span id="msgAssociaCronoprogrammaFPVspan"></span> al calcola FPV<br></p>
					<p>sei sicuro di voler proseguire?</p>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
				<button id="pulsanteAssociaCronoprogrammaFPV" class="btn btn-primary" >si, prosegui</button>
			</div>
	</div>
	<%-- end modale Associa  cronoprogramma Fpv ahmad --%>
	
	<%-- Modale quadratura --%>
	<div id="modaleVerificaQuadratura" aria-hidden="true" aria-labelledby="verQuadraturaLabel" role="dialog" tabindex="-1" class="modal hide fade">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
			<h4 class="nostep-pane">Quadratura</h4>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal">
				<table summary="...." class="table tab_left table-hover">
					<thead>
					</thead>
					<tbody>
						<tr>
							<th scope="col" class="tab_Right borderBottomLight">Entrate</th>
							<td scope="col" class="tab_Right" id="quadraturaEntrata"></td>
						</tr>
						<tr>
							<th scope="col" class="tab_Right borderBottomLight">Spese</th>
							<td scope="col" class="tab_Right" id="quadraturaUscita"></td>
						</tr>
						<tr>
							<th scope="col" class="tab_Right borderBottomLight">Differenza</th>
							<td scope="col" class="tab_Right" id="quadraturaDifferenza"></td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal" class="btn btn-primary">chiudi</button>
		</div>
	</div>
	<%-- end Modale quadratura --%>
	<%-- Modale FPV complessivo --%>
	<div aria-hidden="true" aria-labelledby="msgFPVcomplessivoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleFPVComplessivo">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
			<h4 class="nostep-pane">FPV complessivo</h4>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal">
				<table summary="...." id="tabellaFPVComplessivo"
					class="table tab_left table-hover">
					<thead>
						<tr>
							<th scope="col">&nbsp;</th>
							<th scope="col" class="tab_Right">Entrata prevista</th>
							<th scope="col" class="tab_Right">FPV entrata</th>
							<th scope="col" class="tab_Right">Spesa prevista</th>
							<th scope="col" class="tab_Right">FPV spesa</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal" class="btn btn-primary">chiudi</button>
		</div>
	</div>
	<%-- end Modale FPV complessivo --%>
	<%-- Modale FPV entrata --%>
	<div aria-hidden="true" aria-labelledby="msgFPVentrataLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleFPVEntrata">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
			<h4 class="nostep-pane">FPV entrata</h4>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal">
				<table summary="...." id="tabellaFPVEntrata" class="table tab_left table-hover">
					<thead>
						<tr>
							<th scope="col">&nbsp;</th>
							<th scope="col" class="tab_Right">Entrata prevista</th>
							<th scope="col" class="tab_Right">FPV Entrata per spesa corrente</th>
							<th scope="col" class="tab_Right">FPV entrata per spesa c/capitale</th>
							<th scope="col" class="tab_Right">Totale</th>
							<th scope="col" class="tab_Right">FPV Entrata complessivo</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal" class="btn btn-primary">chiudi</button>
		</div>
	</div>
	<%-- end Modale FPV entrata --%>
	<%-- Modale FPV uscita --%>
	<div aria-hidden="true" aria-labelledby="msgFPVspesaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleFPVUscita">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
			<h4 class="nostep-pane">FPV spesa</h4>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal">
				<table summary="...." id="tabellaFPVUscita"
					class="table tab_left table-hover">
					<thead>
						<tr>
							<th scope="col">&nbsp;</th>
							<th scope="col" class="tab_Right">Missione</th>
							<th scope="col" class="tab_Right">Programma</th>
							<th scope="col" class="tab_Right">Titolo</th>
							<th scope="col" class="tab_Right">Spesa prevista</th>
							<th scope="col" class="tab_Right">FPV spesa</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal" class="btn btn-primary">chiudi</button>
		</div>
	</div>
	<%-- end Modale impegni collegati --%>
	<div id="modaleImpegniCollegatiCronoprogramma" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="impegniCrono" aria-hidden="true">
		<div class="row-fluid">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="nostep-pane">Impegni collegati al cronoprogramma <span id="codiceCrono"> </span></h4>
			</div>
			<div class="modal-body">			
				<table class="table table-hover tab_left" summary="...." id="tabellaImpegniCollegatiAlCronoprogramma">
					<thead>
						<tr id="txt_Left">
							<th scope="col">Impegno</th>
							<th scope="col">Capitolo</th>
							<th scope="col">Provvedimento</th>
							<th scope="col">Soggetto</th>	
							<th class="tab_Right" scope="col">Importo</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
					<tfoot>					
					</tfoot>
				</table>
			</div>
			<div class="modal-footer">
				<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
			</div>
		</div>
	</div>

	<%--fpvGestioneBilancio.jsp contiene le modali corrispondenti a DaBilancio e DaGestione per i calcoli di FPV--%>
	<s:include value="/jsp/progetto/include/fpvCronoprogrammaGestioneBilancio.jsp" />
	
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}ztree/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricercaProvvedimento_collapse.js"></script>
	<script type="text/javascript" src="${jspath}progetto/aggiorna.js"></script>
</body>
</html>