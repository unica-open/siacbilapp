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
												<dfn>Impegnabile</dfn>
												<dl><s:if test="capitolo.flagImpegnabile">S&iacute;</s:if><s:else>No</s:else></dl>
											</li>
										</ul>
									</div>
								</div>

								<h4>Stanziamenti</h4>
								<br/>
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
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamento" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamento" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamento" /></td>
 										</tr>  
									
 										<tr>  
 											<th>- al netto impegni reimp. e da anni preced.</th>  
 											<td class="text-right"></td>  
										<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti0" /></td>
 											<td class="text-right"></td>  
										<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti1" /></td>
 											<td class="text-right"></td>  
										<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti2" /></td>
 										</tr>  
									<s:if test="hasMassimoImpegnabile">
 											<tr>  
 												<th>- assegnato</th>  
 												<td class="text-right"></td>  
											<td class="text-right"><s:property value="massimoImpegnabile0" /></td>
 												<td class="text-right"></td>  
											<td class="text-right"><s:property value="massimoImpegnabile1" /></td>
 												<td class="text-right"></td>  
											<td class="text-right"><s:property value="massimoImpegnabile2" /></td>
 											</tr>  
									</s:if>
<%-- 
 										<tr>  
 											<th>Residuo</th>  
										<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.stanziamentoResiduoIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoResiduo" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoResiduoIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoResiduo" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoResiduoIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoResiduo" /></td>
 										</tr>  
 										<tr>  
 											<th>Cassa</th>  
										<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.stanziamentoCassaIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoCassa" /></td>
										<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione1.stanziamentoCassaIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoCassa" /></td>
										<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione2.stanziamentoCassaIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoCassa" /></td>
 										</tr>   --%>
 									</tbody>  
 								</table> 
 								<br/> 
								
<!-- 								<table class="table table-hover table-bordered"> -->
<!-- 									<thead> -->
<!-- 										<tr class="row-slim-bottom"> -->
<!-- 											<th>&nbsp;</th> -->
<%-- 											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 0}</th> --%>
<%-- 											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 1}</th> --%>
<%-- 											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 2}</th> --%>
<!-- 										</tr> -->
<!-- 										<tr class="row-slim-top"> -->
<!-- 											<th>&nbsp;</th> -->
<!-- 											<th scope="col" class="text-center">Iniziale</th> -->
<!-- 											<th scope="col" class="text-center">Finale</th> -->
<!-- 											<th scope="col" class="text-center">Iniziale</th> -->
<!-- 											<th scope="col" class="text-center">Finale</th> -->
<!-- 											<th scope="col" class="text-center">Iniziale</th> -->
<!-- 											<th scope="col" class="text-center">Finale</th> -->
<!-- 										</tr> -->
<!-- 									</thead> -->
<!-- 									<tbody> -->
<!-- 										<tr> -->
<!-- 											<th>Competenza</th> -->
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamento" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamento" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamento" /></td> --%>
<!-- 										</tr> -->
										
<!-- 										<tr> -->
<!-- 											<th>- al netto impegni reimp. e da anni preced.</th> -->
<!-- 											<td class="text-right"></td> -->
<%-- 											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti0" /></td> --%>
<!-- 											<td class="text-right"></td> -->
<%-- 											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti1" /></td> --%>
<!-- 											<td class="text-right"></td> -->
<%-- 											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti2" /></td> --%>
<!-- 										</tr> -->
<%-- 										<s:if test="hasMassimoImpegnabile"> --%>
<!-- 											<tr> -->
<!-- 												<th>- assegnato</th> -->
<!-- 												<td class="text-right"></td> -->
<%-- 												<td class="text-right"><s:property value="massimoImpegnabile0" /></td> --%>
<!-- 												<td class="text-right"></td> -->
<%-- 												<td class="text-right"><s:property value="massimoImpegnabile1" /></td> --%>
<!-- 												<td class="text-right"></td> -->
<%-- 												<td class="text-right"><s:property value="massimoImpegnabile2" /></td> --%>
<!-- 											</tr> -->
<%-- 										</s:if> --%>

