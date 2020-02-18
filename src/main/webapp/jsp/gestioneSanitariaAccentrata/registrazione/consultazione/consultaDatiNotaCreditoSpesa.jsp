<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="si" %>
<h4 class="step-pane"><s:property value="consultazioneHelper.datiCreazioneModifica" /></h4>
<div class="boxOrSpan2">
	<div class="boxOrInLeft">
		<p>Dati Documento</p>
		<ul class="htmlelt">
			<li>
				<dfn>Data</dfn>
				<dl><s:property value="consultazioneHelper.documento.dataEmissione"/></dl>
			</li>
			<li>
				<dfn>Soggetto</dfn>
				<dl><s:property value="consultazioneHelper.datiSoggetto" /></dl>
			</li>
			<li>
				<dfn>Importi</dfn>
				<dl><s:property value="consultazioneHelper.datiImporti" /></dl>
			</li>
			<li>
				<dfn>Data Scadenza</dfn>
				<dl><s:property value="consultazioneHelper.documento.dataScadenza"/></dl>
			</li>
		</ul>
	</div>

	<div class="boxOrInRight">
		<p>Altri Dati</p>
		<ul class="htmlelt">
			<li>
				<dfn>Descrizione</dfn>
				<dl><s:property value="consultazioneHelper.documento.descrizione"/></dl>
			</li>
			<li>
				<dfn>Data e numero protocollo</dfn>
				<dl><s:property value="consultazioneHelper.dataNumeroProtocollo" /></dl>
			</li>
			<li>
				<dfn>Documenti collegati</dfn>
				<dl><s:property value="consultazioneHelper.totaleImportoDocumentiCollegati"/></dl>
			</li>
		</ul>
	</div>
</div>

<h4>Dati Quote documento collegato (<s:property value="consultazioneHelper.numeroDocumentoOriginale" />)</h4>
<table id="tabellaQuoteConsultazione" class="table table-hover tab_left">
	<thead>
		<tr>
			<th>Numero</th>
			<th>Provvedimento</th>
			<th>Impegno</th>
			<th>Liquidazione</th>
			<th>IVA</th>
			<th>Capitolo</th>
			<th>V liv. PdC Fin</th>
			<th class="tab_Right">Importo</th>
			<th class="tab_Right">Da dedurre</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="consultazioneHelper.listaQuoteDaDedurre" var="q">
			<tr>
				<td>
					<a rel="popover" href="#" data-original-title="Descrizione" data-trigger="hover" data-content="<s:property value="#q.descrizione"/>">
						<s:property value="#q.numero"/>
					</a>
				</td>
				<td>
					<s:if test="%{#q.attoAmministrativo != null}">
						<s:property value="#q.attoAmministrativo.anno"/> / <s:property value="#q.attoAmministrativo.numero"/> -
						<a rel="popover" href="#" data-original-title="Oggetto" data-trigger="hover" data-content="<s:property value="#q.attoAmministrativo.oggetto"/>">
							<s:property value="#q.attoAmministrativo.tipoAtto.codice"/>
						</a>
						<s:if test="%{#q.attoAmministrativo.strutturaAmmContabile != null}">
							- <s:property value="#q.attoAmministrativo.strutturaAmmContabile.codice"/>
						</s:if>
						- Stato: <s:property value="#q.attoAmministrativo.statoOperativo"/>
					</s:if>
				</td>
				<td>
					<s:if test="#q.impegno != null">
						<s:property value="#q.impegno.annoMovimento"/> / <si:plainstringproperty value="#q.impegno.numero"/>
						<s:if test="%{#q.subImpegno != null}">
							- <si:plainstringproperty value="#q.subImpegno.numero"/>
						</s:if>
						<s:if test="%{#q.voceMutuo != null}">
							<a rel="popover" href="#" data-original-title="CIG/CUP" data-trigger="hover" data-content="<s:property value="#q.cig"/> + <s:property value="#q.cup"/>">
								<s:property value="#q.voceMutuo.numeroMutuo"/>
							</a>
						</s:if>
					</s:if>
				</td>
				<td>
					<s:if test="%{#q.liquidazione != null}">
						<s:property value="#q.liquidazione.annoLiquidazione" /> / <si:plainstringproperty value="#q.liquidazione.numeroLiquidazione" />
					</s:if>
				</td>
				<td>
					<s:if test="%{#q.flagRilevanteIVA}">S&Iacute;</s:if><s:else>NO</s:else>
				</td>
				<td>
					<s:if test="%{#q.impegno != null && #q.impegno.capitoloUscitaGestione != null}">
						<s:property value="#q.impegno.capitoloUscitaGestione.annoCapitolo"/>/<s:property value="#q.impegno.capitoloUscitaGestione.numeroCapitolo"/>
					</s:if>
				</td>
				<td>
					<s:if test="%{#q.impegno != null && #q.impegno.capitoloUscitaGestione != null && #q.impegno.capitoloUscitaGestione.elementoPianoDeiConti != null}">
						<s:property value="#q.impegno.capitoloUscitaGestione.elementoPianoDeiConti.codice"/>
					</s:if>
				</td>
				<td class="tab_Right">
					<s:property value="#q.importo"/>
				</td>
				<td class="tab_Right">
					<s:property value="#q.importoDaDedurre"/>
				</td>
			</tr>
		</s:iterator>
	</tbody>
