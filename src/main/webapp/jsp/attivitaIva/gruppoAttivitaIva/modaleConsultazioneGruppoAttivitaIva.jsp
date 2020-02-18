<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div aria-hidden="true" aria-labelledby="labelModaleConsultazioneGruppoAttivitaIva" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConsultazioneGruppoAttivitaIva">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="labelModaleConsultazioneGruppoAttivitaIva">Consulta</h4>
	</div>
	<div class="modal-body">
		<div class="boxOrSpan2">
			<div class="boxOrInline">
				<p>Dati del gruppo</p>
				<ul class="htmlelt">
					<li>
						<dfn>Codice</dfn>
						<dl><s:property value="gruppoAttivitaIva.codice" /></dl>
					</li>
					<li>
						<dfn>Descrizione</dfn>
						<dl><s:property value="gruppoAttivitaIva.descrizione" /></dl>
					</li>
					<li>
						<dfn>Tipo attivit&agrave;</dfn>
						<dl><s:property value="gruppoAttivitaIva.tipoAttivita.codice" /> - <s:property value="gruppoAttivitaIva.tipoAttivita" /></dl>
					</li>
					<li>
						<dfn>Attivit&agrave;</dfn>
						<dl>
							<s:iterator value="gruppoAttivitaIva.listaAttivitaIva" var="ai">
								<s:property value="#ai.codice"/> - <s:property value="#ai.descrizione"/>
								<br/>
							</s:iterator>
						</dl>
					</li>
				</ul>
				<h4>Dati del gruppo per anni</h4>
				<table class="table table-hover tab_left" id="tabellaModaleConsultazioneGruppoAttivitaIva">
					<thead>
						<tr>
							<th scope="col">Anno esercizio</th>
							<th scope="col">Tipo chiusura</th>
							<th class="tab_Right" scope="col">&#37; Pro-rata</th>
							<th class="tab_Right" scope="col">Iva a credito precedente <i>(negativa)</i></th>
						</tr>
					</thead>
					<tbody>
						<s:iterator value="listaElementoAnnualizzazioneGruppoAttivitaIva" var="ann">
							<tr>
								<td><s:property value="#ann.annoEsercizio" /></td>
								<td><s:property value="#ann.tipoChiusura" /></td>
								<td class="tab_Right"><s:property value="#ann.percentualeProRata" /></td>
								<td class="tab_Right"><s:property value="#ann.ivaPrecedente" /></td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn btn-primary">chiudi</button>
	</div>
</div>