<!-- 										<tr> -->
<!-- 											<th>Residuo</th> -->
<%-- 											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.stanziamentoResiduoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoResiduo" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoResiduoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoResiduo" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoResiduoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoResiduo" /></td> --%>
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 											<th>Cassa</th> -->
<%-- 											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.stanziamentoCassaIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoCassa" /></td> --%>
<%-- 											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione1.stanziamentoCassaIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoCassa" /></td> --%>
<%-- 											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione2.stanziamentoCassaIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoCassa" /></td> --%>
<!-- 										</tr> -->
<!-- 									</tbody> -->
<!-- 								</table> -->
								
								<!-- SIAC-6881 -->
								<table class="table table-bordered" id="idStanziamentiTabella">
									<thead>
										<tr class="row-slim-bottom">
											<th>&nbsp;</th>
											<th>&nbsp;</th>
											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt -1}</th>
											<th scope="col" colspan="2" class="text-center">Residui ${annoEsercizioInt + 0}</th>
											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 0}</th>
											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 1}</th>
											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 2}</th>
											<th scope="col" colspan="2" class="text-center">>${annoEsercizioInt + 2}</th>
										</tr>
										<tr class="row-slim-top">
											<th width="14%">&nbsp;</th>
											<th width="14%">&nbsp;</th>
											<th scope="col" class="text-center" width="6%">Iniziale</th>
											<th scope="col" class="text-center" width="6%">Finale</th>
											<th scope="col" class="text-center" width="6%">Iniziale</th>
											<th scope="col" class="text-center" width="6%">Finale</th>
											<th scope="col" class="text-center" width="6%">Iniziale</th>
											<th scope="col" class="text-center" width="6%">Finale</th>
											<th scope="col" class="text-center" width="6%">Iniziale</th>
											<th scope="col" class="text-center" width="6%">Finale</th>
											<th scope="col" class="text-center" width="6%">Iniziale</th>
											<th scope="col" class="text-center" width="6%">Finale</th>
											<th scope="col" class="text-center" width="6%">Iniziale</th>
											<th scope="col" class="text-center" width="6%">Finale</th>
										</tr>
									</thead>
										<tr  class="componentiRowFirst">
											<th rowspan = "2"><a id="competenzaTotale" href="#idStanziamentiTabella"  class="disabled" >Competenza</a></th>
											<td class="text-center">Stanziamento</td>
											<td class="text-right"><s:property value="importiEsercizioPrecedente.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnnoPrec.importo" /></td>
											<td class="text-right"><s:property value="importiResidui.stanziamentoIniziale" /> </td>
											<td class="text-right"><s:property value="competenzaStanziamento.dettaglioResidui.importo" /> </td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno0.importo" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno1.importo" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno2.importo" /></td>
											<td class="text-right"><s:property value="importiCapitoloSuccessivi.stanziamentoIniziale" /></td>
											<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnniSucc.importo" /></td>
										</tr>
										<tr class="componentiRowOther">
											<td class="text-center">Impegnato</td>
											<td class="text-right"></td>
											<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnnoPrec.importo" /></td>
											<td class="text-right"></td>
											<td class="text-right"><s:property value="competenzaImpegnato.dettaglioResidui.importo" /></td>
											<td class="text-right"></td>
											<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.impegnato" /></td>
											<td class="text-right"></td>
											<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.impegnato" /></td>
											<td class="text-right"></td>
											<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.impegnato" /></td>
											<td class="text-right"></td>
											<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnniSucc.importo"  /></td>
										</tr>
																				
										
										<tbody id="componentiCompetenza">

											<s:iterator value="importiComponentiCapitolo" var="cc" status="componentiCapitolo">
												
												<s:if test="#cc.tipoComponenteImportiCapitolo.descrizione=='Stanziamento'">
													<tr  class="componentiCompetenzaRow" >
												</s:if>
												<s:else>
													<tr  class="componentiRowOther" >
												</s:else>												
													
													<td id="description-component" class="componenti-competenza" rowspan="1">
														<s:property value="#cc.tipoComponenteImportiCapitolo.descrizione"/>
													</td> 
														
													<td class="text-center" id="type-component">
														<s:property value="#cc.tipoDettaglioComponenteImportiCapitolo.descrizione"/>
													</td>
													<td class="text-right"></td>
													 <td class="text-right"> 
														<s:property value="#cc.dettaglioAnnoPrec.importo"/>
													</td>
													<td class="text-right"></td>
													<td class="text-right">
														<s:property value="#cc.dettaglioResidui.importo"/>
													</td>
													<td class="text-right"></td>
													<td class="text-right">
														<s:property value="#cc.dettaglioAnno0.importo"/>
													</td>
													<td class="text-right"></td>
													<td class="text-right">
														<s:property value="#cc.dettaglioAnno1.importo"/>
													</td>
													<td class="text-right"></td>
													<td class="text-right">
														<s:property value="#cc.dettaglioAnno2.importo"/>
													</td>
													<td class="text-right"></td>
													<td class="text-right">
														<s:property value="#cc.dettaglioAnniSucc.importo"/>
													</td>
												</tr>
											</s:iterator>
										</tbody>
										
										
										
