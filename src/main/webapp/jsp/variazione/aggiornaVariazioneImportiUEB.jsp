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
				<s:include value="/jsp/include/messaggi.jsp" />
				<s:form cssClass="form-horizontal" action="concludiAggiornamentoVariazioneImporti" novalidate="novalidate" id="aggiornaVariazioneImportiConUEB" method="post">
					<h3>Aggiorna Variazione</h3>
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle" href="#collapseVariazioni" data-parent="#accordion2" data-toggle="collapse">
								Variazione<span class="icon"></span>
							</a>
						</div>
						<div id="collapseVariazioni" class="accordion-body in collapse" style="height: auto;">
							<div class="accordion-inner">
								<dl class="dl-horizontal">
									<dt>Num. variazione</dt>
									<dd>&nbsp;<s:property value="numeroVariazione" /></dd>
									<dt>Stato</dt>
									<dd>&nbsp;<s:property value="elementoStatoOperativoVariazione.descrizione" /></dd>
									<dt>Applicazione</dt>
									<dd>&nbsp;<s:property value="applicazione" /></dd>
									<dt>Tipo Variazione</dt>
									<dd>&nbsp;<s:property value="tipoVariazione.codice" />&nbsp;-&nbsp;<s:property value="tipoVariazione.descrizione" /></dd>
									<dt>Anno di competenza</dt>
									<dd>&nbsp;<s:property value="annoCompetenza" /></dd>
								</dl>
								<h5>Elenco modifiche in variazione</h5>
								<div id="capitoliNellaVariazione" >    
									<table class="table table-condensed table-hover table-bordered" id="tabellaGestioneVariazioni" summary="....">
										<thead>
											<tr>
												<th scope="col">Capitolo</th>
												<th scope="col" class="text-center">Competenza</th>
												<th scope="col" class="text-center">Residuo</th>
												<th scope="col" class="text-center">Cassa</th>
												<th scope="col">&nbsp;</th>
												<th scope="col">&nbsp;</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
										<tfoot>
											<tr class="info">
												<th>Totale entrate</th>
												<td><span id="totaleEntrateCompetenzaVariazione"></span></td>
												<td><span id="totaleEntrateResiduoVariazione"></span></td>
												<td><span id="totaleEntrateCassaVariazione"></span></td>
												<td>&nbsp;</td>
												<td>&nbsp;</td>
											</tr>
											<tr class="info">
												<th>Totale spese</th>
												<td><span id="totaleSpeseCompetenzaVariazione"></span></td>
												<td><span id="totaleSpeseResiduoVariazione"></span></td>
												<td><span id="totaleSpeseCassaVariazione"></span></td>
												<td>&nbsp;</td>
												<td>&nbsp;</td>
											</tr>
											<tr class="info">
												<th>Differenza</th>
												<td><span id="differenzaCompetenzaVariazione"></span></td>
												<td><span id="differenzaResiduoVariazione"></span></td>
												<td><span id="differenzaCassaVariazione"></span></td>
												<td>&nbsp;</td>
												<td>&nbsp;</td>
											</tr>
										</tfoot>
									</table>
								</div>
								<s:include value ="/jsp/variazione/include/ricercaCapitoloNellaVariazione.jsp"/>
								<div id="divEsportazioneDati" class="form-horizontal">
									<button id="pulsanteEsportaDati" type="submit" class="pull-left btn btn-secondary">
										Esporta capitoli in Excel <i class="icon-download-alt icon-large"></i>&nbsp;
									</button>
									<button id="pulsanteEsportaDatiXlsx" type="submit" class="pull-left btn btn-secondary">
										Esporta capitoli in Excel (XLSX) <i class="icon-download-alt icon-large"></i>&nbsp;
									</button>
								</div>
								<br/>
								<h5>Aggiorna le note e/o la descrizione</h5>
								<div class="control-group">
									<label class="control-label" for="descrizioneVariazione">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizioneVariazione" placeholder="descrizione" cssClass="span10" name="descrizione" maxlength="500" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label for="noteVariazione" class="control-label">Note</label>
									<div class="controls">
										<s:textarea rows="2" cols="55" cssClass="span10" id="noteVariazione" name="note" maxlength="500"></s:textarea>
									</div>
								</div>
								
								<s:include value="/jsp/provvedimento/ricercaProvvedimentoCollapse.jsp" />
								<!-- PROVVEDIMENTO OPZIONALE -->
								<s:include value ="/jsp/provvedimento/ricercaProvvedimentoAggiuntivoCollapse.jsp"/>
																
								<s:hidden name="specificaUEB.rientroDaInserimentoNuovaUEB" id="HIDDEN_rientroDaInserimentoNuovaUEB"/>
							</div>
						</div>
					</div>
					<div>
						<div>
							<div>
								<p>&nbsp;</p>
								<p>
									<button type="button" class="btn" id="pulsanteApriRicercaCapitolo">ricerca capitolo</button>
								</p>
								<div>
									<div id="collapse_ricerca" class="collapse">
										<h4>Ricerca capitolo</h4>
										<fieldset class="form-horizontal">
											<div class="control-group">
												<span class="control-label">Capitolo</span>
												<div class="controls">
													<label class="radio inline">
														<input type="radio" name="specificaUEB.tipoCapitolo" data-maintain value="Entrata" <s:if test='%{specificaUEB.tipoCapitolo eq "Entrata"}'>checked="checked"</s:if>>&nbsp;Entrata
													</label>
													<label class="radio inline">
														<input type="radio" name="specificaUEB.tipoCapitolo" data-maintain value="Uscita" <s:if test='%{specificaUEB.tipoCapitolo eq "Uscita"}'>checked="checked"</s:if>>&nbsp;Spesa
													</label>
													<s:hidden name="applicazione" id="HIDDEN_tipoApplicazione" data-maintain="" />
													<s:hidden name="annoEsercizioInt" id="HIDDEN_annoEsercizio" data-maintain="" />
													<s:hidden name="annoCompetenza" id="HIDDEN_annoVariazione" data-maintain="" />
												</div>
											</div>
											<div class="control-group">
												<label class="control-label" for="annoCapitolo">Anno</label>
												<div class="controls">
													<s:textfield id="annoCapitolo" cssClass="lbTextSmall span2" value="%{annoEsercizioInt}" disabled="true" data-maintain="" />
													<s:hidden name="specificaUEB.annoCapitolo" data-maintain="" />
													<span class="al">
														<label class="radio inline" for="numeroCapitolo">Capitolo *</label>
													</span>
													<s:textfield id="numeroCapitolo" cssClass="lbTextSmall span2 soloNumeri" name="specificaUEB.numeroCapitolo" required="true" maxlength="9" />
													<span class="al">
														<label class="radio inline" for="numeroArticolo">Articolo *</label>
													</span>
													<s:textfield id="numeroArticolo" cssClass="lbTextSmall span2 soloNumeri" name="specificaUEB.numeroArticolo" required="true" maxlength="9" />
													<button type="button" class="btn btn-primary"  id="pulsanteRicercaCapitolo">
														<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_CapitoloSorgente"></i>
													</button>
												</div>
											</div>
											<button type="button" class="btn" id="redirezioneNuovoCapitoloButton" >nuovo capitolo</button>
										</fieldset>
										<div id="divRicercaCapitolo" class="hide">
											<h5>Risultati della ricerca</h5>
											<s:hidden name="specificaUEB.numeroUEB" id="maxNumeroUEB" />
											<div class="box-border">
												<dl class="dl-horizontal-inline">
													<dt>Anno:</dt>
													<dd><span id="annoCapitoloTrovato"></span></dd>
													<dt>Capitolo/Articolo:</dt>
													<dd><span id="numeroCapitoloArticoloTrovato"></span></dd>
													<dt>Tipo:</dt>
													<dd><span id="categoriaCapitoloTrovato"></span></dd>
													<dt>Descrizione:</dt>
													<dd><span id="descrizioneCapitoloTrovato"></span></dd>
												</dl>
												<table class="table table-hover table-condensed table-bordered" id="tabellaStanziamentiTotali">
													<tr>
														<th>&nbsp;</th>
														<th class="text-center"><s:property value="%{annoEsercizioInt}"/></th>
														<th class="text-center"><s:property value="%{annoEsercizioInt + 1}"/></th>
														<th class="text-center"><s:property value="%{annoEsercizioInt + 2}"/></th>
													</tr>
													<tr>
														<th>Totale competenza</th>
														<td><span id="totaleCompetenzaTrovatoAnno0"></span></td>
														<td><span id="totaleCompetenzaTrovatoAnno1"></span></td>
														<td><span id="totaleCompetenzaTrovatoAnno2"></span></td>
													</tr>
					
													<tr>
														<th>Totale residuo</th>
														<td><span id="totaleResiduiTrovatoAnno0"></span></td>
														<td><span id="totaleResiduiTrovatoAnno1"></span></td>
														<td><span id="totaleResiduiTrovatoAnno2"></span></td>
													</tr>
													<tr>
														<th>Totale cassa</th>
														<td><span id="totaleCassaTrovatoAnno0"></span></td>
														<td><span id="totaleCassaTrovatoAnno1"></span></td>
														<td><span id="totaleCassaTrovatoAnno2"></span></td>
													</tr>
												</table>
											</div>
											<button class="btn" type="button" id="annullaCapitoloButton">annulla capitolo</button><%-- Gestione annullamento capitolo --%>
											
											<div class="pull-centered margin-large">
												<button type="button" class="btn btn-collapse pull-center" id="collapseGestioneUEB">
													<i class="icon-plus-sign icon-2x"></i>&nbsp;gestione UEB
												</button>
											</div>
										</div>
									</div>
									<div id="collapseUEB" class="collapse-body collapse">
										<div class="collapse-inner">
											<div id="div_elencoUEB">
												<h4><span id="titoloElencoUEB"></span></h4> <%--Elenco UEB capitolo selezionato E-2013/20155/1</h4> --%>
												<table class="table table-bordered" id="tabellaUEBTrovate" summary="....">
													<thead>
														<tr>
															<th scope="col">Capitolo/Articolo</th>
															<th scope="col" class="text-center">Competenza</th>
															<th scope="col" class="text-center">Residuo</th>
															<th scope="col" class="text-center">Cassa</th>
															<th scope="col">&nbsp;</th>
														</tr>
													</thead>
													<tbody>
													</tbody>
												</table>
												<p>
													<button  type="button" class="btn" id="redirezioneNuovaUEBButton" >nuova UEB</button>
												</p>
											</div>
										</div>
									</div>
								</div>
								
								<p class="margin-large">
									<s:include value="/jsp/include/indietro.jsp" />
									&nbsp;<span class="nascosto"> | </span>
									<s:reset cssClass="btn btn-link" value="annulla" />
									&nbsp;<span class="nascosto"> | </span>									
									<button id="aggiornaVariazioneButton" type='button' title='salva variazione' class="btn" >&nbsp;salva&nbsp;<i class="icon-spin icon-refresh spinner" id="spinner_salva" data-spinner-async></i></button>
									&nbsp;<span class="nascosto"> | </span>						
									<button id="annullaVariazioneButton" type='button' id="" class="btn" >annulla variazione</button>
									&nbsp;<span class="nascosto"> | </span>									
									<button type="button" id="concludiVariazioneButton"  class="btn btn-primary pull-right" title='concludi attività variazione'>&nbsp;concludi attivit&agrave;&nbsp;<i class="icon-spin icon-refresh spinner" id="spinner_concludi" data-spinner-async></i></button>									
								</p>
							</div>
						</div>
					</div>
					<div id="editStanziamenti" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="row-fluid overlay-modale">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
								<h3 id="myModalLabel">Modifica gli importi - Anno di competenza selezionato: <s:property value="%{annoVariazione}"/></h3>
							</div>
							<div class="modal-body">
								<div class="alert alert-error alert-persistent hide" id="ERRORI_modaleEditStanziamenti">
									<button type="button" class="close" data-hide="alert">&times;</button>
									<strong>Attenzione!!</strong><br>
									<ul>
									</ul>
								</div>
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label class="control-label" for="competenzaVariazioneUEB">Competenza</label>
										<div class="controls">
											<s:textfield id="competenzaVariazioneUEB" cssClass="lbTextSmall span6 text-right decimale soloNumeri" name="competenzaVariazioneUEB" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="residuoVariazioneUEB">Residuo</label>
										<div class="controls">
											<s:if test="%{isResiduoEditabile}">
												<s:textfield id="residuoVariazioneUEB" cssClass="lbTextSmall span6 text-right decimale soloNumeri" name="residuoVariazioneUEB" />
											</s:if><s:else>
												<s:textfield id="residuoVariazioneUEB" disabled="true" cssClass="lbTextSmall span6 text-right decimale soloNumeri" name="residuoVariazioneUEB" />
											</s:else>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="cassaVariazioneUEB">Cassa</label>
										<div class="controls">
											<s:if test="%{isCassaEditabile}">
												<s:textfield id="cassaVariazioneUEB" cssClass="lbTextSmall span6 text-right decimale soloNumeri" name="cassaVariazioneUEB" />
											</s:if><s:else>
												<s:textfield id="cassaVariazioneUEB" disabled="true" cssClass="lbTextSmall span6 text-right decimale soloNumeri" name="cassaVariazioneUEB" />
											</s:else>											
										</div>
									</div>
								</fieldset>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
								<button type="button" class="btn btn-primary" id="EDIT_salva">inserisci modifiche</button>
							</div>
						</div>
					</div>
					<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
						<div class="overlay-modale">
							<div class="modal-body">
								<div class="alert alert-error alert-persistent">
									<p>
										<strong>Attenzione!!!</strong>
									</p>
									<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
								</div>
								<input type="hidden" id="HIDDEN_uebDaEliminare" />
							</div>
							<div class="modal-footer">
								<button type="button" class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
								<button type="button" class="btn btn-primary" id="EDIT_elimina">s&iacute;, prosegui</button>
							</div>
						</div>
					</div>
					
					<s:include value="/jsp/include/modaleConfermaProsecuzioneSuAzione.jsp" />
					
					<%-- Modale annulla variazione --%>
					<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
						<div class="modal-body">
							<div class="alert alert-error alert-persistent">
								<p>
									<strong>Attenzione!!!</strong>
								</p>
								<p>Stai per annullare la variazione: sei sicuro di voler proseguire?</p>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
							<button type="button" class="btn btn-primary" id="EDIT_annulla">s&iacute;, prosegui</button>
						</div>
					</div>
	<%-- /Modale annulla --%>
				</s:form>
			</div>
		</div>
	</div>

	<%-- Modali --%>
	<%-- Modale modifica stanziamenti Singola UEB --%>
	<%-- /Modale modifica stanziamenti SingolaUEB --%>
	<%-- Modale eliminazione --%>
	
	<%-- /Modale eliminazione --%>
	
	
	
	<div id="iframeContainer"></div>
	<s:include value="/jsp/include/footer.jsp"/>
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}ztree/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricercaProvvedimento_collapse.js"></script>
	<script type="text/javascript" src="${jspath}variazioni/variazioni.js"></script>
	<script type="text/javascript" src="${jspath}variazioni/aggiorna.importi.ueb.js"></script>

</body>
</html>