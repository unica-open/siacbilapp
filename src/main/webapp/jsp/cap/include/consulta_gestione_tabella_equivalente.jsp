<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>


<tr>
	<th scope="rowgroup" rowspan="5">Variazioni in aumento</th>
	<th scope="rowgroup">Bozza</th>
	<td scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaNum0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaCompetenza0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaResiduo0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaCassa0"/></td>
</tr>
<tr>
	<th scope="rowgroup">Giunta o Consiglio</th>
	<td scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioNum0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioCompetenza0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioResiduo0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioCassa0"/></td>
</tr>
<tr>
	<th scope="rowgroup">Pre-definitive</th>
	<td scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaNum0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaCompetenza0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaResiduo0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaCassa0"/></td>
</tr>
<tr>
	<th scope="rowgroup">Definitive</th>
	<td scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaNum0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaCompetenza0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaResiduo0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaCassa0"/></td>
</tr>

<tr>
	<th scope="rowgroup">Totali</th>
	<td scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleNum0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleCompetenza0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleResiduo0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleCassa0"/></td>
</tr>

<tr>
	<th scope="rowgroup" rowspan="5">Variazioni in diminuzione</th>
	<th scope="rowgroup">Bozza</th>
	<td scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaNum0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaCompetenza0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaResiduo0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaCassa0"/></td>
</tr>

<tr>
	<th scope="rowgroup">Giunta o Consiglio</th>
	<td scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioNum0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioCompetenza0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioResiduo0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioCassa0"/></td>
</tr>
<tr>
	<th scope="rowgroup">Pre-definitive</th>
	<td scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaNum0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaCompetenza0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaResiduo0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaCassa0"/></td>
</tr>
<tr>
	<th scope="rowgroup">Definitive</th>
	<td scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaNum0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaCompetenza0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaResiduo0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaCassa0"/></td>
</tr>

<tr>
	<th scope="rowgroup">Totali</th>
	<td scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleNum0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleCompetenza0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleResiduo0"/></td>
	<td><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleCassa0"/></td>
</tr>


<tr><td colspan="6">
<table width="100%">
<tr>
		<th>&nbsp;</th>
		<th scope="col" class="text-center"><abbr title="numero">Bozza</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Giunta o Consiglio</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Pre-definitiva</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Definitiva</abbr></th>
		<th scope="col" class="text-center"><abbr title="numero">Totali</abbr></th>
	</tr>
	<tr>
		<th rowspan="5" scope="rowgroup">
		Variazioni a saldo zero
		</th>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInNeutreBozzaNum0"/></td>
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInNeutreGiuntaConsiglioNum0"/></td>
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInNeutrePreDefinitivaNum0"/></td>
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInNeutreDefinitivaNum0"/></td>
		<td class="text-right" scope="row" id="variazioniInNeutreTotaleNum0"><s:property value="elementoVariazioneConsultazione.variazioniInNeutreTotaleNum0"/></td>
		
	</tr>
</table>
</td>
</tr>


	







