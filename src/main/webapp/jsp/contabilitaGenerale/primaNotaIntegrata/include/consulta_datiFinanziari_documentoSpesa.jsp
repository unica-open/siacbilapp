<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion-inner">
	<br/>
  	<ul class="htmlelt">
		<li>
			<dfn>Intestazione documento</dfn>
			<dl><s:property value="datiAccessoriiMovimentoFinanziario"/>&nbsp;</dl>
		</li>
	</ul>
	
	<table class="table table-hover tab_left" id="tabellaDatiFinanziariMovGest">
		<thead>
			<tr>
				<th>Quota</th>
				<th>Importo</th>
				<th><abbr title="Numero">N.</abbr> impegno</th>
				<th><abbr title="Piano dei conti">Pdc</abbr> finanziario</th>
				<th><abbr title="Numero">N.</abbr> liquidazione</th>
				<th><abbr title="Numero">N.</abbr> ordinativo</th>
			</tr>
		</thead>
		<tbody>			
		</tbody>
	</table>
</div>