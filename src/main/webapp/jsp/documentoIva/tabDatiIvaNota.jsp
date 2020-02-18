<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<fieldset class="form-horizontal">
	<%--h4>
		Non lo voglio. Vedere se sia proprio necessario
		<span class="num_result">19527</span> Risultati trovati
	</h4--%>
	<table class="table table-hover tab_left" id="tabellaNoteCredito">
		<thead>
			<tr>
				<th scope="col">Documento</th>
				<th scope="col">Data</th>
				<th scope="col">Stato</th>
				<th scope="col">Soggetto</th>
				<th scope="col" class="tab_Right">Importo</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="listaNote" var="nota">
				<tr>
					<td><s:property value="%{#nota.descAnnoNumeroTipoDoc}"/></td>
					<td><s:property value="%{#nota.dataEmissione}"/></td>
					<td>
						<a data-content="<s:property value='%{#nota.statoOperativoDocumento.descrizione}'/>"
								data-original-title="Stato" href="#" data-trigger="hover" rel="popover" >
							<s:property value="%{#nota.statoOperativoDocumento.codice}"/>
						</a>
					</td>
					<td><s:property value="%{#nota.soggetto.codiceSoggetto}"/> - <s:property value="%{#nota.soggetto.denominazione}"/></td>
					<td class="tab_Right"><s:property value="%{#nota.importo}"/></td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="4">Totale</th>
				<th class="tab_Right"><s:property value="importoTotaleNote"/></th>
			</tr>
		</tfoot>
	</table>
	<br>
	<table class="table">
		<tbody>
			<tr id="txt_Left">
				<th class="borderBottomLight"><h4>Importo totale note di credito</h4></th>
				<td class="tab_Right">
					<s:textfield name="importoTotaleNote" disabled="true" cssClass="span10" id="importoTotaleNote" />
					<s:hidden name="importoTotaleNote" />
				</td>
			</tr>
			<tr>
				<s:if test="tipoSubdocumentoIvaQuota">
					<th class="borderBottomLight"><h4>Importo da dedurre sulla quota</h4></th>
				</s:if><s:else>
					<th class="borderBottomLight"><h4>Importo da dedurre su quote rilevanti iva</h4></th>
				</s:else>
				<td class="tab_Right">
					<s:textfield id="importoDaDedurre" name="importoDaDedurre" cssClass="span10 soloNumeri decimale" />
				</td>
			</tr>
		</tbody>
	</table>

	<br>
	<s:if test="flagNotaCreditoIvaPresente">
		<s:include value="/jsp/documentoIva/aggiornaNotaIva.jsp" />
	</s:if><s:else>
		<s:include value="/jsp/documentoIva/inserisciNotaIva.jsp" />
	</s:else>
	<%--
	<s:if test="flagIntracomunitarioUtilizzabile">
		<s:include value="/jsp/documentoIva/modaleIntracomunitario.jsp">
			<s:param name="docId">Documento</s:param>
			<s:param name="docName">Nota</s:param>
		</s:include>
	</s:if>
	--%>
</fieldset>