<!-- 										<tr> -->
<!-- 											<th>- al netto impegni reimp. e da anni preced.</th> -->
<!-- 											<td class="text-right"></td> -->
<%-- 											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti0" /></td> --%>
<!-- 											<td class="text-right"></td> -->
<%-- 											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti1" /></td> --%>
<!-- 											<td class="text-right"></td> -->
<%-- 											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti2" /></td> --%>
<!-- 										</tr> -->
<%-- 										<s:if test="hasMassimoImpegnabile"> --%>
<!-- 											<tr> -->
<!-- 												<th>- assegnato</th> -->
<!-- 												<td class="text-right"></td> -->
<%-- 												<td class="text-right"><s:property value="massimoImpegnabile0" /></td> --%>
<!-- 												<td class="text-right"></td> -->
<%-- 												<td class="text-right"><s:property value="massimoImpegnabile1" /></td> --%>
<!-- 												<td class="text-right"></td> -->
<%-- 												<td class="text-right"><s:property value="massimoImpegnabile2" /></td> --%>
<!-- 											</tr> -->
<%-- 										</s:if> --%>

										<tr class="componentiRowFirst">
											<th rowspan="2">Residuo</th>
											<td class="text-center">Presunti</td>
											<td class="text-right"><s:property value="importiEsercizioPrecedente.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="residuiPresunti.dettaglioAnnoPrec.importo" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="residuiPresunti.dettaglioResidui.importo" /></td>
											<td class="text-right" scope="row"><s:property value="importiResidui.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="residuiPresunti.dettaglioAnno0.importo" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="residuiPresunti.dettaglioAnno1.importo" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="residuiPresunti.dettaglioAnno2.importo" /></td>
											<td class="text-right"><s:property value="importiCapitoloSuccessivi.stanziamentoResiduoIniziale" /></td>
											<td class="text-right"><s:property value="residuiPresunti.dettaglioAnniSucc.importo" /></td>
										</tr>
										<tr class="componentiRowOther">
											<td class="text-center">Effettivi</td>
											<td class="text-right" scope="row"><s:property value="importiEsercizioPrecedente.residuoEffettivoIniziale" /></td>
											<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnnoPrec.importo"  /></td>
											<td class="text-right"><s:property value="importiResidui.residuoEffettivoIniziale" /></td>
											<td class="text-right"><s:property value="residuiEffettivi.dettaglioResidui.importo" /></td>
											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.residuoEffettivoIniziale" /></td>
											<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnno0.importo" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.residuoEffettivoIniziale" /></td>
											<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnno1.importo" /></td>
											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.residuoEffettivoIniziale" /></td>
											<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnno2.importo" /></td>
											<td class="text-right"><s:property value="importiCapitoloSuccessivi.residuoEffettivoIniziale" /></td>
											<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnniSucc.importo" /></td>
										</tr>
										<tr class="componentiRowFirst">
											<th rowspan="2">Cassa</th>
											<td class="text-center">Stanziamento</td>
											<td class="text-right" scope="row"><s:property value="importiEsercizioPrecedente.stanziamentoCassaIniziale" /></td>
											<td class="text-right"><s:property value="cassaStanziato.dettaglioAnnoPrec.importo"  /></td>
											<td class="text-right"><s:property value="importiResidui.stanziamentoCassaIniziale" /></td>
											<td class="text-right"><s:property value="cassaStanziato.dettaglioResidui.importo"  /></td>
											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.stanziamentoCassaIniziale" /></td>
											<td class="text-right"><s:property value="cassaStanziato.dettaglioAnno0.importo" /></td>
											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione1.stanziamentoCassaIniziale" /></td>
											<td class="text-right"><s:property value="cassaStanziato.dettaglioAnno1.importo" /></td>
											<td class="text-right" scope="row"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
										<tr class="componentiRowOther">
											<td class="text-center">Pagato</td>
											<td class="text-right" scope="row"><s:property value="importiEsercizioPrecedente.pagatoCassaIniziale" /></td>
											<td class="text-right"><s:property value="cassaPagato.dettaglioAnnoPrec.importo" /></td>
											<td class="text-right"><s:property value="importiResidui.pagatoCassaIniziale" /></td>
											<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneResiduo.pagato" /></td>
											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.pagatoCassaIniziale" /></td>
											<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.pagato" /></td>
											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione1.pagatoCassaIniziale" /></td>
											<td class="text-right">
												<s:if test="%{disponibilitaCapitoloUscitaGestioneAnno1.pagato==null}">
													0,00
												</s:if>
												<s:else>
													<s:property value="disponibilitaCapitoloUscitaGestioneAnno1.pagato" />
												</s:else>
											</td>
											<td class="text-right" scope="row"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
											<td class="text-right"></td>
										</tr>
								</table>

								<hr>

								<div class="accordion" id="accordion2" style="margin-top:20px; margin-bottom:20px;">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a class="accordion-toggle collapsed" data-toggle="collapse" id="ModalAltriDati" data-parent="#accordion2" href="#collapseOne">
												Altri dati<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div id="collapseOne" class="accordion-body collapse">
											<div class="accordion-inner">
												<ul class="htmlelt">
													<li>
														<dfn>Ex Anno / Capitolo / Articolo<s:if test="gestioneUEB"> / <abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr></s:if></dfn>
														<dl>
															<s:if test="%{capitolo.exCapitolo != null && capitolo.exArticolo != null}">
																<s:property value="capitolo.exAnnoCapitolo"/> / <s:property value="capitolo.exCapitolo"/> / <s:property value="capitolo.exArticolo"/> <s:if test="gestioneUEB"> / <s:property value="capitolo.exUEB"/></s:if>
															</s:if>
														</dl>
													</li>
													<li>
														<dfn>Tipo Finanziamento</dfn>
														<dl><s:property value="tipoFinanziamento.codice"/> <s:property value="tipoFinanziamento.descrizione"/></dl>
													</li>
													<li>
														<dfn>Rilevante IVA</dfn>
														<dl><s:if test="capitolo.flagRilevanteIva">S&iacute;</s:if><s:else>No</s:else></dl>
													</li>
													<li>
														<dfn>Funzioni delegate regione</dfn>
														<dl><s:if test="capitolo.funzDelegateRegione">S&iacute;</s:if><s:else>No</s:else></dl>
													</li>
													<li>
														<dfn>Tipo fondo</dfn>
														<dl><s:property value="tipoFondo.codice"/> <s:property value="tipoFondo.descrizione"/></dl>
													</li>
													<li>
														<dfn>Ricorrente</dfn>
														<dl><s:property value="ricorrenteSpesa.descrizione"/></dl>
													</li>
													<li>
														<dfn>Codifica identificativo del perimetro sanitario</dfn>
														<dl><s:property value="perimetroSanitarioSpesa.codice"/> <s:property value="perimetroSanitarioSpesa.descrizione"/></dl>
													</li>
													<li>
														<dfn>Codifica transazione UE</dfn>
														<dl><s:property value="transazioneUnioneEuropeaSpesa.codice"/> <s:property value="transazioneUnioneEuropeaSpesa.descrizione"/></dl>
													</li>
													<li>
														<dfn>Codifica politiche regionali unitarie</dfn>
														<dl><s:property value="politicheRegionaliUnitarie.codice"/> <s:property value="politicheRegionaliUnitarie.descrizione"/></dl>
													</li>
													<s:iterator var="idx" begin="1" end="%{numeroClassificatoriGenerici}">
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
									</div>	
								</div>
								
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
	<script type="text/javascript" src="${jspath}capitolo/consultaCapitolo.js"></script>
	<script type="text/javascript" src="${jspath}capitoloUscitaGestione/consulta.js"></script>
</body>
</html>