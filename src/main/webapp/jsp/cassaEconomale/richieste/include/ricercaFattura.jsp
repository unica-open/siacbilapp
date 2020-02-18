<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="divFattura" class="accordion">
	<div class="accordion-group">
		<div class="accordion-heading">
		<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#divFattura" data-target="#collapseFattura">
			Ricerca fattura
			<span class="infoFattura marginLeft1" id="descrizioneFatturaAssociataSpan">
				<s:if test='%{documentoSpesa != null}'>
					N. <s:property value="descrizioneDocumento" />
				</s:if>
			</span>
			<span class="icon">&nbsp;</span>
		</a>
		</div>
		<s:if test="%{!isAggiornamento || richiestaEconomalePrenotata}">
			<div id="collapseFattura" class="accordion-body collapse">
				<div class="accordion-inner">
				<fieldset id="fieldsetRicercaFatture" class="form-horizontal">
					<s:hidden id="HIDDEN_annoDocumentoSpesa" name="documentoSpesa.anno" />
					<s:hidden id="HIDDEN_numeroDocumentoSpesa" name="documentoSpesa.numero" />
					<s:hidden id="HIDDEN_tipoDocumentoUidDocumentoSpesa" name="documentoSpesa.tipoDocumento.uid" />
					<s:hidden id="HIDDEN_tipoDocumentoDescrizioneDocumentoSpesa" name="documentoSpesa.tipoDocumento.descrizione" />
					<h4>Fattura</h4>
					<div class="control-group">
						<label class="control-label" for="tipoDocumentoDocumentoSpesa">Tipo</label>
						<div class="controls">
							<s:select list="listaTipoDocumento" id="tipoDocumentoDocumentoSpesaRic" name="ric.documentoSpesa.tipoDocumento.uid" cssClass="span4"
								headerKey="" headerValue="" listKey="uid" listValue="descrizione" disabled="%{isAggiornamento && !richiestaEconomalePrenotata}" />
							<span class="al">
								<label class="radio inline">Anno</label>
							</span>
							<s:textfield id="annoDocumentoSpesaRic" name="ric.documentoSpesa.anno" cssClass="lbTextSmall span1 soloNumeri" disabled="%{isAggiornamento && !richiestaEconomalePrenotata}" />
							<span class="al">
								<label class="radio inline">Numero</label>
								<s:textfield id="numeroDocumentoSpesaRic" name="ric.documentoSpesa.numero" cssClass="lbTextSmall span1" disabled="%{isAggiornamento && !richiestaEconomalePrenotata}" />
							</span>
							<button type="button" class="btn btn-primary pull-right" id="pulsanteCompilazioneGuidataFattura">compilazione guidata</button>
						</div>
					</div>
					<fieldset class="form-horizontal">
						<h4 class="step-pane">Soggetto
							<span id="datiRiferimentoSoggettoSpan">
								<s:if test='%{soggetto != null && (soggetto.codice ne null && soggetto.codice != "") && (soggetto.descrizione ne null && soggetto.descrizione != "") && (soggetto.codiceFiscale ne null && soggetto.codiceFiscale != "")}'>
									<s:property value="%{soggetto.codice + ' - ' + soggetto.descrizione + ' - ' + soggetto.codiceFiscale}" />
								</s:if>
							</span>
						</h4>
						<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
						<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />
						<s:hidden id="HIDDEN_soggettoUid" name="ric.soggetto.uid" />
	
						<div class="control-group">
							<label class="control-label" for="codiceSoggettoSoggetto">Codice</label>
							<div class="controls">
								<s:textfield id="codiceSoggettoSoggetto" cssClass="lbTextSmall span2" name="ric.soggetto.codiceSoggetto" maxlength="20" placeholder="codice" disabled="%{isAggiornamento && !richiestaEconomalePrenotata}" />
								<span class="radio guidata">
									<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</a>
								</span>
							</div>
						</div>
					</fieldset>
				</fieldset>
				<p class="margin-medium">
					<button type="button" class="btn btn-primary pull-right" id="pulsanteCercaFattura" <s:if test='%{isAggiornamento && !richiestaEconomalePrenotata}'>disabled</s:if>>
						<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteCercaFattura"></i>
					</button>
				</p>
				</div>
			</div>
		</s:if>
	</div>

	<%-- Risultati ricerca eventuali --%>
	<s:if test="%{!isAggiornamento || richiestaEconomalePrenotata}">
		<div class="hide" id="tabellaFattureDIV">
			<h4>Elenco fatture trovate</h4>
			<table class="table table-hover tab_left" id="tabellaFatture">
				<thead>
					<tr>
						<th class="span1">&nbsp;</th>
						<th>Documento</th>
						<th>Data</th>
						<th>Stato</th>
						<th>Soggetto</th>
						<th class="tab_Right span3">Importo</th>
						<th class="tab_Right span3">Importo da pagare</th>
					</tr>
				</thead>
				<tbody></tbody>
				<tfoot>
					<tr>
						<th colspan="6">&nbsp;</th>
						<th id="totaleFatturaRichiestaEconomale" class="tab_Right">&nbsp;</th>
					</tr>
				</tfoot>
			</table>
			<div id="divRicercaDocumentoSpesaSubdocumento" class="hide">
				<h4>Elenco Subdocumenti</h4>
				<table class="table table-hover tab_left" id="tabellaRicercaDocumentoSpesaSubdocumento">
					<thead>
						<tr>
							<th class="span1">&nbsp;</th>
							<th><abbr title="Numero">N.</abbr> quota</th>
							<th>Importo quota</th>
							<th>Importo split reverse</th>
							<th>Pagato <!-- CR 2673 in cassa <abbr title="economale">econ.</abbr> --></th>
							<th>Data pagamento in <abbr title="cassa economale">cec</abbr></th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
			<p class="margin-medium"><button type="button" class="btn btn-primary pull-right" id="pulsanteAssociaFattura">associa</button></p>
			<div class="clear"></div>
		</div>
	</s:if>
</div>