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
						<h3>Risultati di ricerca Predisposizione di Pagamento</h3>
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
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Conto del tesoriere">Conto del tesoriere</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Data competenza">Data competenza</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Stato doc."><abbr title="Stato documento">Stato doc.</abbr></th>
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
								<div class="span4">
									<button type="button" class="btn btn-secondary" id="pulsanteInserisciPreDocumento">inserisci predisposizione</button>
								</div>
							</s:if>
							<s:if test="associaAbilitato">
								<div class="accordion-group span4" id="accordionAssociaImputazioniContabili">
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
								<div class="accordion-group span4" id="accordionDefinisci">
									<div class="accordion-heading">
										<a href="#collapseDefinisci" data-parent="#accordionDefinisci" data-toggle="collapse" class="accordion-toggle collapsed">
											definisci predisposizione pagamento<span class="icon">&nbsp;</span>
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
								<button class="btn btn-primary" formmethod="post" type="submit" formaction="risultatiRicercaPreDocumentoSpesaAnnulla.do">s&iacute;, prosegui</button>
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
								<p>Proseguire con l'associazione sulla selezione?</p>
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
						<div aria-hidden="true" aria-labelledby="AssociaSelezLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleDefinisci">
							<div class="modal-header">
								<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
								<h4 class="nostep-pane">Definisci predisposizione pagamento</h4>
							</div>
							<div class="modal-body">
								<p><strong>Predisposizioni selezionate n. <span id="numeroPreDocumentiDaDefinire"></span></strong></p>
								<p><strong>Importo totale: <span id="importoTotalePreDocumentiDaDefinire"></span></strong></p>
								<p>Proseguire con la definizione sulla selezione?</p>
							</div>
							<div class="modal-footer">
								<button type="button" aria-hidden="true" class="btn" id="pulsanteAnnullaDefinisci">annulla</button>
								<button type="button" aria-hidden="true" class="btn btn-primary" id="pulsanteConfermaDefinisci">conferma</button>
								<s:if test="modificaAssociazioniContabiliAbilitato">
									<button type="button" aria-hidden="true" class="btn btn-secondary" id="pulsanteModificaAssociaImputazioniContabili">modifica</button>
								</s:if>
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
		<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" />
		<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale_new.jsp" />
	</s:if>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}async.js"></script>
	<s:if test="modificaAssociazioniContabiliAbilitato">
		<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
		<script type="text/javascript" src="${jspath}capitolo/ricercaCapitoloModale.js"></script>
		<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
		<script type="text/javascript" src="${jspath}movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
		<script type="text/javascript" src="${jspath}ztree/ztree_new.js"></script>
		<script type="text/javascript" src="${jspath}provvedimento/ricerca_modale_new.js"></script>
		
	</s:if>
	<script type="text/javascript" src="${jspath}predocumento/risultatiRicercaSpesa.js"></script>
</body>
</html>