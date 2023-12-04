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
			<div class="span12 contentPage" id="accordion2">
				<s:include value="/jsp/include/messaggi.jsp" />
 				<h3>Aggiorna Variazione</h3>
				<s:form cssClass="form-horizontal" novalidate="novalidate" id="aggiornamentoVariazioneCodifiche" method="post">
					<input type="hidden" id="tipoAzione" value="Aggiornamento"/>
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
								</dl>
								<h5>Elenco modifiche in variazione</h5>    
								<table class="table table-condensed table-hover table-bordered" id="codificheNellaVariazione" summary="....">
									<thead>
										<tr>
											<th scope="col">Capitolo</th>
											<th scope="col">Descrizione capitolo</th>
											<th scope="col">Descrizione articolo</th>
											<th scope="col">Struttura amministrativa</th>
											<th scope="col">&nbsp;</th>
											<th scope="col">&nbsp;</th>										
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
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
								
								<p>&nbsp;</p>
								<p>
									<a class="btn" href="#collapse_ricerca" data-toggle="collapse" id="pulsanteRicercaCapitolo">ricerca capitolo</a>
								</p>
							</div>
						</div>
					</div>
					
					<div class="collapse" id="collapse_ricerca">
						<fieldset class="form-horizontal">
							<div class="control-group">
								<span class="control-label">Capitolo</span>
								<div class="controls">
									<label class="radio inline">
										<input type="radio" name="specificaCodifiche.tipoCapitolo" id="tipoCapitoloRadioEntrata" value="Entrata" <s:if test='%{specificaCodifiche.tipoCapitolo eq "Entrata"}'>checked="checked"</s:if>>&nbsp;Entrata
									</label>
									<label class="radio inline">
										<input type="radio" name="specificaCodifiche.tipoCapitolo" id="tipoCapitoloRadioUscita" value="Uscita" <s:if test='%{specificaCodifiche.tipoCapitolo eq "Uscita"}'>checked="checked"</s:if>>&nbsp;Spesa
									</label>
									<s:hidden name="applicazione" id="HIDDEN_tipoApplicazione" />
									<s:hidden name="annoEsercizioInt" id="HIDDEN_annoEsercizio" />
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="annoCapitolo">Anno</label>
								<div class="controls">
									<s:textfield id="annoCapitolo" cssClass="lbTextSmall span2" value="%{annoEsercizioInt}" disabled="true" />
									<s:hidden name="specificaCodifiche.annoCapitolo" />
									<span class="al">
										<label class="radio inline" for="numeroCapitolo">Capitolo *</label>
									</span>
									<s:textfield id="numeroCapitolo" cssClass="lbTextSmall span2" name="specificaCodifiche.numeroCapitolo" required="true" maxlength="9" />
									<span class="al">
										<label class="radio inline" for="numeroArticolo">Articolo *</label>
									</span>
									<s:textfield id="numeroArticolo" cssClass="lbTextSmall span2" name="specificaCodifiche.numeroArticolo" required="true" maxlength="9" />
									<a class="btn btn-primary" href="#" id="BUTTON_ricercaCapitolo">
										<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_CapitoloSorgente"></i>
									</a>
								</div>
							</div>
						</fieldset>
								
						<div id="divRicercaCapitolo" class="hide">
							<h4>Risultati della ricerca</h4>
							<div class="box-border" id="risultatiRicercaCapitolo">
								<dl class="dl-horizontal-inline">
									<dt>Anno:</dt>
									<dd><span id="annoCapitoloTrovato"></span>&nbsp;</dd>
									<dt>Capitolo / Articolo:</dt>
									<dd><span id="numeroCapitoloArticoloTrovato"></span>&nbsp;</dd>
								</dl>
								<div class="pull-centered">
									<a class="btn btn-collapse pull-center" data-toggle="collapse" data-parent="#risultatiRicercaCapitolo" href="#collapseGestioneCodifiche">
										<i class="icon-plus-sign icon-2x"></i>&nbsp;Gestione codifiche
									</a>
								</div>
							</div>
						</div>
					</div>
					<p class="margin-large">
						<s:include value="/jsp/include/indietro.jsp"/>&nbsp;
						<span class="nascosto"> | </span>
						<s:reset cssClass="btn btn-link" value="annulla" />&nbsp;
						<span class="nascosto"> | </span>
						<a class="btn" id="pulsanteSalvaAggiornamentoVariazione" href="#">salva</a>&nbsp;
						<span class="nascosto"> | </span>
						<a href='#msgAnnulla' title='annulla variazione' role='button' class="btn" data-toggle='modal'>annulla variazione</a>
						<span class="nascosto"> | </span>
						<a data-parametri-stampa="Numero Variazione:<s:property value="numeroVariazione" />;Anno Variazione:<s:property value="annoCompetenza" />;Anno Competenza:<s:property value="annoCompetenza" />" href='#modaleStampa' title='stampa' role='button' class="btn" data-toggle='modal'>stampa</a>
						<span class="nascosto"> | </span>
						<a class="btn btn-primary pull-right" id="pulsanteConcludiAggiornamentoVariazione" href="#">concludi attivit&agrave;</a>&nbsp;
					</p>
					
					
						<s:hidden id="idAzioneReportVariazioni" name="idAzioneReportVariazioni"/>
					
				</s:form>
				<div id="collapseGestioneCodifiche" class="collapse-body collapse">
					<div class="collapse-inner">
						<fieldset id="fieldsetCapitoloUscita" class="form-horizontal hide">
							<s:hidden name="uid" id="HIDDEN_uidElementoCapitoloCodificheUscita" />
							<div id="descrizioneCapitoloCapitoloUscitaDiv" class="control-group">
								<label for="descrizioneCapitoloCapitoloUscita" class="control-label">Descrizione *</label>
								<div class="controls">
									<textarea rows="5" cols="15" id="descrizioneCapitoloCapitoloUscita" class="span10" name="descrizioneCapitolo"></textarea>
								</div>
							</div>
							<div id="descrizioneArticoloCapitoloUscitaDiv" class="control-group">
								<label for="descrizioneArticoloCapitoloUscita" class="control-label">Descrizione Articolo</label>
								<div class="controls">
									<textarea rows="5" cols="15" id="descrizioneArticoloCapitoloUscita" class="span10" name="descrizioneArticolo"></textarea>
								</div>
							</div>
							<%-- <div id="missioneCapitoloUscitaDiv" class="control-group">
								<label for="missioneCapitoloUscita" class="control-label">Missione *</label>
								<div class="controls">
									<select id="missioneCapitoloUscita" class="span10" name="missione.uid"></select>
								</div>
							</div>
							<div id="programmaCapitoloUscitaDiv" class="control-group">
								<label for="programmaCapitoloUscita" class="control-label">
									Programma *
									<a class="tooltip-test" title="selezionare prima la Missione" href="#"><i class="icon-info-sign"></i></a>
								</label>
								<div class="controls">
									<select id="programmaCapitoloUscita" class="span10" name="programma.uid"></select>
								</div>
							</div>
							<div id="classificazioneCofogCapitoloUscitaDiv" class="control-group">
								<label for="classificazioneCofogCapitoloUscita" class="control-label">
									Cofog *
									<a class="tooltip-test" title="selezionare prima il Programma" href="#"><i class="icon-info-sign"></i></a>
								</label>
								<div class="controls">
									<select id="classificazioneCofogCapitoloUscita" class="span10" name="classificazioneCofog.uid"></select>
								</div>
							</div>
							<div id="titoloSpesaCapitoloUscitaDiv" class="control-group">
								<label for="titoloSpesaCapitoloUscita" class="control-label">Titolo *</label>
								<div class="controls">
									<select id="titoloSpesaCapitoloUscita" class="span10" name="titoloSpesa.uid"></select>
								</div>
							</div>
							<div id="macroaggregatoCapitoloUscitaDiv" class="control-group">
								<label for="macroaggregatoCapitoloUscita" class="control-label">
									Macroaggregato *
									<a class="tooltip-test" title="selezionare prima il Titolo" href="#"><i class="icon-info-sign"></i></a>
								</label>
								<div class="controls">
									<select id="macroaggregatoCapitoloUscita" class="span10" name="macroaggregato.uid"></select>
								</div>
							</div>
							<div id="elementoPianoDeiContiCapitoloUscitaDiv" class="control-group">
								<label for="elementoPianoDeiContiCapitoloUscita" class="control-label">
									<abbr title="Piano dei Conti">P.d.C.</abbr> finanziario
									<a class="tooltip-test" title="selezionare prima il Macroaggregato" href="#"><i class="icon-info-sign"></i></a>
								</label>
								<div class="controls">
									<input type="hidden" id="elementoPianoDeiContiCapitoloUscita" name="elementoPianoDeiConti.uid" />
									<a href="#" role="button" class="btn" data-toggle="modal" id="elementoPianoDeiContiCapitoloUscitaPulsante">
										Seleziona il Piano dei Conti&nbsp;
										<i class="icon-spin icon-refresh spinner" id="elementoPianoDeiContiCapitoloUscitaSpinner"></i>
									</a>
									
									<div id="elementoPianoDeiContiCapitoloUscitaModale" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="elementoPianoDeiContiCapitoloUscitaModalLabel" aria-hidden="true">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
											<h3 id="elementoPianoDeiContiCapitoloUscitaModalLabel">Piano dei Conti</h3>
										</div>
										<div class="modal-body">
											<ul id="elementoPianoDeiContiCapitoloUscitaTree" class="ztree"></ul>
										</div>
										<div class="modal-footer">
											<button id="elementoPianoDeiContiCapitoloUscitaDeseleziona" class="btn">Deseleziona</button>
										</div>
									</div>
									&nbsp;
									<span id="elementoPianoDeiContiCapitoloUscitaSpan">
											Nessun P.d.C. finanziario selezionato
									</span>
								</div>
							</div>
							
							<div id="siopeSpesaCapitoloUscitaDiv" class="control-group">
								<label for="bottoneSIOPE" class="control-label"> <abbr
									title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr>
									
								</label>
								<div class="controls">
									<s:textfield id="siopeSpesa" data-ricerca-url="ricercaClassificatoreGerarchicoByCodice_siopeSpesa.do" name="siopeSpesa.codice"
										cssClass="span3" />
									&nbsp;<span id="descrizioneSiopeSpesa"><s:property
											value="siopeSpesa.descrizione" /></span>
									<s:hidden id="HIDDEN_idSiopeSpesa" name="siopeSpesa.uid" />
									<s:hidden id="HIDDEN_descrizioneSiopeSpesa"
										name="siopeSpesa.descrizione" />
									<span class="radio guidata">
										<button type="button" class="btn btn-primary"
											id="compilazioneGuidataSIOPESpesa">compilazione guidata</button>
									</span>
								</div>
							</div> --%>
							
							<%--input type="hidden" id="siopeSpesaCapitoloUscita" name="siopeSpesa.uid" /--%>
							
	
							<%--div id="siopeSpesaCapitoloUscitaDiv" class="control-group">
								<label for="siopeSpesaCapitoloUscita" class="control-label">
									<abbr title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr>
									<a class="tooltip-test" title="selezionare prima il P.d.C." href="#"><i class="icon-info-sign"></i></a>
								</label>
								<div class="controls">
									<input type="hidden" id="siopeSpesaCapitoloUscita" name="siopeSpesa.uid" />
									<a href="#" role="button" class="btn" data-toggle="modal" id="siopeSpesaCapitoloUscitaPulsante">
										Seleziona il codice SIOPE&nbsp;
										<i class="icon-spin icon-refresh spinner" id="siopeSpesaCapitoloUscitaSpinner"></i>
									</a>
									<div id="siopeSpesaCapitoloUscitaModale" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="siopeSpesaCapitoloUscitaModalLabel" aria-hidden="true">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
											<h3 id="siopeSpesaCapitoloUscitaModalLabel">SIOPE</h3>
										</div>
										<div class="modal-body">
											<ul id="siopeSpesaCapitoloUscitaTree" class="ztree"></ul>
										</div>
										<div class="modal-footer">
											<button id="siopeSpesaCapitoloUscitaDeseleziona" class="btn">Deseleziona</button>
										</div>
									</div>
									&nbsp;
									<span id="siopeSpesaCapitoloUscitaSpan">
											Nessun SIOPE selezionato
									</span>
								</div>
							</div--%>
							<div id="strutturaAmministrativoContabileCapitoloUscitaDiv" class="control-group">
								<label for="strutturaAmministrativoContabileCapitoloUscita" class="control-label">
									Struttura Amministrativa Responsabile
								</label>
								<div class="controls">
									<input type="hidden" id="strutturaAmministrativoContabileCapitoloUscita" name="strutturaAmministrativoContabile.uid" />
									<a href="#" role="button" class="btn" data-toggle="modal" id="strutturaAmministrativoContabileCapitoloUscitaPulsante">
										Seleziona la Struttura Amministrativa&nbsp;
										<i class="icon-spin icon-refresh spinner" id="strutturaAmministrativoContabileCapitoloUscitaSpinner"></i>
									</a>
									<div id="strutturaAmministrativoContabileCapitoloUscitaModale" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="strutturaAmministrativoContabileCapitoloUscitaModalLabel" aria-hidden="true">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
											<h3 id="strutturaAmministrativoContabileCapitoloUscitaModalLabel">Struttura Amministrativa Responsabile</h3>
										</div>
										<div class="modal-body">
											<ul id="strutturaAmministrativoContabileCapitoloUscitaTree" class="ztree"></ul>
										</div>
										<div class="modal-footer">
											<button id="strutturaAmministrativoContabileCapitoloUscitaDeseleziona" class="btn">Deseleziona</button>
											<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
										</div>
									</div>
									&nbsp;
									<span id="strutturaAmministrativoContabileCapitoloUscitaSpan">
											Nessuna Struttura Amministrativa Responsabile selezionata
									</span>
								</div>
							</div>
							<%-- <div id="categoriaCapitoloCapitoloUscitaDiv" class="control-group">
								<label for="categoriaCapitoloCapitoloUscita" class="control-label">Tipo Capitolo</label>
								<div class="controls">
									<select id="categoriaCapitoloCapitoloUscita" class="span10" name="categoriaCapitolo.uid"></select>
								</div>
							</div>
							<div id="impegnabileCapitoloUscitaDiv" class="control-group">
								<label for="impegnabileCapitoloUscita" class="control-label">Impegnabile</label>
								<div class="controls">
									<input id="impegnabileCapitoloUscita" type="checkbox" value="true" name="impegnabile"/>
								</div>
							</div>
							
							<div class="fieldset">
								<div>
									<h4>Altri dati</h4>
								</div>
								<div>
									<div id="corsivoPerMemoriaCapitoloUscitaDiv" class="control-group">
										<label for="corsivoPerMemoriaCapitoloUscita" class="control-label">Corsivo per memoria</label>
										<div class="controls">
											<input id="corsivoPerMemoriaCapitoloUscita" type="checkbox" value="true" name="flagPerMemoria"/>
										</div>
									</div>
									<div id="tipoFinanziamentoCapitoloUscitaDiv" class="control-group">
										<label for="tipoFinanziamentoCapitoloUscita" class="control-label">Tipo Finanziamento</label>
										<div class="controls">
											<select id="tipoFinanziamentoCapitoloUscita" class="span10" name="tipoFinanziamento.uid"></select>
										</div>
									</div>
									<div id="rilevanteIvaCapitoloUscitaDiv" class="control-group">
										<label for="rilevanteIvaCapitoloUscita" class="control-label">Rilevante IVA</label>
										<div class="controls">
											<input id="rilevanteIvaCapitoloUscita" type="checkbox" value="true" name="flagRilevanteIva"/>
										</div>
									</div>
									<div id="funzioniDelegateRegioneCapitoloUscitaDiv" class="control-group">
										<label for="funzioniDelegateRegioneCapitoloUscita" class="control-label">Funzioni delegate regione</label>
										<div class="controls">
											<input id="funzioniDelegateRegioneCapitoloUscita" type="checkbox" value="true" name="funzDelegateRegione"/>
										</div>
									</div>
									<div id="tipoFondoCapitoloUscitaDiv" class="control-group">
										<label for="tipoFondoCapitoloUscita" class="control-label">Tipo Fondo</label>
										<div class="controls">
											<select id="tipoFondoCapitoloUscita" class="span10" name="tipoFondo.uid"></select>
										</div>
									</div>
									<div id="ricorrenteSpesaCapitoloUscitaDiv" class="control-group">
										<span class="control-label">Spesa</span>
										<div class="controls">
											<label class="radio inline">
												<input type="radio" name="ricorrenteSpesa.uid" value="" checked="checked">&nbsp;Non si applica
											</label>&nbsp;
											<span id="ricorrenteSpesaCapitoloUscita"></span>
										</div>
									</div>
									<div id="perimetroSanitarioSpesaCapitoloUscitaDiv" class="control-group">
										<label for="perimetroSanitarioSpesaCapitoloUscita" class="control-label">Codifica identificativo del perimetro sanitario</label>
										<div class="controls">
											<select id="perimetroSanitarioSpesaCapitoloUscita" class="span10" name="perimetroSanitarioSpesa.uid"></select>
										</div>
									</div>
									<div id="transazioneUnioneEuropeaSpesaCapitoloUscitaDiv" class="control-group">
										<label for="transazioneUnioneEuropeaSpesaCapitoloUscita" class="control-label">Codifica transazione UE</label>
										<div class="controls">
											<select id="transazioneUnioneEuropeaSpesaCapitoloUscita" class="span10" name="transazioneUnioneEuropeaSpesa.uid"></select>
										</div>
									</div>
									<div id="politicheRegionaliUnitarieCapitoloUscitaDiv" class="control-group">
										<label for="politicheRegionaliUnitarieCapitoloUscita" class="control-label">Codifica politiche regionali unitarie</label>
										<div class="controls">
											<select id="politicheRegionaliUnitarieCapitoloUscita" class="span10" name="politicheRegionaliUnitarie.uid"></select>
										</div>
									</div>
									<s:iterator var="idx" begin="1" end="%{specificaCodifiche.numeroClassificatoriGenericiSpesa}">
										<div id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloUscitaDiv" class="control-group">
											<label for="classificatoreGenerico<s:property value="%{#idx}" />CapitoloUscita" class="control-label" id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloUscitaLabel"></label>
											<div class="controls">
												<select id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloUscita" class="span10" name="classificatoreGenerico<s:property value="%{#idx}" />.uid"></select>
											</div>
										</div>
									</s:iterator>
									<div id="noteCapitoloUscitaDiv" class="control-group">
										<label for="noteCapitoloUscita" class="control-label">Note</label>
										<div class="controls">
											<textarea rows="5" cols="15" id="noteCapitoloUscita" name="note" class="span10"></textarea>
										</div>
									</div>
								</div>
							</div> --%>
						</fieldset>
								
						<fieldset id="fieldsetCapitoloEntrata" class="form-horizontal hide">
							<s:hidden name="uid" id="HIDDEN_uidElementoCapitoloCodificheEntrata" />
							<div id="descrizioneCapitoloCapitoloEntrataDiv" class="control-group">
								<label for="descrizioneCapitoloCapitoloEntrata" class="control-label">Descrizione *</label>
								<div class="controls">
									<textarea rows="5" cols="15" id="descrizioneCapitoloCapitoloEntrata" class="span10" name="descrizioneCapitolo"></textarea>
								</div>
							</div>
							<div id="descrizioneArticoloCapitoloEntrataDiv" class="control-group">
								<label for="descrizioneArticoloCapitoloEntrata" class="control-label">Descrizione Articolo</label>
								<div class="controls">
									<textarea rows="5" cols="15" id="descrizioneArticoloCapitoloEntrata" class="span10" name="descrizioneArticolo"></textarea>
								</div>
							</div>
							<%--<div id="titoloEntrataCapitoloEntrataDiv" class="control-group">
								<label for="titoloEntrataCapitoloEntrata" class="control-label">Titolo *</label>
								<div class="controls">
									<select id="titoloEntrataCapitoloEntrata" class="span10" name="titoloEntrata.uid"></select>
									<input type="hidden" id="titoloEntrataCapitoloEntrataCodice" name="titoloEntrata.codice" />
								</div>
							</div>
							<div id="tipologiaTitoloCapitoloEntrataDiv" class="control-group">
								<label for="tipologiaTitoloCapitoloEntrata" class="control-label">
									Tipologia *
									<a class="tooltip-test" title="selezionare prima il Titolo" href="#"><i class="icon-info-sign"></i></a>
								</label>
								<div class="controls">
									<select id="tipologiaTitoloCapitoloEntrata" class="span10" name="tipologiaTitolo.uid"></select>
								</div>
							</div>
							<div id="categoriaTipologiaTitoloCapitoloEntrataDiv" class="control-group">
								<label for="categoriaTipologiaTitoloCapitoloEntrata" class="control-label">
									Categoria *
									<a class="tooltip-test" title="selezionare prima la Tipologia" href="#"><i class="icon-info-sign"></i></a>
								</label>
								<div class="controls">
									<select id="categoriaTipologiaTitoloCapitoloEntrata" class="span10" name="categoriaTipologiaTitolo.uid"></select>
								</div>
							</div>
							<div id="elementoPianoDeiContiCapitoloEntrataDiv" class="control-group">
								<label for="elementoPianoDeiContiCapitoloEntrata" class="control-label">
									<abbr title="Piano dei Conti">P.d.C.</abbr> finanziario
									<a class="tooltip-test" title="selezionare prima la Categoria" href="#"><i class="icon-info-sign"></i></a>
								</label>
								<div class="controls">
									<input type="hidden" id="elementoPianoDeiContiCapitoloEntrata" name="elementoPianoDeiConti.uid" />
									<a href="#" role="button" class="btn" data-toggle="modal" id="elementoPianoDeiContiCapitoloEntrataPulsante">
										Seleziona il Piano dei Conti&nbsp;
										<i class="icon-spin icon-refresh spinner" id="elementoPianoDeiContiCapitoloEntrataSpinner"></i>
									</a>
									
									<div id="elementoPianoDeiContiCapitoloEntrataModale" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="elementoPianoDeiContiCapitoloEntrataModalLabel" aria-hidden="true">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
											<h3 id="elementoPianoDeiContiCapitoloEntrataModalLabel">Piano dei Conti</h3>
										</div>
										<div class="modal-body">
											<ul id="elementoPianoDeiContiCapitoloEntrataTree" class="ztree"></ul>
										</div>
										<div class="modal-footer">
											<button id="elementoPianoDeiContiCapitoloEntrataDeseleziona" class="btn">Deseleziona</button>
										</div>
									</div>
									&nbsp;
									<span id="elementoPianoDeiContiCapitoloEntrataSpan">
											Nessun P.d.C. finanziario selezionato
									</span>
								</div>
							</div>
							
							<div id="siopeEntrataCapitoloUscitaDiv" class="control-group">
								<label for="bottoneSIOPE" class="control-label"> <abbr
									title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr>
								</label>
								<div class="controls">
									<s:textfield id="siopeEntrata" data-ricerca-url="ricercaClassificatoreGerarchicoByCodice_siopeEntrata.do" name="siopeEntrata.codice"
										cssClass="span3" />
									&nbsp;<span id="descrizioneSiopeEntrata"><s:property
											value="siopeEntrata.descrizione" /></span>
									<s:hidden id="HIDDEN_idSiopeEntrata" name="siopeEntrata.uid" />
									<s:hidden id="HIDDEN_descrizioneSiopeEntrata"
										name="siopeEntrata.descrizione" />
									<span class="radio guidata">
										<button type="button" class="btn btn-primary"
											id="compilazioneGuidataSIOPEEntrata">compilazione guidata</button>
									</span>
								</div>
							</div> --%>
							
							<%--input type="hidden" id="siopeEntrataCapitoloEntrata" name="siopeEntrata.uid" /--%>
							<%--div id="siopeEntrataEntrataDiv" class="control-group">
								<label for="siopeEntrataCapitoloEntrata" class="control-label">
									<abbr title="Sistema Informativo sulle OPerazioni degli Enti pubblici">SIOPE</abbr>
									<a class="tooltip-test" title="selezionare prima il P.d.C." href="#"><i class="icon-info-sign"></i></a>
								</label>
								<div class="controls">
									<input type="hidden" id="siopeEntrataCapitoloEntrata" name="siopeEntrata.uid" />
									<a href="#" role="button" class="btn" data-toggle="modal" id="siopeEntrataCapitoloEntrataPulsante">
										Seleziona il codice SIOPE&nbsp;
										<i class="icon-spin icon-refresh spinner" id="siopeEntrataCapitoloEntrataSpinner"></i>
									</a>
									<div id="siopeEntrataCapitoloEntrataModale" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="siopeEntrataCapitoloEntrataModalLabel" aria-hidden="true">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
											<h3 id="siopeEntrataCapitoloEntrataModalLabel">SIOPE</h3>
										</div>
										<div class="modal-body">
											<ul id="siopeEntrataCapitoloEntrataTree" class="ztree"></ul>
										</div>
										<div class="modal-footer">
											<button id="siopeEntrataCapitoloEntrataDeseleziona" class="btn">Deseleziona</button>
										</div>
									</div>
									&nbsp;
									<span id="siopeEntrataCapitoloEntrataSpan">
											Nessun SIOPE selezionato
									</span>
								</div>
							</div--%>
							<div id="strutturaAmministrativoContabileEntrataDiv" class="control-group">
								<label for="strutturaAmministrativoContabileCapitoloEntrata" class="control-label">
									Struttura Amministrativa Responsabile
								</label>
								<div class="controls">
									<input type="hidden" id="strutturaAmministrativoContabileCapitoloEntrata" name="strutturaAmministrativoContabile.uid" />
									<a href="#" role="button" class="btn" data-toggle="modal" id="strutturaAmministrativoContabileCapitoloEntrataPulsante">
										Seleziona la Struttura Amministrativa&nbsp;
										<i class="icon-spin icon-refresh spinner" id="strutturaAmministrativoContabileCapitoloEntrataSpinner"></i>
									</a>
									<div id="strutturaAmministrativoContabileCapitoloEntrataModale" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="strutturaAmministrativoContabileCapitoloEntrataModalLabel" aria-hidden="true">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
											<h3 id="strutturaAmministrativoContabileCapitoloEntrataModalLabel">Struttura Amministrativa Responsabile</h3>
										</div>
										<div class="modal-body">
											<ul id="strutturaAmministrativoContabileCapitoloEntrataTree" class="ztree"></ul>
										</div>
										<div class="modal-footer">
											<button id="strutturaAmministrativoContabileCapitoloEntrataDeseleziona" class="btn">Deseleziona</button>
											<button type="button" class="btn btn-primary pull-right" data-dismiss="modal" aria-hidden="true">Conferma</button>
										</div>
									</div>
									&nbsp;
									<span id="strutturaAmministrativoContabileCapitoloEntrataSpan">
											Nessuna Struttura Amministrativa Responsabile selezionata
									</span>
								</div>
							</div>
							<%-- <div id="categoriaCapitoloCapitoloEntrataDiv" class="control-group">
								<label for="categoriaCapitoloCapitoloEntrata" class="control-label">Tipo Capitolo</label>
								<div class="controls">
									<select id="categoriaCapitoloCapitoloEntrata" class="span10" name="categoriaCapitolo.uid"></select>
								</div>
							</div>
							<div id="impegnabileCapitoloEntrataDiv" class="control-group">
								<label for="impegnabileCapitoloEntrata" class="control-label">Accertabile</label>
								<div class="controls">
									<input id="impegnabileCapitoloEntrata" type="checkbox" value="true" name="impegnabile"/>
								</div>
							</div>
							
							<div class="fieldset">
								<div>
									<h4>Altri dati</h4>
								</div>
								<div>
									<div id="tipoFinanziamentoCapitoloEntrataDiv" class="control-group">
										<label for="tipoFinanziamentoCapitoloEntrata" class="control-label">Tipo Finanziamento</label>
										<div class="controls">
											<select id="tipoFinanziamentoCapitoloEntrata" class="span10" name="tipoFinanziamento.uid"></select>
										</div>
									</div>
									<div id="rilevanteIvaCapitoloEntrataDiv" class="control-group">
										<label for="rilevanteIvaCapitoloEntrata" class="control-label">Rilevante IVA</label>
										<div class="controls">
											<input id="rilevanteIvaCapitoloEntrata" type="checkbox" value="true" name="flagRilevanteIva"/>
										</div>
									</div>
									<div id="tipoFondoCapitoloEntrataDiv" class="control-group">
										<label for="tipoFondoCapitoloEntrata" class="control-label">Tipo Fondo</label>
										<div class="controls">
											<select id="tipoFondoCapitoloEntrata" class="span10" name="tipoFondo.uid"></select>
										</div>
									</div>
									<div id="ricorrenteEntrataCapitoloEntrataDiv" class="control-group">
										<span class="control-label">Entrata</span>
										<div class="controls">
											<label class="radio inline">
												<input type="radio" name="ricorrenteEntrata.uid" value="" checked="checked">&nbsp;Non si applica
											</label>&nbsp;
											<span id="ricorrenteEntrataCapitoloEntrata"></span>
										</div>
									</div>
									<div id="perimetroSanitarioEntrataCapitoloEntrataDiv" class="control-group">
										<label for="perimetroSanitarioEntrataCapitoloEntrata" class="control-label">Codifica identificativo del perimetro sanitario</label>
										<div class="controls">
											<select id="perimetroSanitarioEntrataCapitoloEntrata" class="span10" name="perimetroSanitarioEntrata.uid"></select>
										</div>
									</div>
									<div id="transazioneUnioneEuropeaEntrataCapitoloEntrataDiv" class="control-group">
										<label for="transazioneUnioneEuropeaEntrataCapitoloEntrata" class="control-label">Codifica transazione UE</label>
										<div class="controls">
											<select id="transazioneUnioneEuropeaEntrataCapitoloEntrata" class="span10" name="transazioneUnioneEuropeaEntrata.uid"></select>
										</div>
									</div>
									<s:iterator var="idx" begin="1" end="%{specificaCodifiche.numeroClassificatoriGenericiEntrata}">
										<div id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloEntrataDiv" class="control-group">
											<label for="classificatoreGenerico<s:property value="%{#idx}" />CapitoloEntrata" class="control-label" id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloEntrataLabel"></label>
											<div class="controls">
												<select id="classificatoreGenerico<s:property value="%{#idx}" />CapitoloEntrata" class="span10" name="classificatoreGenerico<s:property value="%{#idx}" />.uid"></select>
											</div>
										</div>
									</s:iterator>
									<div id="noteCapitoloEntrataDiv" class="control-group">
										<label for="noteCapitoloEntrata" class="control-label">Note</label>
										<div class="controls">
											<textarea rows="5" cols="15" id="noteCapitoloEntrata" name="note" class="span10"></textarea>
										</div>
									</div>
								</div>
							</div>--%>
							
							
						</fieldset>
								
						<a href="#" class="btn" id="bottoneInserisciModifica">
							inserisci modifica&nbsp;
							<i class="icon-spin icon-refresh spinner" id="inserisciModificaSpinner"></i>
						</a>
					</div>
				</div>
			</div>
		</div>
		
		<s:include value="/jsp/variazione/step3/include/modaleCompilazioneGuidataSIOPEVariazione_capEntrata.jsp"/>
		<s:include value="/jsp/variazione/step3/include/modaleCompilazioneGuidataSIOPEVariazione_capSpesa.jsp"/>
	</div>
	
	<%-- Modale eliminazione capitolo dalla variazione --%>
	<div aria-hidden="false" aria-labelledby="modaleEliminazioneLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleEliminazione">
		<div class="modal-body">
			<div class="alert alert-error alert-persistent">
				<button data-hide="alert" class="close" type="button">×</button>
				<p><strong>Attenzione!!!</strong></p>
				<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
			</div>
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal" class="btn" id="modaleEliminazionePulsanteCancella">no, indietro</button>
			<button class="btn btn-primary" id="modaleEliminazionePulsanteProsegui">
				si, prosegui
				<i class="icon-spin icon-refresh spinner" id="modaleEliminazioneSpinner"></i>
			</button>
		</div>
	</div>
	
	<%-- Modale annulla variazione --%>
	<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
		<div class="modal-body">
			<div class="alert alert-error">
				<p>
					<strong>Attenzione!!!</strong>
				</p>
				<p>Stai per annullare la variazione: sei sicuro di voler proseguire?</p>
			</div>
		</div>
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
			<button class="btn btn-primary" id="EDIT_annulla">s&iacute;, prosegui</button>
		</div>
	</div>
	<%-- /Modale annulla --%>

	<s:include value="/jsp/include/modaleStampaVariazioni.jsp" />
	
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ztree.js"></script>
	<s:include value="/jsp/include/footer.jsp"/>
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_collapse.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/variazioni.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/classificatori.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/ricercaSIOPE.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/aggiorna.codifiche.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/stampaVariazioni.js"></script>
	
</body>
</html>