<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<fieldset class="form-horizontal">
	<h4 class="step-pane">
		Inserimento: <s:property value="causaleEP.dataCreazione"/> (<s:property value="causaleEP.loginCreazione" />)
		<s:if test="%{causaleEP.dataModificaCausaleEP != null}">
			- Ultima modifica: <s:property value="causaleEP.dataModificaCausaleEP"/> (<s:property value="causaleEP.loginModifica" />)
		</s:if>
	</h4>
	<div class="boxOrSpan2">
		<div class="boxOrInLeft">
			<p>Dati Causale</p>
			<ul class="htmlelt">
				<li>
					<dfn>Tipo</dfn>
					<dl><s:property value="causaleEP.tipoCausale.descrizione"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Codice</dfn>
					<dl><s:property value="causaleEP.codice"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Descrizione</dfn>
					<dl><s:property value="causaleEP.descrizione"/>&nbsp;</dl>
				</li>
				<s:if test="tipoCausaleDiRaccordo">
					<%--li>
						<dfn>Classe conto</dfn>
						<dl><s:property value="%{causaleEP.elementoPianoDeiConti.classePiano.codice + ' - ' + conto.pianoDeiConti.classePiano.descrizione}" />&nbsp;</dl>
					</li>
					<li>
						<dfn>Codice conto</dfn>
						<dl><s:property value="conto.codice" />&nbsp;</dl>
					</li--%>
					<li>
						<dfn>Codice conto finanziario</dfn>
						<dl><s:property value="causaleEP.elementoPianoDeiConti.codice" />&nbsp;</dl>
					</li>
					<li>
						<dfn>Causale Di Default</dfn>
						<dl><s:if test="causaleEP.causaleDiDefault">S&igrave;</s:if><s:else>No</s:else>&nbsp;</dl>
					</li>
				</s:if>
			</ul>
		</div>
		<s:if test="%{tipoCausaleDiRaccordo && causaleEP.soggetto != null && causaleEP.soggetto.uid != 0}">
			<div class="boxOrInRight">
				<p>Soggetto</p>
				<ul class="htmlelt">
					<li>
						<dfn>Codice</dfn>
						<dl><s:property value="causaleEP.soggetto.codiceSoggetto"/>&nbsp;</dl>
					</li>
					<li>
						<dfn>Codice Fiscale</dfn>
						<dl><s:property value="causaleEP.soggetto.codiceFiscale"/>&nbsp;</dl>
					</li>
					<li>
						<dfn>Partita IVA</dfn>
						<dl><s:property value="causaleEP.soggetto.partitaIva"/>&nbsp;</dl>
					</li>
					<li>
						<dfn>Denominazione</dfn>
						<dl><s:property value="causaleEP.soggetto.denominazione"/>&nbsp;</dl>
					</li>
					<%--li>
						<dfn>Sedi secondarie</dfn>
						<dl>xxxxxxxxxxxxxx</dl>
					</li--%>
				</ul>
			</div>
		</s:if>
	</div>
	<s:if test="%{tipoCausaleDiRaccordo && causaleEP.soggetto != null && causaleEP.soggetto.uid != 0}">
		<div class="boxOrSpan2">
			<div class="boxOrInline">
				<p>Classificatori Causale</p>
				<ul class="htmlelt">
					<s:iterator var="idx" begin="1" end="%{numeroClassificatoriEP}">
						<s:if test="%{#attr['labelClassificatoreEP' + #idx] != null}">
							<li>
								<s:set var="cep" value="#attr['classificatoreEP' + #idx]" />
								<dfn><s:property value="%{#attr['labelClassificatoreEP' + #idx]}"/></dfn>
								<dl><s:property value="%{#cep.codice + ' - ' + #cep.descrizione}" /></dl>
							</li>
						</s:if>
					</s:iterator>
				</ul>
			</div>
		</div>
	</s:if>
	<div class="clear"></div>
	<br/>
	<h4 class="step-pane">Elenco eventi</h4>
	<table class="table table-hover tab_left">
		<thead>
			<tr>
				<th class="span4">Tipo</th>
				<th>Evento</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="causaleEP.eventi" var="ev" status="sts">
				<tr>
					<s:if test="#sts.first">
						<td rowspan='<s:property value="%{causaleEP.eventi.size()}"/>' ><s:property value="%{tipoEvento.codice + ' - ' + tipoEvento.descrizione}" /></td>
					</s:if>
					<td><s:property value="%{#ev.codice + ' - ' + #ev.descrizione}" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
	<div class="Border_line"></div>
	<h4>Lista conti associati alla causale</h4>
	<table class="table table-hover tab_left" id="tabellaConti">
		<thead>
			<tr>
				<th>Classe</th>
				<th>Codice conto</th>
				<th>Segno Conto</th>
				<th>Utilizzo Conto</th>
				<th>Utilizzo Importo</th>
				<th>Tipo Importo</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="causaleEP.contiTipoOperazione" var="cto">
				<tr>
					<td><s:property value="%{#cto.conto.pianoDeiConti.classePiano.descrizione + ' - ' + #cto.conto.livello}" /></td>
					<td>
						<a data-original-title="Descrizione" data-trigger="hover" rel="popover" data-content="<s:property value="#cto.conto.descrizione"/>" data-html="true"><s:property value="#cto.conto.codice"/></a>
					</td>
					<td><s:property value="#cto.operazioneSegnoConto.descrizione"/></td>
					<td><s:property value="#cto.operazioneUtilizzoConto.descrizione"/></td>
					<td><s:property value="#cto.operazioneUtilizzoImporto.descrizione"/></td>
					<td><s:property value="#cto.operazioneTipoImporto.descrizione"/></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</fieldset>