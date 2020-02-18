<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<table class="table table-hover table-condensed table-bordered" id="tabellaStanziamentiTotaliPerComponenti">
	<tr>
		<th>&nbsp;</th>
		<th>&nbsp;</th>
		<th class="text-right">${annoEsercizioInt - 1}</th>
		<th class="text-right">Residui ${annoEsercizioInt}</th>
		<th class="text-right">${annoEsercizioInt}</th>
		<th class="text-right">${annoEsercizioInt + 1}</th>
		<th class="text-right">${annoEsercizioInt + 2}</th>
		<th class="text-right">>${annoEsercizioInt + 2}</th>
	</tr>
	
	<s:iterator value="competenzaComponenti" var="comp" status="statusComp">
		
			<tr <s:if test="%{#statusComp.first}"> class="componentiRowFirst" </s:if><s:else> class="componentiRowOther"</s:else>>
			<s:if test="%{#statusComp.first}">
				<th rowspan="3" class="stanziamenti-titoli">
					<a id="competenzaTotaleInserimento" href="#tabellaComponentiInserimento"  class="disabled" >Competenza</a>
				</th>
			</s:if>
			<td class="text-center"><s:property value="#comp.tipoDettaglioComponenteImportiCapitolo.descrizione"/></td>
			<td class="text-right"><s:property value="#comp.dettaglioAnnoPrec.importo"/></td>
			<td class="text-right"><s:property value="#comp.dettaglioResidui.importo"/></td>
			<td class="text-right"><s:property value="#comp.dettaglioAnno0.importo"/></td>
			<td class="text-right"><s:property value="#comp.dettaglioAnno1.importo"/></td>
			<td class="text-right"><s:property value="#comp.dettaglioAnno2.importo"/></td>
			<td class="text-right"><s:property value="#comp.dettaglioAnniSucc.importo"/></td>
		</tr>
	</s:iterator>
	<s:if test="%{importiComponentiCapitolo != null && importiComponentiCapitolo.size() > 0 }">
		<s:iterator value="importiComponentiCapitolo" var="compon" status="statusCompon">
			<tr class="righeComponenti hide componentiRowOther">
				<s:if test="%{#statusCompon.odd}">
					<s:set var="cnt" value="%{#statusCompon.index}" />
					<td class="componenti-competenza" id="tipoComTD0" rowspan="2">
						<s:property value="#compon.tipoComponenteImportiCapitolo.descrizione"/>
					</td>
				</s:if>
				<td class="text-center"><s:property value="#compon.tipoDettaglioComponenteImportiCapitolo.descrizione"/></td>
				<td class="text-right"><s:property value="#compon.dettaglioAnnoPrec.importo"/></td>
				<td class="text-right"><s:property value="#compon.dettaglioResidui.importo"/></td>
				<td class="text-right"><s:property value="#compon.dettaglioAnno0.importo"/></td>
				<td class="text-right"><s:property value="#compon.dettaglioAnno1.importo"/></td>
				<td class="text-right"><s:property value="#compon.dettaglioAnno2.importo"/></td>
				<td class="text-right"><s:property value="#compon.dettaglioAnniSucc.importo"/></td>
			</tr>
		</s:iterator>
	</s:if>
	
	<s:iterator value="residuoComponenti" var="res" status="statusRes">
		<tr <s:if test="%{#statusRes.first}"> class="componentiRowFirst" </s:if><s:else> class="componentiRowOther"</s:else>>
			<s:if test="%{#statusRes.first}">
				<th rowspan="2" class="stanziamenti-titoli">
					Residuo
				</th>
			</s:if>
			<td class="text-center"><s:property value="#res.tipoDettaglioComponenteImportiCapitolo.descrizione"/></td>
			<td class="text-right"><s:property value="#res.dettaglioAnnoPrec.importo"/></td>
			<td class="text-right"><s:property value="#res.dettaglioResidui.importo"/></td>
			<td class="text-right"><s:property value="#res.dettaglioAnno0.importo"/></td>
			<td class="text-right"><s:property value="#res.dettaglioAnno1.importo"/></td>
			<td class="text-right"><s:property value="#res.dettaglioAnno2.importo"/></td>
			<td class="text-right"><s:property value="#res.dettaglioAnniSucc.importo"/></td>
		</tr>
	</s:iterator>
	
	<s:iterator value="cassaComponenti" var="cassa" status="statusCassa">
		<tr <s:if test="%{#statusCassa.first}"> class="componentiRowFirst" </s:if><s:else> class="componentiRowOther"</s:else>>
			<s:if test="%{#statusCassa.first}">
				<th rowspan="2" class="stanziamenti-titoli">
					Cassa
				</th>
			</s:if>
			<td class="text-center"><s:property value="#cassa.tipoDettaglioComponenteImportiCapitolo.descrizione"/></td>
			<td class="text-right"><s:property value="#cassa.dettaglioAnnoPrec.importo"/></td>
			<td class="text-right"><s:property value="#cassa.dettaglioResidui.importo"/></td>
			<td class="text-right"><s:property value="#cassa.dettaglioAnno0.importo"/></td>
			<td class="text-right"><s:property value="#cassa.dettaglioAnno1.importo"/></td>
			<td class="text-right"><s:property value="#cassa.dettaglioAnno2.importo"/></td>
			<td class="text-right"><s:property value="#cassa.dettaglioAnniSucc.importo"/></td>
		</tr>
	</s:iterator>

</table>