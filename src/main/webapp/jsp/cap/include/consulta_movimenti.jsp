<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<h4>Variazioni</h4>
<table class="table table-hover table-bordered" id="tabellaMovimenti">
	<tr>
		<th>&nbsp;</th>
		<th>&nbsp;</th>
		<th scope="col" abbr="anno" colspan="4" class="text-center">${annoEsercizioInt}</th>
		<th scope="col" abbr="anno1" colspan="4" class="text-center">${annoEsercizioInt + 1}</th>
		<th scope="col" abbr="anno2" colspan="4" class="text-center">${annoEsercizioInt + 2}</th>
	</tr>
	<tr>
		<th>&nbsp;</th>
		<th>&nbsp;</th>
		<th scope="col" class="text-center"><abbr title="numero">Num</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Competenza</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Residuo</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Cassa</abbr></th>
		<th scope="col" class="text-center"><abbr title="numero">Num</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Competenza</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Residuo</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Cassa</abbr></th>
		<th scope="col" class="text-center"><abbr title="numero">Num</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Competenza</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Residuo</abbr></th>
		<th scope="col" class="text-center"><abbr title="importo">Cassa</abbr></th>
	</tr>
	<tr>
		<th rowspan="5" scope="rowgroup">
			<a id="ancoraVariazioniAumento" title="visualizza il dettaglio delle variazioni" href="dettaglioVariazioniCapitolo.do?capitolo.uid=<s:property value="uidCapitolo" />&variazioneInAumento=true">Variazioni in aumento</a>
		</th>
		<th scope="rowgroup">Bozza</th>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaNum0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaCompetenza0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaResiduo0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaCassa0"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaNum1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaCompetenza1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaResiduo1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaCassa1"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaNum2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaCompetenza2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaResiduo2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoBozzaCassa2"/></td>
	</tr>
	<tr>
		<th scope="rowgroup"><s:property value="descrizioneGiuntaConsiglio"/></th>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioNum0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioCompetenza0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioResiduo0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioCassa0"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioNum1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioCompetenza1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioResiduo1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioCassa1"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioNum2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioCompetenza2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioResiduo2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoGiuntaConsiglioCassa2"/></td>
	</tr>
	<tr>
		<th scope="rowgroup">Pre-definitiva</th>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaNum0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaCompetenza0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaResiduo0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaCassa0"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaNum1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaCompetenza1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaResiduo1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaCassa1"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaNum2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaCompetenza2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaResiduo2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoPreDefinitivaCassa2"/></td>
	</tr>
	<tr>
		<th scope="rowgroup">Definitive</th>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaNum0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaCompetenza0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaResiduo0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaCassa0"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaNum1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaCompetenza1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaResiduo1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaCassa1"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaNum2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaCompetenza2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaResiduo2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoDefinitivaCassa2"/></td>
	</tr>
	<tr>
		<th scope="rowgroup">Totali</th>
		
		<td class="text-right" scope="row" id="variazioniInAumentoTotaleNum0"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleNum0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleCompetenza0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleResiduo0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleCassa0"/></td>
		
		<td class="text-right" scope="row" id="variazioniInAumentoTotaleNum1"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleNum1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleCompetenza1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleResiduo1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleCassa1"/></td>
		
		<td class="text-right" scope="row" id="variazioniInAumentoTotaleNum2"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleNum2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleCompetenza2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleResiduo2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInAumentoTotaleCassa2"/></td>
	</tr>
	
	<tr>
		<th rowspan="5" scope="rowgroup">
			<a id="ancoraVariazioniDiminuzione" title="visualizza il dettaglio delle variazioni" href="dettaglioVariazioniCapitolo.do?capitolo.uid=<s:property value="uidCapitolo" />&variazioneInAumento=false">Variazioni in diminuzione</a>
		</th>
		<th scope="rowgroup">Bozza</th>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaNum0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaCompetenza0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaResiduo0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaCassa0"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaNum1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaCompetenza1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaResiduo1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaCassa1"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaNum2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaCompetenza2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaResiduo2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneBozzaCassa2"/></td>
	</tr>
	<tr>
		<th scope="rowgroup"><s:property value="descrizioneGiuntaConsiglio"/></th>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioNum0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioCompetenza0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioResiduo0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioCassa0"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioNum1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioCompetenza1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioResiduo1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioCassa1"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioNum2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioCompetenza2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioResiduo2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneGiuntaConsiglioCassa2"/></td>
	</tr>
	<tr>
		<th scope="rowgroup">Pre-definitiva</th>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaNum0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaCompetenza0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaResiduo0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaCassa0"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaNum1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaCompetenza1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaResiduo1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaCassa1"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaNum2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaCompetenza2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaResiduo2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzionePreDefinitivaCassa2"/></td>
	</tr>
	<tr>
		<th scope="rowgroup">Definitive</th>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaNum0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaCompetenza0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaResiduo0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaCassa0"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaNum1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaCompetenza1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaResiduo1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaCassa1"/></td>
		
		<td class="text-right" scope="row"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaNum2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaCompetenza2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaResiduo2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneDefinitivaCassa2"/></td>
	</tr>
	<tr>
		<th scope="rowgroup">Totali</th>
		
		<td class="text-right" scope="row" id="variazioniInDiminuzioneTotaleNum0"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleNum0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleCompetenza0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleResiduo0"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleCassa0"/></td>
		
		<td class="text-right" scope="row" id="variazioniInDiminuzioneTotaleNum1"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleNum1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleCompetenza1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleResiduo1"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleCassa1"/></td>
		
		<td class="text-right" scope="row" id="variazioniInDiminuzioneTotaleNum2"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleNum2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleCompetenza2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleResiduo2"/></td>
		<td class="text-right"><s:property value="elementoVariazioneConsultazione.variazioniInDiminuzioneTotaleCassa2"/></td>
	</tr>
</table>