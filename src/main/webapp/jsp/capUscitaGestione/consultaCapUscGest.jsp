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
							<!-- <li id="tabGestione"><a href="#gestione" data-toggle="tab" class="disabled">Stanziamenti esercizio precedente</a></li> -->
							<%-- SIAC-5254 --%>
							<li id="tabImpegni"><a href="#impegni" data-toggle="tab" class="disabled">Impegni</a></li>
							<li id="tabLiquidazioni"><a href="#liquidazioni" data-toggle="tab" class="disabled">Liquidazioni</a></li>
							<li id="tabOrdinativi"><a href="#ordinativi" data-toggle="tab" class="disabled">Ordinativi</a></li>
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
										<dd><s:property value="capitolo.dataAnnullamento"/>&nbsp;</dd>
									</dl>
								</div>
								<div class="boxOrSpan2">
									<div class="boxOrInLeft" style="width: 100%; margin-right: 0; background-color: white; padding-top:10px;">
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
												<dfn>Missione</dfn>
												<dl><s:property value="missione.codice"/> <s:property value="missione.descrizione"/></dl>
											</li>
											<li>
												<dfn>Programma</dfn>
												<dl><s:property value="programma.codice"/> <s:property value="programma.descrizione"/></dl>
											</li>
											<!-- task-55 -->
											<li>
												<dfn>Capitolo da non inserire nell'allegato A1</dfn>
												<dl>
												     <s:if test='capitolo.flagNonInserireAllegatoA1 != null'>
														 <s:if test='capitolo.flagNonInserireAllegatoA1'>S&iacute;</s:if>
														 <s:else>No</s:else>
													 </s:if>
												</dl>
											</li>
											
											<li>
												<dfn>Cofog</dfn>
												<dl><s:property value="classificazioneCofog.codice"/> <s:property value="classificazioneCofog.descrizione"/></dl>
											</li>
											<li>
												<dfn>Titolo</dfn>
												<dl><s:property value="titoloSpesa.codice"/> <s:property value="titoloSpesa.descrizione"/></dl>
											</li>
											<li>
												<dfn>Macroaggregato</dfn>
												<dl><s:property value="macroaggregato.codice"/> <s:property value="macroaggregato.descrizione"/></dl>
											</li>
											<li>
												<dfn><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</dfn>
												<dl><s:property value="elementoPianoDeiConti.codice"/> <s:property value="elementoPianoDeiConti.descrizione"/></dl>
											</li>
											<li>
												<dfn><abbr title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr></dfn>
												<dl><s:property value="siopeSpesa.codice"/> <s:property value="siopeSpesa.descrizione"/></dl>
											</li>
											<li>
												<dfn><abbr title="Struttura amministrativa">Strutt Amm</abbr> Responsabile</dfn>
												<dl><s:property value="strutturaAmministrativoContabile.codice"/> <s:property value="strutturaAmministrativoContabile.descrizione"/> <s:property value="strutturaAmministrativoContabile.assessorato" /></dl>
											</li>
											<li>
												<dfn><abbr title="N. variazione">Variazione</abbr></dfn>
												<%--dd><s:property value="variazioneDiBilancio.numero" /></dd--%>
												<dl></dl>
											</li>
											<li>
												<dfn>Tipo Capitolo</dfn>
												<dl><s:property value="capitolo.categoriaCapitolo.codice"/> <s:property value="capitolo.categoriaCapitolo.descrizione"/></dl>
											</li>
											<li>
												<dfn>Risorsa accantonata</dfn>
												<dl><s:property value="capitolo.risorsaAccantonata.descrizione"/></dl>
											</li>
											<li>
												<dfn>Impegnabile</dfn>
												<dl><s:if test="capitolo.flagImpegnabile">S&iacute;</s:if><s:else>No</s:else></dl>
											</li>
										</ul>
									</div>
								</div>

								<s:include value="/jsp/capUscitaGestione/include/consultaCapUscGestAltriDati.jsp" />
								<hr>
								<s:include value="/jsp/capUscitaGestione/include/consultaCapUscGestStanziamenti.jsp" />								
								<br/>
								<s:include value="/jsp/capUscitaGestione/include/tabellaDisponibilitaCapUscGest.jsp" />
								<br/>
							</div>
							<%-- Vincoli --%>
							<div class="tab-pane" id="vincoli"></div>
							<%-- Atti di legge --%>
							<div class="tab-pane" id="atti"></div>
							<%-- Movimenti --%>
							<div class="tab-pane" id="movimenti"></div>
							<div class="tab-pane" id="variazioniCodifiche"></div>
							<div class="tab-pane" id="gestione">
								<s:include value="/jsp/cap/include/consulta_esercizio_precedente.jsp" />
							</div>
							<div class="tab-pane" id="impegni">
								<s:include value="/jsp/capUscitaGestione/include/consultaImpegni.jsp" />
							</div>
							<div class="tab-pane" id="liquidazioni">
								<s:include value="/jsp/capUscitaGestione/include/consultaLiquidazioni.jsp" />
							</div>
							<div class="tab-pane" id="ordinativi">
								<s:include value="/jsp/capUscitaGestione/include/consultaOrdinativi.jsp" />
							</div>
							
							
						</div>
						<!--fine tab-->
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</form>
					<div class="hide" id="iframeContainer"></div>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/tabellaComponenteImportiCapitolo.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/consultaCapitolo.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitoloUscitaGestione/consulta.js"></script>
</body>
</html>