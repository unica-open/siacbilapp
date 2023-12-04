<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-json-tags" prefix="json"%>
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
					<form method="get" action="">
						<h3>
							Capitolo <s:property value="capitoloEntrataGestione.numeroCapitolo"/>/<s:property value="capitoloEntrataGestione.numeroArticolo"/>
						</h3>
						<ul class="nav nav-tabs">
							<li class="active"><a href="#capitolo" data-toggle="tab">Capitolo</a></li>
							<li><a href="#uebCollegate" data-toggle="tab"><abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr></a></li>
						</ul>

						<div class="tab-content">
							<div class="tab-pane active" id="capitolo">
								<h3>Capitolo</h3>
								<div class="boxOrSpan2">
									<div class="boxOrInLeft">
										<p>Dati generali</p>
										<ul class="htmlelt">
											<li>
												<dfn>Descrizione Capitolo</dfn>
												<dl><s:property value="capitoloEntrataGestione.descrizione"/></dl>
											</li>
											<li>
												<dfn>Descrizione Articolo</dfn>
												<dl><s:property value="capitoloEntrataGestione.descrizioneArticolo"/></dl>
											</li>
											<s:if test="titoloEntrataEditabile">
												<li>
													<dfn>Titolo</dfn>
													<dl><s:property value="titoloEntrata.codice"/> <s:property value="titoloEntrata.descrizione"/></dl>
												</li>
											</s:if>
											<s:if test="tipologiaTitoloEditabile">
												<li>
													<dfn>Tipologia</dfn>
													<dl><s:property value="tipologiaTitolo.codice"/> <s:property value="tipologiaTitolo.descrizione"/></dl>
												</li>
											</s:if>
											<s:if test="categoriaTipologiaTitoloEditabile">
												<li>
													<dfn>Categoria</dfn>
													<dl><s:property value="categoriaTipologiaTitolo.codice"/> <s:property value="categoriaTipologiaTitolo.descrizione"/></dl>
												</li>
											</s:if>
											<s:if test="elementoPianoDeiContiEditabile">
												<li>
													<dfn><abbr title="Piano dei Conti">P.d.C.</abbr></dfn>
													<dl><s:property value="elementoPianoDeiConti.codice"/> <s:property value="elementoPianoDeiConti.descrizione"/></dl>
												</li>
											</s:if>
											<s:if test="siopeEntrataEditabile">
												<li>
													<dfn><abbr title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr></dfn>
													<dl><s:property value="siopeEntrata.codice"/> <s:property value="siopeEntrata.descrizione"/></dl>
												</li>
											</s:if>
											<s:if test="strutturaAmministrativoContabileEditabile">
												<li>
													<dfn><abbr title="Struttura amministrativa">Strutt Amm</abbr> Responsabile</dfn>
													<dl><s:property value="strutturaAmministrativoContabile.codice"/> <s:property value="strutturaAmministrativoContabile.descrizione"/> <s:property value="strutturaAmministrativoContabile.assessorato" /></dl>
												</li>
											</s:if>
											<li>
												<dfn><abbr title="N. variazione">Variazione</abbr></dfn>
												<%--dl><s:property value="variazioneDiBilancio.numero" /></dl--%>
												<dl></dl>
											</li>
										</ul>
									</div>
									<div class="boxOrInRight">
										<p>Altri dati</p>
										<ul class="htmlelt">
											<s:if test="tipoFinanziamentoEditabile">
												<li>
													<dfn>Tipo Finanziamento</dfn>
													<dl><s:property value="tipoFinanziamento.codice"/> <s:property value="tipoFinanziamento.descrizione"/></dl>
												</li>
											</s:if>
											<s:if test="flagRilevanteIvaEditabile">
												<li>
													<dfn>Rilevante IVA</dfn>
													<dl><s:if test="capitoloEntrataGestione.flagRilevanteIva">S&iacute;</s:if><s:else>No</s:else></dl>
												</li>
											</s:if>
											<s:if test="tipoFondoEditabile">
												<li>
													<dfn>Tipo fondo</dfn>
													<dl><s:property value="tipoFondo.codice"/> <s:property value="tipoFondo.descrizione"/></dl>
												</li>
											</s:if>

											<s:if test="ricorrenteEntrataEditabile">
												<li>
													<dfn>Ricorrente</dfn>
													<dl><s:property value="ricorrenteEntrata.descrizione"/></dl>
												</li>
											</s:if>
											<s:if test="perimetroSanitarioEntrataEditabile">
												<li>
													<dfn>Codifica identificativo del perimetro sanitario</dfn>
													<dl><s:property value="perimetroSanitarioEntrata.codice"/> <s:property value="perimetroSanitarioEntrata.descrizione"/></dl>
												</li>
											</s:if>
											<s:if test="transazioneUnioneEuropeaEntrataEditabile">
												<li>
													<dfn>Codifica transazione UE</dfn>
													<dl><s:property value="transazioneUnioneEuropeaEntrata.codice"/> <s:property value="transazioneUnioneEuropeaEntrata.descrizione"/></dl>
												</li>
											</s:if>
											<s:iterator var="idx" begin="36" end="%{lastIndexClassificatoriGenerici}">
												<s:if test="%{#attr['labelClassificatoreGenerico' + #idx] != null && #attr['classificatoreGenerico' + #idx + 'Editabile']}">
													<li>
														<dfn><s:property value="%{#attr['labelClassificatoreGenerico' + #idx]}"/></dfn>
														<dl><s:property value="%{#attr['labelClassificatoreGenerico' + #idx + '.codice']}"/> <s:property value="%{#attr['labelClassificatoreGenerico' + #idx + '.descrizione']}"/></dl>
													</li>
												</s:if>
											</s:iterator>
											<s:if test="noteEditabile">
												<li>
													<dfn>Note</dfn>
													<dl><s:property value="capitoloEntrataGestione.note"/></dl>
												</li>
											</s:if>
										</ul>
									</div>
								</div>
								<h4>Stanziamenti</h4>
								<table class="table table-hover table-bordered">
									<thead>
										<tr class="row-slim-bottom">
											<th>&nbsp;</th>
											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 0}</th>
											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 1}</th>
											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 2}</th>
										</tr>
										<tr class="row-slim-top">
											<th>&nbsp;</th>
											<th scope="col" class="text-center">Iniziale</th>
											<th scope="col" class="text-center">Finale</th>
											<th scope="col" class="text-center">Iniziale</th>
											<th scope="col" class="text-center">Finale</th>
											<th scope="col" class="text-center">Iniziale</th>
											<th scope="col" class="text-center">Finale</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<th>Competenza</th>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione0.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione0.stanziamento" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione1.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione1.stanziamento" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione2.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione2.stanziamento" /></td>
										</tr>

										<tr>
											<th>Residuo</th>
											<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataGestione0.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione0.stanziamentoResiduo" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione1.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione1.stanziamentoResiduo" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione2.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione2.stanziamentoResiduo" /></td>
										</tr>
										<tr>
											<th>Cassa</th>
											<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataGestione0.stanziamentoCassaIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione0.stanziamentoCassa" /></td>
											<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataGestione1.stanziamentoCassaIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione1.stanziamentoCassa" /></td>
											<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataGestione2.stanziamentoCassaIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataGestione2.stanziamentoCassa" /></td>
										</tr>
									</tbody>
								</table>
							</div>

							<div class="tab-pane" id="uebCollegate">
								<h4><abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr> Collegate</h4>
								<table class="table table-striped table-bordered table-hover dataTable" id="risultatiricerca" summary="....">
									<thead>
										<tr>
											<th scope="col">Capitolo</th>
											<th scope="col">Stato</th>
											<th scope="col">Classificazione</th>
											<th scope="col">Stanz.competenza</th>
						        			<th scope="col">Stanz. residuo</th>
											<th scope="col">Stanz.cassa</th>
											<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
											<th scope="col"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
											<th scope="col">Azioni</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>

								<!-- Modale ELIMINA -->
								<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
									<s:hidden id="HIDDEN_UidDaEliminare" name="uidDaEliminare" />
									<div class="modal-body">
										<div class="alert alert-error">
	            							<button type="button" class="close" data-hide="alert">&times;</button>
	          								<p><strong>Attenzione!</strong></p>
	          								<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
	        							</div>
									</div>
									<div class="modal-footer">
										<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
										<a class="btn btn-primary" id="pulsanteElimina" href="#">s&igrave;, prosegui</a>
									</div>
								</div>
								<!-- /Modale ELIMINA -->

								<!-- Modale ANNULLA -->
								<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
									<s:hidden id="HIDDEN_UidDaAnnullare" name="uidDaAnnullare" />
									<div class="modal-body">
										<div class="alert alert-error">
											<button type="button" class="close" data-hide="alert">&times;</button>
											<p><strong>Attenzione!</strong></p>
											<p>
												Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento:
												sei sicuro di voler proseguire?
											</p>
										</div>
									</div>
									<div class="modal-footer">
										<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
										<a class="btn btn-primary" id="pulsanteAnnulla" href="#">s&igrave;, prosegui</a>
									</div>
								</div>
	  							<!-- /Modale ANNULLA -->
							</div>
						</div>
						<!--fine tab-->
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/risultatiRicerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/consultaMassiva.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloEntrataGestione/consultaMassiva.js"></script>
</body>
</html>