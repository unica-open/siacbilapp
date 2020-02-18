<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="divTabellaCespitiCollegatiRegistroAInner" >
	<table class="dataTable-inner table-hover" style="width:100%">
		  <thead> 
				<tr>
					<th><abbr title="Numero">N.</abbr></th>
					<th>Cespite</th>
					<th>Tipo bene</th>
					<th>Descrizione</th>
					<th>Data entrata</th>
					<th class="tab_Right">Valore bene</th>
					<th class="tab_Right">Valore da prima nota</th>
					<th>&nbsp;</th>
					<th>&nbsp;</th>
				</tr>
		  </thead> 
		<tbody></tbody>
	</table>
	<div class="Border_line"></div>
		<button type="button" class="btn btn-secondary chiudiCespite">chiudi</button>
		<s:if test="abilitaRicercaCespite">
			<button id="cercaCespite" type="button" class="btn btn-primary pull-right cercaCespite">cerca scheda cespite</button>
		</s:if>
		<s:if test="abilitaInserimentoCespite">
			<button type="button" class="btn btn-primary pull-right inserisciCespite"> inserisci cespite </button>
		</s:if>
	<div class="Border_line"></div>
</div>