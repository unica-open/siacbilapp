<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<h4 class="step-pane">Dati Modifica</h4>
<table id="tabellaModifiche" class="table table-hover tab_left">
	<thead>
		<tr>
			<th>Modifiche</th>
			<th>Tipo</th>
			<th>Descrizione</th>
			<th>Stato</th>
			<th>Provvedimento</th>
			<th>Modifica di soggetto</th>
			<th class="tab_Right">Modifica di importo</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="consultazioneHelper.listaModificaMovimentoGestione" var="mmg">
			<tr>
				<td><s:property value="#mmg.domStringModifiche" escapeHtml="false" /></td>
				<td><s:property value="#mmg.domStringTipo" escapeHtml="false" /></td>
				<td><s:property value="#mmg.domStringDescrizione" escapeHtml="false" /></td>
				<td><s:property value="#mmg.domStringStato" escapeHtml="false" /></td>
				<td><s:property value="#mmg.domStringProvvedimento" escapeHtml="false" /></td>
				<td><s:property value="#mmg.domStringModificaDiSoggetto" escapeHtml="false" /></td>
				<td class="tab_Right"><s:property value="#mmg.domStringModificaDiImporto" escapeHtml="false" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
<h4 class="step-pane"><s:property value="consultazioneHelper.datiCreazioneModifica" /></h4>
<div class="boxOrSpan2">
	<div class="boxOrInLeft-multi">
		<div class="boxOrInLeft-multi">
			<p>Dati Impegno</p>
			<ul class="htmlelt">
				<li>
					<dfn>Oggetto</dfn>
					<dl><s:property value="consultazioneHelper.movimentoGestione.descrizione"/></dl>
				</li>
				<li>
					<dfn>Importo attuale</dfn>
					<dl><s:property value="consultazioneHelper.movimentoGestione.importoAttuale"/></dl>
				</li>
				<li>
					<dfn>Tipo impegno</dfn>
					<dl><s:property value="consultazioneHelper.movimentoGestione.tipoImpegno.descrizione"/></dl>
				</li>
				<li>
					<dfn>Scadenza</dfn>
					<dl><s:property value="consultazioneHelper.movimentoGestione.dataScadenza"/></dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight-multi">
			<p>Dati Capitolo</p>
			<ul class="htmlelt">
				<li>
					<dfn>Capitolo</dfn>
					<dl><s:property value="consultazioneHelper.datiBaseCapitolo"/></dl>
				</li>

				<li>
					<dfn>Struttura amministrativa</dfn>
					<dl><s:property value="consultazioneHelper.datiBaseStrutturaAmministrativoContabileCapitolo" /></dl>
				</li>
				<li>
					<dfn>Tipo finanziamento</dfn>
					<dl><s:property value="consultazioneHelper.datiBaseTipoFinanziamentoCapitolo"/></dl>
				</li>
			</ul>
		</div>
	</div>

	<div class="boxOrInRight-multi">
		<div class="boxOrInLeft-multi">
			<p>Dati Soggetto</p>
			<ul class="htmlelt">
				<li>
					<dfn>Codice</dfn>
					<dl><s:property value="consultazioneHelper.movimentoGestione.soggetto.codiceSoggetto"/></dl>
				</li>
				<li>
					<dfn>Denominazione</dfn>
					<dl><s:property value="consultazioneHelper.movimentoGestione.soggetto.denominazione"/></dl>
				</li>
				<li>
					<dfn>Codice Fiscale</dfn>
					<dl><s:property value="consultazioneHelper.movimentoGestione.soggetto.codiceFiscale"/></dl>
				</li>
				<li>
					<dfn>Partita IVA</dfn>
					<dl><s:property value="consultazioneHelper.movimentoGestione.soggetto.partitaIva"/></dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight-multi">
			<p>Dati Provvedimento</p>
			<ul class="htmlelt">
				<li>
					<dfn>Provvedimento</dfn>
					<dl><s:property value="consultazioneHelper.datiAttoAmministrativo" /></dl>
				</li>
				<li>
					<dfn>Tipo</dfn>
					<dl><s:property value="consultazioneHelper.movimentoGestione.attoAmministrativo.tipoAtto.descrizione"/></dl>
				</li>
				<li>
					<dfn>Oggetto</dfn>
					<dl><s:property value="consultazioneHelper.movimentoGestione.attoAmministrativo.oggetto"/></dl>
				</li>
				<li>
					<dfn>Struttura amministrativa</dfn>
					<dl><s:property value="consultazioneHelper.datiStrutturaAmministrativoContabile" /></dl>
				</li>
				<li>
					<dfn>Stato</dfn>
					<dl><s:property value="consultazioneHelper.movimentoGestione.attoAmministrativo.statoOperativo"/></dl>
				</li>
			</ul>
		</div>
	</div>

	<div class="boxOrInRight-multi">
		<div class="boxOrInLeft-multi">
			<p>Da riaccertamento</p>
			<ul class="htmlelt">
				<li>
					<dfn>Riaccertato</dfn>
					<dl><s:property value="consultazioneHelper.daRiaccertamento" escapeHtml="false" /></dl>
				</li>
				<li>
					<dfn>Anno Riaccertato</dfn>
					<dl><s:property value="consultazioneHelper.annoRiaccertato" /></dl>
				</li>
				<li>
					<dfn>Numero Riaccertato</dfn>
					<dl><s:property value="consultazioneHelper.numeroRiaccertato"/></dl>
				</li>
				<li>
					<dfn>Impegno di origine</dfn>
					<dl><s:property value="consultazioneHelper.movimentoOrigine" /></dl>
				</li>
			</ul>
		</div>
	</div>
</div>

<div class="clear"></div>
