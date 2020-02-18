<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>


<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h4>Dettaglio registrazione</h4>
</div>

<div class="modal-body">
<div class="boxOrSpan2">
	<ul class="htmlelt">
		<li><dfn>Registrazione</dfn>
			<dl>
				<b>Numero</b>
				<span id="progressivoIVA">&nbsp;</span>
				<span> - </span>
				<b>Anno</b>
				<span id="annoEsercizio">&nbsp;</span>
			</dl></li>
		<li><dfn>Tipo registrazione</dfn>
			<dl><span id="tipoRegistrazioneIva">&nbsp;</span>
			</dl></li>
		<li><dfn>Tipo registro iva</dfn>
			<dl><span id="tipoRegistroIva">&nbsp;</span>
			</dl></li>
		<li><dfn>Attivit&agrave;</dfn>
			<dl><span id="attivitaIva">&nbsp;</span>
			</dl></li>
		<li>
			<dfn>Rilevante IRAP</dfn>
			<dl><span id="flagRilevanteIRAP">&nbsp;</span>
			</dl>
		</li>
		<li><dfn>Registro</dfn>
			<dl><span id="registroIva">&nbsp;</span>
			</dl></li>
		<li><dfn>Numero protocollo definitivo</dfn>
			<dl><span id="numeroProtocolloDefinitivo">&nbsp;</span>
			</dl></li>
		<li><dfn>Data protocollo definitivo</dfn>
			<dl><span id="dataProtocolloDefinitivo">&nbsp;</span>
			</dl></li>
		<li>
			<dfn>Descrizione</dfn>
			<dl><span id="descrizioneIva">&nbsp;</span></dl>
		</li>
	</ul>
</div>
<h4 class="step-pane">Movimenti iva</h4>
<table id="tabellaModaleMovimentiIva" class="table table-hover tab_left">
	<thead>
		<tr>
			<th>Aliquota iva</th>
			<th>%</th>
			<th class="tab_Right">Imponibile</th>
			<th class="tab_Right">Imposta</th>
			<th class="tab_Right">Importo detraibile</th>
			<th class="tab_Right">Importo indetraibile</th>
			<th class="tab_Right">Totale</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
	<tfoot>
		<tr>
			<th colspan="2">Totali</th>
			<th class="tab_Right"><span id="totaleImponibileMovimentiIva"></span></th>
			<th class="tab_Right"><span id="totaleImpostaMovimentiIva"></span></th>
			<th class="tab_Right">&nbsp;</th>
			<th class="tab_Right">&nbsp;</th>
			<th class="tab_Right"><span id="totaleTotaleMovimentiIva"></span></th>
		</tr>
	</tfoot>
</table>
</div>

	

