<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<p>
	<a class="btn" href="#collapse_ricerca_in_elenco" data-toggle="collapse" id="pulsanteApriRicercaQuoteInelenco">filtra quote in elenco</a>
</p>
<div id="collapse_ricerca_in_elenco" class="collapse">
	<fieldset class="form-horizontal">
		<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
		<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />

		<h4 class="step-pane">Ricerca quote per soggetto
		</h4>
		<div class="control-group">
			<label class="control-label" for="codiceSoggetto">Codice </label>
			<div class="controls">
				<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
				<span class="radio guidata">
					<a href="#" id="pulsanteApriModaleSoggetto" class="btn btn-primary">compilazione guidata</a>
				</span>
			</div>
		</div>
		<div class="clear"></div>
		<p>
			<button type = "button" class="btn btn-primary  pull-left" id="pulsanteRicercaSoggettoInElenco">cerca </button>
		</p>
		<div class="clear"></div>

		<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
		<div id="dettaglioElementiCollegatiFiltrati" class="hide">
			<h4>Dettaglio elementi collegati per soggetto <span id="descrizioneSoggettoFiltrante"> </span></h4>
			<fieldset class="form-horizontal">
				<table class="table table-hover tab_left" id="tabellaDettaglioElementiCollegatiFiltrati">
					<thead>
						<tr>
							<th>Elenco</th>
							<th>Documento-Quota</th>
							<th><abbr title="Modalità pagamento soggetto">Mod.Pag.</abbr></th>
							<th>Soggetto</th>
							<th>Movimento</th>
							<th>Capitolo</th>
							<th>Provv. movimento</th>
							<th>IVA</th>
							<th>Annotazioni</th>
							<th class="tab_Right">Importo in atto</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</div>
	</fieldset>
</div>