</table>

<s:if test="consultazioneHelper.conMovimentiIvaDocCollegatoValorizzati">
	<div class="step-pane active" >
		<div class="accordion" id="datiIVADocCollegato">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle collapsed" data-parent="#datiIVADocCollegato" href="#" data-target="#collapseDatiIvaDocCollegato">
						Dati Iva documento collegato<span class="icon">&nbsp;</span>
					</a>
				</div>
				<div id="collapseDatiIvaDocCollegato" class="accordion-body collapse" >
					<div class="accordion-inner">
						<h4>Importo non Rilevante IVA: <s:property value="consultazioneHelper.totaleNonRilevanteIvaDocCollegato"/> - Importo Rilevante IVA: <s:property value="consultazioneHelper.totaleRilevanteIvaDocCollegato"/></h4>
						<h4 class="step-pane">Elenco Movimenti Note Credito IVA</h4>
						<table id="tabellaDatiIvaDocCollegato" class="table table-hover tab_left">
							<thead>
								<tr>
									<th>Numero quota</th>
									<th>Registrazione Iva</th>
									<th>Attivit&agrave; Iva</th>
									<th>Aliquota Iva</th>
									<th class="tab_Right">Imponibile</th>
									<th class="tab_Right">Imposta</th>
									<th class="tab_Right">Importo detraibile</th>
									<th class="tab_Right">Importo indetraibile</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="consultazioneHelper.listaMovimentiIvaDocCollegato" var="mi">
									<tr>
										<td><s:property value="#mi.numeroQuota" /></td>
										<td><s:property value="#mi.registrazioneIva" escape="false" /></td>
										<td><s:property value="#mi.attivitaIva" escape="false"/></td>
										<td><s:property value="#mi.aliquotaIva" escape="false"/></td>
										<td class="tab_Right"><s:property value="#mi.imponibile"/></td>
										<td class="tab_Right"><s:property value="#mi.imposta"/></td>
										<td class="tab_Right"><s:property value="#mi.impostaDetraibile"/></td>
										<td class="tab_Right"><s:property value="#mi.impostaIndetraibile"/></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</s:if>

<s:if test="consultazioneHelper.conMovimentiIvaValorizzati">
	<div class="step-pane active" >
		<div class="accordion" id="datiIVA">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle collapsed" data-parent="#datiIVA" href="#" data-target="#collapseDatiIva">
						Dati Iva<span class="icon">&nbsp;</span>
					</a>
				</div>
				<div id="collapseDatiIva" class="accordion-body collapse" >
					<div class="accordion-inner">
						<h4>Importo non Rilevante IVA: <s:property value="consultazioneHelper.totaleNonRilevanteIva"/> - Importo Rilevante IVA: <s:property value="consultazioneHelper.totaleRilevanteIva"/></h4>
						<h4 class="step-pane">Elenco Movimenti Note Credito IVA</h4>
						<table id="tabellaDatiIva" class="table table-hover tab_left">
							<thead>
								<tr>
									<th>Numero quota</th>
									<th>Registrazione Iva</th>
									<th>Attivit&agrave; Iva</th>
									<th>Aliquota Iva</th>
									<th class="tab_Right">Imponibile</th>
									<th class="tab_Right">Imposta</th>
									<th class="tab_Right">Importo detraibile</th>
									<th class="tab_Right">Importo indetraibile</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="consultazioneHelper.listaMovimentiIva" var="mi">
									<tr>
										<td><s:property value="#mi.numeroQuota" /></td>
										<td><s:property value="#mi.registrazioneIva" escape="false" /></td>
										<td><s:property value="#mi.attivitaIva" escape="false"/></td>
										<td><s:property value="#mi.aliquotaIva" escape="false"/></td>
										<td class="tab_Right"><s:property value="#mi.imponibile"/></td>
										<td class="tab_Right"><s:property value="#mi.imposta"/></td>
										<td class="tab_Right"><s:property value="#mi.impostaDetraibile"/></td>
										<td class="tab_Right"><s:property value="#mi.impostaIndetraibile"/></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</s:if>
<%-- /DATI IVA --%>

<%-- DATI COLLEGATI --%>
<div class="step-pane active">
	<div class="accordion" id="datiCOLL">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle collapsed" data-parent="#datiCOLL" href="#" data-target="#collapseDocCollegati">
					Dati Documenti Collegati<span class="icon">&nbsp;</span>
				</a>
			</div>
			<div id="collapseDocCollegati" class="accordion-body collapse" >
				<div class="accordion-inner">
					<table id="tabellaDocumentiCollegati" class="table table-hover tab_left">
						<thead>
							<tr>
								<th>Tipo</th>
								<th>Documento</th>
								<th>Data</th>
								<th>Stato</th>
								<th>Soggetto</th>
								<th class="tab_Right">Importo</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="consultazioneHelper.listaDocumentiCollegati" var="dc">
								<tr>
									<td><s:property value="#dc.tipo"/></td>
									<td><s:property value="#dc.documento"/></td>
									<td><s:property value="#dc.data"/></td>
									<td><s:property value="#dc.statoOperativoDocumentoDesc"/></td>
									<td><s:property value="#dc.soggetto"/></td>
									<td class="tab_Right"><s:property value="#dc.importo"/></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<%-- /DATI COLLEGATI --%>
