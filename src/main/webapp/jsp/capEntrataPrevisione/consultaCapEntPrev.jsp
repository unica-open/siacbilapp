<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden name="openTab" id="HIDDEN_openTab" />
						<h3>
							Capitolo <s:property value="capitolo.numeroCapitolo"/> / <s:property value="capitolo.numeroArticolo"/>
							<s:if test="gestioneUEB">
								/ <s:property value="capitolo.numeroUEB"/>
							</s:if>
						</h3>
						<s:hidden name="capitolo.uid" id="HIDDEN_uidCapitolo" />
						<s:hidden name="bilancio.uid" id="HIDDEN_uidBilancio" />
						<ul class="nav nav-tabs">
							<li class="active"><a href="#capitolo" data-toggle="tab">Capitolo</a></li>
							<li id="tabVincoli"><a href="#vincoli" data-toggle="tab" class="disabled">Vincoli</a></li>
							<li id="tabAtti"><a href="#atti" data-toggle="tab" class="disabled">Atti di legge</a></li>
							<li id="tabMovimenti"><a href="#movimenti" data-toggle="tab" class="disabled">Variazioni importi</a></li>
							<li id="tabVariazioniCodifiche"><a href="#variazioniCodifiche" data-toggle="tab" class="disabled">Variazioni codifiche</a></li>
							<li id="tabGestione"><a href="#gestione" data-toggle="tab" class="disabled">Capitolo di gestione</a></li>
						</ul>

						<div class="tab-content">
							<div class="tab-pane active" id="capitolo">
								<h3>Capitolo</h3>
								<div class="well">
									<dl class="dl-horizontal-inline">
										<dt>Stato capitolo:</dt>
										<dd><s:property value="capitolo.statoOperativoElementoDiBilancio"/>&nbsp;</dd>
										<dt>Anno creazione:</dt>
										<dd><s:property value="capitolo.annoCreazioneCapitolo" />&nbsp;</dd>
										<dt>Data annullamento:</dt>
										<dd><s:property value="capitolo.dataAnnullamento"/> &nbsp;</dd>
									</dl>
								</div>
								<div class="boxOrSpan2">
									<div class="boxOrInLeft">
										<p>Dati generali</p>
										<ul class="htmlelt">
											<li>
												<dfn>Descrizione Capitolo</dfn>
												<dl><s:property value="capitolo.descrizione"/></dl>
											</li>
											<li>
												<dfn>Descrizione Articolo</dfn>
												<dl><s:property value="capitolo.descrizioneArticolo"/></dl>
											</li>
											<li>
												<dfn>Titolo</dfn>
												<dl><s:property value="titoloEntrata.codice"/> <s:property value="titoloEntrata.descrizione"/></dl>
											</li>
											<li>
												<dfn>Tipologia</dfn>
												<dl><s:property value="tipologiaTitolo.codice"/> <s:property value="tipologiaTitolo.descrizione"/></dl>
											</li>
											<li>
												<dfn>Categoria</dfn>
												<dl><s:property value="categoriaTipologiaTitolo.codice"/> <s:property value="categoriaTipologiaTitolo.descrizione"/></dl>
											</li>
											<li>
												<dfn><abbr title="Piano dei Conti">P.d.C.</abbr></dfn>
												<dl><s:property value="elementoPianoDeiConti.codice"/> <s:property value="elementoPianoDeiConti.descrizione"/></dl>
											</li>
											<li>
												<dfn><abbr title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr></dfn>
												<dl><s:property value="siopeEntrata.codice"/> <s:property value="siopeEntrata.descrizione"/></dl>
											</li>
											<li>
												<dfn><abbr title="Struttura amministrativa">Strutt Amm</abbr> Responsabile</dfn>
												<dl><s:property value="strutturaAmministrativoContabile.codice"/> <s:property value="strutturaAmministrativoContabile.descrizione"/> <s:property value="strutturaAmministrativoContabile.assessorato" /></dl>
											</li>
											<li>
												<dfn>Tipo Capitolo</dfn>
												<dl><s:property value="capitolo.categoriaCapitolo.codice"/> <s:property value="capitolo.categoriaCapitolo.descrizione"/></dl>
											</li>
											<li>
												<dfn>Accertabile</dfn>
												<dl><s:if test="capitolo.flagImpegnabile">S&iacute;</s:if><s:else>No</s:else></dl>
											</li>
											<!-- SIAC-7858 CM 13/05/2021 Inizio -->
											<li>
												<dfn>Capitolo pertinente per il calcolo FCDE</dfn>
												<dl><s:if test="capitolo.flagEntrataDubbiaEsigFCDE">S&iacute;</s:if><s:else>No</s:else></dl>
											</li>
											<!-- SIAC-7858 CM 13/05/2021 Fine -->
											<li>
												<dfn>Accertato per cassa</dfn>
												<dl><s:if test="capitolo.flagAccertatoPerCassa">S&iacute;</s:if><s:else>No</s:else></dl>
											</li>
										</ul>
									</div>
									<div class="boxOrInRight">
										<p>Altri dati</p>
										<ul class="htmlelt">
											<li>
												<dfn>Ex Anno / Capitolo / Articolo <s:if test="gestioneUEB"> / <abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr></s:if></dfn>
												<dl>
													<s:if test="%{capitolo.exCapitolo != null && capitolo.exArticolo != null}">
														<s:property value="capitolo.exAnnoCapitolo"/> / <s:property value="capitolo.exCapitolo"/> / <s:property value="capitolo.exArticolo"/><s:if test="gestioneUEB"> / <s:property value="capitolo.exUEB"/></s:if>
													</s:if>
												</dl>
											</li>
											<li>
												<dfn>Tipo Finanziamento</dfn>
												<dl><s:property value="tipoFinanziamento.codice"/> <s:property value="tipoFinanziamento.descrizione"/></dl>
											</li>
											<li>
												<dfn>Corsivo per memoria</dfn>
												<dl><s:if test="capitolo.flagPerMemoria">S&iacute;</s:if><s:else>No</s:else></dl>
											</li>
											<li>
												<dfn>Rilevante IVA</dfn>
												<dl><s:if test="capitolo.flagRilevanteIva">S&iacute;</s:if><s:else>No</s:else></dl>
											</li>
											<li>
												<dfn>Tipo fondo</dfn>
												<dl><s:property value="tipoFondo.codice"/> <s:property value="tipoFondo.descrizione"/></dl>
											</li>
											<li>
												<dfn>Ricorrente</dfn>
												<dl><s:property value="ricorrenteEntrata.descrizione"/></dl>
											</li>
											<li>
												<dfn>Codifica identificativo del perimetro sanitario</dfn>
												<dl><s:property value="perimetroSanitarioEntrata.codice"/> <s:property value="perimetroSanitarioEntrata.descrizione"/></dl>
											</li>
											<li>
												<dfn>Codifica transazione UE</dfn>
												<dl><s:property value="transazioneUnioneEuropeaEntrata.codice"/> <s:property value="transazioneUnioneEuropeaEntrata.descrizione"/></dl>
											</li>
											<s:iterator var="idx" begin="36" end="%{lastIndexClassificatoriGenerici}">
												<s:if test="%{#attr['labelClassificatoreGenerico' + #idx] != null}">
													<li>
														<dfn><s:property value="%{#attr['labelClassificatoreGenerico' + #idx]}"/></dfn>
														<dl><s:property value="%{#attr['classificatoreGenerico' + #idx + '.codice']}"/> <s:property value="%{#attr['classificatoreGenerico' + #idx + '.descrizione']}"/></dl>
													</li>
												</s:if>
											</s:iterator>
											<li>
												<dfn>Note</dfn>
												<dl><s:property value="capitolo.note"/></dl>
											</li>
										</ul>
									</div>
								</div>
								<div class="boxOrSpan2">
                                     <div class="boxOrInLeft">
										<p>Disponibilit&agrave;</p>
										<ul class="htmlelt">
											<li>
												<dfn>Disponibilit&agrave; Variare ${annoEsercizioInt + 0}</dfn>
												<dl class="text-right"><s:property value="capitolo.importiCapitolo.disponibilitaVariare"></s:property></dl>
											</li>
											<li>
												<dfn>Disponibilit&agrave; Variare ${annoEsercizioInt + 1}</dfn>
												<dl class="text-right"><s:property value="capitolo.importiCapitolo.disponibilitaVariareAnno2"></s:property></dl>
											</li>
                                            <li>
												<dfn>Disponibilit&agrave; Variare ${annoEsercizioInt + 2}</dfn>
												<dl class="text-right"><s:property value="capitolo.importiCapitolo.disponibilitaVariareAnno3"></s:property></dl>
											</li>
										</ul>
                                     </div>
                                </div>
								<h4>Stanziamenti</h4>
								<table class="table table-hover table-bordered">
									<thead>
										<tr class="row-slim-bottom">
											<th>&nbsp;</th>
											<th scope="col" class="text-center">${annoEsercizioInt - 1}</th>
											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 0}</th>
											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 1}</th>
											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 2}</th>
										</tr>
										<tr class="row-slim-top">
											<th>&nbsp;</th>
											<th scope="col" class="text-center">&nbsp;</th>
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
											<td class="text-right"><s:property value="importiEx.stanziamento" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione0.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione0.stanziamento" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione1.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione1.stanziamento" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione2.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione2.stanziamento" /></td>
										</tr>

										<tr>
											<th>Residuo</th>
											<td class="text-right" scope="row"><s:property value="importiEx.stanziamentoResiduo" /></td>
											<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione0.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione0.stanziamentoResiduo" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione1.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione1.stanziamentoResiduo" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione2.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione2.stanziamentoResiduo" /></td>
										</tr>
										<tr>
											<th>Cassa</th>
											<td class="text-right" scope="row"><s:property value="importiEx.stanziamentoCassa" /></td>
											<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione0.stanziamentoCassaIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione0.stanziamentoCassa" /></td>
											<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione1.stanziamentoCassaIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione1.stanziamentoCassa" /></td>
											<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione2.stanziamentoCassaIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione2.stanziamentoCassa" /></td>
										</tr>
									</tbody>
								</table>
							</div>
							<%-- Vincoli --%>
							<div class="tab-pane" id="vincoli"></div>
							<%-- Atti di legge --%>
							<div class="tab-pane" id="atti"></div>
							<%-- Movimenti --%>
							<div class="tab-pane" id="movimenti"></div>
							<div class="tab-pane" id="variazioniCodifiche">	</div>
							<div class="tab-pane" id="gestione">
								<s:include value="/jsp/cap/include/consulta_gestione_entrata.jsp" />
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
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/consultaCapitolo.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloEntrataPrevisione/consulta.js"></script>
</body>
</html>