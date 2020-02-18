<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
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
				<dfn>Note credito</dfn>
				<dl><s:property value="consultazioneHelper.totaleNote"/></dl>
			</li>
			<li>
				<dfn>Documenti collegati</dfn>
				<dl><s:property value="consultazioneHelper.totaleImportoDocumentiCollegati"/></dl>
			</li>
		</ul>
	</div>
</div>

<h4>Dati Quote</h4>
<table id="tabellaQuoteConsultazione" class="table table-hover tab_left">
	<thead>
		<tr>
			<th>Numero</th>
			<th>Provvedimento</th>
			<th>Accertamento</th>
			<th>IVA</th>
			<th>Capitolo</th>
			<th>V liv. PdC Fin</th>
			<th class="tab_Right">Importo</th>
			<th class="tab_Right">Da dedurre</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="consultazioneHelper.listaQuote" var="q">
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
					<s:if test="#q.accertamento != null">
						<s:property value="#q.accertamento.annoMovimento"/> / <si:plainstringproperty value="#q.accertamento.numero"/>
						<s:if test="%{#q.subAccertamento != null}">
							- <si:plainstringproperty value="#q.subAccertamento.numero"/>
						</s:if>
					</s:if>
				</td>
				<td>
					<s:if test="%{#q.flagRilevanteIVA}">S&Iacute;</s:if><s:else>NO</s:else>
				</td>
				<td>
					<s:if test="%{#q.accertamento != null && #q.accertamento.capitoloEntrataGestione != null}">
						<s:property value="#q.accertamento.capitoloEntrataGestione.annoCapitolo"/>/<s:property value="#q.accertamento.capitoloEntrataGestione.numeroCapitolo"/>
					</s:if>
				</td>
				<td>
					<s:if test="%{#q.accertamento != null && #q.accertamento.capitoloEntrataGestione != null && #q.accertamento.capitoloEntrataGestione.elementoPianoDeiConti != null}">
						<s:property value="#q.accertamento.capitoloEntrataGestione.elementoPianoDeiConti.codice"/>
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

<%-- DATI IVA --%>
<div class="step-pane active" >
	<div class="accordion" id="datiIVA">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle collapsed" data-parent="#datiIVA" href="#" data-target="#collapseDatiIva">
					Dati Iva<span class="icon">&nbsp;</span></a>
			</div>
			<div id="collapseDatiIva" class="accordion-body collapse">
				<div class="accordion-inner">
					<h4>Importo non Rilevante IVA:<s:property value="consultazioneHelper.totaleNonRilevanteIva"/> - Importo Rilevante IVA:<s:property value="consultazioneHelper.totaleRilevanteIva"/></h4>
					<h4 class="step-pane">Elenco Movimenti IVA</h4>
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
									<td>
										<s:if test="%{#mi.subdocumentoIva != null && #mi.subdocumentoIva.subdocumento != null}">
											<s:property value="#mi.subdocumentoIva.subdocumento.numero"/>
										</s:if>
									</td>
									<td>
										<s:if test="%{#mi.subdocumentoIva != null && #mi.subdocumentoIva.subdocumento != null}">
											<s:property value="#mi.subdocumentoIva.subdocumento.numeroRegistrazioneIVA"/> / <s:if test="%{#mi.subdocumentoIva.flagRegistrazioneIva}">S&Igrave;</s:if><s:else>NO</s:else>
										</s:if>
									</td>
									<td>
										<s:if test="%{mi.subdocumentoIva != null && #mi.subdocumentoIva.attivitaIva != null}">
											<s:property value="#mi.subdocumentoIva.attivitaIva.codice"/> - <s:property value="#mi.subdocumentoIva.attivitaIva.descrizione"/>
										</s:if>
									</td>
									<td>
										<s:if test="%{#mi.aliquota != null && #mi.aliquota.aliquotaIva != null}">
											<a rel="popover" href="#" data-original-title="Percentuale aliquota" data-trigger="hover" data-content="<s:property value="#mi.aliquota.aliquotaIva.percentualeAliquota"/>">
												<s:property value="#mi.aliquota.aliquotaIva.codice"/> - <s:property value="#mi.aliquota.aliquotaIva.descrizione"/>
											</a>
										</s:if>
									</td>
									<td class="tab_Right">
										<s:if test="%{#mi.aliquota != null}">
											<s:property value="#mi.aliquota.imponibile"/>
										</s:if>
									</td>
									<td class="tab_Right">
										<s:if test="%{#mi.aliquota != null}">
											<s:property value="#mi.aliquota.imposta"/>
										</s:if>
									</td>
									<td class="tab_Right">
										<s:if test="%{#mi.aliquota != null}">
											<s:property value="#mi.aliquota.impostaDetraibile"/>
										</s:if>
									</td>
									<td class="tab_Right">
										<s:if test="%{#mi.aliquota != null}">
											<s:property value="#mi.aliquota.impostaIndetraibile"/>
										</s:if>
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
			</div>

		</div>
	</div>
</div>
<%-- /DATI IVA --%>

<%-- DATI COLLEGATI --%>
<div class="step-pane active">
	<div class="accordion" id="datiCOLL">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a class="accordion-toggle collapsed" data-parent="#datiCOLL" href="#" data-target="#collapseDocCollegati">
					Dati Documenti Collegati<span class="icon">&nbsp;</span></a>
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
