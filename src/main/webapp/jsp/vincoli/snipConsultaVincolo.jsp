<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<s:include value="/jsp/include/messaggi.jsp" />
<fieldset class="form-horizontal">
	<dl class="dl-horizontal">
		<dt>Descrizione</dt>
		<dd><s:property value="vincolo.descrizione"/>&nbsp;</dd>
		<dt>Trasferimenti vincolati</dt>
		<dd>
			<s:if test="%{vincolo.flagTrasferimentiVincolati}">
				S&iacute;
			</s:if><s:else>
				No
			</s:else>
		</dd>
		<dt>Note</dt>
		<dd><s:property value="vincolo.note"/>&nbsp;</dd>
		<dt>Stato</dt>
		<dd><s:property value="stato"/>&nbsp;</dd> 
		<dt>Bilancio</dt>
		<dd><s:property value="tipoBilancio"/>&nbsp;</dd>
		<dt>Tipo</dt>
		<dd><s:property value="genereVincolo"/>&nbsp;</dd>
	</dl>
</fieldset>

<h4>Capitoli di entrata</h4>
<table class="table table-hover dataTable" id="tabellaCapitoliEntrata_collapse<s:property value="%{vincolo.uid}"/>" summary="...." >
	<thead>
		<tr>
			<th scope="col">Capitolo</th>
			<th scope="col">Classificazione</th>
			<th scope="col">Stanz. comp. <s:property value="%{annoCompetenzaInt}"/></th>
			<th scope="col">Stanz. comp. <s:property value="%{annoCompetenzaInt + 1}"/></th>
			<th scope="col">Stanz. comp. <s:property value="%{annoCompetenzaInt + 2}"/></th>
			<th scope="col">Strutt. Amm. Resp.</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="listaCapitoliEntrata" var="entry">
			<tr>
				<td>
					<a href="#" data-trigger="hover" rel="popover"  data-content="<s:property value="%{#entry.descrizione}"/>" data-original-title="Descrizione">
						<s:property value="%{#entry.capitolo}"/>
					</a>
				</td>
				<td><s:property value="%{#entry.classificazione}"/></td>
				<td><s:property value="%{#entry.competenzaAnno0}"/></td>
				<td><s:property value="%{#entry.competenzaAnno1}"/></td>
				<td><s:property value="%{#entry.competenzaAnno2}"/></td>
				<td><s:property value="%{#entry.strutturaAmministrativoContabile}"/></td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<th scope="col">Totale</th>
			<th scope="col">&nbsp;</th>
			<th scope="col"><s:property value="totaleStanziamentoEntrata0"/></th>
			<th scope="col"><s:property value="totaleStanziamentoEntrata1"/></th>
			<th scope="col"><s:property value="totaleStanziamentoEntrata2"/></th>
			<th scope="col">&nbsp;</th>
		</tr>
	</tfoot>
</table>

<h4>Capitoli di spesa</h4>
<table class="table table-hover dataTable" id="tabellaCapitoliSpesa_collapse<s:property value="%{vincolo.uid}"/>" summary="...." >
	<thead>
		<tr>
			<th scope="col">Capitolo</th>
			<th scope="col">Classificazione</th>
			<th scope="col">Stanz. comp. <s:property value="%{annoCompetenzaInt}"/></th>
			<th scope="col">Stanz. comp. <s:property value="%{annoCompetenzaInt + 1}"/></th>
			<th scope="col">Stanz. comp. <s:property value="%{annoCompetenzaInt + 2}"/></th>
			<th scope="col">Strutt. Amm. Resp.</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="listaCapitoliUscita" var="entry">
			<tr>
				<td>
					<a href="#" data-trigger="hover" rel="popover"  data-content="<s:property value="%{#entry.descrizione}"/>" data-original-title="Descrizione">
						<s:property value="%{#entry.capitolo}"/>
					</a>
				</td>
				<td><s:property value="%{#entry.classificazione}"/></td>
				<td><s:property value="%{#entry.competenzaAnno0}"/></td>
				<td><s:property value="%{#entry.competenzaAnno1}"/></td>
				<td><s:property value="%{#entry.competenzaAnno2}"/></td>
				<td><s:property value="%{#entry.strutturaAmministrativoContabile}"/></td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<th scope="col">Totale</th>
			<th scope="col">&nbsp;</th>
			<th scope="col"><s:property value="totaleStanziamentoUscita0"/></th>
			<th scope="col"><s:property value="totaleStanziamentoUscita1"/></th>
			<th scope="col"><s:property value="totaleStanziamentoUscita2"/></th>
			<th scope="col">&nbsp;</th>
		</tr>
	</tfoot>
</table>