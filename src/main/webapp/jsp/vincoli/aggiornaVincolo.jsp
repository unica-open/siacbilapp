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
					<s:form method="post" action="aggiornamentoVincolo" novalidate="novalidate">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3>Gestione Vincolo</h3>
						
						<fieldset class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="tipoBilancio">Bilancio</label>
								<div class="controls" id="tipoBilancio">
									<label class="radio inline"><s:property value="tipoBilancio"/></label>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="codice">Codice *</label>
								<div class="controls">
									<s:textfield name="vincolo.codice" id="codice" cssClass="lbTextSmall span2" maxlength="200" disabled="true" />
									<s:hidden name="vincolo.codice" data-maintain="" />
								</div>
							</div>
							<div class="control-group">	
								<span class="al">
									<label for="descrizione" class="control-label">Descrizione</label>
								</span>  
								<div class="controls">
									<s:textarea rows="1" cols="15" id="descrizione" cssClass="span10" name="vincolo.descrizione" maxlength="500"></s:textarea>
								</div>
							</div>
							<div class="control-group">	
								<label class="control-label" for="trasferimentiVincolati">Trasferimenti vincolati</label>
								<div class="controls">
									<label class="radio inline">
										<input type="radio" name="vincolo.flagTrasferimentiVincolati" value="false" <s:if test="%{vincolo.flagTrasferimentiVincolati == false}">checked="checked"</s:if>> No
									</label>
									<label class="radio inline">
										<input type="radio" name="vincolo.flagTrasferimentiVincolati" value="true" <s:if test="%{vincolo.flagTrasferimentiVincolati == true}">checked="checked"</s:if>> S&igrave;
									</label>
								</div>
							</div>
							<div class="control-group">
								<span class="al">
									<label for="genereVincolo" class="control-label">Tipo vincolo *</label>
								</span>
								<div class="controls">
									<s:select list="listaGenereVincolo" id="genereVincolo" name="vincolo.genereVincolo.uid" cssClass="span10" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
										headerKey="" headerValue="" />
								</div>
							</div>
							<!-- SIAC-7192 -->
							<div class="control-group">
								<span class="al">
									<label for="risorsaVincolata" class="control-label">Elenco Risorse Vincolate*
										<a id="risorsaVincolataSpan" class="tooltip-test" title="Elenco Risorse Vincolate per Risultato di Amministrazione – All a2 e All a3" >
											<i class="icon-info-sign">&nbsp;<span class="nascosto">Elenco Risorse Vincolate per Risultato di Amministrazione – All a2 e All a3</span></i>
										</a>
									</label>
								</span>
								<div class="controls">
									<%--SIAC-7525 headerKey a 0 --%>
									<s:select list="listaRisorsaVincolata" id="risorsaVincolata" name="vincolo.risorsaVincolata.uid" cssClass="span10" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
										headerKey="0" headerValue="" />
								</div>
							</div>
							<!-- SIAC-7192 -->
							<div class="control-group">
								<span class="al">
									<label for="note" class="control-label">Note </label>
								</span>  
								<div class="controls">
									<s:textarea rows="1" cols="15" id="note" cssClass="span10" name="vincolo.note" maxlength="500"></s:textarea>
								</div>
							</div>
						</fieldset>
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn btn-link reset">annulla</button>
							<s:submit cssClass="btn btn-primary pull-right" value="aggiorna"/>
						</p>
						
						<div class="accordion margin-large" id="accordionEntrata">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordionEntrata" href="#collapseEntrata">
										&nbsp;Capitoli di entrata <span class="icon">&nbsp;</span>
									</a>
								</div>
								<div id="collapseEntrata" class="accordion-body collapse">
									<div class="accordion-inner">
										<table class="table table-hover dataTable" id="capitoliEntrataNelVincolo" summary="...." >
											<thead>
												<tr>
													<th></th>
													<th scope="col">Capitolo</th>
													<th scope="col">Classificazione</th>
													<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt}"/></th>
													<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt + 1}"/></th>
													<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt + 2}"/></th>
													<th scope="col">Strutt. Amm. Resp.</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
											<tfoot>
												<tr>
													<th></th>
													<th scope="col">Totale</th>
													<th scope="col">&nbsp;</th>
													<th scope="col" id="totaleStanziamentoEntrata0" class="tab_Right"><s:property value="totaleStanziamentoEntrata0"/></th>
													<th scope="col" id="totaleStanziamentoEntrata1" class="tab_Right"><s:property value="totaleStanziamentoEntrata1"/></th>
													<th scope="col" id="totaleStanziamentoEntrata2" class="tab_Right"><s:property value="totaleStanziamentoEntrata2"/></th>
													<th scope="col">&nbsp;</th>
												</tr>
											</tfoot>                    
										</table>
										<p>
											<a class="btn" href="#msgElimina" data-toggle="modal" id="pulsanteEliminaEntrata">elimina</a>
											<a class="btn" title="associa nuovo capitolo" data-toggle="collapse" data-target="#associaCapitoloEntrata">associa nuovo capitolo</a>
										</p>
										<div id="associaCapitoloEntrata" class="collapse">
											<div class="accordion_info">
												<fieldset class="form-horizontal" id="fieldsetAnagraficaCapitoloEntrata">
													<div class="control-group">
														<label class="control-label" for="annoCapitoloEntrata">Anno</label>
														<div class="controls">
															<s:textfield id="annoCapitoloEntrata" name="annoCapitolo" cssClass="lbTextSmall span1" disabled="true" value="%{annoEsercizioInt}"/>
															<span class="al">
																<label class="radio inline" for="numeroCapitoloEntrata">Capitolo *</label>
															</span>
															<s:textfield id="numeroCapitoloEntrata" name="numeroCapitolo" cssClass="lbTextSmall span2 soloNumeri" required="required" placeholder="%{'capitolo'}" maxlength="9" />
															<span class="al">
																<label class="radio inline" for="numeroArticoloEntrata">Articolo *</label>
															</span>
															<s:textfield id="numeroArticoloEntrata" name="numeroArticolo" cssClass="lbTextSmall span2 soloNumeri" required="required" placeholder="articolo" maxlength="9" />
															<s:if test="gestioneUEB">
																<span class="al">
																	<label class="radio inline" for="numeroUEBEntrata">UEB</label>
																</span>
																<s:textfield id="numeroUEBEntrata" name="numeroUEB" cssClass="lbTextSmall span2 soloNumeri" placeholder="UEB" maxlength="9" />
															</s:if><s:else>
																<s:hidden id="numeroUEBEntrata" name="numeroUEB" value="1" data-maintain="" />
															</s:else>
															<a class="btn btn-primary" href="#" id="pulsanteCercaEntrata">
																<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_cercaCapitoloEntrata"></i>
															</a>
														</div>
													</div>
												</fieldset>
												<fieldset class="form-horizontal hide" id="fieldsetCapitoloEntrata">
													<table class="table table-hover dataTable" id="capitoliEntrata" summary="...." >
														<thead>
															<tr>
																<th></th>
																<th scope="col">Capitolo</th>
																<th scope="col">Stato</th>
																<th scope="col">Classificazione</th>
																<th scope="col">Stanz.competenza</th>
																<th scope="col">Stanz. residuo</th>
																<th scope="col">Stanz.cassa</th>
																<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
																<th scope="col"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>
													<p>
														<a class="btn btn-secondary" href="#" id="pulsanteAnnullaRicercaEntrata">annulla</a>
														<a class="btn" href="#" id="pulsanteAssociaCapitoloEntrata">
															associa&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_associaCapitoloEntrata"></i>
														</a>
													</p>
												</fieldset>							  
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="accordion margin-large" id="accordionUscita">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordionUscita" href="#collapseUscita">
										&nbsp;Capitoli di spesa <span class="icon">&nbsp;</span>
									</a>
								</div>
								<div id="collapseUscita" class="accordion-body collapse">
									<div class="accordion-inner">
										<table class="table table-hover dataTable" id="capitoliUscitaNelVincolo" summary="...." >
											<thead>
												<tr>
													<th></th>
													<th scope="col">Capitolo</th>
													<th scope="col">Classificazione</th>
													<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt}"/></th>
													<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt + 1}"/></th>
													<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt + 2}"/></th>
													<th scope="col">Strutt. Amm. Resp.</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
											<tfoot>
												<tr>
													<th></th>
													<th scope="col">Totale</th>
													<th scope="col">&nbsp;</th>
													<th scope="col" id="totaleStanziamentoUscita0" class="tab_Right"><s:property value="totaleStanziamentoUscita0"/></th>
													<th scope="col" id="totaleStanziamentoUscita1" class="tab_Right"><s:property value="totaleStanziamentoUscita1"/></th>
													<th scope="col" id="totaleStanziamentoUscita2" class="tab_Right"><s:property value="totaleStanziamentoUscita2"/></th>
													<th scope="col">&nbsp;</th>
												</tr>
											</tfoot>                    
										</table>
										<p>
											<a class="btn" href="#msgElimina" data-toggle="modal" id="pulsanteEliminaUscita">elimina</a>
											<a class="btn" title="associa nuovo capitolo" data-toggle="collapse" data-target="#associaCapitoloUscita">associa nuovo capitolo</a>
										</p>
										<div id="associaCapitoloUscita" class="collapse">
											<div class="accordion_info">
												<fieldset class="form-horizontal" id="fieldsetAnagraficaCapitoloUscita">
													<div class="control-group">
														<label class="control-label" for="annoCapitoloUscita">Anno</label>
														<div class="controls">
															<s:textfield id="annoCapitoloUscita" name="annoCapitolo" cssClass="lbTextSmall span1" disabled="true" value="%{annoEsercizioInt}"/>
															<span class="al">
																<label class="radio inline" for="numeroCapitoloUscita">Capitolo *</label>
															</span>
															<s:textfield id="numeroCapitoloUscita" name="numeroCapitolo" cssClass="lbTextSmall span2 soloNumeri" required="required" placeholder="%{'capitolo'}" maxlength="9" />
															<span class="al">
																<label class="radio inline" for="numeroArticoloUscita">Articolo *</label>
															</span>
															<s:textfield id="numeroArticoloUscita" name="numeroArticolo" cssClass="lbTextSmall span2 soloNumeri" required="required" placeholder="articolo" maxlength="9" />
															<s:if test="gestioneUEB">
																<span class="al">
																	<label class="radio inline" for="numeroUEBUscita">UEB</label>
																</span>
																<s:textfield id="numeroUEBUscita" name="numeroUEB" cssClass="lbTextSmall span2 soloNumeri" placeholder="UEB" maxlength="9" />
															</s:if><s:else>
																<s:hidden id="numeroUEBUscita" name="numeroUEB" value="1" data-maintain="" />
															</s:else>
															<a class="btn btn-primary" href="#" id="pulsanteCercaUscita">
																<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_cercaCapitoloUscita"></i>
															</a>
														</div>
													</div>
												</fieldset>
												<fieldset class="form-horizontal hide"  id="fieldsetCapitoloUscita">
													<table class="table table-hover dataTable" id="capitoliUscita" summary="...." >
														<thead>
															<tr>
																<th></th>
																<th scope="col">Capitolo</th>
																<th scope="col">Stato</th>
																<th scope="col">Classificazione</th>
																<th scope="col">Stanz.competenza</th>
																<th scope="col">Stanz. residuo</th>
																<th scope="col">Stanz.cassa</th>
																<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
																<th scope="col"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>
													<p>
														<a class="btn btn-secondary" href="#" id="pulsanteAnnullaRicercaUscita">annulla</a>
														<a class="btn" href="#" id="pulsanteAssociaCapitoloUscita">
															associa&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_associaCapitoloUscita"></i>
														</a>
													</p>
												</fieldset>							  
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	
	<div aria-hidden="false" aria-labelledby="msgEliminaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="msgElimina" style="display: block;">
		<div class="modal-body">
			<div class="alert alert-error alert-persistent">
				<button data-hide="alert" class="close" type="button">&times;</button>
				<p><strong>Attenzione!!!</strong></p>
				<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
			</div>
			<input type="hidden" id="HIDDEN_tipoCapitolo" />
			<input type="hidden" id="HIDDEN_uidCapitolo" />
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
			<button class="btn btn-primary" id="eliminaCapitolo">
				si, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_elimina"></i>
			</button>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/vincolo/aggiorna.js"></script>

</body>
</html>