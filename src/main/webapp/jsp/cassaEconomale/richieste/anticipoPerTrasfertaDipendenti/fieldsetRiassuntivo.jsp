<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/siac-tags" prefix="si" %>

<fieldset class="form-horizontal">
	<h4><s:property value="stringaRiepilogoRichiestaEconomale"/></h4>
	<div class="boxOrSpan2">
		<div class="boxOrInLeft">
			<p>Dati richiedente</p>
			<ul class="htmlelt">
				<li>
					<dfn>Matricola</dfn>
					<dl><s:property value="datiMatricola" />&nbsp;</dl>
				</li>
				<li>
					<dfn>Unit&agrave; organizzativa</dfn>
					<dl><s:property value="richiestaEconomale.strutturaDiAppartenenza"/>&nbsp;</dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight">
			<p>Dati richiesta</p>
			<ul class="htmlelt">
				<li>
					<dfn>Motivo trasferta</dfn>
					<dl><s:property value="richiestaEconomale.datiTrasfertaMissione.motivo"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Estero</dfn>
					<dl><s:if test="richiestaEconomale.datiTrasfertaMissione.flagEstero">S&igrave;</s:if><s:else>No</s:else>&nbsp;</dl>
				</li>
				<li>
					<dfn>Luogo trasferta</dfn>
					<dl><s:property value="richiestaEconomale.datiTrasfertaMissione.luogo"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Mezzo di trasporto </dfn>
					<dl>
						<s:iterator value="richiestaEconomale.datiTrasfertaMissione.mezziDiTrasporto" var="mdt" status="sts">
							<s:property value="%{#mdt.codice + ' - ' + #mdt.descrizione}" /><s:if test="%{!#sts.last}">,&nbsp;</s:if>
						</s:iterator>
					</dl>
				</li>
				<li>
					<dfn>Data inizio</dfn>
					<dl><s:property value="richiestaEconomale.datiTrasfertaMissione.dataInizio"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Data fine</dfn>
					<dl><s:property value="richiestaEconomale.datiTrasfertaMissione.dataFine"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Delegato incasso</dfn>
					<dl><s:property value="richiestaEconomale.delegatoAllIncasso"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Note</dfn>
					<dl><s:property value="richiestaEconomale.note"/>&nbsp;</dl>
				</li>
			</ul>
		</div>
	</div>
	<div class="boxOrSpan2">
		<div class="boxOrInLeft">
			<p>Impegno</p>
			<ul class="htmlelt">
				<li>
					<dfn>Anno</dfn>
					<dl><s:property value="richiestaEconomale.impegno.annoMovimento"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Numero</dfn>
					<dl><si:plainstringproperty value="richiestaEconomale.impegno.numero"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>SubImpegno</dfn>
					<dl><si:plainstringproperty value="richiestaEconomale.subImpegno.numero"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Descrizione</dfn>
					<dl><s:property value="richiestaEconomale.impegno.descrizione"/>&nbsp;</dl>
				</li>
			</ul>
		</div>
		<div class="boxOrInRight">
			<p>Altri Dati</p>
			<ul class="htmlelt">
				<li>
					<dfn>Capitolo</dfn>
					<dl><s:property value="descrizioneCapitoloMovimento"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Data operazione</dfn>
					<dl><s:property value="richiestaEconomale.movimento.dataMovimento"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Modalit&agrave; di pagamento</dfn>
					<dl><s:property value="stringaRiepilogoModalitaDiPagamento"/>&nbsp;</dl>
				</li>
				<li>
					<dfn>Rendiconto n.</dfn>
					<dl><s:property value="richiestaEconomale.numeroRendicontoStampato"/><span> - </span><b>del</b> <s:property value="richiestaEconomale.dataRendicontoStampato"/></dl>
				</li>
			</ul>
		</div>
	</div>
	<h4>Spese</h4>
	<table class="table table-hover tab_left" id="tabellaGiustificativi">
		<thead>
			<tr>
				<th class="span8">Tipo</th>
				<th class="tab_Right span5">Importo</th>
				<th class="tab_Right span5">Importo spettante</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="richiestaEconomale.giustificativi" var="giu">
				<tr>
					<td><s:property value="#giu.tipoGiustificativo.codice" /> - <s:property value="#giu.tipoGiustificativo.descrizione" /></td>
					<td class="tab_Right"><s:property value="#giu.importoGiustificativo" /></td>
					<td class="tab_Right"><s:property value="#giu.importoSpettanteGiustificativo" /></td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<th colspan="1">Totale</th>
				<th class="tab_Right"><s:property value="totaleImportiGiustificativi"/></th>
				<th class="tab_Right"><s:property value="totaleImportiSpettantiGiustificativi"/></th>
			</tr>
		</tfoot>
	</table>
</fieldset>