<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="si" %>
<h4 class="step-pane"><s:property value="consultazioneHelper.datiCreazioneModifica" /></h4>
<div class="boxOrSpan2">
	<div class="boxOrInLeft-multi">
		<div class="boxOrInLeft-multi">
			<p>Dati Ordinativo</p>
			<ul class="htmlelt">
				<li>
					<dfn>Descrizione</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.descrizione"/></dl>
				</li>
				<li>
					<dfn>Importo</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.importoOrdinativo"/></dl>
				</li>
				<li>
					<dfn>Distinta</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.distinta.descrizione"/></dl>
				</li>
				<li>
					<dfn>Conto Tesoriere</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.contoTesoreria.descrizione"/></dl>
				</li>
				<li>
					<dfn>A copertura</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.flagCopertura"/></dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight-multi">
			<p>Dati Capitolo</p>
			<ul class="htmlelt">
				<li>
					<dfn>Capitolo</dfn>
					<dl><s:property value="consultazioneHelper.datiBaseCapitolo"/></dl>
				</li>

				<li>
					<dfn>Struttura amministrativa</dfn>
					<dl><s:property value="consultazioneHelper.datiBaseStrutturaAmministrativoContabileCapitolo" /></dl>
				</li>
				<li>
					<dfn>Tipo finanziamento</dfn>
					<dl><s:property value="consultazioneHelper.datiBaseTipoFinanziamentoCapitolo"/></dl>
				</li>
			</ul>
		</div>
	</div>

	<div class="boxOrInRight-multi">
		<div class="boxOrInLeft-multi">
			<p>Dati Soggetto</p>
			<ul class="htmlelt">
				<li>
					<dfn>Codice</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.soggetto.codiceSoggetto"/></dl>
				</li>
				<li>
					<dfn>Denominazione</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.soggetto.denominazione"/></dl>
				</li>
				<li>
					<dfn>Codice Fiscale</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.soggetto.codiceFiscale"/></dl>
				</li>
				<li>
					<dfn>Partita IVA</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.soggetto.partitaIva"/></dl>
				</li>
			</ul>
		</div>

		<div class="boxOrInRight-multi">
			<p>Dati Provvedimento</p>
			<ul class="htmlelt">
				<li>
					<dfn>Provvedimento</dfn>
					<dl><s:property value="consultazioneHelper.datiAttoAmministrativo" /></dl>
				</li>
				<li>
					<dfn>Tipo</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.attoAmministrativo.tipoAtto.descrizione"/></dl>
				</li>
				<li>
					<dfn>Oggetto</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.attoAmministrativo.oggetto"/></dl>
				</li>
				<li>
					<dfn>Struttura amministrativa</dfn>
					<dl><s:property value="consultazioneHelper.datiStrutturaAmministrativoContabile" /></dl>
				</li>
				<li>
					<dfn>Stato</dfn>
					<dl><s:property value="consultazioneHelper.ordinativo.attoAmministrativo.statoOperativo"/></dl>
				</li>
			</ul>
		</div>
	</div>
</div>


<h4 class="step-pane">Dati Quote</h4>
<table id="tabellaSubordinativi" class="table table-hover tab_left">
	<thead>
		<tr>
			<th>Numero</th>
			<th>Accertamento</th>
			<th>Documento</th>
			<th>Descrizione</th>
			<th>Data incasso</th>
			<th class="tab_Right">Importo</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="consultazioneHelper.listaSubOrdinativo" var="so">
			<tr>
				<td><s:property value="#so.numero"/></td>
				<td>
					<s:if test="%{#so.accertamento != null}">
						<s:property value="#so.accertamento.annoMovimento"/> / <si:plainstringproperty value="#so.accertamento.numero"/>
						<s:if test="%{#so.accertamento.elencoSubAccertamenti != null}">
							- <si:plainstringproperty value="#so.accertamento.elencoSubAccertamenti.get(0).numero"/>
						</s:if>
					</s:if>
				</td>
				<td>
					<s:if test="%{#so.subDocumentoEntrata != null && #so.subDocumentoEntrata.documento != null}">
						<s:property value="#so.subDocumentoEntrata.documento.anno"/> / <s:property value="#so.subDocumentoEntrata.documento.numero"/>
					</s:if>
				</td>
				<td><s:property value="#so.descrizione"/></td>
				<td><s:property value="#so.dataEsecuzionePagamento"/></td>
				<td class="tab_Right"><s:property value="#so.importoAttuale"/></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
