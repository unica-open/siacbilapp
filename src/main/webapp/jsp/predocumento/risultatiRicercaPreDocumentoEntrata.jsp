<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<s:include value="/jsp/include/head.jsp" />
</head>

<body>
	<s:include value="/jsp/include/header.jsp" />

	<!-- TABELLE RIEPILOGO -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp" />
					<form method="post">
						<s:hidden id="importoTotale" name="importoTotale" />
						<s:hidden id="HIDDEN_anno_datepicker" value="%{annoEsercizioInt}" />
						<h3>Risultati di ricerca Predisposizione di Incasso</h3>
						<h5>Dati di ricerca: <s:property value="riepilogoRicerca" /></h5>
						<h4><span id="id_num_result" class="num_result"></span></h4>
						
						<table class="table table-hover tab_left dataTable" id="risultatiRicercaPreDocumento">
							<thead>
								<tr role="row">
									<th role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending">
										<input type="checkbox" class="tooltip-test" data-original-title="Seleziona tutti" id="checkboxSelezionaTutti">
									</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Numero">Numero</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Struttura">Struttura</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Causale">Causale</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Conto del tesoriere">Conto corrente</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Data competenza">Data competenza</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Stato doc."><abbr title="Stato documento">Stato doc.</abbr></th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Nominativo">Nominativo</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Soggetto">Soggetto</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Stato">Stato</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Data esecuzione">Data esecuzione</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Importo" class="tab_Right">Importo</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1"></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
								<tr>
									<th colspan="10">Totale importi:</th>
									<th class="tab_Right"><s:property value="importoTotale"/></th>
									<th></th>
								</tr>
							</tfoot>
						</table>

						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
						
						<div id="divAzioni">
							<s:if test="inserisciAbilitato">
								<div class="<s:property value="spanClassOperazioni"/>">
									<button type="button" class="btn btn-secondary" id="pulsanteInserisciPreDocumento">inserisci predisposizione</button>
								</div>
							</s:if>
							<s:if test="associaAbilitato">
								<div class="accordion-group <s:property value="spanClassOperazioni"/>" id="accordionAssociaImputazioniContabili">
									<div class="accordion-heading">
										<a href="#collapseAssociaImputazioniContabili" data-parent="#accordionAssociaImputazioniContabili" data-toggle="collapse" class="accordion-toggle collapsed">
											associa imputazioni contabili<span class="icon">&nbsp;</span>
										</a>
									</div>
									<div class="accordion-body collapse" id="collapseAssociaImputazioniContabili">
										<div>
											<ul class="listSelectAccordion">
												<li>
													<a id="pulsanteAssociaImputazioniContabiliTutti" href="#"><span class="iconSmall icon-chevron-right"></span>tutti</a>
												</li>
												<li>
													<a id="pulsanteAssociaImputazioniContabiliSelezionati" href="#"><span class="iconSmall icon-chevron-right"></span>solo selezionati</a>
												</li>
											</ul>
										</div>
									</div>
								</div>
							</s:if>
							<s:if test="definisciAbilitato">
								<div class="accordion-group <s:property value="spanClassOperazioni"/>" id="accordionDefinisci">
									<div class="accordion-heading">
										<a href="#collapseDefinisci" data-parent="#accordionDefinisci" data-toggle="collapse" class="accordion-toggle collapsed">
											definisci predisposizione incasso<span class="icon">&nbsp;</span>
										</a>
									</div>
									<div class="accordion-body collapse" id="collapseDefinisci">
										<div>
											<ul class="listSelectAccordion">
												<li>
													<a id="pulsanteDefinisciTutti" href="#"><span class="iconSmall icon-chevron-right"></span>tutti</a>
												</li>
												<li>
													<a id="pulsanteDefinisciSelezionati" href="#"><span class="iconSmall icon-chevron-right"></span>solo selezionati</a>
												</li>
											</ul>
										</div>
									</div>
								</div>
							</s:if>
							<s:if test="dataTrasmissioneAbilitato">
								<div class="accordion-group <s:property value="spanClassOperazioni"/>" id="accordionDataTrasmissione">
									<div class="accordion-heading">
										<a href="#collapseDataTrasmissione" data-parent="#accordionDataTrasmissione" data-toggle="collapse" class="accordion-toggle collapsed">
											aggiorna data trasmissione<span class="icon">&nbsp;</span>
										</a>
									</div>
									<div class="accordion-body collapse" id="collapseDataTrasmissione">
										<div>
											<ul class="listSelectAccordion">
												<li>
													<a id="pulsanteDataTrasmissioneTutti" href="#"><span class="iconSmall icon-chevron-right"></span>tutti</a>
												</li>
												<li>
													<a id="pulsanteDataTrasmissioneSelezionati" href="#"><span class="iconSmall icon-chevron-right"></span>solo selezionati</a>
												</li>
											</ul>
										</div>
									</div>
								</div>
							</s:if>
						</div>

						<%-- Modale ANNULLA --%>
						<div id="modaleAnnullaPreDocumento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
							<s:hidden id="HIDDEN_UidDaAnnullare" name="uidDaAnnullare" />
							<div class="modal-body">
								<div class="alert alert-error">
									<button type="button" class="close" data-hide="alert">&times;</button>
									<p><strong>Attenzione!</strong></p>
									<p><strong>Elemento selezionato: <span id="elementoSelezionatoAnnullamento"></span></strong></p>
									<p>Stai per annullare l'elemento selezionato: sei sicuro di voler proseguire?</p>
								</div>
							</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
								<button class="btn btn-primary" formmethod="post" type="submit" formaction="risultatiRicercaPreDocumentoEntrataAnnulla.do">s&iacute;, prosegui</button>
							</div>
						</div>
						
						<%-- Modale ASSOCIA --%>
						<div aria-hidden="true" aria-labelledby="AssociaTuttiLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleAssociaImputazioniContabili">
							<div class="modal-header">
								<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
								<h4 class="nostep-pane">Associa imputazioni contabili</h4>
							</div>
							<div class="modal-body">
								<p><strong>Predisposizioni selezionate n. <span id="numeroPreDocumentiDaAssociare"></span></strong></p>
								<p><strong>Importo totale: <span id="importoTotalePreDocumentiDaAssociare"></span></strong></p>
								<s:if test="modificaAssociazioniContabiliAbilitato">
									<p>
										<strong>Riepilogo:</strong>
										<br/>
										<s:property value="riepilogoImputazioniContabili" escape="false" />
									</p>
								</s:if>
								<s:if test="modificaAccertamentoDisponibile">
									<p> L'associazione potrebbe comportare l'adeguamento della disponibilit&agrave; degli accertamenti associati alle predisposizioni di incasso.<p>
									<div class="control-group">
										<div class="controls">
											<label class="radio inline">
												<input type="radio" value="true" name="chooseAdeguaDisponibilitaAccertamento" checked> Adegua la disponibilit&agrave; se necessario
											</label>
											<label class="radio inline">
												<input type="radio" id="" value="false" name="chooseAdeguaDisponibilitaAccertamento">Non adeguare la disponibilit&agrave;
											</label>
										</div>
									</div> 
								</s:if><s:else>
									<input type="hidden" name="chooseAdeguaDisponibilitaAccertamento" value="false" />
								</s:else>
								<%-- modificaAccertamentoDisponibile --%>
								<p> Proseguire con l'associazione sulla selezione?</p>
							</div>
							<div class="modal-footer">
								<button type="button" aria-hidden="true" class="btn" id="pulsanteAnnullaAssociaImputazioniContabili">annulla</button>
								<button type="button" aria-hidden="true" class="btn btn-primary" id="pulsanteConfermaAssociaImputazioniContabili">conferma</button>
								<s:if test="modificaAssociazioniContabiliAbilitato">
									<button type="button" aria-hidden="true" class="btn btn-secondary" id="pulsanteModificaAssociaImputazioniContabili">modifica</button>
								</s:if>
							</div>
						</div>
						
						<%-- Modale DEFINISCI --%>
						<div aria-hidden="true" aria-labelledby="DefinisciSelezLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleDefinisci">
							<div class="modal-header">
								<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
								<h4 class="nostep-pane">Definisci predisposizione incasso</h4>
							</div>
							<div class="modal-body">
								<p><strong>Predisposizioni selezionate n. <span id="numeroPreDocumentiDaDefinire"></span></strong></p>
								<p><strong>Importo totale: <span id="importoTotalePreDocumentiDaDefinire"></span></strong></p>
								<p>Proseguire con la definizione sulla selezione?</p>
							</div>
							<div class="modal-footer">
								<button type="button" aria-hidden="true" class="btn" id="pulsanteAnnullaDefinisci">annulla</button>
								<button type="button" aria-hidden="true" class="btn btn-primary" id="pulsanteConfermaDefinisci">conferma</button>
							</div>
						</div>
						
						<%-- Modale DATA TRASMISSIONE --%>
						<div aria-hidden="true" aria-labelledby="DataTrasmissioneSelezLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleDataTrasmissione">
							<div class="modal-header">
								<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
								<h4 class="nostep-pane">Aggiornamento data trasmissione predisposizione incasso</h4>
							</div>
							<div class="modal-body">
								<p><strong>Predisposizioni selezionate n. <span id="numeroPreDocumentiDaDataTrasmissione"></span></strong></p>
								<p><strong>Importo totale: <span id="importoTotalePreDocumentiDaDataTrasmissione"></span></strong></p>
								<p>Data di trasmissione *&nbsp;&nbsp;<input type="text" class="span2 datepicker" name="dataTrasmissione" id="dataTrasmissione"/></p>
								<p>Proseguire con la modifica della data sulla selezione?</p>
							</div>
							<div class="modal-footer">
								<button type="button" aria-hidden="true" class="btn" id="pulsanteAnnullaDataTrasmissione">annulla</button>
								<button type="button" aria-hidden="true" class="btn btn-primary" id="pulsanteConfermaDataTrasmissione">conferma</button>
							</div>
						</div>
						
						<s:if test="modificaAssociazioniContabiliAbilitato">
							<div class="clear"></div>
							<div class="Border_line"></div>
							<div class="hide" id="collapseImputazioniContabili">
								<h2>Modifica imputazioni contabili</h2>
								<input type="hidden" name="inviaTutti" id="hidden_inviaTutti" />
								<div id="containerImputazioni"></div>
								<p class="pull-right">
									<button type="button" class="btn btn-secondary" id="pulsanteAnnullaImputazioniContabili">annulla</button>
									<button type="button" class="btn btn-primary" id="pulsanteConfermaImputazioniContabili">conferma</button>
								</p>
							</div>
						</s:if>
						<div class="clear"></div>
						<div class="Border_line"></div>
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>

	<s:if test="modificaAssociazioniContabiliAbilitato">
		<s:include value="/jsp/capEntrataGestione/selezionaCapitolo_modale.jsp" />
		<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
		<s:include value="/jsp/movimentoGestione/modaleAccertamento.jsp" />
		<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale_new.jsp" />
	</s:if>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}async.js"></script>
	<s:if test="modificaAssociazioniContabiliAbilitato">
		<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
		<script type="text/javascript" src="${jspath}capitolo/ricercaCapitoloModale.js"></script>
		<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
		<script type="text/javascript" src="${jspath}movimentoGestione/ricercaAccertamentoOttimizzato.js"></script>
		<script type="text/javascript" src="${jspath}ztree/ztree_new.js"></script>
		<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale_new.js"></script>
	</s:if>
	<script type="text/javascript" src="${jspath}predocumento/risultatiRicercaEntrata.js"></script>
</body>
</html>
