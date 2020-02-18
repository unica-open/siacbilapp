<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<div id="associaCapitoliAllaVariazione">
	<s:if test ="%{richiediConfermaQuadratura}">
		<span id = "richiediConferma" class ="hide" ></span>
	</s:if>	
	<dl class="dl-horizontal">
		<dt>Num. variazione</dt>
		<dd>&nbsp;<s:property value="numeroVariazione" /></dd>
		
		<!--SIAC 6884 -->
		<s:hidden name="uidVariazione" id="HIDDEN_uidVariazione" /> <!--uidVariazione per portarla sul javascript-->
		
		<dt>Stato</dt>
		<dd>&nbsp;<s:property value="elementoStatoOperativoVariazione.descrizione" /></dd>
		<dt>Applicazione</dt>
		<dd>&nbsp;<s:property value="applicazione" /></dd>
		<dt>Tipo Variazione</dt>
		<dd>&nbsp;<s:property value="tipoVariazione.codice" />&nbsp;-&nbsp;<s:property value="tipoVariazione.descrizione" /></dd>
		
		<!-- SIAC-6884 -->
			<!--proponente della variazione per portarla sul javascript-->
		
		<s:if test="%{model.isDecentrata}">
			<dt>Data Apertura Proposta</dt>
			<dd>&nbsp;<s:property value="dataAperturaFormatted" /></dd>
			<dt>Data Chiusura Proposta</dt>
			<dd>&nbsp;<s:property value="dataChiusuraFormatted" /></dd>
			<dt>Direzione Proponente</dt>
			<dd>&nbsp;<s:property value="direzioneProponente.codice" />&nbsp;-&nbsp;<s:property value="direzioneProponente.descrizione" /></dd>
			<s:hidden name="direzioneProponente.codice" id="HIDDEN_direzioneProponente"/>
		</s:if>
	</dl>
	<h5>Elenco modifiche in variazione <i class="icon-spin icon-refresh spinner activated" id="SPINNER_leggiCapitoli"></i></h5>    
	<table class="table table-condensed table-hover table-bordered" id="tabellaGestioneVariazioni" summary="....">
		<thead>
			<tr>
				<th scope="col">Capitolo</th>
				<th scope="col" class="text-center">Competenza ${annoEsercizioInt + 0}</th>
				<th scope="col" class="text-center">Residuo ${annoEsercizioInt + 0}</th>
				<th scope="col" class="text-center">Cassa ${annoEsercizioInt + 0}</th>
				<th scope="col" class="text-center">Competenza ${annoEsercizioInt + 1}</th>
				<th scope="col" class="text-center">Competenza ${annoEsercizioInt + 2}</th>
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
				<td><span id="totaleEntrateCompetenzaVariazioneAnnoPiuUno"></span></td>
				<td><span id="totaleEntrateCompetenzaVariazioneAnnoPiuDue"></span></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr class="info">
				<th>Totale spese</th>
				<td><span id="totaleSpeseCompetenzaVariazione"></span></td>
				<td><span id="totaleSpeseResiduoVariazione"></span></td>
				<td><span id="totaleSpeseCassaVariazione"></span></td>
				<td><span id="totaleSpeseCompetenzaVariazioneAnnoPiuUno"></span></td>
				<td><span id="totaleSpeseCompetenzaVariazioneAnnoPiuDue"></span></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr class="info">
				<th>Differenza</th>
				<td><span id="differenzaCompetenzaVariazione"></span></td>
				<td><span id="differenzaResiduoVariazione"></span></td>
				<td><span id="differenzaCassaVariazione"></span></td>
				<td><span id="differenzaCompetenzaVariazioneAnnoPiuUno"></span></td>
				<td><span id="differenzaCompetenzaVariazioneAnnoPiuDue"></span></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</tfoot>
	</table>
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
	
	<s:include value="/jsp/provvedimento/ricercaProvvedimentoCollapse.jsp"/>
	<!-- PROVVEDIMENTO OPZIONALE -->
	<s:include value ="/jsp/provvedimento/ricercaProvvedimentoAggiuntivoCollapse.jsp"/>
	<div>
		<div>
			<div>
				<p>&nbsp;</p>
				<p>
					<a class="btn" href="#collapse_ricerca" data-toggle="collapse" id="pulsanteApriRicercaCapitolo">ricerca capitolo</a>
				</p>
				<div id="collapse_ricerca" class="collapse">
					<h4>&nbsp;Ricerca capitolo</h4>
					<fieldset class="form-horizontal">
						<div class="control-group">
							<span class="control-label">Capitolo</span>
							<div class="controls">
								<label class="radio inline">
									<input type="radio" name="specificaImporti.tipoCapitolo" id="tipoCapitoloRadio1" value="Entrata" <s:if test='%{specificaImporti.tipoCapitolo eq "Entrata"}'>checked="checked"</s:if>>&nbsp;Entrata
								</label>
								<label class="radio inline">
									<input type="radio" name="specificaImporti.tipoCapitolo" id="tipoCapitoloRadio2" value="Uscita" <s:if test='%{specificaImporti.tipoCapitolo eq "Uscita"}'>checked="checked"</s:if>>&nbsp;Spesa
								</label>
								<s:hidden name="applicazione" id="HIDDEN_tipoApplicazione" />
								<s:hidden name="annoEsercizioInt" id="HIDDEN_annoEsercizio" />
								<s:hidden name="annoCompetenza" id="HIDDEN_annoVariazione" />
								<!--SIAC 6884 -->
								<s:hidden name="decentrato" id="HIDDEN_decentrato" /> <!--decentrato per portarlo sul javascript-->
								<s:hidden name="regionePiemonte"  id="HIDDEN_regionePiemonte"/> <!--regione piemnonte per portarlo sul javascript-->
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="annoCapitolo">Anno</label>
							<div class="controls">
								<s:textfield id="annoCapitolo" cssClass="lbTextSmall span2 soloNumeri" value="%{annoEsercizioInt}" disabled="true" />
								<s:hidden name="specificaImporti.annoCapitolo" />
								<span class="al">
									<label class="radio inline" for="numeroCapitolo">Capitolo *</label>
								</span>
								<s:textfield id="numeroCapitolo" cssClass="lbTextSmall span2 soloNumeri" name="specificaImporti.numeroCapitolo" required="true" maxlength="9" />
								<span class="al">
									<label class="radio inline" for="numeroArticolo">Articolo *</label>
								</span>
								<s:textfield id="numeroArticolo" cssClass="lbTextSmall span2 soloNumeri" name="specificaImporti.numeroArticolo" required="true" maxlength="9" />
								<span class="al">
									<label class="radio inline" for="statoCapitolo">Stato *</label>
								</span>
								<s:select list="specificaImporti.statiOperativiElementoDiBilancio" id="statoCapitolo" name="specificaImporti.statoOperativoElementoDiBilancio" cssClass="span3" />
								<button type = "button" class="btn btn-primary" type="button" id="pulsanteRicercaCapitolo">
									<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_CapitoloSorgente"></i>
								</button>
							</div>
						</div>
						<button type="button" class="btn" id="redirezioneNuovoCapitoloButton">nuovo capitolo</button>
					</fieldset>
					<div id="divRicercaCapitolo" class="hide">
						<h5>Risultati della ricerca</h5>
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
							<div id="containerTabellaStanziamentiTotali">
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
							<div id="containerTabellaStanziamentiTotaliPerComponetnti">
							
							</div>
							
						</div>
						<input type="hidden" id= "HIDDEN_uidCapitolo" value =""/>
						<input type="hidden" id= "HIDDEN_statoOperativoElementoDiBilancio" value ="" name ="specificaImporti.elementoCapitoloVariazione.statoOperativoElementoDiBilancio"/>
						<input class="btn" type="button" value="annulla capitolo" id="annullaCapitoloButton" /><%-- Gestione annullamento capitolo --%>
						
						<s:include value="/jsp/variazione/include/modificaImportiCapitoloNonAssociatoVariazione.jsp" />
					</div>
				</div>
			</div>										
		</div>
	</div>

	<s:include value="/jsp/variazione/include/modaleModificaImportiCapitoloInVariazione.jsp" />
	
	<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
		<div class="overlay-modale">
			<div class="modal-body">
				<div class="alert alert-error alert-persistent">
					<p>
						<strong>Attenzione!!!</strong>
					</p>
					<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
				<button type = "button" class="btn btn-primary" id="EDIT_elimina">s&iacute;, prosegui</button>
			</div>
		</div>
	</div>

	<div id="msgAnnulla" class="modal hide fade " tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
		<div class="overlay-modale">
			<div class="modal-body">
				<div class="alert alert-error alert-persistent">
					<p>
						<strong>Attenzione!!!</strong>
					</p>
					<p>Stai per annullare la variazione: sei sicuro di voler proseguire?</p>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
				<button type = "button" class="btn btn-primary" id="EDIT_annulla">s&iacute;, prosegui</button>
			</div>
		</div>
	</div>
	<s:hidden id="idAzioneReportVariazioni" name="idAzioneReportVariazioni"/>
	<div id="iframeContainer"></div>
	
	<s:include value="/jsp/include/modaleStampaVariazioni.jsp" />
</div>
							