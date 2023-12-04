<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<h4>Stanziamenti</h4>
<%--
<table class="table table-hover table-bordered">
	<thead>
		<tr class="row-slim-bottom">
			<th>&nbsp;</th>
			<th scope="col" colspan="2" class="text-center">${annoEsercizioInt - 1}</th>
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
			<th scope="col" class="text-center">Iniziale</th>
			<th scope="col" class="text-center">Finale</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<th>Competenza</th>
			<td class="text-right" scope="row"><s:property value="importiEx.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="importiEx.stanziamento" /></td>
			<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione0.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione0.stanziamento" /></td>
			<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione1.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione1.stanziamento" /></td>
			<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione2.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione2.stanziamento" /></td>
		</tr>
		<tr>
			<th>Residuo</th>
			<td class="text-right" scope="row"><s:property value="importiEx.stanziamentoResiduoIniziale" /></td>
			<td class="text-right"><s:property value="importiEx.stanziamentoResiduo" /></td>
			<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione0.stanziamentoResiduoIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione0.stanziamentoResiduo" /></td>
			<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione1.stanziamentoResiduoIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione1.stanziamentoResiduo" /></td>
			<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione2.stanziamentoResiduoIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione2.stanziamentoResiduo" /></td>
		</tr>
		<tr>
			<th>Cassa</th>
			<td class="text-right" scope="row"><s:property value="importiEx.stanziamentoCassaIniziale" /></td>
			<td class="text-right"><s:property value="importiEx.stanziamentoCassa" /></td>
			<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione0.stanziamentoCassaIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione0.stanziamentoCassa" /></td>
			<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione1.stanziamentoCassaIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione1.stanziamentoCassa" /></td>
			<td class="text-right" scope="row"><s:property value="importiCapitoloEntrataPrevisione2.stanziamentoCassaIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloEntrataPrevisione2.stanziamentoCassa" /></td>
		</tr>
	</tbody>
</table> --%>
<%-- SIAC-8036 si torna alla situazione pre SIAC-6881  --%>
<table class="table table-hover table-bordered">
	<thead>
		<tr>
			<th> </th>
			<th class="text-center" colspan="2" scope="col">${annoEsercizioInt - 1}</th>
		</tr>
		<tr>
			<th> </th>
			<th class="text-center" scope="col">Iniziale</th>
			<th class="text-center" scope="col">Finale</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<th>Competenza</th>
			<td><s:property value="capitolo.importiCapitoloEquivalente.stanziamentoIniziale" /></td>
			<td><s:property value="capitolo.importiCapitoloEquivalente.stanziamento" /></td>
		</tr>
			<tr>
			<th>Residuo </th>
			<td><s:property value="capitolo.importiCapitoloEquivalente.stanziamentoResiduoIniziale" /></td>
			<td><s:property value="capitolo.importiCapitoloEquivalente.stanziamentoResiduo" /></td>
		</tr>
		<tr>
			<th>Cassa </th>
			<td><s:property value="capitolo.importiCapitoloEquivalente.stanziamentoCassaIniziale" /></td>
			<td><s:property value="capitolo.importiCapitoloEquivalente.stanziamentoCassa" /></td>
		</tr>
	</tbody>
</table>
<%-- SIAC-8036 si torna alla situazione pre SIAC-6881  --%>
<h4>Variazioni</h4>
<s:hidden name="capitolo.uidCapitoloEquivalente" id="HIDDEN_UID_EQUIVALENTE" />
<table class="table table-hover table-bordered" id="tabellaMovimentiGestione" data-overlay>
	<thead>
		<tr>
			<th> </th>
			<th> </th>
			<th class="text-center" colspan="4" abbr="anno" scope="col">${annoEsercizioInt + 0}</th>
		</tr>
		<tr>
			<th> </th>
			<th> </th>
			<th class="text-center" scope="col">
				<abbr title="numero">Num</abbr>
			</th>
			<th class="text-center" scope="col">
				<abbr title="importo">Competenza</abbr>
			</th>
			<th class="text-center" scope="col">
				<abbr title="importo">Residuo</abbr>
			</th>
			<th class="text-center" scope="col">
				<abbr title="importo">Cassa</abbr>
			</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<th scope="rowgroup" rowspan="3">Variazioni in aumento</th>
			<th scope="rowgroup">Definitive</th>
			<td scope="row" data-property="variazioniInAumentoDefinitivaNum0" data-numero></td>
			<td data-property="variazioniInAumentoDefinitiveCompetenza0" data-importo></td>
			<td data-property="variazioniInAumentoDefinitiveResiduo0" data-importo></td>
			<td data-property="variazioniInAumentoDefinitiveCassa0" data-importo></td>
		</tr>
		<tr>
			<th scope="rowgroup">Giunta o Consiglio</th>
			<td scope="row" data-property="variazioniInAumentoGiuntaConsiglioNum0" data-numero></td>
			<td data-property="variazioniInAumentoGiuntaConsiglioCompetenza0" data-importo></td>
			<td data-property="variazioniInAumentoGiuntaConsiglioResiduo0" data-importo></td>
			<td data-property="variazioniInAumentoGiuntaConsiglioCassa0" data-importo></td>
		</tr>
		<tr>
			<th scope="rowgroup">Totali</th>
			<td scope="row" data-property="variazioniInAumentoTotaleNum0" data-numero></td>
			<td data-property="variazioniInAumentoTotaleCompetenza0" data-importo></td>
			<td data-property="variazioniInAumentoTotaleResiduo0" data-importo></td>
			<td data-property="variazioniInAumentoTotaleCassa0" data-importo></td>
		</tr>
		<tr>
			<th scope="rowgroup" rowspan="3">Variazioni in diminuzione</th>
			<th scope="rowgroup">Definitive</th>
			<td scope="row" data-property="variazioniInDiminuzioneDefinitivaNum0" data-numero></td>
			<td data-property="variazioniInDiminuzioneDefinitiveCompetenza0" data-importo></td>
			<td data-property="variazioniInDiminuzioneDefinitiveResiduo0" data-importo></td>
			<td data-property="variazioniInDiminuzioneDefinitiveCassa0" data-importo></td>
		</tr>
		<tr>
			<th scope="rowgroup">Giunta o Consiglio</th>
			<td scope="row" data-property="variazioniInDiminuzioneGiuntaConsiglioNum0" data-numero></td>
			<td data-property="variazioniInDiminuzioneGiuntaConsiglioCompetenza0" data-importo></td>
			<td data-property="variazioniInDiminuzioneGiuntaConsiglioResiduo0" data-importo></td>
			<td data-property="variazioniInDiminuzioneGiuntaConsiglioCassa0" data-importo></td>
		</tr>
		<tr>
			<th scope="rowgroup">Totali</th>
			<td scope="row" data-property="variazioniInDiminuzioneTotaleNum0" data-numero></td>
			<td data-property="variazioniInDiminuzioneTotaleCompetenza0" data-importo></td>
			<td data-property="variazioniInDiminuzioneTotaleResiduo0" data-importo></td>
			<td data-property="variazioniInDiminuzioneTotaleCassa0" data-importo></td>
		</tr>
	</tbody>
</table>